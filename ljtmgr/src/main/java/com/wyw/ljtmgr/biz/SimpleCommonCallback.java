package com.wyw.ljtmgr.biz;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wyw.ljtmgr.config.AppConfig;
import com.wyw.ljtmgr.model.ServerResponse;
import com.wyw.ljtmgr.ui.ActivityLogin;
import com.wyw.ljtmgr.ui.BaseActivity;

import org.xutils.common.Callback;

import java.lang.reflect.ParameterizedType;


/**
 * Created by wsy on 18-1-9.
 */

public abstract class SimpleCommonCallback<T extends ServerResponse> implements Callback.CommonCallback<String> {
    private final Context context;

    public SimpleCommonCallback(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess(String result) {
        Log.e(AppConfig.TAG_ERR, context.getClass().getName() + " result:" + result);
        Gson gson = new Gson();
//        Type tt = new TypeToken<T>() {
//        }.getType();
        Class<T> clz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        T rlt = gson.fromJson(result, clz);

        if (ServerResponse.OK.equals(rlt.getSuccess())) {
            handleResult(rlt);
        } else if (ServerResponse.ERR.equals(rlt.getSuccess())) {
            Log.e(AppConfig.TAG_ERR, "data Error........" + rlt.getMsg());
            Toast.makeText(context, "错误:" + rlt.getMsg(), Toast.LENGTH_LONG).show();
        } else if (ServerResponse.TOKEN_DEP.equals(rlt.getSuccess()) || ServerResponse.TOKEN_ERR.equals(rlt.getSuccess())) {
            Toast.makeText(context, "登录错误:" + rlt.getMsg(), Toast.LENGTH_LONG).show();
            UserBiz.logout();

            if (!(context instanceof ActivityLogin)) {
                Intent it = ActivityLogin.getIntent(context);
                context.startActivity(it);
            }
        } else {
            Log.e(AppConfig.TAG_ERR, "data UnKonw stat........" + rlt.getMsg());
            Toast.makeText(context, "未知错误:" + rlt.getMsg(), Toast.LENGTH_LONG).show();
        }
    }

    protected abstract void handleResult(T result);

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        ex.printStackTrace();
        Log.e(AppConfig.TAG_ERR, "onError........" + ex.getMessage());
        Toast.makeText(context, "服务器发生故障,请联系管理员", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCancelled(CancelledException cex) {
        Log.e(AppConfig.TAG_ERR, "onCancelled........");
        cex.printStackTrace();
    }

    @Override
    public void onFinished() {
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).closeLoding();
        }
    }

}
