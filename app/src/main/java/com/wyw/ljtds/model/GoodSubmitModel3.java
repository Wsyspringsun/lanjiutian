package com.wyw.ljtds.model;

/**
 * Created by Administrator on 2017/4/16 0016.
 */

public class GoodSubmitModel3 extends BaseModel {
    private String COMMODITY_COLOR;
    private String COMMODITY_ID;
    private String COMMODITY_SIZE;
//    private String COST_MONEY;
    private int EXCHANGE_QUANLITY;
    private String COMMODITY_NAME;
    private String COMMODITY_COLOR_ID;
    private String COMMODITY_SIZE_ID;
    private String COMMODITY_ORDER_ID;

    public String getCOMMODITY_ORDER_ID() {
        return COMMODITY_ORDER_ID;
    }

    public void setCOMMODITY_ORDER_ID(String COMMODITY_ORDER_ID) {
        this.COMMODITY_ORDER_ID = COMMODITY_ORDER_ID;
    }

    public String getCOMMODITY_COLOR_ID() {
        return COMMODITY_COLOR_ID;
    }

    public void setCOMMODITY_COLOR_ID(String COMMODITY_COLOR_ID) {
        this.COMMODITY_COLOR_ID = COMMODITY_COLOR_ID;
    }

    public String getCOMMODITY_SIZE_ID() {
        return COMMODITY_SIZE_ID;
    }

    public void setCOMMODITY_SIZE_ID(String COMMODITY_SIZE_ID) {
        this.COMMODITY_SIZE_ID = COMMODITY_SIZE_ID;
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

    public int getEXCHANGE_QUANLITY() {
        return EXCHANGE_QUANLITY;
    }

    public void setEXCHANGE_QUANLITY(int EXCHANGE_QUANLITY) {
        this.EXCHANGE_QUANLITY = EXCHANGE_QUANLITY;
    }

    public String getCOMMODITY_NAME() {
        return COMMODITY_NAME;
    }

    public void setCOMMODITY_NAME(String COMMODITY_NAME) {
        this.COMMODITY_NAME = COMMODITY_NAME;
    }
}
