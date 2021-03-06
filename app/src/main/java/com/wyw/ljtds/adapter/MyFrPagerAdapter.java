package com.wyw.ljtds.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.gxz.PagerSlidingTabStrip;
import com.wyw.ljtds.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 顾修忠-guxiuzhong@youku.com/gfj19900401@163.com
 * @Title: MyPagerAdapter
 * @Package com.guxiuzhong.pagerslidingtabstrip.adapter
 * @Description:
 * @date 15/11/29
 * @time 下午12:53
 */
public class MyFrPagerAdapter extends FragmentPagerAdapter  {
    private List<String> titles;

    private List<Fragment> fragments;

    public MyFrPagerAdapter(FragmentManager fm, List<String> list,List<Fragment> fragments) {
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
