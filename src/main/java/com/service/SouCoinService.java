package com.service;



import com.entity.Soucoin;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

// 用于管理搜币充值业务的服务层接口
public interface SouCoinService {

    // 用户的搜币数量变化
    void changeSouCoin(long userID, Integer souCoin);

    // 添加搜币流水
    void addSouCoinLog(long userID, Integer souCoin, String method, String bankName, String cardNumber);

    // 搜币充值
    void topUpSouCoin(long userID, Integer souCoin, String method,
                      String bankName, String cardNumber);

    // 搜币提现
    void withdrawalSouCoin(long userID, Integer souCoin, String method,
                           String bankName, String cardNumber);

    // 列出搜币的记录
    List<Soucoin> listLogs(long userID,  Date startTime, Date endTime,
                           Integer rowsIndex, Integer pageSize, String sort, String order);


}
