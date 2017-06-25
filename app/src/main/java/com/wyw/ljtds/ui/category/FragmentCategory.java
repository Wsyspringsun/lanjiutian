package com.wyw.ljtds.ui.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.gxz.PagerSlidingTabStrip;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.MyFrPagerAdapter;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.ui.home.ActivitySearch;
import com.wyw.ljtds.ui.user.ActivityMessage;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/13
 */

@ContentView(R.layout.fragment_categorys)
public class FragmentCategory extends BaseFragment {
    @ViewInject( R.id.tabs )
    private PagerSlidingTabStrip tabs;
    @ViewInject( R.id.pager )
    private ViewPager pager;


    private ArrayList<String> titles=new ArrayList<>(  );
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentLife fragmentLife;
    private FragmentMedicine fragmentMedicine;

    @Event(value = {R.id.ll_search, R.id.zxing, R.id.ll_message})
    private void onclick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.ll_search:
                it = new Intent(getActivity(), ActivitySearch.class);
                startActivity(it);
                break;

            case R.id.zxing:
                it = new Intent(getActivity(), ActivityScan.class);
                startActivity(it);
                break;

            case R.id.ll_message:
                it = new Intent(getActivity(), ActivityMessage.class);
                startActivity(it);
                break;
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fragmentLife=new FragmentLife();
        fragmentMedicine=new FragmentMedicine();
        fragmentList.add( fragmentLife );
        fragmentList.add( fragmentMedicine );
        titles.add( "生活馆" );
        titles.add( "医药馆" );
        pager.setAdapter( new MyFrPagerAdapter(getActivity().getSupportFragmentManager(),titles,fragmentList ));
        tabs.setViewPager(pager);
        pager.setCurrentItem(1);

    }

}
