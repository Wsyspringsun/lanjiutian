package com.wyw.ljtds.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.widget.dialog.LoadingDialogUtils;

/**
 * Created by wsy on 17-7-10.
 */

public class LoadingDialog {
    //等待动画
    private static Dialog mDialog;
    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case AppConfig.IntentExtraKey.LODING_CONTEXT:
                    LoadingDialogUtils.closeDialog(mDialog);
                    break;
            }
        }
    };

    public static void setLoding(Context context, boolean settime) {
        if (settime) {
            mDialog = LoadingDialogUtils.createLoadingDialog(context, "加载中...");
            mHandler.sendEmptyMessageDelayed(AppConfig.IntentExtraKey.LODING_CONTEXT, 1500);
        } else {
        }
    }

    public static void closeLoding() {
        LoadingDialogUtils.closeDialog(mDialog);
    }

}
