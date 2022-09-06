package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.util.ImageHolder;
import com.entity.PersonInfo;

import java.util.List;

public interface PersonInfoService extends IService<PersonInfo> {
	/**
	 * 根据用户Id获取personInfo信息
	 *
	 */
	PersonInfo getPersonInfoByUserId(long userId);

	PersonInfo getPersonInfoByPhone(String phone);

	/**
	 * 根据查询条件分页返回用户信息列表
	 *
	 */
	List<PersonInfo> getPersonInfoList(PersonInfo personInfoCondition, int pageIndex, int pageSize, String sort, String order);

	/**
	 * 修改用户信息
	 *
	 */
	PersonInfo modifyPersonInfo(PersonInfo personInfo, ImageHolder thumbnail);

	/**
	 * 添加用户信息
	 *
	 */
	PersonInfo addPersonInfo(PersonInfo personInfo, ImageHolder thumbnail) ;

	/**
	 *上传头像
	 */
	PersonInfo uploadImg(long userId, ImageHolder profileImg);

	// 是否通过了实名认证
	Boolean isRealNameAuthenticated(Long userId);

}
