package com.wyw.ljtmgr.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.wyw.ljtmgr.R;
import com.wyw.ljtmgr.config.AppConfig;
import com.wyw.ljtmgr.config.MyApplication;
import com.wyw.ljtmgr.config.SingleCurrentUser;
import com.wyw.ljtmgr.ui.map.BikingRouteOverlay;
import com.wyw.ljtmgr.utils.BitmapUtil;
import com.wyw.ljtmgr.utils.CommonUtil;
import com.wyw.ljtmgr.utils.MapUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import utils.GsonUtils;


/**
 * Created by wsy on 17-12-21.
 */

public class LogisticMapActivity extends AppCompatActivity {
    private final static int authBaseRequestCode = 1;
    private final static int authComRequestCode = 2;
    String authinfo = null;
    private static final String TAG_LATSTART = "TAG_LATSTART";
    private static final String TAG_LNGSTART = "TAG_LNGSTART";
    private static final String TAG_LATEND = "TAG_LATEND";
    private static final String TAG_LNGEND = "TAG_LNGEND";
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private RoutePlanSearch mSearch;
    BitmapDescriptor bitmap;

    MyApplication myApp;

    /*BaiduNaviManager.NavEventListener eventListerner = new BaiduNaviManager.NavEventListener() {

        @Override
        public void onCommonEventCall(int what, int arg1, int arg2, Bundle bundle) {
//            BNEventHandler.getInstance().handleNaviEvent(what, arg1, arg2, bundle);
            CommonUtil.log("NavEventListener onCommonEventCall");

        }
    };*/

    OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {

        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
            Log.e(AppConfig.TAG_ERR, "onGetIndoorRouteResult:" + GsonUtils.Bean2Json(walkingRouteResult));
        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
            Log.e(AppConfig.TAG_ERR, "onGetIndoorRouteResult:" + GsonUtils.Bean2Json(transitRouteResult));

        }

        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {
            Log.e(AppConfig.TAG_ERR, "onGetIndoorRouteResult:" + GsonUtils.Bean2Json(massTransitRouteResult));

        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
            Log.e(AppConfig.TAG_ERR, "onGetIndoorRouteResult:" + GsonUtils.Bean2Json(drivingRouteResult));

        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {
            Log.e(AppConfig.TAG_ERR, "onGetIndoorRouteResult:" + GsonUtils.Bean2Json(indoorRouteResult));

        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult result) {
            Log.e(AppConfig.TAG_ERR, "onGetBikingRouteResult:" + GsonUtils.Bean2Json(result));
            if (result == null) return;
            if (result.getRouteLines() == null) return;
            if (result.getRouteLines().size() == 1) {
                // 直接显示
//                route = result.getRouteLines().get(0);
                BikingRouteOverlay overlay = new MyBikingRouteOverlay(mBaiduMap);
                mBaiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();

            }

        }
    };

    private boolean useDefaultIcon = true;

    private MapUtil mapUtil;
    private ImageView imgView;
    private String mSDCardPath;
    private String APP_FOLDER_NAME = "com.wyw.ljtmgr";
    private CoordinateType mCoordinateType;
    private boolean hasInitSuccess;
    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
    private boolean hasRequestComAuth;
    private String ROUTE_PLAN_NODE = "route_plan_node";
    public static List<Activity> activityList = new LinkedList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        myApp = (MyApplication) getApplication();
        setContentView(R.layout.activity_logistic_trace);
        activityList.add(this);
        initMapView();

        /*if (initDirs()) {
            initNavi();
        }*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //        1 创建骑行线路规划检索实例；
                mSearch = RoutePlanSearch.newInstance();
//        2 创建骑行线路规划检索监听者；
//        3 设置骑行线路规划检索监听者；
                mSearch.setOnGetRoutePlanResultListener(listener);

                Intent it = getIntent();
                double latStart = SingleCurrentUser.location.getLatitude(),
                        lngStart = SingleCurrentUser.location.getLongitude();
                String latEnd = it.getStringExtra(TAG_LATEND),
                        lngEnd = it.getStringExtra(TAG_LNGEND);
                LatLng startLatlng = new LatLng(latStart, lngStart),
                        endLatlng = new LatLng(Double.parseDouble(latEnd), Double.parseDouble(lngEnd));
                Log.e(AppConfig.TAG_ERR, "latStart:" + latStart + "|lngStart:" + lngStart + ",latEnd:" + latEnd + "|lngEnd:" + lngEnd);
                drawTrace(startLatlng, endLatlng);
            }
        }, 1000);
    }


    private void initMapView() {
        BitmapUtil.init();
        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.activity_logistic_map_mapview);
        mBaiduMap = mMapView.getMap();

        imgView = (ImageView) findViewById(R.id.activity_logistic_img_cancel);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mapUtil = MapUtil.getInstance();
        mapUtil.init(mMapView);

//        BDLocation location = SingleCurrentUser.location;
        LatLng ll = new LatLng(35.489899f, 112.864964f);
//        MapUtil.targetMap2LatLng(mBaiduMap, ll);
        mapUtil.updateStatus(ll, false);

    }

    private void drawTrace(LatLng startLatlng, LatLng endLatlng) {
//        4 准备检索起、终点信息；
        PlanNode stNode = PlanNode.withLocation(startLatlng);
        PlanNode enNode = PlanNode.withLocation(endLatlng);
//        5 发起骑行线路规划检索；
        mSearch.bikingSearch((new BikingRoutePlanOption())
                .from(stNode)
                .to(enNode));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearch.destroy();
    }

    public void setUseDefaultIcon(boolean useDefaultIcon) {
        this.useDefaultIcon = useDefaultIcon;
    }

    private class MyBikingRouteOverlay extends BikingRouteOverlay {
        public MyBikingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }


    }

    public static Intent getIntent(Context context, String latStart, String lngStart, String latEnd, String lngEnd) {
        Intent it = new Intent(context, LogisticMapActivity.class);
        it.putExtra(TAG_LATSTART, latStart);
        it.putExtra(TAG_LNGSTART, lngStart);
        it.putExtra(TAG_LATEND, latEnd);
        it.putExtra(TAG_LNGEND, lngEnd);
        return it;
    }


    /*private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private void initNavi() {
        // 申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 0);
        }

        BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME,


                new BaiduNaviManager.NaviInitListener() {
                    @Override
                    public void onAuthResult(int status, String msg) {

                        if (0 == status) {
                            authinfo = "key校验成功!";
                        } else {
                            authinfo = "key校验失败, " + msg;
                        }
                        LogisticMapActivity.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(LogisticMapActivity.this, authinfo, Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    public void initSuccess() {
                        Toast.makeText(LogisticMapActivity.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                    }

                    public void initStart() {
                        Toast.makeText(LogisticMapActivity.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
                    }

                    public void initFailed() {
                        Toast.makeText(LogisticMapActivity.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
                    }
                }, null);
    }


    private boolean hasCompletePhoneAuth() {
        PackageManager pm = this.getPackageManager();
        for (String auth : permissions) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void routeplanToNavi(CoordinateType coType) {
        mCoordinateType = coType;
        if (!hasInitSuccess) {
            Toast.makeText(LogisticMapActivity.this, "还未初始化!", Toast.LENGTH_SHORT).show();
        }
        // 权限申请
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // 保证导航功能完备
            if (!hasCompletePhoneAuth()) {
                if (!hasRequestComAuth) {
                    hasRequestComAuth = true;
                    this.requestPermissions(permissions, authComRequestCode);
                    return;
                } else {
                    Toast.makeText(LogisticMapActivity.this, "没有完备的权限!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;
        switch (coType) {
            case GCJ02: {
                sNode = new BNRoutePlanNode(116.30142, 40.05087, "百度大厦", null, coType);
                eNode = new BNRoutePlanNode(116.39750, 39.90882, "北京天安门", null, coType);
                break;
            }
            case WGS84: {
                sNode = new BNRoutePlanNode(116.300821, 40.050969, "百度大厦", null, coType);
                eNode = new BNRoutePlanNode(116.397491, 39.908749, "北京天安门", null, coType);
                break;
            }
            case BD09_MC: {
                sNode = new BNRoutePlanNode(12947471, 4846474, "百度大厦", null, coType);
                eNode = new BNRoutePlanNode(12958160, 4825947, "北京天安门", null, coType);
                break;
            }
            case BD09LL: {
                sNode = new BNRoutePlanNode(116.30784537597782, 40.057009624099436, "百度大厦", null, coType);
                eNode = new BNRoutePlanNode(116.40386525193937, 39.915160800132085, "北京天安门", null, coType);
                break;
            }
            default:
                ;
        }
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<>();
            list.add(sNode);
            list.add(eNode);

            // 开发者可以使用旧的算路接口，也可以使用新的算路接口,可以接收诱导信息等
            // BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode));
            BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, (sNode),
                    eventListerner);
        }
    }


    public class MyRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public MyRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            *//*
             * 设置途径点以及resetEndNode会回调该接口
             *//*
            for (Activity ac : activityList) {

                if (ac.getClass().getName().endsWith("BNDemoGuideActivity")) {

                    return;
                }
            }
            Intent intent = new Intent(BNDemoMainActivity.this, BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);

        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            Toast.makeText(BNDemoMainActivity.this, "算路失败", Toast.LENGTH_SHORT).show();
        }
    }*/

}
