package com.wyw.ljtds.model;

/**
 * Created by Administrator on 2017/4/26 0026.
 */

public class WriteEvaluateModel extends BaseModel {
    private String BUSINESS_DELIVERY;
    private String LOGISTICS_DELIVERY;
    private String LOGISTICS_SERVE;
    private String DESCRIBE_IDENTICAL;
    private String EVALUATE_CONTGENT;
    private String BUSINESS_SERVE;
    private String EVALUATE_GRADE;
    private String VALID_FLG;
    private String COMMODITY_ID;
    private String COMMODITY_ORDER_ID;
    private String[] IMG;

    public String getBUSINESS_DELIVERY() {
        return BUSINESS_DELIVERY;
    }

    public void setBUSINESS_DELIVERY(String BUSINESS_DELIVERY) {
        this.BUSINESS_DELIVERY = BUSINESS_DELIVERY;
    }

    public String getLOGISTICS_DELIVERY() {
        return LOGISTICS_DELIVERY;
    }

    public void setLOGISTICS_DELIVERY(String LOGISTICS_DELIVERY) {
        this.LOGISTICS_DELIVERY = LOGISTICS_DELIVERY;
    }

    public String getLOGISTICS_SERVE() {
        return LOGISTICS_SERVE;
    }

    public void setLOGISTICS_SERVE(String LOGISTICS_SERVE) {
        this.LOGISTICS_SERVE = LOGISTICS_SERVE;
    }

    public String getDESCRIBE_IDENTICAL() {
        return DESCRIBE_IDENTICAL;
    }

    public void setDESCRIBE_IDENTICAL(String DESCRIBE_IDENTICAL) {
        this.DESCRIBE_IDENTICAL = DESCRIBE_IDENTICAL;
    }

    public String getEVALUATE_CONTGENT() {
        return EVALUATE_CONTGENT;
    }

    public void setEVALUATE_CONTGENT(String EVALUATE_CONTGENT) {
        this.EVALUATE_CONTGENT = EVALUATE_CONTGENT;
    }

    public String getBUSINESS_SERVE() {
        return BUSINESS_SERVE;
    }

    public void setBUSINESS_SERVE(String BUSINESS_SERVE) {
        this.BUSINESS_SERVE = BUSINESS_SERVE;
    }

    public String getEVALUATE_GRADE() {
        return EVALUATE_GRADE;
    }

    public void setEVALUATE_GRADE(String EVALUATE_GRADE) {
        this.EVALUATE_GRADE = EVALUATE_GRADE;
    }

    public String getVALID_FLG() {
        return VALID_FLG;
    }

    public void setVALID_FLG(String VALID_FLG) {
        this.VALID_FLG = VALID_FLG;
    }

    public String getCOMMODITY_ID() {
        return COMMODITY_ID;
    }

    public void setCOMMODITY_ID(String COMMODITY_ID) {
        this.COMMODITY_ID = COMMODITY_ID;
    }

    public String getCOMMODITY_ORDER_ID() {
        return COMMODITY_ORDER_ID;
    }

    public void setCOMMODITY_ORDER_ID(String COMMODITY_ORDER_ID) {
        this.COMMODITY_ORDER_ID = COMMODITY_ORDER_ID;
    }

    public String[] getIMG() {
        return IMG;
    }

    public void setIMG(String[] IMG) {
        this.IMG = IMG;
    }
}
