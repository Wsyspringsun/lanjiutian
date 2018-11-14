package com.wyw.ljtds.model;

import java.util.HashMap;

/**
 * Created by wsy on 17-9-13.
 */

public class IConCatInfo extends HashMap<String,String> {
    private String name;
    private String commodityTypeId;
    private String imgPath;
    private String flg;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommodityTypeId() {
        return commodityTypeId;
    }

    public void setCommodityTypeId(String commodityTypeId) {
        this.commodityTypeId = commodityTypeId;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getFlg() {
        return flg;
    }

    public void setFlg(String flg) {
        this.flg = flg;
    }
}
