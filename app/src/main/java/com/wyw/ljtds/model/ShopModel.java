package com.wyw.ljtds.model;

/**
 * Created by wsy on 17-8-25.
 */
public class ShopModel {
    private String LAT;
    private String LNG;
    private String LOGISTICS_COMPANY;
    private String DISTANCE_TEXT;
    private String DURATION_TEXT;
    private String QISONG;
    private String BAOYOU;

    public String getLAT() {
        return LAT;
    }

    public void setLAT(String LAT) {
        this.LAT = LAT;
    }

    public String getLNG() {
        return LNG;
    }

    public void setLNG(String LNG) {
        this.LNG = LNG;
    }

    public String getLOGISTICS_COMPANY() {
        return LOGISTICS_COMPANY;
    }

    public void setLOGISTICS_COMPANY(String LOGISTICS_COMPANY) {
        this.LOGISTICS_COMPANY = LOGISTICS_COMPANY;
    }

    public String getDISTANCE_TEXT() {
        return DISTANCE_TEXT;
    }

    public void setDISTANCE_TEXT(String DISTANCE_TEXT) {
        this.DISTANCE_TEXT = DISTANCE_TEXT;
    }

    public String getDURATION_TEXT() {
        return DURATION_TEXT;
    }

    public void setDURATION_TEXT(String DURATION_TEXT) {
        this.DURATION_TEXT = DURATION_TEXT;
    }

    public String getQISONG() {
        return QISONG;
    }

    public void setQISONG(String QISONG) {
        this.QISONG = QISONG;
    }

    public String getBAOYOU() {
        return BAOYOU;
    }

    public void setBAOYOU(String BAOYOU) {
        this.BAOYOU = BAOYOU;
    }
}
