package com.dao;


import com.entity.Soucoin;
import com.vo.SoucoinLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface SouCoinDao {
    // 将用户的搜币增加souCoin（该值可为负数，代表倒扣）个
    void changeSouCoin(@Param("userID") Integer userID, @Param("souCoin") Integer souCoin);

    // 添加一条用户充值和提现搜币流水
    void addSouCoinLog(@Param("userID") Integer userID, @Param("souCoin") Integer souCoin,
                       @Param("method") String method,
                       @Param("bankName") String bankName, @Param("cardNumber") String cardNumber);

    // 列出用户的搜币充值和提现记录
    List<SoucoinLog> listLogs(@Param("userID") Integer userID, @Param("userName") String userName,
                              @Param("startTime") Date startTime, @Param("endTime") Date endTime,
                              @Param("page") Integer page,
                              @Param("rows") Integer rows, @Param("sort") String sort,
                              @Param("order") String order);

    // 获得用户的搜币充值和提现记录的总条数
    Long getCount(@Param("userID") Integer userID, @Param("userName") String userName,
                  @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    // 查询当前条件下的充值总金额和提现总金额
    List<Long> getSummary(@Param("userID") Integer userID, @Param("userName") String userName,
                          @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
