package com.hcg.web.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class FirstPageSetting extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
         registry.addViewController("/").setViewName("forward:/login.html");
         registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
         System.out.println("自定义设置成功");
        super.addViewControllers(registry);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Myfilter()).addPathPatterns("/personal/**").excludePathPatterns("/","/user/**");
        super.addInterceptors(registry);
    }

  /*  @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
            System.out.println("资源拦截器");
            registry.addResourceHandler("/page/**").addResourceLocations("classpath:/page/");
        super.addResourceHandlers(registry);
    }*/
}
