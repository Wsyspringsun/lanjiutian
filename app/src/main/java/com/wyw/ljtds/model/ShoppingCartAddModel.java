package com.wyw.ljtds.model;

/**
 * Created by Administrator on 2017/4/13 0013.
 */

public class ShoppingCartAddModel extends BaseModel {
    //颜色  产地
    private String COMMODITY_COLOR;
    //id
    private String COMMODITY_ID;
    //规格
    private String COMMODITY_SIZE;
    //数量
    private String EXCHANGE_QUANLITY;
    //店铺id
    private String INS_USER_ID;

    private String COMMODITY_COLOR_ID;
    private String COMMODITY_SIZE_ID;

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

    public String getEXCHANGE_QUANLITY() {
        return EXCHANGE_QUANLITY;
    }

    public void setEXCHANGE_QUANLITY(String EXCHANGE_QUANLITY) {
        this.EXCHANGE_QUANLITY = EXCHANGE_QUANLITY;
    }

    public String getINS_USER_ID() {
        return INS_USER_ID;
    }

    public void setINS_USER_ID(String INS_USER_ID) {
        this.INS_USER_ID = INS_USER_ID;
    }
}
