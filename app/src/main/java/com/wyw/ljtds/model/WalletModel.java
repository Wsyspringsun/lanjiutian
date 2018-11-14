package com.wyw.ljtds.model;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/3/15 0015.
 */

public class WalletModel extends BaseModel {
    private BigDecimal TOTAL_AMOUNT;
    private BigDecimal USABLE_AMOUNT;
    private BigDecimal FROZE_AMOUNT;
    private BigDecimal AWAIT_AMOUNT;
    private BigDecimal PAY_AMOUNT;
    private BigDecimal RISK_FROZE_AMOUNT;
    private BigDecimal USABLE_POINT;
    private BigDecimal FROZE_POINT;
    private BigDecimal CONVERTED_POINT;

    private String cardbalance;//余额
    private String usablePoint;//积分
    private String postnum;  //免邮券张数
    private String preferentialnum; //优惠券张数
    private String sumCouponNum;//总优惠券张数

    public String getSumCouponNum() {
        return sumCouponNum;
    }

    public void setSumCouponNum(String sumCouponNum) {
        this.sumCouponNum = sumCouponNum;
    }

    public String getPreferentialnum() {
        return preferentialnum;
    }

    public void setPreferentialnum(String preferentialnum) {
        this.preferentialnum = preferentialnum;
    }

    public String getPostnum() {
        return postnum;
    }

    public void setPostnum(String postnum) {
        this.postnum = postnum;
    }


    public BigDecimal getUSABLE_POINT() {
        return USABLE_POINT;
    }

    public void setUSABLE_POINT(BigDecimal USABLE_POINT) {
        this.USABLE_POINT = USABLE_POINT;
    }

    public BigDecimal getFROZE_POINT() {
        return FROZE_POINT;
    }

    public void setFROZE_POINT(BigDecimal FROZE_POINT) {
        this.FROZE_POINT = FROZE_POINT;
    }

    public BigDecimal getCONVERTED_POINT() {
        return CONVERTED_POINT;
    }

    public void setCONVERTED_POINT(BigDecimal CONVERTED_POINT) {
        this.CONVERTED_POINT = CONVERTED_POINT;
    }

    public BigDecimal getTOTAL_AMOUNT() {
        return TOTAL_AMOUNT;
    }

    public void setTOTAL_AMOUNT(BigDecimal TOTAL_AMOUNT) {
        this.TOTAL_AMOUNT = TOTAL_AMOUNT;
    }

    public BigDecimal getUSABLE_AMOUNT() {
        return USABLE_AMOUNT;
    }

    public void setUSABLE_AMOUNT(BigDecimal USABLE_AMOUNT) {
        this.USABLE_AMOUNT = USABLE_AMOUNT;
    }

    public BigDecimal getFROZE_AMOUNT() {
        return FROZE_AMOUNT;
    }

    public void setFROZE_AMOUNT(BigDecimal FROZE_AMOUNT) {
        this.FROZE_AMOUNT = FROZE_AMOUNT;
    }

    public BigDecimal getAWAIT_AMOUNT() {
        return AWAIT_AMOUNT;
    }

    public void setAWAIT_AMOUNT(BigDecimal AWAIT_AMOUNT) {
        this.AWAIT_AMOUNT = AWAIT_AMOUNT;
    }

    public BigDecimal getPAY_AMOUNT() {
        return PAY_AMOUNT;
    }

    public void setPAY_AMOUNT(BigDecimal PAY_AMOUNT) {
        this.PAY_AMOUNT = PAY_AMOUNT;
    }

    public BigDecimal getRISK_FROZE_AMOUNT() {
        return RISK_FROZE_AMOUNT;
    }

    public void setRISK_FROZE_AMOUNT(BigDecimal RISK_FROZE_AMOUNT) {
        this.RISK_FROZE_AMOUNT = RISK_FROZE_AMOUNT;
    }

    public String getCardbalance() {
        return cardbalance;
    }

    public void setCardbalance(String cardbalance) {
        this.cardbalance = cardbalance;
    }

    public String getUsablePoint() {
        return usablePoint;
    }

    public void setUsablePoint(String usablePoint) {
        this.usablePoint = usablePoint;
    }
}
