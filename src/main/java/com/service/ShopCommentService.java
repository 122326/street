package com.service;


import com.dto.ShopCommentExecution;
import com.entity.Shop;
import com.entity.ShopComment;
import com.exceptions.ShopCommentOperationException;

import java.util.List;

public interface ShopCommentService {
	/**
	 * 根据shopCommentCondition分页返回相应评论列表
	 *
	 * @param shopCommentCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public ShopCommentExecution getShopCommentList(ShopComment shopCommentCondition, int pageIndex, int pageSize, String sort, String order);

	/**
	 * 通过shopId获取评论信息
	 *
	 * @param shopId
	 * @return
	 */
	public ShopCommentExecution getByShopId(long shopId, int pageIndex, int pageSize);
	public ShopCommentExecution getByShopId2(long shopId);
	/**
	 * 通过shopId获取服务评分，星级评分平均分
	 *
	 * @param shopId
	 * @return
	 */
	public Shop getAvgByShopId(long shopId);
	/**
	 * 通过userId获取评论信息
	 *
	 * @param userId
	 * @return
	 */
	public ShopCommentExecution getByUserId(long userId, int pageIndex, int pageSize);
	public ShopCommentExecution getByUserId2(long userId);
	/**
	 * 通过评论Id获取评论信息
	 *
	 * @param shopCommentId
	 * @return
	 */
	public ShopComment getByShopCommentId(long shopCommentId);
	/**
	 * 通过orderId获取评论信息 和评论id是一一对应
	 *
	 * @param orderId
	 * @return
	 */
	public ShopComment getByOrderId(long orderId);
	/**
	 * 更新服务评论信息
	 *
	 * @param shopComment
	 *
	 * @return
	 * @throws ShopCommentOperationException
	 */
	public ShopCommentExecution modifyShopComment(ShopComment shopComment) throws ShopCommentOperationException;

	/**
	 * 添加服务评论信息
	 *
	 * @param shopComment
	 * @return
	 * @throws ShopCommentOperationException
	 */
	public ShopCommentExecution addShopComment(ShopComment shopComment) throws ShopCommentOperationException;
	/**
	 * 删除服务评论信息
	 *
	 * @return
	 * @throws ShopCommentOperationException
	 */
	public ShopCommentExecution deleteShopComment(long shopCommentId) throws ShopCommentOperationException;
}
