package com.wyw.ljtds.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class CommodityDetailsModel extends BaseModel {
    //商品id
    private String commodityId;
    //商品类型id
    private String commodityTypeId;
    //名称
    private String title;
    //点击量
    private int clickNum;
    //评价数量
    private int EVALUATE_CNT;
    //品牌
    private String commodityBrand;
    //商品概述
    private String desc;
    //商品详情html
    private String htmlPath;
    //是否vip
    private String vipFlg;
    //营销标志(0新品，1置顶，2推荐，3活动，4品牌，5折扣，6医药，7热卖)
    private String topFlg;
    //店铺id
    private String oidGroupId;
    //店铺名字
    private String groupName;
    //颜色列表
    private List<ColorList> colorList;
    //规格
    private String commodityParameter;
    //0没有收藏 1 是收藏
    private String favorited;
    //评论
    private List<MedicineDetailsEvaluateModel> evaluateList;

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

    public String getCommodityParameter() {
        return commodityParameter;
    }

    public void setCommodityParameter(String commodityParameter) {
        this.commodityParameter = commodityParameter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getClickNum() {
        return clickNum;
    }

    public void setClickNum(int clickNum) {
        this.clickNum = clickNum;
    }

    public String getCommodityBrand() {
        return commodityBrand;
    }

    public void setCommodityBrand(String commodityBrand) {
        this.commodityBrand = commodityBrand;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getHtmlPath() {
        return htmlPath;
    }

    public void setHtmlPath(String htmlPath) {
        this.htmlPath = htmlPath;
    }

    public String getVipFlg() {
        return vipFlg;
    }

    public void setVipFlg(String vipFlg) {
        this.vipFlg = vipFlg;
    }

    public String getTopFlg() {
        return topFlg;
    }

    public void setTopFlg(String topFlg) {
        this.topFlg = topFlg;
    }

    public String getFavorited() {
        return favorited;
    }

    public void setFavorited(String favorited) {
        this.favorited = favorited;
    }

    public String getOidGroupId() {
        return oidGroupId;
    }

    public void setOidGroupId(String oidGroupId) {
        this.oidGroupId = oidGroupId;
    }

    public List<ColorList> getColorList() {
        return colorList;
    }

    public void setColorList(List<ColorList> colorList) {
        this.colorList = colorList;
    }

    public List<MedicineDetailsEvaluateModel> getEvaluateList() {
        return evaluateList;
    }

    public void setEvaluateList(List<MedicineDetailsEvaluateModel> evaluateList) {
        this.evaluateList = evaluateList;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getEVALUATE_CNT() {
        return EVALUATE_CNT;
    }

    public void setEVALUATE_CNT(int EVALUATE_CNT) {
        this.EVALUATE_CNT = EVALUATE_CNT;
    }

    public class ColorList{
        //颜色code
        private String colorCode;
        //颜色名字
        private String colorName;
        //颜色id
        private String commodityColorId;
        //颜色对应的商品id
        private String commodityId;
        //尺寸列表
        private List<SizeList> sizeList;

        public String getColorCode() {
            return colorCode;
        }

        public void setColorCode(String colorCode) {
            this.colorCode = colorCode;
        }

        public String getColorName() {
            return colorName;
        }

        public void setColorName(String colorName) {
            this.colorName = colorName;
        }

        public String getCommodityColorId() {
            return commodityColorId;
        }

        public void setCommodityColorId(String commodityColorId) {
            this.commodityColorId = commodityColorId;
        }

        public String getCommodityId() {
            return commodityId;
        }

        public void setCommodityId(String commodityId) {
            this.commodityId = commodityId;
        }

        public List<SizeList> getSizeList() {
            return sizeList;
        }

        public void setSizeList(List<SizeList> sizeList) {
            this.sizeList = sizeList;
        }
    }

    public class SizeList{
        //尺寸对应的商品id
        private String commodityId;
        //尺寸对应的颜色id
        private String commodityColorId;
        //尺寸
        private String commoditySize;
        //尺寸id
        private String commoditySizeId;
        //销售价格
        private BigDecimal costMoney;
        //市场价
        private BigDecimal marketPrice;
        //可购买库存
        private int quanlityUsable;
        //总库存
        private int quanlityAll;
        //图片
        private String imgPath;
        private String imgPath2;
        private String imgPath3;
        private String imgPath4;
        private String imgPath5;
        private String imgPath6;

        public String getCommodityId() {
            return commodityId;
        }

        public void setCommodityId(String commodityId) {
            this.commodityId = commodityId;
        }

        public String getCommodityColorId() {
            return commodityColorId;
        }

        public void setCommodityColorId(String commodityColorId) {
            this.commodityColorId = commodityColorId;
        }

        public String getCommoditySize() {
            return commoditySize;
        }

        public void setCommoditySize(String commoditySize) {
            this.commoditySize = commoditySize;
        }

        public String getCommoditySizeId() {
            return commoditySizeId;
        }

        public void setCommoditySizeId(String commoditySizeId) {
            this.commoditySizeId = commoditySizeId;
        }

        public BigDecimal getCostMoney() {
            return costMoney;
        }

        public void setCostMoney(BigDecimal costMoney) {
            this.costMoney = costMoney;
        }

        public BigDecimal getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(BigDecimal marketPrice) {
            this.marketPrice = marketPrice;
        }

        public int getQuanlityUsable() {
            return quanlityUsable;
        }

        public void setQuanlityUsable(int quanlityUsable) {
            this.quanlityUsable = quanlityUsable;
        }

        public int getQuanlityAll() {
            return quanlityAll;
        }

        public void setQuanlityAll(int quanlityAll) {
            this.quanlityAll = quanlityAll;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public String getImgPath2() {
            return imgPath2;
        }

        public void setImgPath2(String imgPath2) {
            this.imgPath2 = imgPath2;
        }

        public String getImgPath3() {
            return imgPath3;
        }

        public void setImgPath3(String imgPath3) {
            this.imgPath3 = imgPath3;
        }

        public String getImgPath4() {
            return imgPath4;
        }

        public void setImgPath4(String imgPath4) {
            this.imgPath4 = imgPath4;
        }

        public String getImgPath5() {
            return imgPath5;
        }

        public void setImgPath5(String imgPath5) {
            this.imgPath5 = imgPath5;
        }

        public String getImgPath6() {
            return imgPath6;
        }

        public void setImgPath6(String imgPath6) {
            this.imgPath6 = imgPath6;
        }
    }
}
