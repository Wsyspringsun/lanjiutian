package com.wyw.ljtds.config;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wyw.ljtds.service.LocationService;

import org.xutils.x;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cn.jpush.android.api.JPushInterface;
import cn.xiaoneng.uiapi.Ntalker;
import cn.xiaoneng.uiapi.OnUnreadmsgListener;

/**
 * Created by Administrator on 2016/12/8 0008.
 */

public class MyApplication extends Application {
    //百度
    public LocationService locationService;
    public Vibrator mVibrator;

    //微信
    public IWXAPI wxApi;

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
        Ntalker.getInstance().initSDK(this, "lj_1000", "lanjiutian");
//        Ntalker.getInstance().enableDebug( true );
        Ntalker.getExtendInstance().message().setOnUnreadmsgListener(new OnUnreadmsgListener() {
            /**
             * @param settingid: 接待组id
             * @param username: 客服名字
             * @param msgcontent: 消息内容
             * @param messagecount: 消息总数
             */
            @Override
            public void onUnReadMsg(String settingid, String username, String msgcontent, int messagecount) {
                Log.e(AppConfig.ERR_TAG, settingid + "/" + username + ":" + msgcontent);
            }
        });

        //极光推送通知
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //百度 Location
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
//        SDKInitializer.initialize(getApplicationContext());

        //微信分享 API
        wxApi = WXAPIFactory.createWXAPI(this, AppConfig.WEIXIN_APP_ID, false);
        wxApi.registerApp(AppConfig.WEIXIN_APP_ID);
    }


    /**
     * 获取application cotext
     *
     * @return
     */
    public static Context getAppContext() {
        return mAppContext;
    }

}
