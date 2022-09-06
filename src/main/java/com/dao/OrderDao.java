package com.dao;

import com.entity.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderDao {
	/**
	 * 根据查询条件分页返回订单信息
	 * 可输入的条件有：服务id，用户id,订单状态,订单创建时间,订单结束时间
	 *
	 * @param rowIndex 从第几行开始取数据
	 * @param pageSize 返回的条数
	 */
	List<OrderInfo> queryOrderList(@Param("orderCondition") OrderInfo orderCondition, @Param("rowIndex") int rowIndex,
                                          @Param("pageSize") int pageSize, @Param("sort") String sort, @Param("order") String order);
	/**
	 * 查询订单，可输入的条件有：服务id，用户id,订单状态,订单创建时间,订单结束时间
	 */
	List<OrderInfo> queryOrderList2(@Param("orderCondition") OrderInfo orderCondition);

	/**
	 * 通过order id查询订单
	 */
	OrderInfo queryByOrderId(long orderId);


	/**
	 * 添加订单
	 */
	void  insertOrder(OrderInfo orderInfo);

	
	/**
	 * 更新或删除订单信息
	 * 删除订单操作实际上是改变订单状态
	 */
	void updateOrder(OrderInfo orderInfo);

}
