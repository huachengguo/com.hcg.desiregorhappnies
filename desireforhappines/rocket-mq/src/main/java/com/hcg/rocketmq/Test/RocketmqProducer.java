package com.hcg.rocketmq.Test;

import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RocketmqProducer {

    @Autowired
    DefaultMQProducer defaultProducer;

    @Autowired
    TransactionMQProducer transactionProducer;

    public void sendMsg(String topic,String tag,String keys,String body)
    {
        Message msg = new Message(topic, tag, keys,body.getBytes());
        try {
            defaultProducer.send(msg, new SendCallback() {

                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("消息已经发送成功了");
                    System.out.println(sendResult);
                }

                @Override
                public void onException(Throwable e) {
                    System.out.println("消息发送失败");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public String sendTransMsg(String topic,String tag,String keys,String body)
    {
        SendResult sendResult = null;
        try {
            Message msg = new Message(topic, tag, keys,body.getBytes());

            // 发送事务消息，LocalTransactionExecute的executeLocalTransactionBranch方法中执行本地逻辑
            sendResult = transactionProducer.sendMessageInTransaction(msg, (Message msg1, Object arg) -> {
                int value = 1;

                // TODO 执行本地事务，改变value的值
                // ===================================================
                System.out.println("执行本地事务。。。完成");
                if (arg instanceof Integer) {
                    value = (Integer) arg;
                }
                // ===================================================

                if (value == 0) {
                    throw new RuntimeException("Could not find db");
                } else if ((value % 5) == 0) {
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                } else if ((value % 4) == 0) {
                    return LocalTransactionState.COMMIT_MESSAGE;
                }
                return LocalTransactionState.ROLLBACK_MESSAGE;
            }, 4);
            System.out.println(sendResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendResult.toString();

    }

    public void sendOrderMsg(String topic,String tag,String keys,String body)
    {
        Message msg = new Message(topic, tag, keys,body.getBytes());
        try {
            defaultProducer.send(msg, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    System.out.println("MessageQueue" + arg);
                    int index = ((Integer) arg) % mqs.size();
                    return mqs.get(index);
                }
            },0);// i==arg
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
