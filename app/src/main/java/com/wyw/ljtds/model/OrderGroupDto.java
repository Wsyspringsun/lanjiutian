package com.wyw.ljtds.model;

import android.support.annotation.IntegerRes;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by wsy on 17-11-29.
 */

public class OrderGroupDto {
    private String COIN_FLG; //1:使用 0:没有使用
    private Integer COST_POINT = 0;
    private Double SHARE_MONEY = 0d;
    private String ORDER_GROUP_ID;
    private String ORDER_TRADE_ID;
    private String OID_GROUP_ID;
    private String OID_GROUP_NAME;
    private String POSTAGE;
    private String POSTAGE_FLG; //1:使用 0 : 没有使用
    private Integer POST_NUM; //抵邮券数量
    private String RED_PACKET_ID;
    private String PREFERENTIAL_FLG;
    private Integer PREFERENTIAL_NUM; //优惠券数量
    private String USER_POINT;  //积分数量
    private String PREFERENTIAL_ID;
    private Integer ELECTRONIC_MONEY = 0;
    private Integer ELECTRONIC_USEABLE_MONEY; //电子币数量
    private String COUPON_MONEY;
    private String COUPON_USEABLE_MONEY;
    private String GROUP_EXCHANGE_QUANLITY;
    private String RED_PACKET_MONEY;
    private String RED_PACKET_USEABLE_MONEY;
    private String GROUP_MONEY_ALL;
    private String GROUP_STATUS;
    private BigDecimal GROUP_PAY_AMOUNT;
    private String GROUP_REMARKS;
    private String INVOICE_FLG;
    private String INVOICE_TYPE;
    private String INVOICE_ORG; //1:公司；0:个人
    private String INVOICE_ID;
    private String INVOICE_TAX;
    private String INVOICE_TITLE;
    //发票种类 0：明细  1办公  2：家居   3：药品   4：耗材
    private String INVOICE_CONTENT;
    private String LOGISTICS_COMPANY;
    private String LOGISTICS_COMPANY_ID;
    private String LOGISTICS_ORDER_ID;
    private String DISTRIBUTION_MODE;
    private String DISTRIBUTION_DATE;
    private String DISTRIBUTION_DATE_END;
    private String DISTRIBUTION_DATE_START;
    private String SETTLEMENT_FLG;
    private String DEL_FLG;
    private String INS_USER_ID;
    private String UPD_USER_ID;
    private String INS_DATE;
    private String UPD_DATE;
    private String STATUS;

    private String USER_ADDRESS_LOCATION;//目标直抵

    List<OrderCommDto> DETAILS; //包含的商品

    public List<OrderCommDto> getDETAILS() {
        return DETAILS;
    }

    public void setDETAILS(List<OrderCommDto> DETAILS) {
        this.DETAILS = DETAILS;
    }

    public String getORDER_GROUP_ID() {
        return ORDER_GROUP_ID;
    }

    public void setORDER_GROUP_ID(String ORDER_GROUP_ID) {
        this.ORDER_GROUP_ID = ORDER_GROUP_ID;
    }

    public String getORDER_TRADE_ID() {
        return ORDER_TRADE_ID;
    }

    public void setORDER_TRADE_ID(String ORDER_TRADE_ID) {
        this.ORDER_TRADE_ID = ORDER_TRADE_ID;
    }

    public String getOID_GROUP_ID() {
        return OID_GROUP_ID;
    }

    public void setOID_GROUP_ID(String OID_GROUP_ID) {
        this.OID_GROUP_ID = OID_GROUP_ID;
    }

    public String getOID_GROUP_NAME() {
        return OID_GROUP_NAME;
    }

    public void setOID_GROUP_NAME(String OID_GROUP_NAME) {
        this.OID_GROUP_NAME = OID_GROUP_NAME;
    }

    public String getPOSTAGE() {
        return POSTAGE;
    }

    public void setPOSTAGE(String POSTAGE) {
        this.POSTAGE = POSTAGE;
    }

    public String getPOSTAGE_FLG() {
        return POSTAGE_FLG;
    }

    public void setPOSTAGE_FLG(String POSTAGE_FLG) {
        this.POSTAGE_FLG = POSTAGE_FLG;
    }

    public String getRED_PACKET_ID() {
        return RED_PACKET_ID;
    }

    public void setRED_PACKET_ID(String RED_PACKET_ID) {
        this.RED_PACKET_ID = RED_PACKET_ID;
    }

    public String getPREFERENTIAL_FLG() {
        return PREFERENTIAL_FLG;
    }

    public void setPREFERENTIAL_FLG(String PREFERENTIAL_FLG) {
        this.PREFERENTIAL_FLG = PREFERENTIAL_FLG;
    }

    public String getPREFERENTIAL_ID() {
        return PREFERENTIAL_ID;
    }

    public void setPREFERENTIAL_ID(String PREFERENTIAL_ID) {
        this.PREFERENTIAL_ID = PREFERENTIAL_ID;
    }

    public Integer getELECTRONIC_MONEY() {
        return ELECTRONIC_MONEY;
    }

    public void setELECTRONIC_MONEY(Integer ELECTRONIC_MONEY) {
        this.ELECTRONIC_MONEY = ELECTRONIC_MONEY;
    }

    public Integer getELECTRONIC_USEABLE_MONEY() {
        return ELECTRONIC_USEABLE_MONEY;
    }

    public void setELECTRONIC_USEABLE_MONEY(Integer ELECTRONIC_USEABLE_MONEY) {
        this.ELECTRONIC_USEABLE_MONEY = ELECTRONIC_USEABLE_MONEY;
    }

    public String getCOUPON_MONEY() {
        return COUPON_MONEY;
    }

    public void setCOUPON_MONEY(String COUPON_MONEY) {
        this.COUPON_MONEY = COUPON_MONEY;
    }

    public String getCOUPON_USEABLE_MONEY() {
        return COUPON_USEABLE_MONEY;
    }

    public void setCOUPON_USEABLE_MONEY(String COUPON_USEABLE_MONEY) {
        this.COUPON_USEABLE_MONEY = COUPON_USEABLE_MONEY;
    }

    public String getRED_PACKET_MONEY() {
        return RED_PACKET_MONEY;
    }

    public void setRED_PACKET_MONEY(String RED_PACKET_MONEY) {
        this.RED_PACKET_MONEY = RED_PACKET_MONEY;
    }

    public String getRED_PACKET_USEABLE_MONEY() {
        return RED_PACKET_USEABLE_MONEY;
    }

    public void setRED_PACKET_USEABLE_MONEY(String RED_PACKET_USEABLE_MONEY) {
        this.RED_PACKET_USEABLE_MONEY = RED_PACKET_USEABLE_MONEY;
    }

    public String getGROUP_MONEY_ALL() {
        return GROUP_MONEY_ALL;
    }

    public void setGROUP_MONEY_ALL(String GROUP_MONEY_ALL) {
        this.GROUP_MONEY_ALL = GROUP_MONEY_ALL;
    }

    public String getGROUP_STATUS() {
        return GROUP_STATUS;
    }

    public void setGROUP_STATUS(String GROUP_STATUS) {
        this.GROUP_STATUS = GROUP_STATUS;
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

    public String getINVOICE_ID() {
        return INVOICE_ID;
    }

    public void setINVOICE_ID(String INVOICE_ID) {
        this.INVOICE_ID = INVOICE_ID;
    }

    public String getLOGISTICS_COMPANY() {
        return LOGISTICS_COMPANY;
    }

    public void setLOGISTICS_COMPANY(String LOGISTICS_COMPANY) {
        this.LOGISTICS_COMPANY = LOGISTICS_COMPANY;
    }

    public String getLOGISTICS_COMPANY_ID() {
        return LOGISTICS_COMPANY_ID;
    }

    public void setLOGISTICS_COMPANY_ID(String LOGISTICS_COMPANY_ID) {
        this.LOGISTICS_COMPANY_ID = LOGISTICS_COMPANY_ID;
    }

    public String getLOGISTICS_ORDER_ID() {
        return LOGISTICS_ORDER_ID;
    }

    public void setLOGISTICS_ORDER_ID(String LOGISTICS_ORDER_ID) {
        this.LOGISTICS_ORDER_ID = LOGISTICS_ORDER_ID;
    }

    public String getDISTRIBUTION_MODE() {
        return DISTRIBUTION_MODE;
    }

    public void setDISTRIBUTION_MODE(String DISTRIBUTION_MODE) {
        this.DISTRIBUTION_MODE = DISTRIBUTION_MODE;
    }

    public String getDISTRIBUTION_DATE() {
        return DISTRIBUTION_DATE;
    }

    public void setDISTRIBUTION_DATE(String DISTRIBUTION_DATE) {
        this.DISTRIBUTION_DATE = DISTRIBUTION_DATE;
    }

    public String getSETTLEMENT_FLG() {
        return SETTLEMENT_FLG;
    }

    public void setSETTLEMENT_FLG(String SETTLEMENT_FLG) {
        this.SETTLEMENT_FLG = SETTLEMENT_FLG;
    }

    public String getDEL_FLG() {
        return DEL_FLG;
    }

    public void setDEL_FLG(String DEL_FLG) {
        this.DEL_FLG = DEL_FLG;
    }

    public String getINS_USER_ID() {
        return INS_USER_ID;
    }

    public void setINS_USER_ID(String INS_USER_ID) {
        this.INS_USER_ID = INS_USER_ID;
    }

    public String getUPD_USER_ID() {
        return UPD_USER_ID;
    }

    public void setUPD_USER_ID(String UPD_USER_ID) {
        this.UPD_USER_ID = UPD_USER_ID;
    }

    public String getINS_DATE() {
        return INS_DATE;
    }

    public void setINS_DATE(String INS_DATE) {
        this.INS_DATE = INS_DATE;
    }

    public String getUPD_DATE() {
        return UPD_DATE;
    }

    public void setUPD_DATE(String UPD_DATE) {
        this.UPD_DATE = UPD_DATE;
    }

    public Integer getCOST_POINT() {
        return COST_POINT;
    }

    public void setCOST_POINT(Integer COST_POINT) {
        this.COST_POINT = COST_POINT;
    }

    public String getCOIN_FLG() {
        return COIN_FLG;
    }

    public void setCOIN_FLG(String COIN_FLG) {
        this.COIN_FLG = COIN_FLG;
    }

    public String getINVOICE_TAX() {
        return INVOICE_TAX;
    }

    public void setINVOICE_TAX(String INVOICE_TAX) {
        this.INVOICE_TAX = INVOICE_TAX;
    }

    public String getINVOICE_TITLE() {
        return INVOICE_TITLE;
    }

    public void setINVOICE_TITLE(String INVOICE_TITLE) {
        this.INVOICE_TITLE = INVOICE_TITLE;
    }

    public String getINVOICE_CONTENT() {
        return INVOICE_CONTENT;
    }

    public void setINVOICE_CONTENT(String INVOICE_CONTENT) {
        this.INVOICE_CONTENT = INVOICE_CONTENT;
    }

    public String getGROUP_EXCHANGE_QUANLITY() {
        return GROUP_EXCHANGE_QUANLITY;
    }

    public void setGROUP_EXCHANGE_QUANLITY(String GROUP_EXCHANGE_QUANLITY) {
        this.GROUP_EXCHANGE_QUANLITY = GROUP_EXCHANGE_QUANLITY;
    }

    public String getGROUP_REMARKS() {
        return GROUP_REMARKS;
    }

    public void setGROUP_REMARKS(String GROUP_REMARKS) {
        this.GROUP_REMARKS = GROUP_REMARKS;
    }

    public String getDISTRIBUTION_DATE_START() {
        return DISTRIBUTION_DATE_START;
    }

    public void setDISTRIBUTION_DATE_START(String DISTRIBUTION_DATE_START) {
        this.DISTRIBUTION_DATE_START = DISTRIBUTION_DATE_START;
    }

    public String getDISTRIBUTION_DATE_END() {
        return DISTRIBUTION_DATE_END;
    }

    public void setDISTRIBUTION_DATE_END(String DISTRIBUTION_DATE_END) {
        this.DISTRIBUTION_DATE_END = DISTRIBUTION_DATE_END;
    }

    public String getINVOICE_ORG() {
        return INVOICE_ORG;
    }

    public void setINVOICE_ORG(String INVOICE_ORG) {
        this.INVOICE_ORG = INVOICE_ORG;
    }

    public Integer getPOST_NUM() {
        return POST_NUM;
    }

    public void setPOST_NUM(Integer POST_NUM) {
        this.POST_NUM = POST_NUM;
    }

    public Integer getPREFERENTIAL_NUM() {
        return PREFERENTIAL_NUM;
    }

    public void setPREFERENTIAL_NUM(Integer PREFERENTIAL_NUM) {
        this.PREFERENTIAL_NUM = PREFERENTIAL_NUM;
    }

    public String getUSER_POINT() {
        return USER_POINT;
    }

    public void setUSER_POINT(String USER_POINT) {
        this.USER_POINT = USER_POINT;
    }

    public String getUSER_ADDRESS_LOCATION() {
        return USER_ADDRESS_LOCATION;
    }

    public void setUSER_ADDRESS_LOCATION(String USER_ADDRESS_LOCATION) {
        this.USER_ADDRESS_LOCATION = USER_ADDRESS_LOCATION;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public BigDecimal getGROUP_PAY_AMOUNT() {
        return GROUP_PAY_AMOUNT;
    }

    public void setGROUP_PAY_AMOUNT(BigDecimal GROUP_PAY_AMOUNT) {
        this.GROUP_PAY_AMOUNT = GROUP_PAY_AMOUNT;
    }

    public Double getSHARE_MONEY() {
        return SHARE_MONEY;
    }

    public void setSHARE_MONEY(Double SHARE_MONEY) {
        this.SHARE_MONEY = SHARE_MONEY;
    }
}
