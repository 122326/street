package com.suppercontroller;

import com.dto.ConstantForSuperAdmin;
import com.entity.ShopCollect;
import com.service.ServiceCollectService;
import com.service.ShopCollectService;
import com.vo.ShopCollectLog;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/superadmin")
public class CollectController {
    @Autowired
    private ShopCollectService shopCollectService;
    @Autowired
    private ServiceCollectService serviceCollectService;

    @GetMapping("/list-shop-collect-users")
    @ResponseBody
    public Map<String, Object> listCollectUsers(@Nullable @RequestParam Long shopId,
                                               @RequestParam Integer page,
                                               @RequestParam Integer rows) {
        Map<String, Object> modelMap = new HashMap<>();
        List<ShopCollectLog> collects = shopCollectService.getCollectUsersByShopId(shopId, (page - 1) * rows, rows);
//        List<SoucoinLog> res = souCoinService.listLogs(userID, userName, start, end, (page - 1) * rows, rows, sort, order);
//        Long count = souCoinService.getCount(userID, userName, start, end);
        modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, collects);
        modelMap.put(ConstantForSuperAdmin.TOTAL, collects.size());
        return modelMap;
    }

    @RequestMapping(value = "/cancelShopCollect/{userId}/{shopId}", method = RequestMethod.GET)
    @ResponseBody
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

    @RequestMapping(value = "/addShopCollect/{userId}/{shopId}", method = RequestMethod.GET)
    @ResponseBody
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
}
