package com.hcg.rocketmq.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rocket")
public class RocketmqController {
    @Autowired
    RocketmqProducer rocketmqProducer;
    @RequestMapping()
    public void sendmsg(){
        for (int i=0;i<30;i++)
        {
            String body = "这是roctmq发送出来的信息-------->"+i;
            rocketmqProducer.sendMsg("test","hcg",i+"",body);
        }
    }
}
