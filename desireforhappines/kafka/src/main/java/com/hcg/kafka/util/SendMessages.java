package com.hcg.kafka.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

public class SendMessages {
    @Autowired
    private static KafkaTemplate kafkaTemplate;


    public static void sendMessage(String topic,String message)
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
