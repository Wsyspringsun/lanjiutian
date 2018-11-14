package com.wyw.ljtmgr.model;

import java.util.List;

/**
 * Created by Administrator on 2017/8/5.
 */

public class AwardModel {
    private String integralDrawLogId;
    private String awardId;
    private String awardName;
    private String oidUserId;
    private String userMobile;
    private String userName;
    private String busno;
    private String busname;
    private String orgPrincipal;
    private String orgMobile;
    private String orgAddress;
    private String validFlg;
    private Long insDate;
    private Long updDate;

    public String getIntegralDrawLogId() {
        return integralDrawLogId;
    }

    public void setIntegralDrawLogId(String integralDrawLogId) {
        this.integralDrawLogId = integralDrawLogId;
    }

    public String getAwardId() {
        return awardId;
    }

    public void setAwardId(String awardId) {
        this.awardId = awardId;
    }

    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        this.awardName = awardName;
    }

    public String getOidUserId() {
        return oidUserId;
    }

    public void setOidUserId(String oidUserId) {
        this.oidUserId = oidUserId;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBusno() {
        return busno;
    }

    public void setBusno(String busno) {
        this.busno = busno;
    }

    public String getBusname() {
        return busname;
    }

    public void setBusname(String busname) {
        this.busname = busname;
    }

    public String getOrgPrincipal() {
        return orgPrincipal;
    }

    public void setOrgPrincipal(String orgPrincipal) {
        this.orgPrincipal = orgPrincipal;
    }

    public String getOrgMobile() {
        return orgMobile;
    }

    public void setOrgMobile(String orgMobile) {
        this.orgMobile = orgMobile;
    }

    public String getOrgAddress() {
        return orgAddress;
    }

    public void setOrgAddress(String orgAddress) {
        this.orgAddress = orgAddress;
    }

    public String getValidFlg() {
        return validFlg;
    }

    public void setValidFlg(String validFlg) {
        this.validFlg = validFlg;
    }

    public Long getInsDate() {
        return insDate;
    }

    public void setInsDate(Long insDate) {
        this.insDate = insDate;
    }

    public Long getUpdDate() {
        return updDate;
    }

    public void setUpdDate(Long updDate) {
        this.updDate = updDate;
    }
}
