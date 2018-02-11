package com.wyw.ljtds.ui.user.address;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.model.AreaModel;
import com.wyw.ljtds.model.SingleCurrentUser;
import com.wyw.ljtds.service.LocationService;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.ToolbarManager;

import java.util.List;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

public class AddressMapActivity extends Activity {
    public static final String TAG_ADDRESS_POI_LAT = "com.wyw.ljtds.ui.user.address.AdressMapActivity.TAG_ADDRESS_POI_LAT";
    public static final String TAG_ADDRESS_POI_ADDRESS = "com.wyw.ljtds.ui.user.address.AdressMapActivity.TAG_ADDRESS_POI_ADDRESS";
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;
    private static final int REQUEST_SEARCH_ADDRESS = 0;
    public static final String TAG_FROM = "com.wyw.ljtds.ui.user.address.AddressMapActivity.TAG_FROM";
    public static final String TAG_FROM_MAIN = "1";
    public static final String TAG_FROM_ADDR = "2";

    private int mCurrentDirection = 0;
    private LocationMode mCurrentMode;
    private MyLocationData locData;
    MapView mMapView;
    BaiduMap mBaiduMap;
    private BDLocation myLocation;
    private GeoCoder geoCoder;

    // UI相关
    OnCheckedChangeListener radioButtonListener;
    boolean isFirstLoc = true; // 是否首次定位
    private boolean isSmooth;

    private Button btnConfirm;
    private ImageButton btnLocation;
    private TextView tvAddress;
    private BDLocationListener locationListner = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                myLocation = location;
                targetMyLocation();
                ((MyApplication) AddressMapActivity.this.getApplication()).locationService.unregisterListener(locationListner); //注销掉监听
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        R.id.activity_address_map_mapview
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_address_map);
        //init top toolbar
        ToolbarManager.initialToolbar(this, new ToolbarManager.IconBtnManager() {
            @Override
            public void initIconBtn(View v, int position) {
                switch (position) {
                    case 4:
                        //search address
                        v.setVisibility(View.VISIBLE);
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent it = AddressSearchActivity.getIntent(AddressMapActivity.this);
                                startActivityForResult(it, REQUEST_SEARCH_ADDRESS);
                            }
                        });
                        //go to map search
                        break;
                }
            }
        });
        //location
        btnLocation = (ImageButton) findViewById(R.id.activity_address_map_btn_location);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //back to location
                targetMyLocation();
            }
        });
        tvAddress = (TextView) findViewById(R.id.activity_address_map_tv_mylocation);
        btnConfirm = (Button) findViewById(R.id.activity_address_map_btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();

                if (poi == null) {
                    ToastUtil.show(AddressMapActivity.this, "没有选择地址");
                    return;
                }
                String tf = getIntent().getStringExtra(TAG_FROM);
                if (TAG_FROM_MAIN.equals(tf)) {
                    SingleCurrentUser.locStyle = SingleCurrentUser.locStyleCustom;
                }
                data.putExtra(TAG_ADDRESS_POI_LAT, "|" + poi.location.latitude + "|" + poi.location.longitude);
                data.putExtra(TAG_ADDRESS_POI_ADDRESS, tvAddress.getText().toString());
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.activity_address_map_mapview);
        mBaiduMap = mMapView.getMap();
        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(geoListener);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
                if (1 == i) {
                    //移动
                    isSmooth = true;
                } else {
                }
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                if (isSmooth) {
                    isSmooth = false;
                    //search position to address info
                    updateTvAddress(mapStatus.target);
//                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
                }
            }
        });
        ((MyApplication) AddressMapActivity.this.getApplication()).locationService.registerListener(locationListner);
    }

    /**
     * 返回结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(AppConfig.ERR_TAG, "onActivityResult");
        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case REQUEST_SEARCH_ADDRESS:
                //返回搜索地址结果
//                Log.e(AppConfig.ERR_TAG, "onActivityResult  ...REQUEST_SEARCH_ADDRESS");
                String searchRltJson = data.getStringExtra(AddressSearchActivity.TAG_SELECT_ADDR);
                PoiInfo searchRlt = GsonUtils.Json2Bean(searchRltJson, PoiInfo.class);

                //移动地图中心
                targetMap2Center(searchRlt.location);

                break;
        }
    }

    /**
     * 移动地图中心
     *
     * @param location
     */
    private void targetMap2Center(LatLng lat) {
//        Log.e(AppConfig.ERR_TAG, "onActivityResult  ...REQUEST_SEARCH_ADDRESS targetMap2Center");
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(lat).zoom(AppConfig.MAP_ZOOM);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        //更改文本显示地址
        updateTvAddress(lat);
    }

    /**
     * 更改文本显示地址
     *
     * @param point
     */
    public void updateTvAddress(LatLng point) {
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .location(point));
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
    protected void onStop() {
        //取消注册传感器监听
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        geoCoder.destroy();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    public static Intent getIntent(Context ctx) {
        Intent it = new Intent(ctx, AddressMapActivity.class);
        return it;
    }

    private void targetMyLocation() {
        if (SingleCurrentUser.location == null) return;
        locData = new MyLocationData.Builder()
                .accuracy(myLocation.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(mCurrentDirection).latitude(SingleCurrentUser.location.getLatitude())
                .longitude(SingleCurrentUser.location.getLongitude()).build();

        mBaiduMap.setMyLocationData(locData);
        LatLng ll = new LatLng(myLocation.getLatitude(),
                myLocation.getLongitude());

        targetMap2Center(ll);

    }

    private PoiInfo poi;
    OnGetGeoCoderResultListener geoListener = new OnGetGeoCoderResultListener() {

        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            if (reverseGeoCodeResult == null) {
                tvAddress.setText(getString(R.string.searchposition));
                return;
            }
            List<PoiInfo> poilist = reverseGeoCodeResult.getPoiList();
            if (poilist == null || poilist.size() <= 0) {
                tvAddress.setText(getString(R.string.searchposition));
                return;
            }
            poi = poilist.get(0);
            tvAddress.setText(poi.name + "\n" + poi.address);
        }
    };
}
