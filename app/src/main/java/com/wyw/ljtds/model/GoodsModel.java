package com.wyw.ljtds.model;

/**
 * Created by Administrator on 2017/1/16 0016.
 */

public class GoodsModel {
    //积分商城活动
    public static final String HUODONG_JIFEN="10" ;
    //特价
    public static final String HUODONG_TEJIA="9" ;
    //秒杀
    public static final String HUODONG_MIAOSHA = "8";
    //满曾
    public static final String[] HUODONG_MANZENG = {"11","12","13","14","15","16","17","18"};

    //零元购
    public static final String TOP_FLG_LINGYUAN = "20";
    public static final String TOP_FLG_ZHIJIEGOU = "19";//不能加入购物车，必须直接购买


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
