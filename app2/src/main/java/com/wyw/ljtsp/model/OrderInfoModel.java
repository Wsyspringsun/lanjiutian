package com.wyw.ljtsp.model;

/**
 * Created by Administrator on 2017/8/2.
 */

public class OrderInfoModel {

    private Integer exchangeQuanlity;

    private String groupStatus;

    private String orderTradeId;

    private Double payAmount;

    private String updDate;

    public Integer getExchangeQuanlity() {
        return exchangeQuanlity;
    }

    public void setExchangeQuanlity(Integer exchangeQuanlity) {
        this.exchangeQuanlity = exchangeQuanlity;
    }

    public String getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(String groupStatus) {
        this.groupStatus = groupStatus;
    }

    public String getOrderTradeId() {
        return orderTradeId;
    }

    public void setOrderTradeId(String orderTradeId) {
        this.orderTradeId = orderTradeId;
    }

    public Double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
    }

    public String getUpdDate() {
        return updDate;
    }

    public void setUpdDate(String updDate) {
        this.updDate = updDate;
    }
}
