package com.wyw.ljtwl.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.wyw.ljtwl.config.AppConfig;
import com.wyw.ljtwl.config.MyApplication;

/**
 * 拉取服务器消息的后台服务
 */
public class BackgroundService extends IntentService {
    public static final String PREF_IS_ALARM_ON = "PREF_IS_ALARM_ON";
    public static final String BROADCAST_NOTIFICATION = "com.ljt.www.temp.service.IntentService.BROADCAST_NOTIFICATION";

    private static boolean isNeedNotify = true;
    private MyApplication myApp;
    private boolean startTrace = false;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BackgroundService() {
        super("PullService");
    }

    //鹰眼服务
    private OnTraceListener traceListener = new OnTraceListener() {
        @Override
        public void onBindServiceCallback(int errorNo, String message) {
            Log.e(AppConfig.TAG_ERR, "onBindServiceCallback:" + errorNo + ":" + message);
        }

        @Override
        public void onStartTraceCallback(int errorNo, String message) {
            Log.e(AppConfig.TAG_ERR, "onStartTraceCallback:" + errorNo + ":" + message);


            startTrace = true;
            myApp.mTraceClient.startGather(traceListener);
        }

        @Override
        public void onStopTraceCallback(int errorNo, String message) {
            Log.e(AppConfig.TAG_ERR, "onStopTraceCallback:" + errorNo + ":" + message);
        }

        @Override
        public void onStartGatherCallback(int errorNo, String message) {
            Log.e(AppConfig.TAG_ERR, "onStartGatherCallback:" + errorNo + ":" + message);
        }

        @Override
        public void onStopGatherCallback(int errorNo, String message) {
            Log.e(AppConfig.TAG_ERR, "onStopGatherCallback:" + errorNo + ":" + message);
        }

        @Override
        public void onPushCallback(byte messageType, PushMessage pushMessage) {
            Log.e(AppConfig.TAG_ERR, "onPushCallback:" + pushMessage);
        }

        @Override
        public void onInitBOSCallback(int errorNo, String message) {
            Log.e(AppConfig.TAG_ERR, "onInitBOSCallback:" + errorNo + ":" + message);
        }
    };

    public static void setServiceStat(Context context, boolean on) {
        Intent it = new Intent(context, BackgroundService.class);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREF_IS_ALARM_ON, on).commit();
        //开启服务
//            context.startService(it            //alert start service
        PendingIntent pit = PendingIntent.getService(context, 0, it, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (on) {
            am.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 60 * 1000, pit);
        } else {
            am.cancel(pit);
            pit.cancel();
        }
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(AppConfig.TAG_ERR, "intent.........." + intent);

        if (intent != null) {
            final String action = intent.getAction();
            Log.e(AppConfig.TAG_ERR, "action:" + action);
            if (!startTrace) {

            }
        }
    }

    public static void setServiceNotifyStat(boolean isNeedNotify) {
        BackgroundService.isNeedNotify = isNeedNotify;
    }
}
