package com.wyw.ljtds.model;

/**
 * Created by Administrator on 2017/6/10 0010.
 */

public class NewsModel {

    private String ID;
    private String STATUS;
    private String NEWS_TYPE_ID;
    private String SHOW_TYPE_ID;
    private String TITLE;
    private String TITLE_SUMMARY;
    private String IMG_PATH;
    private String SUMMARY;
    private long PUBLISH_DATE;
    private String AUTHOR;
    private String DEL_FLG;
    private String UPD_USER_ID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getNEWS_TYPE_ID() {
        return NEWS_TYPE_ID;
    }

    public void setNEWS_TYPE_ID(String NEWS_TYPE_ID) {
        this.NEWS_TYPE_ID = NEWS_TYPE_ID;
    }

    public String getSHOW_TYPE_ID() {
        return SHOW_TYPE_ID;
    }

    public void setSHOW_TYPE_ID(String SHOW_TYPE_ID) {
        this.SHOW_TYPE_ID = SHOW_TYPE_ID;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getTITLE_SUMMARY() {
        return TITLE_SUMMARY;
    }

    public void setTITLE_SUMMARY(String TITLE_SUMMARY) {
        this.TITLE_SUMMARY = TITLE_SUMMARY;
    }

    public String getIMG_PATH() {
        return IMG_PATH;
    }

    public void setIMG_PATH(String IMG_PATH) {
        this.IMG_PATH = IMG_PATH;
    }

    public String getSUMMARY() {
        return SUMMARY;
    }

    public void setSUMMARY(String SUMMARY) {
        this.SUMMARY = SUMMARY;
    }

    public long getPUBLISH_DATE() {
        return PUBLISH_DATE;
    }

    public void setPUBLISH_DATE(long PUBLISH_DATE) {
        this.PUBLISH_DATE = PUBLISH_DATE;
    }

    public String getAUTHOR() {
        return AUTHOR;
    }

    public void setAUTHOR(String AUTHOR) {
        this.AUTHOR = AUTHOR;
    }

    public String getDEL_FLG() {
        return DEL_FLG;
    }

    public void setDEL_FLG(String DEL_FLG) {
        this.DEL_FLG = DEL_FLG;
    }

    public String getUPD_USER_ID() {
        return UPD_USER_ID;
    }

    public void setUPD_USER_ID(String UPD_USER_ID) {
        this.UPD_USER_ID = UPD_USER_ID;
    }
}
