package com.entity;

import com.auth0.jwt.internal.com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.annotations.Mapper;


import java.util.Date;

@Mapper
public class AuthOperationLog {
    private String localAuth;
    private String detail;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    public AuthOperationLog() {
    }

    public AuthOperationLog(String localAuth, String detail, Date createTime) {
        this.localAuth = localAuth;
        this.detail = detail;
        this.createTime = createTime;
    }

    public String getLocalAuthId() {
        return localAuth;
    }

    public void setLocalAuthId(String localAuth) {
        this.localAuth = localAuth;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "AuthOperationLog{" +
                "localAuthId=" + localAuth +
                ", detail='" + detail + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
