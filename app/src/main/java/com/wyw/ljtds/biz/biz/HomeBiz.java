package com.wyw.ljtds.biz.biz;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.model.HomePageModel;
import com.wyw.ljtds.model.HomePageModel1;
import com.wyw.ljtds.model.RecommendModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/1 0001.
 */

public class HomeBiz extends BaseBiz {
    /**
     * 医药馆首页
     *
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static HomePageModel getHome() throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor( "Service", "homePage", false );

        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson( element, HomePageModel.class );
    }


    /**
     * 生活馆首页
     *
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static HomePageModel1 getHome1() throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor( "Service", "onlineMallHomePage", false );

        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson( element, HomePageModel1.class );
    }


    /**
     * 推荐商品
     *
     * @param token
     * @param orderId
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static List<RecommendModel> getRecommend(String token, String orderId) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor( "Service", "getRecommandComm", false );

        ksoap.setProperty( "token", token, SoapProcessor.PropertyType.TYPE_STRING );
        ksoap.setProperty( "orderId", orderId, SoapProcessor.PropertyType.TYPE_STRING );

        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<RecommendModel>> tt = new TypeToken<List<RecommendModel>>() {
        };
        List<RecommendModel> fs = gson.fromJson( element, tt.getType() );
        List<RecommendModel> bms = new ArrayList<RecommendModel>();
        bms.addAll( fs );
        return bms;
    }

    /**
     * 检测新版本
     *
     * @throws ZYException
     */
    public static void detectNewVersion() throws ZYException {
//        try {
//            String result = HttpUtil.postRespString(JdbConfig.VERSION_DETECTION_URL);
//            // LogUtil.e(result);
//            Gson gson = new Gson();
//            return gson.fromJson(result, VersionDescription.class);
//        } catch (Exception e) {
//            throw new ZYException();
//        }

    }

}
