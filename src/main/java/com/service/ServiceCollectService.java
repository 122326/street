package com.service;


import com.entity.ServiceCollect;
import com.vo.ServiceCollectLog;

import java.util.List;

public interface ServiceCollectService  {
    public List<ServiceCollect> getServiceCollectList(Long userId);

    public Integer addServiceCollect(Long userId,Long serviceId);

    public ServiceCollect queryByUserIdAndServiceId(Long userId,Long serviceId);

    public Integer cancelServiceCollect(ServiceCollect serviceCollect);
    public List<ServiceCollectLog> getCollectUsersByServiceId(Long serviceId, Integer page,
                                                              Integer rows);
    public Integer getServiceCollectCount(Long serviceId);

    public List<ServiceCollectLog> getUsersByServiceId(Long serviceId);
}
