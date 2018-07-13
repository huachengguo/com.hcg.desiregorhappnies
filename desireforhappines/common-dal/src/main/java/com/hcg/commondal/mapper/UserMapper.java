package com.hcg.commondal.mapper;

import com.hcg.commondal.model.UserInfo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserMapper {

    List<UserInfo> selectAll();

    List<UserInfo> selectPage(Map map);

    List<UserInfo> selectWithCondition(UserInfo userInfo);

    UserInfo onlyOne(UserInfo userInfo);

    int dropOne(UserInfo userInfo);

    int dropMore(int [] list);

    int updateOne(UserInfo userInfo);

    int insertOne(UserInfo userInfo);

    int insertBatch(List <UserInfo> list);

}
