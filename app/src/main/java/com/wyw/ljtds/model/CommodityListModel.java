package com.wyw.ljtds.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class CommodityListModel extends BaseModel {
    //零元购时的原价
    private String marketPrice;
    //商品id
    private String commodityId;
    //商品类型id
    private String commodityTypeId;
    //商品名称
    private String title;
    //价格
    private BigDecimal costMoney;
    //积分
    private String costPoint;
    //图片
    private String imgPath;
    //点击数量
    private int clickNum;
    //店铺id
    private String oidGroupId;
    //店铺名
    private String groupName;
    //营销标志(0新品，1置顶，2推荐，3活动，4品牌，5折扣，6医药，7热卖)
    private String topFlg;

    public String getOidGroupId() {
        return oidGroupId;
    }

    public void setOidGroupId(String oidGroupId) {
        this.oidGroupId = oidGroupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getCommodityTypeId() {
        return commodityTypeId;
    }

    public void setCommodityTypeId(String commodityTypeId) {
        this.commodityTypeId = commodityTypeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getCostMoney() {
        return costMoney;
    }

    public void setCostMoney(BigDecimal costMoney) {
        this.costMoney = costMoney;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public int getClickNum() {
        return clickNum;
    }

    public void setClickNum(int clickNum) {
        this.clickNum = clickNum;
    }

    public String getTopFlg() {
        return topFlg;
    }

    public void setTopFlg(String topFlg) {
        this.topFlg = topFlg;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getCostPoint() {
        return costPoint;
    }

    public void setCostPoint(String costPoint) {
        this.costPoint = costPoint;
    }
}
