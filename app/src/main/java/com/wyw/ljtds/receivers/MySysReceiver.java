package com.wyw.ljtds.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wyw.ljtds.config.AppConfig;

import cn.jpush.android.service.JPushMessageReceiver;

/**
 * Created by wsy on 17-10-19.
 */

public class MySysReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(AppConfig.ERR_TAG, "MySysReceiver intent.getAction:" + intent);
        if (intent != null) {
            Log.e(AppConfig.ERR_TAG, "MySysReceiver intent.getAction:" + intent.getAction());
        }
    }
}
