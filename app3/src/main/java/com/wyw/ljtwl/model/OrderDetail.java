package com.wyw.ljtwl.model;

/**
 * Created by Administrator on 2017/8/5.
 */

public class OrderDetail {

    private String commodityId;

    private String commodityColor;

    private String exchangeQuanlity;

    private Double costMoneyAll;

    private String commoditySize;

    private String title;

    private String imgPath;

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getCommodityColor() {
        return commodityColor;
    }

    public void setCommodityColor(String commodityColor) {
        this.commodityColor = commodityColor;
    }

    public String getExchangeQuanlity() {
        return exchangeQuanlity;
    }

    public void setExchangeQuanlity(String exchangeQuanlity) {
        this.exchangeQuanlity = exchangeQuanlity;
    }

    public Double getCostMoneyAll() {
        return costMoneyAll;
    }

    public void setCostMoneyAll(Double costMoneyAll) {
        this.costMoneyAll = costMoneyAll;
    }

    public String getCommoditySize() {
        return commoditySize;
    }

    public void setCommoditySize(String commoditySize) {
        this.commoditySize = commoditySize;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
