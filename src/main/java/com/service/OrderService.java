package com.service;



import com.entity.OrderInfo;


import java.util.List;

public interface OrderService {
	/**
	 * 根据orderCondition分页返回相应订单列表
	 *
	 */
	List<OrderInfo> getOrderList(OrderInfo orderCondition, int pageIndex, int pageSize, String sort, String order);

	/**
	 * 根据用户Id和订单状态返回相应订单列表
	 *
	 */
	List<OrderInfo> getOrderList2(long userId, int orderStatus);

	/**
	 * 通过serviceId获取订单信息
	 *
	 */
	List<OrderInfo> getByServiceId(long serviceId, int pageIndex, int pageSize);
	List<OrderInfo> getByServiceId2(long serviceId, int orderStatus);

	/**
	 * 通过userId获取订单信息
	 *
	 */
	List<OrderInfo> getByUserId(long userId, int pageIndex, int pageSize);


	/**
	 * 通过订单Id获取订单信息
	 *
	 */
	OrderInfo getByOrderId(long orderId);

	/**
	 * 更新订单信息
	 */
	OrderInfo modifyOrder(OrderInfo order);

	/**
	 * 添加订单信息
	 */
	OrderInfo addOrder(OrderInfo order);
	/**
	 * 删除订单信息
	 */
	void deleteOrder(long orderId);
}
