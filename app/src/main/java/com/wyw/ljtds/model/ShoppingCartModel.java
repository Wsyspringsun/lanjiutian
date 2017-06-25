package com.wyw.ljtds.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017/4/13 0013.
 */

public class ShoppingCartModel extends BaseModel {
    private List<Group> DETAILS;

    public List<Group> getDETAILS() {
        return DETAILS;
    }

    public void setDETAILS(List<Group> DETAILS) {
        this.DETAILS = DETAILS;
    }

    public class Group{
        private String OID_GROUP_ID;
        private String OID_GROUP_NAME;
        private List<Goods> DETAILS;

        public String getOID_GROUP_ID() {
            return OID_GROUP_ID;
        }

        public void setOID_GROUP_ID(String OID_GROUP_ID) {
            this.OID_GROUP_ID = OID_GROUP_ID;
        }

        public String getOID_GROUP_NAME() {
            return OID_GROUP_NAME;
        }

        public void setOID_GROUP_NAME(String OID_GROUP_NAME) {
            this.OID_GROUP_NAME = OID_GROUP_NAME;
        }

        public List<Goods> getDETAILS() {
            return DETAILS;
        }

        public void setDETAILS(List<Goods> DETAILS) {
            this.DETAILS = DETAILS;
        }
    }

    public class Goods{
        private String WARETYPE;
        private String COMMODITY_ORDER_ID;
        private String COMMODITY_ID;
        private String COMMODITY_NAME;
        private String COMMODITY_COLOR;
        private String COMMODITY_SIZE;
        private int EXCHANGE_QUANLITY;
        private double COST_MONEY;
        private double COST_MONEY_ALL;
        private String GROUPID;
        private String GROUPNAME;
        private String PRESCRIPTION_FLG;
        private String IMG_PATH;
        private String INS_USER_ID;

        public String getINS_USER_ID() {
            return INS_USER_ID;
        }

        public void setINS_USER_ID(String INS_USER_ID) {
            this.INS_USER_ID = INS_USER_ID;
        }

        public String getWARETYPE() {
            return WARETYPE;
        }

        public void setWARETYPE(String WARETYPE) {
            this.WARETYPE = WARETYPE;
        }

        public String getPRESCRIPTION_FLG() {
            return PRESCRIPTION_FLG;
        }

        public void setPRESCRIPTION_FLG(String PRESCRIPTION_FLG) {
            this.PRESCRIPTION_FLG = PRESCRIPTION_FLG;
        }

        public String getIMG_PATH() {
            return IMG_PATH;
        }

        public void setIMG_PATH(String IMG_PATH) {
            this.IMG_PATH = IMG_PATH;
        }

        public String getCOMMODITY_ORDER_ID() {
            return COMMODITY_ORDER_ID;
        }

        public void setCOMMODITY_ORDER_ID(String COMMODITY_ORDER_ID) {
            this.COMMODITY_ORDER_ID = COMMODITY_ORDER_ID;
        }

        public String getCOMMODITY_ID() {
            return COMMODITY_ID;
        }

        public void setCOMMODITY_ID(String COMMODITY_ID) {
            this.COMMODITY_ID = COMMODITY_ID;
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

        public int getEXCHANGE_QUANLITY() {
            return EXCHANGE_QUANLITY;
        }

        public void setEXCHANGE_QUANLITY(int EXCHANGE_QUANLITY) {
            this.EXCHANGE_QUANLITY = EXCHANGE_QUANLITY;
        }

        public double getCOST_MONEY() {
            return COST_MONEY;
        }

        public void setCOST_MONEY(double COST_MONEY) {
            this.COST_MONEY = COST_MONEY;
        }

        public double getCOST_MONEY_ALL() {
            return COST_MONEY_ALL;
        }

        public void setCOST_MONEY_ALL(double COST_MONEY_ALL) {
            this.COST_MONEY_ALL = COST_MONEY_ALL;
        }

        public String getGROUPID() {
            return GROUPID;
        }

        public void setGROUPID(String GROUPID) {
            this.GROUPID = GROUPID;
        }

        public String getGROUPNAME() {
            return GROUPNAME;
        }

        public void setGROUPNAME(String GROUPNAME) {
            this.GROUPNAME = GROUPNAME;
        }

        public String getCOMMODITY_NAME() {
            return COMMODITY_NAME;
        }

        public void setCOMMODITY_NAME(String COMMODITY_NAME) {
            this.COMMODITY_NAME = COMMODITY_NAME;
        }
    }
}
