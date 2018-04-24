package com.wyw.ljtds.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
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
import com.wyw.ljtds.model.UserModel;
import com.wyw.ljtds.model.WXUserInfo;
import com.wyw.ljtds.ui.user.ActivityLoginBindPhone;
import com.wyw.ljtds.ui.user.order.ActivityOrder;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.Utils;

import cn.xiaoneng.uiapi.Ntalker;

import static com.wyw.ljtds.R.id.context;
import static com.wyw.ljtds.R.id.ed_phone;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    public static final String SHARE_RESULT_OK = "0"; //进行了分享

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    UserBiz userBiz;
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.log("WX Ent onC");

        api = WXAPIFactory.createWXAPI(this, AppConfig.WEIXIN_APP_ID);
        api.handleIntent(getIntent(), this);

        userBiz = new UserBiz(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Utils.log("WX Ent onNewIntent");
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        Utils.log("WX Ent onReq");
    }

    @Override
    public void onResp(BaseResp resp) {
        Utils.log(" net ...  WX Ent onResp");
        Utils.log("onWXFinish, errCode = " + resp.errCode + "...resp:" + resp);
        if (resp instanceof SendAuth.Resp) {
            //授权登录 的结果
            if (0 == resp.errCode) {
                SendAuth.Resp authResp = (SendAuth.Resp) resp;
                switch (authResp.errCode) {
//                    ERR_OK = 0(用户同意) ERR_AUTH_DENIED = -4（用户拒绝授权） ERR_USER_CANCEL = -2（用户取消）
                    case SendAuth.Resp.ErrCode.ERR_OK:
                        final String code = authResp.code;
//                        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=" + code + "&grant_type=authorization_code";
//                        Utils.getHttpResponse();
                        Utils.log("code:" + code);
                        new BizDataAsyncTask<String>() {

                            @Override
                            protected String doExecute() throws ZYException, BizFailure {
                                return userBiz.loginWx(code);
                            }

                            @Override
                            protected void onExecuteSucceeded(String s) {
                                Utils.log("wexin result:" + s);
                                JsonParser parser = new JsonParser();
                                JsonObject jsonObject = parser.parse(s).getAsJsonObject();
                                String statCode = jsonObject.get(SoapProcessor.RESPONSE_STATUSCODE).getAsString();
                                JsonElement resultElement;
                                if (ServerResponse.STAT_LOGINWX_ERR.equals(statCode)) {
                                    //当前微信用户没有在平台上注册，跳到手机
                                    resultElement = jsonObject.get(SoapProcessor.RESPONSE_RESULT);
                                    Gson gson = new GsonBuilder().create();

                                    TypeToken<WXUserInfo> tt = new TypeToken<WXUserInfo>() {
                                    };
                                    WXUserInfo wxUi = gson.fromJson(resultElement, tt.getType());
                                    startActivity(ActivityLoginBindPhone.getIntent(WXEntryActivity.this, wxUi.getWxId(), wxUi.getNickName()));

                                    finish();
                                } else if (ServerResponse.STAT_OK.equals(statCode)) {
                                    //login 成功  ???
                                    resultElement = jsonObject.get(SoapProcessor.RESPONSE_RESULT);
                                    String token = resultElement.getAsString();
                                    getUserInfo(token);
                                } else if (ServerResponse.STAT_USER_MSG.equals(statCode)) {
                                    String msg = jsonObject.get(SoapProcessor.RESPONSE_MESSAGE).getAsString();
                                    ToastUtil.show(WXEntryActivity.this, msg);
                                } else {
                                    String msg = jsonObject.get(SoapProcessor.RESPONSE_MESSAGE).getAsString();
                                    ToastUtil.show(WXEntryActivity.this, getString(R.string.msg_crash));
                                    Utils.log(msg);
                                }
                            }

                            @Override
                            protected void OnExecuteFailed() {
                                finish();
                            }
                        }.execute();
                        break;
                    case SendAuth.Resp.ErrCode.ERR_AUTH_DENIED:
                    case SendAuth.Resp.ErrCode.ERR_USER_CANCEL:
                        finish();
                        break;
                    default:
                        finish();
                        break;
                }
            } else {
                finish();
            }

        } else {
            //微信分享的结果
            if (0 == resp.errCode) {
                PreferenceCache.putWXShareResult(SHARE_RESULT_OK);
            } else {
                PreferenceCache.putWXShareResult(resp.errCode + "");
            }
            finish();
        }


    }

    private void getUserInfo(final String token) {
        new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                return userBiz.getUserInfo(token);
            }

            @Override
            protected void onExecuteSucceeded(String result) {
                Utils.log("getUserInfo:" + result);
                JsonParser parser = new JsonParser();
                JsonObject jsonObject = parser.parse(result).getAsJsonObject();
                String statCode = jsonObject.get(SoapProcessor.RESPONSE_STATUSCODE).getAsString();
                JsonElement resultElement;
                if (ServerResponse.STAT_OK.equals(statCode)) {
                    resultElement = jsonObject.get(SoapProcessor.RESPONSE_RESULT);
                    Gson gson = new GsonBuilder().create();

                    TypeToken<UserModel> tt = new TypeToken<UserModel>() {
                    };

                    UserModel mUser = gson.fromJson(resultElement, tt.getType());
                    String phone = mUser.getMOBILE();
                    PreferenceCache.putToken(token); // 持久化缓存token
                    PreferenceCache.putAutoLogin(true);// 记录是否自动登录
                    PreferenceCache.putUsername(phone);
                    if (PreferenceCache.isAutoLogin()) {
                        PreferenceCache.putPhoneNum(phone);
                    }
                    Ntalker.getBaseInstance().login(((MyApplication) getApplication()).entityName, phone, 0);
                } else {
                    String msg = jsonObject.get(SoapProcessor.RESPONSE_MESSAGE).getAsString();
                    ToastUtil.show(WXEntryActivity.this, msg);
                }
                finish();

            }

            @Override
            protected void OnExecuteFailed() {
                finish();
            }
        }.execute();
    }
}