package com.hcg.commonservice.webService.impl;

import com.hcg.commondal.mapper.UserMapper;
import com.hcg.commondal.model.UserInfo;
import com.hcg.commonservice.webService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceimpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public int insertOne(UserInfo userInfo) {

        return userMapper.insertOne(userInfo);
    }

    @Override
    public int updateOne(UserInfo userInfo) {

        return userMapper.updateOne(userInfo);
    }

    @Override
    public UserInfo selectOne(UserInfo userInfo) {

        return userMapper.onlyOne(userInfo);
    }

    @Override
    public int dropOne(UserInfo userInfo) {

        return userMapper.dropOne(userInfo);
    }
}
