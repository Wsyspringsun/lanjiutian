package com.wyw.ljtds.ui.user.manage;

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
import com.wyw.ljtds.config.AppManager;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.user.ActivityLogin;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2017/6/4 0004.
 */
@ContentView(R.layout.activity_amend_password)
public class ActivityAmendPassword extends BaseActivity {
    @ViewInject(R.id.header_title)
    private TextView title;
    @ViewInject(R.id.ed_pass1)
    private EditText ed_pass1;
    @ViewInject(R.id.ed_pass2)
    private EditText ed_pass2;
    @ViewInject(R.id.ed_pass3)
    private EditText ed_pass3;


    @Event(value = {R.id.header_return, R.id.next})
    private void onClick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.header_return:
                finish();
                break;

            case R.id.next:
                ed_pass2.setText(ed_pass2.getText().toString().trim());
                ed_pass3.setText(ed_pass3.getText().toString().trim());
                if (StringUtils.isEmpty(ed_pass1.getText().toString().trim())) {
                    ToastUtil.show(this, "请输入原密码");
                } else if (StringUtils.isEmpty(ed_pass2.getText().toString().trim())) {
                    ToastUtil.show(this, "请输入新密码");
                } else if (StringUtils.isEmpty(ed_pass3.getText().toString().trim())) {
                    ToastUtil.show(this, "请再次输入新密码");
                } else if (!ed_pass2.getText().toString().equals(ed_pass3.getText().toString())) {
                    ToastUtil.show(this, "新旧密码不一致");
                } else if (ed_pass2.getText().length() > 20 || ed_pass2.getText().length() < 6) {
                    ToastUtil.show(this, getString(R.string.password_valid));
                } else {
                    modifyPwd();
                }
                break;

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText("修改密码");
    }

    BizDataAsyncTask<Boolean> pwdTask;

    private void modifyPwd() {
        pwdTask = new BizDataAsyncTask<Boolean>() {
            @Override
            protected Boolean doExecute() throws ZYException, BizFailure {
                return UserBiz.modifyPwd(ed_pass1.getText().toString(), ed_pass2.getText().toString());
            }

            @Override
            protected void onExecuteSucceeded(Boolean aBoolean) {
                if (aBoolean) {
                    finish();
                    AppManager.destoryActivity("111");
                    AppManager.destoryActivity("222");
                    startActivity(new Intent(ActivityAmendPassword.this, ActivityLogin.class));
                }
            }

            @Override
            protected void OnExecuteFailed() {

            }
        };
        pwdTask.execute();
    }
}
