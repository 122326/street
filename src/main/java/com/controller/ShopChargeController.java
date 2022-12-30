package com.controller;

import com.dto.*;
import com.entity.OrderInfo;
import com.entity.ServiceInfo;
import com.entity.Shop;
import com.entity.ShopCharge;
import com.enums.OrderStateEnum;
import com.enums.ShopChargeStateEnum;
import com.service.*;
import com.util.HttpServletRequestUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/shopCharge")

public class ShopChargeController {
    @Autowired
    private ShopChargeService shopChargeService;
    @Autowired
    private OrderService OrderService;
    @Autowired
    private SService sService;
    @Autowired
    private ShopService shopService;
    private static final Logger log = LogManager.getLogger(ShopChargeController.class);


    //提交投诉信息
    @RequestMapping(value = "/addshopCharge", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "提交投诉信息")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", name = "createTime", value = "投诉时间", required = true, dataType = "Date")
    })
    private Map<String, Object> addshopCharge(@RequestBody @ApiParam(name = "ShopCharge", value = "传入json格式,要传orderId",
            required = true) ShopCharge shopCharge, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        Date createTime = HttpServletRequestUtil.getDate(request, "createTime");
        // 空值判断
        if (shopCharge != null) {
            try {
                //提交投诉
                shopCharge.setCreateTime(createTime);
                ShopChargeExecution ae = shopChargeService.addShopCharge(shopCharge);
                OrderInfo order= OrderService.getByOrderId(shopCharge.getOrderId());
                try {
                    //更新订单
                    OrderExecution a = OrderService.modifyOrder(order);
                    if (a.getState() == OrderStateEnum.SUCCESS.getState()) {

                    }
                    else {
                        modelMap.put("success", false);
                        modelMap.put("errMsg", a.getStateInfo());
                    }
                } catch (Exception e) {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", e.toString());
                    return modelMap;
                }
                if (ae.getState() == ShopChargeStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                    modelMap.put("shopChargeId", ae.getShopCharge().getShopChargeId());
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", ae.getStateInfo());
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入评论信息");
        }
        log.info("shopChargeInfo:"+shopCharge.toString());
        return modelMap;
    }

    @RequestMapping(value = "/addchargeImg", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "上传投诉相关图片")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "shopChargeId", value = "投诉id", required = true, dataType = "Long")})
    private Map<String, Object> addchargeImg(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        // 1.接收并转化相应的参数，包括投诉id以及图片信息
        Long shopChargeId = HttpServletRequestUtil.getLong(request, "shopChargeId");
//        ImageHolder chargeImg = new ImageHolder("", null);

        CommonsMultipartFile chargeImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            chargeImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("chargeImg");
        }
        if (chargeImg == null) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "缺少图片");
            return modelMap;
        }

        // 2.上传投诉图片
        ChargeImgExecution ce;
        try {
            ImageHolder chargeImgHolder = new ImageHolder(chargeImg.getOriginalFilename(), chargeImg.getInputStream());
            ce = shopChargeService.addChargeImg(shopChargeId, chargeImgHolder);
            if (ce.getState() == ShopChargeStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", ce.getStateInfo());
                System.out.println(ce.getStateInfo());
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
        }

        return modelMap;
    }

    //通过userId获取过去三个月投诉列表
    @RequestMapping(value = "/getshopChargelistbyuid", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "通过userId获取过去三个月所有投诉信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", required = true, dataType = "Long", example = "1")
    })
    private Map<String, Object> getShopChargeListByUId(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        Long userId = HttpServletRequestUtil.getLong(request, "userId");
        try {
            ShopChargeExecution se = shopChargeService.getByUserId2(userId);
            List<ShopCharge> shopChargeList = se.getShopChargeList();
            List<ServiceInfo> serviceList = new ArrayList<ServiceInfo>();
            List<Shop> shopList = new ArrayList<Shop>();

            for(int i = 0; i < shopChargeList.size(); ++ i) {
                OrderInfo order = OrderService.getByOrderId(shopChargeList.get(i).getOrderId());
                ServiceInfo service = sService.getByServiceId(order.getServiceId());
                serviceList.add(service);
                Shop shop = shopService.getByShopId(shopChargeList.get(i).getShopId());
                shopList.add(shop);
            }

            modelMap.put("shopChargeList", shopChargeList);
            modelMap.put("serviceList", serviceList);
            modelMap.put("shopList", shopList);
            modelMap.put("success", true);
        }
        catch(Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }
}
