package com.wyw.ljtmgr.config;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.api.entity.LocRequest;
import com.google.gson.Gson;
import com.wyw.ljtmgr.biz.UserBiz;
import com.wyw.ljtmgr.model.LoginModel;
import com.wyw.ljtmgr.service.LocationService;

import org.xutils.BuildConfig;
import org.xutils.x;

import java.util.Set;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cn.alien95.resthttp.request.RestHttp;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/7/3 0003.
 */

public class MyApplication extends Application {
    private static LoginModel currentLoginer;
    public LBSTraceClient mTraceClient;
    //    public long serviceId = 155924;
    public long serviceId = 158684;
    public static int screenHeight = 0;
    public static int screenWidth = 0;
    //百度
    public LocationService locationService;
    public Vibrator mVibrator;
    private LocRequest locRequest;

    public static void clearLoginer() {
        currentLoginer = null;
    }

    public static LoginModel getCurrentLoginer() {
        if (!UserBiz.isLogined()) return null;
        if (currentLoginer == null) {
            String str = PreferenceCache.getUser();
            Log.e(AppConfig.TAG_ERR, "getCurrentLoginer str:" + str);
            currentLoginer = new Gson().fromJson(str, LoginModel.class);
        }
        return currentLoginer;
    }

    private static Context mAppContext;

    private BDLocationListener locationListner = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                SingleCurrentUser.location = location;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        initView();

        mAppContext = getApplicationContext();

        //xytils3
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);//是否输出debug日志，开启后会影响性能
        //极光推送通知
//        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        //app crash收集
        CustomActivityOnCrash.install(this);

        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(mAppContext);
        //百度 Location
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        mTraceClient = new LBSTraceClient(mAppContext);
        locRequest = new LocRequest(serviceId);

        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(locationListner);
        LocationClientOption locOpt = locationService.getDefaultLocationClientOption();
        locationService.setLocationOption(locOpt);

        RestHttp.initialize(this);
        if (BuildConfig.DEBUG) {
            RestHttp.setDebug(true, "network");
        }

        if (!UserBiz.isLogined()) {
            JPushInterface.cleanTags(getApplicationContext(), 0);
        }
    }

    private void initView() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenHeight = dm.heightPixels;
        screenWidth = dm.widthPixels;
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
