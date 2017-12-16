package com.wyw.ljtds.ui.find;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.sunfusheng.marqueeview.MarqueeView;
import com.wyw.ljtds.MainActivity;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.goodsinfo.NetworkImageHolderView;
import com.wyw.ljtds.biz.biz.HomeBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.HomePageModel;
import com.wyw.ljtds.model.IConCatInfo;
import com.wyw.ljtds.model.NewsModel;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.ui.category.ActivityScan;
import com.wyw.ljtds.ui.goods.ActivityMedicineList;
import com.wyw.ljtds.ui.goods.ActivityMedicinesInfo;
import com.wyw.ljtds.ui.goods.ShopActivity;
import com.wyw.ljtds.ui.home.ActivityHomeWeb;
import com.wyw.ljtds.ui.home.ActivitySearch;
import com.wyw.ljtds.ui.home.HuoDongActivity;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.MyScrollView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/9 0009.
 */

@ContentView(R.layout.fragment_find)
public class FragmentFind extends BaseFragment {

    //    public static final String TAG_FIND_MTD = "com.wyw.ljtds.ui.find.FragmentFind.TAG_FIND_MTD";
    public static final String FIND_MTD_QUICK = "1";
    public static final String FIND_MTD_QUICK2 = "2";
    public static final String FIND_MTD_QUICK3 = "3";
    public static final String TAG_MTD_QUICK_PARAM = "com.wyw.ljtds.ui.find.FragmentFind.TAG_MTD_QUICK_PARAM";


    @ViewInject(R.id.ll_message)
    private LinearLayout llMsg;

    @ViewInject(R.id.right_text)
    private TextView right_text;
    @ViewInject(R.id.tv_jt_djs)
    private TextView tvYqq;

    @ViewInject(R.id.right_img)
    private ImageView right_img;
    @ViewInject(R.id.sv)
    private MyScrollView myScrollView;
    @ViewInject(R.id.llHeadr)
    private LinearLayout header;
    @ViewInject(R.id.banner)
    private ConvenientBanner banner; //轮播图
    //    @ViewInject(R.id.RushBuyCountDownTimerView)
//    private SnapUpCountDownTimerView snapUpCountDownTimerView;
    @ViewInject(R.id.qianggou_rv)
    private RecyclerView recyclerView; //一起抢区域
    @ViewInject(R.id.reclcyer)
    private RecyclerView reclcyer;
    @ViewInject(R.id.reclcyer1)
    private RecyclerView reclcyer1;
    //    @ViewInject(R.id.fragment_find_rv_huodong)
//    private RecyclerView rcHuodong; //活动入口图片集合
    @ViewInject(R.id.sdv_head_img1)
    private SimpleDraweeView simpleDraweeView1; //大幅广告图片
    @ViewInject(R.id.sdv_head_img2)
    private SimpleDraweeView simpleDraweeView2;
    @ViewInject(R.id.sdv_head_img3)
    private SimpleDraweeView simpleDraweeView3;
    @ViewInject(R.id.marqueeView)
    private MarqueeView marqueeView;

    //咨询客服 的图标按钮
    @ViewInject(R.id.fragment_find_btn_consult)
    private FloatingActionButton btnConsult;


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
    private HomePageModel homePageData;
    private Dialog dialogConsult;

    @Event(value = {R.id.ll_search, R.id.zxing, R.id.ll_message, R.id.button1, R.id.button2, R.id.button3,
            R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.btn_kefu, R.id.sel_yigan, R.id.sel_yangwei,
            R.id.sel_fengshi, R.id.sel_xiaochuan, R.id.sel_tuofa, R.id.sel_yiyu, R.id.jt_djs, R.id.tv_jt_djs})
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
                it.putExtra(ActivityMedicineList.TAG_LIST_FROM, FIND_MTD_QUICK);
                it.putExtra(TAG_MTD_QUICK_PARAM, classId);
                startActivity(it);
                break;

            case R.id.button3:
                it = new Intent(getActivity(), ActivityMedicineList.class);
                //女性
//                classId = "2024";
                classId = "2732";
                it.putExtra(ActivityMedicineList.TAG_LIST_FROM, FIND_MTD_QUICK);
                it.putExtra(TAG_MTD_QUICK_PARAM, classId);
                startActivity(it);
                break;

            case R.id.button4:
                //慢性
                classId = "0205";
                it = new Intent(getActivity(), ActivityMedicineList.class);
                it.putExtra(ActivityMedicineList.TAG_LIST_FROM, FIND_MTD_QUICK2);
                it.putExtra(TAG_MTD_QUICK_PARAM, classId);
                startActivity(it);
                break;
            case R.id.button5:
                //保健
                it = new Intent(getActivity(), ActivityMedicineList.class);
                classId = "03";
                it.putExtra(ActivityMedicineList.TAG_LIST_FROM, FIND_MTD_QUICK2);
                it.putExtra(TAG_MTD_QUICK_PARAM, classId);
                startActivity(it);
                break;
            case R.id.button6:
                //器械
                it = new Intent(getActivity(), ActivityMedicineList.class);
                classId = "05";
                it.putExtra(ActivityMedicineList.TAG_LIST_FROM, FIND_MTD_QUICK2);
                it.putExtra(TAG_MTD_QUICK_PARAM, classId);
                startActivity(it);
                break;
            case R.id.button7:
                //成人
                classId = "12";
                it = new Intent(getActivity(), ActivityMedicineList.class);
                it.putExtra(ActivityMedicineList.TAG_LIST_FROM, FIND_MTD_QUICK2);
                it.putExtra(TAG_MTD_QUICK_PARAM, classId);
                startActivity(it);
                break;
            case R.id.sel_yangwei:
                //阳痿
                classId = "2703";
                it = new Intent(getActivity(), ActivityMedicineList.class);
                it.putExtra(ActivityMedicineList.TAG_LIST_FROM, FIND_MTD_QUICK);
                it.putExtra(TAG_MTD_QUICK_PARAM, classId);
                startActivity(it);
                break;
            case R.id.sel_yigan:
                //乙肝
                classId = "3715";
                it = new Intent(getActivity(), ActivityMedicineList.class);
                it.putExtra(ActivityMedicineList.TAG_LIST_FROM, FIND_MTD_QUICK);
                it.putExtra(TAG_MTD_QUICK_PARAM, classId);
                startActivity(it);
                break;
            case R.id.sel_fengshi:
                classId = "2695";
                it = new Intent(getActivity(), ActivityMedicineList.class);
                it.putExtra(ActivityMedicineList.TAG_LIST_FROM, FIND_MTD_QUICK);
                it.putExtra(TAG_MTD_QUICK_PARAM, classId);
                startActivity(it);
                break;
            case R.id.sel_xiaochuan:
                classId = "3716";
                it = new Intent(getActivity(), ActivityMedicineList.class);
                it.putExtra(ActivityMedicineList.TAG_LIST_FROM, FIND_MTD_QUICK);
                it.putExtra(TAG_MTD_QUICK_PARAM, classId);
                startActivity(it);
                break;
            case R.id.sel_tuofa:
                classId = "3714";
                it = new Intent(getActivity(), ActivityMedicineList.class);
                it.putExtra(ActivityMedicineList.TAG_LIST_FROM, FIND_MTD_QUICK);
                it.putExtra(TAG_MTD_QUICK_PARAM, classId);
                startActivity(it);
                break;
            case R.id.sel_yiyu:
                classId = "2699";
                it = new Intent(getActivity(), ActivityMedicineList.class);
                it.putExtra(ActivityMedicineList.TAG_LIST_FROM, FIND_MTD_QUICK);
                it.putExtra(TAG_MTD_QUICK_PARAM, classId);
                startActivity(it);
                break;
            case R.id.btn_kefu:
                activity.openChat("首页咨询", "", settingid1, groupName, false, "");
                break;
            case R.id.jt_djs:
            case R.id.tv_jt_djs:
                //活动 --- 更多
//                it = new Intent(getActivity(), ActivityMedicineList.class);
//                it.putExtra(ActivityMedicineList.TAG_LIST_FROM, FIND_MTD_QUICK3);
//                it.putExtra(TAG_MTD_QUICK_PARAM, "3");
//                startActivity(it);

                //进入一起抢 秒杀活动

                it = HuoDongActivity.getIntent(getActivity(), "4");
                getActivity().startActivity(it);
                break;
            default:
                break;
        }
    }

    //滑动改变状态栏标题栏
    @Event(value = R.id.sv, type = MyScrollView.ScrollViewListener.class)
    private void scrollListenter(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
//        Log.d("alan", "oldy--->" + oldy);
//        Log.d("alan", "y---->" + y);
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

        llMsg.setVisibility(View.GONE);

        Utils.setIconText(getActivity(), tvYqq, "\ue660");

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

        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                Intent it = new Intent(getActivity(), ActivityHomeWeb.class);
                if (homePageData == null) return;
                if (homePageData.getNews() == null) return;
                List<NewsModel> news = homePageData.getNews();
                it.putExtra(AppConfig.IntentExtraKey.Home_News, news.get(position % news.size()).getSUMMARY());
                startActivity(it);
            }
        });

        btnConsult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentFind.this.showConsultImtes((int) v.getX(), (int) v.getY());
            }
        });

        //活动入口 图片数据
//        GridLayoutManager glm = new GridLayoutManager(getActivity(), 2);
//        rcHuodong.setLayoutManager(glm);
//        updHuodongAdapter();


    }

//    private void updHuodongAdapter() {
//        IconListAdapter myAdapter;
//        ArrayList<IConCatInfo> lsHuodongImg = new ArrayList<>();
//
//        IConCatInfo i1 = new IConCatInfo();
//        i1.setImgPath(AppConfig.IMAGE_PATH_LJT + "/.system_1/img_shenghuo_active.jpg");
//        lsHuodongImg.add(i1);
//
//        IConCatInfo i2 = new IConCatInfo();
//        i2.setImgPath(AppConfig.IMAGE_PATH_LJT + "/.system_1/img_shenghuo_active.jpg");
//        lsHuodongImg.add(i2);
//
//        IConCatInfo i3 = new IConCatInfo();
//        i3.setImgPath(AppConfig.IMAGE_PATH_LJT + "/.system_1/img_shenghuo_active.jpg");
//        lsHuodongImg.add(i3);
//
//        IConCatInfo i4 = new IConCatInfo();
//        i4.setImgPath(AppConfig.IMAGE_PATH_LJT + "/.system_1/img_shenghuo_active.jpg");
//        lsHuodongImg.add(i4);
//
//        myAdapter = new IconListAdapter(lsHuodongImg);
//        rcHuodong.setAdapter(myAdapter);
//    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (dialogConsult != null) {
                dialogConsult.dismiss();
            }

            if (marqueeView != null) {
                marqueeView.stopFlipping();
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

        if (dialogConsult != null) {
            dialogConsult.dismiss();
        }
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
                homePageData = homePageModel;

                //首页轮播图
                mList = new ArrayList<>();
                List<Map<String, String>> img = homePageModel.getHeadImgsList();
                if (img != null) {
                    for (int i = 0; i < img.size(); i++) {
                        String imgSrc = img.get(i).get("headImgsUrl");
                        if (!StringUtils.isEmpty(imgSrc)) {
                            mList.add(imgSrc);
                        }
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
                    Utils.log("getHeadImgsList:" + GsonUtils.Bean2Json(homePageData.getHeadImgsList()));
                    //{"flg":"X","headId":"","headImgsUrl":"http://www.lanjiutian.com/upload/images/ef4118c10a1b432ba4e31b674d7f582c/d2a57dc1d883fd21fb9951699df71cc7.jpg"}
                    banner.setOnItemClickListener(new com.bigkoo.convenientbanner.listener.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Map<String, String> mItem = homePageData.getHeadImgsList().get(position);
                            String flg = mItem.get("flg");
                            String headId = mItem.get("headId");
                            //详情：X,列表 L 店铺 D 电子币 DZ 抽奖 CJ 满赠 MZ 特价 TJ 秒杀 MS
                            Intent it = null;
                            switch (flg) {
                                case "X":
                                    it = ActivityMedicinesInfo.getIntent(getActivity(), headId);
                                    break;
                                case "L":
                                    break;
                                case "D":
                                    it = ShopActivity.getIntent(getActivity(), headId);
                                    break;
                                case "DZ":
                                    it = HuoDongActivity.getIntent(getActivity(), "5");
                                    break;
                                case "CJ":
                                    it = HuoDongActivity.getIntent(getActivity(), "1");
                                    break;
                                case "MZ":
                                    it = HuoDongActivity.getIntent(getActivity(), "2");
                                    break;
                                case "TJ":
                                    it = HuoDongActivity.getIntent(getActivity(), "3");
                                    break;
                                case "MS":
                                    it = HuoDongActivity.getIntent(getActivity(), "4");
                                    break;
                                default:
                                    break;
                            }
                            if (it != null)
                                getActivity().startActivity(it);
                        }
                    });
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

                //simpleDraweeView1.setOnClickListener(new ActiveListner("3", FIND_MTD_QUICK3));
                simpleDraweeView1.setOnClickListener(new ActiveListner("3", "1"));
                // 保健 03
                //simpleDraweeView2.setOnClickListener(new ActiveListner("03", FIND_MTD_QUICK2));
                simpleDraweeView2.setOnClickListener(new ActiveListner("03", "3"));
                // 母婴     13
//                simpleDraweeView3.setOnClickListener(new ActiveListner("13", FIND_MTD_QUICK2));
                simpleDraweeView3.setOnClickListener(new ActiveListner("13", "2"));


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
                    .setText(R.id.money, "￥" + activeComms.getSALEPRICE() + "");

            SimpleDraweeView simpleDraweeView = baseViewHolder.getView(R.id.head_img);
            if (!StringUtils.isEmpty(activeComms.getIMG_PATH())) {
//                Log.e(AppConfig.ERR_TAG, "imgPath:" + activeComms.getIMG_PATH());
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

    private class ActiveListner implements View.OnClickListener {
        private String id;
        private String mtd;

        public ActiveListner(String id, String mtd) {
            this.id = id;
            this.mtd = mtd;
        }

        @Override
        public void onClick(View v) {
            Intent it;
            switch (mtd) {
                case "1":
                case "2":
                case "3":
                    //进入活动页面
                    it = HuoDongActivity.getIntent(getActivity(), mtd);
                    getActivity().startActivity(it);
                    break;
                default:
                    it = new Intent(getActivity(), ActivityMedicineList.class);
                    it.putExtra(ActivityMedicineList.TAG_LIST_FROM, mtd);
                    it.putExtra(TAG_MTD_QUICK_PARAM, ActiveListner.this.id);
                    startActivity(it);
                    break;
            }
        }
    }


    //显示客服 或者 电话 的 模态窗口
    public void showConsultImtes(int viewX, int viewY) {
        if (dialogConsult == null) {
            dialogConsult = new Dialog(getActivity(), R.style.Theme_AppCompat_Dialog);
            LayoutInflater inflater = getActivity().getLayoutInflater();

            View layout = inflater.inflate(R.layout.fragment_consult_items, null);
            View itemKefu = layout.findViewById(R.id.fragment_consult_ll_kefu);
            itemKefu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogConsult.dismiss();
                    activity.openChat("首页咨询", "", settingid1, groupName, false, "");
                }
            });
            View itemTel = layout.findViewById(R.id.fragment_consult_ll_tel);
            itemTel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogConsult.dismiss();
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + AppConfig.LJG_TEL));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });

            dialogConsult.setContentView(layout);
            dialogConsult.setCancelable(true);

            Window dialogWindow = dialogConsult.getWindow();
//        dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialogWindow.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
            //显示的坐标
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            //获取整个屏幕的宽度和高度
            WindowManager wm = (WindowManager) getContext()
                    .getSystemService(Context.WINDOW_SERVICE);

//            int wWidth = wm.getDefaultDisplay().getWidth();
            int wHeight = wm.getDefaultDisplay().getHeight();
            lp.y = wHeight - viewY;
            //内容 透明度
//            Log.e(AppConfig.ERR_TAG, lp.y + ":" + lp.x);
//        lp.alpha = 0.2f;
            //遮罩 透明度
            lp.dimAmount = 0.2f;
//        //dialog的大小
            dialogWindow.setAttributes(lp);

        }
        dialogConsult.show();

    }


//    class HuodongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        IConCatInfo itemData;
//        ImageView icon;
//
//        public HuodongViewHolder(View itemView) {
//            super(itemView);
//            icon = (ImageView) itemView.findViewById(R.id.item_list_icon);
//            itemView.setOnClickListener(this);
//        }
//
//        @Override
//        public void onClick(View v) {
//            Log.e(AppConfig.ERR_TAG, "itemData:" + itemData.getName());
//            Intent it = ChouJiangActivity.getIntent(getActivity(), "");
//            getActivity().startActivity(it);
//        }
//    }

//    class IconListAdapter extends RecyclerView.Adapter<HuodongViewHolder> {
//        List<IConCatInfo> list;
//
//        public IconListAdapter(List<IConCatInfo> list) {
//            this.list = list;
//        }
//
//        @Override
//        public HuodongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            LayoutInflater inflater = LayoutInflater.from(getActivity());
//            View view = inflater.inflate(R.layout.item_list_icon, parent, false);
//            return new HuodongViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(HuodongViewHolder holder, int position) {
//            IConCatInfo itemData = list.get(position);
//            holder.itemData = itemData;
//            String imgUrl = itemData.getImgPath();
//            Picasso.with(getActivity()).load(imgUrl).into(holder.icon);
//        }
//
//        @Override
//        public int getItemCount() {
//            return list.size();
//        }
//    }
}
