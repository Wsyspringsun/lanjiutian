package com.wyw.ljtsp.model;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/5.
 */

public class OrderDetailModel {
    private String insDate;

    private String updDate;

    private String paymentMethod;

    private String groupStatus;

    private String courier;//派送员

    private String courierMobile;//派送员电话

    private String logisticsInsDate;//发货时间

    private String userAddressId;

    private List<OrderDetail> detailList;

    public String getInsDate() {
        return insDate;
    }

    public void setInsDate(String insDate) {
        this.insDate = insDate;
    }

    public String getUpdDate() {
        return updDate;
    }

    public void setUpdDate(String updDate) {
        this.updDate = updDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(String groupStatus) {
        this.groupStatus = groupStatus;
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

    public String getLogisticsInsDate() {
        return logisticsInsDate;
    }

    public void setLogisticsInsDate(String logisticsInsDate) {
        this.logisticsInsDate = logisticsInsDate;
    }

    public String getUserAddressId() {
        return userAddressId;
    }

    public void setUserAddressId(String userAddressId) {
        this.userAddressId = userAddressId;
    }

    public List<OrderDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<OrderDetail> detailList) {
        this.detailList = detailList;
    }
}
