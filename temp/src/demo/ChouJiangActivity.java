package com.wyw.ljtds.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.HomeBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.StringUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2017/5/1 0001.
 */

@ContentView(R.layout.activity_webview)
public class ChouJiangActivity extends BaseActivity {
    @ViewInject(R.id.webview)
    private WebView webView;
    @ViewInject(R.id.activity_webview_img_err)
    private ImageView imgErr;
    @ViewInject(R.id.header_title)
    private TextView header_title;
    private String url = AppConfig.WS_BASE_HTML_URL + "choujiang.html";
    private boolean loadError = false;


    @Event(value = {R.id.header_return})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_return:
                webView.loadUrl(url);
                break;

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        header_title.setText("抽奖活动");

        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                webView.setVisibility(View.GONE);
                ChouJiangActivity.this.setLoding(ChouJiangActivity.this, false);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                closeLoding();
                if (loadError) {
                    imgErr.setVisibility(View.VISIBLE);
                    loadError = false;
                    return;
                }
                imgErr.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                loadError = true;
            }
        });


        //响应js发起的抽奖请求
        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void doReq() {
                Log.e(AppConfig.ERR_TAG, "I am Android");
                new BizDataAsyncTask<String>() {

                    @Override
                    protected String doExecute() throws ZYException, BizFailure {
                        return HomeBiz.chouJiang();
                    }

                    @Override
                    protected void onExecuteSucceeded(String s) {
                        closeLoding();
                        Log.e(AppConfig.ERR_TAG, "result:" + s);
                        webView.loadUrl("javascript:doRes('" + s + "');");
                    }

                    @Override
                    protected void OnExecuteFailed() {
                        closeLoding();
                    }
                }.execute();
                setLoding(ChouJiangActivity.this, false);
//                webView.loadUrl("javascript:doRes('" + msg + "')");
//                webView.loadUrl("javascript:doRes('load javascript fun');");
            }
        }, "andObj");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress >= 100) {
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                Log.e(AppConfig.ERR_TAG, title);
                //判断标题 title 中是否包含有“error”字段，如果包含“error”字段，则设置加载失败，显示加载失败的视图
                if (!StringUtils.isEmpty(title) && title.toLowerCase().contains("error")) {
                    loadError = true;
                }
            }
        });

//        webView.loadDataWithBaseURL("fake://not/needed", str, "text/html", "utf-8", null);
        webView.loadUrl(url);
    }

    public static Intent getIntent(Context ctx, String flg) {
        Intent it = new Intent(ctx, ChouJiangActivity.class);
        return it;
    }
}
