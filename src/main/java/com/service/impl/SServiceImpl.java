package com.service.impl;

import com.dao.ServiceDao;
import com.dao.ServiceImgDao;
import com.util.ImageHolder;
import com.entity.ServiceImg;
import com.entity.ServiceInfo;
import com.service.SService;
import com.util.ImageUtil;
import com.util.PageCalculator;
import com.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


@Service
public class SServiceImpl implements SService {
	@Autowired
	private ServiceDao serviceDao;
	@Autowired
	private ServiceImgDao serviceImgDao;
	// 字符串按照整型排序比较器
    static class PriorityComparator implements Comparator<ServiceInfo> {
        private boolean reverseOrder; // 是否倒序
        public PriorityComparator(boolean reverseOrder) {
            this.reverseOrder = reverseOrder;
        }
        
        public int compare(ServiceInfo arg0, ServiceInfo arg1) {
            if(reverseOrder) 
                return (int)(arg1.getServicePriority() - arg0.getServicePriority());
            else 
                return (int)(arg0.getServicePriority() - arg1.getServicePriority());
        }


    }


	@Override
	public List<ServiceInfo> getServiceList(ServiceInfo serviceCondition, int pageIndex, int pageSize,String sort,String order) {
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的服务列表
		List<ServiceInfo> serviceList = serviceDao.queryServiceList(serviceCondition, rowIndex, pageSize,sort,order);
		return serviceList;
	}


	@Override
	public List<ServiceInfo> getByShopId(long shopId, int pageIndex, int pageSize){
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的服务列表
		
		ServiceInfo serviceCondition = new ServiceInfo();
		serviceCondition.setShopId(shopId);
		List<ServiceInfo> serviceList = serviceDao.queryServiceList(serviceCondition, rowIndex, pageSize,null,null);

		return serviceList;
	}

	@Override
	public List<ServiceInfo> getByShopId2(long shopId){
		// 依据查询条件，调用dao层返回相关的服务列表
		ServiceInfo serviceCondition = new ServiceInfo();
		serviceCondition.setShopId(shopId);
		List<ServiceInfo> serviceList = serviceDao.queryServiceList2(serviceCondition);
		List<ServiceInfo> se = new ArrayList<ServiceInfo>();
		return se;
	}



	@Override
	public ServiceInfo getByServiceId(long serviceId) {
		return serviceDao.queryByServiceId(serviceId);
	}


    //更新图片
	@Override
	@Transactional
	public ServiceImg uploadImg(long serviceId, ImageHolder serviceImgHolder,Date createTime)
	{
		ServiceImg seI = new ServiceImg();
		// 根据serviceId获取原来的图片
		ServiceImg serviceImg = serviceImgDao.getServiceImgList(serviceId).get(0);
         if (serviceImgHolder != null && serviceImgHolder.getImage() != null
				 && serviceImgHolder.getImageName() != null
	        	&& !"".equals(serviceImgHolder.getImageName())) {
         	if (serviceImg!=null){
        	//图片存在，则删除图片
				deleteServiceImg(serviceImg);
        	}
        	seI=addServiceImg(serviceId, serviceImgHolder,createTime);
         	//将该图片信息写回数据库
         	serviceImgDao.insertServiceImg(seI);
          }
		return seI;
	}


	@Override
	public void addService(ServiceInfo service)  {
    	// 添加服务信息（从前端读取数据）
		serviceDao.insertService(service);
		ServiceInfo se=new ServiceInfo();
	}


	@Transactional
	@Override
	public ServiceInfo modifyService(ServiceInfo service){
    	// 修改服务信息
		serviceDao.updateService(service);

		return service;
	}

	//将服务图片信息注入服务图片对象
	public ServiceImg addServiceImg(long serviceId, ImageHolder serviceImgHolder,Date createTime) {
		// 获取图片存储路径，这里直接存放到相应服务的文件夹底下
		String dest = PathUtil.getServiceImgPath(serviceId);
		String imgAddr = ImageUtil.generateNormalImg(serviceImgHolder, dest);
		imgAddr=imgAddr.replace("\\","/");
	    ServiceImg serviceImg = new ServiceImg();
	    serviceImg.setImgAddr(imgAddr);
	    serviceImg.setServiceId(serviceId);
	    serviceImg.setCreateTime(createTime);
		return serviceImg;
	}


	@Override
	public List<ServiceImg> getServiceImgList(ServiceImg serviceImg, int pageIndex, int pageSize, String sort, String order) {
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的服务图片列表
		List<ServiceImg> serviceImgList = serviceImgDao.queryServiceImg(serviceImg, rowIndex, pageSize,sort,order);
		return serviceImgList;
	}


	@Override
	public List<ServiceImg> getServiceImg(long serviceId) {
		// 依据查询条件，调用dao层返回相关的服务图片列表
		List<ServiceImg> serviceImgList = serviceImgDao.getServiceImgList(serviceId);
		return serviceImgList;
	}


	/**
	 * 删除某个店铺下的服务详情图
	 *
	 * @param serviceImg
	 */
	public void deleteServiceImg(ServiceImg serviceImg) {

	    ImageUtil.deleteFileOrPath(serviceImg.getImgAddr());	
		// 删除数据库里原有图片的信息
		serviceImgDao.deleteServiceImg(serviceImg.getServiceImgId());
	}

	@Override
	public void deleteService(long serviceId)
	{
		// 删除服务信息
		serviceDao.deleteService(serviceId);
		serviceImgDao.deleteImgByServiceId(serviceId);
	}


}
