package com.wyw.ljtds.config;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2016-10-05.
 */
public class AppConfig {
    public static final float MAP_ZOOM = 18.0f;//地图 缩放比例尺
    public static final String ERR_TAG = "err";
    public static final String ERR_EXCEPTION = "www.ljt.com.exeption";
    public static final String GROUP_LJT = "sxljt";
    public static final String GROUP_LIFE = "ljtlife";
    //客服
    public static String CHAT_XN_LJT_TITLE = "蓝九天药师";// 客服组默认名称
    public static String CHAT_XN_LJT_SETTINGID2 = "lj_1000_1495596285901";
    public static final String ADDRESS_LOCATION_SEP = "\\|";
    public static final String LJG_TEL = "0356-5681888";
    public static final int DEFAULT_INDEX_FRAGMENT = 2;
    public static final String WEIXIN_APP_ID = "wxc245fe43d076d9d3";//微信appid
    public static final int OUT_TIME = 22000;
    public static final int TAG_TRACE = 1;
    //是否测试环境
    private static int TAG_DOMAIN = 2 ;
    public static String WEB_APP_URL = "";
    public static String WEB_DOMAIN = "";
    public static String WS_BASE_URL = "";


    public static String WS_BASE_HTML_URL = "";
    public static String WS_BASE_JSP_URL = "";
    public static String APP_UPDATE_URL = "";
    //    public static String IMAGE_PATH = WEB_DOMAIN + "/upload/images";
    public static String IMAGE_PATH_LJT = "http://www.lanjiutian.com/upload/images";
    public static int MAx_UPLOAD_IMG_CNT = 5;

    static {
        switch (TAG_DOMAIN) {
            case 1:
                WEB_DOMAIN = "http://192.168.2.102:8080";
                WS_BASE_URL = WEB_DOMAIN + "/e-commerce_platform_WebService" + "/services/";
                WS_BASE_HTML_URL = WEB_DOMAIN + "/e-commerce_platform_WebService" + "/html/";
                WS_BASE_JSP_URL = WEB_DOMAIN + "/e-commerce_platform_WebService" + "/jsp/";
                APP_UPDATE_URL = WEB_DOMAIN + "/e-commerce_platform_WebService/version.json";
                WEB_APP_URL = "http://cs.lanjiutian.com/mobile";
//                IMAGE_PATH_LJT = "http://cs.lanjiutian.com/ecommerce/images";
                break;
            case 2:
                WEB_APP_URL = "http://cs.lanjiutian.com/mobile";
                WEB_DOMAIN = "http://cs.lanjiutian.com";
                WS_BASE_URL = WEB_DOMAIN + "/WebService" + "/services/";
                WS_BASE_HTML_URL = WEB_DOMAIN + "/WebService" + "/html/";
                WS_BASE_JSP_URL = WEB_DOMAIN + "/WebService" + "/jsp/";
                APP_UPDATE_URL = WEB_DOMAIN + "/WebService/version.json";
                IMAGE_PATH_LJT = "http://cs.lanjiutian.com/upload/images";
//                IMAGE_PATH_LJT = "http://cs.lanjiutian.com/ecommerce/images";
                break;
            case 3:
                WEB_APP_URL = "http://www.lanjiutian.com/mobile";
                WEB_DOMAIN = "http://app.lanjiutian.com";
                WS_BASE_URL = WEB_DOMAIN + "/WebService" + "/services/";
                WS_BASE_HTML_URL = WEB_DOMAIN + "/WebService" + "/html/";
                WS_BASE_JSP_URL = WEB_DOMAIN + "/WebService" + "/jsp/";
//                IMAGE_PATH_LJT = "http://www.lanjiutian.com/ecommerce/images";
                break;
        }
    }

    boolean debug = false;
    public static String BLANK_URL = AppConfig.WS_BASE_HTML_URL + "blank.html";
    // 命名空间
    public static final String WS_NAME_SPACE = "http://impl.service.ds.ljt.com";
    //APP更新路径

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
    public static int currSel = AppConfig.DEFAULT_INDEX_FRAGMENT;
    public static final int DEFAULT_CURRSEL = 1;


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
        public static final String LOGIN_FROM_MAIN = "login_from_main";//从mainactivity登陆
        public static final int RESULT_OK = 0356;//activityforresult  成功code
        public static final int RESULT_FAILURE = 2;//activityforresult  失败code
        public static final String Home_News = "home_news_web";
        public static final String CAT_FROM = "CAT_FROM";
    }

}
