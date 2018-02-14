package com.wyw.ljtmgr.model;

import java.util.List;

/**
 * Created by wsy on 18-1-9.
 */

public class OrderListResponse extends ServerResponse {
    private String mOrderStat;
    private List<OrderInfoModel> data;

    public List<OrderInfoModel> getData() {
        return data;
    }

    public void setData(List<OrderInfoModel> data) {
        this.data = data;
    }

    public String getmOrderStat() {
        return mOrderStat;
    }

    public void setmOrderStat(String mOrderStat) {
        this.mOrderStat = mOrderStat;
    }
}
