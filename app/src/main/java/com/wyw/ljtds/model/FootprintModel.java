package com.wyw.ljtds.model;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/3/16 0016.
 */

public class FootprintModel extends BaseModel {
    //足迹id
    private int CLICKS_RANKING_ID;
    //商品id
    private String COMMODITY_ID;
    //点击次数
    private int SEARCH_NUM;
    //足迹添加时间
    private String INS_DATE;
    //商品名
    private String TITLE;
    //商品参数
    private String COMMODITY_PARAMETER;
    //介绍
    private String DESCRIPTION;
    //市场价
    private BigDecimal MARKET_PRICE;
    //现价
    private BigDecimal COST_MONEY;
    //图片
    private String IMG_PATH;

    public int getCLICKS_RANKING_ID() {
        return CLICKS_RANKING_ID;
    }

    public void setCLICKS_RANKING_ID(int CLICKS_RANKING_ID) {
        this.CLICKS_RANKING_ID = CLICKS_RANKING_ID;
    }

    public String getCOMMODITY_ID() {
        return COMMODITY_ID;
    }

    public void setCOMMODITY_ID(String COMMODITY_ID) {
        this.COMMODITY_ID = COMMODITY_ID;
    }

    public int getSEARCH_NUM() {
        return SEARCH_NUM;
    }

    public void setSEARCH_NUM(int SEARCH_NUM) {
        this.SEARCH_NUM = SEARCH_NUM;
    }

    public String getINS_DATE() {
        return INS_DATE;
    }

    public void setINS_DATE(String INS_DATE) {
        this.INS_DATE = INS_DATE;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getCOMMODITY_PARAMETER() {
        return COMMODITY_PARAMETER;
    }

    public void setCOMMODITY_PARAMETER(String COMMODITY_PARAMETER) {
        this.COMMODITY_PARAMETER = COMMODITY_PARAMETER;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public BigDecimal getMARKET_PRICE() {
        return MARKET_PRICE;
    }

    public void setMARKET_PRICE(BigDecimal MARKET_PRICE) {
        this.MARKET_PRICE = MARKET_PRICE;
    }

    public BigDecimal getCOST_MONEY() {
        return COST_MONEY;
    }

    public void setCOST_MONEY(BigDecimal COST_MONEY) {
        this.COST_MONEY = COST_MONEY;
    }

    public String getIMG_PATH() {
        return IMG_PATH;
    }

    public void setIMG_PATH(String IMG_PATH) {
        this.IMG_PATH = IMG_PATH;
    }

}
