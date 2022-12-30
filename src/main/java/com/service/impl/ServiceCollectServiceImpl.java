package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dao.ServiceCollectDao;
import com.entity.ServiceCollect;
import com.service.ServiceCollectService;
import com.vo.ServiceCollectLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServiceCollectServiceImpl  implements ServiceCollectService  {
    @Autowired
    private ServiceCollectDao serviceCollectDao;

    @Override
    public List<ServiceCollect> getServiceCollectList(Long userId){

        List<ServiceCollect> serviceCollectList = serviceCollectDao.getServiceCollectList(userId);
        return serviceCollectList;
    }

    @Override
    public Integer addServiceCollect(Long userId, Long serviceId) {
        int line=0;

        ServiceCollect serviceCollect = serviceCollectDao.queryByUserIdAndServiceId(userId, serviceId);
        if (serviceCollect==null){
            ServiceCollect newServiceCollect = new ServiceCollect();
            newServiceCollect.setServiceId(serviceId);
            newServiceCollect.setUserId(userId);
            newServiceCollect.setCreateTime(LocalDateTime.now());
            try {
                line = serviceCollectDao.insertServiceCollect(newServiceCollect);
            }catch (Exception e){
                throw e;
            }
        }else {
            serviceCollect.setIsDelete(0);
            serviceCollect.setCreateTime(LocalDateTime.now());

            line = serviceCollectDao.updateServiceCollect(serviceCollect);
        }

        return line;
    }

    @Override
    public ServiceCollect queryByUserIdAndServiceId(Long userId, Long serviceId) {
        ServiceCollect serviceCollect;
        try {
            serviceCollect = serviceCollectDao.queryByUserIdAndServiceId(userId,serviceId);
        }catch (Exception e){
            throw e;
        }
        return serviceCollect;
    }

    @Override
    public Integer cancelServiceCollect(ServiceCollect serviceCollect) {
        serviceCollect.setIsDelete(1);
        int line;
        try{
            line = serviceCollectDao.updateServiceCollect(serviceCollect);
        }catch (Exception e){
            throw e;
        }
        return line;
    }

    @Override
    public List<ServiceCollectLog> getCollectUsersByServiceId(Long serviceId, Integer page, Integer rows) {
        List<ServiceCollectLog> collectUsersByServiceId = serviceCollectDao.getCollectUsersByServiceId(serviceId, page, rows);
        return collectUsersByServiceId;
    }

    @Override
    public Integer getServiceCollectCount(Long serviceId) {
        return serviceCollectDao.selectCount(new QueryWrapper<ServiceCollectLog>().eq("service_id",serviceId));
    }

    @Override
    public List<ServiceCollectLog> getUsersByServiceId(Long serviceId) {
        List<ServiceCollectLog> collectUsersByServiceId = serviceCollectDao.getUsersByServiceId(serviceId);
        return collectUsersByServiceId;
    }


}
