package com.service;

import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface AuthOperationLogService {
    void addAuthOperationLog(String localAuth,String detail);
}
