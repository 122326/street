package com.service.impl;

import com.dao.AdminMenuDao;
import com.dao.LocalAuthDao;
import com.dao.UserRoleDao;
import com.entity.AdminMenu;
import com.entity.LocalAuth;
import com.entity.UserRole;
import com.service.AdminMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
public class AdminMenuServiceImpl implements AdminMenuService {
    @Autowired
    LocalAuthDao localAuthDao;
    @Autowired
    UserRoleDao userRoleDao;
    @Autowired
    AdminMenuDao adminMenuDao;



    @Override
    public List<AdminMenu> getAdminMenu(String userName) {
        //创建菜单表
        List<AdminMenu> adminMenuList = new ArrayList<>();

        //获取当前用户的LocalAuthId
        LocalAuth localAuth= localAuthDao.queryLocalByUserName(userName);
        long localAuthId=localAuth.getLocalAuthId();

        //根据id获取用户的角色
        List<UserRole> userRoleList=userRoleDao.getRoleIdByLocalAuthId(localAuthId);
        for(UserRole userRole:userRoleList){
            adminMenuList.addAll(adminMenuDao.getAdminMenuByAuthId(userRole.getRoleId()));
        }

        return adminMenuList;

    }
}
