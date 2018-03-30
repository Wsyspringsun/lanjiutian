package com.wyw.ljtmgr.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.wyw.ljtmgr.R;
import com.wyw.ljtmgr.biz.SimpleCommonCallback;
import com.wyw.ljtmgr.biz.UserBiz;
import com.wyw.ljtmgr.config.AppConfig;
import com.wyw.ljtmgr.config.MyApplication;
import com.wyw.ljtmgr.config.SingleCurrentUser;
import com.wyw.ljtmgr.model.LoginModel;
import com.wyw.ljtmgr.model.ServerResponse;
import com.wyw.ljtmgr.model.ShopInfo;
import com.wyw.ljtmgr.weidget.LazyLoadFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import utils.CommonUtil;

/**
 * Created by Administrator on 2017/7/3 0003.
 */

@ContentView(R.layout.fragment_release)
public class FragmentShop extends LazyLoadFragment {
    private static final String ARG_TITLE = "ARG_TITLE";
    @ViewInject(R.id.fragment_shop_tv_shopname)
    private TextView tvShop;

    public static FragmentShop newInstance(String title) {
        FragmentShop frag = new FragmentShop();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        LoginModel loginer = MyApplication.getCurrentLoginer();
        if (loginer == null) return;
        tvShop.setText(loginer.getOidGroupId());
        loadGroup();
    }

    public void loadGroup() {
        UserBiz.loadGroupInfo(new SimpleCommonCallback<ShopInfo>(getActivity()) {

            @Override
            protected void handleResult(ShopInfo result) {
                tvShop.setText(result.getGroupName());
            }
        });
    }


    @Override
    protected void lazyLoad() {
    }

    @Override
    protected void stopLoad() {
        super.stopLoad();
    }
}
