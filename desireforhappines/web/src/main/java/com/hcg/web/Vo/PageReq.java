package com.hcg.web.Vo;

import com.hcg.commondal.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;

public class PageReq {
    @Autowired
    UserInfo userInfo;

    public Integer pageNum;

    public Integer pageSize=20;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
}
