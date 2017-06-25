package com.wyw.ljtds.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class TestModel {
    private BigDecimal COST_POINT;//抵用积分数
    private BigDecimal POINT_MONEY;//积分抵用金额
    private BigDecimal COST_MONEY_ALL;//应付金额
    private BigDecimal PAY_AMOUNT;//实付金额
    private String PAYMENT_METHOD;//支付方式 0在线支付，1货到付款，2，余额支付，3支付宝，4银联，5线下医保卡，6线下其他
    private String INVOICE_FLG;//0不开具，1开具
    private String INVOICE_TYPE;//0普通发票（纸质发票），1电子发票
    private String USER_ADDRESS_ID;//用户地址ID
    private String DISTRIBUTION_MODE;//0送货上门，1门店自取
    public List<Business> DETAILS;

    public BigDecimal getCOST_POINT() {
        return COST_POINT;
    }

    public void setCOST_POINT(BigDecimal COST_POINT) {
        this.COST_POINT = COST_POINT;
    }

    public BigDecimal getPOINT_MONEY() {
        return POINT_MONEY;
    }

    public void setPOINT_MONEY(BigDecimal POINT_MONEY) {
        this.POINT_MONEY = POINT_MONEY;
    }

    public BigDecimal getCOST_MONEY_ALL() {
        return COST_MONEY_ALL;
    }

    public void setCOST_MONEY_ALL(BigDecimal COST_MONEY_ALL) {
        this.COST_MONEY_ALL = COST_MONEY_ALL;
    }

    public BigDecimal getPAY_AMOUNT() {
        return PAY_AMOUNT;
    }

    public void setPAY_AMOUNT(BigDecimal PAY_AMOUNT) {
        this.PAY_AMOUNT = PAY_AMOUNT;
    }

    public String getPAYMENT_METHOD() {
        return PAYMENT_METHOD;
    }

    public void setPAYMENT_METHOD(String PAYMENT_METHOD) {
        this.PAYMENT_METHOD = PAYMENT_METHOD;
    }

    public String getINVOICE_FLG() {
        return INVOICE_FLG;
    }

    public void setINVOICE_FLG(String INVOICE_FLG) {
        this.INVOICE_FLG = INVOICE_FLG;
    }

    public String getINVOICE_TYPE() {
        return INVOICE_TYPE;
    }

    public void setINVOICE_TYPE(String INVOICE_TYPE) {
        this.INVOICE_TYPE = INVOICE_TYPE;
    }

    public String getUSER_ADDRESS_ID() {
        return USER_ADDRESS_ID;
    }

    public void setUSER_ADDRESS_ID(String USER_ADDRESS_ID) {
        this.USER_ADDRESS_ID = USER_ADDRESS_ID;
    }

    public String getDISTRIBUTION_MODE() {
        return DISTRIBUTION_MODE;
    }

    public void setDISTRIBUTION_MODE(String DISTRIBUTION_MODE) {
        this.DISTRIBUTION_MODE = DISTRIBUTION_MODE;
    }

    public List<Business> getDETAILS() {
        return DETAILS;
    }

    public void setDETAILS(List<Business> DETAILS) {
        this.DETAILS = DETAILS;
    }
}
