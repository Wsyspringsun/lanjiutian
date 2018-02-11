package com.wyw.ljtds.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.HomeBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.CommodityListModel;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.base.BaseActivityFragment;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.ui.goods.ActivityGoodsInfo;
import com.wyw.ljtds.ui.goods.ActivityMedicinesInfo;
import com.wyw.ljtds.ui.user.wallet.ChojiangRecActivity;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToolbarManager;

import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wsy on 17-7-28.
 */

@ContentView(R.layout.fragment_web)
public class HuoDongFragment extends BaseFragment implements ToolbarManager.IconBtnManager {
    private static final String ARG_CATEGORY = "arg_category";
    @ViewInject(R.id.fragment_web_webview)
    private WebView webView;
    @ViewInject(R.id.fragment_web_img_err)
    private ImageView imgErr;


    //加载的路径
    private String url;
    //加载是否成功
    private boolean loadError = false;

    public static HuoDongFragment newInstance(String category) {
        HuoDongFragment fragment = new HuoDongFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    /*
     * 毫秒转化时分秒毫秒
     */


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        imgErr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.reload();
            }
        });

        String category = getArguments().getString(ARG_CATEGORY);
        switch (category) {
            case "1":
                url = AppConfig.WS_BASE_JSP_URL + "chojiang.jsp";
                break;
            case "2":
                url = AppConfig.WS_BASE_JSP_URL + "mansong.jsp";
                break;
            case "3":
                url = AppConfig.WS_BASE_JSP_URL + "tejia.jsp";
                break;
            case "4":
                url = AppConfig.WS_BASE_JSP_URL + "xianshiqiang.jsp";
                break;
            case "5":
                url = AppConfig.WS_BASE_JSP_URL + "dianzibi.jsp";
                break;
            default:
                break;
        }


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
                imgErr.setVisibility(View.GONE);
                loadError = false;
                ((BaseActivity) getActivity()).setLoding(getActivity(), false);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                ((BaseActivity) getActivity()).closeLoding();
                if (loadError) {
                    imgErr.setVisibility(View.VISIBLE);
                    return;
                }
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
            public void doReq(String json) {
                Log.e(AppConfig.ERR_TAG, "handleJsEvent....................");
                HuoDongFragment.this.handleJsEvent(json);
//                ((BaseActivity) getActivity()).closeLoding();
//                Log.e(AppConfig.ERR_TAG, "result:" + s);
//                webView.loadUrl("javascript:doRes('" + s + "');");
//                ((BaseActivity) getActivity()).setLoding(getActivity(), false);
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

        return view;
    }

    private void handleJsEvent(String json) {
        String category = getArguments().getString(ARG_CATEGORY);
        Log.e(AppConfig.ERR_TAG, "category:" + category);
        switch (category) {
            case "1":
                //抽奖
                if ("0".equals(json)) {
                    new BizDataAsyncTask<String>() {

                        @Override
                        protected String doExecute() throws ZYException, BizFailure {
                            return HomeBiz.chouJiang();
                        }

                        @Override
                        protected void onExecuteSucceeded(String s) {
                            Log.e(AppConfig.ERR_TAG, "result:" + s);
                            webView.loadUrl("javascript:doRes('" + s + "');");
                        }

                        @Override
                        protected void OnExecuteFailed() {
                            closeLoding();
                        }
                    }.execute();
                } else if ("1".equals(json)) {
                    Intent it = ChojiangRecActivity.getIntent(getActivity());
                    startActivity(it);
                }

//                setLoding(getActivity(), false);
                break;
            case "2":
            case "3":
            case "4":
                //进入商品详情
                Gson gson = new GsonBuilder().create();
                HashMap map = gson.fromJson(json, HashMap.class);
                Log.e(AppConfig.ERR_TAG, "json:" + json);
                String commId = map.get("commid") + "";
                String isMedcine = map.get("isMedicine") + "";
                Log.e(AppConfig.ERR_TAG, "isMedicine:" + isMedcine);
                Log.e(AppConfig.ERR_TAG, "commid:" + commId);
                Intent it = null;
                if ("1".equals(isMedcine)) {
                    //生活馆产品
//                    it = ActivityGoodsInfo.getIntent(getActivity(), commId);
                } else if ("2".equals(isMedcine)) {
                    //医药馆产品
//                    it = ActivityMedicinesInfo.getIntent(getActivity(), commId);
                }
                startActivity(it);
                break;
            default:
                Log.e(AppConfig.ERR_TAG, "request:" + json);
                break;
        }
    }

    @Override
    public void initIconBtn(View v, int position) {
        Log.e(AppConfig.ERR_TAG, "position:" + position);
        switch (position) {
            case 0:
                v.setVisibility(View.GONE);
                break;
            case 1:
                v.setVisibility(View.VISIBLE);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        webView.reload();
                    }
                });
                break;
            default:
                break;
        }
    }
}
