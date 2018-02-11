package com.wyw.ljtds.model;

/**
 * Created by wsy on 17-12-26.
 */

public class MedicineShop {
    public boolean isChecked = false;
    private String LOGISTICS_COMPANY;
    private String LOGISTICS_COMPANY_ID;
    private String ORG_MOBILE;
    private String ORG_PRINCIPAL;
    private String AREA;
    private String LNG;
    private String LAT;
    private String DISTANCE_TEXT;
    private String DURATION_TEXT;
    private String QISONG;
    private String BAOYOU;

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

    public String getORG_PRINCIPAL() {
        return ORG_PRINCIPAL;
    }

    public void setORG_PRINCIPAL(String ORG_PRINCIPAL) {
        this.ORG_PRINCIPAL = ORG_PRINCIPAL;
    }

    public String getAREA() {
        return AREA;
    }

    public void setAREA(String AREA) {
        this.AREA = AREA;
    }

    public String getLNG() {
        return LNG;
    }

    public void setLNG(String LNG) {
        this.LNG = LNG;
    }

    public String getLAT() {
        return LAT;
    }

    public void setLAT(String LAT) {
        this.LAT = LAT;
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

    public String getORG_MOBILE() {
        return ORG_MOBILE;
    }

    public void setORG_MOBILE(String ORG_MOBILE) {
        this.ORG_MOBILE = ORG_MOBILE;
    }
}
