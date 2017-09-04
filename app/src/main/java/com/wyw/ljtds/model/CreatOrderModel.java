package com.wyw.ljtds.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/13 0013.
 */

public class CreatOrderModel extends BaseModel implements Serializable {
    //配送时间 起始
    private String DISTRIBUTION_DATE_START;
    //配送时间 截止
    private String DISTRIBUTION_DATE_END;
    //花费积分数
    private String COST_POINT;
    //支付方式
    private String PAYMENT_METHOD;
    //使用电子币标识
    private String COIN_FLG;
    //使用邮费抵用券标识
    private String POSTAGE_FLG;
    //应付金额
    private String PAY_AMOUNT;
    //积分抵用金额
    private String POINT_MONEY;
    //商品总价额
    private String TRADE_MONEY_ALL;
    //账户下可用积分数量
    private String USER_POINT;
    //地址
    private USER_ADDRESS USER_ADDRESS;
    //店铺list
    private List<Business> DETAILS;

    public List<Business> getDETAILS() {
        return DETAILS;
    }

    public void setDETAILS(List<Business> DETAILS) {
        this.DETAILS = DETAILS;
    }

    public String getCOST_POINT() {
        return COST_POINT;
    }

    public void setCOST_POINT(String COST_POINT) {
        this.COST_POINT = COST_POINT;
    }

    public String getPAYMENT_METHOD() {
        return PAYMENT_METHOD;
    }

    public void setPAYMENT_METHOD(String PAYMENT_METHOD) {
        this.PAYMENT_METHOD = PAYMENT_METHOD;
    }

    public String getPAY_AMOUNT() {
        return PAY_AMOUNT;
    }

    public void setPAY_AMOUNT(String PAY_AMOUNT) {
        this.PAY_AMOUNT = PAY_AMOUNT;
    }

    public String getPOINT_MONEY() {
        return POINT_MONEY;
    }

    public void setPOINT_MONEY(String POINT_MONEY) {
        this.POINT_MONEY = POINT_MONEY;
    }

    public String getTRADE_MONEY_ALL() {
        return TRADE_MONEY_ALL;
    }

    public void setTRADE_MONEY_ALL(String TRADE_MONEY_ALL) {
        this.TRADE_MONEY_ALL = TRADE_MONEY_ALL;
    }

    public String getUSER_POINT() {
        return USER_POINT;
    }

    public void setUSER_POINT(String USER_POINT) {
        this.USER_POINT = USER_POINT;
    }

    public CreatOrderModel.USER_ADDRESS getUSER_ADDRESS() {
        return USER_ADDRESS;
    }

    public void setUSER_ADDRESS(CreatOrderModel.USER_ADDRESS USER_ADDRESS) {
        this.USER_ADDRESS = USER_ADDRESS;
    }

    public String getDISTRIBUTION_DATE_START() {
        return DISTRIBUTION_DATE_START;
    }

    public void setDISTRIBUTION_DATE_START(String DISTRIBUTION_DATE_START) {
        this.DISTRIBUTION_DATE_START = DISTRIBUTION_DATE_START;
    }

    public String getDISTRIBUTION_DATE_END() {
        return DISTRIBUTION_DATE_END;
    }

    public void setDISTRIBUTION_DATE_END(String DISTRIBUTION_DATE_END) {
        this.DISTRIBUTION_DATE_END = DISTRIBUTION_DATE_END;
    }

    public String getPOSTAGE_FLG() {
        return POSTAGE_FLG;
    }

    public void setPOSTAGE_FLG(String POSTAGE_FLG) {
        this.POSTAGE_FLG = POSTAGE_FLG;
    }

    public String getCOIN_FLG() {
        return COIN_FLG;
    }

    public void setCOIN_FLG(String COIN_FLG) {
        this.COIN_FLG = COIN_FLG;
    }

    public class USER_ADDRESS {
        private String ADDRESS_DETAIL;
        private int ADDRESS_ID;
        private String CONSIGNEE_MOBILE;
        private String CONSIGNEE_NAME;
        private String CITY;
        private String PROVINCE;
        private String CONSIGNEE_ADDRESS;
        private int CONSIGNEE_PROVINCE;
        private int CONSIGNEE_CITY;

        public String getCITY() {
            return CITY;
        }

        public void setCITY(String CITY) {
            this.CITY = CITY;
        }

        public String getPROVINCE() {
            return PROVINCE;
        }

        public void setPROVINCE(String PROVINCE) {
            this.PROVINCE = PROVINCE;
        }

        public String getCONSIGNEE_ADDRESS() {
            return CONSIGNEE_ADDRESS;
        }

        public void setCONSIGNEE_ADDRESS(String CONSIGNEE_ADDRESS) {
            this.CONSIGNEE_ADDRESS = CONSIGNEE_ADDRESS;
        }

        public String getADDRESS_DETAIL() {
            return ADDRESS_DETAIL;
        }

        public void setADDRESS_DETAIL(String ADDRESS_DETAIL) {
            this.ADDRESS_DETAIL = ADDRESS_DETAIL;
        }

        public int getADDRESS_ID() {
            return ADDRESS_ID;
        }

        public void setADDRESS_ID(int ADDRESS_ID) {
            this.ADDRESS_ID = ADDRESS_ID;
        }

        public String getCONSIGNEE_MOBILE() {
            return CONSIGNEE_MOBILE;
        }

        public void setCONSIGNEE_MOBILE(String CONSIGNEE_MOBILE) {
            this.CONSIGNEE_MOBILE = CONSIGNEE_MOBILE;
        }

        public String getCONSIGNEE_NAME() {
            return CONSIGNEE_NAME;
        }

        public void setCONSIGNEE_NAME(String CONSIGNEE_NAME) {
            this.CONSIGNEE_NAME = CONSIGNEE_NAME;
        }

        public int getCONSIGNEE_PROVINCE() {
            return CONSIGNEE_PROVINCE;
        }

        public void setCONSIGNEE_PROVINCE(int CONSIGNEE_PROVINCE) {
            this.CONSIGNEE_PROVINCE = CONSIGNEE_PROVINCE;
        }

        public int getCONSIGNEE_CITY() {
            return CONSIGNEE_CITY;
        }

        public void setCONSIGNEE_CITY(int CONSIGNEE_CITY) {
            this.CONSIGNEE_CITY = CONSIGNEE_CITY;
        }


    }
}
