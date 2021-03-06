package com.wyw.ljtds.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.user.manage.ActivityAmendPassword1;
import com.wyw.ljtds.utils.InputMethodUtils;
import com.wyw.ljtds.utils.ToastUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.xiaoneng.uiapi.Ntalker;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

@ContentView(R.layout.activity_login_validcode)
public class ActivityLoginOfValidCode extends BaseActivity {
    @ViewInject(R.id.activity_login_validcode_guanbi)
    private ImageView close;
    @ViewInject(R.id.activity_login_validcode_ed_phone)
    private EditText ed_phone;
    @ViewInject(R.id.activity_login_validcode_ed_pass)
    private EditText ed_pass;
    @ViewInject(R.id.activity_login_validcode_getcode)
    private TextView tvGetCode;

    UserBiz userBiz;

    //    倒计时
    private CountDownTimer timer = new CountDownTimer(AppConfig.PHONE_VALIDCODE_TEIMER * 1000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            tvGetCode.setText(getString(R.string.yanzhengma_huoqu_timer, (millisUntilFinished / 1000)));
        }

        @Override
        public void onFinish() {
            tvGetCode.setText(R.string.yanzhengma_huoqu);
            tvGetCode.setEnabled(true);
        }
    };

    @Event(value = {R.id.activity_login_validcode_next, R.id.activity_login_validcode_guanbi, R.id.activity_login_validcode_getcode, R.id.fragment_login_styles_wechat, R.id.fragment_login_styles_username})
    private void onclick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.activity_login_validcode_guanbi:
                InputMethodUtils.closeSoftKeyboard(this);

                if (AppConfig.currSel == 3 || AppConfig.currSel == 4) {
                    AppConfig.currSel = AppConfig.DEFAULT_INDEX_FRAGMENT;
                }
                finish();
                break;

            case R.id.activity_login_validcode_next:
                doLogin();
                break;
            case R.id.activity_login_validcode_getcode:
                loadValidCode();
                break;
            case R.id.fragment_login_styles_wechat:
                // send oauth request
                IWXAPI wxApi = ((MyApplication) getApplication()).wxApi;
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = System.currentTimeMillis() + "";
                wxApi.sendReq(req);
                break;
            case R.id.fragment_login_styles_username:
                startActivity(ActivityLogin.getIntent(this));
                finish();
                break;
        }
    }

    private void loadValidCode() {
        setLoding(this, false);
        new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                String phone = ed_phone.getText().toString().trim();
                return UserBiz.VerificationCode(phone, UserBiz.VERIFICATION_CODE_LOGINBYVALID);
            }

            @Override
            protected void onExecuteSucceeded(String s) {
                closeLoding();
                ToastUtil.show(getApplicationContext(), "验证码发送成功");
                tvGetCode.setEnabled(false);
                timer.start();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userBiz = UserBiz.getInstance(this);
        //隐藏自己的登录方式---验证登录
        findViewById(R.id.fragment_login_styles_yanzheng).setVisibility(View.GONE);

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
                    InputMethodUtils.closeSoftKeyboard(ActivityLoginOfValidCode.this);
                }
                return false;
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (UserBiz.isLogined()) {
            finish();
        }
    }

    private void doLogin() {
        setLoding(this, false);
        new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                return userBiz.loginCode(ed_phone.getText().toString().trim(),
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

                ActivityLoginOfValidCode.this.finish();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
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
        Intent it = new Intent(context, ActivityLoginOfValidCode.class);
        return it;
    }
}
