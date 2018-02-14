package com.wyw.ljtmgr.utils;

import android.app.Activity;

import com.wyw.ljtmgr.config.AppConfig;
import com.wyw.ljtmgr.ui.BaseActivity;

/**
 * Created by wsy on 18-1-8.
 */

public class ActivityUtil {
    public static boolean isComplete(final Activity context, int needPhase) {
        return (((BaseActivity) context).curPhase & needPhase) == needPhase;
    }

    static int getPreviousPhase(int needPhase) {
        int previousPhase = 0;
        switch (needPhase) {
            case AppConfig.PHASE_LOGIN_IN:
                previousPhase = AppConfig.PHASE_SERVER_OK;
                break;
            case AppConfig.PHASE_SERVER_OK:
                previousPhase = AppConfig.PHASE_NET_OK;
                break;
            case AppConfig.PHASE_NET_OK:
                previousPhase = AppConfig.PHASE_PERMISSION;
                break;
            case AppConfig.PHASE_PERMISSION:
                previousPhase = 0;
                break;

        }
        return previousPhase;
    }

}
