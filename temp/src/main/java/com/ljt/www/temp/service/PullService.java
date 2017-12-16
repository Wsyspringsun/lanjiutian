package com.ljt.www.temp.service;

import android.app.ActivityManager;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.ljt.www.temp.AppConfig;
import com.ljt.www.temp.MainActivity;
import com.ljt.www.temp.R;

import java.util.List;

/**
 * 拉取服务器消息的后台服务
 */
public class PullService extends IntentService {
    public static final String PREF_IS_ALARM_ON = "PREF_IS_ALARM_ON";
    public static final String BROADCAST_NOTIFICATION = "com.ljt.www.temp.service.IntentService.BROADCAST_NOTIFICATION";

    private static final String EXTRA_PARAM2 = "com.ljt.www.temp.extra.PARAM2";
    private static boolean isNeedNotify = true;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public PullService() {
        super("PullService");
    }


    public static void setServiceStat(Context context, boolean on) {
        Intent it = new Intent(context, PullService.class);
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

    private boolean isNetworkConn() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() == null) return false;
        return cm.getActiveNetworkInfo().isConnected();
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(AppConfig.TAG, "intent.........." + intent);
        //Intent it = new Intent(this, MainActivity.class); //此方法无法启动Activity
        Intent it = new Intent(Intent.ACTION_MAIN).setClassName(MainActivity.class.getPackage().getName(), MainActivity.class.getName());
        Log.e(AppConfig.TAG, "MainActivity:" + MainActivity.class.getPackage().getName() + "----" + MainActivity.class.getName());
//        it.setAction(Intent.ACTION_MAIN);
//        it.addCategory(Intent.CATEGORY_LAUNCHER);
//        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//关键的一步，设置启动模式
//        PendingIntent pi = PendingIntent.getService(this, 0, it, PendingIntent.FLAG_ONE_SHOT);//启动 Service
        if (isNeedNotify) {
            PendingIntent pi = PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_ONE_SHOT); // 启动 Activity

            Notification notification = new NotificationCompat.Builder(this)
                    .setTicker("蓝九天通知").setSmallIcon(R.drawable.ic_notifications_black_24dp)
                    .setContentTitle("你有新的dingdan").setContentText("总价188").setContentIntent(pi).setAutoCancel(true).build();
            notification.defaults = Notification.DEFAULT_LIGHTS;
            notification.sound = Uri.parse("android.resource://" + getPackageName()
                    + "/" + R.raw.prompt);
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.notify(0, notification);
        } else {
            //发送通知的广播
            sendBroadcast(new Intent(BROADCAST_NOTIFICATION));
        }


        if (!isNetworkConn()) return;
        if (intent != null) {
            final String action = intent.getAction();
            Log.e(AppConfig.TAG, "action:" + action);
        }
    }

    public static void setServiceNotifyStat(boolean isNeedNotify) {
        PullService.isNeedNotify = isNeedNotify;
    }
}
