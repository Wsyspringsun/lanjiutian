package com.wyw.ljtds.model;

import java.util.List;

/**
 * Created by Administrator on 2017/6/29 0029.
 */

public class GoodsHandingModel extends BaseModel {
    private String returnGoodsHandingId;
    private String orderTradeId;
    private String orderGroupId;
    private String commodityOrderId;
    private List<MedicineOrder> commodityOrderList;
    private String oidGroupId;
    private String groupName;
    private String oidUserId;
    private String quanlity;
    private String returnQuanlity;
    private String costMoney;
    private String returnMoney;
    private String returnStatus;
    private String userAddress;
    private String returnOrChange;
    private String returnReason;
    private String returnRemarks;
    private String imgPath;
    private String imgPath2;
    private String imgPath3;
    private String imgPath4;
    private String imgPath5;
    private String logisticsOrderId;
    private String examineVerifyReason;
    private String delFlg;
    private String insDate;
    private String updDate;
    private String[] imgs;

    public void setReturnGoodsHandingId(String returnGoodsHandingId) {
        this.returnGoodsHandingId = returnGoodsHandingId;
    }

    public void setOrderTradeId(String orderTradeId) {
        this.orderTradeId = orderTradeId;
    }

    public void setOrderGroupId(String orderGroupId) {
        this.orderGroupId = orderGroupId;
    }

    public void setOidGroupId(String oidGroupId) {
        this.oidGroupId = oidGroupId;
    }

    public void setOidUserId(String oidUserId) {
        this.oidUserId = oidUserId;
    }

    public void setQuanlity(String quanlity) {
        this.quanlity = quanlity;
    }

    public void setReturnQuanlity(String returnQuanlity) {
        this.returnQuanlity = returnQuanlity;
    }

    public void setCostMoney(String costMoney) {
        this.costMoney = costMoney;
    }

    public void setReturnMoney(String returnMoney) {
        this.returnMoney = returnMoney;
    }

    public void setReturnStatus(String returnStatus) {
        this.returnStatus = returnStatus;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public void setReturnOrChange(String returnOrChange) {
        this.returnOrChange = returnOrChange;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public void setReturnRemarks(String returnRemarks) {
        this.returnRemarks = returnRemarks;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public void setImgPath2(String imgPath2) {
        this.imgPath2 = imgPath2;
    }

    public void setImgPath3(String imgPath3) {
        this.imgPath3 = imgPath3;
    }

    public void setImgPath4(String imgPath4) {
        this.imgPath4 = imgPath4;
    }

    public void setImgPath5(String imgPath5) {
        this.imgPath5 = imgPath5;
    }

    public void setLogisticsOrderId(String logisticsOrderId) {
        this.logisticsOrderId = logisticsOrderId;
    }

    public void setExamineVerifyReason(String examineVerifyReason) {
        this.examineVerifyReason = examineVerifyReason;
    }

    public void setDelFlg(String delFlg) {
        this.delFlg = delFlg;
    }

    public void setInsDate(String insDate) {
        this.insDate = insDate;
    }

    public void setUpdDate(String updDate) {
        this.updDate = updDate;
    }

    public String getReturnGoodsHandingId() {
        return this.returnGoodsHandingId;
    }

    public String getOrderTradeId() {
        return this.orderTradeId;
    }

    public String getOrderGroupId() {
        return this.orderGroupId;
    }

    public String getOidGroupId() {
        return this.oidGroupId;
    }

    public String getOidUserId() {
        return this.oidUserId;
    }

    public String getQuanlity() {
        return this.quanlity;
    }

    public String getReturnQuanlity() {
        return this.returnQuanlity;
    }

    public String getCostMoney() {
        return this.costMoney;
    }

    public String getReturnMoney() {
        return this.returnMoney;
    }

    public String getReturnStatus() {
        return this.returnStatus;
    }

    public String getUserAddress() {
        return this.userAddress;
    }

    public String getReturnOrChange() {
        return this.returnOrChange;
    }

    public String getReturnReason() {
        return this.returnReason;
    }

    public String getReturnRemarks() {
        return this.returnRemarks;
    }

    public String getImgPath() {
        return this.imgPath;
    }

    public String getImgPath2() {
        return this.imgPath2;
    }

    public String getImgPath3() {
        return this.imgPath3;
    }

    public String getImgPath4() {
        return this.imgPath4;
    }

    public String getImgPath5() {
        return this.imgPath5;
    }

    public String getLogisticsOrderId() {
        return this.logisticsOrderId;
    }

    public String getExamineVerifyReason() {
        return this.examineVerifyReason;
    }

    public String getDelFlg() {
        return this.delFlg;
    }

    public String getInsDate() {
        return this.insDate;
    }

    public String getUpdDate() {
        return this.updDate;
    }


    public String[] getImgs() {
        return imgs;
    }

    public void setImgs(String[] imgs) {
        this.imgs = imgs;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<MedicineOrder> getCommodityOrderList() {
        return commodityOrderList;
    }

    public void setCommodityOrderList(List<MedicineOrder> commodityOrderList) {
        this.commodityOrderList = commodityOrderList;
    }

    public String getCommodityOrderId() {
        return commodityOrderId;
    }

    public void setCommodityOrderId(String commodityOrderId) {
        this.commodityOrderId = commodityOrderId;
    }
}
