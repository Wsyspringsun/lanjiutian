package com.wyw.ljtds.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkDetector {

    /**
     * 设备网络是否处于可用状态
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
//                ToastUtil.showMessage(context, "无法连接网络");
                return false;
            }

            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 判断当前网络是否已经连接
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
//        ToastUtil.showMessage(context, "无法连接网络");
        return false;
    }

    /**
     * 设备wifi网络是否处于可用状态
     *
     * @param context
     * @return
     */
    public static boolean isWifiAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }

        NetworkInfo networkInfo = manager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return !(networkInfo == null || !networkInfo.isAvailable());

    }

    /**
     * 设备数据网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isMobileAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }

        NetworkInfo networkInfo = manager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return !(networkInfo == null || !networkInfo.isAvailable());

    }

    /**
     * 取得当前网络连接类型
     *
     * @param context
     * @return
     */
    public static int getConnectionType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return -1;
        }

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo == null || networkInfo.isAvailable()) {
            return networkInfo.getType();
        }

        return -1;
    }
}
