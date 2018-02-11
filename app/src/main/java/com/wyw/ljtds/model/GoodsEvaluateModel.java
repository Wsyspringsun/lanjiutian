package com.wyw.ljtds.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public class GoodsEvaluateModel {
    private String COMMODITY_ORDER_ID;
    private String COMMODITY_ID;
    private String EVALUATE_GRADE = "0";
    private String EVALUATE_CONTGENT;
    private String DESCRIBE_IDENTICAL;
    private String BUSINESS_SERVE;
    private String LOGISTICS_SERVE;
    private String BUSINESS_DELIVERY;
    private String LOGISTICS_DELIVERY;
    private String IMG_PATH; //商品图片
    private List<String> IMG;

    public String getCOMMODITY_ORDER_ID() {
        return COMMODITY_ORDER_ID;
    }

    public void setCOMMODITY_ORDER_ID(String COMMODITY_ORDER_ID) {
        this.COMMODITY_ORDER_ID = COMMODITY_ORDER_ID;
    }

    public String getCOMMODITY_ID() {
        return COMMODITY_ID;
    }

    public void setCOMMODITY_ID(String COMMODITY_ID) {
        this.COMMODITY_ID = COMMODITY_ID;
    }

    public String getEVALUATE_GRADE() {
        return EVALUATE_GRADE;
    }

    public void setEVALUATE_GRADE(String EVALUATE_GRADE) {
        this.EVALUATE_GRADE = EVALUATE_GRADE;
    }

    public String getEVALUATE_CONTGENT() {
        return EVALUATE_CONTGENT;
    }

    public void setEVALUATE_CONTGENT(String EVALUATE_CONTGENT) {
        this.EVALUATE_CONTGENT = EVALUATE_CONTGENT;
    }

    public String getDESCRIBE_IDENTICAL() {
        return DESCRIBE_IDENTICAL;
    }

    public void setDESCRIBE_IDENTICAL(String DESCRIBE_IDENTICAL) {
        this.DESCRIBE_IDENTICAL = DESCRIBE_IDENTICAL;
    }

    public String getBUSINESS_SERVE() {
        return BUSINESS_SERVE;
    }

    public void setBUSINESS_SERVE(String BUSINESS_SERVE) {
        this.BUSINESS_SERVE = BUSINESS_SERVE;
    }

    public String getLOGISTICS_SERVE() {
        return LOGISTICS_SERVE;
    }

    public void setLOGISTICS_SERVE(String LOGISTICS_SERVE) {
        this.LOGISTICS_SERVE = LOGISTICS_SERVE;
    }

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

    public List<String> getIMG() {
        return IMG;
    }

    public void setIMG(List<String> IMG) {
        this.IMG = IMG;
    }

    public String getIMG_PATH() {
        return IMG_PATH;
    }

    public void setIMG_PATH(String IMG_PATH) {
        this.IMG_PATH = IMG_PATH;
    }
}
