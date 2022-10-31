package com.service;



import com.dto.OrderExecution;
import com.entity.OrderCondition;
import com.entity.OrderInfo;
import com.exceptions.OrderOperationException;


import java.util.List;

public interface OrderService {
	/**
	 * 根据orderCondition分页返回相应订单列表
	 *
	 * @param orderCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public OrderExecution getOrderList(OrderInfo orderCondition, int pageIndex, int pageSize,String sort,String order);

	/**
	 * 根据orderCondition分页返回相应订单列表
	 *
	 * @return
	 */
	public OrderExecution getOrderList2(long userId,int orderStatus);

	/**
	 * 根据orderCondition分页返回相应订单列表
	 *
	 * @param orderCondition
	 * @return
	 */
	public OrderExecution getOrderList3(OrderCondition orderCondition);
	/**
	 * 通过serviceId获取订单信息
	 *
	 * @return
	 */
	public OrderExecution getByServiceId(long serviceId, int pageIndex, int pageSize);
	public OrderExecution getByServiceId2(long serviceId,int orderStatus);


	public List<OrderExecution>  getByServiceIdList(List<Long> serviceIdList, int pageIndex, int pageSize);
	/**
	 * 通过userId获取订单信息
	 *
	 * @param userId
	 * @return
	 */
	public OrderExecution getByUserId(long userId, int pageIndex, int pageSize);
	/**
	 * 通过userId serviceId获取订单信息
	 *
	 * @param userId serviceId
	 * @return
	 */
	public OrderExecution getByUserIdAndServiceId(long userId, long serviceId);
	/**
	 * 通过订单Id获取订单信息
	 *
	 * @param orderId
	 * @return
	 */
	public OrderInfo getByOrderId(long orderId);
	/**
	 * 更新订单信息
	 *
	 * @param order
	 *
	 * @return
	 * @throws OrderOperationException
	 */
	public OrderExecution modifyOrder(OrderInfo order) throws OrderOperationException;

	/**
	 * 添加订单信息
	 *
	 * @param order
	 * @return
	 * @throws OrderOperationException
	 */
	public OrderExecution addOrder(OrderInfo order) throws OrderOperationException;
	/**
	 * 删除订单信息
	 *
	 * @return
	 * @throws OrderOperationException
	 */
	public OrderExecution deleteOrder(long orderId) throws OrderOperationException;
}
