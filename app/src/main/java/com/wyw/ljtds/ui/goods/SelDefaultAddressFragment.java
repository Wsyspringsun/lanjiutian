package com.wyw.ljtds.ui.goods;

import android.app.Activity;
import android.app.Dialog;
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
import com.wyw.ljtds.utils.Utils;

import java.util.List;

/***
 * wsy 2018-1-29
 * select default address
 */
public class SelDefaultAddressFragment extends DialogFragment {
    private static final String TAG_SEL = "com.wyw.ljtds.ui.goods.SelDefaultAddressFragment.TAG_SEL";
    private TextView tvAddressCurrent;
    private TextView tvAddressOhter;
    private TextView tvLoading;
    private ImageButton btnCancel;

    private MyLocation location;

    private View.OnClickListener listners = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_sel_address_sel:
                    SelDefaultAddressFragment.this.dismiss();
                    sendResult(R.id.dialog_sel_address_sel);
                    break;
                case R.id.dialog_sel_address_info:
                    SelDefaultAddressFragment.this.dismiss();
                    sendResult(R.id.dialog_sel_address_info);
                    break;
                case R.id.dialog_sel_address_cancel:
                    SelDefaultAddressFragment.this.dismiss();
                    break;
                default:
                    return;
            }
        }
    };

    private List<AddressModel> list;

    private void sendResult(int id) {
        if (getTargetFragment() == null) return;
        Intent it = null;
        switch (id) {
            case R.id.dialog_sel_address_sel:
                break;
            case R.id.dialog_sel_address_info:
                it = new Intent();
                it.putExtra(TAG_SEL, "1");
                break;
        }
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, it);
    }

    public static SelDefaultAddressFragment newInstance() {
        Bundle args = new Bundle();
        SelDefaultAddressFragment frag = new SelDefaultAddressFragment();
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.setCancelable(false);
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_sel_address, null);
        initView(v);
        initEvent();
        initData();
//        Utils.log("onCreateDialog:.......");

        return new AlertDialog.Builder(getActivity()).setView(v).setCancelable(false).create();
    }

    @Override
    public void onStart() {
        super.onStart();
        watchLocation();
    }

    private void watchLocation() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SingleCurrentUser.location != null) {
                    tvAddressCurrent.setText(SingleCurrentUser.location.getAddrStr());
                    tvLoading.setVisibility(View.GONE);
                    return;
                } else {
                    watchLocation();
                }
            }
        }, 1000);
    }

    private void initEvent() {
        tvAddressOhter.setOnClickListener(listners);
        tvLoading.setOnClickListener(listners);
        tvAddressCurrent.setOnClickListener(listners);
        btnCancel.setOnClickListener(listners);

    }

    private void initData() {
        tvAddressCurrent.setText("");
    }

    private void initView(View v) {
        tvAddressCurrent = (TextView) v.findViewById(R.id.dialog_sel_address_info);
        tvAddressOhter = (TextView) v.findViewById(R.id.dialog_sel_address_sel);
        tvLoading = (TextView) v.findViewById(R.id.dialog_sel_address_loading);
        btnCancel = (ImageButton) v.findViewById(R.id.dialog_sel_address_cancel);
    }
}
