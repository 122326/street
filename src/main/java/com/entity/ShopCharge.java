package com.entity;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class ShopCharge {
    @ApiModelProperty(value = "服务投诉Id", required = true)
    private Long shopChargeId;
    @ApiModelProperty(value = "商家Id", required = true)
    private Long shopId;
    @ApiModelProperty(value = "订单Id", required = true)
    private Long orderId;
    @ApiModelProperty(value = "用户Id", required = true)
    private Long userId;
    @ApiModelProperty(value = "投诉内容", required = true)
    private String chargeContent;
    @ApiModelProperty(value = "商家回复", required = true)
    private String chargeReply;
    @ApiModelProperty(value = "投诉时间")
    private Date createTime;

    public Long getShopChargeId() {
        return shopChargeId;
    }

    public void setShopChargeId(Long shopChargeId) {
        this.shopChargeId = shopChargeId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getChargeContent() {
        return chargeContent;
    }

    public void setChargeContent(String chargeContent) {
        this.chargeContent = chargeContent;
    }

    public String getChargeReply() {
        return chargeReply;
    }

    public void setChargeReply(String chargeReply) {
        this.chargeReply = chargeReply;
    }

    public Date getCreateTime() { return createTime; }

    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    @Override
    public String toString() {
        return "ShopCharge{" +
                "shopChargeId=" + shopChargeId +
                ", shopId=" + shopId +
                ", orderId=" + orderId +
                ", userId=" + userId +
                ", chargeContent='" + chargeContent + '\'' +
                ", chargeReply='" + chargeReply + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
