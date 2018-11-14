package com.wyw.ljtds.model;

import android.test.ApplicationTestCase;

import com.wyw.ljtds.R;
import com.wyw.ljtds.config.MyApplication;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class Business extends OrderGroupDto {
    public static Map<String, String> mapFapiaoCatText = new HashMap<>();

    static {
        mapFapiaoCatText.put("0", "发票明细");
        mapFapiaoCatText.put("1", "办公用品");
        mapFapiaoCatText.put("2", "家居用品");
        mapFapiaoCatText.put("3", "药品");
        mapFapiaoCatText.put("4", "耗材");
    }
/*
    //配送方式  0送货上门  1门店自取
    private String DISTRIBUTION_MODE;
    //店铺价格小计
    private String GROUP_MONEY_ALL;
    //店铺数量小计
    private String GROUP_EXCHANGE_QUANLITY;
    //是否开发票 0 不开 1 开
    private String INVOICE_FLG;
    //发票类型 0：纸质  1：电子
    private String INVOICE_TYPE;
    //发票抬头
    private String INVOICE_TITLE;
    //发票种类 0：明细  1办公  2：家居   3：药品   4：耗材
    private String INVOICE_CONTENT;
    //发票 税务登记 号码
    private String INVOICE_TAX;
    //店铺id
    private String OID_GROUP_ID;
    //店铺名字
    private String OID_GROUP_NAME;
    //运费
    private String POSTAGE;
    private String POST_NUM;

    private String ELECTRONIC_MONEY;
    private String ELECTRONIC_USEABLE_MONEY;
    //商品list
    private List<Good> DETAILS;

    public String getGROUP_MONEY_ALL() {
        return GROUP_MONEY_ALL;
    }

    public void setGROUP_MONEY_ALL(String GROUP_MONEY_ALL) {
        this.GROUP_MONEY_ALL = GROUP_MONEY_ALL;
    }

    public String getDISTRIBUTION_MODE() {
        return DISTRIBUTION_MODE;
    }

    public void setDISTRIBUTION_MODE(String DISTRIBUTION_MODE) {
        this.DISTRIBUTION_MODE = DISTRIBUTION_MODE;
    }

    public String getINVOICE_TYPE() {
        return INVOICE_TYPE;
    }

    public void setINVOICE_TYPE(String INVOICE_TYPE) {
        this.INVOICE_TYPE = INVOICE_TYPE;
    }

    public String getINVOICE_FLG() {
        return INVOICE_FLG;
    }

    public void setINVOICE_FLG(String INVOICE_FLG) {
        this.INVOICE_FLG = INVOICE_FLG;
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

    public String getPOSTAGE() {
        return POSTAGE;
    }

    public void setPOSTAGE(String POSTAGE) {
        this.POSTAGE = POSTAGE;
    }

    public List<Good> getDETAILS() {
        return DETAILS;
    }

    public void setDETAILS(List<Good> DETAILS) {
        this.DETAILS = DETAILS;
    }

    public String getGROUP_EXCHANGE_QUANLITY() {
        return GROUP_EXCHANGE_QUANLITY;
    }

    public void setGROUP_EXCHANGE_QUANLITY(String GROUP_EXCHANGE_QUANLITY) {
        this.GROUP_EXCHANGE_QUANLITY = GROUP_EXCHANGE_QUANLITY;
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

    public String getELECTRONIC_MONEY() {
        return ELECTRONIC_MONEY;
    }

    public void setELECTRONIC_MONEY(String ELECTRONIC_MONEY) {
        this.ELECTRONIC_MONEY = ELECTRONIC_MONEY;
    }

    public String getELECTRONIC_USEABLE_MONEY() {
        return ELECTRONIC_USEABLE_MONEY;
    }

    public void setELECTRONIC_USEABLE_MONEY(String ELECTRONIC_USEABLE_MONEY) {
        this.ELECTRONIC_USEABLE_MONEY = ELECTRONIC_USEABLE_MONEY;
    }

    public String getPOST_NUM() {
        return POST_NUM;
    }

    public void setPOST_NUM(String POST_NUM) {
        this.POST_NUM = POST_NUM;
    }

    public String getINVOICE_TAX() {
        return INVOICE_TAX;
    }

    public void setINVOICE_TAX(String INVOICE_TAX) {
        this.INVOICE_TAX = INVOICE_TAX;
    }*/
}