package com.wyw.ljtds.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.wyw.ljtds.R;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class BottomDialog {
    private Dialog bottomDialog;

    public BottomDialog(Context context, View contentView) {

        bottomDialog = new Dialog( context, R.style.BottomDialog );

        bottomDialog.setContentView( contentView );
//        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
//        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
//        contentView.setLayoutParams(layoutParams);
        WindowManager windowManager = bottomDialog.getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = bottomDialog.getWindow().getAttributes();
        params.height = (int) (display.getHeight() * 0.5); // 高度设置为屏幕的0.5
        params.width = (int) (display.getWidth() * 1);
        bottomDialog.getWindow().setAttributes( params );
        bottomDialog.getWindow().setGravity( Gravity.BOTTOM );
        bottomDialog.getWindow().setWindowAnimations( R.style.popWindow_anim_style );
    }

    public BottomDialog setWH(double x,double y){
        WindowManager windowManager = bottomDialog.getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = bottomDialog.getWindow().getAttributes();
        params.height = (int) (display.getHeight() * y);
        params.width = (int) (display.getWidth() * x);
        bottomDialog.getWindow().setAttributes( params );
        return this;
    }

    public BottomDialog show() {
        bottomDialog.show();
        return this;
    }

    public BottomDialog setCancelable(boolean cancel) {
        bottomDialog.setCancelable( cancel );
        return this;
    }

    public void dissmiss() {
        if (bottomDialog != null && bottomDialog.isShowing()) {
            bottomDialog.dismiss();
        }
    }

}
