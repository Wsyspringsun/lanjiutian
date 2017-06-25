package com.wyw.ljtds.ui.category;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.goods.ActivityMedicineList;
import com.wyw.ljtds.ui.goods.ActivityMedicinesInfo;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2017/2/13 0013.
 */

@ContentView(R.layout.activity_search_scan)
public class ActivityScan extends BaseActivity implements QRCodeView.Delegate, EasyPermissions.PermissionCallbacks {
    @ViewInject(R.id.zxingview)
    private QRCodeView mQRCodeView;
    @ViewInject(R.id.lights)
    private ImageView lights;
    @ViewInject(R.id.header_title)
    private TextView title;

    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;

    private int index = 0;

    @Event(value = {R.id.photo, R.id.lights, R.id.header_return})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.lights:
                if (index == 0) {
                    mQRCodeView.openFlashlight();
                    lights.setImageDrawable(getResources().getDrawable(R.mipmap.dengguang_kai));
                    index = 1;
                } else {
                    mQRCodeView.closeFlashlight();
                    lights.setImageDrawable(getResources().getDrawable(R.mipmap.dengguang_guan));
                    index = 0;
                }
                break;

            case R.id.photo:
                startActivityForResult(BGAPhotoPickerActivity.newIntent(this, null, 1, null, false), REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
                break;

            case R.id.header_return:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText(R.string.saoma);

        requestCodeQRCodePermissions();

        requestCodeQRCodePermissions();

        mQRCodeView.setDelegate(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
//        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);

        mQRCodeView.showScanRect();
        mQRCodeView.startSpot();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    //调去振动
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    //<editor-fold desc="Description">
    BizDataAsyncTask<String> goodsTask;
    @Override
    //</editor-fold>
    public void onScanQRCodeSuccess(final String result) {
        Log.e("error:",result);
//        ToastUtil.show(this, result);
        goodsTask = new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                String id = GoodsBiz.getGoodsIdByBarcode(result);
                Log.e("ljt","aaaa");
                return id;
            }

            @Override
            protected void onExecuteSucceeded(String s) {
                Intent i = new Intent(ActivityScan.this, ActivityMedicinesInfo.class);
                i.putExtra(AppConfig.IntentExtraKey.MEDICINE_INFO_ID, s);
                finish();
                startActivity(i);
            }

            @Override
            protected void OnExecuteFailed() {
            }
        };
        goodsTask.execute();

        vibrate();
//        mQRCodeView.startCamera();
        mQRCodeView.startSpot();
//        mQRCodeView.showScanRect();
//        if (StringUtils.isEmpty(result)) return;
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e("scan_earry", "打开相机出错");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mQRCodeView.showScanRect();

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            final String picturePath = BGAPhotoPickerActivity.getSelectedImages(data).get(0);

            /*
            这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
            请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github.com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
             */
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    return QRCodeDecoder.syncDecodeQRCode(picturePath);
                }

                @Override
                protected void onPostExecute(String result) {
                    if (TextUtils.isEmpty(result)) {
                        ToastUtil.show(ActivityScan.this, "未发现二维码");
                    } else {
                        Log.e("*****************", result);
                    }
                }
            }.execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }
}
