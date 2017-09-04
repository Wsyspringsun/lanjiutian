package com.wyw.ljtwl.ui;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.wyw.ljtwl.R;
import com.wyw.ljtwl.config.AppConfig;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

/**
 * Created by wsy on 17-9-1.
 */

@ContentView(R.layout.fragment_delegater_edit)
public class DelegaterEditFragment extends BaseFragment {
    private static final String ARG_ORDER_ID = "com.wyw.ljtwl.ui.arg_order_id";

    @Event(value = {R.id.fragment_delegater_submit})
    private void onclick(View view) {
        Intent it = null;
        switch (view.getId()) {
            case R.id.fragment_delegater_submit:
                break;

            default:
                break;

        }
    }

    /**
     * @param cat 余额类型
     * @return
     */
    public static DelegaterEditFragment newInstance(String orderId) {
        Log.e(AppConfig.TAG_ERR, "DelegaterEditFragment");
        DelegaterEditFragment fragment = new DelegaterEditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ORDER_ID, orderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
