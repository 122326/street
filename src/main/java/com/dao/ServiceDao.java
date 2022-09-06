package com.dao;

import com.entity.ServiceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ServiceDao{
	/**
	 * 根据查询条件分页返回服务信息
	 * 可输入的条件有：服务名（模糊），店铺id，服务价格
	 *
	 */
	List<ServiceInfo> queryServiceList(@Param("serviceCondition") ServiceInfo serviceCondition, @Param("rowIndex") int rowIndex,
                                              @Param("pageSize") int pageSize, @Param("sort") String sort, @Param("order") String order);
	/**
	 * 通过serviceCondition查询服务
	 *
	 */
	List<ServiceInfo> queryServiceList2(@Param("serviceCondition") ServiceInfo serviceCondition);


	/**
	 * 通过service id查询服务
	 *
	 */
	ServiceInfo queryByServiceId(long serviceId);

	/**
	 * 新建服务
	 */
	void  insertService(ServiceInfo serviceInfo);
	
	/**
	 * 更新服务信息
	 *
	 */
	void updateService(ServiceInfo serviceInfo);

	/**
	 * 删除服务信息
	 *
	 */
	void deleteService(long serviceId);
}
