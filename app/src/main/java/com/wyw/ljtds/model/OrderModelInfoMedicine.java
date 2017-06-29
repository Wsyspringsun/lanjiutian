package com.wyw.ljtds.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017/4/23 0023.
 */

public class OrderModelInfoMedicine extends BaseModel {
    private String DISTRIBUTION_MODE;
    private int GROUP_EXCHANGE_QUANLITY;
    private BigDecimal POINT_MONEY;
    private BigDecimal PAY_AMOUNT;
    private BigDecimal GROUP_MONEY_ALL;
    private String INVOICE_CONTENT;
    private String INVOICE_FLG;
    private String INVOICE_TITLE;
    private String INVOICE_TYPE;
    private String OID_GROUP_ID;
    private String OID_GROUP_NAME;
    private String ORDER_GROUP_ID;
    private String ORDER_TRADE_ID;
    private BigDecimal POSTAGE;
    private String STATUS;
    private String USER_ADDRESS_ID;
    private List<Goods> DETAILS;
    private Long COMPLETE_DATE;
    private Long CREATE_DATE;
    private String CONTACT_TEL;
    private CreatOrderModel.USER_ADDRESS USER_ADDRESS;

    public String getDISTRIBUTION_MODE() {
        return DISTRIBUTION_MODE;
    }

    public void setDISTRIBUTION_MODE(String DISTRIBUTION_MODE) {
        this.DISTRIBUTION_MODE = DISTRIBUTION_MODE;
    }

    public int getGROUP_EXCHANGE_QUANLITY() {
        return GROUP_EXCHANGE_QUANLITY;
    }

    public void setGROUP_EXCHANGE_QUANLITY(int GROUP_EXCHANGE_QUANLITY) {
        this.GROUP_EXCHANGE_QUANLITY = GROUP_EXCHANGE_QUANLITY;
    }

    public Long getCOMPLETE_DATE() {
        return COMPLETE_DATE;
    }

    public void setCOMPLETE_DATE(Long COMPLETE_DATE) {
        this.COMPLETE_DATE = COMPLETE_DATE;
    }

    public Long getCREATE_DATE() {
        return CREATE_DATE;
    }

    public void setCREATE_DATE(Long CREATE_DATE) {
        this.CREATE_DATE = CREATE_DATE;
    }

    public String getCONTACT_TEL() {
        return CONTACT_TEL;
    }

    public void setCONTACT_TEL(String CONTACT_TEL) {
        this.CONTACT_TEL = CONTACT_TEL;
    }

    public BigDecimal getGROUP_MONEY_ALL() {
        return GROUP_MONEY_ALL;
    }

    public void setGROUP_MONEY_ALL(BigDecimal GROUP_MONEY_ALL) {
        this.GROUP_MONEY_ALL = GROUP_MONEY_ALL;
    }

    public String getINVOICE_CONTENT() {
        return INVOICE_CONTENT;
    }

    public void setINVOICE_CONTENT(String INVOICE_CONTENT) {
        this.INVOICE_CONTENT = INVOICE_CONTENT;
    }

    public String getINVOICE_FLG() {
        return INVOICE_FLG;
    }

    public void setINVOICE_FLG(String INVOICE_FLG) {
        this.INVOICE_FLG = INVOICE_FLG;
    }

    public String getINVOICE_TITLE() {
        return INVOICE_TITLE;
    }

    public void setINVOICE_TITLE(String INVOICE_TITLE) {
        this.INVOICE_TITLE = INVOICE_TITLE;
    }

    public String getINVOICE_TYPE() {
        return INVOICE_TYPE;
    }

    public void setINVOICE_TYPE(String INVOICE_TYPE) {
        this.INVOICE_TYPE = INVOICE_TYPE;
    }

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

    public String getORDER_GROUP_ID() {
        return ORDER_GROUP_ID;
    }

    public void setORDER_GROUP_ID(String ORDER_GROUP_ID) {
        this.ORDER_GROUP_ID = ORDER_GROUP_ID;
    }

    public String getORDER_TRADE_ID() {
        return ORDER_TRADE_ID;
    }

    public void setORDER_TRADE_ID(String ORDER_TRADE_ID) {
        this.ORDER_TRADE_ID = ORDER_TRADE_ID;
    }

    public BigDecimal getPOSTAGE() {
        return POSTAGE;
    }

    public void setPOSTAGE(BigDecimal POSTAGE) {
        this.POSTAGE = POSTAGE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public List<Goods> getDETAILS() {
        return DETAILS;
    }

    public void setDETAILS(List<Goods> DETAILS) {
        this.DETAILS = DETAILS;
    }

    public CreatOrderModel.USER_ADDRESS getUSER_ADDRESS() {
        return USER_ADDRESS;
    }

    public BigDecimal getPOINT_MONEY() {
        return POINT_MONEY;
    }

    public void setPOINT_MONEY(BigDecimal POINT_MONEY) {
        this.POINT_MONEY = POINT_MONEY;
    }

    public BigDecimal getPAY_AMOUNT() {
        return PAY_AMOUNT;
    }

    public String getUSER_ADDRESS_ID() {
        return USER_ADDRESS_ID;
    }

    public void setUSER_ADDRESS_ID(String USER_ADDRESS_ID) {
        this.USER_ADDRESS_ID = USER_ADDRESS_ID;
    }

    public void setPAY_AMOUNT(BigDecimal PAY_AMOUNT) {
        this.PAY_AMOUNT = PAY_AMOUNT;
    }

    public void setUSER_ADDRESS(CreatOrderModel.USER_ADDRESS USER_ADDRESS) {
        this.USER_ADDRESS = USER_ADDRESS;
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
        //商品订单号
        private String COMMODITY_ORDER_ID;
        //商品订单状态
        private String ORDER_STATUS;

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

        public String getCOMMODITY_ORDER_ID() {
            return COMMODITY_ORDER_ID;
        }

        public void setCOMMODITY_ORDER_ID(String COMMODITY_ORDER_ID) {
            this.COMMODITY_ORDER_ID = COMMODITY_ORDER_ID;
        }

        public String getORDER_STATUS() {
            return ORDER_STATUS;
        }

        public void setORDER_STATUS(String ORDER_STATUS) {
            this.ORDER_STATUS = ORDER_STATUS;
        }
    }


}
