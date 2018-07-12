package com.hcg.commonservice.webService;

import com.hcg.commondal.model.UserInfo;

import java.util.List;

public interface UserService {

    int insertOne(UserInfo userInfo);

    int updateOne(UserInfo userInfo);

    UserInfo selectOne(UserInfo userInfo);

    int dropOne(UserInfo userInfo);

    List <UserInfo> selectWithCondition(UserInfo userInfo);

}
