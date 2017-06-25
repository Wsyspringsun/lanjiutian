package com.wyw.ljtds.widget.commodity;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/5/29 0029.
 */

public class CheckInchModel {
    private String[] image;
    private String cololr;
    private String color_id;
    private String size_id;
    private String size;
    private BigDecimal new_money;
    private BigDecimal old_money;
    private int num;
    private int usable;
    private int color_option;
    private int size_option;


    public String getColor_id() {
        return color_id;
    }

    public void setColor_id(String color_id) {
        this.color_id = color_id;
    }

    public String getSize_id() {
        return size_id;
    }

    public void setSize_id(String size_id) {
        this.size_id = size_id;
    }

    public int getUsable() {
        return usable;
    }

    public void setUsable(int usable) {
        this.usable = usable;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String[] getImage() {
        return image;
    }

    public void setImage(String[] image) {
        this.image = image;
    }

    public String getCololr() {
        return cololr;
    }

    public void setCololr(String cololr) {
        this.cololr = cololr;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public BigDecimal getNew_money() {
        return new_money;
    }

    public void setNew_money(BigDecimal new_money) {
        this.new_money = new_money;
    }

    public BigDecimal getOld_money() {
        return old_money;
    }

    public void setOld_money(BigDecimal old_money) {
        this.old_money = old_money;
    }

    public int getColor_option() {
        return color_option;
    }

    public void setColor_option(int color_option) {
        this.color_option = color_option;
    }

    public int getSize_option() {
        return size_option;
    }

    public void setSize_option(int size_option) {
        this.size_option = size_option;
    }

}
