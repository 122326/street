package com.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.PersonInfo;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface PersonInfoDao extends BaseMapper<PersonInfo> {

	/**
	 * 根据查询条件分页返回用户信息列表
	 *
	 */
	List<PersonInfo> queryPersonInfoList(@Param("personInfoCondition") PersonInfo personInfoCondition,
                                         @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize,
                                         @Param("sort") String sort, @Param("order") String order);

	/**
	 * 通过用户Id查询用户
	 *
	 */
	PersonInfo queryPersonInfoByUserId(long userId);


	PersonInfo queryPersonInfoByPhone(String phone);
	PersonInfo queryPersonInfoByUserName(String userName);
	/**
	 * 添加用户信息
	 *
	 */
	void insertPersonInfo(PersonInfo personInfo);
	
	/**
	 * 修改用户信息
	 *
	 */
	int updatePersonInfo(PersonInfo personInfo);


}
