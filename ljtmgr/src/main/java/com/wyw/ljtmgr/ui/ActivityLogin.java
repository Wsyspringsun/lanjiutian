package com.wyw.ljtmgr.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wyw.ljtmgr.R;
import com.wyw.ljtmgr.biz.SimpleCommonCallback;
import com.wyw.ljtmgr.biz.UserBiz;
import com.wyw.ljtmgr.config.AppConfig;
import com.wyw.ljtmgr.config.PreferenceCache;
import com.wyw.ljtmgr.model.LoginModel;
import com.wyw.ljtmgr.utils.InputMethodUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import utils.CommonUtil;

/**
 * Created by Administrator on 2017/7/3 0003.
 */

@ContentView(R.layout.activity_login)
public class ActivityLogin extends BaseActivity {
    //    @ViewInject(R.id.guanbi)
//    private ImageView close;
    @ViewInject(R.id.ed_phone)
    private EditText ed_phone;
    @ViewInject(R.id.ed_pass)
    private EditText ed_pass;

    private Context context;


    @Event(value = {R.id.guanbi, R.id.next})
    private void onclick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.guanbi:
                InputMethodUtils.closeSoftKeyboard(this);
                finish();
                break;

            case R.id.next:
                doLogin();
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            InputMethodUtils.closeSoftKeyboard(this);
//            AppConfig.currSel = 0;
//            it.putExtra( AppConfig.IntentExtraKey.BOTTOM_MENU_INDEX, AppConfig.currSel );
            startActivity(MainActivity.getIntent(ActivityLogin.this));
        }
        return super.onKeyDown(keyCode, event);
    }

    public static Intent getIntent(Context ctx) {
        Intent it = new Intent(ctx, ActivityLogin.class);
        it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return it;
    }

    public void doLogin() {
        LoginModel loginModel = new LoginModel();
        loginModel.setUserName(ed_phone.getText().toString());
        loginModel.setPassWord(ed_pass.getText().toString());
        loginModel.setMos("andriod");
        setLoding();
        UserBiz.login(loginModel, new SimpleCommonCallback<LoginModel>(this) {
            @Override
            protected void handleResult(LoginModel result) {
                PreferenceCache.putToken(result.getToken()); // 持久化缓存token
                PreferenceCache.putUser(new Gson().toJson(result)); // 持久化缓存token

                startActivity(MainActivity.getIntent(ActivityLogin.this));
                ActivityLogin.this.finish();
            }
        });
    }
}
