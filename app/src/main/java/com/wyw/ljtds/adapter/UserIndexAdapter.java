package com.wyw.ljtds.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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

import com.baidu.mobstat.ExtraInfo;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.gestures.GestureDetector;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jauker.widget.BadgeView;
import com.squareup.picasso.Picasso;
import com.sunfusheng.marqueeview.MarqueeView;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.HomePageModel1;
import com.wyw.ljtds.model.IConCatInfo;
import com.wyw.ljtds.model.IconText;
import com.wyw.ljtds.model.RecommendModel;
import com.wyw.ljtds.model.UserDataModel;
import com.wyw.ljtds.model.UserIndexModel;
import com.wyw.ljtds.model.UserModel;
import com.wyw.ljtds.ui.base.ActivityWebView;
import com.wyw.ljtds.ui.goods.ActivityGoodsList;
import com.wyw.ljtds.ui.goods.ActivityLifeGoodsInfo;
import com.wyw.ljtds.ui.goods.ActivityMedicinesInfo;
import com.wyw.ljtds.ui.goods.ShopActivity;
import com.wyw.ljtds.ui.home.ActivityHomeWeb;
import com.wyw.ljtds.ui.home.HuoDongActivity;
import com.wyw.ljtds.ui.user.ActivityCollect;
import com.wyw.ljtds.ui.user.ActivityLogin;
import com.wyw.ljtds.ui.user.ActivityWallet;
import com.wyw.ljtds.ui.user.manage.ActivityManage;
import com.wyw.ljtds.ui.user.order.ActivityAfterMarket;
import com.wyw.ljtds.ui.user.order.ActivityOrder;
import com.wyw.ljtds.ui.user.order.ReturnGoodsOrderListActivity;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by wsy on 17-12-9.
 */

public class UserIndexAdapter extends RecyclerView.Adapter {
    private static final int UNKNOW = -1;
    private final Context context;
    private LayoutInflater inflater;

    private static final int BANNER = 0; //big carousel banner
    private static final int ICONORDER = 1; //category icon list
    private static final int ICONEXTRA = 2; //category icon list
    private static final int TITLEORDER = 3;//holiday activity
    private static final int RECOMMAND = 4; //recommand commodities
    private static final int TITLERECOMMAND = 5; //title

    List<ItemTypeInfo> itemTypeList;

    private UserIndexModel model;

    public UserIndexModel getModel() {
        return model;
    }

    public void setModel(UserIndexModel model) {
        this.model = model;

    }

    public UserIndexAdapter(Context mContext, UserIndexModel model) {
        inflater = LayoutInflater.from(mContext);
        this.context = mContext;
        this.model = model;
    }

    @Override
    public int getItemCount() {

        ItemTypeInfo bannerView = new ItemTypeInfo(BANNER, 1);
        ItemTypeInfo orderTitleView = new ItemTypeInfo(TITLEORDER, 1);
        ItemTypeInfo orderIconView = new ItemTypeInfo(ICONORDER, 5);
//        ItemTypeInfo border1 = new ItemTypeInfo(TITLERECOMMAND, 1);
        ItemTypeInfo extraIconView = new ItemTypeInfo(ICONEXTRA, 4);
//        ItemTypeInfo border2 = new ItemTypeInfo(TITLERECOMMAND, 1);

        int recommandSize = 0;
        if (model.getRecommendModels() != null) {
            recommandSize = model.getRecommendModels().size();
        }
        ItemTypeInfo remCommListView = new ItemTypeInfo(RECOMMAND, recommandSize);

        //各类型视图的有序列表
        ItemTypeInfo[] viArr = {bannerView, orderTitleView, orderIconView, extraIconView, remCommListView};
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


        ItemTypeInfo lastItem = itemTypeList.get(itemTypeList.size() - 1);
        int cnt = lastItem.itemStartPos + lastItem.itemCount;
        return cnt;
    }

    @Override
    public int getItemViewType(int position) {
        ItemTypeInfo itemTypeInfo = getItemTypeInfo(position);
        if (itemTypeInfo.itemType != UNKNOW)
            return itemTypeInfo.itemType;
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case BANNER:
                view = inflater.inflate(R.layout.fragment_user_index_userinfo, parent, false);
                return new BannerHolder(view);
            case ICONORDER:
                view = inflater.inflate(R.layout.item_fragment_home_iconlist, parent, false);
                return new OrderIconHolder(view);
            case ICONEXTRA:
                view = inflater.inflate(R.layout.item_fragment_home_iconextra, parent, false);
                return new ExtraIconHolder(view);
            case TITLEORDER:
                view = inflater.inflate(R.layout.fragment_user_index_ordertitle, parent, false);
                return new OrderTitleHolder(view);
            case TITLERECOMMAND:
                view = inflater.inflate(R.layout.fragment_border, parent, false);
                return new TempViewHolder(view);
            case RECOMMAND:
                view = inflater.inflate(R.layout.item_goods_grid, parent, false);
                return new RecommandGoodsHolder(view);
            /*case ICONCAT:
                View itemIconCat = inflater.inflate(R.layout.item_fragment_home_iconlist, parent, false);
                return new IconListViewHolder(itemIconCat);

            case MARQUEE:
                View itemNews = inflater.inflate(R.layout.item_marquee_news, parent, false);
                return new NewsMarqueeHolder(itemNews);

            case WEBACTIVITY:
                WebView wvActivity = new WebView(context);
                String url = AppConfig.WS_BASE_HTML_URL + "huodongindex.html";
                Log.e(AppConfig.ERR_TAG, "url:" + url);
                wvActivity.loadUrl(url);
                initWebView(wvActivity);
                return new SimpleViewHolder(wvActivity);
            case RECOMMAND:
                View itemTuijian = inflater.inflate(R.layout.item_home_hot, parent, false);
                return new TuiJianHolder(itemTuijian);
            case TITLE:
                TextView vTitle = new TextView(context);
//                vTitle.setText("");
//                vTitle.setHeight(85);
                vTitle.setBackground(context.getResources().getDrawable(R.drawable.ic_home_hot));
                return new SimpleViewHolder(vTitle);*/
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemTypeInfo typeInfo = getItemTypeInfo(position);
        //数据在数据列表中实际索引
        int dataPos = position - typeInfo.itemStartPos;
//        Log.e(AppConfig.ERR_TAG, "onBindViewHolder:" + position);
        if (holder instanceof BannerHolder) {
            BannerHolder hBannerHolder = (BannerHolder) holder;
            UserModel data = model.getUserModel();
            hBannerHolder.data = data;
            if (data == null) {
                hBannerHolder.infojifen.setText("积分：--分");
                hBannerHolder.infoname.setText("--");
                hBannerHolder.photo.setImageURI(Uri.parse(""));
            } else {
                hBannerHolder.infojifen.setText("积分：" + data.getUSER_POINT() + "分");
                hBannerHolder.infoname.setText(data.getMOBILE());

                if (StringUtils.isEmpty(data.getUSER_ICON_FILE_ID())) {
                    hBannerHolder.photo.setImageURI(Uri.parse(""));
                } else {
                    hBannerHolder.photo.setImageURI(Uri.parse(AppConfig.IMAGE_PATH_LJT + data.getUSER_ICON_FILE_ID()));
                }
            }

        } else if (holder instanceof OrderIconHolder) {
            OrderIconHolder hOrderIconHolder = (OrderIconHolder) holder;
            IconText data = model.getOrderIcons().get(dataPos);
            hOrderIconHolder.index = dataPos;
            hOrderIconHolder.icon.setImageDrawable(ActivityCompat.getDrawable(context, data.getImgId()));
            hOrderIconHolder.text.setText(data.getText());
            UserDataModel nm = model.getUserOrderNumberModel();
            if (nm != null) {
                int cnt = 0;
                switch (dataPos) {
                    case 0:
                        cnt = nm.getDaiFu();
                        break;
                    case 1:
                        cnt = nm.getDaiFa();
                        break;
                    case 2:
                        cnt = nm.getDaiShou();
                        break;
                    case 3:
                        cnt = nm.getDaiPing();
                        break;
                    case 4:
                        cnt = nm.getShouHou();
                        break;
                }
                if (cnt <= 0) {
                    //hidden count
                    hOrderIconHolder.badge.setText(null);
                } else {
                    hOrderIconHolder.badge.setBadgeCount(cnt);
                }
            }
        } else if (holder instanceof ExtraIconHolder) {
            ExtraIconHolder hExtraIconHolder = (ExtraIconHolder) holder;
            IconText data = model.getExtraIcons().get(dataPos);
            hExtraIconHolder.icon.setImageDrawable(ActivityCompat.getDrawable(context, data.getImgId()));
            hExtraIconHolder.text.setText(data.getText());
        } else if (holder instanceof RecommandGoodsHolder) {
            RecommandGoodsHolder hRecommandGoodsHolder = (RecommandGoodsHolder) holder;
            if (model.getRecommendModels() == null) return;
            if (model.getRecommendModels().size() <= 0) return;
            RecommendModel data = model.getRecommendModels().get(dataPos);
            hRecommandGoodsHolder.model = data;
            hRecommandGoodsHolder.tvMoney.setText("￥" + data.getSALEPRICE());
            hRecommandGoodsHolder.tvTitle.setText(StringUtils.deletaFirst(data.getWARENAME()));
            if (StringUtils.isEmpty(data.getIMG_PATH())) {
                hRecommandGoodsHolder.sdv.setImageURI(Uri.parse(""));
            } else {
                hRecommandGoodsHolder.sdv.setImageURI(Uri.parse(AppConfig.IMAGE_PATH_LJT + data.getIMG_PATH()));
            }
        }
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


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    switch (type) {
                        case BANNER:
                            return 20;
                        case ICONORDER:
                            return 4;
                        case ICONEXTRA:
                            return 5;
                        case RECOMMAND:
                            return 10;
                        case TITLEORDER:
                            return 20;
                        case TITLERECOMMAND:
                            return 20;
                        default:
                            return 20;
                    }
                }
            });
        }
    }


    /*
    carousel banner
     */
    public class BannerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        SimpleDraweeView photo;
        TextView infoname;
        TextView infojifen;
        LinearLayout zhanghuguanli;
        public UserModel data;

        public BannerHolder(View itemView) {
            super(itemView);
            photo = (SimpleDraweeView) itemView.findViewById(R.id.fragment_user_index_userinfo_img_photo);
            infoname = (TextView) itemView.findViewById(R.id.fragment_user_index_userinfo_ll_infoname);
            infojifen = (TextView) itemView.findViewById(R.id.fragment_user_index_userinfo_ll_infoname);
            zhanghuguanli = (LinearLayout) itemView.findViewById(R.id.fragment_user_info_userinfo_zhanghuguanli);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (data == null) return;
            Intent it = new Intent(context, ActivityManage.class);
            it.putExtra("user", data);
            context.startActivity(it);
        }
    }

    public class ExtraIconHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public int index;
        public final ImageView icon;
        public final TextView text;

        public ExtraIconHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.item_fragment_home_iconextra_icon);
            text = (TextView) itemView.findViewById(R.id.item_fragment_home_iconextra_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int i = index;
            Intent it = null;
            if (i == 3) {
                //case 3://帮助中心
                it = new Intent(context, ActivityWebView.class);
                context.startActivity(it);
                return;
            }

            if (!UserBiz.isLogined()) {
                //去登录
                ActivityLogin.goLogin(context);
                return;
            }

            switch (i) {
                case 0://我的收藏
                    it = ActivityCollect.getIntent(context, ActivityCollect.TAG_MY_SHOUCANG);
                    context.startActivity(it);
                    break;

                case 1://足迹
                    it = ActivityCollect.getIntent(context, ActivityCollect.TAG_MY_ZUJI);
                    context.startActivity(it);
                    context.startActivity(it);
                    break;

                case 2://我的钱包 qianbao
                    it = new Intent(context, ActivityWallet.class);
                    context.startActivity(it);
                    break;
                default:
                    break;


            }
        }
    }

    public class OrderIconHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public int index;
        public final ImageView icon;
        public final TextView text;
        public final BadgeView badge;

        public OrderIconHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.item_fragment_home_iconlist_icon);
            text = (TextView) itemView.findViewById(R.id.item_fragment_home_iconlist_name);
            badge = new BadgeView(context);
            badge.setTargetView(icon);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int idx = index % 5;
            Intent it;
            if (idx == 4) {
                it = new Intent(context, ReturnGoodsOrderListActivity.class);
            } else {
                it = ActivityOrder.getIntent(context, idx + 1);
            }
            context.startActivity(it);
        }
    }

    public class RecommandGoodsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle;
        TextView tvMoney;
        SimpleDraweeView sdv;
        public RecommendModel model;

        public RecommandGoodsHolder(View itemView) {
            super(itemView);
//             item_goods_grid
            tvTitle = (TextView) itemView.findViewById(R.id.goods_title);
            tvMoney = (TextView) itemView.findViewById(R.id.money);
            sdv = (SimpleDraweeView) itemView.findViewById(R.id.item_goods_grid_sdv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            context.startActivity(ActivityMedicinesInfo.getIntent(context, model.getWAREID(), model.getLOGISTICS_COMPANY_ID()));
        }
    }

    public class OrderTitleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public OrderTitleHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent it = ActivityOrder.getIntent(context, 0);
            context.startActivity(it);
        }
    }

    public class TempViewHolder extends RecyclerView.ViewHolder {

        public TempViewHolder(View itemView) {
            super(itemView);
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

}
