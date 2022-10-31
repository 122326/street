package com.service;

import com.dto.ImageHolder;
import com.dto.ServiceExecution;
import com.entity.ServiceImg;
import com.entity.ServiceInfo;
import com.exceptions.ServiceOperationException;

import java.util.Date;
import java.util.List;

public interface SService {
	/**
	 * 根据serviceCondition分页返回相应服务列表
	 *
	 * @param serviceCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public ServiceExecution getServiceList(ServiceInfo serviceCondition, int pageIndex, int pageSize, String sort, String order);

	public ServiceExecution getByShopId(long shopId, int pageIndex, int pageSize);
	public ServiceExecution getByShopId2(long shopId);
	/**
	 * 通过服务Id获取服务信息
	 *
	 * @param serviceId
	 * @return
	 */
	public ServiceInfo getByServiceId(long serviceId);
	/**
	 * 更新服务信息，不包括对图片的处理
	 *
	 * @param service
	 *
	 * @return
	 * @throws ServiceOperationException
	 */
	public ServiceExecution modifyService(ServiceInfo service) throws ServiceOperationException;
	public ServiceExecution getServiceImgList(ServiceImg serviceImg,int pageIndex, int pageSize,String sort,String order);
	public ServiceExecution getServiceImg(long serviceId);
	/**
	 * 上传服务图片
	 *
	 * @param serviceId
	 * @param serviceImg
	 *
	 *
	 * @return
	 * @throws ServiceOperationException
	 */
	public ServiceExecution uploadImg(long serviceId, ImageHolder serviceImg,Date createTime)
			throws ServiceOperationException;
	public ServiceExecution createServiceImg(Long serviceId, ImageHolder serviceImgHolder);
	/**
	 * 添加服务信息，不包括对图片的处理
	 *
	 * @param service
	 * @return
	 * @throws ServiceOperationException
	 */
	public ServiceExecution addService(ServiceInfo service) throws ServiceOperationException;
	/**
	 * 删除服务信息
	 *
	 * @return
	 * @throws ServiceOperationException
	 */
	public ServiceExecution deleteService(long serviceId) throws ServiceOperationException;
	public ServiceExecution deleteServiceImg(ServiceImg serviceImg) ;
	public ServiceExecution addServiceImg(long serviceId, ImageHolder serviceImgHolder,Date createTime) throws ServiceOperationException;

}
