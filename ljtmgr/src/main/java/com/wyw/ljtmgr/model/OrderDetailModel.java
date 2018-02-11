package com.wyw.ljtwl.model;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/8/5.
 */

public class OrderDetailModel extends ServerResponse {
    private String orderId;
    private String insDate;

    private String updDate;

    private String paymentMethod;

    private String groupStatus;

    private String courier;//派送员

    private String courierMobile;//派送员电话

    private String logisticsInsDate;//发货时间

    private String userAddressId;
    private String receiverAddress = "";
    private String receiverName = "";
    private String receiverPhone = "";
    private String remark = "";
    private String payAmount;
    private String postageMoneyAll;
    private String invoiceFlg;
    private String invoiceTitle;
    private String invoiceTax;
    private String distance;
    private String distributionDate;
    private String costMoneyAll;
    private String discount;
    private String lat;
    private String lng;

    public String getCostMoneyAll() {
        return costMoneyAll;
    }

    public void setCostMoneyAll(String costMoneyAll) {
        this.costMoneyAll = costMoneyAll;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getPostageMoneyAll() {
        return postageMoneyAll;
    }

    public void setPostageMoneyAll(String postageMoneyAll) {
        this.postageMoneyAll = postageMoneyAll;
    }

    public String getInvoiceFlg() {
        return invoiceFlg;
    }

    public void setInvoiceFlg(String invoiceFlg) {
        this.invoiceFlg = invoiceFlg;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getInvoiceTax() {
        return invoiceTax;
    }

    public void setInvoiceTax(String invoiceTax) {
        this.invoiceTax = invoiceTax;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDistributionDate() {
        return distributionDate;
    }

    public void setDistributionDate(String distributionDate) {
        this.distributionDate = distributionDate;
    }

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

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
