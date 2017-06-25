package com.wyw.ljtds.model;

import java.util.List;

/**
 * Created by Administrator on 2017/4/16 0016.
 */

public class GoodSubmitModel2 extends BaseModel {
    private String OID_GROUP_ID;
    private String OID_GROUP_NAME;
    private List<GoodSubmitModel3> DETAILS;

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

    public List<GoodSubmitModel3> getDETAILS() {
        return DETAILS;
    }

    public void setDETAILS(List<GoodSubmitModel3> DETAILS) {
        this.DETAILS = DETAILS;
    }
}
