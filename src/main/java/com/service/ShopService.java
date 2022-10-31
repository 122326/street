package com.service;

import java.util.Date;

import com.dto.ImageHolder;
import com.dto.ShopExecution;
import com.dto.ShopImgExecution;
import com.entity.Shop;
import com.entity.ShopImg;
import com.exceptions.ShopOperationException;

public interface ShopService {
	/**
	 * 根据shopCondition分页返回相应店铺列表
	 *
	 * @param shopCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize, String sort, String order);

	/**
	 * 根据经纬范围返回附近店铺列表
	 *
	 * @param maxlat
	 * @param minlat
	 * @param maxlng
	 * @param minlng
	 * @return
	 */
	public ShopExecution getNearbyShopList(float maxlat, float minlat, float maxlng, float minlng, String shopName);

	/**
	 * 通过店铺Id获取店铺信息
	 *
	 * @param shopId
	 * @return
	 */
	Shop getByShopId(long shopId);

	/**
	 * 更新店铺信息，不包括对图片的处理
	 *
	 * @param shop
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution modifyShop(Shop shop) throws ShopOperationException;

	/**
	 * 上传店铺图片
	 *
	 * @param shopId
	 * @param shopImg
	 * @param businessLicenseImg
	 * @param profileImg
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution uploadImg(Long shopId, ImageHolder shopImg, ImageHolder businessLicenseImg, ImageHolder profileImg,
							Date createTime) throws ShopOperationException;

	/**
	 * 注册店铺信息，不包括对图片的处理
	 *
	 * @param shop
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution addShop(Shop shop) throws ShopOperationException;

	/**
	 * 注册商铺信息，包括对图片的处理
	 *
	 * @param shop
	 * @param businessLicenseImg
	 * @param profileImg
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution addShop(Shop shop, ImageHolder businessLicenseImg, ImageHolder profileImg)
			throws ShopOperationException;

	/**
	 * 修改商铺信息，包括对图片的处理
	 *
	 * @param shop
	 * @param businessLicenseImg
	 * @param profileImg
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution modifyShop(Shop shop, ImageHolder businessLicenseImg, ImageHolder profileImg)
			throws ShopOperationException;

	/**
	 * 分页获取商铺图片
	 *
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	ShopImgExecution getShopImgList(ShopImg shopImg, int pageIndex, int pageSize,String sort,String order);

	/**
	 * 根据商铺图片Id删除商铺图片
	 *
	 * @param shopImgId
	 * @throws ShopOperationException
	 */
	ShopImgExecution delShopImg(Long shopImgId) throws ShopOperationException;

	/**
	 * 添加商铺图片
	 *
	 * @param shopId
	 * @param shopImgHolder
	 * @throws ShopOperationException
	 */
	ShopImgExecution addShopImg(Long shopId, ImageHolder shopImgHolder) throws ShopOperationException;

	Integer getCount(Integer shopId, String shopName);
}
