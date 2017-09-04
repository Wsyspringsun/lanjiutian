package com.wyw.ljtds.ui.find;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
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
import com.wyw.ljtds.model.HomePageModel1;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.ui.category.ActivityScan;
import com.wyw.ljtds.ui.goods.ActivityGoodsInfo;
import com.wyw.ljtds.ui.goods.ActivityGoodsList;
import com.wyw.ljtds.ui.goods.ActivityMedicineList;
import com.wyw.ljtds.ui.home.ActivityHomeWeb;
import com.wyw.ljtds.ui.home.ActivitySearch;
import com.wyw.ljtds.ui.user.order.ActivityEvaluate;
import com.wyw.ljtds.ui.user.order.ActivityLogistics;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.widget.MyScrollView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/9 0009.
 */

@ContentView(R.layout.fragment_home)
public class FragmentHome extends BaseFragment {
    @ViewInject(R.id.sv)
    private MyScrollView myScrollView;
    @ViewInject(R.id.llHeadr)
    private LinearLayout header;
    @ViewInject(R.id.banner)
    private ConvenientBanner banner;
    @ViewInject(R.id.tuijian)
    private RecyclerView tuijian;
    @ViewInject(R.id.marqueeView)
    private MarqueeView marqueeView;
    @ViewInject(R.id.zxing)
    private LinearLayout zXing;//扫一扫

    // 轮播部分
    private List<String> mList;
    //广播部分
    private List<String> data;
    //推荐
    private List<HomePageModel1.RecommendComms> list1;


    private MyAdapter adapter;
    private MainActivity activity;
    //客服
    String settingid1 = "lj_1000_1493167191869";// 客服组id示例kf_9979_1452750735837
    String groupName = "蓝九天";// 客服组默认名称
    private HomePageModel1 model1;

    //搜索
    @Event(value = {R.id.ll_search, R.id.zxing, R.id.fenlei1, R.id.fenlei2, R.id.fenlei3, R.id.fenlei4, R.id.fenlei5,
            R.id.fenlei6, R.id.fenlei7, R.id.fenlei8})
    private void onclick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.ll_search:
                it = new Intent(getActivity(), ActivitySearch.class);
                it.putExtra("from", 1);
                startActivity(it);
                break;

            case R.id.zxing:
                it = new Intent(getActivity(), ActivityScan.class);
                startActivity(it);
                break;

            case R.id.fenlei1:
                it = new Intent(getActivity(), ActivityMedicineList.class);
                it.putExtra("search", "");
                startActivity(it);
                break;

            case R.id.fenlei2:
                it = new Intent(getActivity(), ActivityGoodsList.class);
                it.putExtra("search", "特产");
                startActivity(it);
                break;

            case R.id.fenlei3:
                it = new Intent(getActivity(), ActivityGoodsList.class);
                it.putExtra("search", "服饰");
                startActivity(it);
                break;

            case R.id.fenlei4:
                it = new Intent(getActivity(), ActivityGoodsList.class);
                it.putExtra("search", "美妆");
                startActivity(it);
                break;

            case R.id.fenlei5:
                it = new Intent(getActivity(), ActivityGoodsList.class);
                it.putExtra("search", "配饰");
                startActivity(it);
                break;

            case R.id.fenlei6:
                it = new Intent(getActivity(), ActivityGoodsList.class);
                it.putExtra("search", "家居");
                startActivity(it);
                break;

            case R.id.fenlei7:
                it = new Intent(getActivity(), ActivityGoodsList.class);
                it.putExtra("search", "母婴");
                startActivity(it);
                break;

            case R.id.fenlei8:
                it = new Intent(getActivity(), ActivityGoodsList.class);
                it.putExtra("search", "家电");
                startActivity(it);
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

        myScrollView.setVerticalScrollBarEnabled(false);

        gethome();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());//必须有
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置方向滑动
        tuijian.setLayoutManager(linearLayoutManager);
        tuijian.setItemAnimator(new DefaultItemAnimator());
        adapter = new MyAdapter();
        tuijian.setAdapter(adapter);

        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                Intent it = new Intent(getActivity(), ActivityHomeWeb.class);
                it.putExtra(AppConfig.IntentExtraKey.Home_News, model1.getNews().get(position).getSUMMARY());
                startActivity(it);
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
            if (AppConfig.currSel == 0) {
                gethome();
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

    BizDataAsyncTask<HomePageModel1> homeTask;

    private void gethome() {
        homeTask = new BizDataAsyncTask<HomePageModel1>() {
            @Override
            protected HomePageModel1 doExecute() throws ZYException, BizFailure {
                return HomeBiz.getHome1();
            }

            @Override
            protected void onExecuteSucceeded(HomePageModel1 homePageModel) {
                model1 = homePageModel;

                //首页轮播图
                mList = new ArrayList<>();
                String[] img = homePageModel.getHeadImgs();
                for (int i = 0; i < img.length; i++) {
                    if (!StringUtils.isEmpty(img[i])) {
                        mList.add(img[i]);
                    }
                }
                mList = new ArrayList<>();
                mList.add("http://www.lanjiutian.com/medicine/DetailsPage.html?wareid=009554");
                mList.add("http://www.lanjiutian.com/medicine/DetailsPage.html?wareid=005406");
                mList.add("http://www.lanjiutian.com/medicine/DetailsPage.html?wareid=006206");
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


                //推荐
                list1 = new ArrayList<>();
                list1 = homePageModel.getRecommendComms();
                for (int j = 0; j < list1.size(); j++) {
                    list1.get(j).setFlg(false);
                }
                adapter.setNewData(list1);
                adapter.notifyDataSetChanged();
            }

            @Override
            protected void OnExecuteFailed() {

            }
        };
        homeTask.execute();
    }


    private class MyAdapter extends BaseQuickAdapter<HomePageModel1.RecommendComms> {
        public MyAdapter() {
            super(R.layout.item_home_hot, list1);
        }

        @Override
        protected void convert(final BaseViewHolder baseViewHolder, final HomePageModel1.RecommendComms recommendComms) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());//必须有
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//设置方向滑动
            final RecyclerView recyclerView = baseViewHolder.getView(R.id.hot_list_img);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(new MyAdapter1(recommendComms.getCommodityList()));


            baseViewHolder.setText(R.id.number, recommendComms.getCommodityList().size() + "")
                    .setAlpha(R.id.hot_ll_wai, 150)
                    .setAlpha(R.id.hot_ll_nei, 110)
                    .setOnClickListener(R.id.hot_ll_wai, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.e("**********", recommendComms.isFlg() + "");
                            if (recommendComms.isFlg()) {
                                recyclerView.setVisibility(View.GONE);
                                adapter.getData().get(baseViewHolder.getPosition()).setFlg(false);
                            } else {
                                //设置进入动画
                                recyclerView.setVisibility(View.VISIBLE);
                                recyclerView.startAnimation(anm());

                                adapter.getData().get(baseViewHolder.getPosition()).setFlg(true);
                            }

                        }
                    });

            SimpleDraweeView simpleDraweeView = baseViewHolder.getView(R.id.sdv_item_head_img);
            simpleDraweeView.setImageURI(Uri.parse("http://www.lanjiutian.com/upload/images" + recommendComms.getImgPath()));
        }
    }


    private class MyAdapter1 extends BaseQuickAdapter<HomePageModel1.goods> {

        public MyAdapter1(List<HomePageModel1.goods> goodsList) {
            super(R.layout.item_home_hot_img, goodsList);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, final HomePageModel1.goods goods) {
            baseViewHolder.setOnClickListener(R.id.image, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it = new Intent(getActivity(), ActivityGoodsInfo.class);
                    it.putExtra(AppConfig.IntentExtraKey.MEDICINE_INFO_ID, goods.getCommodityId());
                    startActivity(it);
                }
            });
            SimpleDraweeView image = baseViewHolder.getView(R.id.image);
            image.setImageURI(Uri.parse("http://www.lanjiutian.com/upload/images" + goods.getImgPath()));


        }
    }

    private AnimationSet anm() {
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation animation = new TranslateAnimation(650, 0, 0, 0);
        animation.setDuration(700);
        animationSet.addAnimation(animation);
        return animationSet;
    }

}
