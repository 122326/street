package com.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;


import java.time.LocalDateTime;

public class OrderCondition {

    @ApiModelProperty(value = "服务名称")
    private String serviceName;
    @ApiModelProperty(value = "用户电话")
    private String phone;
    @ApiModelProperty(value = "用户email")
    private String email;
    @ApiModelProperty(value = "用户名")
    private String userName;
    @ApiModelProperty(value = "搜索内容")
    private String content;

    @ApiModelProperty(value = "评价（0好评，1中评，2差评）")
    private Integer reputation;

    @ApiModelProperty(value = "搜索方式（0服务名称，1用户名，2用户电话,3用户Email）", required = true)
    private Integer wayOfSearch;


    @ApiModelProperty(value = "订单是否被用户删除（0否，1是）")
    private Integer isDelete;
    /**
     * 订单状态
     * 0已下单，1完成订单，
     * 2已取消订单,3已删除
     *
     */
    @ApiModelProperty(value = "订单状态（0已下单，1待评价，2已完成,3已取消，4已回复）")
    private Integer orderStatus;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "订单创建时间（起始）")
    private LocalDateTime orderStart;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "订单创建时间(结束)")
    private LocalDateTime orderEnd;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "订单结束时间（起始）")
    private LocalDateTime finishStart;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "订单结束时间（结束）")
    private LocalDateTime finishEnd;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getReputation() {
        return reputation;
    }

    public void setReputation(Integer reputation) {
        this.reputation = reputation;
    }

    public Integer getWayOfSearch() {
        return wayOfSearch;
    }

    public void setWayOfSearch(Integer wayOfSearch) {
        this.wayOfSearch = wayOfSearch;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderStart() {
        return orderStart;
    }

    public void setOrderStart(LocalDateTime orderStart) {
        this.orderStart = orderStart;
    }

    public LocalDateTime getOrderEnd() {
        return orderEnd;
    }

    public void setOrderEnd(LocalDateTime orderEnd) {
        this.orderEnd = orderEnd;
    }

    public LocalDateTime getFinishStart() {
        return finishStart;
    }

    public void setFinishStart(LocalDateTime finishStart) {
        this.finishStart = finishStart;
    }

    public LocalDateTime getFinishEnd() {
        return finishEnd;
    }

    public void setFinishEnd(LocalDateTime finishEnd) {
        this.finishEnd = finishEnd;
    }

    @Override
    public String toString() {
        return "OrderCondition{" +
                "serviceName='" + serviceName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", content='" + content + '\'' +
                ", reputation=" + reputation +
                ", wayOfSearch=" + wayOfSearch +
                ", isDelete=" + isDelete +
                ", orderStatus=" + orderStatus +
                ", orderStart=" + orderStart +
                ", orderEnd=" + orderEnd +
                ", finishStart=" + finishStart +
                ", finishEnd=" + finishEnd +
                '}';
    }
}
