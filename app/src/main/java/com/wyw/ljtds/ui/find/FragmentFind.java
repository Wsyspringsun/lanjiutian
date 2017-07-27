package com.wyw.ljtds.ui.find;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sunfusheng.marqueeview.MarqueeView;
import com.wyw.ljtds.MainActivity;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.goodsinfo.NetworkImageHolderView;
import com.wyw.ljtds.biz.biz.HomeBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.HeadlineBean;
import com.wyw.ljtds.model.HomePageModel;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.ui.category.ActivityScan;
import com.wyw.ljtds.ui.goods.ActivityGoodsImages;
import com.wyw.ljtds.ui.goods.ActivityMedicineList;
import com.wyw.ljtds.ui.goods.ActivityMedicinesInfo;
import com.wyw.ljtds.ui.home.ActivityGuide;
import com.wyw.ljtds.ui.home.ActivityHomeWeb;
import com.wyw.ljtds.ui.home.ActivitySearch;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.widget.MyScrollView;
import com.wyw.ljtds.widget.banner.TaobaoHeadline;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/9 0009.
 */

@ContentView(R.layout.fragment_find)
public class FragmentFind extends BaseFragment {

    public static final String TAG_FIND_MTD = "com.wyw.ljtds.ui.find.FragmentFind.TAG_FIND_MTD";
    public static final String FIND_MTD_QUICK = "1";
    public static final String FIND_MTD_QUICK2 = "2";
    public static final String TAG_MTD_QUICK_PARAM = "com.wyw.ljtds.ui.find.FragmentFind.TAG_MTD_QUICK_PARAM";


    @ViewInject(R.id.right_text)
    private TextView right_text;
    @ViewInject(R.id.right_img)
    private ImageView right_img;
    @ViewInject(R.id.sv)
    private MyScrollView myScrollView;
    @ViewInject(R.id.llHeadr)
    private LinearLayout header;
    @ViewInject(R.id.banner)
    private ConvenientBanner banner;
    //    @ViewInject(R.id.RushBuyCountDownTimerView)
//    private SnapUpCountDownTimerView snapUpCountDownTimerView;
    @ViewInject(R.id.qianggou_rv)
    private RecyclerView recyclerView;
    @ViewInject(R.id.reclcyer)
    private RecyclerView reclcyer;
    @ViewInject(R.id.reclcyer1)
    private RecyclerView reclcyer1;
    @ViewInject(R.id.sdv_head_img1)
    private SimpleDraweeView simpleDraweeView1;
    @ViewInject(R.id.sdv_head_img2)
    private SimpleDraweeView simpleDraweeView2;
    @ViewInject(R.id.sdv_head_img3)
    private SimpleDraweeView simpleDraweeView3;
    @ViewInject(R.id.marqueeView)
    private MarqueeView marqueeView;


    // 轮播部分
    private List<String> mList;
    //广播部分
    private List<String> data;
    //活动药品
    private List<HomePageModel.activeComms> list;
    private MyAdapter adapter;
    //推荐药品
    private List<HomePageModel.CLASS> list1;
    private MyAdapter1 adapter1;
    private List<HomePageModel.DETAILS> list2;
    private MyAdapter2 adapter2;

    private MainActivity activity;
    //客服
    String groupName = "蓝九天医师";// 客服组默认名称
    String settingid1 = "lj_1000_1495596285901";

    private String index = "";//推荐分类选中

    @Event(value = {R.id.ll_search, R.id.zxing, R.id.ll_message, R.id.button1, R.id.button2, R.id.button3,
            R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.sel_yigan, R.id.sel_yangwei,
            R.id.sel_fengshi, R.id.sel_xiaochuan, R.id.sel_tuofa, R.id.sel_yiyu})
    private void onclick(View view) {
        Intent it;
        String classId = "";
        switch (view.getId()) {
            case R.id.ll_search:
                it = new Intent(getActivity(), ActivitySearch.class);
                it.putExtra("from", 0);
                startActivity(it);
                break;

            case R.id.zxing:
                startActivity(new Intent(getActivity(), ActivityScan.class));
                break;

            case R.id.ll_message:
                startActivity(new Intent(getActivity(), ActivityMapLocation.class));
                break;

            case R.id.button1:
                startActivity(new Intent(getActivity(), ActivityFindCategort.class));
                break;

            case R.id.button2:
                it = new Intent(getActivity(), ActivityMedicineList.class);
                //男性
//                classId = "2025";
                classId = "2731";
                it.putExtra(TAG_FIND_MTD, FIND_MTD_QUICK);
                it.putExtra(TAG_MTD_QUICK_PARAM, classId);
                startActivity(it);
                break;

            case R.id.button3:
                it = new Intent(getActivity(), ActivityMedicineList.class);
                //女性
//                classId = "2024";
                classId = "2732";
                it.putExtra(TAG_FIND_MTD, FIND_MTD_QUICK);
                it.putExtra(TAG_MTD_QUICK_PARAM, classId);
                startActivity(it);
                break;

            case R.id.button4:
                //慢性
                classId = "0205";
                it = new Intent(getActivity(), ActivityMedicineList.class);
                it.putExtra(TAG_FIND_MTD, FIND_MTD_QUICK2);
                it.putExtra(TAG_MTD_QUICK_PARAM, classId);
                startActivity(it);
                break;
            case R.id.button5:
                //保健
                it = new Intent(getActivity(), ActivityMedicineList.class);
                classId = "03";
                it.putExtra(TAG_FIND_MTD, FIND_MTD_QUICK2);
                it.putExtra(TAG_MTD_QUICK_PARAM, classId);
                startActivity(it);
                break;
            case R.id.button6:
                //器械
                it = new Intent(getActivity(), ActivityMedicineList.class);
                classId = "05";
                it.putExtra(TAG_FIND_MTD, FIND_MTD_QUICK2);
                it.putExtra(TAG_MTD_QUICK_PARAM, classId);
                startActivity(it);
                break;
            case R.id.button7:
                //成人
                classId = "12";
                it = new Intent(getActivity(), ActivityMedicineList.class);
                it.putExtra(TAG_FIND_MTD, FIND_MTD_QUICK2);
                it.putExtra(TAG_MTD_QUICK_PARAM, classId);
                startActivity(it);
                break;
            case R.id.sel_yangwei:
                //阳痿
                classId = "2703";
                it = new Intent(getActivity(), ActivityMedicineList.class);
                it.putExtra(TAG_FIND_MTD, FIND_MTD_QUICK);
                it.putExtra(TAG_MTD_QUICK_PARAM, classId);
                startActivity(it);
                break;
            case R.id.sel_yigan:
                //乙肝
                classId = "2682";
                it = new Intent(getActivity(), ActivityMedicineList.class);
                it.putExtra(TAG_FIND_MTD, FIND_MTD_QUICK);
                it.putExtra(TAG_MTD_QUICK_PARAM, classId);
                startActivity(it);
                break;
            case R.id.sel_fengshi:
                classId = "2695";
                it = new Intent(getActivity(), ActivityMedicineList.class);
                it.putExtra(TAG_FIND_MTD, FIND_MTD_QUICK);
                it.putExtra(TAG_MTD_QUICK_PARAM, classId);
                startActivity(it);
                break;
            case R.id.sel_xiaochuan:
                classId = "2769";
                it = new Intent(getActivity(), ActivityMedicineList.class);
                it.putExtra(TAG_FIND_MTD, FIND_MTD_QUICK);
                it.putExtra(TAG_MTD_QUICK_PARAM, classId);
                startActivity(it);
                break;
            case R.id.sel_tuofa:
                classId = "2770";
                it = new Intent(getActivity(), ActivityMedicineList.class);
                it.putExtra(TAG_FIND_MTD, FIND_MTD_QUICK);
                it.putExtra(TAG_MTD_QUICK_PARAM, classId);
                startActivity(it);
                break;
            case R.id.sel_yiyu:
                classId = "2799";
                it = new Intent(getActivity(), ActivityMedicineList.class);
                it.putExtra(TAG_FIND_MTD, FIND_MTD_QUICK);
                it.putExtra(TAG_MTD_QUICK_PARAM, classId);
                startActivity(it);
                break;
            case R.id.button8:
                activity.openChat("首页咨询", "", settingid1, groupName, false, "");
                break;
        }
    }

    //滑动改变状态栏标题栏
    @Event(value = R.id.sv, type = MyScrollView.ScrollViewListener.class)
    private void scrollListenter(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
        Log.d("alan", "oldy--->" + oldy);
        Log.d("alan", "y---->" + y);
        int ivHeight = banner.getHeight();

        if (y <= 0) {
            header.setBackgroundColor(Color.TRANSPARENT);
        } else if (y < ivHeight) {
            float scale = (float) y / (float) ivHeight;
            float alpha = 255 * scale;
            // TODO: 2016/9/3 注释里面的方法也可以实现
//            llHeader.setBackgroundColor(Color.argb((int) alpha, 144, 151, 166));


            //先设置一个背景，然后在让背景乘以透明度
            header.setBackgroundColor(getResources().getColor(R.color.base_bar));
            header.getBackground().setAlpha((int) alpha);

        } else if (y >= ivHeight) {
//            llHeader.setAlpha(1);
//            llHeader.setBackgroundColor(Color.argb((int) 255, 144, 151, 166));


            header.setBackgroundColor(getResources().getColor(R.color.base_bar));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        right_img.setImageResource(R.mipmap.icon_dingwei);
        right_text.setText("定位");

        myScrollView.setVerticalScrollBarEnabled(false);

        getHome();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());//必须有
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//设置方向滑动
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new MyAdapter();
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent it = new Intent(getActivity(), ActivityMedicinesInfo.class);
                it.putExtra(AppConfig.IntentExtraKey.MEDICINE_INFO_ID, adapter.getData().get(i).getWAREID());
                startActivity(it);
            }
        });


        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());//必须有
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);//设置方向滑动
        reclcyer.setLayoutManager(linearLayoutManager1);

        adapter1 = new MyAdapter1();
        reclcyer.setAdapter(adapter1);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());//必须有
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);//设置方向滑动
        reclcyer1.setLayoutManager(linearLayoutManager2);

        adapter2 = new MyAdapter2();
        reclcyer1.setAdapter(adapter2);
        reclcyer1.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent it = new Intent(getActivity(), ActivityMedicinesInfo.class);
                it.putExtra(AppConfig.IntentExtraKey.MEDICINE_INFO_ID, adapter2.getData().get(i).getWAREID());
                startActivity(it);
            }
        });


        reclcyer.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                index = adapter1.getData().get(i).getCLASSCODE();
                adapter1.notifyDataSetChanged();

                list2 = adapter1.getData().get(i).getDETAILS();
                adapter2.setNewData(list2);
                adapter2.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            data = null;
            marqueeView.stopFlipping();
        } else {
            if (AppConfig.currSel == 2) {
                getHome();
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        marqueeView.startFlipping();
        //开始自动翻页
        banner.startTurning(3000);
    }

    @Override
    public void onStop() {
        super.onStop();
        marqueeView.stopFlipping();
        //停止翻页
        banner.stopTurning();
    }


    BizDataAsyncTask<HomePageModel> homeTask;

    private void getHome() {
        homeTask = new BizDataAsyncTask<HomePageModel>() {
            @Override
            protected HomePageModel doExecute() throws ZYException, BizFailure {
                return HomeBiz.getHome();
            }

            @Override
            protected void onExecuteSucceeded(HomePageModel homePageModel) {


                //首页轮播图
                mList = new ArrayList<>();
                String[] img = homePageModel.getHeadImgs();
                for (int i = 0; i < img.length; i++) {
                    if (!StringUtils.isEmpty(img[i])) {
                        mList.add(img[i]);
                    }
                }

                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                if (mList != null && !mList.isEmpty()) {
                    banner.setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused});
                    banner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
                    banner.setPages(new CBViewHolderCreator() {
                        @Override
                        public Object createHolder() {
                            return new NetworkImageHolderView();
                        }
                    }, mList);
                }


                //广播新闻
                data = new ArrayList<>();
                for (int i = 0; i < homePageModel.getNews().size(); i++) {
                    data.add(homePageModel.getNews().get(i).getTITLE());
                }
                marqueeView.startWithList(data);


                //抢购
//                snapUpCountDownTimerView.setTime( 1, 59, 59 );//设置倒计时
//                //开始倒计时
//                snapUpCountDownTimerView.start();
                list = new ArrayList<>();
                list = homePageModel.getActiveComms();
                adapter.setNewData(list);
                adapter.notifyDataSetChanged();


                //推荐药品
                index = homePageModel.getRecommendComms().get(0).getCLASSCODE();
                list1 = new ArrayList<>();
                list1 = homePageModel.getRecommendComms();
                adapter1.setNewData(list1);
                adapter1.notifyDataSetChanged();
                list2 = new ArrayList<>();
                list2 = homePageModel.getRecommendComms().get(0).getDETAILS();
                adapter2.setNewData(list2);
                adapter2.notifyDataSetChanged();

                if (StringUtils.isEmpty(homePageModel.getAdvImgs()[0])) {
                    simpleDraweeView1.setImageURI(Uri.parse(""));
                } else {
                    simpleDraweeView1.setImageURI(Uri.parse(homePageModel.getAdvImgs()[0]));
                }

                if (StringUtils.isEmpty(homePageModel.getAdvImgs()[1])) {
                    simpleDraweeView2.setImageURI(Uri.parse(""));
                } else {
                    simpleDraweeView2.setImageURI(Uri.parse(homePageModel.getAdvImgs()[1]));
                }

                if (StringUtils.isEmpty(homePageModel.getAdvImgs()[2])) {
                    simpleDraweeView3.setImageURI(Uri.parse(""));
                } else {
                    simpleDraweeView3.setImageURI(Uri.parse(homePageModel.getAdvImgs()[2]));
                }


            }

            @Override
            protected void OnExecuteFailed() {
                //停止倒计时
//                snapUpCountDownTimerView.stop();
            }
        };
        homeTask.execute();
    }


    private class MyAdapter extends BaseQuickAdapter<HomePageModel.activeComms> {
        public MyAdapter() {
            super(R.layout.item_find_snap, list);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, HomePageModel.activeComms activeComms) {
            baseViewHolder.setText(R.id.name, StringUtils.deletaFirst(activeComms.getWARENAME()))
                    .setText(R.id.money, activeComms.getSALEPRICE() + "");

            SimpleDraweeView simpleDraweeView = baseViewHolder.getView(R.id.head_img);
            if (!StringUtils.isEmpty(activeComms.getIMG_PATH())) {
                simpleDraweeView.setImageURI(Uri.parse(activeComms.getIMG_PATH()));
            }

        }
    }


    private class MyAdapter1 extends BaseQuickAdapter<HomePageModel.CLASS> {

        public MyAdapter1() {
            super(R.layout.item_find_tuijian, list1);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, HomePageModel.CLASS classess) {
            baseViewHolder.setText(R.id.text, StringUtils.deletaFirst(classess.getCLASSNAME()));
            if (index.equals(classess.getCLASSCODE())) {
                baseViewHolder.setTextColor(R.id.text, getResources().getColor(R.color.base_bar))
                        .setVisible(R.id.views, true);
            } else {
                baseViewHolder.setTextColor(R.id.text, getResources().getColor(R.color.font_black2))
                        .setVisible(R.id.views, false);
            }
        }
    }


    private class MyAdapter2 extends BaseQuickAdapter<HomePageModel.DETAILS> {

        public MyAdapter2() {
            super(R.layout.item_find_snap, list2);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, HomePageModel.DETAILS details) {
            baseViewHolder.setText(R.id.name, StringUtils.deletaFirst(details.getWARENAME()))
                    .setText(R.id.money, details.getSALEPRICE() + "");

            SimpleDraweeView simpleDraweeView = baseViewHolder.getView(R.id.head_img);
            if (!StringUtils.isEmpty(details.getIMG_PATH())) {
                simpleDraweeView.setImageURI(Uri.parse(details.getIMG_PATH()));
            }
        }
    }

}
