package com.hcg.rocketmq.Test;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.hcg.rocketmq.Test.RocketmqProperties.PREFIX;

@Configuration
@EnableConfigurationProperties(RocketmqProperties.class)
@ConditionalOnProperty(prefix = PREFIX, value = "namesrvAddr")
public class RocketmqAutoConfiguration {

    @Autowired
    private RocketmqProperties properties;

    @Value("${rocketmq.producerGroupName}")
    private String producerGroupName;

    @Value("${rocketmq.consumerGroupName}")
    private String consumerGroupName;

    @Autowired
    private ApplicationEventPublisher publisher;
    /**
     * 初始化向rocketmq发送普通消息的生产者
     */
    @Bean
    @ConditionalOnProperty(prefix = PREFIX, value = "producerInstanceName")
    public DefaultMQProducer defaultProducer() throws MQClientException {
        /**
         * 一个应用创建一个Producer，由应用来维护此对象，可以设置为全局对象或者单例<br>
         * 注意：ProducerGroupName需要由应用来保证唯一<br>
         * ProducerGroup这个概念发送普通的消息时，作用不大，但是发送分布式事务消息时，比较关键，
         * 因为服务器会回查这个Group下的任意一个Producer
         */
        DefaultMQProducer producer = new DefaultMQProducer(producerGroupName);
        producer.setNamesrvAddr(properties.getNamesrvAddr());
        producer.setInstanceName(properties.getProducerInstanceName());
        producer.setVipChannelEnabled(false);

        /**
         * Producer对象在使用之前必须要调用start初始化，初始化一次即可<br>
         * 注意：切记不可以在每次发送消息时，都调用start方法
         */
        producer.start();
        System.out.println("RocketMq defaultProducer Started.");
        return producer;
    }

    /**
     * 初始化向rocketmq发送事务消息的生产者
     */
    @Bean
    @ConditionalOnProperty(prefix = PREFIX, value = "producerTranInstanceName")
    public TransactionMQProducer transactionProducer() throws MQClientException{
        /**
         * 一个应用创建一个Producer，由应用来维护此对象，可以设置为全局对象或者单例<br>
         * 注意：ProducerGroupName需要由应用来保证唯一<br>
         * ProducerGroup这个概念发送普通的消息时，作用不大，但是发送分布式事务消息时，比较关键，
         * 因为服务器会回查这个Group下的任意一个Producer
         */
        TransactionMQProducer producer = new TransactionMQProducer("TransactionProducerGroupName");
        producer.setNamesrvAddr(properties.getNamesrvAddr());
        producer.setInstanceName(properties.getProducerTranInstanceName());

        // 事务回查最小并发数
        producer.setCheckThreadPoolMinSize(2);
        // 事务回查最大并发数
        producer.setCheckThreadPoolMaxSize(2);
        // 队列数
        producer.setCheckRequestHoldMax(2000);
        //设置发送失败的重试次数为3
        producer.setRetryTimesWhenSendFailed(3);


        /**
         * Producer对象在使用之前必须要调用start初始化，初始化一次即可<br>
         * 注意：切记不可以在每次发送消息时，都调用start方法
         */
        producer.start();

        System.out.println("RocketMq TransactionMQProducer Started.");
        return producer;
    }
    /**
     * 初始化rocketmq消息监听方式的消费者
     */
    @Bean
    @ConditionalOnProperty(prefix = PREFIX, value = "consumerInstanceName")
    public DefaultMQPushConsumer pushConsumer() throws MQClientException{
        /**
         * 一个应用创建一个Consumer，由应用来维护此对象，可以设置为全局对象或者单例<br>
         * 注意：ConsumerGroupName需要由应用来保证唯一
         */
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroupName);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setNamesrvAddr(properties.getNamesrvAddr());
        consumer.setInstanceName(properties.getConsumerInstanceName());
        consumer.setConsumeMessageBatchMaxSize(5);//设置批量消费，以提升消费吞吐量，默认是1


        /**
         * 订阅指定topic下tags
         */
        List<String> subscribeList = properties.getSubscribe();
        for (String sunscribe : subscribeList) {
            consumer.subscribe(sunscribe.split(":")[0], sunscribe.split(":")[1]);
        }

       /* consumer.registerMessageListener((List<MessageExt> msgs, ConsumeConcurrentlyContext context) -> {

                for (MessageExt msg : msgs) {

                    System.out.println("messageExt: " + msg);//输出消息内容
                    try {
                        String messageBody = new String(msg.getBody(), RemotingHelper.DEFAULT_CHARSET);
                        System.out.println("消费响应：msgId : " + msg.getMsgId() + ",  msgBody : " + messageBody);//输出消息内容
                        this.publisher.publishEvent(new RocketmqEvent(msg,consumer));
                        System.out.println(Thread.currentThread().getName() + " Receive New Messages: " + msgs.size());
                        System.out.println("消息到达事件已经发布成功！");
                    } catch (Exception e) {
                        e.printStackTrace();
                        if(msg.getReconsumeTimes()<=3){//重复消费3次
                            //TODO 进行日志记录
                            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                        } else {
                            //TODO 消息消费失败，进行日志记录
                            System.out.println("消息消费失败了");
                        }
                    }
                }
            //如果没有return success，consumer会重复消费此信息，直到success。
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });*/
        consumer.registerMessageListener(new MessageListenerConcurrently() {



            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                //*System.out.printf(Thread.currentThread().getName() + " Receive New Messages: " + msgs + "%n");
//                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                for (MessageExt msg : msgs) {
                    System.out.println("messageExt: " + msg);//输出消息内容
                    try {
                        String messageBody = new String(msg.getBody(), RemotingHelper.DEFAULT_CHARSET);
                        System.out.println("消费响应：msgId : " + msg.getMsgId() + ",  msgBody : " + messageBody);//输出消息内容
//                        this.publisher.publishEvent(new RocketmqEvent(msg,consumer));
                        publisher.publishEvent(new RocketmqEvent(msg,consumer));
                        System.out.println(Thread.currentThread().getName() + " Receive New Messages: " + msgs.size());
                        System.out.println("消息到达事件已经发布成功！");
                    } catch (Exception e) {
                        e.printStackTrace();
                        if(msg.getReconsumeTimes()<=3){//重复消费3次（主动请求重复消费）
                            //TODO 进行日志记录
                            System.out.println("稍后再试");
                            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                        } else {
                            //TODO 消息消费失败，进行日志记录
                            System.out.println("消息消费失败了");
                        }
                    }

                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);//延迟5秒再启动，主要是等待spring事件监听相关程序初始化完成，否则，回出现对RocketMQ的消息进行消费后立即发布消息到达的事件，然而此事件的监听程序还未初始化，从而造成消息的丢失
                    /**
                     * Consumer对象在使用之前必须要调用start初始化，初始化一次即可<br>
                     */
                    try {
                        consumer.start();
                    } catch (Exception e) {
                        System.out.println("RocketMq pushConsumer Start failure!!!.");
                        e.printStackTrace();
                    }

                    System.out.println("RocketMq pushConsumer Started.");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();

        return consumer;
    }

}
