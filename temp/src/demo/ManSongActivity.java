package com.wyw.ljtds.ui.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.ui.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * create by wsy on 2017-07-28
 */
@ContentView(R.layout.activity_tab_fragment)
public class ManSongActivity extends BaseActivity {
    @ViewInject(R.id.activity_fragment_title)
    private TextView tvTitle;
    @ViewInject(R.id.activity_tab_fragment_tab)
    private TabLayout tabChangci;
    @ViewInject(R.id.activity_tab_fragment_vp_fragments)
    private ViewPager vpChangci;

    private List<XianShiQiangFragment> lsChangci;

    @ViewInject(R.id.toolbar_common)
    private Toolbar tbMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lsChangci = new ArrayList<>();
        lsChangci.add(XianShiQiangFragment.newInstance(1));
        lsChangci.add(XianShiQiangFragment.newInstance(2));
        lsChangci.add(XianShiQiangFragment.newInstance(2));
        lsChangci.add(XianShiQiangFragment.newInstance(2));
        lsChangci.add(XianShiQiangFragment.newInstance(2));
        lsChangci.add(XianShiQiangFragment.newInstance(2));

        vpChangci.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public CharSequence getPageTitle(int position) {
                return "1月1日 9：00--11：00Tab..." + position;
            }

            @Override
            public Fragment getItem(int position) {
                return lsChangci.get(position);
            }

            @Override
            public int getCount() {
                Log.e(AppConfig.ERR_TAG, "size........" + lsChangci.size());
                return lsChangci.size();
            }
        });

        tabChangci.setupWithViewPager(vpChangci);
        vpChangci.setCurrentItem(0);

        Log.e(AppConfig.ERR_TAG, ".............complete");

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
