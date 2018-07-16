package com.hcg.kafka.Consume;

import com.hcg.kafka.kafkaConfig.KafkaConsumerConfig;
import com.hcg.kafka.kafkaConfig.WorkThread;
import com.hcg.kafka.util.Cache;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

//@PropertySource({"classpath:application.properties"})
public class TopicPartitionThread extends Thread {

    /*@Value("${kafka.consumer.servers}")
    private String servers;
    @Value("${kafka.consumer.session.timeout}")
    private String sessionTimeout;

    @Value("${kafka.consumer.group.id}")
    private String groupId;*/

    private static Logger logger = LoggerFactory.getLogger(TopicPartitionThread.class);

    private ExecutorService workerExecutorService;

    private Semaphore semaphore;

    private Map<TopicPartition, OffsetAndMetadata> offsetsMap = new HashMap<>();

    private List<Future<String>> taskList = new ArrayList<>();

    public TopicPartitionThread(ExecutorService workerExecutorService, Semaphore semaphore){
        this.workerExecutorService = workerExecutorService;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        //启动kafka消费
        Properties props = new Properties();
        props.put("bootstrap.servers", "107.0.0.1:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "false");
        props.put("session.timeout.ms", "30000");
        props.put("max.poll.records", 100); //每次poll最多获取100条数据
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Arrays.asList("test", "test001"),
                new ConsumerRebalanceListener(){

                    @Override
                    public void onPartitionsRevoked(
                            Collection<TopicPartition> partitions) {
                        logger.info("threadId = {}, onPartitionsRevoked.", Thread.currentThread().getId());
                        consumer.commitSync(offsetsMap);
                    }

                    @Override
                    public void onPartitionsAssigned(
                            Collection<TopicPartition> partitions) {
                        logger.info("threadId = {}, onPartitionsAssigned.", Thread.currentThread().getId());
                        offsetsMap.clear();
                        //清空taskList列表
                        taskList.clear();
                    }});

        //接收kafka消息
        while (Cache.getInstance().isKafkaThreadStatus()) {
            try {
                //使用100ms作为获取超时时间
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (final ConsumerRecord<String, String> record : records) {
                    semaphore.acquire();
                    //记录当前 TopicPartition和OffsetAndMetadata
                    TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
                    OffsetAndMetadata offset = new OffsetAndMetadata(record.offset());
                    offsetsMap.put(topicPartition, offset);

                    //提交任务到线程池处理
                    taskList.add(workerExecutorService.submit(new WorkThread(record.topic(), record.value(), semaphore)));
                }

                //判断kafka消息是否处理完成
                for(Future<String> task : taskList){
                    //阻塞，直到消息处理完成
                    task.get();
                }

                //同步向kafka集群中提交offset
                consumer.commitSync();
            } catch (Exception e) {
                logger.error("TopicPartitionThread run error.", e);
            } finally{
                //清空taskList列表
                taskList.clear();
            }
        }

        //关闭comsumer连接
        consumer.close();
    }


}
