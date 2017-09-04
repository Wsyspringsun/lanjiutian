package com.wyw.ljtds.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.ui.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/5/1 0001.
 */

@ContentView(R.layout.activity_webview)
public class ActivityHomeWeb extends BaseActivity {
    @ViewInject(R.id.webview)
    private WebView webView;
    @ViewInject(R.id.header_title)
    private TextView header_title;


    @Event(value = {R.id.header_return})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_return:
                finish();
                break;

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        header_title.setText("新闻公告");

        WebSettings setting = webView.getSettings();
        setting.setDomStorageEnabled(true);
        setting.setLoadWithOverviewMode(true);
        setting.setJavaScriptEnabled(true);
        setting.setUseWideViewPort(true);
        setting.setBuiltInZoomControls(true);
        setting.setSupportZoom(true);

        String html = getIntent().getStringExtra(AppConfig.IntentExtraKey.Home_News);
//        DecimalFormat format=new DecimalFormat("#%");//format格式化百分比
//        String s="<head><style type='text/css'>img{width:"+format.format(1)+";height:auto}</style></head>";//这里的100%  必须用这种方式
//        String str=html.replace("<head></head>",s);//替换
        String str = "<style>img{width:100%;display:block;}</style>\n<!DOCTYPE html>\n" +
                "<html>\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no\" />" + html + "</html>";
        Log.e(AppConfig.ERR_TAG, str);
        webView.loadDataWithBaseURL("fake://not/needed", str, "text/html", "utf-8", null);
    }
}
