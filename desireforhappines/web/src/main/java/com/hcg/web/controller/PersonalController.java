package com.hcg.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/personal")
public class PersonalController {

    @RequestMapping("/")
    public String getIndex()
    {
        System.out.println("成功进入首页");
        return "/page/index.html";
    }

}
