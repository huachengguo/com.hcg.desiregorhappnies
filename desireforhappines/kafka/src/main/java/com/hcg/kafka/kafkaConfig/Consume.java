package com.hcg.kafka.kafkaConfig;

import com.hcg.kafka.util.Cache;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class Consume {
    /*@Autowired
    private KafkaConsumerConfig kafkaConsumerConfig;*/
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


   @KafkaListener(topics = {"${kafka.consumer.topic}"})
    public void listen(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());

        if (kafkaMessage.isPresent()) {
            long offset = record.offset();

            Object message = kafkaMessage.get();

            logger.info("----------------- record =" + record);
            logger.info("------------------ message =" + message);
           /* ConsumerFactory<String, String> stringStringConsumerFactory = kafkaConsumerConfig.consumerFactory();
            Consumer<String, String> consumer = stringStringConsumerFactory.createConsumer();
            consumer.commitSync();*/

        }


    }
  /* private static Logger logger = LoggerFactory.getLogger(Consume.class);
    *//**
     * 用于接收kafka 消息的线程池
     *//*
    private ExecutorService kafkaConsumerExecutorService;
    *//**
     * 具体处理kafka消息的线程池
     *//*
    private ExecutorService workerExecutorService;
    *//**
     * 用于阻塞往线程池中提交新的任务，直到有可用的线程
     *//*
    private Semaphore semaphore;
    private int kafkaConsumerExecutorNumber = 10;
    private int workerExecutorNumber = 50;

    public Consume(){
        //线程池的大小可以根据自己需要来调节，这里简单点就使用了固定线程池
        //用于接收kafka 消息的线程池
        kafkaConsumerExecutorService = Executors.newFixedThreadPool(kafkaConsumerExecutorNumber);
        //具体处理kafka消息的线程池
        workerExecutorService = Executors.newFixedThreadPool(workerExecutorNumber);
        //用于阻塞往线程池中提交新的任务，直到有可用的线程
        semaphore = new Semaphore(workerExecutorNumber);
    }


    public void start(){
        for(int i = 0; i < kafkaConsumerExecutorNumber; i++){
            kafkaConsumerExecutorService.submit(new TopicPartitionThread(workerExecutorService, semaphore));
        }
    }

    public void destroy() throws Exception {
        //停止kafka 消费线程
        Cache.getInstance().setKafkaThreadStatus(false);
        //关闭线程池
        kafkaConsumerExecutorService.shutdown();
        while(!kafkaConsumerExecutorService.awaitTermination(1, TimeUnit.SECONDS)){
            logger.info("await kafkaConsumerExecutorService stop...");
        }
        logger.info("kafkaConsumerExecutorService stoped.");
        workerExecutorService.shutdown();
        while(!workerExecutorService.awaitTermination(1, TimeUnit.SECONDS)){
            logger.info("await workerExecutorService stop...");
        }
        logger.info("workerExecutorService stoped.");
    }*/

}
