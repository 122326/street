package com.service;


import com.entity.PersonInfo;
import com.entity.UsersInform;

public interface UsersInformService {
    /**
     * 通过openId查找平台对应的帐号
     *
     */
    UsersInform getUsersInformByOpenId(String openId);



    /**
     * 更新用户信息
     *
     */
    void updatePersonInfo(UsersInform wechatAuth, PersonInfo personInfo) throws java.lang.Exception;


    /**
     * 通过用户ID获取微信账号
     *
     */
    UsersInform getUsersInformByUserId(long userId);

}