package com.util;

import com.auth0.jwt.internal.org.apache.commons.codec.digest.DigestUtils;

/**
 * 这次开发不采用MD5加密
 */
public class MD5Util {

    /**
     * 加密方法
     * @param src
     * @return
     */
    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    //固定盐
    private static final String salt = "1a2b3c4d";

    /**
     * 将用户输入的明文密码与固定盐进行拼装后再进行MD5加密
     * @param inputPass
     * @return
     */
    public static String getMD5(String inputPass) {
        String str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);
        System.out.println(str);
        return md5(str);
    }


}