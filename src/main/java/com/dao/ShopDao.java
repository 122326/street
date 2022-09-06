package com.dao;

import com.entity.Shop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShopDao {
	/**
	 * 根据查询条件分页返回店铺信息
	 * 可输入的条件有：店铺名(模糊),店铺状态，省份，城市，地区，是否移动，user
	 * @param rowIndex      从第几行开始取数据
	 * @param pageSize      返回的条数
	 */
	List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition, @Param("rowIndex") int rowIndex,
                             @Param("pageSize") int pageSize, @Param("sort") String sort, @Param("order") String order);

	/**
	 * 根据经纬度范围查找店铺
	 */
	List<Shop> queryNearbyShopList(@Param("maxlat") float maxlat, @Param("minlat") float minlat,
                                   @Param("maxlng") float maxlng, @Param("minlng") float minlng, @Param("shopName") String shopName);


	/**
	 * 通过shop id查询店铺
	 */
	Shop queryByShopId(long shopId);

	/**
	 * 添加店铺
	 *
	 */
	void insertShop(Shop shop);

	/**
	 * 更新店铺信息
	 *
	 */
	void updateShop(Shop shop);

	}
