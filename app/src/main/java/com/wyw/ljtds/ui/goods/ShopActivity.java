package com.wyw.ljtds.ui.goods;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gxz.PagerSlidingTabStrip;
import com.huawei.android.pushselfshow.richpush.html.a;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.MedicineDetailsModel;
import com.wyw.ljtds.model.MedicineListModel;
import com.wyw.ljtds.model.ShopModel;
import com.wyw.ljtds.model.SingleCurrentUser;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.cart.FragmentCart;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.MyCallback;
import com.wyw.ljtds.widget.dialog.CatListDialog;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wyw.ljtds.ui.goods.ShopGoodsFragment.TYPE_HOT;
import static com.wyw.ljtds.ui.goods.ShopGoodsFragment.TYPE_SHOP;

/**
 * create by wsy on 2017-07-28
 * update by wsy on 2018-01-03
 * 店铺
 */
@ContentView(R.layout.activity_shopinfo)
public class ShopActivity extends BaseActivity {
    private static final String TAG_SHOP_ID = "com.wyw.ljtds.ui.goods.tag_shop_id";
    @ViewInject(R.id.fragment_shopimg_vp_main)
    ViewPager vpMain;
    @ViewInject(R.id.activity_shopinfo_nsv_main)
    NestedScrollView scrollMain;
    @ViewInject(R.id.fragment_shopimg_tab_main)
    PagerSlidingTabStrip tabMain;
    @ViewInject(R.id.fragment_shopimg_tab_main_top)
    PagerSlidingTabStrip tabMainTop;

    @ViewInject(R.id.fragment_shopimg_ll_header)
    LinearLayout llHeader;
    @ViewInject(R.id.activity_shopinfo_img_banner)
    ImageView imgShpBanner;
    @ViewInject(R.id.fragment_shopimg_img_previous)
    ImageView imgPrevious;
    @ViewInject(R.id.fragment_shopimg_catlist)
    ImageView imgCatlist;
    @ViewInject(R.id.fragment_shopimg_search)
    TextView edSearch;
    //    @ViewInject(R.id.fragment_shop_bottomline)
//    View bottomView;
    @ViewInject(R.id.fragment_shopimg_img_sou)
    ImageView imgSou;
    @ViewInject(R.id.activity_goods_info_tv_shopname)
    TextView tvShopname;
    @ViewInject(R.id.activity_goods_info_tv_shopinfo)
    TextView tvShopinfo;

    private CatListDialog dialogCatList;
    private int page = 1;
    private List<MedicineListModel> medicineList;
    List<MyCallback> searchCallbackList = new ArrayList<>();
    List<MyCallback> catCallbackList = new ArrayList<>();

    ShopGoodsFragment fragGoodsShop;
    ShopGoodsFragment fragGoodsRecommand;
    private ShopModel shopModel;

    ShopActivity addSearchCallBack(MyCallback callback) {
        searchCallbackList.add(callback);
        return this;
    }

    ShopActivity addCatSelCallBack(MyCallback callback) {
        catCallbackList.add(callback);
        return this;
    }

    @Event(value = {R.id.fragment_shopimg_catlist, R.id.fragment_shopimg_img_previous})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_shopimg_catlist:
                if (dialogCatList == null) {
                    int[] pos = new int[2];
                    view.getLocationInWindow(pos);
                    dialogCatList = new CatListDialog(this, pos[0], pos[1], R.style.AllScreenWidth_Dialog);
                    dialogCatList.setCallback(new MyCallback() {
                        @Override
                        public void callback(Object... params) {
                            //selected item from dialog
                            for (MyCallback c : catCallbackList) {
                                c.callback(params);
                            }
                            dialogCatList.dismiss();
                        }
                    });
                }

                dialogCatList.show();
                break;
            case R.id.fragment_shopimg_img_previous:
                finish();
                break;
        }
    }

    public static Intent getIntent(Context ctx, String shopId) {
        Intent it = new Intent(ctx, ShopActivity.class);
        it.putExtra(TAG_SHOP_ID, shopId);
        return it;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String shopId = getIntent().getStringExtra(TAG_SHOP_ID);

        fragGoodsShop = ShopGoodsFragment.newInstance(shopId, TYPE_SHOP);
        fragGoodsRecommand = ShopGoodsFragment.newInstance(shopId, TYPE_HOT);

        tabMainTop.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            scrollMain.setNestedScrollingEnabled(false);
        }
        imgSou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (MyCallback callback : searchCallbackList) {
                    callback.callback(edSearch.getText().toString());
                }
            }
        });
        scrollMain.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int[] tabLoc = new int[2];
                int[] tabTopLoc = new int[2];
                tabMain.getLocationInWindow(tabLoc);
                tabMainTop.getLocationInWindow(tabTopLoc);
                if (tabLoc[1] <= tabTopLoc[1]) {
                    tabMainTop.setVisibility(View.VISIBLE);
                    llHeader.setBackgroundColor(ActivityCompat.getColor(ShopActivity.this, R.color.white));
                    imgPrevious.setImageDrawable(ActivityCompat.getDrawable(ShopActivity.this, R.drawable.ic_houtui));
                    imgCatlist.setImageDrawable(ActivityCompat.getDrawable(ShopActivity.this, R.drawable.ic_fenlei));
                    edSearch.setTextColor(ActivityCompat.getColor(ShopActivity.this, R.color.gray));
//                    bottomView.setBackgroundColor(ActivityCompat.getColor(ShopActivity.this, R.color.gray));
                    imgSou.setImageDrawable(ActivityCompat.getDrawable(ShopActivity.this, R.drawable.ic_action_search_black));
                } else {
                    tabMainTop.setVisibility(View.GONE);
                    llHeader.setBackgroundColor(ActivityCompat.getColor(ShopActivity.this, R.color.transparent));
                    imgPrevious.setImageDrawable(ActivityCompat.getDrawable(ShopActivity.this, R.drawable.ic_houtui_bai));
                    imgCatlist.setImageDrawable(ActivityCompat.getDrawable(ShopActivity.this, R.drawable.ic_fenlei_bai));
                    edSearch.setTextColor(ActivityCompat.getColor(ShopActivity.this, R.color.white));
//                    bottomView.setBackgroundColor(ActivityCompat.getColor(ShopActivity.this, R.color.white));
                    imgSou.setImageDrawable(ActivityCompat.getDrawable(ShopActivity.this, R.drawable.ic_sousuo_white));
                }
            }
        });
        final String[] tabTitles = {"全店推荐", "全部宝贝", "店铺详情"};
        vpMain.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public CharSequence getPageTitle(int position) {
                return tabTitles[position];
            }

            @Override
            public Fragment getItem(int position) {
//                return new ShopFragment();
                switch (position) {
                    case 0:
                        return fragGoodsRecommand;
                    case 1:
                        return fragGoodsShop;
                    default:
                        return ShopFragment.newInstance(shopId, "");
                }
            }

            @Override
            public int getCount() {
                return tabTitles.length;
            }
        });
        tabMain.setViewPager(vpMain);
        tabMainTop.setViewPager(vpMain);
        vpMain.setCurrentItem(0);

        loadData();
    }

    private void loadData() {
        setLoding(this, false);
        new BizDataAsyncTask<ShopModel>() {
            @Override
            protected ShopModel doExecute() throws ZYException, BizFailure {
                closeLoding();
//                String shopId = "001";
                String shopId = getIntent().getStringExtra(TAG_SHOP_ID);
                Map<String, String> data = new HashMap<>();
                data.put("busno", shopId);
                data.put("lat", SingleCurrentUser.location.getLatitude() + "");
                data.put("lng", SingleCurrentUser.location.getLongitude() + "");
                return GoodsBiz.loadShopInfo(GsonUtils.Bean2Json(data));
            }

            @Override
            protected void onExecuteSucceeded(ShopModel shop) {
                closeLoding();
                shopModel = shop;
                bindData2View();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    private void bindData2View() {
        if (shopModel == null) return;
        String str1 = "蓝九天大药房(" + shopModel.getLOGISTICS_COMPANY() + ")";
        String str2 = "￥" + shopModel.getQISONG() + "起送|￥" + shopModel.getBAOYOU() + "包邮  " + shopModel.getDISTANCE_TEXT() + "|" + shopModel.getDURATION_TEXT();
        StringBuilder sb = new StringBuilder(str1 + "\n" + str2);
//        SpannableString sbs = new SpannableString(sb.toString());
//        sbs.setSpan(new AbsoluteSizeSpan(55), 0, str1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvShopname.setText(str1);
        tvShopinfo.setText(str2);
    }


}
