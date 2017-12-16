package com.ljt.www.temp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import com.ljt.www.temp.AppConfig;
import com.ljt.www.temp.MainActivity;
import com.ljt.www.temp.service.PullService;


/**
 * Created by wsy on 17-12-8.
 */

public class StartupReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(AppConfig.TAG, "action:" + intent.getAction());
        //恢复后台任务状态
        boolean isOn = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PullService.PREF_IS_ALARM_ON, false);
        PullService.setServiceStat(context, isOn);
    }
}
