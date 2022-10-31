package com.service.impl;

import com.dao.PersonInfoDao;
import com.entity.PersonInfo;
import com.service.PersonInfoService;
import com.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * 实现UserDetailService接口，实现自定义登陆逻辑
 * 重写loadUserByUsername方法
 * Author:kai
 * Date:2022/9/11 15:15
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PersonInfoDao personInfoDao;

    @Autowired
    private PersonInfoService personInfoService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        PersonInfo user;
        //1、在service中自定义登陆，根据用户名获取用户信息
        log.info("根据电话查找结果2");
        if(redisUtil.hasKey("userInfo_"+ userName)){
            //缓存中存在用户信息，直接从redis中取
            user = (PersonInfo)redisUtil.getValue("userInfo_" + userName);
            redisUtil.expire("userInfo_" + userName, 10000);
        }else{
            user = personInfoService.getPersonInfoByPhone(userName);
            if(user == null){
                throw new UsernameNotFoundException("用户名或者密码错误");
            }
            redisUtil.setValueTime("userInfo_" + userName, user, 300);
        }

        return user;
    }
}

