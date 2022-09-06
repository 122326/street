package com.service;

import com.util.ImageHolder;
import com.entity.ServiceImg;
import com.entity.ServiceInfo;

import java.util.Date;
import java.util.List;

public interface SService {
	/**
	 * 根据serviceCondition分页返回相应服务列表
	 */
	List<ServiceInfo> getServiceList(ServiceInfo serviceCondition, int pageIndex, int pageSize, String sort, String order);

	/**
	 * 通过商铺Id分页获取服务信息
	 */
	List<ServiceInfo> getByShopId(long shopId, int pageIndex, int pageSize);
	List<ServiceInfo> getByShopId2(long shopId);

	/**
	 * 通过服务Id获取服务信息
	 */
	ServiceInfo getByServiceId(long serviceId);

	/**
	 * 更新服务信息，不包括对图片的处理
	 *
	 */
	ServiceInfo modifyService(ServiceInfo service) ;

	List<ServiceImg> getServiceImgList(ServiceImg serviceImg, int pageIndex, int pageSize, String sort, String order);
	List<ServiceImg> getServiceImg(long serviceId);

	/**
	 * 上传服务图片
	 *
	 *
	 */
	ServiceImg uploadImg(long serviceId, ImageHolder serviceImg, Date createTime);


	/**
	 * 添加服务信息，不包括对图片的处理
	 *
	 */
	void addService(ServiceInfo service) ;

	/**
	 * 删除服务信息或照片信息
	 */
	void deleteService(long serviceId) ;
	void deleteServiceImg(ServiceImg serviceImg) ;

	/**
	 * 添加服务信息
	 */
	ServiceImg addServiceImg(long serviceId, ImageHolder serviceImgHolder, Date createTime);

}
