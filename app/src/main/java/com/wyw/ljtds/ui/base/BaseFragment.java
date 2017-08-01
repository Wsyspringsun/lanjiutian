package com.wyw.ljtds.ui.base;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.ui.user.FragmentUser;
import com.wyw.ljtds.widget.dialog.LoadingDialogUtils;

import org.xutils.x;

/**
 * Created by Administrator on 2016/12/8 0008.
 */

public class BaseFragment extends Fragment {
    private boolean injected = false;

    private Dialog mDialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case AppConfig.IntentExtraKey.LODING_CONTEXT:
                    LoadingDialogUtils.closeDialog(mDialog);
                    break;
            }
        }
    };
    protected boolean loading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        injected = true;
        return x.view().inject(this,inflater,container);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!injected) {
            x.view().inject(this, this.getView());
        }
    }

    public View findViewById(int resId) {
        return getView().findViewById(resId);
    }

    public void setLoding(Context context, boolean settime){
        loading = true;
        if(settime){
            mDialog = LoadingDialogUtils.createLoadingDialog(context, "加载中...");
            mHandler.sendEmptyMessageDelayed(AppConfig.IntentExtraKey.LODING_CONTEXT, 2000);
        }else {
            mDialog = LoadingDialogUtils.createLoadingDialog(context, "加载中...");
        }
    }

    public void closeLoding(){
        loading = false;
        LoadingDialogUtils.closeDialog(mDialog);
    }

    /**
     * 刷新界面
     */
    public void Refresh(){}
}
