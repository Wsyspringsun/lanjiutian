package com.wyw.ljtds.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by sunfusheng on 15/8/7.
 */
public class ToastUtil {

    private static Toast mToast;

    public static void show(Context context, @StringRes int resId) {
        show(context, context.getResources().getText(resId));
    }

    public static void show(Context context, CharSequence message) {
        if (context == null || TextUtils.isEmpty(message)) return;
        int duration;
        if (message.length() > 3) {
            duration = Toast.LENGTH_LONG; //如果字符串比较长，那么显示的时间也长一些。
        } else {
            duration = Toast.LENGTH_SHORT;
        }
        if (mToast == null) {
            mToast = Toast.makeText(context, message, duration);
        } else {
            mToast.setText(message);
            mToast.setDuration(duration);
        }
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }
}
