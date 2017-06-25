package com.wyw.ljtds.model;

import java.math.BigDecimal;
import java.util.List;

public class LogisticsModel {
    private String COMMODITY_ORDER_ID;
    private String LOGISTICS_ORDER_ID;
    private Order ORDER;
    private List<NodeProgress> DETAILS;

    public String getCOMMODITY_ORDER_ID() {
        return COMMODITY_ORDER_ID;
    }

    public void setCOMMODITY_ORDER_ID(String COMMODITY_ORDER_ID) {
        this.COMMODITY_ORDER_ID = COMMODITY_ORDER_ID;
    }

    public String getLOGISTICS_ORDER_ID() {
        return LOGISTICS_ORDER_ID;
    }

    public void setLOGISTICS_ORDER_ID(String LOGISTICS_ORDER_ID) {
        this.LOGISTICS_ORDER_ID = LOGISTICS_ORDER_ID;
    }

    public Order getORDER() {
        return ORDER;
    }

    public void setORDER(Order ORDER) {
        this.ORDER = ORDER;
    }

    public List<NodeProgress> getDETAILS() {
        return DETAILS;
    }

    public void setDETAILS(List<NodeProgress> DETAILS) {
        this.DETAILS = DETAILS;
    }

    public class Order{
        private String STATUS;
        private BigDecimal POSTAGE;
        private String OID_GROUP_NAME;
        private String OID_GROUP_ID;
        private List<Goods> DETAILS;

        public String getSTATUS() {
            return STATUS;
        }

        public void setSTATUS(String STATUS) {
            this.STATUS = STATUS;
        }

        public BigDecimal getPOSTAGE() {
            return POSTAGE;
        }

        public void setPOSTAGE(BigDecimal POSTAGE) {
            this.POSTAGE = POSTAGE;
        }

        public String getOID_GROUP_NAME() {
            return OID_GROUP_NAME;
        }

        public void setOID_GROUP_NAME(String OID_GROUP_NAME) {
            this.OID_GROUP_NAME = OID_GROUP_NAME;
        }

        public String getOID_GROUP_ID() {
            return OID_GROUP_ID;
        }

        public void setOID_GROUP_ID(String OID_GROUP_ID) {
            this.OID_GROUP_ID = OID_GROUP_ID;
        }

        public List<Goods> getDETAILS() {
            return DETAILS;
        }

        public void setDETAILS(List<Goods> DETAILS) {
            this.DETAILS = DETAILS;
        }
    }

    public class Goods{
        //商品id
        private String COMMODITY_ID;
        //商品名字
        private String COMMODITY_NAME;
        private String COMMODITY_COLOR;
        private String COMMODITY_SIZE;
        private int EXCHANGE_QUANLITY;
        private BigDecimal COST_MONEY;
        private String IMG_PATH;

        public String getIMG_PATH() {
            return IMG_PATH;
        }

        public void setIMG_PATH(String IMG_PATH) {
            this.IMG_PATH = IMG_PATH;
        }

        public String getCOMMODITY_ID() {
            return COMMODITY_ID;
        }

        public void setCOMMODITY_ID(String COMMODITY_ID) {
            this.COMMODITY_ID = COMMODITY_ID;
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

        public BigDecimal getCOST_MONEY() {
            return COST_MONEY;
        }

        public void setCOST_MONEY(BigDecimal COST_MONEY) {
            this.COST_MONEY = COST_MONEY;
        }
    }
}