package com.hcg.commondal.mapper;

import com.hcg.commondal.model.UserInfo;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    List<UserInfo> selectAll();

    List<UserInfo> selectPage(Map map);

    UserInfo onlyOne(UserInfo userInfo);

    int dropOne(UserInfo userInfo);

    int dropMore(int [] list);

    int updateOne(UserInfo userInfo);

    int insertOne(UserInfo userInfo);

    int insertBatch(List <UserInfo> list);

}
