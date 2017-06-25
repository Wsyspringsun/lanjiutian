package com.wyw.ljtds.config;

/**
 * Created by Administrator on 2017/5/2 0002.
 */

public class MenoryUtil {

    /**
     * 是否刷新第一个界面
     */
    public boolean isRefreshMain = false;

    /**
     * 是否刷新第二个界面
     */
    public boolean isRefreshSecond = false;

    /**
     * 是否刷新第三个界面
     */
    public boolean isRefreshThree = false;

    private static MenoryUtil mMS;
    public static MenoryUtil MS = MenoryUtil.getIntance();

    private static MenoryUtil getIntance() {
        if (mMS == null) mMS = new MenoryUtil();
        return mMS;
    }

    /**
     * 清理保存的内存
     */
    public static void clearMomery() {
        mMS = null;
    }
}
