package com.wyw.ljtwl.model;

import java.util.List;

/**
 * Created by wsy on 18-1-9.
 */

public class OrderListResponse extends ServerResponse {
    private List<OrderInfoModel> data;

    public List<OrderInfoModel> getData() {
        return data;
    }

    public void setData(List<OrderInfoModel> data) {
        this.data = data;
    }
}
