package com.wyw.ljtds.model;

import com.wyw.ljtds.utils.Utils;

/**
 * Created by Administrator on 2017/3/29 0029.
 */

public class MedicineListModel extends GoodsModel {
    //id
    private String WAREID;
    //名字
    private String WARENAME;
    //价格
    private String SALEPRICE;
    //介绍
    private String TREATMENT;
    //图片
    private String IMG_PATH;
    //店铺id
    private String GROUPID;
    private String LNG; // 经度
    private String LAT; // 纬度
    private String BUSAVLID_FLG = ""; // 营业状态  0:营业中，1:休息中
    private String DISTANCE_TEXT = ""; // 距离
    private String DURATION_TEXT = ""; // 时间
    private String QISONG = "-"; // 起送价格
    private String BAOYOU = "-"; // 包邮价格
    private String POSTAGE = "-"; // 配送费
    private String COMMODITY_BRAND = ""; //品牌
    private String COMMODITY_PARAMETER = "";//促销内容
    private String WARESPEC = ""; //规格
    private String LOGISTICS_COMPANY = ""; //店铺名称
    private String LOGISTICS_COMPANY_ID;//店铺 id

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

    public String getBUSAVLID_FLG() {
        return BUSAVLID_FLG;
    }

    public void setBUSAVLID_FLG(String BUSAVLID_FLG) {
        this.BUSAVLID_FLG = BUSAVLID_FLG;
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

    public String getPOSTAGE() {
        return POSTAGE;
    }

    public void setPOSTAGE(String POSTAGE) {
        this.POSTAGE = POSTAGE;
    }

    public String getGROUPID() {
        return GROUPID;
    }

    public void setGROUPID(String GROUPID) {
        this.GROUPID = GROUPID;
    }

    public String getWAREID() {
        return WAREID;
    }

    public void setWAREID(String WAREID) {
        this.WAREID = WAREID;
    }

    public String getWARENAME() {
        return WARENAME;
    }

    public void setWARENAME(String WARENAME) {
        this.WARENAME = WARENAME;
    }

    public String getSALEPRICE() {
        return Utils.formatFee(SALEPRICE);
    }

    public void setSALEPRICE(String SALEPRICE) {
        this.SALEPRICE = SALEPRICE;
    }

    public String getTREATMENT() {
        return TREATMENT;
    }

    public void setTREATMENT(String TREATMENT) {
        this.TREATMENT = TREATMENT;
    }

    public String getIMG_PATH() {
        return IMG_PATH;
    }

    public void setIMG_PATH(String IMG_PATH) {
        this.IMG_PATH = IMG_PATH;
    }

    public String getCOMMODITY_BRAND() {
        if (COMMODITY_BRAND == null) return "";
        return COMMODITY_BRAND;
    }

    public void setCOMMODITY_BRAND(String COMMODITY_BRAND) {
        this.COMMODITY_BRAND = COMMODITY_BRAND;
    }

    public String getCOMMODITY_PARAMETER() {
        return COMMODITY_PARAMETER;
    }

    public void setCOMMODITY_PARAMETER(String COMMODITY_PARAMETER) {
        this.COMMODITY_PARAMETER = COMMODITY_PARAMETER;
    }

    public String getWARESPEC() {
        return WARESPEC;
    }

    public void setWARESPEC(String WARESPEC) {
        this.WARESPEC = WARESPEC;
    }

    public String getBUSAVLID_FLGText() {
        if ("0".equals(BUSAVLID_FLG)) {
            return "营业中";
        } else if ("1".equals(BUSAVLID_FLG)) {
            return "休息中";
        } else {
            return "未知";
        }

    }
}
