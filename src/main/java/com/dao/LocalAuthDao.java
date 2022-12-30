package com.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.entity.LocalAuth;

@Mapper
public interface LocalAuthDao {
	/**
	 * 获取所有用户名
	 * @return
	 */
	List<String> queryUserName();

	/**
	 * 通过用户名获取账号和密码
	 *
	 * @param userName
	 * @return
	 */
	LocalAuth queryLocalByUserName(@Param("userName") String userName);

	/**
	 * 通过账号和密码查询对应信息，登录用
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	LocalAuth queryLocalByUserNameAndPwd(@Param("userName") String userName, @Param("password") String password);
	LocalAuth querySuperByUserNameAndPwd(@Param("userName") String userName, @Param("password") String password);
	/**
	 * 通过用户Id查询对应localauth
	 * 
	 * @param userId
	 * @return
	 */
	LocalAuth queryLocalByUserId(@Param("userId") long userId);

	/**
	 * 分页查询本地账号
	 * 
	 * @param localAuthCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<LocalAuth> queryLocalList(@Param("LocalAuthCondition")LocalAuth localAuthCondition,
								   @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize,
								   @Param("sort") String sort, @Param("order") String order);

	/**
	 * 查询账号总数
	 * 
	 * @return
	 */
	int queryLocalCount(@Param("LocalAuthCondition")LocalAuth localAuthCondition);

	/**
	 * 添加平台账号
	 * 
	 * @param localAuth
	 * @return
	 */
	int insertLocalAuth(LocalAuth localAuth);

	/**
	 * 通过userId,username,password更改密码
	 * 
	 * @param userId
	 * @param userName
	 * @param password
	 * @param newPassword
	 * @param lastEditTime
	 * @return
	 */
	int updateLocalAuth(@Param("userId") Long userId, @Param("userName") String userName,
			@Param("password") String password, @Param("newPassword") String newPassword,
			@Param("lastEditTime") Date lastEditTime);
}
