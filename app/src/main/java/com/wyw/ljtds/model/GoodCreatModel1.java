package com.wyw.ljtds.model;

import java.util.List;

/**
 * Created by Administrator on 2017/4/16 0016.
 */

public class GoodCreatModel1 extends BaseModel {
    private List<GoodCreatModel2> DETAILS;
    private String ADDRESS_ID;
    private String COST_POINT;
    private String PAYMENT_METHOD;
    //配送时间 起始
    private String DISTRIBUTION_DATE_START;
    //配送时间 截止
    private String DISTRIBUTION_DATE_END;
    //用户留言
    private String REMARKS;
    //使用电子币标识
    private String COIN_FLG;
    //使用邮费抵用券标识
    private String POSTAGE_FLG;

    public List<GoodCreatModel2> getDETAILS() {
        return DETAILS;
    }

    public void setDETAILS(List<GoodCreatModel2> DETAILS) {
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

    public String getDISTRIBUTION_DATE_END() {
        return DISTRIBUTION_DATE_END;
    }

    public void setDISTRIBUTION_DATE_END(String DISTRIBUTION_DATE_END) {
        this.DISTRIBUTION_DATE_END = DISTRIBUTION_DATE_END;
    }

    public String getDISTRIBUTION_DATE_START() {
        return DISTRIBUTION_DATE_START;
    }

    public void setDISTRIBUTION_DATE_START(String DISTRIBUTION_DATE_START) {
        this.DISTRIBUTION_DATE_START = DISTRIBUTION_DATE_START;
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

    public String getREMARKS() {
        return REMARKS;
    }

    public void setREMARKS(String REMARKS) {
        this.REMARKS = REMARKS;
    }
}
