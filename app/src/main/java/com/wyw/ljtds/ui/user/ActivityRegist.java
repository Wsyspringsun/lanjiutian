package com.wyw.ljtds.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyw.ljtds.MainActivity;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.ui.TestActivity;
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
                if (StringUtils.isEmpty(ed_phone.getText().toString().trim()) || !Utils.validPhoneNum(ed_phone.getText().toString().trim())) {
                    ToastUtil.show(this, getResources().getString(R.string.phone_error));
                    return;
                }

                Intent in = new Intent(this, TestActivity.class);
                in.putExtra(AppConfig.IntentExtraKey.PHONE_NUMBER, ed_phone.getText().toString().trim());
                startActivity(in);
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


}
