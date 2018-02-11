package com.wyw.ljtds.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.WindowManager;

import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.MyApplication;

/**
 * Created by baidu on 17/3/21.
 */

public class NetUtil {

    /**
     * 检测网络状态是否联通
     *
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (null != info && info.isConnected() && info.isAvailable()) {
                return true;
            }
        } catch (Exception e) {
            Log.e(AppConfig.ERR_TAG, "current network is not available");
            return false;
        }
        return false;
    }

    public static void showNetSettingDialog(Activity parent) {
        Log.e(AppConfig.ERR_TAG, "isNetworkAvailable no");
        AlertDialog alert = new AlertDialog.Builder(parent).create();
//        alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);//使用ApplicationContext 必须更改type
        alert.setTitle(R.string.alert_tishi);
        alert.setMessage(parent.getResources().getString(R.string.confirm_order_submit));
        alert.setButton(DialogInterface.BUTTON_POSITIVE, parent.getResources().getString(R.string.alert_queding), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.e(AppConfig.ERR_TAG, "isNetworkAvailable do confirm");
                //跳转到系统网络设置的界面
                Intent intent = new Intent();
                intent.setClassName("com.android.settings", "com.android.settings.Settings");
                MyApplication.getAppContext().startActivity(intent);
            }
        });
        alert.show();
        return;

    }
}
