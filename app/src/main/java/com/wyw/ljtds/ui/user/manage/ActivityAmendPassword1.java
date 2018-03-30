package com.wyw.ljtds.ui.user.manage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.user.ActivityLogin;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/6/4 0004.
 */
@ContentView( R.layout.activity_amend_password1)
public class ActivityAmendPassword1 extends BaseActivity {
    @ViewInject(R.id.header_title)
    private TextView title;
    @ViewInject(R.id.fasong)
    private Button button;
    @ViewInject( R.id.code )
    private EditText code;
    @ViewInject( R.id.yanzheng )
    private EditText yanzheng;
    @ViewInject( R.id.mima )
    private EditText mima;


    private Timer timer;
    private TimerTask timerTask;
    private int count = 60;//60秒

    @Event(value = { R.id.header_return,R.id.fasong,R.id.next})
    private void onClick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.header_return:
                finish();
                break;

            case R.id.fasong:
                if (StringUtils.isEmpty( code.getText().toString() .trim() )||code.getText().toString() .trim().length()!=11){
                    ToastUtil.show( this,"请输入正确手机号码" );
                }else {
                    getCode( code.getText().toString() .trim());
                    runTimerTask();
                }

                break;

            case R.id.next:
                if (StringUtils.isEmpty( yanzheng.getText().toString().trim() )||yanzheng.getText().toString().trim().length()!=6){
                    ToastUtil.show( this,"请输入正确的验证码" );
                }else if (StringUtils.isEmpty( mima.getText().toString().trim() )){
                    ToastUtil.show( this,"请输入新的的密码" );
                }else {
                    findPwd();
                }
                break;
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        title.setText( "找回密码" );
    }

    private Handler mHandler = new Handler() {

        @Override
        public void dispatchMessage(Message msg) {
            // TODO Auto-generated method stub
            if (count >= 0) {
                button.setText( count + "s" );
                button.setClickable( false );
                count--;
            } else {
                resetTimer();
            }
        }
    };


    BizDataAsyncTask<String> codeTask;

    private void getCode(final String phone) {
        codeTask = new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                return UserBiz.VerificationCode( phone, "1" );
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

    BizDataAsyncTask<Boolean> findPwdTask;
    private void findPwd(){
        findPwdTask=new BizDataAsyncTask<Boolean>() {
            @Override
            protected Boolean doExecute() throws ZYException, BizFailure {
                return UserBiz.findPassWord( code.getText().toString(),yanzheng.getText().toString().trim(),mima.getText().toString() );
            }

            @Override
            protected void onExecuteSucceeded(Boolean s) {
                if (s){
                    ToastUtil.show(ActivityAmendPassword1.this, "修改密码成功!");
                    startActivity( new Intent( ActivityAmendPassword1.this, ActivityLogin.class ) );
                    finish();
                }
            }

            @Override
            protected void OnExecuteFailed() {

            }
        };
        findPwdTask.execute(  );
    }



    /**
     * 启动timer计时器
     */
    private void runTimerTask() {
        timer = new Timer();
        timerTask = new TimerTask() {

            @Override
            public void run() {
                mHandler.sendEmptyMessage( 0 );
            }
        };
        timer.schedule( timerTask, 1000, 1000 );

    }

    private void resetTimer() {
        button.setText( R.string.yanzhengma3 );
        button.setClickable( true );
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
