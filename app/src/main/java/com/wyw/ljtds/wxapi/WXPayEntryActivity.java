package com.wyw.ljtds.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.ui.user.order.ActivityOrder;
import com.wyw.ljtds.utils.Utils;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.log("pay Ent onC");

        api = WXAPIFactory.createWXAPI(this, AppConfig.WEIXIN_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Utils.log("pay Ent onNewIntent");
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        Utils.log("pay Ent onReq");
    }

    @Override
    public void onResp(BaseResp resp) {
        Utils.log(" net ...  pay Ent onResp");
        Utils.log("onPayFinish, errCode = " + resp.errCode);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle(R.string.alert_tishi);
            if (resp.errCode == 0) {
                Toast.makeText(this, getString(R.string.pay_result_callback_msg, "成功"), Toast.LENGTH_SHORT).show();

//                builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
//                builder.show();
            } else if (-2 == resp.errCode) {
                Toast.makeText(this, getString(R.string.pay_result_callback_msg, "取消支付"), Toast.LENGTH_SHORT).show();
            }
            Intent it = new Intent(this, ActivityOrder.class);
            startActivity(it);
            finish();

        }
    }
}