package com.entity;

import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

public class OrderInfo {

	@ApiModelProperty(value = "订单id 主键", required = true)
	private Long orderId;
	@ApiModelProperty(value = "服务id", required = true)
	private Long serviceId;
	@ApiModelProperty(value = "用户名", required = true)
	private String userName;
	@ApiModelProperty(value = "用户id", required = true)
	private Long userId;
	@ApiModelProperty(value = "服务数量", required = true)
	private Long serviceCount;
	@ApiModelProperty(value = "订单状态（0已下单，1待评价，2已完成,3已取消，4已回复）", required = true)
	private Integer orderStatus;
	@ApiModelProperty(value = "订单创建时间")
	private LocalDateTime createTime;
	@ApiModelProperty(value = "订单结束时间")
	private LocalDateTime overTime;
	@ApiModelProperty(value = "服务名称")
	private String serviceName;
	@ApiModelProperty(value = "订单价格", required = true)
	private double orderPrice;

	@ApiModelProperty(value = "逻辑删除字段")
	private Integer isDelete;

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getServiceCount() {
		return serviceCount;
	}
	public void setServiceCount(Long serviceCount) {
		this.serviceCount = serviceCount;
	}
	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	public LocalDateTime getCreateTime() {
		return createTime;
	}
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}
	public LocalDateTime getOverTime() {
		return overTime;
	}
	public void setOverTime(LocalDateTime overTime) {
		this.overTime = overTime;
	}
	public double getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(double orderPrice) {
		this.orderPrice = orderPrice;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	@Override
	public String toString() {
		return "OrderInfo [orderId=" + orderId + ", serviceId=" + serviceId + ", userId=" + userId + ", serviceCount="
				+ serviceCount + ", orderStatus=" + orderStatus + ", createTime=" + createTime + ", overTime="
				+ overTime + ", serviceName=" + serviceName + ", orderPrice=" + orderPrice + ", userName=" + userName +
				", isDelete="+isDelete+"]";
	}
	
}
