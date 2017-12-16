package com.wyw.ljtds.config;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2016-10-05.
 */
public class AppConfig {

    public static final String ERR_TAG = "err";
    public static final String ERR_EXCEPTION = "www.ljt.com.exeption";
    public static final String GROUP_LJT = "sxljt";
    public static final int RESPONSE_OK = 1;
    public static final String LJG_TEL = "0356-5681888";
    public static final int DEFAULT_INDEX_FRAGMENT = 2;
    public static final String WEIXIN_APP_ID = "wxc245fe43d076d9d3";//微信appid
    public static final String WEB_APP_URL = "http://www.lanjiutian.com/mobile";
    public static final long OUT_TIME = 12000;
    //是否测试环境
    public static boolean debug = true;
    //    public static boolean test = true;
    public static boolean test = false; // http://192.168.2.102:8080
    //http://124.164.246.214:8011
    public static final String WS_DOMAIN = test ? "http://192.168.2.110:8080" : "http://www.lanjiutian.com";
    public static final String VIRTUAL_PATH = debug ? "/e-commerce_platform_WebService" : "/WebService";
    public static String WS_BASE_URL = test ? WS_DOMAIN + VIRTUAL_PATH + "/services/" : "http://www.lanjiutian.com/WebService/services/";
    public static final String WS_BASE_HTML_URL = test ? WS_DOMAIN + VIRTUAL_PATH + "/html/" : "http://www.lanjiutian.com/WebService/html/";
    public static final String WS_BASE_JSP_URL = test ? WS_DOMAIN + VIRTUAL_PATH + "/jsp/" : "http://www.lanjiutian.com/WebService/jsp/";
    public static String BLANK_URL = AppConfig.WS_BASE_HTML_URL + "blank.html";
    public static final String IMAGE_PATH = test ? WS_DOMAIN + "/upload/images" : "http://www.lanjiutian.com/upload/images";
    public static final String IMAGE_PATH_LJT = "http://www.lanjiutian.com/upload/images";
    // 命名空间
    public static final String WS_NAME_SPACE = "http://impl.service.ds.ljt.com";
    //APP更新路径
    public static final String APP_UPDATE_URL = test ? WS_DOMAIN + "/e-commerce_platform_WebService/version.json" : "http://www.lanjiutian.com/WebService/version.json";

    public static class AppAction {
        /**
         * 通用action前缀
         */
        public static final String Base_ACTION_PREFIX = "ljtds.action.";

        /**
         * token过期
         */
        public static final String ACTION_TOKEN_EXPIRE = Base_ACTION_PREFIX + "token.expire";
        /**
         * 重置主界面
         */
        public static final String ACTION_RESETMAIN = Base_ACTION_PREFIX + "reset.main";

        /**
         * 地址为空
         */
        public static final String ACTION_ADDRESS_EXPIRE = Base_ACTION_PREFIX
                + "address.expire";

    }

    /**
     * 银联支付环境  00正式  01测试
     */
    public static final String UPPAY_MODE = "00";

    /**
     * 选中哪个主菜单
     */
    public static int currSel = 2;
    public static final int DEFAULT_CURRSEL = 2;


    /**
     * 默认加载数量
     */
    public static final int DEFAULT_PAGE_COUNT = 20;

    /**
     * 本地存储的根路径
     */
    public static final String EXT_STORAGE_ROOT = Environment
            .getExternalStorageDirectory().getAbsolutePath();

    /**
     * 本地存储根目录名
     */
    public static final String CACHE_ROOT_NAME = EXT_STORAGE_ROOT + File.separator + "LJT";

    /**
     * 本地存储缓存根目录名
     */
    public static final String CACHE_ROOT_CACHE_NAME = CACHE_ROOT_NAME + "cache";

    /**
     * 本地存储图片根目录名
     */
    public static final String CACHE_PIC_ROOT_NAME = CACHE_ROOT_NAME + "image";

    /**
     * 数据库目录
     */
    public static final String CACHE_DB_ROOT_NAME = CACHE_ROOT_NAME + "db";

    //生活馆
    public static final Integer LIFE = 0;
    //医药馆
    public static final Integer MEDICINE = 1;

    public static class IntentExtraKey {
        public static final String BOTTOM_MENU_INDEX = "bottom_menu_index";//全局index
        public static final String PHONE_NUMBER = "phone_number";//手机号码
        public static final int LODING_CONTEXT = 12;//hander 的全局loding动画
        public static final String ORDER_INDEX = "order_index";
        public static final String ADDRESS_FROM = "adress_from";//判断从哪里进入的省市列表
        public static final String MEDICINE_INFO_FROM = "medicine_info_from";//进入商品详情页时的key  生活馆 医药馆
        public static final String MEDICINE_INFO_ID = "medicine_info_id";//进入商品详情页时的id   生活馆 医药馆
        public static final String LOGIN_FROM_MAIN = "login_from_main";//从mainactivity登陆
        public static final int RESULT_OK = 0356;//activityforresult  成功code
        public static final int RESULT_FAILURE = 2;//activityforresult  失败code
        public static final String Home_News = "home_news_web";
        public static final String CAT_FROM = "CAT_FROM";
    }

}
