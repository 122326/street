package com.util;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;


/*
* 前端请求回传数据处理
*
* */
public class HttpServletRequestUtil {
	public static int getInt(HttpServletRequest request, String key) {
		try {
			return Integer.decode(request.getParameter(key));
		} catch (Exception e) {
			return -1;
		}
	}
	
	public static Integer getInteger(HttpServletRequest request, String key) {
		try {
			return Integer.decode(request.getParameter(key));
		} catch (Exception e) {
			return null;
		}
	}

	public static Long getLong(HttpServletRequest request, String key) {
		try {
			return Long.valueOf(request.getParameter(key));
		} catch (Exception e) {
			return null;
		}
	}

	public static float getFloat(HttpServletRequest request, String key) {
		try {
			return Float.valueOf(request.getParameter(key));
		} catch (Exception e) {
			return -1f;
		}
	}
	
	public static Double getDouble(HttpServletRequest request, String key) {
		try {
			return Double.valueOf(request.getParameter(key));
		} catch (Exception e) {
			return -1d;
		}
	}

	public static boolean getBoolean(HttpServletRequest request, String key) {
		try {
			return Boolean.valueOf(request.getParameter(key));
		} catch (Exception e) {
			return false;
		}
	}

	public static String getString(HttpServletRequest request, String key) {
		try {
			String result = request.getParameter(key);
			if (result != null) {
				result = result.trim();
			}
			if ("".equals(result)) {
				result = null;
			}
			return result;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Date getDate(HttpServletRequest request, String key) {
			String s = request.getParameter(key);
			//System.out.println("最先收到的生日：" + s);
		    if (StringUtils.isBlank(s)) {
		    	System.out.println("getDate的字符串是空的");
		    	return null;
		    }
			try {
				return new SimpleDateFormat("yyyy-MM-dd").parse(s);
			} catch (Exception e) {
				System.out.println("getDate的catch错的");
				System.out.println(e.toString());
				return null;
			}
	}
}
