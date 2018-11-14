package com.wyw.ljtds.ui.user.address;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IntegerRes;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;

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
import com.wyw.ljtds.model.MyLocation;
import com.wyw.ljtds.model.SingleCurrentUser;
import com.wyw.ljtds.service.LocationService;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.ToolbarManager;
import com.wyw.ljtds.utils.Utils;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by Administrator on 2017/3/9 0009.
 */

public class AddressMapActivity extends Activity {
    public static final String TAG_ADDRESS_POI_LAT = "com.wyw.ljtds.ui.user.address.AdressMapActivity.TAG_ADDRESS_POI_LAT";
    public static final String TAG_ADDRESS_POI_ADDRESS = "com.wyw.ljtds.ui.user.address.AdressMapActivity.TAG_ADDRESS_POI_ADDRESS";
    private static final int REQUEST_SEARCH_ADDRESS = 0;
    public static final String TAG_FROM = "com.wyw.ljtds.ui.user.address.AddressMapActivity.TAG_FROM";
    public static final String TAG_FROM_MAIN = "1";
    private static final int REQUEST_PERMS_LOCATION = 0;

    private int mCurrentDirection = 0;
    private MyLocationData locData;
    MapView mMapView;
    BaiduMap mBaiduMap;
    private MyLocation myLocation; //location到的地址
    private GeoCoder geoCoder;

    private String name = ""; //名称概要
    private String address; //具体地址
    private View.OnClickListener goSearchListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent it = AddressSearchActivity.getIntent(AddressMapActivity.this, name);
            startActivityForResult(it, REQUEST_SEARCH_ADDRESS);
        }
    };

    // UI相关
    OnCheckedChangeListener radioButtonListener;
    private boolean isSmooth;
    private ImageView ivCenterPoint;
    private Button btnConfirm;
    private ImageButton btnLocation;
    private TextView tvAddress;

    double lat = 0d;
    double lng = 0d;
    String addr = "";

    int[] iCenter = new int[2];
    private BDLocationListener locationListner = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                SingleCurrentUser.bdLocation = location;
                myLocation = MyLocation.newInstance(location.getLatitude(), location.getLongitude(), location.getAddrStr());
                targetMyLocation();
                ((MyApplication) AddressMapActivity.this.getApplication()).locationService.unregisterListener(locationListner); //注销掉监听
            }
        }
    };
    private RelativeLayout rlAddressDesc;

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
                        v.setOnClickListener(goSearchListner);
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
                if (myLocation != null) {
                    targetMyLocation();
                    return;
                }
                //check permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    String[] perms = {android.Manifest.permission.ACCESS_FINE_LOCATION};
                    if (!EasyPermissions.hasPermissions(AddressMapActivity.this, perms)) {
                        EasyPermissions.requestPermissions(this, getString(R.string.perm_loc), REQUEST_PERMS_LOCATION, perms);
                    } else {
                        regLocListener();
                    }
                } else {
                    //location
                    regLocListener();
                }

                //back to location
            }
        });


        ivCenterPoint = (ImageView) findViewById(R.id.activity_address_map_centerpoint);
        ivCenterPoint.getLocationInWindow(iCenter);

        rlAddressDesc = (RelativeLayout) findViewById(R.id.activity_address_map_rl_addressdesc);
        rlAddressDesc.setOnClickListener(goSearchListner);
        tvAddress = (TextView) findViewById(R.id.activity_address_map_tv_mylocation);
        btnConfirm = (Button) findViewById(R.id.activity_address_map_btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                if (getString(R.string.searchposition).equals(tvAddress.getText())) {
                    return;
                }

                String tf = getIntent().getStringExtra(TAG_FROM);
                if (TAG_FROM_MAIN.equals(tf)) {
                    SingleCurrentUser.locStyle = SingleCurrentUser.locStyleCustom;
                }

                Utils.log("locationrlt:lng|lat" + lng + "|" + lat + "|" + addr);

                data.putExtra(TAG_ADDRESS_POI_LAT, "|" + lng + "|" + lat);
                data.putExtra(TAG_ADDRESS_POI_ADDRESS, addr);

                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.activity_address_map_mapview);
        mMapView.showScaleControl(false);
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

        if (SingleCurrentUser.bdLocation != null) {
            myLocation = MyLocation.newInstance(SingleCurrentUser.bdLocation.getLatitude(), SingleCurrentUser.bdLocation.getLongitude(), SingleCurrentUser.bdLocation.getAddrStr());
        } else {
            myLocation = MyLocation.newInstance(SingleCurrentUser.defaultLat, SingleCurrentUser.defaultLng, SingleCurrentUser.defaultAddrStr);
        }
        targetMyLocation();

    }

    private void regLocListener() {
        ((MyApplication) AddressMapActivity.this.getApplication()).locationService.registerListener(locationListner);
        ((MyApplication) AddressMapActivity.this.getApplication()).locationService.start();
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

                LatLng srl = searchRlt.location;
                LatLng ll = new LatLng(srl.latitude,
                        srl.longitude);
                targetMap2Center(ll);

                lat = srl.latitude;
                lng = srl.longitude;
                name = searchRlt.name;
                address = searchRlt.address;
                addr = searchRlt.name + "\n" + searchRlt.address;

                tvAddress.setText(addr);

                break;
        }
    }

    /**
     * 移动地图中心
     *
     * @param location
     */
    private void targetMap2Center(LatLng latlng) {

//        mBaiduMap.clear();
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latlng).zoom(AppConfig.MAP_ZOOM);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        //更改文本显示地址
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 500);*/
    }

    /**
     * 更改文本显示地址
     *
     * @param point
     */
    public void updateTvAddress(LatLng point) {
//        LatLng point = mBaiduMap.getMapStatus().target;
        Log.e(AppConfig.ERR_TAG, "updateTvAddress ---" + GsonUtils.Bean2Json(point));
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
        if (myLocation == null) return;
        locData = new MyLocationData.Builder()
//                .accuracy(myLocation.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(mCurrentDirection).latitude(myLocation.getLatitude())
                .longitude(myLocation.getLongitude()).build();

        mBaiduMap.setMyLocationData(locData);
        LatLng ll = new LatLng(myLocation.getLatitude(),
                myLocation.getLongitude());

        targetMap2Center(ll);
        updateTvAddress(ll);

    }

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
            PoiInfo poi = poilist.get(0);

            lng = poi.location.longitude;
            lat = poi.location.latitude;
            name = poi.name;
            address = poi.address;
            addr = poi.name + "\n" + poi.address;

            tvAddress.setText(addr);

        }
    };
}
