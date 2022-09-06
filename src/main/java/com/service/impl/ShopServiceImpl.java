package com.service.impl;

import com.dao.ShopDao;
import com.dao.ShopImgDao;
import com.util.ImageHolder;
import com.entity.Shop;
import com.entity.ShopImg;
import com.service.ShopService;
import com.util.ImageUtil;
import com.util.PageCalculator;
import com.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {
	@Autowired
	private ShopDao shopDao;
	@Autowired
	private ShopImgDao shopImgDao;

	@Override
	public List<Shop> getNearbyShopList(float maxlat, float minlat, float maxlng, float minlng, String shopName) {
		List<Shop> shopList = shopDao.queryNearbyShopList(maxlat, minlat, maxlng, minlng, shopName);
		return shopList;
	}


	@Override
	public List<Shop> getShopList(Shop shopCondition, int pageIndex, int pageSize, String sort, String order) {
		// 将页码转换成行码
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 依据查询条件，调用dao层返回相关的店铺列表
		List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize,sort,order);
		return shopList;
	}

	@Override
	public Shop getByShopId(long shopId) {
		return shopDao.queryByShopId(shopId);
	}

	@Override
	@Transactional
	public String uploadImg(Long shopId, ImageHolder shopImg, ImageHolder businessLicenseImg,
                                   ImageHolder profileImg, Date createTime) {
		Shop shop = new Shop();
		shop.setShopId(shopId);
		Shop tempShop = shopDao.queryByShopId(shopId);
		if (shopImg != null && shopImg.getImage() != null && shopImg.getImageName() != null
				&& !"".equals(shopImg.getImageName())) {
			if (createTime != null) {
				deleteShopImgList(shopId, createTime);
			}
			addShopImg(shopId, shopImg, createTime);
			}
			if (businessLicenseImg != null && businessLicenseImg.getImage() != null
					&& businessLicenseImg.getImageName() != null && !"".equals(businessLicenseImg.getImageName())) {
				if (tempShop.getBusinessLicenseImg() != null) {
					ImageUtil.deleteFileOrPath(tempShop.getBusinessLicenseImg());
				}
				addBusinessLicenseImg(shop, businessLicenseImg);
			}
			if (profileImg != null && profileImg.getImage() != null && profileImg.getImageName() != null
					&& !"".equals(profileImg.getImageName())) {
				if (tempShop.getProfileImg() != null) {
					ImageUtil.deleteFileOrPath(tempShop.getProfileImg());
				}
				addProfileImg(shop, profileImg);
			}
			shop.setLastEditTime(new Date());
			return "上传成功！";
			}

	@Override
	public Shop addShop(Shop shop)  {
		// 给店铺信息赋初始值
		shop.setEnableStatus(0);
		shop.setCreateTime(new Date());
		shop.setLastEditTime(new Date());
		if (shop.getPerCost() == null || shop.getPerCost() <0) {
			shop.setPerCost(0);
		}
		// 添加店铺信息
		shopDao.insertShop(shop);
		return shop;
	}

	@Override
	public void modifyShop(Shop shop){
		shop.setLastEditTime(new Date());
		if (shop.getPerCost() == null || shop.getPerCost() <0) {
			shop.setPerCost(0);
		}
		// 修改店铺信息
		shopDao.updateShop(shop);
	}

	private void addShopImg(long shopId, ImageHolder shopImgHolder, Date createTime) {
		// 获取图片存储路径，这里直接存放到相应店铺的文件夹底下
		String dest = PathUtil.getShopImgPath(shopId);
		String imgAddr = ImageUtil.generateNormalImg(shopImgHolder, dest);
		ShopImg shopImg = new ShopImg();
		imgAddr=imgAddr.replace("\\","/");
		shopImg.setImgAddr(imgAddr);
		shopImg.setShopId(shopId);
		shopImg.setCreateTime(createTime);
		shopImgDao.insertShopImg(shopImg);

	}

	/**
	 * 删除某个店铺下的所有旧的详情图
	 * 
	 * @param shopId
	 * @param createTime 最新上传详情图的时间
	 */
	private void deleteShopImgList(long shopId, Date createTime) {
		// 根据shopId获取原来的图片
		List<ShopImg> shopImgList = shopImgDao.getShopImgList(shopId);
		if (shopImgList != null && shopImgList.size() > 0) {
			// 干掉原来的图片
			for (ShopImg shopImg : shopImgList) {
				if (shopImg.getCreateTime().getTime() < createTime.getTime())
					ImageUtil.deleteFileOrPath(shopImg.getImgAddr());
			}
			// 删除数据库里原有图片的信息
			shopImgDao.deleteShopImgByShopIdAndCreateTime(shopId, createTime);
		}
	}

	private void addBusinessLicenseImg(Shop shop, ImageHolder businessLicenseImg) {
		// 获取businessLicenseImg图片目录的相对值路径
		String dest = PathUtil.getShopBusinessLicenseImgPath(shop.getShopId());
		String businessLicenseImgAddr = ImageUtil.generateNormalImg(businessLicenseImg, dest);
		businessLicenseImgAddr=businessLicenseImgAddr.replace("\\","/");
		shop.setBusinessLicenseImg(businessLicenseImgAddr);
	}

	private void addProfileImg(Shop shop, ImageHolder profileImg) {
		// 获取profileImg图片目录的相对值路径
		String dest = PathUtil.getShopProfileImgPath(shop.getShopId());
		String profileImgAddr = ImageUtil.generateThumbnail(profileImg, dest);
		profileImgAddr=profileImgAddr.replace("\\","/");
		shop.setProfileImg(profileImgAddr);
	}

	@Override
	@Transactional
	public Shop addShop(Shop shop, ImageHolder businessLicenseImg, ImageHolder profileImg)
	 {
		// 给店铺信息赋初始值
		shop.setEnableStatus(0);
		shop.setCreateTime(new Date());
		if (shop.getPerCost() == null || shop.getPerCost() <0) {
			shop.setPerCost(0);
		}
		// 添加店铺信息
		shopDao.insertShop(shop);
		if (businessLicenseImg != null && businessLicenseImg.getImage() != null
			&& businessLicenseImg.getImageName() != null && !"".equals(businessLicenseImg.getImageName())) {
		// 存储图片
		addBusinessLicenseImg(shop, businessLicenseImg);}
		if (profileImg != null && profileImg.getImage() != null && profileImg.getImageName() != null
			&& !"".equals(profileImg.getImageName())) {
			// 存储图片
			addProfileImg(shop, profileImg);}
		// 更新店铺的图片地址
		shopDao.updateShop(shop);
		return shop;
	}

	@Override
	public Shop modifyShop(Shop shop, ImageHolder businessLicenseImg, ImageHolder profileImg){
			Shop tempShop = shopDao.queryByShopId(shop.getShopId());
			if (businessLicenseImg != null && businessLicenseImg.getImage() != null
					&& businessLicenseImg.getImageName() != null && !"".equals(businessLicenseImg.getImageName())) {
				// 存储图片
				if (tempShop.getBusinessLicenseImg() != null) {
						ImageUtil.deleteFileOrPath(tempShop.getBusinessLicenseImg());
					}
					addBusinessLicenseImg(shop, businessLicenseImg);
			}
			if (profileImg != null && profileImg.getImage() != null && profileImg.getImageName() != null
					&& !"".equals(profileImg.getImageName())) {
				// 存储图片
					if (tempShop.getProfileImg() != null) {
						ImageUtil.deleteFileOrPath(tempShop.getProfileImg());
					}
					addProfileImg(shop, profileImg);

			}
			// 更新店铺的图片地址
			shop.setLastEditTime(new Date());
			if (shop.getPerCost() == null || shop.getPerCost() <0) {
				shop.setPerCost(0);
			}
			shopDao.updateShop(shop);
		return shop;
	}

	@Override
	public String delShopImg(Long shopImgId){
		if (shopImgId != null) {
			ShopImg shopImg = shopImgDao.getShopImg(shopImgId);
			if (shopImg != null) {
				ImageUtil.deleteFileOrPath(shopImg.getImgAddr());
				shopImgDao.delShopImgByShopImgId(shopImgId);
			}
		}
		return "删除成功！";
	}

	@Override
	public ShopImg addShopImg(Long shopId, ImageHolder shopImgHolder) {
		addShopImg(shopId, shopImgHolder, new Date());
		ShopImg shopImg=shopImgDao.getShopImg(shopId);
		return shopImg;
	}


}
