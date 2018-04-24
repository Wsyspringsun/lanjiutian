package com.wyw.ljtds.ui.user.order;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.model.AddressModel;
import com.wyw.ljtds.ui.user.ActivityLogin;
import com.wyw.ljtds.ui.user.manage.ActivityAmendPassword1;
import com.wyw.ljtds.widget.MyCallback;

import java.util.List;

/***
 * 已经存在的手机号提示
 * 已经注册的手机号，提示进入找回密码
 * wsy 2018-1-29
 * select default address
 */
public class WXShare4OrderDialogFragment extends DialogFragment {
    private static final String ARG_PHONE = "com.wyw.ljtds.ui.goods.SelDefaultAddressFragment.ARG_PHONE";
    private static final String TAG_SHARE_DIALOG_RESULT = "com.wyw.ljtds.ui.user.order.WXShare4OrderDialogFragment.TAG_SHARE_DIALOG_RESULT";
    private static final String SHARE_DIALOG_RESULT_YES = "0";
    private static final String SHARE_DIALOG_RESULT_NO = "1";

    private MyCallback myCallback;


    private ImageButton btnCancel;
    private TextView tvExistPhone;
    private TextView tvGoLogin;
    private TextView tvFindPwd;

    private View.OnClickListener listners = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }

        }
    };

    public static WXShare4OrderDialogFragment newInstance() {
        Bundle args = new Bundle();
        WXShare4OrderDialogFragment frag = new WXShare4OrderDialogFragment();
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.setCancelable(false);
        final Intent it = new Intent();
        return new AlertDialog.Builder(getActivity()).setTitle(R.string.order_title_sharewx).setMessage(R.string.order_content_sharewx).setPositiveButton(R.string.order_btn_sharewx_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (myCallback != null)
                    myCallback.callback(true);
                WXShare4OrderDialogFragment.this.dismiss();
            }
        }).setNegativeButton(R.string.order_btn_sharewx_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (myCallback != null)
                    myCallback.callback(false);
                WXShare4OrderDialogFragment.this.dismiss();
            }
        }).setCancelable(false).create();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public MyCallback getMyCallback() {
        return myCallback;
    }

    public void setMyCallback(MyCallback myCallback) {
        this.myCallback = myCallback;
    }
}
