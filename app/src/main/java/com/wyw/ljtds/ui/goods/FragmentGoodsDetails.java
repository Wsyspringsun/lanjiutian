package com.wyw.ljtds.ui.goods;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.model.CommodityDetailsModel;
import com.wyw.ljtds.model.MedicineDetailsModel;
import com.wyw.ljtds.ui.base.BaseFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/1/4 0004.
 */

@ContentView( R.layout.fragment_goods_info_details )
public class FragmentGoodsDetails extends BaseFragment {
    @ViewInject(R.id.webviews)
    private WebView webView;



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );

        WebSettings setting = webView.getSettings();
        setting.setDomStorageEnabled( true );
        setting.setLoadWithOverviewMode( true );
        setting.setJavaScriptEnabled( true );
        setting.setLayoutAlgorithm( WebSettings.LayoutAlgorithm.SINGLE_COLUMN );
        setting.setUseWideViewPort( true );
        setting.setBuiltInZoomControls( true );
        setting.setSupportZoom( true );

//        MedicineDetailsModel medicineDetailsModel = activity.getmodel();
//        try {
//            Log.e( "*********", medicineDetailsModel.getWARENAME() );
//
//        } catch (Exception e) {
//            Log.e( "*********", e.toString() );
//        }
    }

    public void getmodel(MedicineDetailsModel model){
        String html = model.getHTML_PATH();
        DecimalFormat format=new DecimalFormat("#%");//format格式化百分比
            String str = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n"+
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no\" />\n"+
                    "<style type='text/css'>img{width:"+format.format(1)+";height:auto}</style>"+
                    "</head>\n"+
                    "<body>\n"+ html+"\n</body>\n" +
                    "</html>";

        Log.e( "html",str );
            webView.loadDataWithBaseURL( "fake://not/needed", str, "text/html", "utf-8", null );
    }

    public void getmodel(CommodityDetailsModel model){
        String html = model.getHtmlPath();
        DecimalFormat format=new DecimalFormat("#%");//format格式化百分比
        String str = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n"+
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no\" />\n"+
                "<style type='text/css'>img{width:"+format.format(1)+";height:auto}</style>"+
                "</head>\n"+
                "<body>\n"+ html+"\n</body>\n" +
                "</html>";

        Log.e( "html",str );
        webView.loadDataWithBaseURL( "fake://not/needed", str, "text/html", "utf-8", null );
    }
}
