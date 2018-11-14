package com.wyw.ljtds.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.HomeBiz;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.ui.goods.ActivityGoodsList;
import com.wyw.ljtds.ui.goods.ActivityLifeGoodsInfo;
import com.wyw.ljtds.ui.goods.ActivityMedicinesInfo;
import com.wyw.ljtds.ui.user.ActivityLoginOfValidCode;
import com.wyw.ljtds.ui.user.wallet.ChojiangRecActivity;
import com.wyw.ljtds.ui.user.wallet.PointShopActivity;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.ToolbarManager;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.wxapi.WXEntryActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.Map;

import static com.wyw.ljtds.ui.home.HuoDongActivity.FLG_HUODONG_CHOJIANG;
import static com.wyw.ljtds.ui.home.HuoDongActivity.FLG_HUODONG_DIANZIBI;
import static com.wyw.ljtds.ui.home.HuoDongActivity.FLG_HUODONG_JIFENDUIHUAN;
import static com.wyw.ljtds.ui.home.HuoDongActivity.FLG_HUODONG_KANJIA;
import static com.wyw.ljtds.ui.home.HuoDongActivity.FLG_HUODONG_LINGYUANGOU;
import static com.wyw.ljtds.ui.home.HuoDongActivity.FLG_HUODONG_LIST;
import static com.wyw.ljtds.ui.home.HuoDongActivity.FLG_HUODONG_MANZENG;
import static com.wyw.ljtds.ui.home.HuoDongActivity.FLG_HUODONG_MIAOSHA;
import static com.wyw.ljtds.ui.home.HuoDongActivity.FLG_HUODONG_TEJIA;

/**
 * Created by wsy on 17-7-28.
 */

@ContentView(R.layout.fragment_web)
public class HuoDongFragment extends BaseFragment implements ToolbarManager.IconBtnManager {
    private static final String ARG_CATEGORY = "arg_category";
    private static final String DIALOG_WXSHARE = "DIALOG_WXSHARE";
    @ViewInject(R.id.fragment_web_webview)
    private WebView webView;
    @ViewInject(R.id.fragment_web_img_err)
    private ImageView imgErr;

    UserBiz userBiz;

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

    @Override
    public void onResume() {
        super.onResume();
        String category = getArguments().getString(ARG_CATEGORY);
        //分享了抽奖页面 赠送积分
        if (FLG_HUODONG_CHOJIANG.equals(category)) {
            if (!UserBiz.isLogined()) return;
            if (WXEntryActivity.SHARE_RESULT_OK.equals(PreferenceCache.getWXShareResult())) {
                PreferenceCache.putWXShareResult("");

                setLoding(getActivity(), false);
                new BizDataAsyncTask<String>() {
                    @Override
                    protected String doExecute() throws ZYException, BizFailure {
                        return userBiz.givePoint();
                    }

                    @Override
                    protected void onExecuteSucceeded(String s) {
                        closeLoding();
                        try {
                            Map<String, Object> rlt = GsonUtils.Json2Bean(s, HashMap.class);
                            String msg = (String) rlt.get("message");
                            ToastUtil.show(getActivity(), msg);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Utils.log("GivePointEx data:" + s);
                            Utils.log("GivePointEx err:" + ex.getMessage());
                        } finally {
                            if (webView != null)
                                webView.reload();
                        }
                    }

                    @Override
                    protected void OnExecuteFailed() {
                        closeLoding();
                    }
                }.execute();


            }

        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userBiz = UserBiz.getInstance(getActivity());
        String category = getArguments().getString(ARG_CATEGORY);
        switch (category) {
            case FLG_HUODONG_LIST:
                url = AppConfig.WS_BASE_HTML_URL + "huodonglist.html";
                break;
            case FLG_HUODONG_CHOJIANG:
                if (UserBiz.isLogined()) {
                    Utils.log("tn:" + PreferenceCache.getToken());
                    url = AppConfig.WS_BASE_JSP_URL + "chojiang.jsp?tn=" + PreferenceCache.getToken();
                } else {
                    startActivity(ActivityLoginOfValidCode.getIntent(getActivity()));
                    getActivity().finish();
                    return;
                }
                break;
            case FLG_HUODONG_MANZENG:
                url = AppConfig.WS_BASE_JSP_URL + "mansong.jsp";
                break;
            case FLG_HUODONG_TEJIA:
                url = AppConfig.WS_BASE_JSP_URL + "tejia.jsp";
                break;
            case FLG_HUODONG_MIAOSHA:
                url = AppConfig.WS_BASE_JSP_URL + "xianshiqiang.jsp";
                break;
            case FLG_HUODONG_KANJIA:
                if (UserBiz.isLogined()) {
//                    url = AppConfig.WEB_APP_URL + "/activity/bargain.html?tn=" + PreferenceCache.getToken();
                    url = AppConfig.WEB_APP_URL + "/activity/bargain.html?s=app&token=" + PreferenceCache.getToken();
                } else {
                    ToastUtil.show(getActivity(), "请登录");
                    startActivity(ActivityLoginOfValidCode.getIntent(getActivity()));
                    getActivity().finish();
                    return;
                }
//                url = AppConfig.WS_BASE_JSP_URL + "dianzibi.jsp";
                break;
            case FLG_HUODONG_DIANZIBI:
                url = AppConfig.WS_BASE_JSP_URL + "dianzibi.jsp";
                break;
            case FLG_HUODONG_LINGYUANGOU:
                url = AppConfig.WEB_APP_URL + "/lifeSearch.html?s=app&keypress=0元购";

//                startActivity(ActivityGoodsList.getIntent(getActivity(), "", "0元购"));
//                getActivity().finish();
                break;
            case FLG_HUODONG_JIFENDUIHUAN:
                if (UserBiz.isLogined()) {
//                    url = AppConfig.WEB_APP_URL + "/activity/bargain.html?tn=" + PreferenceCache.getToken();
                    startActivity(PointShopActivity.getIntent(getActivity()));
                    getActivity().finish();
                } else {
                    ToastUtil.show(getActivity(), "请登录");
                    startActivity(ActivityLoginOfValidCode.getIntent(getActivity()));
                    getActivity().finish();
                    return;
                }
                break;
            default:
                break;
        }

    }

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


        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 如下方案可在非微信内部WebView的H5页面中调出微信支付
                if (url.startsWith("weixin://wap/pay?")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }

                return super.shouldOverrideUrlLoading(view, url);
//                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                webView.setVisibility(View.GONE);
                imgErr.setVisibility(View.GONE);
                loadError = false;
                if (!isAdded()) return;
                ((BaseActivity) getActivity()).setLoding(getActivity(), false);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (!isAdded()) return;
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
//                if (newProgress >= 100) {
//                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                Utils.log("webview title:" + title);
                //判断标题 title 中是否包含有“error”字段，如果包含“error”字段，则设置加载失败，显示加载失败的视图
                if (!StringUtils.isEmpty(title) && title.toLowerCase().contains("error")) {
                    loadError = true;
                }
            }
        });


//        webView.loadDataWithBaseURL("fake://not/needed", str, "text/html", "utf-8", null);
        if (!StringUtils.isEmpty(url)) {
            Utils.log("webview url:" + url);
            webView.loadUrl(url);
        }
        return view;
    }

    private void handleJsEvent(String json) {
        String category = getArguments().getString(ARG_CATEGORY);
        Log.e(AppConfig.ERR_TAG, "category:" + category);
        Gson gson = new GsonBuilder().create();
        HashMap map = null;
        Intent it = null;
        switch (category) {
            case FLG_HUODONG_LIST:
                it = HuoDongActivity.getIntent(getActivity(), json);
                startActivity(it);
                break;
            case FLG_HUODONG_CHOJIANG:
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
                    it = ChojiangRecActivity.getIntent(getActivity());
                    startActivity(it);
                }

//                setLoding(getActivity(), false);
                break;
            case FLG_HUODONG_MANZENG:
            case FLG_HUODONG_TEJIA:
            case FLG_HUODONG_MIAOSHA:
            case FLG_HUODONG_DIANZIBI:
            case FLG_HUODONG_LINGYUANGOU:
                //进入商品详情
                map = gson.fromJson(json, HashMap.class);
                Log.e(AppConfig.ERR_TAG, "json:" + json);
                String commId = map.get("commid") + "";
                String isMedcine = map.get("isMedicine") + "";
                if ("2".equals(isMedcine)) {
                    //医药馆产品
                    it = ActivityMedicinesInfo.getIntent(getActivity(), commId, (String) map.get("logistId"));
                } else {
                    //生活馆产品
                    it = ActivityLifeGoodsInfo.getIntent(getActivity(), commId);
                }
                startActivity(it);
                break;
            case FLG_HUODONG_KANJIA:
                //调用微信分享
                map = gson.fromJson(json, HashMap.class);
                Log.e(AppConfig.ERR_TAG, "json:" + json);
                String imgUrl = map.get("imgUrl") + "";
                String link = map.get("link") + "";
                Utils.log("webview link:" + link);
                String title = map.get("title") + "";
                String desc = map.get("desc") + "";
                Utils.wechatShare(getActivity(), title, desc, imgUrl, link);
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
            case 3:
                String category = getArguments().getString(ARG_CATEGORY);
                if (FLG_HUODONG_CHOJIANG.equals(category)) {
                    v.setVisibility(View.VISIBLE);
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String imgUrl = "empty";
                            String link = AppConfig.WS_BASE_JSP_URL + "chojiang.jsp";
                            String desc = "蓝九天积分抽大奖，快来试手气！";
                            Utils.wechatShare(getActivity(), "积分抽奖", desc, imgUrl, link);
                        }
                    });
                }

                break;
            default:
                break;
        }
    }
}
