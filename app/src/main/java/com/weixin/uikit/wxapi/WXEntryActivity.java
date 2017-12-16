package com.weixin.uikit.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wyw.ljtds.config.AppConfig;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;

    private Button gotoBtn, regBtn, launchBtn, checkBtn, scanBtn;

    // IWXAPI ÊÇµÚÈý·½appºÍÎ¢ÐÅÍ¨ÐÅµÄopenapi½Ó¿Ú
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.entry);

        // Í¨¹ýWXAPIFactory¹¤³§£¬»ñÈ¡IWXAPIµÄÊµÀý
        api = WXAPIFactory.createWXAPI(this, AppConfig.WEIXIN_APP_ID, false);
        api.registerApp(AppConfig.WEIXIN_APP_ID);
        //×¢Òâ£º
        //µÚÈý·½¿ª·¢ÕßÈç¹ûÊ¹ÓÃÍ¸Ã÷½çÃæÀ´ÊµÏÖWXEntryActivity£¬ÐèÒªÅÐ¶ÏhandleIntentµÄ·µ»ØÖµ£¬Èç¹û·µ»ØÖµÎªfalse£¬ÔòËµÃ÷Èë²Î²»ºÏ·¨Î´±»SDK´¦Àí£¬Ó¦finishµ±Ç°Í¸Ã÷½çÃæ£¬±ÜÃâÍâ²¿Í¨¹ý´«µÝ·Ç·¨²ÎÊýµÄIntentµ¼ÖÂÍ£ÁôÔÚÍ¸Ã÷½çÃæ£¬ÒýÆðÓÃ»§µÄÒÉ»ó
        try {
            api.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        api.handleIntent(intent, this);
    }

    // Î¢ÐÅ·¢ËÍÇëÇóµ½µÚÈý·½Ó¦ÓÃÊ±£¬»á»Øµ÷µ½¸Ã·½·¨
    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                break;
            default:
                break;
        }
        Log.e(AppConfig.ERR_TAG, req.getType() + "");
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e(AppConfig.ERR_TAG, resp.getType() + "");
    }

}
