package com.wyw.ljtds.model;

import android.app.Activity;
import android.graphics.Bitmap;

import com.baidu.location.BDLocation;
import com.wyw.ljtds.utils.FileUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.Utils;

import java.io.File;

/**
 * Created by wsy on 17-12-21.
 */

public class SingleCurrentUser {



    public static UserModel userInfo;
    public static MyLocation location;
    public static BDLocation bdLocation;
    public static final double defaultLat = 35.48991025753593f;
    public static final double defaultLng = 112.86508950898732;
    public static final String defaultAddrStr = "晋城市人民医院";
    public static AddressModel defaltAddr;

    //标识获取未知方式
    public static int locStyle = 0;
    public static int locStyleLocation = 0; //通过Location
    public static int locStyleCustom = 1;//通过用户选择

    public static void updateLocation(MyLocation loc) {
//        Utils.log(loc.getLatitude() + "|" + loc.getLongitude() + "|" + loc.getAddrStr());
        if (SingleCurrentUser.location == null) {
            SingleCurrentUser.location = MyLocation.newInstance(loc.getLatitude(), loc.getLongitude(), loc.getAddrStr());
        }
        SingleCurrentUser.location.setADDRESS_ID(loc.getADDRESS_ID());
        SingleCurrentUser.location.setAddrStr(loc.getAddrStr());
        SingleCurrentUser.location.setLatitude(loc.getLatitude());
        SingleCurrentUser.location.setLongitude(loc.getLongitude());
    }

    public static void updateLocation(double lat, double lng, String addr) {
        MyLocation loc = MyLocation.newInstance(lat, lng, addr);
        updateLocation(loc);
//        Utils.log(location.getLatitude() + "|" + location.getLongitude() + "|" + location.getAddrStr());
    }
}
