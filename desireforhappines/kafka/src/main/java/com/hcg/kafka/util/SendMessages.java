package com.hcg.kafka.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SendMessages {
    @Autowired
    KafkaTemplate kafkaTemplate;


    public void sendMessage(String topic,String message)
    {
        try
        {
            kafkaTemplate.send(topic, "key", message);
        }catch (Exception e)
        {
            System.out.println("消息发送失败");
        }

    }
}
