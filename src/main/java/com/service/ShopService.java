package com.service;

import com.util.ImageHolder;
import com.entity.Shop;
import com.entity.ShopImg;

import java.util.Date;
import java.util.List;

public interface ShopService {
	/**
	 * 根据shopCondition分页返回相应店铺列表
	 *
	 */
	List<Shop> getShopList(Shop shopCondition, int pageIndex, int pageSize,
								  String sort, String order);

	/**
	 * 根据经纬范围返回附近店铺列表
	 *
	 */
	List<Shop> getNearbyShopList(float maxlat, float minlat, float maxlng, float minlng, String shopName);

	/**
	 * 通过店铺Id获取店铺信息
	 *
	 */
	Shop getByShopId(long shopId);

	/**
	 * 更新店铺信息，不包括对图片的处理
	 *
	 */
	void modifyShop(Shop shop);

	/**
	 * 上传店铺图片
	 *
	 */
	String uploadImg(Long shopId, ImageHolder shopImg, ImageHolder businessLicenseImg, ImageHolder profileImg,
                            Date createTime) ;

	/**
	 * 注册店铺信息，不包括对图片的处理
	 *
	 */
	Shop addShop(Shop shop) ;

	/**
	 * 注册商铺信息，包括对图片的处理
	 *
	 */
	Shop addShop(Shop shop, ImageHolder businessLicenseImg, ImageHolder profileImg);

	/**
	 * 修改商铺信息，包括对图片的处理
	 *
	 */
	Shop modifyShop(Shop shop, ImageHolder businessLicenseImg, ImageHolder profileImg);

	/**
	 * 根据商铺图片Id删除商铺图片
	 *
	 */
	String delShopImg(Long shopImgId);

	/**
	 * 添加商铺图片
	 *
	 */
	ShopImg addShopImg(Long shopId, ImageHolder shopImgHolder);


}
