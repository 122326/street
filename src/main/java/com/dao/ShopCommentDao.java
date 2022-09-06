package com.dao;

import com.entity.ShopComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShopCommentDao {
	/**
	 * 根据查询条件分页返回评价信息
	 * 可输入的条件有：店铺id，用户id,
	 *
	 * @param rowIndex 从第几行开始取数据
	 * @param pageSize 返回的条数
	 */
	List<ShopComment> queryShopCommentList(@Param("shopCommentCondition") ShopComment shopCommentCondition, @Param("rowIndex") int rowIndex,
                                                  @Param("pageSize") int pageSize, @Param("sort") String sort, @Param("order") String order);
	List<ShopComment> queryShopCommentList2(@Param("shopCommentCondition") ShopComment shopCommentCondition);

	/**
	 * 返回星级评分平均分
	 *
	 */
	float queryAvgStarRating(long shopId);

	/**
	 * 返回服务评分平均分
	 *
	 */
	float queryAvgServiceRating(long shopId);
	
	/**
	 * 通过shopCommentId查询评论
	 */
	ShopComment queryByShopCommentId(long shopCommentId);

	/**
	 * 通过orderId查询评论
	 */
	ShopComment queryByOrderId(long orderId);


	/**
	 * 添加评论
	 */
	void insertShopComment(ShopComment shopCommentInfo);

	/**
	 * 更新评论信息
	 */
	void updateShopComment(ShopComment shopCommentInfo);

	/**
	 * 删除评论信息
	 */
	void deleteShopComment(long shopCommentId);

}
