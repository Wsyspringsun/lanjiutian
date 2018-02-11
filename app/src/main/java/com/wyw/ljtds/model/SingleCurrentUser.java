package com.wyw.ljtds.model;

import com.baidu.location.BDLocation;
import com.wyw.ljtds.utils.Utils;

/**
 * Created by wsy on 17-12-21.
 */

public class SingleCurrentUser {
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
        Utils.log(loc.getLatitude() + "|" + loc.getLongitude() + "|" + loc.getAddrStr());
        SingleCurrentUser.location = loc;
    }

    public static void updateLocation(double lat, double lng, String addr) {
        MyLocation location = MyLocation.newInstance(lat, lng, addr);
        location.setAddrStr(addr);
        location.setLongitude(lng);
        location.setLatitude(lat);
        SingleCurrentUser.location = location;
        Utils.log(location.getLatitude() + "|" + location.getLongitude() + "|" + location.getAddrStr());
    }
}
