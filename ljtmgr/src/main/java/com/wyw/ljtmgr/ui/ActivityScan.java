package com.wyw.ljtwl.ui;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wyw.ljtwl.R;
import com.wyw.ljtwl.config.AppConfig;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by wsy on 2018/1/17 0947
 */

@ContentView(R.layout.activity_logistic_scan)
public class ActivityScan extends BaseActivity implements QRCodeView.Delegate {

    public static final String TAG_QRCODE = "com.wyw.ljtwl.ui.ActivityScan.TAG_QRCODE";
    @ViewInject(R.id.activity_logistic_scan_zxingview)
    private QRCodeView mQRCodeView;
    @ViewInject(R.id.activity_logistic_scan_lights)
    private ImageView lights;
    @ViewInject(R.id.header_title)
    private TextView title;

    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;

    private int index = 0;

    @Event(value = {R.id.activity_logistic_scan_photo, R.id.activity_logistic_scan_lights, R.id.header_return})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_logistic_scan_lights:
                if (index == 0) {
                    mQRCodeView.openFlashlight();
                    lights.setImageDrawable(getResources().getDrawable(R.drawable.dengguang_kai));
                    index = 1;
                } else {
                    mQRCodeView.closeFlashlight();
                    lights.setImageDrawable(getResources().getDrawable(R.drawable.dengguang_guan));
                    index = 0;
                }
                break;

            case R.id.activity_logistic_scan_photo:
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

        mQRCodeView.setDelegate(this);
    }



    @Override
    protected void onStart() {
        super.onStart();
        List<String> permissions = new ArrayList<>();
        addPermission(permissions, Manifest.permission.CAMERA);
        addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isNeedRequestPermissions(permissions)) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), 0);
        }

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

    @Override
    //</editor-fold>
    public void onScanQRCodeSuccess(final String result) {
        Intent it = new Intent();
        it.putExtra(ActivityScan.TAG_QRCODE, result);
        setResult(Activity.RESULT_OK, it);
        finish();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Toast.makeText(this, "打开相机出错", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(ActivityScan.this, "未发现二维码", Toast.LENGTH_LONG).show();
                }
            }.execute();
        }
    }


    public static Intent getIntent(LogisticInfoActivity context) {
        Intent it = new Intent(context, ActivityScan.class);
        return it;
    }
}
