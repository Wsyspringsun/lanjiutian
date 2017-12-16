package com.wyw.ljtds.model;

import java.util.List;

/**
 * Created by wsy on 17-11-29.
 * 整个交易 order
 */
public class OrderTradeDto {
    private String COIN_FLG; //是否使用电子币
    private String COST_POINT; //使用积分数量
    private String DISTRIBUTION_DATE_END; //派送时间截止
    private String DISTRIBUTION_DATE_START; //派送时间起始
    private String ELECTRONIC_MONEY; //电子币 使用额度
    private String OID_USER_ID; //客户的 id
    private String PAYMENT_METHOD; //使用的支付方式
    private String PAY_AMOUNT; //实际 支付 总额度
    private String POINT_MONEY; //积分抵用的 现金数量
    private String POSTAGE_FLG; //是否使用 邮费抵用券
    private String TRADE_MONEY_ALL;//额度汇总

    private String USABLE_AMOUNT; //可用的电子币额度

    private String USER_ADDRESS_ID; //客户地址概要
    private String USER_POINT; //客户积分数量

    private AddressModel USER_ADDRESS; //客户 地址
    List<OrderGroupDto> DETAILS; //包含的商铺

    public String getCOIN_FLG() {
        return COIN_FLG;
    }

    public void setCOIN_FLG(String COIN_FLG) {
        this.COIN_FLG = COIN_FLG;
    }

    public String getCOST_POINT() {
        return COST_POINT;
    }

    public void setCOST_POINT(String COST_POINT) {
        this.COST_POINT = COST_POINT;
    }

    public String getDISTRIBUTION_DATE_END() {
        return DISTRIBUTION_DATE_END;
    }

    public void setDISTRIBUTION_DATE_END(String DISTRIBUTION_DATE_END) {
        this.DISTRIBUTION_DATE_END = DISTRIBUTION_DATE_END;
    }

    public String getDISTRIBUTION_DATE_START() {
        return DISTRIBUTION_DATE_START;
    }

    public void setDISTRIBUTION_DATE_START(String DISTRIBUTION_DATE_START) {
        this.DISTRIBUTION_DATE_START = DISTRIBUTION_DATE_START;
    }

    public String getELECTRONIC_MONEY() {
        return ELECTRONIC_MONEY;
    }

    public void setELECTRONIC_MONEY(String ELECTRONIC_MONEY) {
        this.ELECTRONIC_MONEY = ELECTRONIC_MONEY;
    }

    public String getOID_USER_ID() {
        return OID_USER_ID;
    }

    public void setOID_USER_ID(String OID_USER_ID) {
        this.OID_USER_ID = OID_USER_ID;
    }

    public String getPAYMENT_METHOD() {
        return PAYMENT_METHOD;
    }

    public void setPAYMENT_METHOD(String PAYMENT_METHOD) {
        this.PAYMENT_METHOD = PAYMENT_METHOD;
    }

    public String getPAY_AMOUNT() {
        return PAY_AMOUNT;
    }

    public void setPAY_AMOUNT(String PAY_AMOUNT) {
        this.PAY_AMOUNT = PAY_AMOUNT;
    }

    public String getPOINT_MONEY() {
        return POINT_MONEY;
    }

    public void setPOINT_MONEY(String POINT_MONEY) {
        this.POINT_MONEY = POINT_MONEY;
    }

    public String getPOSTAGE_FLG() {
        return POSTAGE_FLG;
    }

    public void setPOSTAGE_FLG(String POSTAGE_FLG) {
        this.POSTAGE_FLG = POSTAGE_FLG;
    }

    public String getTRADE_MONEY_ALL() {
        return TRADE_MONEY_ALL;
    }

    public void setTRADE_MONEY_ALL(String TRADE_MONEY_ALL) {
        this.TRADE_MONEY_ALL = TRADE_MONEY_ALL;
    }

    public String getUSABLE_AMOUNT() {
        return USABLE_AMOUNT;
    }

    public void setUSABLE_AMOUNT(String USABLE_AMOUNT) {
        this.USABLE_AMOUNT = USABLE_AMOUNT;
    }

    public String getUSER_ADDRESS_ID() {
        return USER_ADDRESS_ID;
    }

    public void setUSER_ADDRESS_ID(String USER_ADDRESS_ID) {
        this.USER_ADDRESS_ID = USER_ADDRESS_ID;
    }

    public String getUSER_POINT() {
        return USER_POINT;
    }

    public void setUSER_POINT(String USER_POINT) {
        this.USER_POINT = USER_POINT;
    }

    public AddressModel getUSER_ADDRESS() {
        return USER_ADDRESS;
    }

    public void setUSER_ADDRESS(AddressModel USER_ADDRESS) {
        this.USER_ADDRESS = USER_ADDRESS;
    }

    public List<OrderGroupDto> getDETAILS() {
        return DETAILS;
    }

    public void setDETAILS(List<OrderGroupDto> DETAILS) {
        this.DETAILS = DETAILS;
    }
}
