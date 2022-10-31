package com.util;

public class SQLUtil {

    /**
     * 参数校验
     *
     * @param str
     * 返回真代表有人想搞破坏
     */
    public static boolean sqlValidate(String str) {
        str = str.toLowerCase();//统一转为小写
        String badStr = "select|update|and|or|delete|insert|truncate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute|table";
        String[] badStrs = badStr.split("\\|");
        for (String s : badStrs) {
            //循环检测，判断在请求参数当中是否包含SQL关键字
            if (str.contains(s)) {
                return true;
            }
        }
        return false;
    }
}
