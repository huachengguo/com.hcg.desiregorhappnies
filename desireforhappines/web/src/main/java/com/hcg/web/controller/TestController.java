package com.hcg.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
        @Autowired
        KafkaTemplate kafkaTemplate;

        @RequestMapping("/query/queryAllItem")
        public String queryAllItem(Model model) {
            kafkaTemplate.send("test01", "bootc", "bootcwnao");
            return "item/itemList";
        }
}
