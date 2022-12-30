package com.controller;

import com.entity.*;
import com.service.ShopCollectService;
import com.service.ShopService;
import com.vo.ShopCollectLog;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shopCollect")
public class ShopCollectController {


    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCollectService shopCollectService;

    @RequestMapping(value = "/getShopCollectByuserId/{userId}", method = RequestMethod.GET)
    @ApiOperation(value = "按userid搜索收藏商铺")
    private Map<String, Object> getShopCollectByuserId(
            @PathVariable(name = "userId") Long userId, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();


        // 空值判断
        if (userId != null) {
            try {
                List<ShopCollect> shopCollectList = shopCollectService.getShopCollectList(userId);
                ArrayList<Shop> shops = new ArrayList<>();
                for (ShopCollect s :
                        shopCollectList) {
                    Shop shop = shopService.getByShopId(s.getShopId());
                    shops.add(shop);
                }

                    modelMap.put("OrderList", shopCollectList);
                    modelMap.put("ShopList",shops);
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

    @RequestMapping(value = "/getUserCollectByshopId/{shopId}", method = RequestMethod.GET)
    @ApiOperation(value = "按shopId搜索收藏该商铺的用户")
    private Map<String, Object> getUserCollectByshopId(
            @PathVariable(name = "shopId") Long shopId, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();


        // 空值判断
        if (shopId != null) {
            try {
                List<ShopCollectLog> shopCollectList = shopCollectService.getCollectUsersByShopId(shopId,null,null);

                modelMap.put("userList", shopCollectList);
                modelMap.put("count",shopCollectList.size());
                modelMap.put("success", true);

            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "无shopId");
        }
        return modelMap;
    }



    @RequestMapping(value = "/addShopCollect/{userId}/{shopId}", method = RequestMethod.GET)
    @ApiOperation(value = "按userid和shopId收藏商铺")
    private Map<String, Object> addShopCollect(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "shopId") Long shopId,
            HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();


        // 空值判断
        if (userId != null && shopId !=null) {
            try {

                    Integer integer = shopCollectService.addShopCollect(userId, shopId);
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

    @RequestMapping(value = "/searchShopCollect/{userId}/{shopId}", method = RequestMethod.GET)
    @ApiOperation(value = "按userid和shopId搜索收藏商铺")
    private Map<String, Object> searchShopCollectByUserIdAndShopId(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "shopId") Long shopId,
            HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();


        // 空值判断
        if (userId != null && shopId !=null) {
            try {
                ShopCollect shopCollect = shopCollectService.queryByUserIdAndShopId(userId, shopId);
                if (shopCollect!=null){
                    modelMap.put("shopCollect",shopCollect);
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
    @RequestMapping(value = "/cancelShopCollect/{userId}/{shopId}", method = RequestMethod.GET)
    @ApiOperation(value = "按userid和shopId取消收藏商铺")
    private Map<String, Object> cancelShopCollectByUserIdAndShopId(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "shopId") Long shopId,
            HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();


        // 空值判断
        if (userId != null && shopId !=null) {
            try {
                ShopCollect shopCollect = shopCollectService.queryByUserIdAndShopId(userId, shopId);
                if (shopCollect==null){
                    modelMap.put("errMsg", "查询失败");

                }else {
                    Integer integer = shopCollectService.cancelShopCollect(shopCollect);
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

