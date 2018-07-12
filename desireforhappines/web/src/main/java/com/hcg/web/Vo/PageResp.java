package com.hcg.web.Vo;

public class PageResp {

    public Integer ErrorCode;   //-1  代表系统错误  0代表请求成功  1 代表请求成功返回错误异常

    public String msg;

    public Object object;

    public Integer getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(Integer errorCode) {
        ErrorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public PageResp(Integer errorCode, String msg, Object object) {
        ErrorCode = errorCode;
        this.msg = msg;
        this.object = object;
    }

    public PageResp() {
    }
}
