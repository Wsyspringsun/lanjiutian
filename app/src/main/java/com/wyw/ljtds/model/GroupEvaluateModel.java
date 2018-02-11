package com.wyw.ljtds.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public class GroupEvaluateModel {
    private String ORDER_GROUP_ID;
    private String GROUP_STATUS;
    private String ORDER_SOURCE = "0";
    private List<GoodsEvaluateModel> DETAILS;

    public String getORDER_GROUP_ID() {
        return ORDER_GROUP_ID;
    }

    public void setORDER_GROUP_ID(String ORDER_GROUP_ID) {
        this.ORDER_GROUP_ID = ORDER_GROUP_ID;
    }

    public String getGROUP_STATUS() {
        return GROUP_STATUS;
    }

    public void setGROUP_STATUS(String GROUP_STATUS) {
        this.GROUP_STATUS = GROUP_STATUS;
    }

    public String getORDER_SOURCE() {
        return ORDER_SOURCE;
    }

    public void setORDER_SOURCE(String ORDER_SOURCE) {
        this.ORDER_SOURCE = ORDER_SOURCE;
    }

    public List<GoodsEvaluateModel> getDETAILS() {
        return DETAILS;
    }

    public void setDETAILS(List<GoodsEvaluateModel> DETAILS) {
        this.DETAILS = DETAILS;
    }
}
