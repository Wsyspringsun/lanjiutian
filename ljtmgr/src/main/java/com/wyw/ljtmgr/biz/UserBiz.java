package com.wyw.ljtmgr.biz;

import android.util.Log;

import com.google.gson.Gson;
import com.wyw.ljtmgr.config.AppConfig;
import com.wyw.ljtmgr.config.MyApplication;
import com.wyw.ljtmgr.config.PreferenceCache;
import com.wyw.ljtmgr.model.BaseJson;
import com.wyw.ljtmgr.model.Header;
import com.wyw.ljtmgr.model.LoginModel;
import com.wyw.ljtmgr.utils.CommonUtil;
import com.wyw.ljtmgr.utils.StringUtils;

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
        String user = PreferenceCache.getUser();
        return !StringUtils.isEmpty(token) && !StringUtils.isEmpty(user);
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
        MyApplication.clearLoginer();
    }

    public static void loadGroupInfo(Callback.CommonCallback callback) {
        BaseJson<Map<String, String>> baseJson = new BaseJson<>();
        Map<String, String> data = new HashMap<>();
        LoginModel loginer = MyApplication.getCurrentLoginer();
        data.put("oidGroupId", loginer.getOidGroupId());
        CommonUtil.log(loginer.getOidGroupId());
        Header head = CommonBiz.getDataHeader();
        baseJson.setHead(head);
        baseJson.setBody(data);
        Gson gson = new Gson();
        String json = gson.toJson(baseJson);
        RequestParams params = new RequestParams(AppConfig.WEB_DOMAIN + "/v/shop/shopInfo");
        params.setAsJsonContent(true);
        params.setBodyContent(json);
        x.http().post(params, callback);
    }
}
