package com.dto;

import com.entity.ChargeImg;
import com.enums.ShopChargeStateEnum;

import java.util.List;

public class ChargeImgExecution {
    // 结果状态
    private int state;

    // 状态标识
    private String stateInfo;

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

    public List<ChargeImg> getChargeImgList() {
        return chargeImgList;
    }

    public void setChargeImgList(List<ChargeImg> chargeImgList) {
        this.chargeImgList = chargeImgList;
    }

    // 投诉图片数量
    private int count;

    // appealImg列表(查询求助图片列表的时候使用)
    private List<ChargeImg> chargeImgList;

    public ChargeImgExecution() {

    }

    // 求助图片操作失败的时候使用的构造器
    public ChargeImgExecution(ShopChargeStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    // 求助图片操作成功的时候使用的构造器
    public ChargeImgExecution(ShopChargeStateEnum stateEnum, List<ChargeImg> chargeImgList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.chargeImgList = chargeImgList;
    }


}
