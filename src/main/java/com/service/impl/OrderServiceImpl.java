package com.service.impl;

import com.dao.OrderDao;
import com.dto.OrderExecution;
import com.entity.OrderCondition;
import com.entity.OrderInfo;
import com.enums.OrderStateEnum;
import com.exceptions.OrderOperationException;
import com.service.OrderService;
import com.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderDao orderDao;

	@Override
	public OrderExecution getOrderList(OrderInfo orderCondition, int pageIndex, int pageSize,String sort,String order) {
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的订单列表
		List<OrderInfo > orderList = orderDao.queryOrderList(orderCondition, rowIndex, pageSize,sort,order);
		// 依据相同的查询条件，返回订单总数
		int count = orderDao.queryOrderCount(orderCondition);
		OrderExecution se = new OrderExecution();
		if (orderList != null) {
			se.setOrderList(orderList);
			se.setCount(count);
		} else {
			se.setState(OrderStateEnum.INNER_ERROR.getState());
		}
		return se;
	}
	@Override
	public OrderExecution getOrderList2(long userId,int orderStatus) {
		// 依据查询条件，调用dao层返回相关的订单列表
		OrderInfo orderCondition=new OrderInfo();
		orderCondition.setCreateTime(LocalDateTime.now().minusDays(90));
		if(orderStatus!=-1)
			orderCondition.setOrderStatus(orderStatus);
		orderCondition.setIsDelete(0);
		orderCondition.setUserId(userId);
		List<OrderInfo > orderList = orderDao.queryOrderList2(orderCondition);
		// 依据相同的查询条件，返回订单总数
		int count = orderDao.queryOrderCount(orderCondition);
		OrderExecution se = new OrderExecution();
		if (orderList != null) {
			se.setOrderList(orderList);
			se.setCount(count);
		} else {
			se.setState(OrderStateEnum.INNER_ERROR.getState());
		}
		return se;
	}

	@Override
	public OrderExecution getOrderList3(OrderCondition orderCondition){
// 依据查询条件，调用dao层返回相关的订单列表

		List<OrderInfo > orderList = orderDao.queryOrderList4(orderCondition);
		OrderExecution se = new OrderExecution();
		if (orderList != null) {
			se.setOrderList(orderList);
		} else {
			se.setState(OrderStateEnum.INNER_ERROR.getState());
		}
		return se;
	};


	@Override
	public OrderExecution getByServiceId(long serviceId, int pageIndex, int pageSize){
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的订单列表

		OrderInfo  orderCondition = new OrderInfo ();
		orderCondition.setServiceId(serviceId);
		List<OrderInfo > orderList = orderDao.queryOrderList(orderCondition, rowIndex, pageSize,null,null);
		OrderExecution se = new OrderExecution();
		// 依据相同的查询条件，返回订单总数
		int count = orderDao.queryOrderCount(orderCondition);
		if (orderList != null) {
			se.setOrderList(orderList);
			se.setCount(count);
		} else {
			se.setState(OrderStateEnum.INNER_ERROR.getState());
		}
		return se;
	}
	@Override
	public OrderExecution getByServiceId2(long serviceId,int orderStatus){
		// 依据查询条件，调用dao层返回相关的订单列表

		OrderInfo  orderCondition = new OrderInfo ();
		orderCondition.setCreateTime(LocalDateTime.now().minusDays(90));
		if(orderStatus!=-1)
			orderCondition.setOrderStatus(orderStatus);
		orderCondition.setServiceId(serviceId);
		List<OrderInfo > orderList = orderDao.queryOrderList2(orderCondition);
		OrderExecution se = new OrderExecution();
		// 依据相同的查询条件，返回订单总数
		int count = orderDao.queryOrderCount(orderCondition);
		if (orderList != null) {
			se.setOrderList(orderList);
			se.setCount(count);
		} else {
			se.setState(OrderStateEnum.INNER_ERROR.getState());
		}
		return se;
	}




	@Override
	public List<OrderExecution> getByServiceIdList(List<Long> serviceIdList, int pageIndex, int pageSize){
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的订单列表
		List<OrderInfo > torderList=new ArrayList<OrderInfo>();
		OrderInfo  orderCondition = new OrderInfo ();
		List<OrderExecution> tse = new ArrayList<OrderExecution>();
		for(int i=0;i<serviceIdList.size();i++)
		{
			orderCondition.setServiceId(serviceIdList.get(i));
			List<OrderInfo > orderList = orderDao.queryOrderList(orderCondition, rowIndex, pageSize,null,null);
			// 依据相同的查询条件，返回订单总数
			int count = orderDao.queryOrderCount(orderCondition);
			OrderExecution se = new OrderExecution(i);
			if (orderList != null) {

				se.setCount(torderList.size()+count);
				//se.setCount(torderList.size()+orderList.size());
				torderList.addAll(orderList);
				se.setOrderList(torderList);

				//se.setCount(torderList.size());
			} else {
				se.setState(OrderStateEnum.INNER_ERROR.getState());
			}
		}
		return tse;
	}
	@Override
	public OrderExecution getByUserId(long userId, int pageIndex, int pageSize){
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的订单列表

		OrderInfo orderCondition = new OrderInfo();
		orderCondition.setUserId(userId);
		List<OrderInfo> orderList = orderDao.queryOrderList(orderCondition, rowIndex, pageSize,null,null);
		OrderExecution se = new OrderExecution();
		// 依据相同的查询条件，返回订单总数
		int count = orderDao.queryOrderCount(orderCondition);
		if (orderList != null) {
			se.setOrderList(orderList);
			se.setCount(count);
		} else {
			se.setState(OrderStateEnum.INNER_ERROR.getState());
		}
		return se;
	}
	@Override
	public OrderExecution getByUserIdAndServiceId(long userId,long serviceId){
		// 依据查询条件，调用dao层返回相关的订单列表
		//查询正在进行的服务订单
		OrderInfo orderCondition = new OrderInfo();
		orderCondition.setUserId(userId);
		orderCondition.setServiceId(serviceId);
		orderCondition.setOrderStatus(0);
		OrderExecution se = new OrderExecution();
		try
		{
			OrderInfo order= orderDao.queryOrderByUS(orderCondition);
			if (order!=null) {
				se.setOrder(order);
				se.setState(OrderStateEnum.SUCCESS.getState());
			}
			else {
				se.setState(OrderStateEnum.INNER_ERROR.getState());
			}
		}catch (Exception e) {
			throw new OrderOperationException("getByUserIdAndServiceId error:" + e.toString());
		}
		return se;
	}
	@Override
	public OrderInfo getByOrderId(long orderId) {
		return orderDao.queryByOrderId(orderId);
	}
	@Override
	public OrderExecution addOrder(OrderInfo order) throws OrderOperationException {
		// 空值判断
		if (order == null) {
			return new OrderExecution(OrderStateEnum.NULL_ORDER);
		}
		try {
			// 添加订单信息（从前端读取数据）
			int effectedNum = orderDao.insertOrder(order);
			if (effectedNum <= 0) {
				throw new OrderOperationException("订单创建失败");
			}
		} catch (Exception e) {
			throw new OrderOperationException("addOrder error:" + e.toString());
		}
		return new OrderExecution(OrderStateEnum.SUCCESS, order);
	}

	@Transactional
	@Override
	public OrderExecution modifyOrder(OrderInfo order) throws OrderOperationException{
		// 空值判断
		if (order == null) {
			return new OrderExecution(OrderStateEnum.NULL_ORDER);
		}
		try {
			// 修改订单信息
			int effectedNum = orderDao.updateOrder(order);
			System.out.println(effectedNum);
			if (effectedNum <= 0) {
				throw new OrderOperationException("订单修改失败");
			}
		} catch (Exception e) {
			throw new OrderOperationException("modifyOrder error:" + e.toString());
		}
		return new OrderExecution(OrderStateEnum.SUCCESS, order);
	}


	@Override
	public OrderExecution deleteOrder(long orderId) throws OrderOperationException
	{
		// 空值判断
		if (orderId<=0) {
			return new OrderExecution(OrderStateEnum.NULL_ORDERID);
		}
		try {
			// 删除订单信息
			// int effectedNum = orderDao.deleteOrder(orderId);
			OrderInfo orderInfo = orderDao.queryByOrderId(orderId);
			orderInfo.setIsDelete(1);
			orderInfo.setOrderStatus(3);
			int effectedNum = orderDao.updateOrder(orderInfo);
			if (effectedNum <= 0) {
				throw new OrderOperationException("订单删除失败");
			}
		} catch (Exception e) {
			throw new OrderOperationException("deleteOrder error:" + e.toString());
		}
		return new OrderExecution(OrderStateEnum.SUCCESS, orderDao.queryByOrderId(orderId));
	}




}
