package com.wyw.ljtds.config;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2016-10-05.
 */
public class AppConfig {
    public static final String ERR_TAG = "www.ljt.com.err";
    //是否测试环境
    private static boolean test = true;
    public static final String WS_BASE_URL = test ? "http://192.168.2.110:8080/e-commerce_platform_WebService/services/" : "http://www.lanjiutian.com/WebService/services/";
    public static final String IMAGE_PATH = test ? "http://192.168.2.110:8080/upload/images" : "http://www.lanjiutian.com/upload/images";
    public static final String IMAGE_PATH_LJT = "http://www.lanjiutian.com/upload/images";
    // 命名空间
    public static final String WS_NAME_SPACE = "http://impl.service.ds.ljt.com";
    //APP更新路径
    public static final String APP_UPDATE_URL = test ? "http://192.168.2.110:8080/e-commerce_platform_WebService/version.json" : "http://www.lanjiutian.com/WebService/version.json";

    public static class AppAction {
        /**
         * 通用action前缀
         */
        public static final String Base_ACTION_PREFIX = "ljtds.action.";

        /**
         * token过期
         */
        public static final String ACTION_TOKEN_EXPIRE = Base_ACTION_PREFIX
                + "token.expire";

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
    public static int currSel = 0;

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
