package com.service.impl;

import com.dao.PersonInfoDao;
import com.dao.SouCoinDao;
import com.entity.Soucoin;
import com.service.SouCoinService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class SouCoinServiceImpl implements SouCoinService {
    private final SouCoinDao souCoinDao;
    private final PersonInfoDao personInfoDao;

    public SouCoinServiceImpl(SouCoinDao souCoinDao, PersonInfoDao personInfoDao) {
        this.souCoinDao = souCoinDao;
        this.personInfoDao = personInfoDao;
    }

    @Override
    public void changeSouCoin(long userID, Integer souCoin) {
        souCoinDao.changeSouCoin(userID, souCoin);
    }

    @Override
    public void addSouCoinLog(long userID, Integer souCoin, String method,
                              String bankName, String cardNumber) {
        souCoinDao.addSouCoinLog(userID, souCoin, method, bankName, cardNumber);
    }

    @Override
    // 充值涉及到修改用户的搜币数量与添加搜币记录，所以需要开启事务处理
    @Transactional
    public void topUpSouCoin(long userID, Integer souCoin, String method, String bankName, String cardNumber) {
        changeSouCoin(userID, souCoin);
        addSouCoinLog(userID, souCoin, method, bankName, cardNumber);
    }

    @Override
    // 提现涉及到修改用户的搜币数量与添加搜币记录，所以需要开启事务处理
    @Transactional
    public void withdrawalSouCoin(long userID, Integer souCoin, String method, String bankName, String cardNumber) {
        changeSouCoin(userID, -souCoin);
        addSouCoinLog(userID, -souCoin, method, bankName, cardNumber);
    }

    @Override
    public List<Soucoin> listLogs(long userID,
                                  Date startTime, Date endTime,
                                  Integer rowsIndex,
                                  Integer pageSize, String sort,
                                  String order) {
        return souCoinDao.listLogs(userID, startTime, endTime, rowsIndex, pageSize, sort, order);
    }

}
