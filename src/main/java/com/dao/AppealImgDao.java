package com.dao;


import com.entity.AppealImg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AppealImgDao {
	/**
	 * 根据查询条件分页返回求助图片
	 *
	 */
	List<AppealImg> queryAppealImgList(@Param("appealImgCondition") AppealImg appealImgCondition,
									   @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize,
									   @Param("sort") String sort, @Param("order") String order);

	/**
	 * 根据求助图片Id获取求助图片
	 *
	 */
	AppealImg getAppealImg(long appealImgId);

	/**
	 * 根据求助Id获取求助图片列表
	 */
	List<AppealImg> getAppealImgList(long appealId);

	/**
	 * 添加求助详情图片
	 */
	void insertAppealImg(AppealImg appealImg);

	/**
	 * 删除求助图片
	 *
	 */
	void deleteAppealImgById(long appealImgId);

}
