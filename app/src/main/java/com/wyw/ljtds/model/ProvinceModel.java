package com.wyw.ljtds.model;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

public class ProvinceModel extends BaseModel{
    //ID
    private int ID;
    //名字
    private String NAME;
    //级别
    private boolean AREA_LEVEL;
    //利用状态
    private boolean USING_FLG;
    //上级id
    private int PARENT_ID;
    //orby
    private int ORBY;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public boolean isAREA_LEVEL() {
        return AREA_LEVEL;
    }

    public void setAREA_LEVEL(boolean AREA_LEVEL) {
        this.AREA_LEVEL = AREA_LEVEL;
    }

    public boolean isUSING_FLG() {
        return USING_FLG;
    }

    public void setUSING_FLG(boolean USING_FLG) {
        this.USING_FLG = USING_FLG;
    }

    public int getPARENT_ID() {
        return PARENT_ID;
    }

    public void setPARENT_ID(int PARENT_ID) {
        this.PARENT_ID = PARENT_ID;
    }

    public int getORBY() {
        return ORBY;
    }

    public void setORBY(int ORBY) {
        this.ORBY = ORBY;
    }

}
