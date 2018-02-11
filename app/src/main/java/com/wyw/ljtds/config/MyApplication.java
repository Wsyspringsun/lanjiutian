package com.wyw.ljtds.config;

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
import com.baidu.trace.Trace;
import com.baidu.trace.api.entity.LocRequest;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.api.track.LatestPointRequest;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.model.ProcessOption;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.model.AddressModel;
import com.wyw.ljtds.model.MyLocation;
import com.wyw.ljtds.model.SingleCurrentUser;
import com.wyw.ljtds.service.LocationService;
import com.wyw.ljtds.utils.CommonUtil;
import com.wyw.ljtds.utils.NetUtil;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.Utils;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cn.jpush.android.api.JPushInterface;
import cn.xiaoneng.uiapi.Ntalker;
import cn.xiaoneng.uiapi.OnUnreadmsgListener;

/**
 * Created by Administrator on 2016/12/8 0008.
 */

public class MyApplication extends Application {

    public interface LocationedInterface {
        public void afterLocation(BDLocation location);
    }

    public static int screenHeight = 0;
    public static int screenWidth = 0;
    //百度
    public LocationService locationService;
    public Vibrator mVibrator;

    //微信
    public IWXAPI wxApi;

    private static final String TAG = "CrashingApp";
    private static Context mAppContext;
    public LBSTraceClient mTraceClient = null;
    //    public long serviceId = 155924;
    public long serviceId = 158684;
    public String entityName = "";
    public Trace mTrace;
    public boolean isServerOk;//valid is server can connect
    private LocRequest locRequest;

    // Location listener
    private BDLocationListener locationListner = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                SingleCurrentUser.bdLocation = location;
                if (SingleCurrentUser.location == null) {
                    SingleCurrentUser.updateLocation(MyLocation.newInstance(location.getLatitude(), location.getLongitude(), location.getAddrStr()));
                }
//                locationService.unregisterListener(locationListner);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        //设备标识
        entityName = "861007030662863";
//        entityName = Utils.getImei(this);
        mAppContext = getApplicationContext();
        initView();
        //xytils3
        x.Ext.init(this);
        //x.Ext.setDebug(BuildConfig.DEBUG);//是否输出debug日志，开启后会影响性能

        //网络数据服务

        //fresco
        Fresco.initialize(this);
        //app crash收集
        CustomActivityOnCrash.install(this);
        //小能
//        Ntalker.getInstance().initSDK(this, "lj_1000", "lanjiutian"); //old version
        /**
         *  @params appContext
         *  @params siteid: 企业id，即企业唯一标识。格式示例：kf_9979【必填】
         *  @params sdkkey: 企业key，即小能通行密钥【必填】
         *  @return int  0 表示初始化成功, 其他值请查看错误码
         */
        String siteId = "lj_1000";
        String appkey = "lanjiutian";
        if (UserBiz.isLogined()) {
            Ntalker.getBaseInstance().initSDK(this, siteId, appkey);
        } else {
            Ntalker.getBaseInstance().initSDK(this, siteId, appkey, entityName);
        }
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

        //微信分享 API
        wxApi = WXAPIFactory.createWXAPI(this, AppConfig.WEIXIN_APP_ID, false);
        wxApi.registerApp(AppConfig.WEIXIN_APP_ID);

        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(mAppContext);
        //百度 Location
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        mTraceClient = new LBSTraceClient(mAppContext);
        mTrace = new Trace(serviceId, entityName);
        locRequest = new LocRequest(serviceId);

        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(locationListner);
        LocationClientOption locOpt = locationService.getDefaultLocationClientOption();
        locationService.setLocationOption(locOpt);

    }

    public void onDestory() {
        locationService.unregisterListener(locationListner); //注销掉监听
        locationService.stop(); //停止定位服务
    }


    /**
     * 初始化当前设备 尺寸等界面信息
     */
    private void initView() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenHeight = dm.heightPixels;
        screenWidth = dm.widthPixels;
    }

    /**
     * 获取当前位置
     */
    public void getCurrentLocation(OnEntityListener entityListener, OnTrackListener trackListener, String entityName) {
        // 网络连接正常，开启服务及采集，则查询纠偏后实时位置；否则进行实时定位
        if (NetUtil.isNetworkAvailable(mAppContext)) {
            Log.e(AppConfig.ERR_TAG, "........not queryRealTimeLoc.........");
            LatestPointRequest request = new LatestPointRequest(AppConfig.TAG_TRACE, serviceId, entityName);
            ProcessOption processOption = new ProcessOption();
            processOption.setNeedDenoise(true);
            processOption.setRadiusThreshold(100);
            request.setProcessOption(processOption);
            mTraceClient.queryLatestPoint(request, trackListener);
        } else {
            Log.e(AppConfig.ERR_TAG, "queryRealTimeLoc.........");
            mTraceClient.queryRealTimeLoc(locRequest, entityListener);
        }
    }


    /**
     * 获取application cotext
     *
     * @return
     */
    public static Context getAppContext() {
        return mAppContext;
    }

    public static void initLoginer() {
        new BizDataAsyncTask<List<AddressModel>>() {
            @Override
            protected List<AddressModel> doExecute() throws ZYException, BizFailure {
                Log.e(AppConfig.ERR_TAG, "initLoginer  doExecute......");
                return UserBiz.selectUserAddress();
            }

            @Override
            protected void onExecuteSucceeded(List<AddressModel> addressModels) {
                Log.e(AppConfig.ERR_TAG, "initLoginer.  onExecuteSucceeded.....");
                if (addressModels == null || addressModels.size() <= 0) {
                    return;
                }
                Log.e(AppConfig.ERR_TAG, "initLoginer.  onExecuteSucceeded....addressModels.");
                AddressModel defaultAddr = addressModels.get(0);
                for (AddressModel addr : addressModels) {
                    if ("0".equals(addr.getDEFAULT_FLG())) {
                        defaultAddr = addr;
                        break;
                    }
                }
                String locStr = defaultAddr.getADDRESS_LOCATION();
                Log.e(AppConfig.ERR_TAG, "initLoginer.  onExecuteSucceeded...getADDRESS_LOCATION.....");
                if (StringUtils.isEmpty(locStr)) {
                    return;
                }
                String[] locArr = locStr.split("|");
                String lat = locArr[1];
                String lng = locArr[2];
                defaultAddr.setLAT(lat);
                defaultAddr.setLNG(lng);
                //det location is wenchang
                SingleCurrentUser.defaltAddr = defaultAddr;
            }

            @Override
            protected void OnExecuteFailed() {
            }
        }.execute();
    }

}
