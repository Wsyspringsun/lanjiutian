package com.wyw.ljtds.ui.user;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.model.AddressModel;
import com.wyw.ljtds.model.MyLocation;
import com.wyw.ljtds.model.SingleCurrentUser;
import com.wyw.ljtds.ui.user.manage.ActivityAmendPassword1;

import java.util.List;

/***
 * 已经存在的手机号提示
 * 已经注册的手机号，提示进入找回密码
 * wsy 2018-1-29
 * select default address
 */
public class ExistUserPhoneDialogFragment extends DialogFragment {
    private static final String ARG_PHONE = "com.wyw.ljtds.ui.goods.SelDefaultAddressFragment.ARG_PHONE";

    private ImageButton btnCancel;
    private TextView tvExistPhone;
    private TextView tvGoLogin;
    private TextView tvFindPwd;

    private View.OnClickListener listners = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_to_findpwd_tv_gologin:
                    startActivity(ActivityLogin.getIntent(getActivity()));
                    break;
                case R.id.dialog_to_findpwd_tv_findpwd:
                    startActivity(new Intent(getActivity(), ActivityAmendPassword1.class));
                    break;
                case R.id.dialog_to_findpwd_cancel:
                    ExistUserPhoneDialogFragment.this.dismiss();
                    break;
            }

        }
    };

    private List<AddressModel> list;

    public static ExistUserPhoneDialogFragment newInstance(String phone) {
        Bundle args = new Bundle();
        args.putString(ARG_PHONE, phone);
        ExistUserPhoneDialogFragment frag = new ExistUserPhoneDialogFragment();
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.setCancelable(false);
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_to_findpwd, null);
        initView(v);
        initEvent();
        initData();

        return new AlertDialog.Builder(getActivity()).setView(v).setTitle(R.string.alert_tishi).setPositiveButton(R.string.reg_to_login, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(ActivityLogin.getIntent(getActivity()));
            }
        }).setNegativeButton(R.string.reg_to_find_pwd, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getActivity(), ActivityAmendPassword1.class));
            }
        }).setCancelable(false).create();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initEvent() {
        tvGoLogin.setOnClickListener(listners);
        tvFindPwd.setOnClickListener(listners);
        btnCancel.setOnClickListener(listners);

    }

    private void initData() {
        String phone = getArguments().getString(ARG_PHONE);
        tvExistPhone.setText(getString(R.string.reg_warning_phone_exist, phone));
    }

    private void initView(View v) {
        tvExistPhone = (TextView) v.findViewById(R.id.dialog_to_findpwd_tv_warning);
        tvGoLogin = (TextView) v.findViewById(R.id.dialog_to_findpwd_tv_gologin);
        tvFindPwd = (TextView) v.findViewById(R.id.dialog_to_findpwd_tv_findpwd);
        btnCancel = (ImageButton) v.findViewById(R.id.dialog_to_findpwd_cancel);
    }
}
