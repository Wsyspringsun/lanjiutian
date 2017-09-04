package com.wyw.ljtwl.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wyw.ljtwl.R;
import com.wyw.ljtwl.config.PreferenceCache;
import com.wyw.ljtwl.model.BaseJson;
import com.wyw.ljtwl.model.Header;
import com.wyw.ljtwl.model.LogOutModel;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Administrator on 2017/7/4 0004.
 */

@ContentView(R.layout.activity_setting)
public class ActivitySetting extends BaseActivitry {
    @ViewInject(R.id.header_title)
    private TextView title;

    @Event(value = {R.id.header_return,R.id.sign_out,R.id.updateInfo})
    private void onClick(View view){
        switch (view.getId()){
            case R.id.header_return:
                finish();
                break;
            case R.id.updateInfo:
                startActivity(new Intent(this,UpdatePassword.class));
                break;
            case R.id.sign_out:
                String userId = PreferenceCache.getUserId();
                String token = PreferenceCache.getToken();
                Log.e("jsonData", userId);
                BaseJson<LogOutModel> baseJson = new BaseJson<LogOutModel>();
                Header head = new Header();
                head.setToken(token);
                LogOutModel logOutModel = new LogOutModel();
                logOutModel.setUserName(userId);
                baseJson.setHead(head);
                baseJson.setBody(logOutModel);
                Gson gson = new Gson();
                String data = gson.toJson(baseJson);
                doLogOut(data);
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText(R.string.setting);
    }

    public void doLogOut(String data){
        RequestParams params = new RequestParams("http://www.lanjiutian.com/ljt_mobile_store/v/user/logout");

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
                        Log.e("success---", "success");
                        Intent it = new Intent(ActivitySetting.this, ActivityLogin.class);
                        startActivity(it);
//                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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
