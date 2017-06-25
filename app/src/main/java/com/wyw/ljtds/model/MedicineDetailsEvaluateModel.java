package com.wyw.ljtds.model;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public class MedicineDetailsEvaluateModel extends BaseModel {
    private String EVALUATE_CONTGENT;
    private String IMG_PATH1;
    private String IMG_PATH2;
    private String IMG_PATH3;
    private String IMG_PATH4;
    private String IMG_PATH5;
    private long INS_DATE;
    private String EVALUATE_GRADE;
    private String USER_ICON_FILE_ID;
    private String MOBILE;

    public String getEVALUATE_CONTGENT() {
        return EVALUATE_CONTGENT;
    }

    public void setEVALUATE_CONTGENT(String EVALUATE_CONTGENT) {
        this.EVALUATE_CONTGENT = EVALUATE_CONTGENT;
    }

    public String getEVALUATE_GRADE() {
        return EVALUATE_GRADE;
    }

    public void setEVALUATE_GRADE(String EVALUATE_GRADE) {
        this.EVALUATE_GRADE = EVALUATE_GRADE;
    }

    public long getINS_DATE() {
        return INS_DATE;
    }

    public void setINS_DATE(long INS_DATE) {
        this.INS_DATE = INS_DATE;
    }

    public String getIMG_PATH5() {
        return IMG_PATH5;
    }

    public void setIMG_PATH5(String IMG_PATH5) {
        this.IMG_PATH5 = IMG_PATH5;
    }

    public String getIMG_PATH4() {
        return IMG_PATH4;
    }

    public void setIMG_PATH4(String IMG_PATH4) {
        this.IMG_PATH4 = IMG_PATH4;
    }

    public String getIMG_PATH3() {
        return IMG_PATH3;
    }

    public void setIMG_PATH3(String IMG_PATH3) {
        this.IMG_PATH3 = IMG_PATH3;
    }

    public String getIMG_PATH2() {
        return IMG_PATH2;
    }

    public void setIMG_PATH2(String IMG_PATH2) {
        this.IMG_PATH2 = IMG_PATH2;
    }

    public String getIMG_PATH1() {
        return IMG_PATH1;
    }

    public void setIMG_PATH1(String IMG_PATH1) {
        this.IMG_PATH1 = IMG_PATH1;
    }

    public String getUSER_ICON_FILE_ID() {
        return USER_ICON_FILE_ID;
    }

    public void setUSER_ICON_FILE_ID(String USER_ICON_FILE_ID) {
        this.USER_ICON_FILE_ID = USER_ICON_FILE_ID;
    }

    public String getMOBILE() {
        return MOBILE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }
}
