package com.dao;

import com.entity.ServiceInfo;
import com.entity.Shop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ServiceDao{
	/**
	 * 分页查询服务，可输入的条件有：服务名（模糊），店铺id，服务价格
	 *
	 * @param serviceCondition
	 * @param rowIndex
	 *            从第几行开始取数据
	 * @param pageSize
	 *            返回的条数
	 * @return
	 */
	public List<ServiceInfo> queryServiceList(@Param("serviceCondition")ServiceInfo serviceCondition, @Param("rowIndex") int rowIndex,
											  @Param("pageSize") int pageSize, @Param("sort") String sort, @Param("order") String order);

	/**
	 * 返回service数量
	 *
	 * @param serviceCondition
	 * @return
	 */
	public int queryServiceCount(@Param("serviceCondition")ServiceInfo serviceCondition);
	/**
	 * 通过service id查询服务
	 * @param serviceId
	 * @return
	 */
	public ServiceInfo queryByServiceId(long serviceId);
	/**
	 * 通过serviceCondition查询服务
	 * @param serviceCondition
	 * @return
	 */
	public List<ServiceInfo> queryServiceList2(@Param("serviceCondition")ServiceInfo serviceCondition);

	public int  insertService(ServiceInfo serviceInfo);
	/**
	 * 批量添加服务
	 * @param serviceList
	 * @return
	 */
	public int  insertServiceInfo(List<ServiceInfo> serviceList);

	/**
	 * 更新服务信息
	 * @param serviceInfo
	 * @return
	 */
	public int updateService(ServiceInfo serviceInfo);

	/**
	 * 删除服务信息
	 * @param serviceId
	 * @return
	 */
	public int deleteService(long serviceId);
}
