package com.suppercontroller;

import com.dto.ConstantForSuperAdmin;
import com.entity.ServiceCollect;
import com.service.ServiceCollectService;
import com.vo.ServiceCollectLog;
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
public class SVCollectController {
    @Autowired
    private ServiceCollectService serviceCollectService;

    @GetMapping("/list-service-collect-users")
    @ResponseBody
    public Map<String, Object> listServiceCollectUsers(@Nullable @RequestParam Long serviceId,
                                                @RequestParam Integer page,
                                                @RequestParam Integer rows) {
        Map<String, Object> modelMap = new HashMap<>();
        List<ServiceCollectLog> collects = serviceCollectService.getCollectUsersByServiceId(serviceId, (page - 1) * rows, rows);
//        List<SoucoinLog> res = souCoinService.listLogs(userID, userName, start, end, (page - 1) * rows, rows, sort, order);
//        Long count = souCoinService.getCount(userID, userName, start, end);
        modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, collects);
        modelMap.put(ConstantForSuperAdmin.TOTAL, collects.size());
        return modelMap;
    }

    @RequestMapping(value = "/cancelServiceCollect/{userId}/{ServiceId}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "按userid和ServiceId取消收藏")
    private Map<String, Object> cancelServiceCollectByUserIdAndShopId(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "ServiceId") Long serviceId,
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

    @RequestMapping(value = "/addServiceCollect/{userId}/{serviceId}", method = RequestMethod.GET)
    @ResponseBody
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
}
