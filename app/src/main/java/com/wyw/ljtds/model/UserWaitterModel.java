package com.wyw.ljtds.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/3/20 0020.
 */

public class UserWaitterModel extends BaseModel {
    private String USERID;//客服工号
    private String USERNAME;//客服姓名
    private String TEL;//客服电话
    private String LOGISTICS_COMPANY;//门店名称
    private String ORG_PRINCIPAL;//门店联系人
    private String ORG_MOBILE;//门店联系电话
    private String ORG_ADDRESS;//门店地址
    private String CUSTOMER_MOBILE;//客服投诉电话

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getTEL() {
        return TEL;
    }

    public void setTEL(String TEL) {
        this.TEL = TEL;
    }

    public String getLOGISTICS_COMPANY() {
        return LOGISTICS_COMPANY;
    }

    public void setLOGISTICS_COMPANY(String LOGISTICS_COMPANY) {
        this.LOGISTICS_COMPANY = LOGISTICS_COMPANY;
    }

    public String getORG_PRINCIPAL() {
        return ORG_PRINCIPAL;
    }

    public void setORG_PRINCIPAL(String ORG_PRINCIPAL) {
        this.ORG_PRINCIPAL = ORG_PRINCIPAL;
    }

    public String getORG_MOBILE() {
        return ORG_MOBILE;
    }

    public void setORG_MOBILE(String ORG_MOBILE) {
        this.ORG_MOBILE = ORG_MOBILE;
    }

    public String getORG_ADDRESS() {
        return ORG_ADDRESS;
    }

    public void setORG_ADDRESS(String ORG_ADDRESS) {
        this.ORG_ADDRESS = ORG_ADDRESS;
    }

    public String getCUSTOMER_MOBILE() {
        return CUSTOMER_MOBILE;
    }

    public void setCUSTOMER_MOBILE(String CUSTOMER_MOBILE) {
        this.CUSTOMER_MOBILE = CUSTOMER_MOBILE;
    }
}
