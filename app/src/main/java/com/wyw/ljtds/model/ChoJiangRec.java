package com.wyw.ljtds.model;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

public class ChoJiangRec extends BaseModel {
    /*INTEGRAL_DRAW_LOG_ID	integral_draw_log_id	bigint	20	*	*			积分抽奖记录ID
AWARD_ID	award_id	char	3		*			积分奖品编码
AWARD_NAME	award_name	varchar	32		*			积分奖品名称
OID_USER_ID	oid_user_id	varchar	32		*			获奖用户ID
USER_MOBILE	user_mobile	char	11		*			获奖用户手机号码
USER_NAME	user_name	varchar	32		*			获奖用户姓名
BUSNO	busno	char	3					领奖门店ID
BUSNAME	busname	varchar	64					门店名称
ORG_PRINCIPAL	org_principal	varchar	20					门店联系人
ORG_MOBILE	org_mobile	varchar	20					门店联系电话
ORG_ADDRESS	org_address	varchar	255					门店注册地址
VALID_FLG	valid_flg	char	1		*	‘0’		是否领奖（0：未领奖，1：已领奖，2：已过期）
INS_DATE	ins_date	datetime	0		*			抽奖时间
UPD_DATE	upd_date	datetime	0		*			领奖时间
@吴思阳 */

    public  static final String AWARD_TYPE_SHIWU = "1";//1：实物，0：非实物
    private String INTEGRAL_DRAW_LOG_ID;
    private String AWARD_ID;
    private String AWARD_NAME;
    private String AWARD_TYPE;//1：实物，0：非实物
    private String BUSNAME;
    private String ORG_MOBILE;
    private String OID_USER_ID;
    private String USER_MOBILE;
    private String USER_NAME;
    private String VALID_FLG;
    private String INS_DATE;
    private String UPD_DATE;

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

    public String getORG_MOBILE() {
        return ORG_MOBILE;
    }

    public void setORG_MOBILE(String ORG_MOBILE) {
        this.ORG_MOBILE = ORG_MOBILE;
    }

    public String getBUSNAME() {
        return BUSNAME;
    }

    public void setBUSNAME(String BUSNAME) {
        this.BUSNAME = BUSNAME;
    }

    public String getUPD_DATE() {
        return UPD_DATE;
    }

    public void setUPD_DATE(String UPD_DATE) {
        this.UPD_DATE = UPD_DATE;
    }

    public String getAWARD_TYPE() {
        return AWARD_TYPE;
    }

    public void setAWARD_TYPE(String AWARD_TYPE) {
        this.AWARD_TYPE = AWARD_TYPE;
    }
}
