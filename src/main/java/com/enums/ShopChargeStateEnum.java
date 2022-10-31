package com.enums;

public enum ShopChargeStateEnum {
    SUCCESS(3, "操作成功"), INNER_ERROR(-1001, "内部系统错误"),
    NULL_SHOPID(-1002, "ShopId为空"), NULL_SHOPCHARGE(-1003, "ShopCharge为空"),NULL_USERID(-1004, "userId为空"),
    NULL_SHOPCHARGEID(-1007, "ShopChargeId参数错误"), NULL_IMAGE(-1008, "chargeImg为空");
    private int state;
    private String stateInfo;

    private ShopChargeStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    /**
     * 依据传入的state返回相应的enum值
     */
    public static ShopChargeStateEnum stateOf(int state) {
        for (ShopChargeStateEnum stateEnum : values()) {
            if (stateEnum.getState() == state) {
                return stateEnum;
            }
        }
        return null;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }
}
