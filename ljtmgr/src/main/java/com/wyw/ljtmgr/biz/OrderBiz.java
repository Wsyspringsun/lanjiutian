package com.wyw.ljtwl.biz;

import android.util.Log;

import com.google.gson.Gson;
import com.wyw.ljtwl.config.AppConfig;
import com.wyw.ljtwl.config.MyApplication;
import com.wyw.ljtwl.model.BaseJson;
import com.wyw.ljtwl.model.Header;
import com.wyw.ljtwl.model.LoginModel;
import com.wyw.ljtwl.model.LogisticInfo;
import com.wyw.ljtwl.model.OrderDetailGetModel;
import com.wyw.ljtwl.model.UserModel;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by wsy on 18-1-9.
 */

public class OrderBiz {

    /**
     * load order list by order status
     *
     * @param stat     status
     * @param callback handle events of after response from server completed
     */
    public static void loadOrder(String stat, Callback.CommonCallback callback) {
        BaseJson<UserModel> baseJson = new BaseJson<UserModel>();
        LoginModel loginer = MyApplication.getCurrentLoginer();
        UserModel userModel = new UserModel();

        Header head = CommonBiz.getDataHeader();

        userModel.setLogisticsCompanyId(loginer.getOidGroupId());
        userModel.setPageNum("1");
        userModel.setPageSize(AppConfig.PAGE_NUM);
//        Log.e("status", status);
        userModel.setClassify(stat);
        baseJson.setHead(head);
        baseJson.setBody(userModel);
        Gson gson = new Gson();
        String data = gson.toJson(baseJson);
        RequestParams params = new RequestParams(AppConfig.WEB_DOMAIN + "/v/order/orderList");
        params.setAsJsonContent(true);
        params.setBodyContent(data);
        Log.e(AppConfig.TAG_ERR, "params......." + gson.toJson(params));
        x.http().post(params, callback);
    }

    /**
     * load order detail from server
     *
     * @param orderId  order unique id
     * @param callback callback
     */
    public static void loadOrderDetail(String orderId, Callback.CommonCallback callback) {
        Header head = CommonBiz.getDataHeader();

        OrderDetailGetModel orderDetailGetModel = new OrderDetailGetModel();
        orderDetailGetModel.setOrderGroupId(orderId);
//        orderDetailGetModel.setOrderTradeId(orderId);
//        orderDetailGetModel.setOidGroupId("sxljt");

        BaseJson<OrderDetailGetModel> baseJson = new BaseJson<>();
        baseJson.setHead(head);
        baseJson.setBody(orderDetailGetModel);
        Gson gson = new Gson();
        String data = gson.toJson(baseJson);

        RequestParams params = new RequestParams(AppConfig.WEB_DOMAIN + "/v/order/orderDetail");
        params.setAsJsonContent(true);
        params.setBodyContent(data);
        x.http().post(params, callback);
    }

    public static void sendOrder(LogisticInfo model, Callback.CommonCallback callback) {


        Header head = CommonBiz.getDataHeader();
        BaseJson<LogisticInfo> baseJson = new BaseJson<>();
        baseJson.setHead(head);
        baseJson.setBody(model);
        Gson gson = new Gson();
        String data = gson.toJson(baseJson);

        RequestParams params = new RequestParams(AppConfig.WEB_DOMAIN + "/v/order/orderSend");
        params.setAsJsonContent(true);
        params.setBodyContent(data);
        x.http().post(params, callback);
    }

}
