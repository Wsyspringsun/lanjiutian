package com.wyw.ljtwl.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Administrator on 2017/4/2 0002.
 */

public class InputMethodUtils {
    /**
     * 为给定的编辑器开启软键盘
     *
     * @param editText 给定的编辑器
     */
    public static void openSoftKeyboard(Context context, EditText editText) {
        editText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService( Context.INPUT_METHOD_SERVICE );
        inputMethodManager.showSoftInput( editText, InputMethodManager.SHOW_IMPLICIT );
    }


    /**
     * 关闭软键盘
     */
    public static void closeSoftKeyboard(Activity activity) {
        //隐藏软键盘
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity.getSystemService( Context.INPUT_METHOD_SERVICE );
            inputmanger.hideSoftInputFromWindow( view.getWindowToken(), 0 );
        }
    }

}
