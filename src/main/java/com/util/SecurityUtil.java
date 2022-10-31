package com.util;

import com.entity.PersonInfo;
import org.springframework.security.core.context.SecurityContextHolder;


public class SecurityUtil {

    /**
     * 获取用户名
     * @return
     */
    public static String getUsername() {
        try {
            return ((PersonInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取用户ID
     * @return
     */
    public static Long getUserId() {
        try {
            return ((PersonInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取用户信息
     * @return
     */
    public static PersonInfo getUserInfo() {
        try {
            return (PersonInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }


}
