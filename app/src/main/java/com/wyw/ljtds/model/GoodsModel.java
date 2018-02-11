package com.wyw.ljtds.model;

/**
 * Created by Administrator on 2017/1/16 0016.
 */

public class GoodsModel {
    private String DEL_FLG; // 删除标识
    //点击数量
    private int CLICK_NUM;
    //0没有收藏 1 是收藏
    private String FAVORITED;

    public String getFavorited() {
        return FAVORITED;
    }

    public void setFavorited(String FAVORITED) {
        this.FAVORITED = FAVORITED;
    }

    public int getCLICK_NUM() {
        return CLICK_NUM;
    }

    public void setCLICK_NUM(int CLICK_NUM) {
        this.CLICK_NUM = CLICK_NUM;
    }

    public String getDEL_FLG() {
        return DEL_FLG;
    }

    public void setDEL_FLG(String DEL_FLG) {
        this.DEL_FLG = DEL_FLG;
    }
}
