package com.wyw.ljtds.model;

import java.util.List;

/**
 * Created by Administrator on 2017/4/13 0013.
 */

public class ShoppingCartMedicineModel extends OrderCommDto {
    public boolean checed = false;
    private String WARENAME;
    private String WARETYPE;
    private String GROUPID;
    private String GROUPNAME;
    private String PRESCRIPTION_FLG;

    public String getWARETYPE() {
        return WARETYPE;
    }

    public void setWARETYPE(String WARETYPE) {
        this.WARETYPE = WARETYPE;
    }

    public String getGROUPID() {
        return GROUPID;
    }

    public void setGROUPID(String GROUPID) {
        this.GROUPID = GROUPID;
    }

    public String getGROUPNAME() {
        return GROUPNAME;
    }

    public void setGROUPNAME(String GROUPNAME) {
        this.GROUPNAME = GROUPNAME;
    }

    public String getPRESCRIPTION_FLG() {
        return PRESCRIPTION_FLG;
    }

    public void setPRESCRIPTION_FLG(String PRESCRIPTION_FLG) {
        this.PRESCRIPTION_FLG = PRESCRIPTION_FLG;
    }

    public String getWARENAME() {
        return WARENAME;
    }

    public void setWARENAME(String WARENAME) {
        this.WARENAME = WARENAME;
    }
}
