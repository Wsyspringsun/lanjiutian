package com.wyw.ljtds.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
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
import com.wyw.ljtds.biz.task.DownloadThread;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.HomePageModel1;
import com.wyw.ljtds.model.IConCatInfo;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.ui.category.ActivityScan;
import com.wyw.ljtds.ui.goods.ActivityGoodsInfo;
import com.wyw.ljtds.ui.goods.ActivityGoodsList;
import com.wyw.ljtds.ui.goods.ActivityMedicinesInfo;
import com.wyw.ljtds.ui.goods.ShopActivity;
import com.wyw.ljtds.ui.user.ActivityMessage;
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

@ContentView(R.layout.fragment_home)
public class FragmentHome extends BaseFragment {
    @ViewInject(R.id.fragment_home_rv_iconcatlist)
    private RecyclerView rvIconCatList;
    private Map<String, Bitmap> iconMap;
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
    @ViewInject(R.id.img_home_active_goods)
    ImageView imgHomeActiveGoods;
    @ViewInject(R.id.fragment_home_webview_huodong)
    private WebView webView;
    @ViewInject(R.id.fragment_home_img_loaderr)
    private ImageView imgLoaderr;


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

    private DownloadThread<IconListViewHolder> downloadThread;

    //标识web页面加载是否成功
    private boolean loadError = true;


    @Event(value = {R.id.ll_search, R.id.zxing, R.id.right_img, R.id.img_home_active_goods})
    private void onclick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.ll_search:
                it = new Intent(getActivity(), ActivitySearch.class);
                it.putExtra("from", 1);
                startActivity(it);
                break;
            //扫描二维码
            case R.id.zxing:
                //生活馆
                it = new Intent(getActivity(), ActivityScan.class);
                it.putExtra(AppConfig.IntentExtraKey.CAT_FROM, AppConfig.LIFE);
                startActivity(it);
                break;
            case R.id.right_img:
                startActivity(new Intent(getActivity(), ActivityMessage.class));
                break;
            default:
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //缓存图标
//        iconMap = new HashMap<>();
//
//        Handler responseHandler = new Handler();
//        downloadThread = new DownloadThread<>("fragmentHome_download", responseHandler);
//        downloadThread.setDownloadListner(new DownloadThread.DownloadListener<IconListViewHolder>() {
//            @Override
//            public void completeDownload(IconListViewHolder token, String url, Bitmap bitMap) {
//                Drawable drawable = new BitmapDrawable(getResources(), bitMap);
//                token.sdvIcon.setImageDrawable(drawable);
//                Log.e(AppConfig.ERR_TAG, "put bitmap in holder:" + url);
//                iconMap.put(url, bitMap);
//            }
//        });
//        downloadThread.start();
//        downloadThread.getLooper();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        downloadThread.quit();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myScrollView.setVerticalScrollBarEnabled(false);

        //活动 图片
        Picasso.with(getActivity()).load(AppConfig.IMAGE_PATH_LJT + "/.system_1/img_shenghuo_active.jpg").into(imgHomeActiveGoods);
        //首页 快捷分类的图标
        GridLayoutManager glm = new GridLayoutManager(getActivity(), 4);
        rvIconCatList.setLayoutManager(glm);

        adapter = new MyAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());//必须有
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置方向滑动
        tuijian.setLayoutManager(linearLayoutManager);
        tuijian.setItemAnimator(new DefaultItemAnimator());
        View noData = getActivity().getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) tuijian.getParent(), false);
        adapter.setEmptyView(noData);
        tuijian.setAdapter(adapter);


        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                Intent it = new Intent(getActivity(), ActivityHomeWeb.class);
                it.putExtra(AppConfig.IntentExtraKey.Home_News, model1.getNews().get(position).getSUMMARY());
                startActivity(it);
            }
        });


        //刷新嵌入的WebView
        initWebView();
    }

    /**
     * 设置 活动的 WebView
     */
    private void initWebView() {
        imgLoaderr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.reload();
            }
        });

        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loadError = false;

                imgLoaderr.setVisibility(View.GONE);
                webView.setVisibility(View.GONE);
//                ((BaseActivity) getActivity()).setLoding(getActivity(), false);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
//                ((BaseActivity) getActivity()).closeLoding();
                if (loadError) {
                    imgLoaderr.setVisibility(View.VISIBLE);
                    return;
                }
//                imgErr.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                loadError = true;
            }
        });


        //响应js发起的抽奖请求
        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void doReq(String flg) {
                Intent it = HuoDongActivity.getIntent(getActivity(), flg);
                getActivity().startActivity(it);
            }
        }, "andObj");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress >= 100) {
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                Log.e(AppConfig.ERR_TAG, title);
                //判断标题 title 中是否包含有“error”字段，如果包含“error”字段，则设置加载失败，显示加载失败的视图
                if (!StringUtils.isEmpty(title) && title.toLowerCase().contains("error")) {
                    Log.e(AppConfig.ERR_TAG, "title:" + title);
                    loadError = true;
                }
            }
        });


    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (marqueeView != null) {
//            data = null;
                marqueeView.stopFlipping();
            }
        } else {
//            if (AppConfig.currSel == 0) {
//                gethome();
//            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        gethome();

        //        webView.loadDataWithBaseURL("fake://not/needed", str, "text/html", "utf-8", null);
        String url = AppConfig.WS_BASE_HTML_URL + "huodongindex.html";
//        String url = AppConfig.WS_BASE_JSP_URL+"huodongindex.jsp";
        //        String url = AppConfig.BLANK_URL;
        Log.e(AppConfig.ERR_TAG, "url:" + url);
        webView.loadUrl(url);
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
                List<Map<String, String>> img = homePageModel.getHeadImgsList();
                Utils.log("getHeadImgsList:" + GsonUtils.Bean2Json(img));
                ;
                if (img != null) {
                    for (int i = 0; i < img.size(); i++) {
                        String imgSrc = img.get(i).get("headImgsUrl");
                        if (!StringUtils.isEmpty(imgSrc)) {
                            Log.e(AppConfig.ERR_TAG, "imgSrc:" + imgSrc);
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

                    //{"flg":"X","headId":"","headImgsUrl":"http://www.lanjiutian.com/upload/images/ef4118c10a1b432ba4e31b674d7f582c/d2a57dc1d883fd21fb9951699df71cc7.jpg"}
                    banner.setOnItemClickListener(new com.bigkoo.convenientbanner.listener.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Map<String, String> mItem = model1.getHeadImgsList().get(position);
                            String flg = mItem.get("flg");
                            String headId = mItem.get("headId");
                            //详情：X,列表 L 店铺 D 电子币 DZ 抽奖 CJ 满赠 MZ 特价 TJ 秒杀 MS
                            Intent it = null;
                            switch (flg) {
                                case "X":
//                                    it = ActivityGoodsInfo.getIntent(getActivity(), headId);
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


                //推荐
                list1 = new ArrayList<>();
                list1 = homePageModel.getRecommendComms();
                for (int j = 0; j < list1.size(); j++) {
                    list1.get(j).setFlg(false);
                }
                adapter.setNewData(list1);
                adapter.notifyDataSetChanged();

                updIconRecyview(homePageModel);
            }

            @Override
            protected void OnExecuteFailed() {

            }
        };
        homeTask.execute();
    }

    private void updIconRecyview(HomePageModel1 homePageModel) {
        if (rvIconCatList == null)
            return;
        if (homePageModel == null) return;
        List<IConCatInfo> list = homePageModel.getIconImg();
        if (list == null)
            return;
        IconListAdapter myAdapter;
        if (rvIconCatList.getAdapter() == null) {
            myAdapter = new IconListAdapter(list);
            rvIconCatList.setAdapter(myAdapter);
            ;
        } else {
            myAdapter = (IconListAdapter) rvIconCatList.getAdapter();
        }
        myAdapter.notifyDataSetChanged();
    }

    /**
     * ===============处理分类图标
     */
    class IconListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        IConCatInfo itemData;
        TextView tvCatName;
        ImageView sdvIcon;

        public IconListViewHolder(View itemView) {
            super(itemView);
            sdvIcon = (ImageView) itemView.findViewById(R.id.item_fragment_home_iconlist_icon);
            tvCatName = (TextView) itemView.findViewById(R.id.item_fragment_home_iconlist_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.e(AppConfig.ERR_TAG, "itemData:" + itemData.getName());

            Intent it = ActivityGoodsList.getStartMeIntent(getActivity(), itemData.getCommodityTypeId());
            startActivity(it);
        }
    }

    class IconListAdapter extends RecyclerView.Adapter<IconListViewHolder> {
        List<IConCatInfo> list;

        public IconListAdapter(List<IConCatInfo> list) {
            this.list = list;
        }

        @Override
        public IconListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.item_fragment_home_iconlist, parent, false);
            return new IconListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(IconListViewHolder holder, int position) {
            IConCatInfo itemData = list.get(position);
            holder.itemData = itemData;
            holder.tvCatName.setText(itemData.getName());
//            holder.sdvIcon.setImageURI(Uri.parse(itemData.getImgPath()));
            String imgUrl = itemData.getImgPath();
            Picasso.with(getActivity()).load(imgUrl).into(holder.sdvIcon);
//            Bitmap bitMap = iconMap.get(imgUrl);
//
//            if (bitMap == null) {
//                Log.e(AppConfig.ERR_TAG, "no bitmap in holder:" + imgUrl);
//                iconMap.remove(imgUrl);
//                downloadThread.queneObj(holder, imgUrl);
//            } else {
//                Log.e(AppConfig.ERR_TAG, "has bitmap in holder:" + imgUrl);
//                Drawable drawable = new BitmapDrawable(getResources(), bitMap);
//                holder.sdvIcon.setImageDrawable(drawable);
//            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    /**
     * ===============处理分类图标
     */

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
                            Utils.log("recommendComms:" + recommendComms.getName() + "/" + recommendComms.isFlg());
                            if (recyclerView.getVisibility() == View.VISIBLE) {
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                //设置进入动画
                                recyclerView.setVisibility(View.VISIBLE);
                                recyclerView.startAnimation(anm());
                            }

                        }
                    })
                    .setOnClickListener(R.id.sdv_item_head_img, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (adapter != null) {
                                HomePageModel1.RecommendComms itemData = adapter.getData().get(baseViewHolder.getPosition());

                                String typeId = itemData.getCommodityTypeId();
                                Log.e(AppConfig.ERR_TAG, "item click" + typeId);
                                Intent it = ActivityGoodsList.getStartMeIntent(getActivity(), typeId);
                                getActivity().startActivity(it);
                            }
                        }
                    });
//

            SimpleDraweeView simpleDraweeView = baseViewHolder.getView(R.id.sdv_item_head_img);
            simpleDraweeView.setImageURI(Uri.parse(AppConfig.IMAGE_PATH_LJT + recommendComms.getImgPath()));
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
//                    it.putExtra(AppConfig.IntentExtraKey.MEDICINE_INFO_ID, goods.getCommodityId());
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
