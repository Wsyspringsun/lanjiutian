package com.wyw.ljtmgr.biz;

import android.util.Log;

import com.google.gson.Gson;
import com.wyw.ljtmgr.config.AppConfig;
import com.wyw.ljtmgr.config.MyApplication;
import com.wyw.ljtmgr.model.BaseJson;
import com.wyw.ljtmgr.model.Header;
import com.wyw.ljtmgr.model.LoginModel;
import com.wyw.ljtmgr.model.LogisticInfo;
import com.wyw.ljtmgr.model.OrderDetailGetModel;
import com.wyw.ljtmgr.model.OrderDetailModel;
import com.wyw.ljtmgr.model.ServerResponse;
import com.wyw.ljtmgr.model.UserModel;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

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
    public static void loadOrder(String stat, String page, Callback.CommonCallback callback) {
        BaseJson<UserModel> baseJson = new BaseJson<UserModel>();
        LoginModel loginer = MyApplication.getCurrentLoginer();
        UserModel userModel = new UserModel();

        Header head = CommonBiz.getDataHeader();

        userModel.setLogisticsCompanyId(loginer.getOidGroupId());
        userModel.setPageNum(page);
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
    public static void loadOrderDetail(String orderId, String orderStat, Callback.CommonCallback callback) {
        Header head = CommonBiz.getDataHeader();

//        OrderDetailGetModel orderDetailGetModel = new OrderDetailGetModel();
//        orderDetailGetModel.setOrderGroupId(orderId);
        Map<String, String> map = new HashMap<>();
        map.put("orderGroupId", orderId);
        map.put("classify", orderStat);
//        orderDetailGetModel.setOrderGroupId(orderId);
//        orderDetailGetModel.setOrderTradeId(orderId);
//        orderDetailGetModel.setOidGroupId("sxljt");

        BaseJson<Map<String, String>> baseJson = new BaseJson<>();
        baseJson.setHead(head);
        baseJson.setBody(map);
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

    public static void orderAfterSale(String orderid, String isAgree, String handleReason, Callback.CommonCallback callback) {
        Header head = CommonBiz.getDataHeader();
        Map<String, String> model = new HashMap<>();
        model.put("orderGroupId", orderid);
        model.put("handleStatus", isAgree);
        model.put("handleReason", handleReason);


        BaseJson<Map<String, String>> baseJson = new BaseJson<>();
        baseJson.setHead(head);
        baseJson.setBody(model);
        Gson gson = new Gson();
        String data = gson.toJson(baseJson);

        RequestParams params = new RequestParams(AppConfig.WEB_DOMAIN + "/v/order/orderAfterSale");
        params.setAsJsonContent(true);
        params.setBodyContent(data);
        x.http().post(params, callback);
    }

    public static void orderArrived(OrderDetailModel model, SimpleCommonCallback<ServerResponse> callback) {
        Header head = CommonBiz.getDataHeader();
        BaseJson<Map<String, String>> baseJson = new BaseJson<>();
        baseJson.setHead(head);
        Map<String, String> m = new HashMap<>();
        m.put("orderGroupId", model.getOrderId());
        m.put("courier", model.getCourier());
        m.put("courierMobile", model.getCourierMobile());
        baseJson.setBody(m);
        Gson gson = new Gson();
        String data = gson.toJson(baseJson);

        RequestParams params = new RequestParams(AppConfig.WEB_DOMAIN + "/v/order/orderArrived");
        params.setAsJsonContent(true);
        params.setBodyContent(data);
        x.http().post(params, callback);
    }

    public static void orderRefused(OrderDetailModel model, SimpleCommonCallback<ServerResponse> callback) {
        Header head = CommonBiz.getDataHeader();
        BaseJson<Map<String, String>> baseJson = new BaseJson<>();
        baseJson.setHead(head);
        Map<String, String> m = new HashMap<>();

        m.put("orderGroupId", model.getOrderId());
        m.put("courier", model.getCourier());
        m.put("courierMobile", model.getCourierMobile());
        baseJson.setBody(m);
        Gson gson = new Gson();
        String data = gson.toJson(baseJson);

        RequestParams params = new RequestParams(AppConfig.WEB_DOMAIN + "/v/order/orderRefused");
        params.setAsJsonContent(true);
        params.setBodyContent(data);
        x.http().post(params, callback);
    }
}
