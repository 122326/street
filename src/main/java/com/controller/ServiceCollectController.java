package com.controller;

import com.entity.ServiceCollect;
import com.entity.ServiceInfo;
import com.service.*;
import com.vo.ServiceCollectLog;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/serviceCollect")
public class ServiceCollectController {

    @Autowired
    private SService sService;
    @Autowired
    private ServiceCollectService serviceCollectService;

    @RequestMapping(value = "/getServiceCollectByuserId/{userId}", method = RequestMethod.GET)
    @ApiOperation(value = "按userid搜索收藏的服务")
    private Map<String, Object> getServiceCollectByuserId(
            @PathVariable(name = "userId") Long userId, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();


        // 空值判断
        if (userId != null) {
            try {
                List<ServiceCollect> serviceCollectList = serviceCollectService.getServiceCollectList(userId);
                ArrayList<ServiceInfo> serviceInfos = new ArrayList<>();
                for (ServiceCollect s :
                        serviceCollectList) {
                    ServiceInfo serviceInfo = sService.getByServiceId(s.getServiceId());
                    serviceInfos.add(serviceInfo);
                }

                modelMap.put("ServiceCollectList", serviceCollectList);
                modelMap.put("ServiceInfoList",serviceInfos);
                modelMap.put("success", true);

            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "无userId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/getUsersCollectByServiceId/{serviceId}", method = RequestMethod.GET)
    @ApiOperation(value = "按userid搜索收藏的服务")
    private Map<String, Object> getUsersCollectByServiceId(
            @PathVariable(name = "serviceId") Long serviceId, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();


        // 空值判断
        if (serviceId != null) {
            try {
                List<ServiceCollectLog> users = serviceCollectService.getUsersByServiceId(serviceId);

                modelMap.put("userList", users);
                modelMap.put("count",users.size());
                modelMap.put("success", true);

            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;

            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "无serviceId");
        }
        return modelMap;
    }


    @RequestMapping(value = "/getCountByServiceId/{serviceId}", method = RequestMethod.GET)
    @ApiOperation(value = "按serviceid搜索收藏服务的用户数")
    private Map<String, Object> getServiceCollectByServiceId(
            @PathVariable(name = "serviceId") Long serviceId, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
//        try {
//            //先做身份验证
//            wechatAuthService.getWechatAuthByOpenId(JWT.unsign((String) request.getHeader("token"), UserCode2Session.class).getOpenId());
//        } catch (Exception e) {
//            modelMap.put("success", false);
//            modelMap.put("errMsg", "身份验证失败！");
//            return modelMap;
//        }

        // 空值判断
        if (serviceId != null) {
            try {
                Integer count = serviceCollectService.getServiceCollectCount(serviceId);

                modelMap.put("count", count);

                modelMap.put("success", true);

            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "无userId");
        }
        return modelMap;
    }


    @RequestMapping(value = "/addServiceCollect/{userId}/{serviceId}", method = RequestMethod.GET)
    @ApiOperation(value = "按userid和shopId收藏商铺")
    private Map<String, Object> addServiceCollect(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "serviceId") Long serviceId,
            HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();


        // 空值判断
        if (userId != null && serviceId !=null) {
            try {

                Integer integer = serviceCollectService.addServiceCollect(userId, serviceId);
                if (integer > 0) {
                    modelMap.put("row", integer);
                    modelMap.put("success", true);
                } else {
                    modelMap.put("errMsg", "添加失败");
                }

            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "无必要信息");
        }
        return modelMap;
    }

    @RequestMapping(value = "/searchServiceCollect/{userId}/{serviceId}", method = RequestMethod.GET)
    @ApiOperation(value = "按userid和shopId搜索收藏商铺")
    private Map<String, Object> searchShopCollectByUserIdAndShopId(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "serviceId") Long serviceId,
            HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();


        // 空值判断
        if (userId != null && serviceId !=null) {
            try {
                ServiceCollect serviceCollect = serviceCollectService.queryByUserIdAndServiceId(userId, serviceId);
                if (serviceCollect!=null){
                    modelMap.put("serviceCollect",serviceCollect);
                    modelMap.put("success", true);
                }else {
                    modelMap.put("errMsg", "查询失败");
                }

            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "无必要信息");
        }
        return modelMap;
    }
    @RequestMapping(value = "/cancelServiceCollect/{userId}/{serviceId}", method = RequestMethod.GET)
    @ApiOperation(value = "按userid和shopId取消收藏商铺")
    private Map<String, Object> cancelShopCollectByUserIdAndShopId(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "serviceId") Long serviceId,
            HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();


        // 空值判断
        if (userId != null && serviceId !=null) {
            try {
                ServiceCollect serviceCollect = serviceCollectService.queryByUserIdAndServiceId(userId, serviceId);
                if (serviceCollect==null){
                    modelMap.put("errMsg", "查询失败");

                }else {
                    Integer integer = serviceCollectService.cancelServiceCollect(serviceCollect);
                    if (integer>0){
                        modelMap.put("row",integer);
                        modelMap.put("success", true);
                        modelMap.put("message","取消收藏成功");
                    }
                }

            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "无必要信息");
        }
        return modelMap;
    }
}
