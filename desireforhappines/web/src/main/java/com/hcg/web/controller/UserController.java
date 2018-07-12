package com.hcg.web.controller;

import com.hcg.commondal.mapper.UserMapper;
import com.hcg.commondal.model.UserInfo;
import com.hcg.commonservice.webService.UserService;
import com.hcg.web.Vo.PageResp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping("/regesiter")
    @ResponseBody
    public PageResp regesiter(@RequestBody UserInfo userInfo){
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
            }
            int num = userService.insertOne(userInfo);
            if (num>0)
            {
                resp.setMsg("注册成功");
                resp.setErrorCode(0);
            }
        }
        return resp;
    }

    @RequestMapping("/login")
    @ResponseBody
    public PageResp getLogin(@RequestBody UserInfo userInfo)
    {
        PageResp resp = new PageResp();
        resp.setErrorCode(1);
        if (StringUtils.isEmpty(userInfo.getUsername())|| StringUtils.isEmpty(userInfo.getPassword()))
        {
            resp.setErrorCode(1);
            resp.setMsg("账号或密码不能为空");
            return resp;
        }
        UserInfo user= userService.selectOne(userInfo);
        if (user != null)
        {
            resp.setErrorCode(0);
            resp.setMsg("登录成功");
        }else
        {
            resp.setMsg("账号密码错");
        }
        return resp;
    }

}
