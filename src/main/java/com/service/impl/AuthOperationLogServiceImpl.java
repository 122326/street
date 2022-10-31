package com.service.impl;

import com.dao.AuthOperationLogDao;
import com.entity.AuthOperationLog;
import com.service.AuthOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthOperationLogServiceImpl implements AuthOperationLogService {
    @Autowired
    private AuthOperationLogDao authOperationLogDao;


    @Override
    public void addAuthOperationLog(String localAuth, String detail) {
        AuthOperationLog authOperationLog = new AuthOperationLog(localAuth,detail,new Date());
        authOperationLogDao.addAuthOperationLog(authOperationLog);
    }
}
