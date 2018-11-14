package com.wyw.ljtds.ui.user;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import com.wyw.ljtds.MainActivity;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.SoapProcessor;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.model.ServerResponse;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Timer;
import java.util.TimerTask;

import cn.xiaoneng.uiapi.Ntalker;

/**
 * Created by Administrator on 2016/12/22 0022.
 */

@ContentView(R.layout.activity_login_bindphone)
public class ActivityLoginBindPhone extends BaseActivity {
    private static final String TAG_OPENID = "com.wyw.ljtds.ui.user.ActivityLoginBindPhone.TAG_OPENID";
    private static final String TAG_WXID = "com.wyw.ljtds.ui.user.ActivityLoginBindPhone.TAG_WXID";
    private static final String TAG_NICKNAME = "com.wyw.ljtds.ui.user.ActivityLoginBindPhone.TAG_NICKNAME";
    UserBiz bizUser;
    //    @ViewInject(R.id.header_title)
//    private TextView title;
    @ViewInject(R.id.activity_login_bindphone_toolbar)
    private Toolbar toolbar;
    @ViewInject(R.id.activity_login_bindphone_getcode)
    private TextView tvGetCode;
    @ViewInject(R.id.activity_login_bindphone_ed_phone)
    private EditText tvPhone;
    @ViewInject(R.id.activity_login_bindphone_ed_pass)
    private EditText tvValidCode;


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

    @Event(value = {R.id.activity_login_bindphone_getcode, R.id.activity_login_bindphone_next})
    private void onclick(View v) {
        String phone = tvPhone.getText().toString().trim();
        switch (v.getId()) {
            case R.id.activity_login_bindphone_getcode:
                if (StringUtils.isEmpty(phone)) return;
                setLoding(this, false);
                requestVerificationCode(phone);

                break;
            case R.id.activity_login_bindphone_next:
                if (StringUtils.isEmpty(phone)) return;
                String validcode = tvValidCode.getText().toString().trim();
                if (StringUtils.isEmpty(validcode)) return;
                String wxId = getIntent().getStringExtra(TAG_WXID);
                String nickName = getIntent().getStringExtra(TAG_NICKNAME);
                String openId = getIntent().getStringExtra(TAG_OPENID);
                registWX(wxId, openId, nickName, phone, validcode);
                break;

        }
    }

    private void registWX(final String wxId, final String openId, final String nickName, final String phone, final String validcode) {
        setLoding(this, false);
        new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                return bizUser.registWX(wxId, openId, nickName, phone, validcode);
            }

            @Override
            protected void onExecuteSucceeded(String s) {
                closeLoding();
//                Utils.log("registWX:" + s);
                JsonParser parser = new JsonParser();
                JsonObject jsonObject = parser.parse(s).getAsJsonObject();
                String statCode = jsonObject.get(SoapProcessor.RESPONSE_STATUSCODE).getAsString();
                JsonElement resultElement;
                if (ServerResponse.STAT_OK.equals(statCode)) {
                    //login 成功  ???
                    resultElement = jsonObject.get(SoapProcessor.RESPONSE_RESULT);
                    String token = resultElement.getAsString();
                    PreferenceCache.putToken(token); // 持久化缓存token
                    PreferenceCache.putAutoLogin(true);// 记录是否自动登录
                    PreferenceCache.putUsername(tvPhone.getText().toString().trim());
                    if (PreferenceCache.isAutoLogin()) {
                        PreferenceCache.putPhoneNum(tvPhone.getText().toString().trim());
                    }
                    Ntalker.getBaseInstance().login(((MyApplication) getApplication()).entityName, tvPhone.getText().toString().trim(), 0);
                    finish();

                    //弹出赠送
//                    isShowSentTicket("1");

                }
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    private void requestVerificationCode(final String phone) {
        new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
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
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bizUser = UserBiz.getInstance(this);
        initViews();

        initEvents();
    }

    private void showSentTicket() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.fragment_send_ticket, (ViewGroup) findViewById(R.id.fragment_con));
        ImageView ivClose = (ImageView) layout.findViewById(R.id.fragment_send_ticket_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = MainActivity.getIntent(ActivityLoginBindPhone.this);
                AppConfig.currSel = AppConfig.DEFAULT_INDEX_FRAGMENT;
//                it.putExtra(AppConfig.IntentExtraKey.BOTTOM_MENU_INDEX, AppConfig.currSel);
                startActivity(it);
                ActivityLoginBindPhone.this.finish();
            }
        });

        ImageView sdv = (ImageView) layout.findViewById(R.id.fragment_send_ticket_sdv_show);
        Picasso.with(this).load(Uri.parse(AppConfig.IMAGE_PATH_LJT_ECOMERCE + "/ecommerce/images/mobileIndexImages/regist_ok.png")).placeholder(R.drawable.img_adv_zhanwei).into(sdv);
        Dialog dialog = new Dialog(ActivityLoginBindPhone.this, R.style.Theme_AppCompat_Dialog);
        dialog.setContentView(layout);
        dialog.setCancelable(false);
        dialog.setTitle(R.string.gongxi);

        dialog.show();

        View btn = layout.findViewById(R.id.btn_sent_ticket);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.TAG_CMD = MainActivity.TAG_CMD_UserInfoExtraActivity;
                AppConfig.currSel = AppConfig.DEFAULT_INDEX_FRAGMENT;
                Intent it = MainActivity.getIntent(ActivityLoginBindPhone.this);
                startActivity(it);
                ActivityLoginBindPhone.this.finish();

            }
        });


    }

    private void isShowSentTicket(String isShowImg) {
        switch (isShowImg) {
            case "1":
                //显示赠送优惠券的
                showSentTicket();
                break;
            default:
                AppConfig.currSel = 4;
                Intent it = MainActivity.getIntent(ActivityLoginBindPhone.this);
                startActivity(it);
                ActivityLoginBindPhone.this.finish();
                break;
        }
    }

    private void initViews() {
        setTitle("");
    }

    private void initEvents() {
        toolbar.findViewById(R.id.toolbar_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public static Intent getIntent(Context context, String openId, String wxId, String nickName) {
        Intent intent = new Intent(context, ActivityLoginBindPhone.class);
        intent.putExtra(TAG_OPENID, openId);
        intent.putExtra(TAG_WXID, wxId);
        intent.putExtra(TAG_NICKNAME, nickName);
        return intent;
    }


}
