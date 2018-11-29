package com.wyw.ljtds.ui.goods;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.CommodityDetailsModel;
import com.wyw.ljtds.model.MedicineDetailsModel;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.ui.user.manage.ActivityBigImage;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/1/4 0004.
 */

@ContentView(R.layout.fragment_goods_info_details)
public class FragmentGoodsDetails extends BaseFragment {
    @ViewInject(R.id.webviews)
    private WebView webView;
    DecimalFormat format = new DecimalFormat("#%");//format格式化百分比

    public static FragmentGoodsDetails newInstance() {
        FragmentGoodsDetails fragment = new FragmentGoodsDetails();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        Log.e(AppConfig.ERR_TAG, "FragmentGoodsDetails  onCreateView ........");
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        WebSettings setting = webView.getSettings();
        setting.setDomStorageEnabled(true);
        setting.setLoadWithOverviewMode(true);
        setting.setJavaScriptEnabled(true);
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        setting.setUseWideViewPort(true);
        setting.setBuiltInZoomControls(true);
        setting.setSupportZoom(true);

    }

    public void bindData2View(String html) {
        String str = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no\" />\n" +
                "<style type='text/css'>img{width:100%;margin-top:-5px;}</style>" +
                "</head>\n" +
                "<body>\n" + html +
                "<script src=\"http://code.jquery.com/jquery-1.11.3.min.js\"></script>"+
                "<script type='text/javascript'> $(function(){ $('img').click(function(){ var me = $(this); var data = me.attr('src'); alert(data); if (typeof (andObj) != 'undefined' && andObj != null) { andObj.doReq(data); return false; } return true; }); }); </script>"+
                "\n</body>\n" +
                "</html>";
        /*//响应js发起的抽奖请求
        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void doReq(String json) {
                startActivity(ActivityBigImage.getIntent(getActivity(), json));
            }
        }, "andObj");*/

        webView.loadDataWithBaseURL("fake://not/needed", str, "text/html", "utf-8", null);
    }
}
