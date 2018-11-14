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
    private String CUXIAO_FLG;
    private String PROMPRICE;
    private String SUMQTY;

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

    public String getCUXIAO_FLG() {
        return CUXIAO_FLG;
    }

    public void setCUXIAO_FLG(String CUXIAO_FLG) {
        this.CUXIAO_FLG = CUXIAO_FLG;
    }

    public String getPROMPRICE() {
        return PROMPRICE;
    }

    public void setPROMPRICE(String PROMPRICE) {
        this.PROMPRICE = PROMPRICE;
    }

    public String getSUMQTY() {
        return SUMQTY;
    }

    public void setSUMQTY(String SUMQTY) {
        this.SUMQTY = SUMQTY;
    }
}
