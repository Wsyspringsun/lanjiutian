package com.wyw.ljtwl.ui;

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
import android.widget.Toast;

import com.google.gson.Gson;
import com.wyw.ljtwl.R;
import com.wyw.ljtwl.config.AppConfig;
import com.wyw.ljtwl.config.PreferenceCache;
import com.wyw.ljtwl.model.LoginModel;
import com.wyw.ljtwl.utils.InputMethodUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Administrator on 2017/7/3 0003.
 */

@ContentView(R.layout.activity_login)
public class ActivityLogin extends BaseActivitry {
    @ViewInject(R.id.guanbi)
    private ImageView close;
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
                it = new Intent(this, MainActivity.class);
//                AppConfig.currSel = 0;
//                it.putExtra( AppConfig.IntentExtraKey.BOTTOM_MENU_INDEX, AppConfig.currSel );
                startActivity(it);
                break;

            case R.id.next:
                doLogin();
//                doGet();
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
            finish();
            Intent it = new Intent(this, MainActivity.class);
//            AppConfig.currSel = 0;
//            it.putExtra( AppConfig.IntentExtraKey.BOTTOM_MENU_INDEX, AppConfig.currSel );
            startActivity(it);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void doLogin() {
        LoginModel loginModel = new LoginModel();
        Log.e(AppConfig.TAG_ERR, "111aaaa");
//        JSONObject jsonObject = new JSONObject();

        loginModel.setUserName(ed_phone.getText().toString());
        loginModel.setPassWord(ed_pass.getText().toString());
        loginModel.setMos("andriod");
//            JSONObject jsonObject = new JSONObject();
        Gson gson = new Gson();
        String data = gson.toJson(loginModel);
//            jsonObject.put("username", ed_phone.getText());
//            jsonObject.put("passWord", ed_pass.getText());
//            jsonObject.put("mos", "andriod");


        Log.e(AppConfig.TAG_ERR, data);
//                //String s = new HttpRequestUtil("http://192.168.2.114:8080/ljt_mobile_store/login/userLogin",jsonObject.toString()).doRequest();
//                //String s = new HttpRequestUtil("http://192.168.2.114:8080/conn/MyServlet",jsonObject.toString()).doRequest();
//        RequestParams params = new RequestParams("http://www.lanjiutian.com/ljt_mobile_logistics/login/userLogin");
        RequestParams params = new RequestParams(AppConfig.WEB_DOMAIN + "/login/userLogin");

        params.setAsJsonContent(true);
        params.setBodyContent(data);
//                setLoding(getActivity(), false);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(AppConfig.TAG_ERR, result);
                try {
                    JSONObject jsonData = new JSONObject(result);
                    String success = jsonData.getString("success");
                    String msg = jsonData.getString("msg");
                    Log.e(AppConfig.TAG_ERR, success);
                    if (success.equals("0")) {
                        String token = jsonData.getString("token");
                        String adminUserId = jsonData.getString("adminUserId");
                        String oidGroupId = jsonData.getString("oidGroupId");
                        PreferenceCache.putToken(token); // 持久化缓存token
                        PreferenceCache.putAutoLogin(true);// 记录是否自动登录
                        PreferenceCache.putUsername(jsonData.getString("adminUserName"));
                        PreferenceCache.putUserId(adminUserId);
                        PreferenceCache.putOidGroupId(oidGroupId);
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("jsonData", jsonData.toString());
                        startActivity(intent);
                    } else if (success.equals("2")) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(AppConfig.TAG_ERR, "JSONException");
                } catch (Exception ex) {
                    Log.e(AppConfig.TAG_ERR, "Exception:" + ex.getMessage());
                }

                //解析result
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(AppConfig.TAG_ERR, ex.getMessage());
                Log.e(AppConfig.TAG_ERR, "error.........");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                closeLoding();
                Log.e(AppConfig.TAG_ERR, "cancel.........");
            }

            @Override
            public void onFinished() {
                Log.e(AppConfig.TAG_ERR, "finished.........");
            }
        });
    }
}
