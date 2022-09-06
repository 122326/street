package com.service.impl;

import com.dao.OrderDao;
import com.entity.OrderInfo;
import com.service.OrderCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderCheckServiceImpl implements OrderCheckService {
	@Autowired
	private OrderDao orderDao;
	@Override
	@Transactional
	public void checkOrder(){
		OrderInfo orderCondition = new OrderInfo();
		orderCondition.setOrderStatus(0);
		List<OrderInfo> orderList = orderDao.queryOrderList2(orderCondition);


		LocalDateTime t=LocalDateTime.now().minusDays(7);
		for (OrderInfo order : orderList) {
			if (order.getCreateTime().isBefore(t)) {
				order.setOrderStatus(1);
				order.setOverTime(LocalDateTime.now());
				// 修改订单信息 超时自动确认订单
				orderDao.updateOrder(order);
		}
	}
  }

}