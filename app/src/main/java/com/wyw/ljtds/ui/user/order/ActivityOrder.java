package com.wyw.ljtds.ui.user.order;

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
public class ActivityOrder extends BaseActivity{
    @ViewInject(R.id.viewPager1)
    private ViewPager viewPager;
    @ViewInject( R.id.tabs )
    private PagerSlidingTabStrip tabs;


    private ArrayList<String> titles=new ArrayList<>(  );
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentOrderList orderList1;
    private FragmentOrderList2 orderList2;
    private FragmentOrderList3 orderList3;
    private FragmentOrderList4 orderList4;
    private FragmentOrderList5 orderList5;

    @Event( value = {R.id.back,R.id.message})
    private void onClick(View v){
        switch (v.getId()){
            case R.id.back:
                finish();
                break;

            case R.id.message:
                startActivity( new Intent( this, ActivityMessage.class ) );
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        orderList1=new FragmentOrderList();
        orderList2=new FragmentOrderList2();
        orderList3=new FragmentOrderList3();
        orderList4=new FragmentOrderList4();
        orderList5=new FragmentOrderList5();
        fragmentList.add( orderList1 );
        fragmentList.add( orderList2 );
        fragmentList.add( orderList3 );
        fragmentList.add( orderList4 );
        fragmentList.add( orderList5 );
        titles.add( getResources().getString( R.string.all ) );
        titles.add( getResources().getString( R.string.daifu ) );
        titles.add( getResources().getString( R.string.daifa ) );
        titles.add( getResources().getString( R.string.daishou ) );
        titles.add( getResources().getString( R.string.daiping ) );

        viewPager.setAdapter( new MyFrPagerAdapter(getSupportFragmentManager(),titles,fragmentList ));
        tabs.setViewPager(viewPager);
        viewPager.setCurrentItem(getIntent().getIntExtra( "index",0 ));
        AppManager.addDestoryActivity( this,"order" );
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
