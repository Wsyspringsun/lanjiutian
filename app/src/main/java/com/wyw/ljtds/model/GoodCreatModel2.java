package com.wyw.ljtds.model;

import java.util.List;

/**
 * Created by Administrator on 2017/4/16 0016.
 */

public class GoodCreatModel2 extends BaseModel {
    private String DISTRIBUTION_MODE;
    private String INVOICE_FLG;
    private String INVOICE_TYPE;
    private String INVOICE_TITLE;
    private String INVOICE_CONTENT;
    private String INVOICE_TAX;
    private String OID_GROUP_ID;
    private String OID_GROUP_NAME;

    private String POSTAGE;
    private String POST_NUM;
    private String ELECTRONIC_MONEY;
    private String ELECTRONIC_USEABLE_MONEY;

    private List<GoodCreatModel3> DETAILS;

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

    public String getDISTRIBUTION_MODE() {
        return DISTRIBUTION_MODE;
    }

    public void setDISTRIBUTION_MODE(String DISTRIBUTION_MODE) {
        this.DISTRIBUTION_MODE = DISTRIBUTION_MODE;
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

    public List<GoodCreatModel3> getDETAILS() {
        return DETAILS;
    }

    public void setDETAILS(List<GoodCreatModel3> DETAILS) {
        this.DETAILS = DETAILS;
    }

    public String getELECTRONIC_USEABLE_MONEY() {
        return ELECTRONIC_USEABLE_MONEY;
    }

    public void setELECTRONIC_USEABLE_MONEY(String ELECTRONIC_USEABLE_MONEY) {
        this.ELECTRONIC_USEABLE_MONEY = ELECTRONIC_USEABLE_MONEY;
    }

    public String getELECTRONIC_MONEY() {
        return ELECTRONIC_MONEY;
    }

    public void setELECTRONIC_MONEY(String ELECTRONIC_MONEY) {
        this.ELECTRONIC_MONEY = ELECTRONIC_MONEY;
    }

    public String getPOSTAGE() {
        return POSTAGE;
    }

    public void setPOSTAGE(String POSTAGE) {
        this.POSTAGE = POSTAGE;
    }

    public String getPOST_NUM() {
        return POST_NUM;
    }

    public void setPOST_NUM(String POST_NUM) {
        this.POST_NUM = POST_NUM;
    }

    public String getINVOICE_TAX() {
        return INVOICE_TAX;
    }

    public void setINVOICE_TAX(String INVOICE_TAX) {
        this.INVOICE_TAX = INVOICE_TAX;
    }
}
