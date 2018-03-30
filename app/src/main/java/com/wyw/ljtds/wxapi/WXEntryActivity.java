package com.wyw.ljtds.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.ui.user.order.ActivityOrder;
import com.wyw.ljtds.utils.Utils;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.log("WX Ent onC");

        api = WXAPIFactory.createWXAPI(this, AppConfig.WEIXIN_APP_ID);
        api.handleIntent(getIntent(), this);
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
        Utils.log("onWXFinish, errCode = " + resp.errCode);
        if (0 == resp.errCode) {
            if (resp instanceof SendAuth.Resp) {
                SendAuth.Resp authResp = (SendAuth.Resp) resp;
                switch (authResp.errCode) {
//                    ERR_OK = 0(用户同意) ERR_AUTH_DENIED = -4（用户拒绝授权） ERR_USER_CANCEL = -2（用户取消）
                    case SendAuth.Resp.ErrCode.ERR_OK:
//                        String code = authResp.code;
//                        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=" + code + "&grant_type=authorization_code";
//                        Utils.getHttpResponse();
                        break;
                    case SendAuth.Resp.ErrCode.ERR_AUTH_DENIED:
                    case SendAuth.Resp.ErrCode.ERR_USER_CANCEL:
                        break;
                    default:
                        break;
                }
            }
        }
        finish();
    }
}