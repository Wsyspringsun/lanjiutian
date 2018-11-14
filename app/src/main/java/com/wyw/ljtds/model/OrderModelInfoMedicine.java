package com.wyw.ljtds.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017/4/23 0023.
 */

public class OrderModelInfoMedicine extends OrderGroupDto {
    private XiaoNengData xiaonengData;
    private String ADDRESS; //商铺地址
    private BigDecimal PAY_AMOUNT;
    private String USER_ADDRESS_ID;
    private Long COMPLETE_DATE;
    private Long CREATE_DATE;
    private String ORG_ADDRESS;
    private String ORG_MOBILE;
    private String PAYMENT_METHOD;
    private CreatOrderModel.USER_ADDRESS USER_ADDRESS;


    public Long getCOMPLETE_DATE() {
        return COMPLETE_DATE;
    }

    public void setCOMPLETE_DATE(Long COMPLETE_DATE) {
        this.COMPLETE_DATE = COMPLETE_DATE;
    }

    public Long getCREATE_DATE() {
        return CREATE_DATE;
    }

    public void setCREATE_DATE(Long CREATE_DATE) {
        this.CREATE_DATE = CREATE_DATE;
    }




    public CreatOrderModel.USER_ADDRESS getUSER_ADDRESS() {
        return USER_ADDRESS;
    }

    public BigDecimal getPAY_AMOUNT() {
        return PAY_AMOUNT;
    }

    public String getUSER_ADDRESS_ID() {
        return USER_ADDRESS_ID;
    }

    public void setUSER_ADDRESS_ID(String USER_ADDRESS_ID) {
        this.USER_ADDRESS_ID = USER_ADDRESS_ID;
    }

    public void setPAY_AMOUNT(BigDecimal PAY_AMOUNT) {
        this.PAY_AMOUNT = PAY_AMOUNT;
    }

    public void setUSER_ADDRESS(CreatOrderModel.USER_ADDRESS USER_ADDRESS) {
        this.USER_ADDRESS = USER_ADDRESS;
    }

    public XiaoNengData getXiaonengData() {
        return xiaonengData;
    }

    public void setXiaonengData(XiaoNengData xiaonengData) {
        this.xiaonengData = xiaonengData;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getORG_ADDRESS() {
        return ORG_ADDRESS;
    }

    public void setORG_ADDRESS(String ORG_ADDRESS) {
        this.ORG_ADDRESS = ORG_ADDRESS;
    }

    public String getORG_MOBILE() {
        return ORG_MOBILE;
    }

    public void setORG_MOBILE(String ORG_MOBILE) {
        this.ORG_MOBILE = ORG_MOBILE;
    }

    public String getPAYMENT_METHOD() {
        return PAYMENT_METHOD;
    }

    public void setPAYMENT_METHOD(String PAYMENT_METHOD) {
        this.PAYMENT_METHOD = PAYMENT_METHOD;
    }

}
