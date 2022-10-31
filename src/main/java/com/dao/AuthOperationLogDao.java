package com.dao;

import com.entity.AuthOperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface AuthOperationLogDao {
    int addAuthOperationLog(AuthOperationLog authOperationLog);
}
