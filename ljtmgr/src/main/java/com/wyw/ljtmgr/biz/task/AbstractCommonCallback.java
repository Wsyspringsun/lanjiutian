package com.wyw.ljtmgr.biz.task;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.wyw.ljtmgr.config.AppConfig;
import com.wyw.ljtmgr.ui.ActivityLogin;
import com.wyw.ljtmgr.ui.BaseActivity;

import org.json.JSONObject;
import org.xutils.common.Callback;

/**
 * Created by wsy on 17-9-2.
 */

public abstract class AbstractCommonCallback implements Callback.CommonCallback<String> {
    public abstract void handleSuccess(String result) throws Exception;

    public abstract BaseActivity getContextActivity();

    @Override
    public void onSuccess(String result) {
        Log.e(AppConfig.TAG_ERR, "response result:" + String.valueOf(result));
        try {
            JSONObject jsonData = new JSONObject(result);
            String success = jsonData.getString("success");
            String msg = jsonData.getString("msg");
            Log.e(AppConfig.TAG_ERR, "success:" + success);
            if ("0".equals(success)) {
                handleSuccess(result);
            } else if (success.equals("2")) {
                Toast.makeText(getContextActivity(), msg, Toast.LENGTH_SHORT).show();
            } else if (success.equals("2")) {
                Toast.makeText(getContextActivity(), "身份信息过期,请重新登录", Toast.LENGTH_LONG).show();
                Intent it = ActivityLogin.getIntent(getContextActivity());
                getContextActivity().startActivity(it);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(AppConfig.TAG_ERR, "Exception:" + e.getMessage());
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        Throwable t = ex;
        while (t != null) {
            Log.e(AppConfig.TAG_ERR, "ex:" + t.getMessage());
            t = ex.getCause();
        }
        Log.e(AppConfig.TAG_ERR, "error.........");
    }

    @Override
    public void onCancelled(CancelledException cex) {
        Log.e(AppConfig.TAG_ERR, "cancel.........");
    }

    @Override
    public void onFinished() {
        Log.e(AppConfig.TAG_ERR, "finished.........");
        getContextActivity().closeLoding();
    }


}
