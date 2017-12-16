package com.wyw.ljtwl.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.wyw.ljtwl.config.AppConfig;
import com.wyw.ljtwl.config.AppManager;
import com.wyw.ljtwl.weidget.LoadingDialogUtils;

import org.xutils.x;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2017/7/3 0003.
 */

public class BaseActivitry extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    //等待动画
    private Dialog mDialog;
    protected boolean loading = false;

    private Handler mHandler = new Handler() {
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);//xutils初始化视图
        Fresco.initialize(this);
        AppManager.getAppManager().addActivity(this);//activity管理
    }

    @Override
    protected void onStop() {
        super.onStop();
        closeLoding();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeLoding();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeLoding();
    }

    public void setLoding() {
        loading = true;
        mDialog = LoadingDialogUtils.createLoadingDialog(this, "加载中...");
    }

    public void closeLoding() {
        loading = false;
        LoadingDialogUtils.closeDialog(mDialog);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

}
