package com.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface UserRoleDao extends BaseMapper<UserRole> {

    List<UserRole> getRoleIdByLocalAuthId(@Param("localAuthId") long localAuthId);
}
