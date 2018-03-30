package com.wyw.ljtmgr.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wyw.ljtmgr.R;
import com.wyw.ljtmgr.biz.SimpleCommonCallback;
import com.wyw.ljtmgr.biz.UserBiz;
import com.wyw.ljtmgr.config.MyApplication;
import com.wyw.ljtmgr.config.PreferenceCache;
import com.wyw.ljtmgr.model.BaseJson;
import com.wyw.ljtmgr.model.Header;
import com.wyw.ljtmgr.model.LoginModel;
import com.wyw.ljtmgr.model.ServerResponse;
import com.wyw.ljtmgr.model.UpdatePassWordModel;
import com.wyw.ljtmgr.utils.CommonUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import utils.GsonUtils;

/**
 * Created by Administrator on 2017/8/9.
 */
@ContentView(R.layout.update_pass)
public class UpdatePasswordActivity extends BaseActivity {
    @ViewInject(R.id.newPwd)
    private TextView tvNewPwd;
    @ViewInject(R.id.oldPwd)
    private TextView tvOldPwd;

    @Event(value = {R.id.confirm})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                LoginModel loginer = MyApplication.getCurrentLoginer();
                BaseJson<UpdatePassWordModel> baseJson = new BaseJson<UpdatePassWordModel>();
                String token = PreferenceCache.getToken();
                String userId = loginer.getAdminUserId();
                String oldPwd = tvOldPwd.getText().toString();
                String newPwd = tvNewPwd.getText().toString();
                Header head = new Header();
                head.setToken(token);
                UpdatePassWordModel updatePassWordModel = new UpdatePassWordModel();
                updatePassWordModel.setAdminUserId(userId);
                updatePassWordModel.setNewPwd(newPwd);
                updatePassWordModel.setOldPwd(oldPwd);

                UserBiz.updatePwd(updatePassWordModel, new SimpleCommonCallback<ServerResponse>(UpdatePasswordActivity.this) {
                    @Override
                    protected void handleResult(ServerResponse result) {
                        Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                        UserBiz.logout(UpdatePasswordActivity.this);
                        finish();
                    }
                });
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();
    }

    public static Intent getIntent(Context context) {
        Intent it = new Intent(context, UpdatePasswordActivity.class);
        return it;
    }

}
