package com.hcg.kafka.Controller;

import com.hcg.kafka.kafkaConfig.Consume;
import com.hcg.kafka.util.SendMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/kafka")
public class CollectController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Autowired
    private SendMessages sendMessages;

    @RequestMapping(value = "/send")
    public String sendKafka() {
        try {
//            String message = request.getParameter("message");
            for (int i=0; i<20;i++)
            {
                sendMessages.sendMessage("test1","这是在生产消息"+i);
            }
/*            logger.info("kafka的消息={}",message);
            kafkaTemplate.send("test1", "key", message);*/
            logger.info("发送kafka成功.");
            return "发送kafka成功";
        } catch (Exception e) {
            //这里需要对发送失败的消息重新进行推送
            logger.error("发送kafka失败", e);
            return "发送kafka失败";
        }
    }

    @RequestMapping(value = "/consume")
    public String consume()
    {
       /* try {
            Consume consume = new Consume();
            //启动kafka消费
            consume.start();
            //运行一段时间后停止kafka消息接收
            Thread.sleep(3600 * 1000); //运行一个小时
            //关闭线程池，实际部署在生产上的应用，不要直接kill -9 强制关闭，请使用kill ，给应用关闭前做一些清理操作
            consume.destroy();
        }catch (Exception e)
        {
            return e.toString();
        }
       return "消费成功啦";*/
       return null;
    }

}