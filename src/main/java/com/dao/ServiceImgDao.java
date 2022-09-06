package com.dao;

import com.entity.ServiceImg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ServiceImgDao {
	/*
	* 根据查询条件分页返回服务图片
	*
	*/
	List<ServiceImg> queryServiceImg(@Param("serviceImgCondition") ServiceImg serviceImgCondition, @Param("rowIndex") int rowIndex,
                                            @Param("pageSize") int pageSize, @Param("sort") String sort, @Param("order") String order);


	/**
	 * 根据服务Id获取服务图片
	 *
	 * @param serviceId
	 * @return
	 */
	List<ServiceImg> getServiceImgList(long serviceId);

	/**
	 * 添加服务图片
	 * 
	 * @param serviceImg
	 * @return
	 */
	void insertServiceImg(ServiceImg serviceImg);


	/**
	 * 根据图片id删除服务图片
	 * 
	 * @param serviceImgId
	 * @return
	 */
	void deleteServiceImg(long serviceImgId);

	/**
	 * 根据服务id删除相关服务图片
	 *
	 * @param serviceId
	 * @return
	 */
	void deleteImgByServiceId(long serviceId);
}
