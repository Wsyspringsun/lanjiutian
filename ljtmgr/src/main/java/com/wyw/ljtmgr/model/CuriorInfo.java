package com.wyw.ljtwl.model;

import java.util.List;

/**
 * Created by Administrator on 2017/8/5.
 */

public class CuriorInfo {
    private String orderTradeId;
    private String courier;
    private String courierMobile;
    private String courierCar;

    public String getOrderTradeId() {
        return orderTradeId;
    }

    public void setOrderTradeId(String orderTradeId) {
        this.orderTradeId = orderTradeId;
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

    public String getCourierCar() {
        return courierCar;
    }

    public void setCourierCar(String courierCar) {
        this.courierCar = courierCar;
    }
}
