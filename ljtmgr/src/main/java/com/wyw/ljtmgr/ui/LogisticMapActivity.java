package com.wyw.ljtmgr.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
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
import com.wyw.ljtmgr.R;
import com.wyw.ljtmgr.config.AppConfig;
import com.wyw.ljtmgr.config.MyApplication;
import com.wyw.ljtmgr.config.SingleCurrentUser;
import com.wyw.ljtmgr.ui.map.BikingRouteOverlay;
import com.wyw.ljtmgr.utils.BitmapUtil;
import com.wyw.ljtmgr.utils.MapUtil;

import utils.GsonUtils;

/**
 * Created by wsy on 17-12-21.
 */

public class LogisticMapActivity extends AppCompatActivity {
    private static final String TAG_LATSTART = "TAG_LATSTART";
    private static final String TAG_LNGSTART = "TAG_LNGSTART";
    private static final String TAG_LATEND = "TAG_LATEND";
    private static final String TAG_LNGEND = "TAG_LNGEND";
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private RoutePlanSearch mSearch;
    BitmapDescriptor bitmap;

    MyApplication myApp;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myApp = (MyApplication) getApplication();
        setContentView(R.layout.activity_logistic_trace);
        initMapView();
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
                drawTrace(startLatlng, endLatlng);
            }
        }, 2000);
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

}
