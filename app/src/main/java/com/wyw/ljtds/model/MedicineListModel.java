package com.wyw.ljtds.model;

/**
 * Created by Administrator on 2017/3/29 0029.
 */

public class MedicineListModel extends BaseModel {
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
        return SALEPRICE;
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
}
