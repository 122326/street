package com.dao;

import com.entity.ChargeImg;
import com.entity.ShopCharge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShopChargeDao {
    /**
     * 提交投诉
     * @param ShopChargeInfo
     * @return
     */
    public int insertShopCharge(ShopCharge ShopChargeInfo);
    /**
     * 提交投诉图片
     * @param chargeImg
     * @return
     */
    public int insertChargeImg(ChargeImg chargeImg);

    /**
     * 查询投诉
     */
    public List<ShopCharge> queryShopChargeList2(@Param("shopChargeCondition")ShopCharge shopChargeCondition);
}
