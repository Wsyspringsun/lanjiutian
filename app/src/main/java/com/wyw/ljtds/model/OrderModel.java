package com.wyw.ljtds.model;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class OrderModel extends BaseModel {
    //商品订单id
    private String COMMODITY_ORDER_ID;
    //商品id
    private String COMMODITY_ID;
    //颜色
    private String COMMODITY_COLOR;
    //规格
    private String COMMODITY_SIZE;
    //购买数量
    private int EXCHANGE_QUANLITY;
    //抵用积分
    private BigDecimal COST_POINT;
    //单价
    private BigDecimal COST_MONEY;
    //积分抵用金额
    private BigDecimal POINT_MONEY;
    //应付金额
    private BigDecimal COST_MONEY_ALL;
    //实付金额
    private BigDecimal PAY_AMOUNT;
    //支付方式(0线下医保卡，1支付宝，2银联，3货到付款，4线下其他)
    private String PAYMENT_METHOD;
    //商品订单状态(0待确认，1待付款，2已取消，3购买超时，4已下单，5已发货，6已签收，7申请退货，8退货中，9已退货，10已退款)
    private String STATUS;
    //是否需要开具发票(0不开具，1开具)
    private String INVOICE_FLG;
    //发票类型(0电子发票，1纸质发票)
    private String INVOICE_TYPE;
    //发票id
    private String INVOICE_ID;
    //用户地址
    private String USER_ADDRESS_ID;
    //快递公司名称
    private String LOGISTICS_COMPANY;
    //快递公司id
    private String LOGISTICS_COMPANY_ID;
    //快递单号id
    private String LOGISTICS_ORDER_ID;
    //配送方式(0送货上门，1门店自取)
    private String DISTRIBUTION_MODE;
    //配送时间
    private String DISTRIBUTION_DATE;
    //删除状态
    private String DEL_FLG;
    //商品名字
    private String TITLE;
    //商品介绍
    private String DESCRIPTION;
    //图片
    private String IMG_PATH;
    //市场价  原价
    private BigDecimal MARKET_PRICE;

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

    public BigDecimal getCOST_POINT() {
        return COST_POINT;
    }

    public void setCOST_POINT(BigDecimal COST_POINT) {
        this.COST_POINT = COST_POINT;
    }

    public BigDecimal getCOST_MONEY() {
        return COST_MONEY;
    }

    public void setCOST_MONEY(BigDecimal COST_MONEY) {
        this.COST_MONEY = COST_MONEY;
    }

    public BigDecimal getPOINT_MONEY() {
        return POINT_MONEY;
    }

    public void setPOINT_MONEY(BigDecimal POINT_MONEY) {
        this.POINT_MONEY = POINT_MONEY;
    }

    public BigDecimal getCOST_MONEY_ALL() {
        return COST_MONEY_ALL;
    }

    public void setCOST_MONEY_ALL(BigDecimal COST_MONEY_ALL) {
        this.COST_MONEY_ALL = COST_MONEY_ALL;
    }

    public BigDecimal getPAY_AMOUNT() {
        return PAY_AMOUNT;
    }

    public void setPAY_AMOUNT(BigDecimal PAY_AMOUNT) {
        this.PAY_AMOUNT = PAY_AMOUNT;
    }

    public String getPAYMENT_METHOD() {
        return PAYMENT_METHOD;
    }

    public void setPAYMENT_METHOD(String PAYMENT_METHOD) {
        this.PAYMENT_METHOD = PAYMENT_METHOD;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

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

    public String getUSER_ADDRESS_ID() {
        return USER_ADDRESS_ID;
    }

    public void setUSER_ADDRESS_ID(String USER_ADDRESS_ID) {
        this.USER_ADDRESS_ID = USER_ADDRESS_ID;
    }

    public String getLOGISTICS_COMPANY() {
        return LOGISTICS_COMPANY;
    }

    public void setLOGISTICS_COMPANY(String LOGISTICS_COMPANY) {
        this.LOGISTICS_COMPANY = LOGISTICS_COMPANY;
    }

    public String getLOGISTICS_COMPANY_ID() {
        return LOGISTICS_COMPANY_ID;
    }

    public void setLOGISTICS_COMPANY_ID(String LOGISTICS_COMPANY_ID) {
        this.LOGISTICS_COMPANY_ID = LOGISTICS_COMPANY_ID;
    }

    public String getDISTRIBUTION_MODE() {
        return DISTRIBUTION_MODE;
    }

    public void setDISTRIBUTION_MODE(String DISTRIBUTION_MODE) {
        this.DISTRIBUTION_MODE = DISTRIBUTION_MODE;
    }

    public String getDISTRIBUTION_DATE() {
        return DISTRIBUTION_DATE;
    }

    public void setDISTRIBUTION_DATE(String DISTRIBUTION_DATE) {
        this.DISTRIBUTION_DATE = DISTRIBUTION_DATE;
    }

    public String getDEL_FLG() {
        return DEL_FLG;
    }

    public void setDEL_FLG(String DEL_FLG) {
        this.DEL_FLG = DEL_FLG;
    }

    public String getLOGISTICS_ORDER_ID() {
        return LOGISTICS_ORDER_ID;
    }

    public void setLOGISTICS_ORDER_ID(String LOGISTICS_ORDER_ID) {
        this.LOGISTICS_ORDER_ID = LOGISTICS_ORDER_ID;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getIMG_PATH() {
        return IMG_PATH;
    }

    public void setIMG_PATH(String IMG_PATH) {
        this.IMG_PATH = IMG_PATH;
    }

    public BigDecimal getMARKET_PRICE() {
        return MARKET_PRICE;
    }

    public void setMARKET_PRICE(BigDecimal MARKET_PRICE) {
        this.MARKET_PRICE = MARKET_PRICE;
    }
}
