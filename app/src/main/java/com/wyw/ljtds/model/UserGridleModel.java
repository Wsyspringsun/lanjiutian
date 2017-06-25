package com.wyw.ljtds.model;

/**
 * Created by Administrator on 2016/12/15 0015.
 */

public class UserGridleModel {
    private int imgId;
    private int str;

    public UserGridleModel(){

    }

    public UserGridleModel(int imgId,int str){
        this.imgId = imgId;
        this.str = str;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public int getStr() {
        return str;
    }

    public void setStr(int str) {
        this.str = str;
    }
}
