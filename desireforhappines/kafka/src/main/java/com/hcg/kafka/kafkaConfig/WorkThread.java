package com.hcg.kafka.kafkaConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;

public class WorkThread implements Callable<String> {

    private static Logger logger = LoggerFactory.getLogger(WorkThread.class);
    /**
     * 记录解析失败的日志或者发送到kafka集群失败的日志
     */
    private String topic;
    private String message;
    private Semaphore semaphore;

    public WorkThread(String topic, String message, Semaphore semaphore){
        this.topic = topic;
        this.message = message;
        this.semaphore = semaphore;
    }

    @Override
    public String call() throws Exception {
        try {
            //deal your business
            //......

            //这里只打印下，如果在实际业务中处理失败，可能是代码bug或者系统不稳定等，先将消息记录到日志中，后续可以处理,不影响主流程的继续运行
            logger.info("topic is {}, message is {}", topic, message);
        } catch (Exception e) {
            logger.error("ParseKafkaLogJob run error. ", e);
        } finally{
            semaphore.release();
        }
        return "done";
    }

}
