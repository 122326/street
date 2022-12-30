package com.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.ServiceCollect;
import com.vo.ServiceCollectLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.lang.Nullable;


import java.util.List;

@Mapper
public interface ServiceCollectDao extends BaseMapper<ServiceCollectLog> {
    public List<ServiceCollect> getServiceCollectList(Long userId);

    public int  insertServiceCollect(ServiceCollect serviceCollect);

    public ServiceCollect queryByUserIdAndServiceId(Long userId,Long serviceId);

    public int updateServiceCollect(ServiceCollect serviceCollect);

    public List<ServiceCollectLog> getCollectUsersByServiceId(Long serviceId,  Integer page,  Integer rows);

    public List<ServiceCollectLog> getUsersByServiceId(Long serviceId);
}
