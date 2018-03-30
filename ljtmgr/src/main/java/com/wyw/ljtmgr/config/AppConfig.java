package com.wyw.ljtmgr.config;

/**
 * Created by Administrator on 2017/7/3 0003.
 */

public class AppConfig {
    //显示日志
    public static final String TAG_ERR = "com.wyw.ljtwl.err";
    //超时时间
    public static final int TIME_OUT = 6000;
    //启动步骤
    public static final int PHASE_START = 0; //权限
    public static final int PHASE_PERMISSION = 1 << 0; //权限
    public static final int PHASE_NET_OK = 1 << 1;  //网络正常
    public static final int PHASE_LOGIN_IN = 1 << 2; //登录完成
    public static final int PHASE_SERVER_OK = 1 << 3; //服务端正常
    public static final String PAGE_NUM = "10";
    public static final float MAP_ZOOM = 18.0f;//地图 缩放比例尺
    public static final int SEQUENCE = 1000;


    //    private static boolean test = false;
    private static int TAG_DOMAIN = 3;
    public static String WEB_DOMAIN = "";
    public static String IMAGE_PATH_LJT = "http://www.lanjiutian.com/upload/images";

    static {
        switch (TAG_DOMAIN) {
            case 1:
                WEB_DOMAIN = "http://192.168.2.114:8080/ljt_mobile_store";
                IMAGE_PATH_LJT = "http://cs.lanjiutian.com/upload/images";
                break;
            case 2:
                WEB_DOMAIN = "http://cs.lanjiutian.com/ljt_mobile_store";
                IMAGE_PATH_LJT = "http://cs.lanjiutian.com/upload/images";
                break;
            case 3:
                WEB_DOMAIN = "http://www.lanjiutian.com/ljt_mobile_store";
                IMAGE_PATH_LJT = "http://www.lanjiutian.com/upload/images";
                break;
        }
    }


/*
    public static class IntentExtraKey {
        public static final int LODING_CONTEXT = 12;//hander 的全局loding动画
        public static final int RESULT_OK = 01;//activityforresult  成功code
        public static final int RESULT_FAILURE = 02;//activityforresult  失败code
        public static final String MAIN_GO_TO = "main_go_to";//mainactivity 跳转
    }
*/


}
