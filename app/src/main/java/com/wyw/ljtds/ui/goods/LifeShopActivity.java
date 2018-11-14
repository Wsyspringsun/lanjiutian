package com.wyw.ljtds.ui.goods;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.ShopImg;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.category.FragmentLife;
import com.wyw.ljtds.ui.user.ActivityMessage;
import com.wyw.ljtds.ui.user.wallet.BalanceFragment;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * create by wsy on 2017-07-28
 */
@ContentView(R.layout.activity_lifeshop)
public class LifeShopActivity extends BaseActivity {
    private static final String TAG_SHOP_ID = "com.wyw.ljtds.ui.goods.tag_shop_id";

    @ViewInject(R.id.activity_lifeshop_tabs)
    TabLayout tabLayout;
    @ViewInject(R.id.fragment_lifeshop_shopimg_vp_main)
    ViewPager vpMain;
    String[] tabTitles = {"店铺首页", "全部宝贝", "新品上架"};
    int[] tabImgs = {R.drawable.ic_shouye, R.drawable.ic_quanbu, R.drawable.ic_shangxin};
    private String shopId;
    @ViewInject(R.id.activity_lifeshop_img_banner)
    ImageView shopImg;
    private LifeShopFragmentPagerAdapter fpAdapter;

    public static Intent getIntent(Context ctx, String shopId) {
        Intent it = new Intent(ctx, LifeShopActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        it.putExtra(TAG_SHOP_ID, shopId);
        return it;
    }

    @Event(value = {R.id.back, R.id.message})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.message:
                startActivity(new Intent(this, ActivityMessage.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }*/
        super.onCreate(savedInstanceState);

        shopId = getIntent().getStringExtra(TAG_SHOP_ID);
        for (int i = 0; i < tabTitles.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(tabTitles[i]));
        }
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(LifeShopAllGoodsFragment.newInstance(shopId, "0"));
        fragments.add(LifeShopAllGoodsFragment.newInstance(shopId, ""));
        fragments.add(LifeShopAllGoodsFragment.newInstance(shopId, "1"));
        fpAdapter = new LifeShopFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        vpMain.setAdapter(fpAdapter);
        vpMain.setOffscreenPageLimit(tabTitles.length);
        tabLayout.setupWithViewPager(vpMain);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(fpAdapter.getTabView(i));
                if (tab.getCustomView() != null) {
                    View tabView = (View) tab.getCustomView().getParent();
                    tabView.setTag(i);
//                    tabView.setOnClickListener(mTabOnClickListener);
                }
            }
        }


        vpMain.setCurrentItem(0);

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    public void loadData() {
        new BizDataAsyncTask<List<ShopImg>>() {
            @Override
            protected List<ShopImg> doExecute() throws ZYException, BizFailure {
                return GoodsBiz.readShopPage(shopId);
            }

            @Override
            protected void onExecuteSucceeded(List<ShopImg> shopImgs) {
                closeLoding();
                updView(shopImgs);
            }


            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    public void updView(List<ShopImg> shopImgs) {
        if (shopImgs == null || shopImgs.isEmpty())
            return;
        ShopImg itemShop = shopImgs.get(0);
//        tvShopName.setText(itemShop.getSHOP_NAME());
//        sdvShopLogo.setImageURI(Uri.parse(itemShop.getIMAGE_PATH1()));
        Utils.log("getIMAGE_PATH1():"+itemShop.getIMAGE_PATH1());
        if(!StringUtils.isEmpty(itemShop.getIMAGE_PATH1())){
            Picasso.with(this).load(Uri.parse(itemShop.getIMAGE_PATH1())).into(shopImg);
        }
//        Picasso.with(this).load(Uri.parse("http://www.lanjiutian.com/upload/images/368412d9df934cac99253c95ed5ac2fe/87a29a6baeda89941496febdae6308ed.png")).into(shopImg);
    }

    class LifeShopFragmentPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragments;

        public LifeShopFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        public View getTabView(int position) {
            View view = LayoutInflater.from(LifeShopActivity.this).inflate(R.layout.item_lifeshop_tab, null);
            TextView tv = (TextView) view.findViewById(R.id.item_lifeshop_tab_tv);
            tv.setText(tabTitles[position]);
            ImageView img = (ImageView) view.findViewById(R.id.item_lifeshop_tab_img);
            img.setImageResource(tabImgs[position]);
            return view;
        }


        @Override
        public Fragment getItem(int position) {
            return fragments.get(position % 3);
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
}
