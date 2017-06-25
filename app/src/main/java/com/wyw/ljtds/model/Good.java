package com.wyw.ljtds.model;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class Good extends BaseModel{
    //名字
    private String COMMODITY_NAME;
    //产地  颜色
    private String COMMODITY_COLOR;
    //id
    private String COMMODITY_ID;
    //规格
    private String COMMODITY_SIZE;
    //单价
    private String COST_MONEY;
    //数量
    private String EXCHANGE_QUANLITY;
    //总价
    private String COST_MONEY_ALL;
    //图片
    private String IMG_PATH;
    private String COMMODITY_SIZE_ID;
    private String COMMODITY_COLOR_ID;
    private String COMMODITY_ORDER_ID;

    public String getCOMMODITY_ORDER_ID() {
        return COMMODITY_ORDER_ID;
    }

    public void setCOMMODITY_ORDER_ID(String COMMODITY_ORDER_ID) {
        this.COMMODITY_ORDER_ID = COMMODITY_ORDER_ID;
    }

    public String getCOMMODITY_SIZE_ID() {
        return COMMODITY_SIZE_ID;
    }

    public void setCOMMODITY_SIZE_ID(String COMMODITY_SIZE_ID) {
        this.COMMODITY_SIZE_ID = COMMODITY_SIZE_ID;
    }

    public String getCOMMODITY_COLOR_ID() {
        return COMMODITY_COLOR_ID;
    }

    public void setCOMMODITY_COLOR_ID(String COMMODITY_COLOR_ID) {
        this.COMMODITY_COLOR_ID = COMMODITY_COLOR_ID;
    }

    public String getIMG_PATH() {
        return IMG_PATH;
    }

    public void setIMG_PATH(String IMG_PATH) {
        this.IMG_PATH = IMG_PATH;
    }

    public String getCOMMODITY_NAME() {
        return COMMODITY_NAME;
    }

    public void setCOMMODITY_NAME(String COMMODITY_NAME) {
        this.COMMODITY_NAME = COMMODITY_NAME;
    }

    public String getCOMMODITY_COLOR() {
        return COMMODITY_COLOR;
    }

    public void setCOMMODITY_COLOR(String COMMODITY_COLOR) {
        this.COMMODITY_COLOR = COMMODITY_COLOR;
    }

    public String getCOMMODITY_ID() {
        return COMMODITY_ID;
    }

    public void setCOMMODITY_ID(String COMMODITY_ID) {
        this.COMMODITY_ID = COMMODITY_ID;
    }

    public String getCOMMODITY_SIZE() {
        return COMMODITY_SIZE;
    }

    public void setCOMMODITY_SIZE(String COMMODITY_SIZE) {
        this.COMMODITY_SIZE = COMMODITY_SIZE;
    }

    public String getCOST_MONEY() {
        return COST_MONEY;
    }

    public void setCOST_MONEY(String COST_MONEY) {
        this.COST_MONEY = COST_MONEY;
    }

    public String getEXCHANGE_QUANLITY() {
        return EXCHANGE_QUANLITY;
    }

    public void setEXCHANGE_QUANLITY(String EXCHANGE_QUANLITY) {
        this.EXCHANGE_QUANLITY = EXCHANGE_QUANLITY;
    }

    public String getCOST_MONEY_ALL() {
        return COST_MONEY_ALL;
    }

    public void setCOST_MONEY_ALL(String COST_MONEY_ALL) {
        this.COST_MONEY_ALL = COST_MONEY_ALL;
    }
}
