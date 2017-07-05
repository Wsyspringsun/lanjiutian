package com.wyw.ljtds.config;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.xutils.x;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cn.xiaoneng.uiapi.Ntalker;

/**
 * Created by Administrator on 2016/12/8 0008.
 */

public class MyApplication extends Application {
    private static final String TAG = "CrashingApp";
    private static Context mAppContext;
    @Override
    public void onCreate() {
        super.onCreate();

        mAppContext = getApplicationContext();

        //xytils3
        x.Ext.init(this);
        //x.Ext.setDebug(BuildConfig.DEBUG);//是否输出debug日志，开启后会影响性能

        //fresco
        Fresco.initialize(this);

        //app crash收集
        CustomActivityOnCrash.install(this);

        //小能
        Ntalker.getInstance().initSDK( this, "lj_1000", "lanjiutian" );
//        Ntalker.getInstance().enableDebug( true );

    }

    /**
     * 获取application cotext
     * @return
     */
    public static Context getAppContext() {
        return mAppContext;
    }

}
