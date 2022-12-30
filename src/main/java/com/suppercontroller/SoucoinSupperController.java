package com.suppercontroller;

import com.dto.ConstantForSuperAdmin;
import com.service.SouCoinService;
import com.sun.istack.internal.NotNull;
import com.util.NameUtil;
import com.util.SQLUtil;
import com.vo.SoucoinLog;
import com.vo.SoucoinLogSummary;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/superadmin")
public class SoucoinSupperController {
    private final SouCoinService souCoinService;

    public SoucoinSupperController(SouCoinService souCoinService) {
        this.souCoinService = souCoinService;
    }

    @GetMapping("/list-soucoin-logs")
    public Map<String, Object> listSouCoinLogs(@Nullable @RequestParam Integer userID, @Nullable @RequestParam String userName,
                                               @Nullable @RequestParam String startTime, @Nullable @RequestParam String endTime,
                                               @RequestParam Integer page, @RequestParam Integer rows,
                                               @Nullable @RequestParam String sort, @Nullable @RequestParam String order) {
        Map<String, Object> modelMap = new HashMap<>();
        if (sort != null && SQLUtil.sqlValidate(sort) || order != null && SQLUtil.sqlValidate(order)) {
            modelMap.put("Warning:", "您的行为已被记录，请勿再次操作，否则报警处理!");
            return modelMap;
        }
        if (sort != null) {
            sort = NameUtil.HumpToUnderline(sort);
        }
        Date start = null;
        Date end = null;
        if (startTime != null) {
            start = new Date(Long.parseLong(startTime));
        }
        if (endTime != null) {
            end = new Date(Long.parseLong(endTime));
        }
        List<SoucoinLog> res = souCoinService.listLogs(userID, userName, start, end, (page - 1) * rows, rows, sort, order);
        Long count = souCoinService.getCount(userID, userName, start, end);
        modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, res);
        modelMap.put(ConstantForSuperAdmin.TOTAL, count);
        return modelMap;
    }

    @GetMapping("/get-soucoin-summary")
    public SoucoinLogSummary getSouCoinSummary(@Nullable @RequestParam Integer userID, @Nullable @RequestParam String userName,
                                               @NotNull @RequestParam Boolean isAllUsersSouCoin,
                                               @Nullable @RequestParam String startTime, @Nullable @RequestParam String endTime) {
        Date start = null;
        Date end = null;
        if (startTime != null) {
            start = new Date(Long.parseLong(startTime));
        }
        if (endTime != null) {
            end = new Date(Long.parseLong(endTime));
        }

        return souCoinService.getSummary(userID, userName, isAllUsersSouCoin, start, end);
    }
}