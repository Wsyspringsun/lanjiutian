package com.wyw.ljtds.model;

import android.support.annotation.IntegerRes;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017/3/30 0030.
 */

public class MedicineDetailsModel extends MedicineListModel {
    public static final String PRESCRIPTION_FLG_OTC = "1";
    //商品折扣
    private int COMMODITY_DISCOUNT;
    private Integer SUMQTY;
    //批准文号
    private String FILENO;
    //html
    private String HTML_PATH;
    //图片
    private String[] IMAGES;
    //联系手机
    private String CONTACT_MOBILE;
    //联系 座机
    private String CONTACT_TEL;
    private AddressModel USER_ADDRESS_OBJ;


    private String USER_ADDRESS;
    //最新会员价
    private BigDecimal MEMPRICE;
    private BigDecimal MEMPRICE2;
    private BigDecimal MEMPRICE3;
    private BigDecimal MEMPRICE4;
    private String FLG_DETAIL; //活动消费额抵扣 描述
    //处方标识（1处方，其他 非处方）
    private String PRESCRIPTION_FLG;
    //销售积分
    private BigDecimal PILE;
    //销量
    private int SALE_NUM;
    //营销标识    0:新品,1：置顶,2:推荐,3:活动，4：品牌，5：折扣，6：医药，7:热卖
    private String TOP_FLG;
    //计量单位
    private String WAREUNIT;
    //产地
    private String PROD_ADD;
    //厂家名
    private String PRODUCER;
    //储存环境
    private String STORE_REQ;
    //活动结束时间
    private String ACTIVE_END_DATE;
    //活动开始时间
    private String ACTIVE_START_DATE;
    //主要成分
    private String MAIN_ELEMENT;
    //说明书
    private String EXPLAINS;
    //概要
    private String DESCRIPTION;
    private int EVALUATE_CNT;
    //评论
    private List<MedicineDetailsEvaluateModel> EVALUATE;

    public String getWAREUNIT() {
        return WAREUNIT;
    }

    public void setWAREUNIT(String WAREUNIT) {
        this.WAREUNIT = WAREUNIT;
    }

    public String getPROD_ADD() {
        return PROD_ADD;
    }

    public void setPROD_ADD(String PROD_ADD) {
        this.PROD_ADD = PROD_ADD;
    }

    public String getPRODUCER() {
        return PRODUCER;
    }

    public void setPRODUCER(String PRODUCER) {
        this.PRODUCER = PRODUCER;
    }

    public String getFILENO() {
        return FILENO;
    }

    public void setFILENO(String FILENO) {
        this.FILENO = FILENO;
    }

    public String getSTORE_REQ() {
        return STORE_REQ;
    }

    public void setSTORE_REQ(String STORE_REQ) {
        this.STORE_REQ = STORE_REQ;
    }


    public int getCOMMODITY_DISCOUNT() {
        return COMMODITY_DISCOUNT;
    }

    public void setCOMMODITY_DISCOUNT(int COMMODITY_DISCOUNT) {
        this.COMMODITY_DISCOUNT = COMMODITY_DISCOUNT;
    }


    public String getHTML_PATH() {
        return HTML_PATH;
    }

    public void setHTML_PATH(String HTML_PATH) {
        this.HTML_PATH = HTML_PATH;
    }

    public String[] getIMAGES() {
        return IMAGES;
    }

    public void setIMAGES(String[] IMAGES) {
        this.IMAGES = IMAGES;
    }

    public BigDecimal getMEMPRICE() {
        return MEMPRICE;
    }

    public void setMEMPRICE(BigDecimal MEMPRICE) {
        this.MEMPRICE = MEMPRICE;
    }

    public String getPRESCRIPTION_FLG() {
        return PRESCRIPTION_FLG;
    }

    public void setPRESCRIPTION_FLG(String PRESCRIPTION_FLG) {
        this.PRESCRIPTION_FLG = PRESCRIPTION_FLG;
    }

    public int getSALE_NUM() {
        return SALE_NUM;
    }

    public void setSALE_NUM(int SALE_NUM) {
        this.SALE_NUM = SALE_NUM;
    }

    public String getTOP_FLG() {
        return TOP_FLG;
    }

    public void setTOP_FLG(String TOP_FLG) {
        this.TOP_FLG = TOP_FLG;
    }

    public BigDecimal getMEMPRICE2() {
        return MEMPRICE2;
    }

    public BigDecimal getMEMPRICE3() {
        return MEMPRICE3;
    }

    public void setMEMPRICE3(BigDecimal MEMPRICE3) {
        this.MEMPRICE3 = MEMPRICE3;
    }

    public BigDecimal getMEMPRICE4() {
        return MEMPRICE4;
    }

    public void setMEMPRICE4(BigDecimal MEMPRICE4) {
        this.MEMPRICE4 = MEMPRICE4;
    }

    public BigDecimal getPILE() {
        return PILE;
    }

    public void setPILE(BigDecimal PILE) {
        this.PILE = PILE;
    }

    public String getACTIVE_END_DATE() {
        return ACTIVE_END_DATE;
    }

    public void setACTIVE_END_DATE(String ACTIVE_END_DATE) {
        this.ACTIVE_END_DATE = ACTIVE_END_DATE;
    }

    public String getACTIVE_START_DATE() {
        return ACTIVE_START_DATE;
    }

    public void setACTIVE_START_DATE(String ACTIVE_START_DATE) {
        this.ACTIVE_START_DATE = ACTIVE_START_DATE;
    }


    public String getMAIN_ELEMENT() {
        return MAIN_ELEMENT;
    }

    public void setMAIN_ELEMENT(String MAIN_ELEMENT) {
        this.MAIN_ELEMENT = MAIN_ELEMENT;
    }

    public String getEXPLAINS() {
        return EXPLAINS;
    }

    public void setEXPLAINS(String EXPLAINS) {
        this.EXPLAINS = EXPLAINS;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public List<MedicineDetailsEvaluateModel> getEVALUATE() {
        return EVALUATE;
    }

    public void setEVALUATE(List<MedicineDetailsEvaluateModel> EVALUATE) {
        this.EVALUATE = EVALUATE;
    }

    public int getEVALUATE_CNT() {
        return EVALUATE_CNT;
    }

    public void setEVALUATE_CNT(int EVALUATE_CNT) {
        this.EVALUATE_CNT = EVALUATE_CNT;
    }

    public String getCONTACT_TEL() {
        return CONTACT_TEL;
    }

    public void setCONTACT_TEL(String CONTACT_TEL) {
        this.CONTACT_TEL = CONTACT_TEL;
    }

    public String getCONTACT_MOBILE() {
        return CONTACT_MOBILE;
    }

    public void setCONTACT_MOBILE(String CONTACT_MOBILE) {
        this.CONTACT_MOBILE = CONTACT_MOBILE;
    }

    public String getFLG_DETAIL() {
        return FLG_DETAIL;
    }

    public void setFLG_DETAIL(String FLG_DETAIL) {
        this.FLG_DETAIL = FLG_DETAIL;
    }

    public AddressModel getUSER_ADDRESS_OBJ() {
        return USER_ADDRESS_OBJ;
    }

    public void setUSER_ADDRESS_OBJ(AddressModel USER_ADDRESS_OBJ) {
        this.USER_ADDRESS_OBJ = USER_ADDRESS_OBJ;
    }

    public String getUSER_ADDRESS() {
        return USER_ADDRESS;
    }

    public void setUSER_ADDRESS(String USER_ADDRESS) {
        this.USER_ADDRESS = USER_ADDRESS;
    }


    public Integer getSUMQTY() {
        return SUMQTY;
    }

    public void setSUMQTY(Integer SUMQTY) {
        this.SUMQTY = SUMQTY;
    }
}
