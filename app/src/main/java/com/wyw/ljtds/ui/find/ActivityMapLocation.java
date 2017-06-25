package com.wyw.ljtds.ui.find;

import android.os.Bundle;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.wyw.ljtds.R;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.ToastUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2017/2/21 0021.
 */

@ContentView(R.layout.activity_map_location)
public class ActivityMapLocation extends BaseActivity {
    @ViewInject(R.id.bmapView)
    private MapView mMapView;

    LocationClient mLocClient;// 定位相关
    public MyLocationListenner myListener = new MyLocationListenner();//监听事件
    BitmapDescriptor mCurrentMarker = null;//标记点mark
    BaiduMap mBaiduMap;
    boolean isFirstLoc = true; // 是否首次定位


    @Event(value = R.id.button1)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:

                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //百度地图SDK初始化
        SDKInitializer.initialize( MyApplication.getAppContext() );
        super.onCreate( savedInstanceState );

        // 地图初始化
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled( true );
        mBaiduMap.setMyLocationConfigeration( new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker ) );
        mBaiduMap.setMapStatus( MapStatusUpdateFactory.newMapStatus( new MapStatus.Builder().zoom( 14 ).build() ) );// 设置级别

        // 定位初始化
        mLocClient = new LocationClient(this );
        mLocClient.registerLocationListener( myListener );//注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode( LocationClientOption.LocationMode.Hight_Accuracy );//设置精准度
        option.setIsNeedAddress( true );//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps( true ); // 打开gps
        option.setPriority( LocationClientOption.GpsFirst ); // 设置GPS优先
        option.setAddrType( "all" );//返回的定位结果包含地址信息
        option.setCoorType( "bd09ll" ); // 设置坐标类型
        option.setScanSpan( 5000 );//设置定时定位的时间间隔。单位毫秒
        option.disableCache( false );//禁止启用缓存定位
        option.setIsNeedLocationDescribe( true );//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIgnoreKillProcess( false );//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException( false );//可选，默认false，设置是否收集CRASH信息，默认收集
        mLocClient.setLocOption( option );
        mLocClient.start();

    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                ToastUtil.show( ActivityMapLocation.this, getResources().getString( R.string.dingwei_error ) );
                return;
            }
//            MyLocationData locData = new MyLocationData.Builder()
//                    .accuracy( location.getRadius() )
//                    // 此处设置开发者获取到的方向信息，顺时针0-360
//                    .direction( 100 ).latitude( location.getLatitude() )
//                    .longitude( location.getLongitude() ).build();
//            mBaiduMap.setMyLocationData( locData );
//
//            StringBuffer sb = new StringBuffer( 256 );
//            sb.append( "Time : " );
//            sb.append( location.getTime() );
//            sb.append( "\nError code : " );
//            sb.append( location.getLocType() );
//            sb.append( "\nLatitude : " );
//            sb.append( location.getLatitude() );
//            sb.append( "\nLontitude : " );
//            sb.append( location.getLongitude() );
//            sb.append( "\nRadius : " );
//            sb.append( location.getRadius() );
//            if (location.getLocType() == BDLocation.TypeGpsLocation) {
//                sb.append( "\nSpeed : " );
//                sb.append( location.getSpeed() );
//                sb.append( "\nSatellite : " );
//                sb.append( location.getSatelliteNumber() );
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
//                sb.append( "\nAddress : " );
//                sb.append( location.getAddrStr() );
//            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy( location.getRadius() )
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction( 100 ).latitude( location.getLatitude() )
                    .longitude( location.getLongitude() ).build();
            mBaiduMap.setMyLocationData( locData );

        }

        public void onReceivePoi(BDLocation poiLocation) {

        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled( false );
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();

    }

}
