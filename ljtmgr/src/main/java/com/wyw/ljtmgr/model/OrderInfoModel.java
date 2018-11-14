package com.wyw.ljtmgr.model;

/**
 * Created by Administrator on 2017/8/2.
 */

public class OrderInfoModel {
    public static final String CLASSIFY_RETURN = "D";

    private String classify;//标识是否退货

    private String returnGoodsHandingId; // 退换货id

    private Integer exchangeQuanlity;

    private String groupStatus;

    private String orderGroupId;

    private Double groupPayAmount;

    /**
     *
     */
    private String oidGroupId;

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

    public String getOidGroupId() {
        return oidGroupId;
    }

    public String getOrderGroupId() {
        return orderGroupId;
    }

    public void setOrderGroupId(String orderGroupId) {
        this.orderGroupId = orderGroupId;
    }

    public Double getGroupPayAmount() {
        return groupPayAmount;
    }

    public void setGroupPayAmount(Double groupPayAmount) {
        this.groupPayAmount = groupPayAmount;
    }

    public void setOidGroupId(String oidGroupId) {
        this.oidGroupId = oidGroupId;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getReturnGoodsHandingId() {
        return returnGoodsHandingId;
    }

    public void setReturnGoodsHandingId(String returnGoodsHandingId) {
        this.returnGoodsHandingId = returnGoodsHandingId;
    }
}
