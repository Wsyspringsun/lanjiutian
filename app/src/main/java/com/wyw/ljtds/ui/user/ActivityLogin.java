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
import com.wyw.ljtds.ui.user.manage.ActivityAmendPassword1;
import com.wyw.ljtds.utils.InputMethodUtils;

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

    @Event(value = {R.id.guanbi, R.id.zhuce, R.id.next, R.id.chongzhi})
    private void onclick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.guanbi:
                InputMethodUtils.closeSoftKeyboard(this);

//                it = new Intent(ActivityLogin.this, MainActivity.class);
//                startActivity(it);

                finish();
                break;

            case R.id.zhuce:
                it = new Intent(this, ActivityRegist.class);
                startActivity(it);
                break;

            case R.id.next:
                doLogin();
                break;

            case R.id.chongzhi:
                finish();
                startActivity(new Intent(this, ActivityAmendPassword1.class));
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


    BizDataAsyncTask<String> loginTask;

    private void doLogin() {
        setLoding(this, false);
        loginTask = new BizDataAsyncTask<String>() {

            @Override
            protected void onExecuteSucceeded(String result) {
                closeLoding();
                PreferenceCache.putToken(result); // 持久化缓存token
                PreferenceCache.putAutoLogin(true);// 记录是否自动登录
                PreferenceCache.putUsername(ed_phone.getText().toString().trim());

                if (PreferenceCache.isAutoLogin()) {
                    PreferenceCache.putPhoneNum(ed_phone.getText().toString().trim());
                }


                ActivityLogin.this.finish();
                AppManager.destoryActivity("submit");//处理订单页未登录状态


            }

            @Override
            protected String doExecute() throws ZYException, BizFailure {

                return UserBiz.login(ed_phone.getText().toString().trim(),
                        ed_pass.getText().toString().trim(), "0");
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
        if (keyCode == event.KEYCODE_BACK) {
            InputMethodUtils.keyBoxIsShow(this);

//            Intent it = new Intent(this, MainActivity.class);
//            startActivity(it);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    public static void goLogin(Context context) {
        Intent it = new Intent(context, ActivityLogin.class);
        context.startActivity(it);
    }
}
