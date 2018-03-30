package com.wyw.ljtds.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.wyw.ljtds.utils.Utils;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by wsy on 17-10-19.
 */

public class MyXiangNengReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            Utils.log("[MyReceiver] onReceive - ");

        } catch (Exception e) {

        }
    }

    private void processCustomMessage(Context context, Bundle bundle) {
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        Utils.log(message);
    }
}