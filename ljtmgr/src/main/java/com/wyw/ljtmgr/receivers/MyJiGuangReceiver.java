package com.wyw.ljtmgr.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import com.wyw.ljtmgr.config.AppConfig;
import com.wyw.ljtmgr.service.RealTimeBackgroundService;
import com.wyw.ljtmgr.utils.CommonUtil;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.service.PushService;


/**
 * Created by wsy on 17-10-19.
 */

public class MyJiGuangReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            CommonUtil.log("[MyReceiver] onReceive - ");
            Log.e(AppConfig.TAG_ERR, "getAction:" + intent.getAction());

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                CommonUtil.log("[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                CommonUtil.log("[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                processCustomMessage(context, bundle);

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//                CommonUtil.log("[MyReceiver] 接收到推送下来的通知" + PreferenceCache.getActived() + " ..." + UserBiz.isLogined());
                RealTimeBackgroundService.playVoice(context, true);
            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                RealTimeBackgroundService.openActivity(context, true);
                CommonUtil.log("[MyReceiver] 用户点击打开了通知");


            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                CommonUtil.log("[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                CommonUtil.log("[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                CommonUtil.log("[MyReceiver] Unhandled intent - " + intent.getAction());
                Intent pushintent = new Intent(context, PushService.class);//启动极光推送的服务
                context.startService(pushintent);
            }
        } catch (Exception e) {

        }
    }

    private void processCustomMessage(Context context, Bundle bundle) {
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        CommonUtil.log(message);
    }
}