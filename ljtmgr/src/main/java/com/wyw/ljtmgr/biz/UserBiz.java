package com.wyw.ljtmgr.biz;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.wyw.ljtmgr.config.AppConfig;
import com.wyw.ljtmgr.config.MyApplication;
import com.wyw.ljtmgr.config.PreferenceCache;
import com.wyw.ljtmgr.model.BaseJson;
import com.wyw.ljtmgr.model.Header;
import com.wyw.ljtmgr.model.LoginModel;
import com.wyw.ljtmgr.model.UpdatePassWordModel;
import com.wyw.ljtmgr.utils.CommonUtil;
import com.wyw.ljtmgr.utils.StringUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

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

    public static void logout(Context context) {
        PreferenceCache.putToken(""); // 持久化缓存token
        PreferenceCache.putUser(""); // 持久化缓存token
        MyApplication.clearLoginer();
        JPushInterface.cleanTags(context, 0);
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

    public static void updatePwd(UpdatePassWordModel updatePassWordModel, Callback.CommonCallback callback) {
        BaseJson<UpdatePassWordModel> baseJson = new BaseJson<>();
        Header head = CommonBiz.getDataHeader();
        baseJson.setHead(head);
        baseJson.setBody(updatePassWordModel);
        Gson gson = new Gson();
        String json = gson.toJson(baseJson);
        RequestParams params = new RequestParams(AppConfig.WEB_DOMAIN + "/v/user/updatePwd");
        params.setAsJsonContent(true);
        params.setBodyContent(json);
        x.http().post(params, callback);
        /*x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("err", result);
                try {
                    JSONObject jsonData = new JSONObject(result);
                    String success = jsonData.getString("success");
                    String msg = jsonData.getString("msg");
                    if (success.equals("0")) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                        finish();
                        // Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    } else if (success.equals("2")) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //解析result
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
                closeLoding();
            }

            @Override
            public void onFinished() {
            }
        });*/
    }
}
