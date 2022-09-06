package com.service;


import com.entity.Shop;
import com.entity.ShopComment;

import java.util.List;

public interface ShopCommentService {
	/**
	 * 根据shopCommentCondition分页返回相应评论列表
	 *
	 */
	List<ShopComment> getShopCommentList(ShopComment shopCommentCondition,
												int pageIndex, int pageSize, String sort, String order);

	/**
	 * 通过shopId获取评论信息
	 *
	 */
	List<ShopComment> getByShopId(long shopId, int pageIndex, int pageSize);
	List<ShopComment> getByShopId2(long shopId);
	/**
	 * 通过shopId获取服务评分，星级评分平均分
	 *
	 */
	Shop getAvgByShopId(long shopId);
	/**
	 * 通过userId获取评论信息
	 *
	 */
	List<ShopComment> getByUserId(long userId, int pageIndex, int pageSize);
	List<ShopComment> getByUserId2(long userId);
	/**
	 * 通过评论Id获取评论信息
	 *
	 */
	ShopComment getByShopCommentId(long shopCommentId);
	/**
	 * 通过orderId获取评论信息 和评论id是一一对应
	 *
	 */
	ShopComment getByOrderId(long orderId);
	/**
	 * 更新服务评论信息
	 *
	 */
	void modifyShopComment(ShopComment shopComment);

	/**
	 * 添加服务评论信息
	 *
	 */
	void addShopComment(ShopComment shopComment);

	/**
	 * 删除服务评论信息
	 *
	 */
	void deleteShopComment(long shopCommentId);
}
