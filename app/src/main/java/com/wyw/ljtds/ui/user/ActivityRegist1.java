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
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
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

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/12/22 0022.
 */

@ContentView(R.layout.activity_regist1)
public class ActivityRegist1 extends BaseActivity {
    @ViewInject(R.id.header_title)
    private TextView title;
    @ViewInject(R.id.header_return_text)
    private TextView return_tv;
    @ViewInject(R.id.shoujihao)
    private TextView phone_num;
    @ViewInject(R.id.fasong)
    private Button button;
    @ViewInject(R.id.code)
    private EditText code;
    @ViewInject(R.id.mima)
    private EditText password;

    @ViewInject(R.id.chk_agree_license)
    private CheckBox chkAgreeLicense;


    private Timer timer;
    private TimerTask timerTask;
    private int count = 60;//60秒
    private String phone = "";

    @Event(value = {R.id.next, R.id.header_return, R.id.fasong, R.id.zhuce_text})
    private void onclick(View v) {
        switch (v.getId()) {
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
                Log.e(AppConfig.ERR_TAG, "data:" + password.getText().toString() + "/len:" + password.getText().length());
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

        title.setText("设置密码");
        return_tv.setText("账号登陆");

        phone = getIntent().getStringExtra(AppConfig.IntentExtraKey.PHONE_NUMBER);
        phone_num.setText(getResources().getString(R.string.yanzhengma4) + phone);

        getCode(phone);
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
    BizDataAsyncTask<String> registTask;

    private void doRegist() {
        registTask = new BizDataAsyncTask<String>() {

            @Override
            protected String doExecute() throws ZYException, BizFailure {
                return UserBiz.register(phone, code.getText().toString().trim(), password.getText().toString().trim(), "", "0");
            }

            @Override
            protected void onExecuteSucceeded(String result) {
                closeLoding();

//                getLog = 1
                PreferenceCache.putToken(result); // 持久化缓存token
                PreferenceCache.putAutoLogin(true);// 记录是否自动登录
                PreferenceCache.putUsername(phone);

                if (PreferenceCache.isAutoLogin()) {
                    PreferenceCache.putPhoneNum(phone);
                }

                //显示赠送优惠券的
                showSentTicket();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        };
        registTask.execute();

    }

    private void showSentTicket() {
        super.onResume();
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.fragment_send_ticket, (ViewGroup) findViewById(R.id.fragment_con));
        SimpleDraweeView sdv = (SimpleDraweeView) layout.findViewById(R.id.fragment_send_ticket_sdv_show);
        sdv.setImageURI(Uri.parse(AppConfig.IMAGE_PATH_LJT + "/.appinit/regist_ok.png"));
//       final Dialog dialog = new AlertDialog.Builder(this)).setView(layout).create();
        Dialog dialog = new Dialog(ActivityRegist1.this, R.style.Theme_AppCompat_Dialog);
//        dialog.setContentView(R.layout.fragment_send_ticket);
        dialog.setContentView(layout);
        dialog.setCancelable(false);
        dialog.setTitle(R.string.gongxi);

//        Window dialogWindow = dialog.getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
//        lp.alpha = 0.9f; // 透明度
        dialog.show();

        View view = layout.findViewById(R.id.btn_sent_ticket);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //申请领取优惠券
//                doGetTicketTask();
//                Log.e(AppConfig.ERR_TAG, "恭喜您");
                Intent it = new Intent(ActivityRegist1.this, MainActivity.class);
                AppConfig.currSel = 4;
                it.putExtra(AppConfig.IntentExtraKey.BOTTOM_MENU_INDEX, AppConfig.currSel);
                startActivity(it);

//                dialog.dismiss();
//                Intent it = new Intent(TestActivity.this, ActivityLogin.class);
//                startActivity(it);
            }
        });


    }

    BizDataAsyncTask<String> codeTask;

    private void getCode(final String phone) {
        codeTask = new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                return UserBiz.VerificationCode(phone, "0");
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
