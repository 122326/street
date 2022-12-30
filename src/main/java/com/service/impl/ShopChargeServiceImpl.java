package com.service.impl;

import com.dao.ShopChargeDao;
import com.dto.ChargeImgExecution;
import com.dto.ImageHolder;
import com.dto.ShopChargeExecution;
import com.entity.ChargeImg;
import com.entity.ShopCharge;
import com.enums.ShopChargeStateEnum;
import com.exceptions.ShopChargeOperationException;
import com.service.ShopChargeService;
import com.util.ImageUtil;
import com.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ShopChargeServiceImpl implements ShopChargeService {
    @Autowired
    private ShopChargeDao shopChargeDao;

    @Override
    public ShopChargeExecution addShopCharge(ShopCharge shopCharge) throws ShopChargeOperationException {
        // 空值判断
        if (shopCharge == null) {
            return new ShopChargeExecution(ShopChargeStateEnum.NULL_SHOPCHARGE);
        }
        try {
            // 添加评论信息（从前端读取数据）
            int effectedNum = shopChargeDao.insertShopCharge(shopCharge);
            if (effectedNum <= 0) {
                throw new ShopChargeOperationException("投诉创建失败");
            }
        } catch (Exception e) {
            throw new ShopChargeOperationException("addShopCharge error:" + e.getMessage());
        }
        return new ShopChargeExecution(ShopChargeStateEnum.SUCCESS, shopCharge);
    }
    @Override
    @Transactional
    public ChargeImgExecution addChargeImg(Long shopChargeId, ImageHolder chargeImg) throws ShopChargeOperationException {
        if(shopChargeId == null) {
            return new ChargeImgExecution(ShopChargeStateEnum.NULL_SHOPCHARGEID);
        }
        try {
            addImg(shopChargeId, chargeImg);
        }
        catch (Exception e) {
            throw new ShopChargeOperationException("addChargeImg error:" + e.toString());
        }
        return new ChargeImgExecution(ShopChargeStateEnum.SUCCESS);
    }

    @Override
    public ShopChargeExecution getByUserId(long userId, int pageIndex, int pageSize) {
        ShopChargeExecution se = new ShopChargeExecution();
        return se;
    }

    @Override
    public ShopChargeExecution getByUserId2(long userId) {
        ShopCharge shopChargeCondition = new ShopCharge();
        shopChargeCondition.setUserId(userId);

        List<ShopCharge> shopChargeList = shopChargeDao.queryShopChargeList2(shopChargeCondition);
        ShopChargeExecution se = new ShopChargeExecution();
        if(shopChargeList != null) {
            se.setShopChargeList(shopChargeList);
            se.setCount(shopChargeList.size());
        }
        else {
            se.setState(ShopChargeStateEnum.INNER_ERROR.getState());
        }
        return se;
    }

    public void addImg(long shopChargeId, ImageHolder chargeImgHolder) throws ShopChargeOperationException {
        // 获取图片存储路径，这里直接存放到相应投诉id的文件夹底下
        String dest = PathUtil.getChargeImgPath(shopChargeId);
        String imgAddr = ImageUtil.generateNormalImg(chargeImgHolder, dest);
        ChargeImg chargeImg = new ChargeImg();
        imgAddr=imgAddr.replace("\\","/");
        chargeImg.setImgAddr(imgAddr);
        chargeImg.setShopChargeId(shopChargeId);
        try {
            int effectedNum = shopChargeDao.insertChargeImg(chargeImg);
            if (effectedNum <= 0) {
                throw new ShopChargeOperationException("添加投诉图片失败");
            }
        } catch (Exception e) {
            throw new ShopChargeOperationException("创建店铺详情图片失败:" + e.toString());
        }
    }
}

