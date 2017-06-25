package com.wyw.ljtds.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.GuideViewPagerAdapter;
import com.wyw.ljtds.ui.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

@ContentView( R.layout.activity_guide )
public class ActivityGuide extends BaseActivity implements ViewPager.OnPageChangeListener {
    @ViewInject( R.id.vp_guide )
    private ViewPager viewPager;

    private GuideViewPagerAdapter vpAdapter;
    private List<View> views;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        LayoutInflater inflater = LayoutInflater.from(this);

        views = new ArrayList<View>();
        // 初始化引导图片列表
        views.add(inflater.inflate(R.layout.welcome_one, null));
        views.add(inflater.inflate(R.layout.welcome_two, null));

        // 初始化Adapter
        vpAdapter = new GuideViewPagerAdapter(views, this);

        viewPager = (ViewPager) findViewById(R.id.vp_guide);
        viewPager.setAdapter(vpAdapter);
        // 绑定回调
        viewPager.setOnPageChangeListener(this);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
