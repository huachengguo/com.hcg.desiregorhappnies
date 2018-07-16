package com.hcg.web.controller;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class testConsumerHandler {
    @KafkaListener(topics = {"test01"})
    public void processMessage(ConsumerRecord<?, ?> record) {
        System.out.println(record.toString());
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            System.out.println("this is the testTopic send message:" + message);
        }
    }
}
