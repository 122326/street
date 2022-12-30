package com.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.time.LocalDateTime;


@ApiModel(value = "ServiceCollect对象", description = "服务收藏对象")
@TableName("tb_service_collect")
public class ServiceCollect {
    @ApiModelProperty(value = "收藏服务Id", required = true)
    private Long ServiceCollectId;
    @ApiModelProperty(value = "服务ID", required = true)
    private Long serviceId;
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;
    private LocalDateTime createTime;
    @ApiModelProperty(value = "逻辑删除字段")
    private Integer isDelete;

    public Long getServiceCollectId() {
        return ServiceCollectId;
    }

    public void setServiceCollectId(Long serviceCollectId) {
        ServiceCollectId = serviceCollectId;
    }

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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String toString() {
        return "ServiceCollect{" +
                "ServiceCollectId=" + ServiceCollectId +
                ", serviceId=" + serviceId +
                ", userId=" + userId +
                ", createTime=" + createTime +
                ", isDelete=" + isDelete +
                '}';
    }
}
