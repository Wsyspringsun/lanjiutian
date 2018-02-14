package com.wyw.ljtmgr.model;

/**
 * save info for logistic
 * Created by wsy on 18-1-19.
 */
public class LogisticInfo {
    private String orderGroupId;
    private String logisticsFlg;
    private String logisticsCompanyName;
    private String logisticsNumber;
    private String courier;
    private String courierMobile;

    public String getOrderGroupId() {
        return orderGroupId;
    }

    public void setOrderGroupId(String orderGroupId) {
        this.orderGroupId = orderGroupId;
    }

    public String getLogisticsFlg() {
        return logisticsFlg;
    }

    public void setLogisticsFlg(String logisticsFlg) {
        this.logisticsFlg = logisticsFlg;
    }

    public String getLogisticsCompanyName() {
        return logisticsCompanyName;
    }

    public void setLogisticsCompanyName(String logisticsCompanyName) {
        this.logisticsCompanyName = logisticsCompanyName;
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }

    public String getCourier() {
        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public String getCourierMobile() {
        return courierMobile;
    }

    public void setCourierMobile(String courierMobile) {
        this.courierMobile = courierMobile;
    }
}
