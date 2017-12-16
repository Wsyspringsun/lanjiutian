package com.wyw.ljtds.model;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

public class ChoJiangRec extends BaseModel {
    private String INTEGRAL_DRAW_LOG_ID;
    private String AWARD_ID;
    private String AWARD_NAME;
    private String OID_USER_ID;
    private String USER_MOBILE;
    private String USER_NAME;
    private String VALID_FLG;
    private String INS_DATE;

    public String getINTEGRAL_DRAW_LOG_ID() {
        return INTEGRAL_DRAW_LOG_ID;
    }

    public void setINTEGRAL_DRAW_LOG_ID(String INTEGRAL_DRAW_LOG_ID) {
        this.INTEGRAL_DRAW_LOG_ID = INTEGRAL_DRAW_LOG_ID;
    }

    public String getAWARD_ID() {
        return AWARD_ID;
    }

    public void setAWARD_ID(String AWARD_ID) {
        this.AWARD_ID = AWARD_ID;
    }

    public String getAWARD_NAME() {
        return AWARD_NAME;
    }

    public void setAWARD_NAME(String AWARD_NAME) {
        this.AWARD_NAME = AWARD_NAME;
    }

    public String getOID_USER_ID() {
        return OID_USER_ID;
    }

    public void setOID_USER_ID(String OID_USER_ID) {
        this.OID_USER_ID = OID_USER_ID;
    }

    public String getUSER_MOBILE() {
        return USER_MOBILE;
    }

    public void setUSER_MOBILE(String USER_MOBILE) {
        this.USER_MOBILE = USER_MOBILE;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getVALID_FLG() {
        return VALID_FLG;
    }

    public void setVALID_FLG(String VALID_FLG) {
        this.VALID_FLG = VALID_FLG;
    }

    public String getINS_DATE() {
        return INS_DATE;
    }

    public void setINS_DATE(String INS_DATE) {
        this.INS_DATE = INS_DATE;
    }
}
