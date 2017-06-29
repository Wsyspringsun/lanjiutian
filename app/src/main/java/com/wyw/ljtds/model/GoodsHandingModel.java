package com.wyw.ljtds.model;

/**
 * Created by Administrator on 2017/6/29 0029.
 */

public class GoodsHandingModel extends BaseModel {
    private String[] imgs;
    private String orderGroupId;
    private String commodityOrderId;
    private String returnOrChange;
    private String returnReason;
    private String returnRemarks;

    public String[] getImgs() {
        return imgs;
    }

    public void setImgs(String[] imgs) {
        this.imgs = imgs;
    }

    public String getOrderGroupId() {
        return orderGroupId;
    }

    public void setOrderGroupId(String orderGroupId) {
        this.orderGroupId = orderGroupId;
    }

    public String getCommodityOrderId() {
        return commodityOrderId;
    }

    public void setCommodityOrderId(String commodityOrderId) {
        this.commodityOrderId = commodityOrderId;
    }

    public String getReturnOrChange() {
        return returnOrChange;
    }

    public void setReturnOrChange(String returnOrChange) {
        this.returnOrChange = returnOrChange;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public String getReturnRemarks() {
        return returnRemarks;
    }

    public void setReturnRemarks(String returnRemarks) {
        this.returnRemarks = returnRemarks;
    }
}
