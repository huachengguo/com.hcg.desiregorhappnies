package com.hcg.web.controller;

import com.hcg.commondal.mapper.UserMapper;
import com.hcg.commondal.model.UserInfo;
import com.hcg.commonservice.webService.UserService;
import com.hcg.web.Vo.PageResp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/regesiter")
    @ResponseBody
    public PageResp regesiter(@RequestBody UserInfo userInfo ){
        PageResp resp = new PageResp();
        resp.setErrorCode(1);
        if (userInfo != null)
        {
            if (StringUtils.isEmpty(userInfo.getUsername()) || StringUtils.isEmpty(userInfo.getPassword()))
            {
                resp.setMsg("账号或密码不能为空");
            }
            UserInfo param = new UserInfo();
            param.setUsername(userInfo.getUsername());
            List <UserInfo> list = userService.selectWithCondition(param);
            if (list.size()>0)
            {
                resp.setMsg("账号已存在");
                return resp;
            }
            int num = userService.insertOne(userInfo);
            if (num>0)
            {
                resp.setMsg("注册成功,快去登录吧");
                resp.setErrorCode(0);
            }
        }
        return resp;
    }

    @RequestMapping("/login")
    @ResponseBody
    public PageResp getLogin(HttpServletRequest request, HttpServletResponse response)
    {
        PageResp resp = new PageResp();
        resp.setErrorCode(1);
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (StringUtils.isEmpty(username)|| StringUtils.isEmpty(password))
        {
            resp.setErrorCode(1);
            resp.setMsg("账号或密码不能为空");
            return resp;
        }
        UserInfo param = new UserInfo();
        param.setUsername(username);
        param.setPassword(password);
        UserInfo user= userService.selectOne(param);
        if (user != null)
        {
            Cookie cookies = new Cookie("username",username);
            cookies.setMaxAge(1800);
            cookies.setPath("/");
            response.addCookie(cookies);
            HttpSession session = request.getSession();
            session.setAttribute("username",username);
            session.setMaxInactiveInterval(300);
            resp.setErrorCode(0);
            resp.setMsg("登录成功");
        }else
        {
            resp.setMsg("账号密码错");
        }
        return resp;
    }
    @RequestMapping()
    public String index()
    {
        return "/login.html";
    }

}
