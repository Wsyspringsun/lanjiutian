package com.wyw.ljtds.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.wyw.ljtds.MainActivity;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.AppManager;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.goods.ActivityMedicinesInfo;
import com.wyw.ljtds.ui.user.manage.ActivityAmendPassword1;
import com.wyw.ljtds.utils.InputMethodUtils;
import com.wyw.ljtds.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.xiaoneng.uiapi.Ntalker;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

@ContentView(R.layout.activity_login)
public class ActivityLogin extends BaseActivity {
    @ViewInject(R.id.guanbi)
    private ImageView close;
    @ViewInject(R.id.zhuce)
    private TextView regist;
    @ViewInject(R.id.ed_phone)
    private EditText ed_phone;
    @ViewInject(R.id.ed_pass)
    private EditText ed_pass;

    UserBiz userBiz;

    @Event(value = {R.id.fragment_login_styles_wechat, R.id.fragment_login_styles_yanzheng, R.id.guanbi, R.id.zhuce, R.id.next, R.id.chongzhi})
    private void onclick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.guanbi:
                InputMethodUtils.closeSoftKeyboard(this);

                if (AppConfig.currSel == 3 || AppConfig.currSel == 4) {
                    AppConfig.currSel = AppConfig.DEFAULT_INDEX_FRAGMENT;
                }
//                it = new Intent(ActivityLogin.this, MainActivity.class);
//                startActivity(it);

                finish();
                break;

            case R.id.zhuce:
                it = ActivityRegist.getIntent(this);
                startActivity(it);
//                finish();
                break;

            case R.id.next:
                doLogin();
                break;

            case R.id.chongzhi:
                finish();
                startActivity(new Intent(this, ActivityAmendPassword1.class));
                break;
            case R.id.fragment_login_styles_yanzheng:
                startActivity(ActivityLoginOfValidCode.getIntent(this));
                finish();
                break;
            case R.id.fragment_login_styles_wechat:
                // send oauth request
                IWXAPI wxApi = ((MyApplication) getApplication()).wxApi;
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = System.currentTimeMillis() + "";
                wxApi.sendReq(req);
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userBiz = new UserBiz(this);

        //隐藏自己的登录方式---帐号密码登录
        findViewById(R.id.fragment_login_styles_username).setVisibility(View.GONE);

        ed_phone.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        ed_pass.setImeOptions(EditorInfo.IME_ACTION_DONE);

        ed_phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    ed_pass.requestFocus();
                }
                return false;
            }
        });
        ed_pass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    InputMethodUtils.closeSoftKeyboard(ActivityLogin.this);
                }
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (UserBiz.isLogined()) {
            finish();
        }
    }

    BizDataAsyncTask<String> loginTask;

    private void doLogin() {
        setLoding(this, false);
        loginTask = new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                return userBiz.login(ed_phone.getText().toString().trim(),
                        ed_pass.getText().toString().trim());
            }

            @Override
            protected void onExecuteSucceeded(String result) {
                closeLoding();
                PreferenceCache.putToken(result); // 持久化缓存token
                PreferenceCache.putAutoLogin(true);// 记录是否自动登录
                PreferenceCache.putUsername(ed_phone.getText().toString().trim());
                if (PreferenceCache.isAutoLogin()) {
                    PreferenceCache.putPhoneNum(ed_phone.getText().toString().trim());
                }
                Ntalker.getBaseInstance().login(((MyApplication) getApplication()).entityName, ed_phone.getText().toString(), 0);
                finish();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        };

        loginTask.execute();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            InputMethodUtils.keyBoxIsShow(this);

//            Intent it = new Intent(this, MainActivity.class);
//            startActivity(it);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }



    public static Intent getIntent(Context context) {
        Intent it = new Intent(context, ActivityLogin.class);
        return it;
    }
}
