package com.controller;

import com.entity.Soucoin;
import com.service.SouCoinService;
import com.service.UsersInformService;
import com.util.HttpServletRequestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    private UsersInformService usersInformService;

    @Value("${exchange-rate}")
    private Double exchangeRate;



    //充值搜币
    @PostMapping("/topupsoucoin")
    @ApiOperation("充值搜币，会记录到搜币流水中")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userID", value = "用户ID", required = true, dataType = "long", example = "3"),
            @ApiImplicitParam(paramType = "query", name = "souCoin", value = "充值的数量", required = true, dataType = "Integer", example = "20"),
            @ApiImplicitParam(paramType = "query", name = "method", value = "充值或提现", required = true, dataType = "String", example = "微信支付"),
            @ApiImplicitParam(paramType = "query", name = "bankName", value = "充值的银行", required = true, dataType = "String", example = "工商银行"),
            @ApiImplicitParam(paramType = "query", name = "cardNumber", value = "充值的银行卡号", required = true, dataType = "String", example = "612345678901222"),
            })
    private Map<String, Object> topUpSouCoin(@RequestBody Map<String, Object> params) {
        Map<String, Object> modelMap = new HashMap<>();
        Integer souCoin = (Integer) params.get("souCoin");
        // 检查充值数量是否为正
        if (souCoin <= 0) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "充值数量错误！");
            return modelMap;
        }
        long userID = (long) params.get("userID");
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
    @PostMapping("/withdrawalsoucoin")
    @ApiOperation("搜币提现，会记录到搜币流水中")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userID", value = "用户ID", required = true, dataType = "long", example = "3"),
            @ApiImplicitParam(paramType = "query", name = "souCoin", value = "提现的数量", required = true, dataType = "Integer", example = "20"),
            @ApiImplicitParam(paramType = "query", name = "method", value = "充值或提现", required = true, dataType = "String", example = "微信支付"),
            @ApiImplicitParam(paramType = "query", name = "bankName", value = "提现的银行", required = true, dataType = "String", example = "工商银行"),
            @ApiImplicitParam(paramType = "query", name = "cardNumber", value = "提现的银行卡号", required = true, dataType = "String", example = "612345678901222"),
            })
    private Map<String, Object> withdrawalSouCoin(@RequestBody Map<String, Object> params) {
        Map<String, Object> modelMap = new HashMap<>();
        Integer souCoin = (Integer) params.get("souCoin");
        // 检查提现数量是否为正
        if (souCoin <= 0) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "身份验证失败！");
            return modelMap;
        }
        long userID = (long) params.get("userID");
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
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", required = true, dataType = "long", example = "3"),
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true, dataType = "Integer", example = "1"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "页面大小", required = true, dataType = "Integer", example = "20"),
            @ApiImplicitParam(paramType = "query", name = "sort", value = "按照什么来排序", required = false, dataType = "String", example = "time"),
            @ApiImplicitParam(paramType = "query", name = "order", value = "排序方式", required = false, dataType = "String", example = "DESC"),
            @ApiImplicitParam(paramType = "query", name = "starttime", value = "时间区间（开始）", required = false, dataType = "Date"),
            @ApiImplicitParam(paramType = "query", name = "endtime", value = "时间区间（截止）", required = false, dataType = "Date")})
    private Map<String, Object> listlogs(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        long userId = HttpServletRequestUtil.getLong(request,"userId");
        Integer pageIndex = HttpServletRequestUtil.getInteger(request,"pageIndex");
        Integer pageSize = HttpServletRequestUtil.getInteger(request,"pageSize");
        String sort = HttpServletRequestUtil.getString(request,"sort");
        String order = HttpServletRequestUtil.getString(request,"order");
        Date starttime = HttpServletRequestUtil.getDate(request,"starttime");
        Date endtime = HttpServletRequestUtil.getDate(request,"endtime");
        int rowsIndex=(pageIndex-1)*pageSize;
        if (pageIndex>0&&pageSize>0) {
            if (userId >0) {
                List<Soucoin> sc= souCoinService.listLogs(userId, starttime, endtime, rowsIndex, pageSize, sort, order);
                modelMap.put("success", true);
                modelMap.put("soucoinlist",sc);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "请输入合理的userId!");
            }
        }else{
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入正确分页信息！");
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
