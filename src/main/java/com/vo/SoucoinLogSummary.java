package com.vo;

public class SoucoinLogSummary {
    private Long total;
    private Long topUpSummary;
    private Long withdrawalSummary;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getTopUpSummary() {
        return topUpSummary;
    }

    public void setTopUpSummary(Long topUpSummary) {
        this.topUpSummary = topUpSummary;
    }

    public Long getWithdrawalSummary() {
        return withdrawalSummary;
    }

    public void setWithdrawalSummary(Long withdrawalSummary) {
        this.withdrawalSummary = withdrawalSummary;
    }

    public SoucoinLogSummary() {
    }

}
