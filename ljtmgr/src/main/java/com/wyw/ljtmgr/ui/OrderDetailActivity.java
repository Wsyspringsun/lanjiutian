package com.wyw.ljtmgr.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.wyw.ljtmgr.R;
import com.wyw.ljtmgr.config.AppConfig;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_order_detail)
public class OrderDetailActivity extends BaseActivity {
    private static final String TAG_ORDER_ID = "com.wyw.ljtwl.ui.OrderDetailActivity.TAG_ORDER_ID";
    private static final String TAG_STAT = "com.wyw.ljtwl.ui.OrderDetailActivity.TAG_STAT";
    @ViewInject(R.id.activity_order_detail_vp)
    ViewPager vp;

    FragmentPagerAdapter vpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();


    }

    private void initView() {
//        initToolbar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindData2View();
    }

    private void bindData2View() {
        vpAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public Fragment getItem(int position) {
                String orderId = getIntent().getStringExtra(TAG_ORDER_ID);
                String stat = getIntent().getStringExtra(TAG_STAT);

//                String orderId = "201801081740441407";
                return OrderDetailFragment.newInstance(orderId, stat);
            }
        };
        vp.setAdapter(vpAdapter);
    }

    public static Intent getIntent(Activity context, String stat, String orderId) {
        Intent it = new Intent(context, OrderDetailActivity.class);
        it.putExtra(TAG_ORDER_ID, orderId);
        it.putExtra(TAG_STAT, stat);
        Log.e(AppConfig.TAG_ERR, "stat:" + stat);
        return it;
    }
}
