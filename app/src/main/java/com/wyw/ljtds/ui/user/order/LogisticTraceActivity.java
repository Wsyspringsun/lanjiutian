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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.location.CoordinateType;
import com.baidu.trace.api.entity.LocRequest;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.api.track.DistanceRequest;
import com.baidu.trace.api.track.HistoryTrackResponse;
import com.baidu.trace.api.track.LatestPoint;
import com.baidu.trace.api.track.LatestPointRequest;
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
import com.wyw.ljtds.model.ProductInfo;
import com.wyw.ljtds.model.SingleCurrentUser;
import com.wyw.ljtds.receivers.TrackReceiver;
import com.wyw.ljtds.ui.base.BikingRouteOverlay;
import com.wyw.ljtds.ui.base.WalkingRouteOverlay;
import com.wyw.ljtds.utils.BitmapUtil;
import com.wyw.ljtds.utils.CommonUtil;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.MapUtil;
import com.wyw.ljtds.utils.NetUtil;
import com.wyw.ljtds.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import static com.wyw.ljtds.model.CurrentLocation.latitude;
import static com.wyw.ljtds.model.CurrentLocation.longitude;

/**
 * Created by wsy on 17-12-21.
 */

public class LogisticTraceActivity extends AppCompatActivity {
    private static final String TAG_ENTITYNAME = "com.wyw.ljtds.ui.user.order.LogisticTraceActivity.TAG_ENTITYNAME";
    private static final String TAG_LAT = "com.wyw.ljtds.ui.user.order.LogisticTraceActivity.TAG_LAT";
    private static final String TAG_LNG = "com.wyw.ljtds.ui.user.order.LogisticTraceActivity.TAG_LNG";
    private String txtDistance = "距离您:--米";
    private MapView mMapView;
    public BaiduMap baiduMap = null;

//    private BaiduMap mBaiduMap;
    //    private RoutePlanSearch mSearch;
//    BitmapDescriptor bitmap;

    BitmapDescriptor pic = BitmapDescriptorFactory.fromResource(R.drawable.ic_guiji);
//    BitmapDescriptor pic = BitmapDescriptorFactory.fromResource(R.drawable.ic_logistic_curior);

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

            LatLng currentLatLng = MapUtil.convertTrace2Map(point.getLocation());
            if (null == currentLatLng) {
                return;
            }
            CurrentLocation.locTime = point.getLocTime();
            latitude = currentLatLng.latitude;
            longitude = currentLatLng.longitude;
            updateDistance(currentLatLng);
            Log.e(AppConfig.ERR_TAG, "onHistoryTrackCallback updateok:" + GsonUtils.Bean2Json(response));


        }
    };

    private void resetPos() {
        //        BDLocation location = SingleCurrentUser.location;
        LatLng ll = new LatLng(SingleCurrentUser.defaultLat, SingleCurrentUser.defaultLng);
//        MapUtil.targetMap2LatLng(mBaiduMap, ll);
        updateDistance(ll);
        Log.e(AppConfig.ERR_TAG, "resetPos updateok");
    }

    private void updateDistance(LatLng currentLatLng) {
        double distance = DistanceUtil.getDistance(currentLatLng, targetPoint);
        txtDistance = "距离您:" + (int) distance + "米";
//            TextView tvDistance = new TextView(getApplicationContext());
        TextView tvDistance = (TextView) LayoutInflater.from(LogisticTraceActivity.this).inflate(R.layout.fragment_logistic_trace_distance, null);
        tvDistance.setText(txtDistance);

        mapUtil.updateStatus(currentLatLng, false);
        MarkerOptions overlayOptions = new MarkerOptions().icon(pic).position(currentLatLng).title(txtDistance);
        baiduMap.clear();
        baiduMap.addOverlay(overlayOptions);
//创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
        InfoWindow mInfoWindow = new InfoWindow(tvDistance, currentLatLng, -1 * getResources().getDimensionPixelSize(R.dimen.x32));
//显示InfoWindow
        baiduMap.showInfoWindow(mInfoWindow);
    }

    private boolean useDefaultIcon = true;
    //    private TrackReceiver trackReceiver = null;
    private PowerManager.WakeLock wakeLock = null;

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
            latitude = currentLatLng.latitude;
            longitude = currentLatLng.longitude;

            if (null != mapUtil) {
                updateDistance(currentLatLng);
            }
        }

    };
    private MapUtil mapUtil;
    private String entityName;
    private ImageView imgCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Double lat = getIntent().getDoubleExtra(TAG_LAT, 0);
        Double lng = getIntent().getDoubleExtra(TAG_LNG, 0);
        if (lat <= 0 || lng <= 0) {
            ToastUtil.show(this, "目标定位错误");
            finish();
            return;
        }

        targetPoint = new LatLng(lat, lng);

        setContentView(R.layout.activity_logistic_trace);
        initMapView();
        myApp = (MyApplication) getApplication();

    }


    @Override
    protected void onResume() {
        super.onResume();
        mapUtil.setCenter();

//        resetPos();
        isRealTimeRunning = true;

        //        1 创建骑行线路规划检索实例；
//        mSearch = RoutePlanSearch.newInstance();
//        2 创建骑行线路规划检索监听者；
//        3 设置骑行线路规划检索监听者；
//        mSearch.setOnGetRoutePlanResultListener(listener);
        this.entityName = getIntent().getStringExtra(TAG_ENTITYNAME);
//        this.entityName = "861007030662863";
//        Log.e(AppConfig.ERR_TAG, "entityName:" + this.entityName);
        ToastUtil.show(LogisticTraceActivity.this, "开始查看轨迹");
        realTimeLocRunnable = new RealTimeLocRunnable(2);
        realTimeHandler.post(realTimeLocRunnable);

    }

    @Override
    protected void onPause() {
        super.onPause();
        isRealTimeRunning = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initMapView() {
        BitmapUtil.init();
//        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_logistic_curior);
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.activity_logistic_trace_mapview);
        baiduMap = mMapView.getMap();

//        tvDistance = (TextView) findViewById(R.id.activity_logistic_trace_distance);
        imgCancel = (ImageView) findViewById(R.id.activity_logistic_trace_img_cancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        mBaiduMap = mMapView.getMap();

        mapUtil = MapUtil.getInstance();
        mapUtil.init(mMapView);
        mapUtil.setCenter();
//        resetPos();

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

    /*private void drawLines(HistoryTrackResponse response) {
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


    }*/


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
    }

    public void setUseDefaultIcon(boolean useDefaultIcon) {
        this.useDefaultIcon = useDefaultIcon;
    }

    /*private class MyBikingRouteOverlay extends BikingRouteOverlay {
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


    }*/

    /*private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

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
    }*/

    /**
     * 获取实时轨迹
     */
    class RealTimeLocRunnable implements Runnable {

        private int interval = 1;

        public RealTimeLocRunnable(int interval) {
            this.interval = interval;
        }

        @Override
        public void run() {
            if (isRealTimeRunning) {
                getEntityTrace(entityListener, mTrackListener);
                realTimeHandler.postDelayed(this, interval * 1000);
            }
        }
    }

    /**
     * 获取当前位置
     */
    public void getEntityTrace(OnEntityListener entityListener, OnTrackListener trackListener) {
        // 网络连接正常，开启服务及采集，则查询纠偏后实时位置；否则进行实时定位
        if (NetUtil.isNetworkAvailable(getApplicationContext())) {
            LatestPointRequest request = new LatestPointRequest(AppConfig.TAG_TRACE, AppConfig.SERVICEID, entityName);
            ProcessOption processOption = new ProcessOption();
            processOption.setNeedDenoise(true);
            processOption.setRadiusThreshold(100);
            request.setProcessOption(processOption);
            myApp.mTraceClient.queryLatestPoint(request, trackListener);
        } else {
            Log.e(AppConfig.ERR_TAG, "queryRealTimeLoc.........");
            LocRequest locRequest = new LocRequest(AppConfig.SERVICEID);
            myApp.mTraceClient.queryRealTimeLoc(locRequest, entityListener);
        }
    }

    static class RealTimeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }

    }

    public static Intent getIntent(Context context, String entityName, double lat, double lng) {
        Intent it = new Intent(context, LogisticTraceActivity.class);
        it.putExtra(TAG_ENTITYNAME, entityName);
        it.putExtra(TAG_LAT, lat);
        it.putExtra(TAG_LNG, lng);
        return it;
    }

}
