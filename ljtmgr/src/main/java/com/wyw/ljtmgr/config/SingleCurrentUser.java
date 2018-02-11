package com.wyw.ljtwl.config;

import com.baidu.location.BDLocation;

/**
 * Created by wsy on 17-12-21.
 */

public class SingleCurrentUser {
    public static BDLocation location;
    public static final float defaultLat = 35.504755f;
    public static final float defaultLng = 112.839483f;

    static {
        //det location is wenchang
        location = new BDLocation();
        location.setLatitude(defaultLat);
        location.setLongitude(defaultLng);
    }

}
