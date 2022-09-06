package com.dao;

import com.entity.UsersInform;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UsersInformDao {


	/**
	 * 通过openId查询对应的WechatAuth
	 *
	 */
	UsersInform queryWechatByOpenId(@Param("openId") String openId);

	/**
	 * 添加微信账号
	 *
	 */
	int insertWechatAuth(UsersInform wechatAuth);

	/**
	 * 通过userID查询对应的WechatAuth
	 */
	UsersInform queryWechatByUserId(Long userId);


}
