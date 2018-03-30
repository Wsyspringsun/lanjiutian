package com.wyw.ljtmgr.model;

import java.math.BigDecimal;

/**
 * Created by wsy on 18-3-11.
 * 退货 信息
 */
public class ReturnGoodsInfo {
    private BigDecimal returnMoney;
    private String returnStatus;
    private String returnOrChange;
    private String returnReason;
    private String returnRemarks;
    private String[] imgPathList;

    public BigDecimal getReturnMoney() {
        return returnMoney;
    }

    public void setReturnMoney(BigDecimal returnMoney) {
        this.returnMoney = returnMoney;
    }

    public String getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(String returnStatus) {
        this.returnStatus = returnStatus;
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

    public String[] getImgPathList() {
        return imgPathList;
    }

    public void setImgPathList(String[] imgPathList) {
        this.imgPathList = imgPathList;
    }
}
