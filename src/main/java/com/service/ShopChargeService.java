package com.service;

import com.dto.ChargeImgExecution;
import com.dto.ImageHolder;
import com.dto.ShopChargeExecution;
import com.entity.ShopCharge;
import com.exceptions.ShopChargeOperationException;

public interface ShopChargeService {
    public ShopChargeExecution addShopCharge(ShopCharge ShopCharge) throws ShopChargeOperationException;

    public ChargeImgExecution addChargeImg(Long shopChargeId, ImageHolder chargeImg) throws ShopChargeOperationException;

    /**
     * 根据userid获取投诉信息
     */
    public ShopChargeExecution getByUserId(long userId, int pageIndex, int pageSize);
    public ShopChargeExecution getByUserId2(long userId);
}
