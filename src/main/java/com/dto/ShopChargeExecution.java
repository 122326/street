package com.dto;

import com.entity.ShopCharge;
import com.enums.ShopChargeStateEnum;

import java.util.List;

public class ShopChargeExecution {
    // 结果状态
    private int state;
    // 状态标识
    private String stateInfo;
    // 投诉数量
    private int count;
    // 操作的shopComment(增删投诉的时候用到)
    private ShopCharge shopCharge;
    // 投诉列表(查询投诉列表的时候使用)
    private List<ShopCharge> shopChargeList;

    public ShopChargeExecution() {

    }
    // 投诉操作失败的时候使用的构造器
    public ShopChargeExecution(ShopChargeStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }
    // 增删投诉操作成功的时候使用的构造器
    public ShopChargeExecution(ShopChargeStateEnum stateEnum, ShopCharge shopCharge) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shopCharge = shopCharge;
    }
    // 查询投诉操作成功的时候使用的构造器
    public ShopChargeExecution(ShopChargeStateEnum stateEnum, List<ShopCharge> shopChargeList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shopChargeList = shopChargeList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ShopCharge getShopCharge() {
        return shopCharge;
    }

    public void setShopCharge(ShopCharge shopCharge) {
        this.shopCharge = shopCharge;
    }

    public List<ShopCharge> getShopChargeList() {
        return shopChargeList;
    }

    public void setShopChargeList(List<ShopCharge> shopChargeList) {
        this.shopChargeList = shopChargeList;
    }
}
