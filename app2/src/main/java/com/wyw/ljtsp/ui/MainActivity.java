package com.wyw.ljtsp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gxz.PagerSlidingTabStrip;
import com.wyw.ljtsp.R;
import com.wyw.ljtsp.config.AppConfig;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivitry {
    @ViewInject(R.id.tabs)
    private PagerSlidingTabStrip tabs;
    @ViewInject(R.id.viewPager)
    private ViewPager viewPager;


    private ArrayList<String> titles = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentOrder fragmentOrder;
    private FragmentShop fragmentShop;


    @Event(value = {R.id.setting})
    private void onClick(View view){
        switch (view.getId()){
            case R.id.setting:
                startActivity(new Intent(this,ActivitySetting.class));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        fragmentOrder = new FragmentOrder();
        fragmentShop = new FragmentShop();
        fragmentList.add(fragmentShop);
        fragmentList.add(fragmentOrder);
        titles.add(getResources().getString(R.string.shop));
        titles.add(getResources().getString(R.string.order));

        viewPager.setAdapter(new MyFrPagerAdapter(getSupportFragmentManager(), titles, fragmentList));
        tabs.setViewPager(viewPager);
        viewPager.setCurrentItem(getIntent().getIntExtra(AppConfig.IntentExtraKey.MAIN_GO_TO, 1));
    }

    class MyFrPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<String> titles;

        private List<Fragment> fragments;

        public MyFrPagerAdapter(FragmentManager fm, ArrayList<String> list, List<Fragment> fragments) {
            super(fm);
            this.titles = list;
            this.fragments=fragments;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
    }

}
