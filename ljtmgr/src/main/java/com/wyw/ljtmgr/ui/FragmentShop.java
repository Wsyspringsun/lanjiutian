package com.wyw.ljtwl.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.wyw.ljtwl.R;
import com.wyw.ljtwl.weidget.LazyLoadFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2017/7/3 0003.
 */

@ContentView(R.layout.fragment_release)
public class FragmentShop extends LazyLoadFragment {
    private static final String ARG_TITLE = "ARG_TITLE";
    @ViewInject(R.id.fragment_shop_tv_shopname)
    private TextView shop;

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
    protected void lazyLoad() {
        shop.setVisibility(View.VISIBLE);
    }

    @Override
    protected void stopLoad() {
        super.stopLoad();
        shop.setVisibility(View.GONE);
    }
}
