package com.wyw.ljtds.config;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2016-10-05.
 */
public class AppConfig {
    public static final String TERMINALTYPE = "0";
    public static final float MAP_ZOOM = 18.0f;//地图 缩放比例尺
    public static final String ERR_TAG = "err";
    public static final String ERR_EXCEPTION = "www.ljt.com.exeption";
    public static final String GROUP_LJT = "sxljt";
    public static final String GROUP_LIFE = "ljtlife";

    /**
     * 百度服务
     */
    public static final long SERVICEID = 158684;//鹰眼轨迹

    // 轨迹（专有参数）
    public static String CHAT_XN_SELLERID = "lj_1001";// 商户id,平台版企业(B2B2C企业)使用此参数，B2C企业此参数传""
    //客服
    public static String CHAT_XN_LJT_TITLE2 = "蓝九天药师";// 客服组默认名称
    public static String CHAT_XN_LJT_SETTINGID2 = "lj_1000_1495596285901";//蓝九天药师
    public static String CHAT_XN_LJT_TITLE1 = "蓝九天客服";// 客服组默认名称
    public static String CHAT_XN_LJT_SETTINGID1 = "lj_1000_1493167191869";//客服
    public static final String ADDRESS_LOCATION_SEP = "\\|";
    public static final String LJG_TEL = "0356-5681888";
    public static final int DEFAULT_INDEX_FRAGMENT = 2;
    //原来经过验证的
    public static final String WEIXIN_APP_ID = "wxc245fe43d076d9d3";//微信appid
    //Web端使用的
//    public static final String WEIXIN_APP_ID = "wx13f4524a2a5e1cdf";//微信appid
    public static final int OUT_TIME = 22000;
    public static final int TAG_TRACE = 1;
    public static final int PHONE_VALIDCODE_TEIMER = 60;//短信验证码重发间隔时间  单位：秒
    //是否测试环境
    private static int TAG_DOMAIN = 3;
    public static String WEB_APP_URL = "";
    public static String WEB_DOMAIN = "";
    public static String WS_BASE_URL = "";


    public static String WS_BASE_HTML_URL = "";
    public static String WS_BASE_JSP_URL = "";
    public static String APP_UPDATE_URL = "";
    //    public static String IMAGE_PATH = WEB_DOMAIN + "/upload/images";
    public static String IMAGE_PATH_LJT = "http://www.lanjiutian.com/upload/images";
    public static int MAx_UPLOAD_IMG_CNT = 5;

    public static String IMAGE_PATH_LJT_ECOMERCE;
    public static int THUMB_SIZE = 90;//微信分享要求的图片压缩

    static {
        switch (TAG_DOMAIN) {
            case 1:
                WEB_DOMAIN = "http://192.168.2.104:8080";
                WEB_APP_URL = "http://www.lanjiutian.com/mobile";
//                WEB_APP_URL = "http://192.168.2.114:8080/emobile";
                WS_BASE_URL = WEB_DOMAIN + "/e-commerce_platform_WebService" + "/services/";
                WS_BASE_HTML_URL = WEB_DOMAIN + "/e-commerce_platform_WebService" + "/html/";
                WS_BASE_JSP_URL = WEB_DOMAIN + "/e-commerce_platform_WebService" + "/jsp/";
                APP_UPDATE_URL = WEB_DOMAIN + "/e-commerce_platform_WebService/version.json";
//                IMAGE_PATH_LJT = "http://192.168.3.88/upload/images";
                IMAGE_PATH_LJT = "http://www.lanjiutian.com/upload/images";
                IMAGE_PATH_LJT_ECOMERCE = "http://www.lanjiutian.com";
                break;
            case 2:
//                WEB_DOMAIN = "http://192.168.3.88";
                WEB_DOMAIN = "http://csbj.lanjiutian.com";
                WEB_APP_URL = WEB_DOMAIN + "/mobile";
                WS_BASE_URL = WEB_DOMAIN + "/WebService" + "/services/";
                WS_BASE_HTML_URL = WEB_DOMAIN + "/WebService" + "/html/";
                WS_BASE_JSP_URL = WEB_DOMAIN + "/WebService" + "/jsp/";
                APP_UPDATE_URL = WEB_DOMAIN + "/WebService/version.json";
//                IMAGE_PATH_LJT = "http://www.lanjiutian.com/upload/images";
                IMAGE_PATH_LJT_ECOMERCE = "http://www.lanjiutian.com";
                IMAGE_PATH_LJT = WEB_DOMAIN + "/upload/images";
                break;
            case 3:
                //正式环境
                WEB_APP_URL = "http://www.lanjiutian.com/mobile";
                WEB_DOMAIN = "http://www.lanjiutian.com";
                WS_BASE_URL = WEB_DOMAIN + "/WebService" + "/services/";
                APP_UPDATE_URL = WEB_DOMAIN + "/WebService/version.json";
                WS_BASE_HTML_URL = WEB_DOMAIN + "/WebService" + "/html/";
                WS_BASE_JSP_URL = WEB_DOMAIN + "/WebService" + "/jsp/";
                IMAGE_PATH_LJT = "http://www.lanjiutian.com/upload/images";
                IMAGE_PATH_LJT_ECOMERCE = "http://www.lanjiutian.com";
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
