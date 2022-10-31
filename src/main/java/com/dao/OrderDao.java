package com.dao;

import com.entity.OrderCondition;
import com.entity.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderDao {
	/**
	 * 分页查询订单，可输入的条件有：服务id，用户id,订单状态,订单创建时间,订单结束时间
	 *
	 * @param orderCondition
	 * @param rowIndex
	 *            从第几行开始取数据
	 * @param pageSize
	 *            返回的条数
	 * @return
	 */
	public List<OrderInfo> queryOrderList(@Param("orderCondition")OrderInfo orderCondition, @Param("rowIndex") int rowIndex,
										  @Param("pageSize") int pageSize, @Param("sort") String sort, @Param("order") String order);

	/**
	 * 返回queryOrderList总数
	 *
	 * @param orderCondition
	 * @return
	 */
	public int queryOrderCount(@Param("orderCondition")OrderInfo orderCondition);
	/**
	 * 通过order id查询订单
	 * @param orderId
	 * @return
	 */
	public OrderInfo queryByOrderId(long orderId);
	/**
	 * 通过user id查询订单
	 * @param userId
	 * @return
	 */
//	public List<OrderInfo> queryByUserId(long userId);
	/**
	 * 查询订单，可输入的条件有：服务id，用户id,订单状态,订单创建时间,订单结束时间
	 *
	 * @param orderCondition
	 * @return
	 */
	public List<OrderInfo> queryOrderList2(@Param("orderCondition")OrderInfo orderCondition);
	/* 查询除正在进行的订单
	 *
	 * @param orderCondition
	 * @return
	 */
	public List<OrderInfo> queryOrderList3(@Param("orderCondition")OrderInfo orderCondition);

	/* 查询订单
	 *
	 * @param orderCondition
	 * @return
	 */
	public List<OrderInfo> queryOrderList4(@Param("orderCondition") OrderCondition orderCondition);

	/*
	 * 查询正在进行的订单
	 */
	public OrderInfo queryOrderByUS(@Param("orderCondition")OrderInfo orderCondition);
	/**
	 * 添加订单
	 * @param orderInfo
	 * @return
	 */
	public int  insertOrder(OrderInfo orderInfo);
	/**
	 * 批量添加订单
	 * @return
	 */
	public int  insertOrderInfo(List<OrderInfo> orderList);

	/**
	 * 更新订单信息
	 * @param orderInfo
	 * @return
	 */
	public int updateOrder(OrderInfo orderInfo);

	/**
	 * 删除订单信息
	 * @return
	 */
	public int deleteOrder(long orderId);

}