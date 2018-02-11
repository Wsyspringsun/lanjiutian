package com.wyw.ljtwl.biz;

import android.util.Log;

import com.google.gson.Gson;
import com.wyw.ljtwl.config.AppConfig;
import com.wyw.ljtwl.config.PreferenceCache;
import com.wyw.ljtwl.model.BaseJson;
import com.wyw.ljtwl.model.Header;
import com.wyw.ljtwl.model.LoginModel;
import com.wyw.ljtwl.model.ServerResponse;
import com.wyw.ljtwl.utils.StringUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wsy on 18-1-7.
 */

public class UserBiz {
    /**
     * 判断是否登录
     *
     * @return
     */
    public static boolean isLogined() {
        String token = PreferenceCache.getToken();
        return !StringUtils.isEmpty(token);
    }

    public static void groupAccount(String busId, Callback.CommonCallback callback) {
        BaseJson<Map<String, String>> baseJson = new BaseJson<>();
        Map<String, String> data = new HashMap<>();
        data.put("businessesId", busId);
        Header head = CommonBiz.getDataHeader();
        baseJson.setHead(head);
        baseJson.setBody(data);
        Gson gson = new Gson();
        String json = gson.toJson(baseJson);
        RequestParams params = new RequestParams(AppConfig.WEB_DOMAIN + "/v/order/orderList");
        params.setAsJsonContent(true);
        params.setBodyContent(json);

        x.http().post(params, callback);
    }

    public static void login(LoginModel loginModel, Callback.CommonCallback callback) {
        Gson gson = new Gson();
        String data = gson.toJson(loginModel);
        RequestParams params = new RequestParams(AppConfig.WEB_DOMAIN + "/login/userLogin");
        params.setAsJsonContent(true);
        params.setBodyContent(data);
        x.http().post(params, callback);
    }

    public static void logout() {
        PreferenceCache.putToken(""); // 持久化缓存token
        PreferenceCache.putUser(""); // 持久化缓存token
    }
}
