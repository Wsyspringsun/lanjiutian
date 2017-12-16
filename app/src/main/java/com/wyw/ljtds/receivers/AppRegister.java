package com.wyw.ljtds.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wyw.ljtds.config.AppConfig;

/**
 * Created by wsy on 17-10-26.
 */

public class AppRegister extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final IWXAPI api = WXAPIFactory.createWXAPI(context, null);

        Log.e(AppConfig.ERR_TAG, "...........registerApp WEIXIN_APP_ID");
        // 注册微信
        api.registerApp(AppConfig.WEIXIN_APP_ID);
    }
}
