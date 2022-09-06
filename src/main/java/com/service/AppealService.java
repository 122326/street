package com.service;


import com.util.ImageHolder;
import com.entity.Appeal;
import com.entity.AppealImg;


import java.util.List;

public interface AppealService {

	/**
	 * 根据appealCondition返回相应求助列表
	 *
	 */
	List<Appeal> getAppealList(
			Appeal appealCondition, int pageIndex, int pageSize,
			String sort, String order);

	/**
	 * 根据经纬范围返回附近求助列表
	 *
	 */
	List<Appeal> getNearbyAppealList(float maxlat, float minlat, float maxlng, float minlng,
                                               String appealTitle);

	/**
	 * 通过求助Id获取求助信息
	 *
	 */
	Appeal getByAppealId(Long appealId);

	/**
	 * 更新求助信息，不包括对图片的处理
	 *
	 */
	Appeal modifyAppeal(Appeal appeal) ;

	/**
	 * 上传求助图片
	 *
	 */
	AppealImg uploadImg(Long appealId, ImageHolder appealImg, Long userId);

	/**
	 * 添加求助信息，不包括对图片的处理
	 *
	 */
	Appeal addAppeal(Appeal appeal);

	/**
	 * 求助完成之后的相关操作
	 *
	 */
	Appeal completeAppeal(Long appealId, Long helpId, Long appealUserId);

	/**
	 * 撤销求助
	 *
	 */

	void cancelAppeal(Long userId, Long appealId);


	/**
	 * 分页获取求助图片
	 */
	List<AppealImg> getAppealImgList(AppealImg appealImg,
							 int pageIndex, int pageSize, String sort, String order);

	/**
	 * 根据求助图片Id删除求助图片
	 *
	 */
	void delAppealImg(Long appealImgId);

	/**
	 * 添加求助图片
	 *
	 */
	AppealImg createAppealImg(Long appealId, ImageHolder appealImgHolder) ;


}
