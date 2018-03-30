package com.wyw.ljtds.model;

import com.wyw.ljtds.utils.Utils;

/**
 * Created by wsy on 17-11-29.
 */

public class OrderCommDto {
    private String COMMODITY_ORDER_ID;
    private String OID_USER_ID;
    private String COMMODITY_ID;
    private String COMMODITY_NAME = "";
    private String ORDER_GROUP_ID;
    private String COMMODITY_COLOR = "";
    private String COMMODITY_SIZE = "";
    private Integer EXCHANGE_QUANLITY;
    private String LOGISTICS_COMPANY_ID; //店铺编号
    private String LOGISTICS_COMPANY; //店铺名称
    private String COST_MONEY;
    private String COST_MONEY_ALL;
    private String SINGLE_ELECTRONIC_MONEY;
    private String GIVEAWAY;
    private String ORDER_SOURCE;
    private String ORDER_STATUS;
    private String DEL_FLG;
    private String INS_USER_ID;
    private String UPD_USER_ID;
    private String INS_DATE;
    private String UPD_DATE;
    private String IMG_PATH;
    private String IS_RETURNGOODS; //1:可以退  0 不可退

    public String getCOMMODITY_ORDER_ID() {
        return COMMODITY_ORDER_ID;
    }

    public void setCOMMODITY_ORDER_ID(String COMMODITY_ORDER_ID) {
        this.COMMODITY_ORDER_ID = COMMODITY_ORDER_ID;
    }

    public String getOID_USER_ID() {
        return OID_USER_ID;
    }

    public void setOID_USER_ID(String OID_USER_ID) {
        this.OID_USER_ID = OID_USER_ID;
    }

    public String getCOMMODITY_ID() {
        return COMMODITY_ID;
    }

    public void setCOMMODITY_ID(String COMMODITY_ID) {
        this.COMMODITY_ID = COMMODITY_ID;
    }

    public String getORDER_GROUP_ID() {
        return ORDER_GROUP_ID;
    }

    public void setORDER_GROUP_ID(String ORDER_GROUP_ID) {
        this.ORDER_GROUP_ID = ORDER_GROUP_ID;
    }

    public String getCOMMODITY_COLOR() {
        return COMMODITY_COLOR;
    }

    public void setCOMMODITY_COLOR(String COMMODITY_COLOR) {
        this.COMMODITY_COLOR = COMMODITY_COLOR;
    }

    public String getCOMMODITY_SIZE() {
        return COMMODITY_SIZE;
    }

    public void setCOMMODITY_SIZE(String COMMODITY_SIZE) {
        this.COMMODITY_SIZE = COMMODITY_SIZE;
    }

    public Integer getEXCHANGE_QUANLITY() {
        return EXCHANGE_QUANLITY;
    }

    public void setEXCHANGE_QUANLITY(Integer EXCHANGE_QUANLITY) {
        this.EXCHANGE_QUANLITY = EXCHANGE_QUANLITY;
    }

    public String getCOST_MONEY() {
        return Utils.formatFee(COST_MONEY);
    }

    public void setCOST_MONEY(String COST_MONEY) {
        this.COST_MONEY = COST_MONEY;
    }

    public String getCOST_MONEY_ALL() {
        return COST_MONEY_ALL;
    }

    public void setCOST_MONEY_ALL(String COST_MONEY_ALL) {
        this.COST_MONEY_ALL = COST_MONEY_ALL;
    }

    public String getSINGLE_ELECTRONIC_MONEY() {
        return SINGLE_ELECTRONIC_MONEY;
    }

    public void setSINGLE_ELECTRONIC_MONEY(String SINGLE_ELECTRONIC_MONEY) {
        this.SINGLE_ELECTRONIC_MONEY = SINGLE_ELECTRONIC_MONEY;
    }

    public String getGIVEAWAY() {
        return GIVEAWAY;
    }

    public void setGIVEAWAY(String GIVEAWAY) {
        this.GIVEAWAY = GIVEAWAY;
    }

    public String getORDER_SOURCE() {
        return ORDER_SOURCE;
    }

    public void setORDER_SOURCE(String ORDER_SOURCE) {
        this.ORDER_SOURCE = ORDER_SOURCE;
    }

    public String getORDER_STATUS() {
        return ORDER_STATUS;
    }

    public void setORDER_STATUS(String ORDER_STATUS) {
        this.ORDER_STATUS = ORDER_STATUS;
    }

    public String getDEL_FLG() {
        return DEL_FLG;
    }

    public void setDEL_FLG(String DEL_FLG) {
        this.DEL_FLG = DEL_FLG;
    }

    public String getINS_USER_ID() {
        return INS_USER_ID;
    }

    public void setINS_USER_ID(String INS_USER_ID) {
        this.INS_USER_ID = INS_USER_ID;
    }

    public String getUPD_USER_ID() {
        return UPD_USER_ID;
    }

    public void setUPD_USER_ID(String UPD_USER_ID) {
        this.UPD_USER_ID = UPD_USER_ID;
    }

    public String getINS_DATE() {
        return INS_DATE;
    }

    public void setINS_DATE(String INS_DATE) {
        this.INS_DATE = INS_DATE;
    }

    public String getUPD_DATE() {
        return UPD_DATE;
    }

    public void setUPD_DATE(String UPD_DATE) {
        this.UPD_DATE = UPD_DATE;
    }

    public String getCOMMODITY_NAME() {
        return COMMODITY_NAME;
    }

    public void setCOMMODITY_NAME(String COMMODITY_NAME) {
        this.COMMODITY_NAME = COMMODITY_NAME;
    }

    public String getBUSNAME() {
        return LOGISTICS_COMPANY;
    }

    public void setBUSNAME(String LOGISTICS_COMPANY) {
        this.LOGISTICS_COMPANY = LOGISTICS_COMPANY;
    }

    public String getBUSNO() {
        return LOGISTICS_COMPANY_ID;
    }

    public void setBUSNO(String LOGISTICS_COMPANY_ID) {
        this.LOGISTICS_COMPANY_ID = LOGISTICS_COMPANY_ID;
    }

    public String getIMG_PATH() {
        return IMG_PATH;
    }

    public void setIMG_PATH(String IMG_PATH) {
        this.IMG_PATH = IMG_PATH;
    }

    public String getIS_RETURNGOODS() {
        return IS_RETURNGOODS;
    }

    public void setIS_RETURNGOODS(String IS_RETURNGOODS) {
        this.IS_RETURNGOODS = IS_RETURNGOODS;
    }
}
