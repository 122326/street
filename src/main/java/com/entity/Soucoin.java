package com.entity;

import java.util.Date;

public class Soucoin {

    private Long logId;
    private Long userId;
    private String userName;
    private Integer value;
    private String method;
    private String bankName;
    private String cardNumber;
    private Date time;

    private Integer version;

    public Long getLogId() {  return logId;  }

    public void setLogId(Long logId) { this.logId = logId; }

    public Soucoin() {    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() { return userName; }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getValue() { return value; }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
