package com.wyw.ljtmgr.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.wyw.ljtmgr.R;
import com.wyw.ljtmgr.service.BackgroundService;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;


/**
 * 第三方物流
 */
@ContentView(R.layout.activity_logistic_info)
public class LogisticInfoActivity extends BaseActivity {
    private static final int REQUEST_QRCODE = 1;
    public static final String TAG_NAME = "com.wyw.ljtwl.ui.LogisticInfoActivity.TAG_NAME" ;
    public static final String TAG_CODE = "com.wyw.ljtwl.ui.LogisticInfoActivity.TAG_CODE" ;
    @ViewInject(R.id.activity_logistic_info_name)
    EditText etName;
    @ViewInject(R.id.activity_logistic_info_code)
    EditText etCode;
    @ViewInject(R.id.activity_logistic_info_scan)
    ImageView imgScan;
    @ViewInject(R.id.activity_logistic_btn_submit)
    Button btnSubmit;

    View.OnClickListener clickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.activity_logistic_info_scan:
                    Intent it = ActivityScan.getIntent(LogisticInfoActivity.this);
                    startActivityForResult(it, REQUEST_QRCODE);
                    break;
                case R.id.activity_logistic_btn_submit:
                    Intent itData = new Intent();
                    itData.putExtra(TAG_NAME,etName.getText().toString());
                    itData.putExtra(TAG_CODE,etCode.getText().toString());
                    setResult(Activity.RESULT_OK,itData);
                    finish();
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BackgroundService.setServiceStat(this, true);

        initView();
    }

    private void initView() {
        initToolbar();

        imgScan.setOnClickListener(clickListner);
        btnSubmit.setOnClickListener(clickListner);

    }

    @Override
    protected void onStart() {
        super.onStart();

        bindData2View();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK != resultCode)
            return;

        switch (requestCode) {
            case REQUEST_QRCODE:
                String code = data.getStringExtra(ActivityScan.TAG_QRCODE);
                etCode.setText(code);
                break;
        }

    }

    private void bindData2View() {
    }

    public static Intent getIntent(Activity context, String orderId) {
        Intent it = new Intent(context, LogisticInfoActivity.class);
        return it;
    }
}
