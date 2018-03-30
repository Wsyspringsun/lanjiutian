package com.wyw.ljtds.ui.goods;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.view.View;
import android.view.ViewTreeObserver;
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
    private String shopId;
    @ViewInject(R.id.activity_lifeshop_img_banner)
    ImageView shopImg;

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
        super.onCreate(savedInstanceState);

        shopId = getIntent().getStringExtra(TAG_SHOP_ID);
        for (int i = 0; i < tabTitles.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(tabTitles[i]));
        }
        vpMain.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public CharSequence getPageTitle(int position) {
                return tabTitles[position];
            }

            @Override
            public Fragment getItem(int position) {
                return LifeShopFragment.newInstance(shopId, position);
                /*switch (position) {
                    case 0:
                    case 1:
                        return frag2;
                    case 2:
                        return frag3;
                    default:
                        return null;
                }*/
            }

            @Override
            public int getCount() {
                return tabTitles.length;
            }
        });

        tabLayout.setupWithViewPager(vpMain);

        loadData();
    }


    @Override
    protected void onResume() {
        super.onResume();
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
        Utils.log("itemShop.getIMAGE_PATH1():" + itemShop.getIMAGE_PATH1());
        Picasso.with(this).load(Uri.parse(itemShop.getIMAGE_PATH1())).into(shopImg);
    }
}
