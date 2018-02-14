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
import com.wyw.ljtmgr.config.MyApplication;
import com.wyw.ljtmgr.config.PreferenceCache;
import com.wyw.ljtmgr.model.BaseJson;
import com.wyw.ljtmgr.model.Header;
import com.wyw.ljtmgr.model.LoginModel;
import com.wyw.ljtmgr.model.UpdatePassWordModel;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

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
                String oldPwd = tvNewPwd.getText().toString();
                String newPwd = tvNewPwd.getText().toString();
                Header head = new Header();
                head.setToken(token);
                UpdatePassWordModel updatePassWordModel = new UpdatePassWordModel();
                updatePassWordModel.setAdminUserId(userId);
                updatePassWordModel.setNewPwd(newPwd);
                updatePassWordModel.setOldPwd(oldPwd);
                baseJson.setHead(head);
                baseJson.setBody(updatePassWordModel);
                Gson gson = new Gson();
                String data = gson.toJson(baseJson);
                Log.e("jsondata", data);
                doUpdate(data);
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

    public void doUpdate(String data) {
        RequestParams params = new RequestParams("http://www.lanjiutian.com/ljt_mobile_store/v/user/updatePwd");

        params.setAsJsonContent(true);
        params.setBodyContent(data);
//                setLoding(getActivity(), false);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("err", result);
                try {
                    JSONObject jsonData = new JSONObject(result);
                    String success = jsonData.getString("success");
                    String msg = jsonData.getString("msg");
                    Log.e("success", success);
                    if (success.equals("0")) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                        finish();
                        // Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    } else if (success.equals("2")) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //解析result
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("err", ex.getMessage());
                Log.e("err", "error.........");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                closeLoding();
                Log.e("err", "cancel.........");
            }

            @Override
            public void onFinished() {
                Log.e("err", "finished.........");
            }
        });
    }
}
