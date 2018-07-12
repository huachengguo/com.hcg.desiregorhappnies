package com.hcg.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/index")
public class IndexController {

    @RequestMapping()
    public String getIndex()
    {
        System.out.println("成功进入首页");
        return "/login";
    }

}
