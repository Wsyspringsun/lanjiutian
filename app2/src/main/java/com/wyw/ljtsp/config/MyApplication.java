package com.wyw.ljtsp.config;

import android.app.Application;
import android.content.Context;

import org.xutils.x;

/**
 * Created by Administrator on 2017/7/3 0003.
 */

public class MyApplication extends Application {
    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppContext = getApplicationContext();

        //xytils3
        x.Ext.init(this);
        //x.Ext.setDebug(BuildConfig.DEBUG);//是否输出debug日志，开启后会影响性能

    }

    /**
     * 获取application cotext
     * @return
     */
    public static Context getAppContext() {
        return mAppContext;
    }
}
