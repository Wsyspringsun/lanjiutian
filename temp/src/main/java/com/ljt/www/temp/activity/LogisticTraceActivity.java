package com.ljt.www.temp.activity;

import android.support.v7.app.AppCompatActivity;


/**
 * Created by wsy on 17-12-21.
 */

public class LogisticTraceActivity extends AppCompatActivity {
/*    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private RoutePlanSearch mSearch;
    BitmapDescriptor bitmap;

    private boolean isRealTimeRunning = true;
    private RealTimeHandler realTimeHandler = new RealTimeHandler();
    MyApplication myApp;
    OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {

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
    };
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

            if (null != mapUtil) {
                mapUtil.updateStatus(currentLatLng, true);
            }
        }
    };

    private boolean useDefaultIcon = true;
    private TrackReceiver trackReceiver = null;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_logistic_trace);
        initMapView();
        myApp = (MyApplication) getApplication();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //        1 创建骑行线路规划检索实例；
        mSearch = RoutePlanSearch.newInstance();
//        2 创建骑行线路规划检索监听者；
//        3 设置骑行线路规划检索监听者；
        mSearch.setOnGetRoutePlanResultListener(listener);

        Log.e(AppConfig.ERR_TAG, "entityName:" + myApp.entityName);

        View tvService = findViewById(R.id.activity_logistic_trace_tv_service);
        View tvGather = findViewById(R.id.activity_logistic_trace_tv_gather);
        View tvQuery = findViewById(R.id.activity_logistic_trace_tv_query);
        tvService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                float lng = 112.839483f, lat = 35.504755f;
//                LatLng shopLL = new LatLng(lat, lng);
//                LatLng ll = new LatLng(35.489899f, 112.864964f);
//                drawTrace(ll, shopLL);


                Log.e("BaiduTraceSDK", "Start trace service........");
                ToastUtil.show(LogisticTraceActivity.this,"开启轨迹服务,请点击[采集]");
                myApp.mTraceClient.startTrace(myApp.mTrace, traceListener);
            }
        });

        tvGather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(AppConfig.ERR_TAG, "Start trace gather........");
                ToastUtil.show(LogisticTraceActivity.this,"开始采集轨迹,请点击[查询]");
                myApp.mTraceClient.startGather(traceListener);
            }
        });

        tvQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(LogisticTraceActivity.this,"开始跟着轨迹");
                realTimeLocRunnable = new RealTimeLocRunnable(30);
                realTimeHandler.post(realTimeLocRunnable);
            }
        });


    }




    private void initMapView() {
        BitmapUtil.init();
        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.activity_logistic_trace_mapview);
        mBaiduMap = mMapView.getMap();

        mapUtil = MapUtil.getInstance();
        mapUtil.init(mMapView);
        mapUtil.setCenter(myApp);

//        BDLocation location = SingleCurrentUser.location;
        LatLng ll = new LatLng(35.489899f, 112.864964f);
//        MapUtil.targetMap2LatLng(mBaiduMap, ll);
        mapUtil.updateStatus(ll, false);

    }

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
        myApp.mTraceClient.stopTrace(myApp.mTrace, traceListener);
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

    *//**
     * 获取实时轨迹
     *//*
    class RealTimeLocRunnable implements Runnable {

        private int interval = 10;

        public RealTimeLocRunnable(int interval) {
            this.interval = interval;
        }

        @Override
        public void run() {
            if (isRealTimeRunning) {
                myApp.getCurrentLocation(entityListener, mTrackListener);
                realTimeHandler.postDelayed(this, interval * 1000);
            }
        }
    }

    static class RealTimeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }*/

}
