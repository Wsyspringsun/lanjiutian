package com.wyw.ljtds.ui.user.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.location.CoordinateType;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.api.track.DistanceRequest;
import com.baidu.trace.api.track.HistoryTrackResponse;
import com.baidu.trace.api.track.LatestPoint;
import com.baidu.trace.api.track.LatestPointResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.SupplementMode;
import com.baidu.trace.api.track.TrackPoint;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.StatusCodes;
import com.baidu.trace.model.TraceLocation;
import com.baidu.trace.model.TransportMode;
import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.model.CurrentLocation;
import com.wyw.ljtds.receivers.TrackReceiver;
import com.wyw.ljtds.ui.base.BikingRouteOverlay;
import com.wyw.ljtds.ui.base.WalkingRouteOverlay;
import com.wyw.ljtds.utils.BitmapUtil;
import com.wyw.ljtds.utils.CommonUtil;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.MapUtil;
import com.wyw.ljtds.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wsy on 17-12-21.
 */

public class LogisticTraceActivity extends AppCompatActivity {
    private static final String TAG_ENTITYNAME = "com.wyw.ljtds.ui.user.order.LogisticTraceActivity.TAG_ENTITYNAME";
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    //    private RoutePlanSearch mSearch;
    BitmapDescriptor bitmap;

    private boolean isRealTimeRunning = true;
    private RealTimeHandler realTimeHandler = new RealTimeHandler();
    MyApplication myApp;
    private LatLng targetPoint;
    /*OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {

            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
                Log.e(AppConfig.ERR_TAG, "onGetIndoorRouteResult:" + GsonUtils.Bean2Json(walkingRouteResult));
            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
                Log.e(AppConfig.ERR_TAG, "onGetIndoorRouteResult:" + GsonUtils.Bean2Json(transitRouteResult));

            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {
                Log.e(AppConfig.ERR_TAG, "onGetIndoorRouteResult:" + GsonUtils.Bean2Json(massTransitRouteResult));

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
                Log.e(AppConfig.ERR_TAG, "onGetIndoorRouteResult:" + GsonUtils.Bean2Json(drivingRouteResult));

            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {
                Log.e(AppConfig.ERR_TAG, "onGetIndoorRouteResult:" + GsonUtils.Bean2Json(indoorRouteResult));

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult result) {
    //            Log.e(AppConfig.ERR_TAG, "onGetBikingRouteResult:" + GsonUtils.Bean2Json(bikingRouteResult));
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
        };*/
    OnTrackListener mTrackListener = new OnTrackListener() {
        @Override
        public void onLatestPointCallback(LatestPointResponse response) {
            Log.e(AppConfig.ERR_TAG, "onHistoryTrackCallback:" + GsonUtils.Bean2Json(response));
//            drawLines(response);
            if (StatusCodes.SUCCESS != response.getStatus()) {
                return;
            }

            LatestPoint point = response.getLatestPoint();
            if (null == point || CommonUtil.isZeroPoint(point.getLocation().getLatitude(), point.getLocation()
                    .getLongitude())) {
                return;
            }

            LatLng currentLatLng = mapUtil.convertTrace2Map(point.getLocation());
            if (null == currentLatLng) {
                return;
            }
            CurrentLocation.locTime = point.getLocTime();
            CurrentLocation.latitude = currentLatLng.latitude;
            CurrentLocation.longitude = currentLatLng.longitude;

            double distance = DistanceUtil.getDistance(currentLatLng, targetPoint);
            tvDistance.setText("距离您:" + (int)distance + "米");

            if (null != mapUtil) {
                mapUtil.updateStatus(currentLatLng, true);
            }
        }
    };

    private boolean useDefaultIcon = true;
    //    private TrackReceiver trackReceiver = null;
    private PowerManager.WakeLock wakeLock = null;

    //鹰眼服务
    private OnTraceListener traceListener = new OnTraceListener() {
        @Override
        public void onBindServiceCallback(int errorNo, String message) {
            Log.e(AppConfig.ERR_TAG, "onBindServiceCallback:" + errorNo + ":" + message);
        }

        @Override
        public void onStartTraceCallback(int errorNo, String message) {
            Log.e(AppConfig.ERR_TAG, "onStartTraceCallback:" + errorNo + ":" + message);
        }

        @Override
        public void onStopTraceCallback(int errorNo, String message) {
            Log.e(AppConfig.ERR_TAG, "onStopTraceCallback:" + errorNo + ":" + message);
        }

        @Override
        public void onStartGatherCallback(int errorNo, String message) {
            Log.e(AppConfig.ERR_TAG, "onStartGatherCallback:" + errorNo + ":" + message);
        }

        @Override
        public void onStopGatherCallback(int errorNo, String message) {
            Log.e(AppConfig.ERR_TAG, "onStopGatherCallback:" + errorNo + ":" + message);
        }

        @Override
        public void onPushCallback(byte messageType, PushMessage pushMessage) {
            Log.e(AppConfig.ERR_TAG, "onPushCallback:" + pushMessage);
        }

        @Override
        public void onInitBOSCallback(int errorNo, String message) {
            Log.e(AppConfig.ERR_TAG, "onInitBOSCallback:" + errorNo + ":" + message);
        }
    };
    private int tag = 1;
    private RealTimeLocRunnable realTimeLocRunnable;
    private OnEntityListener entityListener = new OnEntityListener() {

        @Override
        public void onReceiveLocation(TraceLocation location) {
            Log.e(AppConfig.ERR_TAG, "OnEntityListener.onReceiveLocation:" + GsonUtils.Bean2Json(location));
            if (StatusCodes.SUCCESS != location.getStatus() || CommonUtil.isZeroPoint(location.getLatitude(),
                    location.getLongitude())) {
                return;
            }
            LatLng currentLatLng = MapUtil.convertTraceLocation2Map(location);
            if (null == currentLatLng) {
                return;
            }
            CurrentLocation.locTime = CommonUtil.toTimeStamp(location.getTime());
            CurrentLocation.latitude = currentLatLng.latitude;
            CurrentLocation.longitude = currentLatLng.longitude;

            if (null != mapUtil) {
                mapUtil.updateStatus(currentLatLng, true);
            }
        }

    };
    private MapUtil mapUtil;
    private String entityName;
    private TextView tvDistance;
    private ImageView imgCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        targetPoint = new LatLng(35.504755f, 112.839483f);

        setContentView(R.layout.activity_logistic_trace);
        initMapView();
        myApp = (MyApplication) getApplication();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //        1 创建骑行线路规划检索实例；
//        mSearch = RoutePlanSearch.newInstance();
//        2 创建骑行线路规划检索监听者；
//        3 设置骑行线路规划检索监听者；
//        mSearch.setOnGetRoutePlanResultListener(listener);
//        this.entityName = getIntent().getStringExtra(TAG_ENTITYNAME);
        this.entityName = "861007030662863";
//        Log.e(AppConfig.ERR_TAG, "entityName:" + this.entityName);
        ToastUtil.show(LogisticTraceActivity.this, "开始查看轨迹");
        realTimeLocRunnable = new RealTimeLocRunnable(20);
        realTimeHandler.post(realTimeLocRunnable);
    }


    private void initMapView() {
        BitmapUtil.init();
        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.activity_logistic_trace_mapview);
        tvDistance = (TextView) findViewById(R.id.activity_logistic_trace_distance);
        imgCancel = (ImageView) findViewById(R.id.activity_logistic_trace_img_cancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBaiduMap = mMapView.getMap();

        mapUtil = MapUtil.getInstance();
        mapUtil.init(mMapView);
        mapUtil.setCenter(myApp);

//        BDLocation location = SingleCurrentUser.location;
        LatLng ll = new LatLng(35.489899f, 112.864964f);
//        MapUtil.targetMap2LatLng(mBaiduMap, ll);
        mapUtil.updateStatus(ll, false);

    }


    /*private void routeplanToNavi(CoordinateType coType) {
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;
        switch(coType) {
            case GCJ02: {
                sNode = new BNRoutePlanNode(116.30142, 40.05087,
                        "百度大厦", null, coType);
                eNode = new BNRoutePlanNode(116.39750, 39.90882,
                        "北京天安门", null, coType);
                break;
            }
            case WGS84: {
                sNode = new BNRoutePlanNode(116.300821,40.050969,
                        "百度大厦", null, coType);
                eNode = new BNRoutePlanNode(116.397491,39.908749,
                        "北京天安门", null, coType);
                break;
            }
            case BD09_MC: {
                sNode = new BNRoutePlanNode(12947471,4846474,
                        "百度大厦", null, coType);
                eNode = new BNRoutePlanNode(12958160,4825947,
                        "北京天安门", null, coType);
                break;
            }
            case BD09LL: {
                sNode = new BNRoutePlanNode(116.30784537597782,
                        40.057009624099436, "百度大厦", null, coType);
                eNode = new BNRoutePlanNode(116.40386525193937,
                        39.915160800132085, "北京天安门", null, coType);
                break;
            }
            default : ;
        }
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode));
        }
    }*/

    private void drawLines(HistoryTrackResponse response) {
        //构建MarkerOption，用于在地图上添加Marker
        List<TrackPoint> trackPoints = response.getTrackPoints();
        if (trackPoints != null && trackPoints.size() > 1) {
            //必须大于两个点
            List<LatLng> points = new ArrayList<>();
            for (TrackPoint tp : trackPoints) {
                LatLng ll = new LatLng(tp.getLocation().getLatitude(), tp.getLocation().getLongitude());
                points.add(ll);
            }
            OverlayOptions ooPolyline = new PolylineOptions()
                    .width(10)
                    .color(0xAAFF0000).points(points);
            mBaiduMap.addOverlay(ooPolyline);
        }


    }


/*    private void drawTrace(LatLng startLatlng, LatLng endLatlng) {
//        4 准备检索起、终点信息；
        PlanNode stNode = PlanNode.withLocation(startLatlng);
        PlanNode enNode = PlanNode.withLocation(endLatlng);
//        5 发起骑行线路规划检索；
        mSearch.bikingSearch((new BikingRoutePlanOption())
                .from(stNode)
                .to(enNode));
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mSearch.destroy();
//        myApp.mTraceClient.stopTrace(myApp.mTrace, traceListener);
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

    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
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

    /**
     * 获取实时轨迹
     */
    class RealTimeLocRunnable implements Runnable {

        private int interval = 10;

        public RealTimeLocRunnable(int interval) {
            this.interval = interval;
        }

        @Override
        public void run() {
            if (isRealTimeRunning) {
                myApp.getCurrentLocation(entityListener, mTrackListener, entityName);
                realTimeHandler.postDelayed(this, interval * 1000);
            }
        }
    }

    static class RealTimeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }

    }

    public static Intent getIntent(Context context, String entityName) {
        Intent it = new Intent(context, LogisticTraceActivity.class);
        it.putExtra(TAG_ENTITYNAME, entityName);
        return it;
    }

}
