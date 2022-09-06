package com.dao;


import com.entity.Soucoin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface SouCoinDao {
    /*
     * 搜币金额变化
     * 搜币增加souCoin（该值可为负数，代表倒扣）个
     * */
    void changeSouCoin(@Param("userID") long userID, @Param("souCoin") Integer souCoin);

    // 添加搜币流水
    void addSouCoinLog(@Param("userID") long userID, @Param("souCoin") Integer souCoin,
                       @Param("method") String method,
                       @Param("bankName") String bankName, @Param("cardNumber") String cardNumber);

    // 根据查询条件返回搜币流水
    List<Soucoin> listLogs(@Param("userID") long userID,
                           @Param("startTime") Date startTime, @Param("endTime") Date endTime,
                           @Param("rowsIndex") Integer rowsIndex,
                           @Param("pageSize") Integer pageSize, @Param("sort") String sort,
                           @Param("order") String order);

    // 查询某条件下的充值总金额和提现总金额
    List<Long> getSummary(@Param("userID") long userID,
                          @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
