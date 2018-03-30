package com.wyw.ljtds.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.sunfusheng.marqueeview.MarqueeView;
import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.HomePageModel1;
import com.wyw.ljtds.model.IConCatInfo;
import com.wyw.ljtds.ui.goods.ActivityGoodsInfo;
import com.wyw.ljtds.ui.goods.ActivityGoodsList;
import com.wyw.ljtds.ui.goods.ActivityLifeGoodsInfo;
import com.wyw.ljtds.ui.goods.ShopActivity;
import com.wyw.ljtds.ui.home.ActivityHomeWeb;
import com.wyw.ljtds.ui.home.HuoDongActivity;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by wsy on 17-12-9.
 */

public class LifeIndexAdapter extends RecyclerView.Adapter {
    private static final int UNKNOW = -1;
    private final HomePageModel1 data;
    private final Context context;
    private LayoutInflater inflater;

    private static final int BANNER = 0; //big carousel banner
    private static final int ICONCAT = 1; //category icon list
    private static final int MARQUEE = 2; //marquee news
    private static final int WEBACTIVITY = 3;//holiday activity
    private static final int RECOMMAND = 4; //recommand commodities
    private static final int TITLE = 5; //title

    List<ItemTypeInfo> itemTypeList;

    public LifeIndexAdapter(Context mContext, HomePageModel1 homePageModel) {
        inflater = LayoutInflater.from(mContext);
        this.context = mContext;
        this.data = homePageModel;

        ItemTypeInfo bannerView = new ItemTypeInfo(BANNER, 1);
        ItemTypeInfo catIconsView = new ItemTypeInfo(ICONCAT, data.getIconImg().size());
        ItemTypeInfo newsView = new ItemTypeInfo(MARQUEE, 1);
        ItemTypeInfo activityView = new ItemTypeInfo(WEBACTIVITY, 1);
        ItemTypeInfo remTitle = new ItemTypeInfo(TITLE, 1);
        ItemTypeInfo remCommListView = new ItemTypeInfo(RECOMMAND, data.getRecommendComms().size());

        //各类型视图的有序列表
        ItemTypeInfo[] viArr = {bannerView, catIconsView, newsView, activityView, remTitle, remCommListView};
        itemTypeList = Arrays.asList(viArr);
        //计算每个类型视图起始位置
        for (int i = 0; i < itemTypeList.size(); i++) {
            ItemTypeInfo vi = itemTypeList.get(i);
            if (i == 0) {
                vi.setItemStartPos(0);
            } else {
                ItemTypeInfo preItem = itemTypeList.get(i - 1);
                vi.setItemStartPos(preItem.getItemStartPos() + preItem.getItemCount());
            }
        }


    }

    @Override
    public int getItemCount() {
        ItemTypeInfo lastItem = itemTypeList.get(itemTypeList.size() - 1);
        return lastItem.itemStartPos + lastItem.itemCount;
    }

    @Override
    public int getItemViewType(int position) {
//        if (position == 0) {
//            return BANNER;
//        }
//        int recCommCnt = 0;
//        if (data.getRecommendComms() != null) {
//            recCommCnt = data.getRecommendComms().size();
//        }
//        if (position > 0 && position < (recCommCnt + BANNER_ITEM_CNT)) {
//            return NUM_TWO;
//        }

        ItemTypeInfo itemTypeInfo = getItemTypeInfo(position);
        if (itemTypeInfo.itemType != UNKNOW)
            return itemTypeInfo.itemType;
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.e(AppConfig.ERR_TAG, "onCreateViewHolder:" + viewType);
//        TextView vMarquee = new TextView(context);
//        vMarquee.setText("XXX");
//        return new SimpleViewHolder(vMarquee);
        switch (viewType) {
            case BANNER:
                View itemBanner = inflater.inflate(R.layout.item_banner_convenient, parent, false);
                return new BannerHolder(itemBanner);
            case ICONCAT:
                View itemIconCat = inflater.inflate(R.layout.item_fragment_home_iconlist, parent, false);
                return new IconListViewHolder(itemIconCat);

            case MARQUEE:
                View itemNews = inflater.inflate(R.layout.item_marquee_news, parent, false);
                return new NewsMarqueeHolder(itemNews);

            case WEBACTIVITY:
                WebView wvActivity = new WebView(context);
                String url = AppConfig.WS_BASE_HTML_URL + "huodongindex.html?dt=" + System.currentTimeMillis();
                Log.e(AppConfig.ERR_TAG, "url:" + url);
                wvActivity.loadUrl(url);
                initWebView(wvActivity);
                return new SimpleViewHolder(wvActivity);
            case RECOMMAND:
                View itemTuijian = inflater.inflate(R.layout.item_home_hot, parent, false);
                return new TuiJianHolder(itemTuijian);
            case TITLE:
                View hotTitle = inflater.inflate(R.layout.fragment_life_index_hot_title, parent, false);
//                vTitle.setBackground(context.getResources().getDrawable(R.drawable.ic_home_hot));
                return new SimpleViewHolder(hotTitle);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemTypeInfo typeInfo = getItemTypeInfo(position);
        //数据在数据列表中实际索引
        int dataPos = position - typeInfo.itemStartPos;
//        Log.e(AppConfig.ERR_TAG, "onBindViewHolder:" + position + "------dataPos:" + dataPos);
        if (holder instanceof TuiJianHolder) {
            HomePageModel1.RecommendComms tuijianComm = data.getRecommendComms().get(dataPos);
            bindTuiJianData((TuiJianHolder) holder, tuijianComm);
        } else if (holder instanceof IconListViewHolder) {
            IConCatInfo iconData = data.getIconImg().get(dataPos);
            bindIconListData((IconListViewHolder) holder, iconData);
        }

    }

    private void bindIconListData(IconListViewHolder holder, IConCatInfo iconData) {
        holder.data = iconData;
        holder.tvCatName.setText(iconData.getName());
//            holder.sdvIcon.setImageURI(Uri.parse(itemData.getImgPath()));
        String imgUrl = iconData.getImgPath();
        Utils.log("imgUrl:" + imgUrl);
        Picasso.with(context).load(imgUrl).into(holder.sdvIcon);
    }

    private ItemTypeInfo getItemTypeInfo(int position) {
        for (int i = 0; i < itemTypeList.size(); i++) {
            ItemTypeInfo vti = itemTypeList.get(i);
            if (position >= vti.itemStartPos && position < (vti.itemStartPos + vti.itemCount)) {
                return vti;
            }
        }
        return new ItemTypeInfo(UNKNOW, 0);
    }

    /**
     * 推荐商品列表大类
     *
     * @param holder
     */

    private void bindTuiJianData(TuiJianHolder holder, HomePageModel1.RecommendComms recComm) {
        holder.data = recComm;
        //大类数据
        if (recComm.getCommodityList() != null) {
            holder.tvNumber.setText("" + recComm.getCommodityList().size() + "\n单品");
        }
//        holder.imgCat.setImageURI(Uri.parse(AppConfig.IMAGE_PATH_LJT + recComm.getImgPath()));
        Utils.log("recComm:" + AppConfig.IMAGE_PATH_LJT_ECOMERCE + recComm.getImgPath());
        Picasso.with(context).load(Uri.parse(AppConfig.IMAGE_PATH_LJT_ECOMERCE + recComm.getImgPath())).into(holder.imgCat);

        holder.bindCommList(recComm.getCommodityList());
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Log.e(AppConfig.ERR_TAG, "onAttachedToRecyclerView............");
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    switch (type) {
                        case BANNER:
                            return 12;
                        case ICONCAT:
                            return 3;
                        case MARQUEE:
                            return 12;
                        case RECOMMAND:
                            return 12;
                        case TITLE:
                            return 12;
                        default:
                            return 12;
                    }
                }
            });
        }
    }

    private void initWebView(final WebView webView) {
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
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });


        //响应js发起的抽奖请求
        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void doReq(String flg) {
                Intent it = HuoDongActivity.getIntent(context, flg);
                context.startActivity(it);
            }
        }, "andObj");
        /*webView.setWebChromeClient(new WebChromeClient() {
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
                }
            }
        });*/
    }

    /*
    carousel banner
     */
    public class BannerHolder extends RecyclerView.ViewHolder {
        //轮播图组件
        ConvenientBanner banner;

        BannerHolder(View itemView) {
            super(itemView);
            banner = (ConvenientBanner) itemView.findViewById(R.id.item_convenient_banner);

            final List<Map<String, String>> bannerImgList = data.getHeadImgsList();
            if (bannerImgList != null) {
                banner.setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused});
                banner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
                banner.setPages(new CBViewHolderCreator() {
                    @Override
                    public Object createHolder() {
                        return new Holder<Map<String, String>>() {
                            private ImageView iv;

                            @Override
                            public View createView(Context context) {
                                iv = new ImageView(context);
                                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                                return iv;
                            }

                            @Override
                            public void UpdateUI(Context context, int position, Map<String, String> data) {
                                String imgSrc = bannerImgList.get(position).get("headImgsUrl");
                                if (!StringUtils.isEmpty(imgSrc)) {
                                    Picasso.with(context).load(Uri.parse(imgSrc)).into(iv);
                                }
                            }
                        }
                                ;
                    }
                }, bannerImgList);

                //添加轮播图点击响应事件
                //{"flg":"X","headId":"","headImgsUrl":"http://www.lanjiutian.com/upload/images/ef4118c10a1b432ba4e31b674d7f582c/d2a57dc1d883fd21fb9951699df71cc7.jpg"}
                banner.setOnItemClickListener(new com.bigkoo.convenientbanner.listener.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Map<String, String> mItem = bannerImgList.get(position);
                        String flg = mItem.get("flg");
                        String headId = mItem.get("headId");
                        //详情：X,列表 L 店铺 D 电子币 DZ 抽奖 CJ 满赠 MZ 特价 TJ 秒杀 MS
                        Intent it = null;
                        switch (flg) {
                            case "X":
//                                it = ActivityGoodsInfo.getIntent(context, headId);
                                break;
                            case "L":
                                break;
                            case "D":
                                it = ShopActivity.getIntent(context, headId);
                                break;
                            case "DZ":
                                it = HuoDongActivity.getIntent(context, "5");
                                break;
                            case "CJ":
                                it = HuoDongActivity.getIntent(context, "1");
                                break;
                            case "MZ":
                                it = HuoDongActivity.getIntent(context, "2");
                                break;
                            case "TJ":
                                it = HuoDongActivity.getIntent(context, "3");
                                break;
                            case "MS":
                                it = HuoDongActivity.getIntent(context, "4");
                                break;
                            default:
                                break;
                        }
                        if (it != null)
                            context.startActivity(it);
                    }
                });
                //开始自动翻页
                banner.startTurning(3000);

            }
        }
    }

    class IconListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        IConCatInfo data;
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
            Intent it = ActivityGoodsList.getStartMeIntent(context, data.getCommodityTypeId());
            context.startActivity(it);
        }
    }

    private class TuiJianHolder extends RecyclerView.ViewHolder {
        HomePageModel1.RecommendComms data;
        LinearLayoutManager linearLayoutManager;
        RecyclerView rylvCommList; // 商品列表
        TextView tvNumber; //商品数量
        //        SimpleDraweeView imgCat;
        ImageView imgCat; //类别图片

        public TuiJianHolder(View view) {
            super(view);


            linearLayoutManager = new LinearLayoutManager(context);//必须有
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//设置方向滑动
//            baseViewHolder.setText()
//                    .setAlpha(R.id.hot_ll_wai, 150)
//                    .setAlpha(R.id.hot_ll_nei, 110)
//                    .setOnClickListener(R.id.hot_ll_wai, )
//                    .setOnClickListener(R.id.sdv_item_head_img, );
            tvNumber = (TextView) view.findViewById(R.id.number);
//            imgCat = (SimpleDraweeView) view.findViewById(R.id.sdv_item_head_img);
            imgCat = (ImageView) view.findViewById(R.id.sdv_item_head_img);
            imgCat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (data == null) return;
                    String typeId = data.getCommodityTypeId();
                    Intent it = ActivityGoodsList.getStartMeIntent(context, typeId);
                    context.startActivity(it);
                }
            });
            //商品列表
            rylvCommList = (RecyclerView) view.findViewById(R.id.hot_list_img);
            rylvCommList.setLayoutManager(linearLayoutManager);
            rylvCommList.setItemAnimator(new

                    DefaultItemAnimator());
            //点击数量图标 显示商品列表
            tvNumber.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View view) {
                    if (rylvCommList.getVisibility() == View.VISIBLE) {
                        data.setFlg(false);
                        rylvCommList.setVisibility(View.GONE);
                    } else {
                        //设置进入动画
                        data.setFlg(true);
                        rylvCommList.setVisibility(View.VISIBLE);
                    }
                }
            });


        }

        private AnimationSet anm() {
            AnimationSet animationSet = new AnimationSet(true);
            TranslateAnimation animation = new TranslateAnimation(650, 0, 0, 0);
            animation.setDuration(700);
            animationSet.addAnimation(animation);
            return animationSet;
        }

        public void bindCommList(List<HomePageModel1.goods> commodityList) {
            if (data.isFlg()) {
                rylvCommList.setVisibility(View.VISIBLE);
            } else {
                rylvCommList.setVisibility(View.GONE);
            }
            //商品列表
            rylvCommList.setAdapter(new SimpleCommListAdapter(commodityList));

//            SimpleCommListAdapter adp = (SimpleCommListAdapter) rylvCommList.getAdapter();
//            adp.setNewData(commodityList);
//            adp.notifyDataSetChanged();
        }
    }

    class SimpleCommListAdapter extends BaseQuickAdapter<HomePageModel1.goods> {

        public SimpleCommListAdapter(List<HomePageModel1.goods> goodsList) {
            super(R.layout.item_home_hot_img, goodsList);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, final HomePageModel1.goods goods) {
            baseViewHolder.setOnClickListener(R.id.image, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it = ActivityLifeGoodsInfo.getIntent(context, goods.getCommodityId());
//                    it.putExtra(AppConfig.IntentExtraKey.MEDICINE_INFO_ID, );
                    context.startActivity(it);
                }
            });
            SimpleDraweeView image = baseViewHolder.getView(R.id.image);
            image.setImageURI(Uri.parse(AppConfig.IMAGE_PATH_LJT + goods.getImgPath()));
        }
    }

    class ItemTypeInfo {
        int itemType;
        int itemStartPos;
        int itemCount;

        private ItemTypeInfo() {
        }

        public ItemTypeInfo(int itemType, int itemCount) {
            this.itemType = itemType;
            this.itemCount = itemCount;
        }

        public int getItemStartPos() {
            return itemStartPos;
        }

        public void setItemStartPos(int itemStartPos) {
            this.itemStartPos = itemStartPos;
        }

        public int getItemCount() {
            return itemCount;
        }

        public void setItemCount(int itemCount) {
            this.itemCount = itemCount;
        }


        public int getItemType() {
            return itemType;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder {

        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 滚动的 新闻公告
     */
    private class NewsMarqueeHolder extends RecyclerView.ViewHolder {
        MarqueeView marqueeContent;

        public NewsMarqueeHolder(View itemNews) {
            super(itemNews);
            marqueeContent = (MarqueeView) itemNews.findViewById(R.id.item_marquee_news_tv_content);
            List<String> marqueeContentList = new ArrayList<>();
            //为广播的公告添加滚动的内容
            for (int i = 0; i < data.getNews().size(); i++) {
                marqueeContentList.add(data.getNews().get(i).getTITLE());
            }
            marqueeContent.startWithList(marqueeContentList);
            marqueeContent.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
                @Override
                public void onItemClick(int position, TextView textView) {
                    Intent it = new Intent(context, ActivityHomeWeb.class);
                    it.putExtra(AppConfig.IntentExtraKey.Home_News, data.getNews().get(position).getSUMMARY());
                    context.startActivity(it);
                }
            });
            marqueeContent.startFlipping();
        }


    }
}
