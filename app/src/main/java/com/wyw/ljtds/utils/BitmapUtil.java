package com.wyw.ljtds.utils;

import android.content.Context;
import android.content.res.Resources;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.wyw.ljtds.R;

/**
 * Created by baidu on 17/3/8.
 */

public class BitmapUtil {

    public static BitmapDescriptor bmArrowPoint = null;

    public static BitmapDescriptor bmStart = null;

    public static BitmapDescriptor bmEnd = null;

    public static BitmapDescriptor bmGeo = null;

    public static BitmapDescriptor bmGcoding = null;

    /**
     * 创建bitmap，在MainActivity onCreate()中调用
     */
    public static void init() {
        bmArrowPoint = BitmapDescriptorFactory.fromResource(R.drawable.ic_guiji);
        bmStart = BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
        bmEnd = BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
        bmGeo = BitmapDescriptorFactory.fromResource(R.drawable.ic_location_my);
        bmGcoding = BitmapDescriptorFactory.fromResource(R.mipmap.bga_pp_ic_arrow_down);
    }

    /**
     * 回收bitmap，在MainActivity onDestroy()中调用
     */
    public static void clear() {
        bmArrowPoint.recycle();
        bmStart.recycle();
        bmEnd.recycle();
        bmGeo.recycle();
    }

    public static BitmapDescriptor getMark(Context context, int index) {
        Resources res = context.getResources();
        int resourceId;
        if (index <= 10) {
            resourceId = res.getIdentifier("icon_mark" + index, "mipmap", context.getPackageName());
        } else {
            resourceId = res.getIdentifier("icon_markx", "mipmap", context.getPackageName());
        }
        return BitmapDescriptorFactory.fromResource(resourceId);
    }
}
