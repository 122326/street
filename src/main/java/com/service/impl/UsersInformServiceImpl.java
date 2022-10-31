package com.service.impl;


import com.dao.UsersInformDao;

import com.entity.UsersInform;
import com.dao.PersonInfoDao;
import com.entity.PersonInfo;
import com.service.UsersInformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UsersInformServiceImpl implements UsersInformService {
    @Autowired
    private UsersInformDao usersInformDao;
    @Autowired
    private PersonInfoDao personInfoDao;

    @Override
    @Transactional
    public UsersInform getUsersInformByOpenId(String openId) {
        String key = openId;
        // 定义接收对象
        UsersInform wechatAuth;
        // 从数据库里面取出相应数据
        wechatAuth = usersInformDao.queryWechatByOpenId(openId);
        if (wechatAuth == null) {
            return null;
        }
        return wechatAuth;
    }



    @Override
    public void updatePersonInfo(UsersInform wechatAuth, PersonInfo personInfo) {
        wechatAuth.setUserId(personInfo.getUserId());
    }


    @Override
    public UsersInform getUsersInformByUserId(long userId) {
        return usersInformDao.queryWechatByUserId(userId);
    }

}
