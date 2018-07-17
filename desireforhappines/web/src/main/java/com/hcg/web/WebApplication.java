package com.hcg.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.hcg.commondal","com.hcg.commonservice","com.hcg.web"})
@MapperScan("com.hcg.commondal.mapper")
public class WebApplication {

    public static void main(String[] args) {
        System.out.println("启动成功!");
        SpringApplication.run(WebApplication.class, args);
        }

}
