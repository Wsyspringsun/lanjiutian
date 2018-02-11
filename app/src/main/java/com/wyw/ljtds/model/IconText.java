package com.wyw.ljtds.model;

import android.support.annotation.IntegerRes;

import java.util.List;

/**
 * Created by Administrator on 2017/6/10 0010.
 */

public class IconText extends BaseModel {
    @IntegerRes
    int imgId;
    String text;

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
