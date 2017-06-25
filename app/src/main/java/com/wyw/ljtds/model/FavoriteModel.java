package com.wyw.ljtds.model;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/3/16 0016.
 */

public class FavoriteModel extends BaseModel {
    private String commodityId;
    private String goodsFlg;
    private BigDecimal costMoney;
    private String imgPath;
    private String title;
    private String favoritesGoodsId;
    private boolean check;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getGoodsFlg() {
        return goodsFlg;
    }

    public void setGoodsFlg(String goodsFlg) {
        this.goodsFlg = goodsFlg;
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

    public String getFavoritesGoodsId() {
        return favoritesGoodsId;
    }

    public void setFavoritesGoodsId(String favoritesGoodsId) {
        this.favoritesGoodsId = favoritesGoodsId;
    }
}
