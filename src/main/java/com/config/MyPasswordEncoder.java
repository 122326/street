package com.config;

import com.util.MD5Util;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Author:kai
 * Date:2022/9/13 15:38
 * 重写security内置的加密方法，使用MD5加密
 * 已弃用
 */
public class MyPasswordEncoder implements PasswordEncoder {
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(MD5Util.getMD5((String) rawPassword));
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return MD5Util.getMD5((String) rawPassword);
    }
}
