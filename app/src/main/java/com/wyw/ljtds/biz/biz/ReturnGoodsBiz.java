package com.wyw.ljtds.biz.biz;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.model.CommodityDetailsModel;
import com.wyw.ljtds.model.CreatOrderModel;
import com.wyw.ljtds.model.GoodsHandingModel;
import com.wyw.ljtds.model.KeyValue;
import com.wyw.ljtds.model.LogisticsModel;
import com.wyw.ljtds.model.MedicineDetailsEvaluateModel;
import com.wyw.ljtds.model.MedicineDetailsModel;
import com.wyw.ljtds.model.OnlinePayModel;
import com.wyw.ljtds.model.OrderModelInfoMedicine;
import com.wyw.ljtds.model.OrderModelMedicine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/17 0017.
 */

public class ReturnGoodsBiz extends BaseBiz {
    public static List<GoodsHandingModel> read() throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "returnGoodsHanding", true);

        ksoap.setProperty("op", "read", SoapProcessor.PropertyType.TYPE_STRING);
        ksoap.setProperty("data", "", SoapProcessor.PropertyType.TYPE_STRING);
        JsonElement element = ksoap.request();

        Gson gson = new GsonBuilder().create();
        TypeToken<List<GoodsHandingModel>> tt = new TypeToken<List<GoodsHandingModel>>() {
        };
        List<GoodsHandingModel> ls = gson.fromJson(element, tt.getType());
        return ls;
    }

    public static List<KeyValue> readReturnReasonList() throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "returnReasonList", true);

        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<KeyValue>> tt = new TypeToken<List<KeyValue>>() {
        };
        List<KeyValue> fs = gson.fromJson(element, tt.getType());
        return fs;
    }

    public static String getReturnMoney(String data) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "returnGoodsHanding", true);

        ksoap.setProperty("op", "getReturnMoney", SoapProcessor.PropertyType.TYPE_STRING);
        ksoap.setProperty("data", data, SoapProcessor.PropertyType.TYPE_STRING);
        JsonElement element = ksoap.request();
        return element.getAsString();
    }
}
