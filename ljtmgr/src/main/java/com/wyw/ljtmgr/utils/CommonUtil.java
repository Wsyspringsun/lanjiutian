package com.wyw.ljtmgr.utils;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.wyw.ljtmgr.config.AppConfig;
import com.wyw.ljtmgr.ui.BaseActivity;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by baidu on 17/1/23.
 */

public class CommonUtil {

    private static DecimalFormat df = new DecimalFormat("######0.00");

    public static final double DISTANCE = 0.0001;

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }

    /**
     * 获取当前时间戳(单位：秒)
     *
     * @return
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 校验double数值是否为0
     *
     * @param value
     * @return
     */
    public static boolean isEqualToZero(double value) {
        return Math.abs(value - 0.0) < 0.01 ? true : false;
    }

    /**
     * 经纬度是否为(0,0)点
     *
     * @return
     */
    public static boolean isZeroPoint(double latitude, double longitude) {
        return isEqualToZero(latitude) && isEqualToZero(longitude);
    }

    /**
     * 将字符串转为时间戳
     */
    public static long toTimeStamp(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.CHINA);
        Date date;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        return date.getTime() / 1000;
    }

    /**
     * 获取时分秒
     *
     * @param timestamp 时间戳（单位：毫秒）
     * @return
     */
    public static String getHMS(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            return sdf.format(new Timestamp(timestamp));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(timestamp);
    }

    /**
     * 获取年月日 时分秒
     *
     * @param timestamp 时间戳（单位：毫秒）
     * @return
     */
    public static String formatTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.format(new Timestamp(timestamp));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(timestamp);
    }

    /**
     * 计算x方向每次移动的距离
     */
    public static double getXMoveDistance(double slope) {
        if (slope == Double.MAX_VALUE) {
            return DISTANCE;
        }
        return Math.abs((DISTANCE * slope) / Math.sqrt(1 + slope * slope));
    }

    /**
     * 根据点和斜率算取截距
     */
    public static double getInterception(double slope, LatLng point) {
        return point.latitude - slope * point.longitude;
    }

    /**
     * 算斜率
     */
    public static double getSlope(LatLng fromPoint, LatLng toPoint) {
        if (toPoint.longitude == fromPoint.longitude) {
            return Double.MAX_VALUE;
        }
        return (toPoint.latitude - fromPoint.latitude) / (toPoint.longitude - fromPoint.longitude);
    }

    /**
     * 根据两点算取图标转的角度
     */
    public static double getAngle(LatLng fromPoint, LatLng toPoint) {
        double slope = getSlope(fromPoint, toPoint);
        if (slope == Double.MAX_VALUE) {
            if (toPoint.latitude > fromPoint.latitude) {
                return 0;
            } else {
                return 180;
            }
        }
        float deltAngle = 0;
        if ((toPoint.latitude - fromPoint.latitude) * slope < 0) {
            deltAngle = 180;
        }
        double radio = Math.atan(slope);
        return 180 * (radio / Math.PI) + deltAngle - 90;
    }

    public static boolean isNetworkConn(BaseActivity context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() == null) return false;
        return cm.getActiveNetworkInfo().isConnected();
    }

    public static void log(String content) {
//        if (!AppConfig.test) return;
        StackTraceElement[] trace = new Throwable().getStackTrace();
        if (trace != null && trace.length > 1) {
            int segmentSize = 3 * 1024;
            long length = content.length();
            if (length <= segmentSize) {// 长度小于等于限制直接打印
                Log.e(AppConfig.TAG_ERR, trace[1].getClassName() + "#" + trace[1].getMethodName() + " > " + trace[1].getLineNumber() + " :" + content);
            } else {
                Log.e(AppConfig.TAG_ERR, trace[1].getClassName() + "#" + trace[1].getMethodName() + " > " + trace[1].getLineNumber() + " :");
                while (content.length() > segmentSize) {// 循环分段打印日志
                    String logContent = content.substring(0, segmentSize);
                    content = content.replace(logContent, "");
                    Log.e(AppConfig.TAG_ERR, "> " + logContent);
                }
                Log.e(AppConfig.TAG_ERR, " > " + content);
            }
        }
    }

    private static final int REQUEST_CODE_PERMISSION_CALL_PHONE = 1;
    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_CALL_PHONE)
    public static void call(final Context context, final String phone) {
        if (EasyPermissions.hasPermissions(context, Manifest.permission.CALL_PHONE)) {
            Log.e(AppConfig.TAG_ERR, "是否有权限" + EasyPermissions.hasPermissions(context, Manifest.permission.CALL_PHONE) + "");
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("是否拨打电话：" + phone);
            builder.setNegativeButton("取消", null);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + phone));
                    //开启系统拨号器
                    context.startActivity(intent);
                }
            });
            builder.create().show();
        } else {
            EasyPermissions.requestPermissions(context , "需要以下权限:拨打电话。", REQUEST_CODE_PERMISSION_CALL_PHONE, Manifest.permission.CALL_PHONE);
        }
    }
}
