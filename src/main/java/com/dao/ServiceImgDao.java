package com.dao;

import com.entity.ServiceImg;
import com.entity.ShopImg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ServiceImgDao {
	public List<ServiceImg> queryServiceImg(@Param("serviceImgCondition")ServiceImg serviceImgCondition, @Param("rowIndex") int rowIndex,
											@Param("pageSize") int pageSize, @Param("sort") String sort, @Param("order") String order);
	int queryServiceImgCount(@Param("serviceImgCondition") ServiceImg serviceImgCondition);
	/**
	 * 列出某个店铺的某服务图
	 *
	 * @param serviceId
	 * @return
	 */
	public ServiceImg getServiceImg(long serviceId);
	/**
	 * 列出某个店铺的服务图列表
	 *
	 * @param serviceId
	 * @return
	 */
	public List<ServiceImg> getServiceImgList(long serviceId);
	/**
	 * 添加服务图片
	 *
	 * @param serviceImg
	 * @return
	 */
	public int insertServiceImg(ServiceImg serviceImg);

	/**
	 * 更改服务图片
	 *
	 * @param serviceImg
	 * @return
	 */
	public int updateServiceImg(ServiceImg serviceImg);

	/**
	 * 根据图片id删除服务图片
	 *
	 * @param serviceImgId
	 * @return
	 */
	public int deleteServiceImg(long serviceImgId);

	/**
	 * 根据服务id删除相关服务图片
	 *
	 * @param serviceId
	 * @return
	 */
	public int deleteImgByServiceId(long serviceId);
}
