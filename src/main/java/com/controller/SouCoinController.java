package com.controller;

import com.service.SouCoinService;
import com.suppercontroller.SoucoinSupperController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 这个Controller专门用来处理搜币相关的操作
 */
@RestController
@RequestMapping("/soucoin")
@Api("SouCoinController|专门用来处理搜币相关的操作的控制器")
public class SouCoinController {
    @Autowired
    private SouCoinService souCoinService;


    @Autowired
    private SoucoinSupperController soucoinSupperController;

    @Value("${exchange-rate}")
    private Double exchangeRate;

    //修改搜币数量
    @PostMapping("/update")
    @ApiOperation("根据userID修改搜币数量，不会记录到搜币流水中")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userID", value = "用户ID", required = true, dataType = "Integer", example = "3"),
            @ApiImplicitParam(paramType = "query", name = "souCoin", value = "修改的数量（可正可负）", required = true, dataType = "Integer", example = "-20"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "用于身份认证的令牌", required = true, dataType = "String")})
    private Map<String, Object> changeSouCoin(@RequestBody Map<String, Object> params) {
        Map<String, Object> modelMap = new HashMap<>();
        Integer userID = (Integer) params.get("userID");
        Integer souCoin = (Integer) params.get("souCoin");
        if (userID != null) {
            souCoinService.changeSouCoin(userID, souCoin);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "用户ID不能为空");
        }
        return modelMap;
    }

    //充值搜币
    @PostMapping("/top-up-soucoin")
    @ApiOperation("充值搜币，会记录到搜币流水中")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userID", value = "用户ID", required = true, dataType = "Integer", example = "3"),
            @ApiImplicitParam(paramType = "query", name = "souCoin", value = "充值的数量", required = true, dataType = "Integer", example = "20"),
            @ApiImplicitParam(paramType = "query", name = "method", value = "充值的方式", required = true, dataType = "String", example = "微信支付"),
            @ApiImplicitParam(paramType = "query", name = "bankName", value = "充值的银行", required = true, dataType = "String", example = "工商银行"),
            @ApiImplicitParam(paramType = "query", name = "cardNumber", value = "充值的银行卡号", required = true, dataType = "String", example = "612345678901222"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "用于身份认证的令牌", required = true, dataType = "String")})
    private Map<String, Object> topUpSouCoin(@RequestBody Map<String, Object> params) {
        Map<String, Object> modelMap = new HashMap<>();
        Integer souCoin = (Integer) params.get("souCoin");
        // 检查充值数量是否为正
        if (souCoin <= 0) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "身份验证失败！");
            return modelMap;
        }
        Integer userID = (Integer) params.get("userID");
        String method = (String) params.get("method");
        String bankName = (String) params.get("bankName");
        String cardNumber = (String) params.get("cardNumber");
        try {
            souCoinService.topUpSouCoin(userID, souCoin, method, bankName, cardNumber);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "搜币充值失败！");
            return modelMap;
        }
        modelMap.put("success", true);
        return modelMap;
    }

    //搜币提现
    @PostMapping("/withdrawal-soucoin")
    @ApiOperation("搜币提现，会记录到搜币流水中")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userID", value = "用户ID", required = true, dataType = "Integer", example = "3"),
            @ApiImplicitParam(paramType = "query", name = "souCoin", value = "提现的数量", required = true, dataType = "Integer", example = "20"),
            @ApiImplicitParam(paramType = "query", name = "method", value = "提现的方式", required = true, dataType = "String", example = "微信支付"),
            @ApiImplicitParam(paramType = "query", name = "bankName", value = "提现的银行", required = true, dataType = "String", example = "工商银行"),
            @ApiImplicitParam(paramType = "query", name = "cardNumber", value = "提现的银行卡号", required = true, dataType = "String", example = "612345678901222"),
            @ApiImplicitParam(paramType = "query", name = "token", value = "用于身份认证的令牌", required = true, dataType = "String")})
    private Map<String, Object> withdrawalSouCoin(@RequestBody Map<String, Object> params) {
        Map<String, Object> modelMap = new HashMap<>();
        Integer souCoin = (Integer) params.get("souCoin");
        // 检查提现数量是否为正
        if (souCoin <= 0) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "身份验证失败！");
            return modelMap;
        }
        Integer userID = (Integer) params.get("userID");
        String method = (String) params.get("method");
        String bankName = (String) params.get("bankName");
        String cardNumber = (String) params.get("cardNumber");
        try {
            souCoinService.withdrawalSouCoin(userID, souCoin, method, bankName, cardNumber);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "搜币提现失败！");
            return modelMap;
        }
        modelMap.put("success", true);
        return modelMap;
    }

    //列出充值或提现记录
    @PostMapping("/listlogs")
    @ApiOperation("根据userID列出搜币流水")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userID", value = "用户ID", required = true, dataType = "Integer", example = "3"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "当前处于第几页", required = true, dataType = "Integer", example = "1"),
            @ApiImplicitParam(paramType = "query", name = "rows", value = "每一页显示多少条数据", required = true, dataType = "String", example = "20"),
            @ApiImplicitParam(paramType = "query", name = "sort", value = "按照什么来排序", required = true, dataType = "String", example = "time"),
            @ApiImplicitParam(paramType = "query", name = "order", value = "排序方式", required = true, dataType = "String", example = "DESC")})
    private Map<String, Object> listlogs(@RequestBody Map<String, Object> params) {
        Map<String, Object> modelMap = new HashMap<>();
        Integer userID = (Integer) params.get("userID");
        Integer page = (Integer) params.get("page");
        Integer rows = (Integer) params.get("rows");
        String sort = (String) params.get("sort");
        String order = (String) params.get("order");
        if (userID != null) {
            // 借用管理页面的控制器中已经写好的方法来使用
            modelMap = soucoinSupperController.listSouCoinLogs(userID, null, null, null, page, rows, sort, order);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "用户ID不能为空");
        }
        return modelMap;
    }

    // 获得人民币搜币的兑换比例，1人民币能兑换多少搜币
    @GetMapping("/get-exchange-rate")
    @ApiOperation("获得人民币搜币的兑换比例，1人民币能兑换多少搜币")
    private Double getExchangeRate() {
        return this.exchangeRate;
    }

}
