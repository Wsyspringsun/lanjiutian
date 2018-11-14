package com.wyw.ljtmgr.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.wyw.ljtmgr.R;
import com.wyw.ljtmgr.biz.UserBiz;
import com.wyw.ljtmgr.config.AppConfig;
import com.wyw.ljtmgr.config.AppManager;
import com.wyw.ljtmgr.utils.CommonUtil;
import com.wyw.ljtmgr.weidget.LoadingDialogUtils;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2017/7/3 0003.
 */

public class BaseActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    //等待动画
    private Dialog mDialog;
    protected boolean loading = false;
    public int curPhase = AppConfig.PHASE_START;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);//xutils初始化视图
        Fresco.initialize(this);
        AppManager.getAppManager().addActivity(this);//activity管理
        curPhase |= AppConfig.PHASE_PERMISSION;

        List<String> permissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isNeedRequestPermissions(permissions)) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), 0);
        }

        if (CommonUtil.isNetworkConn(this)) {
            curPhase |= AppConfig.PHASE_NET_OK;
        } else {
            if ((curPhase & AppConfig.PHASE_NET_OK) == AppConfig.PHASE_NET_OK)
                curPhase ^= AppConfig.PHASE_NET_OK;
        }

    }

    protected boolean isNeedRequestPermissions(List<String> permissions) {
        // 定位精确位置
        addPermission(permissions, android.Manifest.permission.ACCESS_FINE_LOCATION);
        // 存储权限
        addPermission(permissions, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        addPermission(permissions, Manifest.permission.CAMERA);
        // 读取手机状态
        addPermission(permissions, android.Manifest.permission.READ_PHONE_STATE);
        return permissions.size() > 0;
    }

    protected void addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.e(AppConfig.TAG_ERR, "onStart .." + this.getLocalClassName());

        if (UserBiz.isLogined()) {
            curPhase |= AppConfig.PHASE_LOGIN_IN;
        } else {
            if ((curPhase & AppConfig.PHASE_LOGIN_IN) == AppConfig.PHASE_LOGIN_IN)
                curPhase ^= AppConfig.PHASE_LOGIN_IN;

            //to login activity
            if (!(this instanceof ActivityLogin)) {
                Intent it = ActivityLogin.getIntent(this);
                startActivity(it);
            }
        }
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
        if (loading) return;
        loading = true;
        mDialog = LoadingDialogUtils.createLoadingDialog(this, "加载中...");
    }

    public void closeLoding() {
        if (!loading) return;
        loading = false;
        LoadingDialogUtils.closeDialog(mDialog);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        curPhase |= AppConfig.PHASE_PERMISSION;
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if ((curPhase & AppConfig.PHASE_PERMISSION) == AppConfig.PHASE_PERMISSION)
            curPhase ^= AppConfig.PHASE_PERMISSION;
    }

    View.OnClickListener toolbarListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if ("previous".equals(v.getTag())) {
                BaseActivity.this.finish();
            }
        }
    };

    /**
     * if use toolar_common init it's click event listener
     */
    protected void initToolbar() {
        TextView title = (TextView) findViewById(R.id.fragment_toolbar_common_title);
        title.setText(getTitle());
        LinearLayout llLeft = (LinearLayout) findViewById(R.id.fragment_toolbar_common_left);
//        LinearLayout llRight = (LinearLayout) findViewById(R.id.fragment_toolbar_common_right);
        for (int i = 0; i < llLeft.getChildCount(); i++) {
            ImageView imgItem = (ImageView) llLeft.getChildAt(i);
            imgItem.setOnClickListener(toolbarListener);
        }

    }

}
