package com.hcg.web.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Myfilter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("进入拦截器了");
        Cookie [] cookies = request.getCookies();
        for (Cookie cookie :cookies)
        {
            if ("username".equals(cookie.getName()))
            {
                if (cookie.getMaxAge()<5)
                {
                    cookie.setMaxAge(1800);
                }
                System.out.println("您已成功通过拦截器了");
                return true;
            }
        }
        response.sendRedirect("/user");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
