package com.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.Properties;

public class PathUtil {
    private static String seperator = System.getProperty("file.separator");

    public static String getImgBasePath() {
        try {
            Resource resource = new ClassPathResource("application.properties");
            Properties props = PropertiesLoaderUtils.loadProperties(resource);
            String uploadPath = props.getProperty("upload-path");
            return uploadPath.startsWith("file:") ? uploadPath.substring(5) : uploadPath;
        } catch (Exception e) {
            System.out.println("————读取配置文件：" + "application.properties" + "出现异常，读取失败————");
            e.printStackTrace();
        }
        return null;
    }

    public static String getShopImgPath(long shopId) {
        String imagePath = "/upload/images/item/shop/shopImg/" + shopId + "/";
        return imagePath.replace("/", seperator);
    }

    public static String getChargeImgPath(long chargeId) {
        String imagePath = "/upload/images/item/shop/chargeImg/" + chargeId + "/";
        return imagePath.replace("/", seperator);
    }

    public static String getShopBusinessLicenseImgPath(long shopId) {
        String imagePath = "/upload/images/item/shop/businessLicenseImg/" + shopId + "/";
        return imagePath.replace("/", seperator);
    }

    public static String getShopProfileImgPath(long shopId) {
        String imagePath = "/upload/images/item/shop/profileImg/" + shopId + "/";
        return imagePath.replace("/", seperator);
    }

    public static String getuserProfileImgPath(long userId) {
        String imagePath = "/upload/images/item/personInfo/profileImg/" + userId + "/";
        return imagePath.replace("/", seperator);
    }

    public static String getAppealImgPath(long appealId) {
        String imagePath = "/upload/images/item/appeal/appealImg/" + appealId + "/";
        return imagePath.replace("/", seperator);
    }

    public static String getServiceImgPath(long serviceId) {
        String imagePath = "/upload/images/item/shop/serviceImg/" + serviceId + "/";
        return imagePath.replace("/", seperator);
    }
}
