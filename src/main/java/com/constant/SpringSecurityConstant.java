package com.constant;

/**
 * @Author 阿杰
 * @create 2021-04-25 11:28
 */
public class SpringSecurityConstant {

    /**
     * 放开权限校验的接口
     */
    public static final String[] WHITE_LIST = {


            //swagger相关的
            "/swagger-ui.html",
            "/doc.html",
            "/webjars/**",
            "/swagger-resources/**",
            "/v2/**",
            "/configuration/ui",
            "/configuration/security",

            //后端的
            "/PersonInfo/login",
            "/PersonInfo/addPersonInfo",

    };
}
