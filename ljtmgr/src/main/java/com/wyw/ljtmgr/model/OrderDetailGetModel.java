package com.wyw.ljtmgr.model;

/**
 * Created by Administrator on 2017/8/7.
 */

public class OrderDetailGetModel {
    private String orderGroupId;

    private String orderTradeId;

    private String oidGroupId;

    public String getOrderTradeId() {
        return orderTradeId;
    }

    public void setOrderTradeId(String orderTradeId) {
        this.orderTradeId = orderTradeId;
    }

    public String getOidGroupId() {
        return oidGroupId;
    }

    public void setOidGroupId(String oidGroupId) {
        this.oidGroupId = oidGroupId;
    }

    public String getOrderGroupId() {
        return orderGroupId;
    }

    public void setOrderGroupId(String orderGroupId) {
        this.orderGroupId = orderGroupId;
    }
}
