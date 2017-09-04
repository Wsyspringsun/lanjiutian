package com.wyw.ljtwl.config;

/**
 * Created by Administrator on 2017/7/3 0003.
 */

public class AppConfig {
    public static final String TAG_ERR = "com.wyw.ljtwl.err";
//        private static boolean test=false;
    private static boolean test = true;
    public static String WEB_DOMAIN = test ? "http://192.168.2.114:8080/ljt_mobile_logistics" : "http://www.lanjiutian.com";
    public static final String IMAGE_PATH_LJT = "http://www.lanjiutian.com/upload/images";

    public static class IntentExtraKey {
        public static final int LODING_CONTEXT = 12;//hander 的全局loding动画
        public static final int RESULT_OK = 01;//activityforresult  成功code
        public static final int RESULT_FAILURE = 02;//activityforresult  失败code
        public static final String MAIN_GO_TO = "main_go_to";//mainactivity 跳转
    }
}
