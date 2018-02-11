package com.wyw.ljtds.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.InputMethodUtils;

import static com.wyw.ljtds.R.id.activity_address_area_reclcyer;
import static com.wyw.ljtds.R.id.ed_pass;

/**
 * Created by wsy on 17-12-12.
 */

public class LoginDialog {
    static Dialog dialog = null;
    private static EditText ed_phone;
    private static EditText ed_pass;

    public static void show(Activity context) {
        Log.e(AppConfig.ERR_TAG, "showLogin:" + context.getClass().getName());
        dialog = new Dialog(context, R.style.Theme_AppCompat_Dialog);
        //显示登录弹出框
        LayoutInflater inflater = context.getLayoutInflater();
//                    View layout = inflater.inflate(R.layout.activity_login, null, false);
        View layout = inflater.inflate(R.layout.activity_login, null);
        dialog.setContentView(layout);
        dialog.setCancelable(false);
        dialog.setTitle(R.string.gongxi);

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        WindowManager wm;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= 21) {
            dialogWindow.setBackgroundDrawable(context.getDrawable(R.drawable.background_submit));
        } else {
            //noinspection deprecation
            dialogWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_submit));
        }
        lp.width = wm.getDefaultDisplay().getWidth();
//                    lp.height = wm.getDefaultDisplay().getHeight();
        dialogWindow.setAttributes(lp);

        initEvents(layout, context);

        dialog.show();
    }

    private static void initEvents(View layout, final Activity context) {
        ed_phone = (EditText) layout.findViewById(R.id.ed_phone);
        ed_pass = (EditText) layout.findViewById(R.id.ed_pass);
        ed_phone.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        ed_pass.setImeOptions(EditorInfo.IME_ACTION_DONE);

        ImageView ivGuanBi = (ImageView) layout.findViewById(R.id.guanbi);
        ivGuanBi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodUtils.closeSoftKeyboard(context);
                dialog.dismiss();
            }
        });

        Button ivNext = (Button) layout.findViewById(R.id.next);
        Log.e(AppConfig.ERR_TAG,"ivNext");
        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(AppConfig.ERR_TAG,"ivNext click.......");
                ((BaseActivity) context).setLoding(context, false);
                new BizDataAsyncTask<String>() {
                    @Override
                    protected String doExecute() throws ZYException, BizFailure {
                        return UserBiz.login(ed_phone.getText().toString().trim(),
                                ed_pass.getText().toString().trim(), "0");
                    }

                    @Override
                    protected void onExecuteSucceeded(String result) {
                        PreferenceCache.putToken(result); // 持久化缓存token
                        PreferenceCache.putAutoLogin(true);// 记录是否自动登录
                        PreferenceCache.putUsername(ed_phone.getText().toString().trim());
                        if (PreferenceCache.isAutoLogin()) {
                            PreferenceCache.putPhoneNum(ed_phone.getText().toString().trim());
                        }
                        dialog.dismiss();
                        ((BaseActivity) context).closeLoding();
                    }

                    @Override
                    protected void OnExecuteFailed() {
                        ((BaseActivity) context).closeLoding();
                    }
                };
            }
        });

        ed_phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    ed_pass.requestFocus();
                }
                return false;
            }
        });
        ed_pass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    InputMethodUtils.closeSoftKeyboard(context);
                }
                return false;
            }
        });
    }

}
