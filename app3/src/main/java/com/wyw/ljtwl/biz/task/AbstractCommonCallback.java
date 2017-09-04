package com.wyw.ljtwl.biz.task;

import android.util.Log;

import com.wyw.ljtwl.config.AppConfig;
import com.wyw.ljtwl.ui.BaseActivitry;

import org.xutils.common.Callback;

/**
 * Created by wsy on 17-9-2.
 */

public abstract class AbstractCommonCallback<T> implements Callback.CommonCallback<T> {
    public abstract void handleSuccess(T result) throws Exception;

    public abstract BaseActivitry getActivity();

    @Override
    public void onSuccess(T result) {
        Log.e(AppConfig.TAG_ERR, String.valueOf(result));
        try {
            handleSuccess(result);
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
        getActivity().closeLoding();
        Log.e(AppConfig.TAG_ERR, "finished.........");
    }


}
