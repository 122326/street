package com.service.impl;

import com.dao.OrderDao;
import com.dao.ServiceDao;
import com.dao.ShopCommentDao;
import com.dao.ShopDao;
import com.entity.OrderInfo;
import com.entity.ServiceInfo;
import com.entity.Shop;
import com.entity.ShopComment;
import com.service.ShopCommentService;
import com.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class ShopCommentServiceImpl implements ShopCommentService {
	@Autowired
	private ShopCommentDao shopCommentDao;
	@Autowired
	private ShopDao shopDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private ServiceDao serviceDao;
	@Override
	public List<ShopComment> getShopCommentList(ShopComment shopCommentCondition, int pageIndex, int pageSize, String sort, String order) {
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的评论列表
		List<ShopComment> shopCommentList = shopCommentDao.queryShopCommentList(shopCommentCondition, rowIndex, pageSize,sort,order);
		return shopCommentList;
	}
	@Override
	public List<ShopComment> getByShopId(long shopId, int pageIndex, int pageSize){
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的评论列表
		
		ShopComment shopCommentCondition = new ShopComment();
		shopCommentCondition.setShopId(shopId);
		List<ShopComment> shopCommentList = shopCommentDao.queryShopCommentList(shopCommentCondition, rowIndex, pageSize,null,null);
		return shopCommentList;
	}
	@Override
	public List<ShopComment> getByShopId2(long shopId){
		// 依据查询条件，调用dao层返回相关的评论列表
		
		ShopComment shopCommentCondition = new ShopComment();
		shopCommentCondition.setShopId(shopId);
		List<ShopComment> shopCommentList = shopCommentDao.queryShopCommentList2(shopCommentCondition);
		return shopCommentList;
	}


	@Override
	public Shop getAvgByShopId(long shopId)
	{
		float serviceAvg=0;
		float starAvg=0;
		int successRate=0;
		Shop shop=new Shop();

			shop=shopDao.queryByShopId(shopId);
			ShopComment shopComment=new ShopComment();
			shopComment.setShopId(shopId);
			List<ShopComment> shopCommentList=shopCommentDao.queryShopCommentList2(shopComment);
			if(shopCommentList!=null&&shopCommentList.size()!=0)
			{
				//平均分
				serviceAvg = shopCommentDao.queryAvgServiceRating(shopId);
				starAvg=shopCommentDao.queryAvgStarRating(shopId);
				List<ServiceInfo> servicelist=new ArrayList<ServiceInfo>();
				List<OrderInfo> orderlist=new ArrayList<OrderInfo>();
				List<OrderInfo> torderlist=new ArrayList<OrderInfo>();
				ServiceInfo serviceCondition=new ServiceInfo();
				serviceCondition.setShopId(shopId);
				//通过shopId取service
				servicelist=serviceDao.queryServiceList2(serviceCondition);
				//取全部订单前，应该获得服务列表，再查对应的订单
				for(int i=0;i<servicelist.size();i++)
				{

					OrderInfo orderCondition =new OrderInfo();
					orderCondition.setServiceId(servicelist.get(i).getServiceId());
					torderlist.addAll(orderDao.queryOrderList2(orderCondition));
				}
				for(int i=0;i<torderlist.size();i++)
				{
					//已取消的订单列表
				if(torderlist.get(i).getOrderStatus()==3)
					{
						orderlist.add(torderlist.get(i));
						System.out.println("已取消:"+torderlist.get(i).toString());
					}
				}

				int torderNum=torderlist.size();
				if(torderNum!=0)
				{
					int forderNum=torderNum-orderlist.size();
					float s=((float)forderNum/torderNum)*100;
					successRate=(int)s;
					System.out.println("取消的数量:"+orderlist.size());
				}

			shop.setServiceAvg(serviceAvg);
			shop.setStarAvg(starAvg);
			shop.setSuccessRate(successRate);
		}
		return shop;
		
	}

	@Override
	public List<ShopComment> getByUserId2(long userId){
		// 依据查询条件，调用dao层返回相关的评论列表
		
		ShopComment shopCommentCondition = new ShopComment();
		shopCommentCondition.setUserId(userId);
		List<ShopComment> shopCommentList = shopCommentDao.queryShopCommentList2(shopCommentCondition);
		return shopCommentList;
	}


	@Override
	public List<ShopComment> getByUserId(long userId, int pageIndex, int pageSize){
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的评论列表
		
		ShopComment shopCommentCondition = new ShopComment();
		shopCommentCondition.setUserId(userId);
		List<ShopComment> shopCommentList = shopCommentDao.queryShopCommentList(shopCommentCondition, rowIndex, pageSize,null,null);
		return shopCommentList;
	}


	@Override
	public ShopComment getByShopCommentId(long shopCommentId) {
		return shopCommentDao.queryByShopCommentId(shopCommentId);
	}
	@Override
	public ShopComment getByOrderId(long orderId) {
		return shopCommentDao.queryByOrderId(orderId);
	}
   
	@Override
	public void addShopComment(ShopComment shopComment)  {
		// 添加评论信息（从前端读取数据）
		shopCommentDao.insertShopComment(shopComment);
	}
	
	@Transactional
	@Override
	public void modifyShopComment(ShopComment shopComment){
		// 修改评论信息
		shopCommentDao.updateShopComment(shopComment);
	}

	
	@Override
	public void deleteShopComment(long shopCommentId)
	{
		// 删除评论信息
		shopCommentDao.deleteShopComment(shopCommentId);
	}




}
