package com.service.impl;

import com.dao.OrderDao;
import com.entity.OrderInfo;
import com.service.OrderService;
import com.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderDao orderDao;
	
	@Override
	public List<OrderInfo> getOrderList(OrderInfo orderCondition, int pageIndex, int pageSize, String sort, String order) {
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的订单列表
		List<OrderInfo> orderList = orderDao.queryOrderList(orderCondition, rowIndex, pageSize,sort,order);
		return orderList;
	}
	@Override
	public List<OrderInfo> getOrderList2(long userId,int orderStatus) {
		// 依据查询条件，调用dao层返回相关的订单列表
		OrderInfo orderCondition=new OrderInfo();
		orderCondition.setCreateTime(LocalDateTime.now().minusDays(90));
		orderCondition.setOrderStatus(orderStatus);
		orderCondition.setIsDelete(0);
		orderCondition.setUserId(userId);
		List<OrderInfo> orderList = orderDao.queryOrderList2(orderCondition);
		return orderList;
	}


	@Override
	public List<OrderInfo> getByServiceId(long serviceId, int pageIndex, int pageSize){
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的订单列表
		
		OrderInfo orderCondition = new OrderInfo();
		orderCondition.setServiceId(serviceId);
		List<OrderInfo> orderList = orderDao.queryOrderList(orderCondition, rowIndex, pageSize,null,null);
		return orderList;
	}
	@Override
	public List<OrderInfo> getByServiceId2(long serviceId,int orderStatus){
		// 依据查询条件，调用dao层返回相关的订单列表
		
		OrderInfo orderCondition = new OrderInfo();
		orderCondition.setCreateTime(LocalDateTime.now().minusDays(90));
		if(orderStatus!=-1)
			orderCondition.setOrderStatus(orderStatus);
		orderCondition.setServiceId(serviceId);
		List<OrderInfo> orderList = orderDao.queryOrderList2(orderCondition);
		return orderList;
	}


	@Override
	public List<OrderInfo> getByUserId(long userId, int pageIndex, int pageSize){
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的订单列表
		
		OrderInfo orderCondition = new OrderInfo();
		orderCondition.setUserId(userId);
		List<OrderInfo> orderList = orderDao.queryOrderList(orderCondition, rowIndex, pageSize,null,null);
		return orderList;
	}


	@Override
	public OrderInfo getByOrderId(long orderId) {
		return orderDao.queryByOrderId(orderId);
	}

	@Override
	public OrderInfo addOrder(OrderInfo order) {
		// 添加订单信息（从前端读取数据）
		orderDao.insertOrder(order);
		return order;
	}

	@Transactional
	@Override
	public OrderInfo modifyOrder(OrderInfo order){
		// 修改订单信息
		orderDao.updateOrder(order);
		return order;
	}

	
	@Override
	public void deleteOrder(long orderId)
	{
		// 删除订单信息
		OrderInfo orderInfo = orderDao.queryByOrderId(orderId);
		orderInfo.setIsDelete(1);
		orderInfo.setOrderStatus(3);
		orderDao.updateOrder(orderInfo);
    }




}
