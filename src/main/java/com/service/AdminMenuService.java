package com.service;

import com.entity.AdminMenu;

import java.util.List;



public interface AdminMenuService {

    /**
     * 根据用户账号获取其菜单
     *
     * @param userName
     * @return
     */
    List<AdminMenu> getAdminMenu(String userName);

}
