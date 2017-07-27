package com.wyw.ljtds.ui.user.help;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.webkit.WebView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.ui.base.BaseActivity;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_web_show)
public class WebShowActivity extends BaseActivity {
    @ViewInject(R.id.wv_html)
    WebView mWvHtml;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mWvHtml.loadUrl(AppConfig.WS_BASE_HTML_URL);
    }

    @Event(value = {R.id.header_return})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_return:
                finish();
                break;
        }
    }

}
