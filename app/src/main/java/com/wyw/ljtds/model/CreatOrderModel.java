package com.wyw.ljtds.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/13 0013.
 */

public class CreatOrderModel extends OrderTradeDto implements Serializable {
    private String INVOICE_FLG = "0";
    private String INVOICE_TYPE;
    private String INVOICE_ID;
    private String INVOICE_TAX;
    private String INVOICE_ORG;
    private String INVOICE_TITLE;
    //发票种类 0：明细  1办公  2：家居   3：药品   4：耗材
    private String INVOICE_CONTENT;

    private String DISTRIBUTION_MODE = "0";

    public String getINVOICE_FLG() {
        return INVOICE_FLG;
    }

    public void setINVOICE_FLG(String INVOICE_FLG) {
        this.INVOICE_FLG = INVOICE_FLG;
    }

    public String getINVOICE_TYPE() {
        return INVOICE_TYPE;
    }

    public void setINVOICE_TYPE(String INVOICE_TYPE) {
        this.INVOICE_TYPE = INVOICE_TYPE;
    }

    public String getINVOICE_ID() {
        return INVOICE_ID;
    }

    public void setINVOICE_ID(String INVOICE_ID) {
        this.INVOICE_ID = INVOICE_ID;
    }

    public String getINVOICE_TAX() {
        return INVOICE_TAX;
    }

    public void setINVOICE_TAX(String INVOICE_TAX) {
        this.INVOICE_TAX = INVOICE_TAX;
    }

    public String getINVOICE_TITLE() {
        return INVOICE_TITLE;
    }

    public void setINVOICE_TITLE(String INVOICE_TITLE) {
        this.INVOICE_TITLE = INVOICE_TITLE;
    }

    public String getINVOICE_CONTENT() {
        return INVOICE_CONTENT;
    }

    public void setINVOICE_CONTENT(String INVOICE_CONTENT) {
        this.INVOICE_CONTENT = INVOICE_CONTENT;
    }

    public String getDISTRIBUTION_MODE() {
        return DISTRIBUTION_MODE;
    }

    public void setDISTRIBUTION_MODE(String DISTRIBUTION_MODE) {
        this.DISTRIBUTION_MODE = DISTRIBUTION_MODE;
    }

    public String getINVOICE_ORG() {
        return INVOICE_ORG;
    }

    public void setINVOICE_ORG(String INVOICE_ORG) {
        this.INVOICE_ORG = INVOICE_ORG;
    }

    /*    //获取的 地址 id
            private String ADDRESS_ID;
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

            public String getADDRESS_ID() {
                return ADDRESS_ID;
            }

            public void setADDRESS_ID(String ADDRESS_ID) {
                this.ADDRESS_ID = ADDRESS_ID;
            }

            */
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
