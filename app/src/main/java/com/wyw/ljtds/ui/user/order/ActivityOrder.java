package com.wyw.ljtds.ui.user.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gxz.PagerSlidingTabStrip;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.FragmentAdapter;
import com.wyw.ljtds.adapter.MyFrPagerAdapter;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.AppManager;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.user.ActivityMessage;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/5 0005.
 */

@ContentView(R.layout.activity_order)
public class ActivityOrder extends BaseActivity {
    @ViewInject(R.id.viewPager1)
    private ViewPager viewPager;
    @ViewInject(R.id.tabs)
    private PagerSlidingTabStrip tabs;


    private ArrayList<String> titles = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private static final String TAG_INDEX= "com.wyw.ljtds.ui.user.order.tag_index";

    @Event(value = {R.id.back, R.id.message})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.message:
                startActivity(new Intent(this, ActivityMessage.class));
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentOrderList orderList1 = new FragmentOrderList();
        FragmentOrderList orderList2 = new FragmentOrderList();
        orderList2.setStateCat("A");
        FragmentOrderList orderList3 = new FragmentOrderList();
        orderList3.setStateCat("B");
        FragmentOrderList orderList4 = new FragmentOrderList();
        orderList4.setStateCat("C");
        FragmentOrderList orderList5 = new FragmentOrderList();
        orderList5.setStateCat("D");
        fragmentList.add(orderList1);
        fragmentList.add(orderList2);
        fragmentList.add(orderList3);
        fragmentList.add(orderList4);
        fragmentList.add(orderList5);
        titles.add(getResources().getString(R.string.all));
        titles.add(getResources().getString(R.string.daifu));
        titles.add(getResources().getString(R.string.daifa));
        titles.add(getResources().getString(R.string.daishou));
        titles.add(getResources().getString(R.string.daiping));

        viewPager.setAdapter(new MyFrPagerAdapter(getSupportFragmentManager(), titles, fragmentList));
        tabs.setViewPager(viewPager);
        viewPager.setCurrentItem(getIntent().getIntExtra(TAG_INDEX, 0));
        AppManager.addDestoryActivity(this, "order");
    }

    public static Intent getIntent(Context ctx, int index) {
        Intent it = new Intent(ctx, ActivityOrder.class);
        it.putExtra(TAG_INDEX, index);
        return it;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(AppConfig.ERR_TAG, "activity resume");
    }
}
