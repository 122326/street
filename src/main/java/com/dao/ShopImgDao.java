package com.dao;

import com.entity.ShopImg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ShopImgDao {
	/**
	 * 列出某个商铺的详情图列表
	 *
	 */
	List<ShopImg> getShopImgList(long shopId);

	/**
	 * 根据图片Id获取商铺图片
	 *
	 */
	ShopImg getShopImg(long shopImgId);

	/**
	 * 添加商铺详情图片
	 *
	 */
	void insertShopImg(ShopImg shopImg);

	/**
	 * 通过商铺图片ID删除商铺图片
	 *
	 */
	void delShopImgByShopImgId(long shopImgId);

	/**
	 * 删除指定商铺下的所有老旧详情图
	 *
	 */
	void deleteShopImgByShopIdAndCreateTime(@Param("shopId") long shopId, @Param("createTime") Date createTime);
}
