package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dao.PersonInfoDao;
import com.dao.SouCoinDao;
import com.entity.PersonInfo;
import com.entity.Soucoin;
import com.service.SouCoinService;
import com.vo.SoucoinLog;
import com.vo.SoucoinLogSummary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SouCoinServiceImpl implements SouCoinService {
    private final SouCoinDao souCoinDao;
    private final PersonInfoDao personInfoDao;

    public SouCoinServiceImpl(SouCoinDao souCoinDao, PersonInfoDao personInfoDao) {
        this.souCoinDao = souCoinDao;
        this.personInfoDao = personInfoDao;
    }

    @Override
    public void changeSouCoin(Integer userID, Integer souCoin) {
        souCoinDao.changeSouCoin(userID, souCoin);
    }

    @Override
    public void addSouCoinLog(Integer userID, Integer souCoin, String method, String bankName, String cardNumber) {
        souCoinDao.addSouCoinLog(userID, souCoin, method, bankName, cardNumber);
    }

    @Override
    // 充值涉及到修改用户的搜币数量与添加搜币记录，所以需要开启事务处理
    @Transactional
    public void topUpSouCoin(Integer userID, Integer souCoin, String method, String bankName, String cardNumber) {
        changeSouCoin(userID, souCoin);
        addSouCoinLog(userID, souCoin, method, bankName, cardNumber);
    }

    @Override
    // 提现涉及到修改用户的搜币数量与添加搜币记录，所以需要开启事务处理
    @Transactional
    public void withdrawalSouCoin(Integer userID, Integer souCoin, String method, String bankName, String cardNumber) {
        changeSouCoin(userID, -souCoin);
        addSouCoinLog(userID, -souCoin, method, bankName, cardNumber);
    }

    @Override
    public List<SoucoinLog> listLogs(Integer userID, String userName,
                                     Date startTime, Date endTime,
                                     Integer page,
                                     Integer rows, String sort,
                                     String order) {
        return souCoinDao.listLogs(userID, userName, startTime, endTime, page, rows, sort, order);
    }

    @Override
    public Long getCount(Integer userID, String userName, Date startTime, Date endTime) {
        return souCoinDao.getCount(userID, userName, startTime, endTime);
    }

    @Override
    public SoucoinLogSummary getSummary(Integer userID, String userName, Boolean isAllUsersSouCoin,
                                        Date startTime, Date endTime) {
        SoucoinLogSummary soucoinLogSummary = new SoucoinLogSummary();
        List<Long> summary = souCoinDao.getSummary(userID, userName, startTime, endTime);
        Long souCoinSum = null;
        if (isAllUsersSouCoin) {
            souCoinSum = personInfoDao.getSouCoinSum();
        } else {
            souCoinSum = personInfoDao.selectOne(new QueryWrapper<PersonInfo>()
                    .eq("user_name", userName)).getSouCoin();
        }
        summary = summary.stream().map(e -> e == null ? 0 : e).collect(Collectors.toList());
        if (souCoinSum == null) {
            souCoinSum = 0L;
        }
        soucoinLogSummary.setTotal(souCoinSum);
        soucoinLogSummary.setTopUpSummary(summary.get(0));
        soucoinLogSummary.setWithdrawalSummary(-summary.get(1));
        return soucoinLogSummary;
    }

}
