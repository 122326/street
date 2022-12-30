package com.dao;


import com.entity.AppealImg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AppealImgDao {

	/**
	 * 添加求助详情图片
	 *
	 * @param appealImg
	 * @return
	 */
	int insertAppealImg(AppealImg appealImg);

	/**
	 * 删除指定求助下的所有详情图
	 *
	 * @param appealId
	 * @return
	 */
	int deleteAppealImgByAppealId(long appealId);

	/**
	 * 获取求助图片
	 *
	 * @param appealImgId
	 * @return
	 */
	AppealImg getAppealImg(long appealImgId);
	/**
	 * 根据求助id获取求助图片
	 *
	 * @param appealId
	 * @return
	 */
	List<AppealImg> getAppealImgList(long appealId);

	/**
	 * 删除指定求助图片
	 *
	 * @param appealImgId
	 * @return
	 */
	int deleteAppealImgById(long appealImgId);

	/**
	 * 获取求助图片总数
	 *
	 * @param appealImgCondition
	 * @return
	 */
	int queryAppealImgCount(@Param("appealImgCondition") AppealImg appealImgCondition);

	/**
	 * 分页查询求助图片
	 *
	 * @param appealImgCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<AppealImg> queryAppealImgList(@Param("appealImgCondition") AppealImg appealImgCondition,
									   @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize
			, @Param("sort") String sort, @Param("order") String order);
}
