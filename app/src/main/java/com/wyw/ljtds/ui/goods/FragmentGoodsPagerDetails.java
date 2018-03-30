package com.wyw.ljtds.ui.goods;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.CommodityDetailsModel;
import com.wyw.ljtds.model.MedicineDetailsModel;
import com.wyw.ljtds.ui.base.BaseFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/12 0012.
 */

@ContentView(R.layout.fragment_goods_detail)
public class FragmentGoodsPagerDetails extends BaseFragment {
    @ViewInject(R.id.ll_goods_detail)
    private LinearLayout ll_goods_detail;
    @ViewInject(R.id.ll_goods_config)
    private LinearLayout ll_goods_config;
    @ViewInject(R.id.tv_goods_detail)
    private TextView tv_goods_detail;
    @ViewInject(R.id.tv_goods_config)
    private TextView tv_goods_config;
    @ViewInject(R.id.fl_content)
    private FrameLayout fl_content;
    @ViewInject(R.id.v_tab_cursor)
    private View v_tab_cursor;

    public FragmentGoodsParameter fragmentGoodsParameter;
    public FragmentGoodsDetails fragmentGoodsDetails;
    private Fragment nowFragment;//当前是哪个fragment
    private int nowIndex;//fragment判定下标
    private float fromX;
    private List<TextView> tabTextList;
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;


    @Event(value = {R.id.ll_goods_detail, R.id.ll_goods_config})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_goods_detail:
                //商品详情tab
                switchFragment(nowFragment, fragmentGoodsDetails);
                nowIndex = 0;
                nowFragment = fragmentGoodsDetails;
                scrollCursor();
                break;

            case R.id.ll_goods_config:
                //规格参数tab
                switchFragment(nowFragment, fragmentGoodsParameter);
                nowIndex = 1;
                nowFragment = fragmentGoodsParameter;
                scrollCursor();
                break;

            default:
                break;
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(AppConfig.ERR_TAG, "GoodsPagerDetails   onActivityCreated........");

        tabTextList = new ArrayList<>();
        tabTextList.add(tv_goods_detail);
        tabTextList.add(tv_goods_config);

        fragmentGoodsDetails = new FragmentGoodsDetails();
        fragmentGoodsParameter = new FragmentGoodsParameter();
        nowFragment = fragmentGoodsDetails;
        fragmentManager = getChildFragmentManager();
        //默认显示商品详情tab
        fragmentManager.beginTransaction().replace(R.id.fl_content, nowFragment).commitAllowingStateLoss();
    }

    /**
     * 滑动游标
     */
    private void scrollCursor() {
        TranslateAnimation anim = new TranslateAnimation(fromX, nowIndex * v_tab_cursor.getWidth(), 0, 0);
        anim.setFillAfter(true);//设置动画结束时停在动画结束的位置
        anim.setDuration(50);
        //保存动画结束时游标的位置,作为下次滑动的起点
        fromX = nowIndex * v_tab_cursor.getWidth();
        v_tab_cursor.startAnimation(anim);

        //设置Tab切换颜色
        for (int i = 0; i < tabTextList.size(); i++) {
            tabTextList.get(i).setTextColor(i == nowIndex ? getResources().getColor(R.color.base_bar) : getResources().getColor(R.color.font_black2));
        }
    }

    /**
     * 切换Fragment
     * <p>(hide、show、add)
     *
     * @param fromFragment
     * @param toFragment
     */
    private void switchFragment(Fragment fromFragment, Fragment toFragment) {
        if (nowFragment != toFragment) {
            fragmentTransaction = fragmentManager.beginTransaction();
            if (!toFragment.isAdded()) {    // 先判断是否被add过
                fragmentTransaction.hide(fromFragment).add(R.id.fl_content, toFragment).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到activity中
            } else {
                fragmentTransaction.hide(fromFragment).show(toFragment).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
            }

        }
    }

    public void bindData2View(MedicineDetailsModel medicineModel) {
        if (!isAdded()) return;
        if (medicineModel == null) return;
        this.fragmentGoodsDetails.bindData2View(medicineModel.getHTML_PATH());
        this.fragmentGoodsParameter.bindData2View(medicineModel);
    }

    public void bindData2View(CommodityDetailsModel commodityModel) {
        if (commodityModel == null) return;
        this.fragmentGoodsDetails.bindData2View(commodityModel.getHtmlPath());
        this.fragmentGoodsParameter.bindData2View(commodityModel);
    }
}
