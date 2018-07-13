package com.hcg.commonservice.webService.impl;

import com.hcg.commondal.mapper.UserMapper;
import com.hcg.commondal.model.UserInfo;
import com.hcg.commonservice.webService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceimpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public int insertOne(UserInfo userInfo) {

        return userMapper.insertOne(userInfo);
//        return 0;
    }

    @Override
    public int updateOne(UserInfo userInfo) {

        return userMapper.updateOne(userInfo);
//        return 0;
    }

    @Override
    public UserInfo selectOne(UserInfo userInfo) {

        return userMapper.onlyOne(userInfo);
//        return null;
    }

    @Override
    public int dropOne(UserInfo userInfo) {

        return userMapper.dropOne(userInfo);
//        return 0;
    }

    @Override
    public List<UserInfo> selectWithCondition(UserInfo userInfo) {
        List <UserInfo> list = userMapper.selectWithCondition(userInfo);
        return list;
    }
}
