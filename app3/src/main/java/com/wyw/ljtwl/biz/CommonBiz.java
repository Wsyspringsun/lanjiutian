package com.wyw.ljtwl.biz;

import android.util.Log;

import com.google.gson.Gson;
import com.wyw.ljtwl.biz.task.AbstractCommonCallback;
import com.wyw.ljtwl.config.AppConfig;
import com.wyw.ljtwl.config.PreferenceCache;
import com.wyw.ljtwl.model.BaseJson;
import com.wyw.ljtwl.model.Header;

import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by wsy on 17-9-2.
 */

public class CommonBiz {
    public static <T,R> void handle(String url, T model, AbstractCommonCallback<R> task) {
        BaseJson<T> baseJson = new BaseJson<T>();
        String token = PreferenceCache.getToken();
        Header head = new Header();
        head.setToken(token);
        baseJson.setHead(head);
        baseJson.setBody(model);
        Gson gson = new Gson();
        String data = gson.toJson(baseJson);
        Log.e(AppConfig.TAG_ERR, "jsondata:" + data);

        RequestParams params = new RequestParams(AppConfig.WEB_DOMAIN + url);
        params.setAsJsonContent(true);
        params.setBodyContent(data);
        task.getActivity().setLoding();
        x.http().post(params, task);
    }
}
