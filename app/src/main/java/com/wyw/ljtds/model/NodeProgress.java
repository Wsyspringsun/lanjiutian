package com.wyw.ljtds.model;

import com.wyw.ljtds.model.BaseModel;

/**
 * Created by Administrator on 2017/4/30 0030.
 */

public class NodeProgress extends BaseModel {
    //配送员
    private String COURIER;
    //车辆信息
    private String COURIER_CAR;
    //手机号
    private String COURIER_MOBILE;
    //负责配送的门店
    private String LOGISTICS_COMPANY;
    //物流信息id
    private String LOGISTICS_COMPANY_ID;
    //详情信息
    private String LOGISTICS_DETAIL;

    private long INS_DATE;

    public String getCOURIER() {
        return COURIER;
    }

    public void setCOURIER(String COURIER) {
        this.COURIER = COURIER;
    }

    public String getCOURIER_CAR() {
        return COURIER_CAR;
    }

    public void setCOURIER_CAR(String COURIER_CAR) {
        this.COURIER_CAR = COURIER_CAR;
    }

    public String getLOGISTICS_COMPANY() {
        return LOGISTICS_COMPANY;
    }

    public void setLOGISTICS_COMPANY(String LOGISTICS_COMPANY) {
        this.LOGISTICS_COMPANY = LOGISTICS_COMPANY;
    }

    public String getCOURIER_MOBILE() {
        return COURIER_MOBILE;
    }

    public void setCOURIER_MOBILE(String COURIER_MOBILE) {
        this.COURIER_MOBILE = COURIER_MOBILE;
    }

    public String getLOGISTICS_COMPANY_ID() {
        return LOGISTICS_COMPANY_ID;
    }

    public void setLOGISTICS_COMPANY_ID(String LOGISTICS_COMPANY_ID) {
        this.LOGISTICS_COMPANY_ID = LOGISTICS_COMPANY_ID;
    }

    public String getLOGISTICS_DETAIL() {
        return LOGISTICS_DETAIL;
    }

    public void setLOGISTICS_DETAIL(String LOGISTICS_DETAIL) {
        this.LOGISTICS_DETAIL = LOGISTICS_DETAIL;
    }

    public long getINS_DATE() {
        return INS_DATE;
    }

    public void setINS_DATE(long INS_DATE) {
        this.INS_DATE = INS_DATE;
    }
}
