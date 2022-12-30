package com.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;


import java.time.LocalDateTime;


@TableName("tb_service_collect")
public class ServiceCollectLog {
    @ApiModelProperty(value = "服务ID", required = true)
    private Long serviceId;
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

    private String userName;
    private String serviceName;

    private String profileImg;

    private LocalDateTime createTime;

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ServiceCollectLog{" +
                "serviceId=" + serviceId +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", profileImg='" + profileImg + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
