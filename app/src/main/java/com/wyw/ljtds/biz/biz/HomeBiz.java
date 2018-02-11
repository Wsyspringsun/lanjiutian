package com.wyw.ljtds.biz.biz;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.model.HomePageModel;
import com.wyw.ljtds.model.HomePageModel1;
import com.wyw.ljtds.model.RecommendModel;
import com.wyw.ljtds.model.SingleCurrentUser;

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
        SoapProcessor ksoap = new SoapProcessor("Service", "homePage", false);

        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(element, HomePageModel.class);
    }


    /**
     * 生活馆首页
     *
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static HomePageModel1 getHome1() throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "onlineMallHomePage", false);

        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(element, HomePageModel1.class);
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
    public static List<RecommendModel> getRecommend() throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "getRecommandComm", false);
        String lat = SingleCurrentUser.defaultLat + "", lng = SingleCurrentUser.defaultLng + "";
        if (SingleCurrentUser.location != null) {
            lat = SingleCurrentUser.location.getLatitude() + "";
            lng = SingleCurrentUser.location.getLongitude() + "";
        }
        ksoap.setProperty("lat", lat, SoapProcessor.PropertyType.TYPE_STRING);
        ksoap.setProperty("lng", lng, SoapProcessor.PropertyType.TYPE_STRING);
//        ksoap.setProperty("token", token, SoapProcessor.PropertyType.TYPE_STRING);
//        ksoap.setProperty("orderId", orderId, SoapProcessor.PropertyType.TYPE_STRING);

        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<RecommendModel>> tt = new TypeToken<List<RecommendModel>>() {
        };
        List<RecommendModel> fs = gson.fromJson(element, tt.getType());
        return fs;
    }

    public static String chouJiang() throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "choJiang", true);
        ksoap.request();
        String rlt = ksoap.requestStr();
        return rlt;
//        Gson gson = new GsonBuilder().create();

//        TypeToken<List<RecommendModel>> tt = new TypeToken<List<RecommendModel>>() {
//        };
//        List<RecommendModel> fs = gson.fromJson(element, tt.getType());
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
