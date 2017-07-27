package com.wyw.ljtds.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2017/5/17 0017.
 */
@ContentView(R.layout.activity_webview)
public class ActivityWebView extends BaseActivity {
    public static final String tag_tutorial = "com.wyw.ljtds.ui.base.ActivityWebView";
    @ViewInject(R.id.webview)
    private WebView webView;
    @ViewInject(R.id.header_title)
    private TextView header_title;

    public static final String TAG_HELP_CAT = "com.wyw.ljtds.ui.base.ActivityWebView.tag_help_cat";
    public static final int CAT_QUESTION = 1;
    public static final int CAT_LOGISTIC = 2;
    public static final int CAT_TUTORIAL = 3;

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
        header_title.setText("帮助中心");

        WebSettings setting = webView.getSettings();
        setting.setDomStorageEnabled(true);
        setting.setLoadWithOverviewMode(true);
        setting.setJavaScriptEnabled(true);
        setting.setUseWideViewPort(true);
        setting.setBuiltInZoomControls(true);
        setting.setSupportZoom(true);

        String url = AppConfig.WS_BASE_HTML_URL;
//        int cat = getIntent().getIntExtra(TAG_HELP_CAT,0);
//        switch (cat) {
//            case CAT_LOGISTIC:
//                url += "";
//                break;
//            case CAT_QUESTION:
////        webView.loadUrl( "http://192.168.2.110:8080//e-commerce_platform_WebService/agreement.html" );
////        webView.loadUrl( "http://192.168.2.110:8080//e-commerce_platform_WebService/agreement.html" );
//                url += "";
//                break;
//            default:
//                url += "goBuy";
//                break;
//        }
        url += "help-center.html";
        webView.loadUrl(url);
    }
}
