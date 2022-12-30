package com.service;




import com.vo.SoucoinLog;
import com.vo.SoucoinLogSummary;

import java.util.Date;
import java.util.List;

// 用于管理搜币充值业务的服务层接口
public interface SouCoinService {

    // 用于修改某个用户的搜币数量
    void changeSouCoin(Integer userID, Integer souCoin);

    // 用于添加一条搜币流水
    void addSouCoinLog(Integer userID, Integer souCoin, String method, String bankName, String cardNumber);

    // 搜币充值
    void topUpSouCoin(Integer userID, Integer souCoin, String method, String bankName, String cardNumber);

    // 搜币提现
    void withdrawalSouCoin(Integer userID, Integer souCoin, String method, String bankName, String cardNumber);

    // 用于列出搜币的记录
    List<SoucoinLog> listLogs(Integer userID, String userName,
                              Date startTime, Date endTime,
                              Integer page,
                              Integer rows, String sort,
                              String order);

    // 获得搜币流水的总条数
    Long getCount(Integer userID, String userName, Date startTime, Date endTime);

    // 查询所有用户搜币总额以及当前条件下的充值总金额和提现总金额
    SoucoinLogSummary getSummary(Integer userID, String userName, Boolean isAllUsersSouCoin,
                                 Date startTime, Date endTime);

}


