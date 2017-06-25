package com.wyw.ljtds.model;

import java.util.List;

/**
 * Created by Administrator on 2017/4/16 0016.
 */

public class GoodCreatModel1 extends BaseModel {
    private List<GoodCreatModel2> DETAILS;
    private String COST_POINT;
    private String PAYMENT_METHOD;

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
}
