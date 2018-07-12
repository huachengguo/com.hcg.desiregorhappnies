package com.hcg.web.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class FirstPageSetting extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
         registry.addViewController("/").setViewName("forward:/page/login.html");
         registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
         System.out.println("自定义设置成功");
        super.addViewControllers(registry);
    }
}