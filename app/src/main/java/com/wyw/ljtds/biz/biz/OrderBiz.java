package com.wyw.ljtds.biz.biz;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.model.OrderTradeDto;

/**
 * Created by wsy on 17-7-17.
 */

public class OrderBiz {
    public static OrderTradeDto showOrder(String data) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "order", true);

        ksoap.setProperty("op", "showOrder", SoapProcessor.PropertyType.TYPE_STRING);
        ksoap.setProperty("data", data, SoapProcessor.PropertyType.TYPE_STRING);

        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(element, OrderTradeDto.class);
    }
}
