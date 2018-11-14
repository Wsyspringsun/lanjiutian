package com.wyw.ljtds.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2016/12/20 0020.
 */

@ContentView(R.layout.activity_regist)
public class ActivityRegist extends BaseActivity {
    private static final int REQUEST_REGISTED = 0;
    private static final String DIALOG_EXIST_REG = "DIALOG_EXIST_REG";
    @ViewInject(R.id.header_title)
    private TextView title;
    @ViewInject(R.id.header_return_text)
    private TextView return_tv;
    @ViewInject(R.id.ed_phone)
    private EditText ed_phone;


    @Event(value = {R.id.next, R.id.header_return})
    private void onclick(View v) {
        switch (v.getId()) {
            case R.id.next:
                if (StringUtils.isEmpty(ed_phone.getText().toString().trim())) {
                    ToastUtil.show(this, getResources().getString(R.string.phone_error));
                    return;
                }
                getCode(ed_phone.getText().toString().trim());
                break;

            case R.id.header_return:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText("手机注册");
        return_tv.setText("账号登陆");

    }

    private void getCode(final String phone) {
        setLoding(this, false);
        new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                return UserBiz.VerificationCode(phone, "0");
            }

            @Override
            protected void onExecuteSucceeded(String s) {
                closeLoding();
//                Log.e(AppConfig.ERR_TAG, "getcode s:" + s);
                Intent in = new Intent(ActivityRegist.this, ActivityRegist1.class);
                in.putExtra(AppConfig.IntentExtraKey.PHONE_NUMBER, ed_phone.getText().toString().trim());
                startActivity(in);
            }

            @Override
            protected void OnExecuteFailed() {
                Log.e(AppConfig.ERR_TAG, "getcode OnExecuteFailed");

                ExistUserPhoneDialogFragment frag = ExistUserPhoneDialogFragment.newInstance(ed_phone.getText().toString().trim());
//                frag.setTargetFragment(ActivityRegist.this, REQUEST_REGISTED);
                frag.show(ActivityRegist.this.getSupportFragmentManager(), DIALOG_EXIST_REG);
                closeLoding();
            }
        }.execute();
    }

    public static Intent getIntent(Context ctx) {
        Intent it = new Intent(ctx, ActivityRegist.class);
        it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return it;
    }

}
