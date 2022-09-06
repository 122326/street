package com.service;



public interface OrderCheckService {

	/**
	 * 检测进行中的order是否过期，如果过期，则修改order的状态，自动确认订单
	 */
	public void checkOrder();
	
}
