package com.wyw.ljtds.ui.user;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.wyw.ljtds.MainActivity;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.ui.base.ActivityWebView;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/12/22 0022.
 */

@ContentView(R.layout.activity_regist1)
public class ActivityRegist1 extends BaseActivity {
    @ViewInject(R.id.activity_fragment_title)
    private TextView title;
    @ViewInject(R.id.fasong)
    private Button button;
    @ViewInject(R.id.activity_regist1_code)
    private EditText code;
    @ViewInject(R.id.activity_regist1_mima)
    private EditText password;
    @ViewInject(R.id.activity_regist1_tuijianren_mobile)
    private EditText tuijianMobile;

    @ViewInject(R.id.chk_agree_license)
    private CheckBox chkAgreeLicense;


    private Timer timer;
    private TimerTask timerTask;
    private int count = AppConfig.PHONE_VALIDCODE_TEIMER;//60秒
    private String phone = "";

    @Event(value = {R.id.back, R.id.next, R.id.header_return, R.id.fasong, R.id.zhuce_text})
    private void onclick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.next:
//                if (StringUtils.isEmpty(code.getText().toString().trim())){
//                    //ToastUtil.show(this,);
//                }
//                 同意协议
                if (!chkAgreeLicense.isChecked()) {
                    ToastUtil.show(this, getString(R.string.agree_license));
                    return;
                }
                password.setText(password.getText().toString().trim());
                if (StringUtils.isEmpty(password.getText())) {
                    ToastUtil.show(this, getString(R.string.password_valid));
                } else if (password.getText().length() > 20 || password.getText().length() < 6) {
                    ToastUtil.show(this, getString(R.string.password_valid));
                } else {
                    setLoding(this, false);
                    doRegist();
                }
                break;

            case R.id.header_return:
                finish();
                startActivity(new Intent(this, ActivityLogin.class));
                break;

            case R.id.fasong:
                getCode(phone);
                runTimerTask();
                break;

            case R.id.zhuce_text:
                startActivity(new Intent(this, ActivityWebView.class));
                break;

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        showSentTicket();

        initView();
    }

    private void initView() {
        title.setText("设置密码");
        phone = getIntent().getStringExtra(AppConfig.IntentExtraKey.PHONE_NUMBER);

        runTimerTask();
    }

    private Handler mHandler = new Handler() {

        @Override
        public void dispatchMessage(Message msg) {
            // TODO Auto-generated method stub
            if (count >= 0) {
                button.setText(count + "s");
                button.setClickable(false);
                count--;
            } else {
                resetTimer();
            }
        }
    };

    /**
     * 注册
     */
    private void doRegist() {
        setLoding(this, false);
        new BizDataAsyncTask<Map>() {

            @Override
            protected Map doExecute() throws ZYException, BizFailure {
                return UserBiz.register(phone, code.getText().toString().trim(), password.getText().toString().trim(), tuijianMobile.getText().toString().trim());
            }

            @Override
            protected void onExecuteSucceeded(Map result) {
                closeLoding();

                String token = (String) result.get("token");
                String isShowImg = (String) result.get("isShowImg");

//                getLog = 1
                PreferenceCache.putToken(token); // 持久化缓存token
                PreferenceCache.putAutoLogin(true);// 记录是否自动登录
                PreferenceCache.putUsername(phone);

                if (PreferenceCache.isAutoLogin()) {
                    PreferenceCache.putPhoneNum(phone);
                }
                isShowSentTicket(isShowImg);
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();

    }

    private void isShowSentTicket(String isShowImg) {
        switch (isShowImg) {
            case "1":
                //显示赠送优惠券的
                showSentTicket();
                break;
            default:
                AppConfig.currSel = 4;
                Intent it = MainActivity.getIntent(ActivityRegist1.this);
                startActivity(it);
                ActivityRegist1.this.finish();
                break;
        }
    }

    private void showSentTicket() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.fragment_send_ticket, (ViewGroup) findViewById(R.id.fragment_con));
        ImageView ivClose = (ImageView) layout.findViewById(R.id.fragment_send_ticket_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = MainActivity.getIntent(ActivityRegist1.this);
                AppConfig.currSel = AppConfig.DEFAULT_INDEX_FRAGMENT;
//                it.putExtra(AppConfig.IntentExtraKey.BOTTOM_MENU_INDEX, AppConfig.currSel);
                startActivity(it);
                ActivityRegist1.this.finish();
            }
        });

        ImageView sdv = (ImageView) layout.findViewById(R.id.fragment_send_ticket_sdv_show);
        Picasso.with(this).load(Uri.parse(AppConfig.IMAGE_PATH_LJT_ECOMERCE + "/ecommerce/images/mobileIndexImages/regist_ok.png")).placeholder(R.drawable.img_adv_zhanwei).into(sdv);
        Dialog dialog = new Dialog(ActivityRegist1.this, R.style.Theme_AppCompat_Dialog);
        dialog.setContentView(layout);
        dialog.setCancelable(false);
        dialog.setTitle(R.string.gongxi);

        sdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.TAG_CMD = MainActivity.TAG_CMD_UserInfoExtraActivity;
                AppConfig.currSel = AppConfig.DEFAULT_INDEX_FRAGMENT;
                Intent it = MainActivity.getIntent(ActivityRegist1.this);
                startActivity(it);
                ActivityRegist1.this.finish();

            }
        });

        dialog.show();

    }

    BizDataAsyncTask<String> codeTask;

    private void getCode(final String phone) {
        codeTask = new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                return UserBiz.VerificationCode(phone, UserBiz.VERIFICATION_CODE_REG);
            }

            @Override
            protected void onExecuteSucceeded(String s) {

            }

            @Override
            protected void OnExecuteFailed() {

            }
        };
        codeTask.execute();
    }

    /**
     * 启动timer计时器
     */
    private void runTimerTask() {
        timer = new Timer();
        timerTask = new TimerTask() {

            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        };
        timer.schedule(timerTask, 1000, 1000);

    }

    private void resetTimer() {
        button.setText(R.string.yanzhengma3);
        button.setClickable(true);
        count = 60;
        if (timerTask != null) {
            timerTask.cancel();
        }
        if (timer != null) {
            timer.cancel();
        }
        timerTask = null;
        timer = null;
    }
}
