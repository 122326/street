package com.dao;


import com.entity.AdminMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMenuDao {

    /**
     * 根据管理员Id获取管理员菜单
     *
     * @param roleId
     * @return
     */
    List<AdminMenu> getAdminMenuByAuthId(@Param("roleId") long roleId);

}
