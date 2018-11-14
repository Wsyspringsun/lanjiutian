package com.wyw.ljtds.ui.goods;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.CategoryBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.CommodityListModel;
import com.wyw.ljtds.model.GoodsModel;
import com.wyw.ljtds.model.SingleCurrentUser;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.home.ActivitySearch;
import com.wyw.ljtds.ui.user.ActivityMessage;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.DividerGridItemDecoration;
import com.wyw.ljtds.widget.RecycleViewDivider;
import com.wyw.ljtds.widget.SpaceItemDecoration;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import static com.wyw.ljtds.adapter.goodsinfo.MedicineItemAdapter1.RESIZE;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

@ContentView(R.layout.activity_goods_list)
public class ActivityGoodsList extends BaseActivity {
    private static final String TAG_TYPE_ID = "com.wyw.ljtds.ui.goods.ActivityGoodsList.TAG_TYPE_ID";
    private static final String TAG_TYPE_KEYWORD = "search";
    @ViewInject(R.id.activity_goods_list_sr)
    SwipeRefreshLayout srl;
    @ViewInject(R.id.recyclerView)
    private RecyclerView recyclerView;
    @ViewInject(R.id.back)
    private ImageView back;
    @ViewInject(R.id.zxing)
    private LinearLayout zxing;
    @ViewInject(R.id.edHeader)
    private TextView search;
    @ViewInject(R.id.ll_message)
    private LinearLayout llMessage;
    @ViewInject(R.id.paixu1_tv)
    private TextView paixu1_tv;
    @ViewInject(R.id.paixu1_iv)
    private ImageView paixu1_iv;
    @ViewInject(R.id.paixu2_tv)
    private TextView paixu2_tv;
    @ViewInject(R.id.paixu3_tv)
    private TextView paixu3_tv;
    @ViewInject(R.id.paixu3_iv)
    private ImageView paixu3_iv;
    @ViewInject(R.id.paixu4_tv)
    private TextView paixu4_tv;

    @ViewInject(R.id.main_header_location)
    TextView tvLocation;

    //无数据时的界面
    private View noData;
    private boolean isRise = true;
    private List<CommodityListModel> list;
    private MyAdapter adapter;
    private int pageIndex = 1;
    private boolean end = false;
    private String keyword = "";//搜索的关键字
    private String classId = "";//分类的typeid
    private String orderby = "";

    public static Intent getIntent(Context ctx, String typeId, String keyword) {
        Intent it = new Intent(ctx, ActivityGoodsList.class);
        it.putExtra(TAG_TYPE_ID, typeId);
        it.putExtra(TAG_TYPE_KEYWORD, keyword);
        return it;
    }

    @Event(value = {R.id.back, R.id.edHeader, R.id.paixu1, R.id.paixu2, R.id.paixu3, R.id.paixu4})
    private void onclick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.edHeader:
                startActivity(ActivitySearch.getIntent(this, 1, search.getText().toString()));
                break;

            case R.id.paixu1:
                reset();
                paixu1_tv.setTextColor(getResources().getColor(R.color.base_bar));
                paixu1_iv.setImageDrawable(getResources().getDrawable(R.mipmap.paixu_1));
                pageIndex = 1;
                getlist(classId, true, true, "", keyword);
                orderby = "";
                break;

            case R.id.paixu2:
                reset();
                paixu2_tv.setTextColor(getResources().getColor(R.color.base_bar));
                pageIndex = 1;
                getlist(classId, true, true, "SALE_NUM", keyword);
                orderby = "SALE_NUM";
                break;

            case R.id.paixu3:
                if (isRise) {
                    reset();
                    paixu3_tv.setTextColor(getResources().getColor(R.color.base_bar));
                    paixu3_iv.setImageDrawable(getResources().getDrawable(R.mipmap.paixu_4));
                    isRise = false;
                    pageIndex = 1;
                    getlist(classId, true, true, "COST_MONEY", keyword);
                    orderby = "COST_MONEY";
                } else {
                    reset();
                    paixu3_tv.setTextColor(getResources().getColor(R.color.base_bar));
                    paixu3_iv.setImageDrawable(getResources().getDrawable(R.mipmap.paixu_5));
                    isRise = true;
                    pageIndex = 1;
                    getlist(classId, true, true, "COST_MONEY desc", keyword);
                    orderby = "COST_MONEY desc";
                }
                break;

            case R.id.paixu4:
                reset();
                paixu4_tv.setTextColor(getResources().getColor(R.color.base_bar));
                pageIndex = 1;
                getlist(classId, true, true, "EVALUATE", keyword);
                orderby = "EVALUATE";
                break;
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getlist(classId, true, true, "", keyword);
                srl.setRefreshing(false);
            }
        });
        keyword = getIntent().getStringExtra(TAG_TYPE_KEYWORD);
        if (!StringUtils.isEmpty(keyword)) {
            search.setText(keyword);
        }

        classId = getIntent().getStringExtra("typeid");
        if (StringUtils.isEmpty(classId)) {
            classId = getIntent().getStringExtra(TAG_TYPE_ID);
        }
        getlist(classId, true, true, "", keyword);


//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);//必须有
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置方向滑动
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        noData = getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recyclerView.getParent(), false);
        recyclerView.addItemDecoration(new SpaceItemDecoration(10));


        adapter = new MyAdapter();
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getlist(classId, true, false, orderby, keyword);
            }
        });
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                String id = adapter.getItem(i).getCommodityId();
                Intent it = ActivityLifeGoodsInfo.getIntent(ActivityGoodsList.this, id);
//                it.putExtra(AppConfig.IntentExtraKey.MEDICINE_INFO_ID, adapter.getData().get(i).getCommodityId());
                startActivity(it);
            }
        });
        recyclerView.setAdapter(adapter);

        zxing.setVisibility(View.GONE);
        llMessage.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
        setLocation();
    }

    BizDataAsyncTask<List<CommodityListModel>> getListTask;

    private void getlist(final String classid, final boolean loadmore, final boolean refresh, final String orderby, final String keyword) {
        setLoding(this, false);
        getListTask = new BizDataAsyncTask<List<CommodityListModel>>() {
            @Override
            protected List<CommodityListModel> doExecute() throws ZYException, BizFailure {
                Log.e(AppConfig.ERR_TAG, "params:" + classid + "; " + keyword + "; " + orderby + "; " + loadmore + "; " + refresh);
                if (refresh) {
                    return CategoryBiz.getCommodityList("", classid, orderby, keyword, "0", AppConfig.DEFAULT_PAGE_COUNT + "");
                } else {
                    return CategoryBiz.getCommodityList(GoodsModel.HUODONG_JIFEN, classid, orderby, keyword, pageIndex + "", AppConfig.DEFAULT_PAGE_COUNT + "");
                }
            }

            @Override
            protected void onExecuteSucceeded(List<CommodityListModel> commodityListModels) {
                if (commodityListModels.size() < AppConfig.DEFAULT_PAGE_COUNT) {
                    end = true;
                    //可以加入emptyview
                    if (loadmore && commodityListModels.size() == 0) {
                        adapter.setEmptyView(noData);
                    }
                } else {
                    end = false;

                }

                list = commodityListModels;
                if (loadmore) {
                    adapter.addData(list);
                    adapter.notifyDataSetChanged();
                }
                if (refresh) {
                    adapter.setNewData(list);
                    adapter.notifyDataSetChanged();
                }


                if (end) {
                    adapter.addFooterView(getFooterView());
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.removeAllFooterView();
                    adapter.notifyDataSetChanged();
                }


                pageIndex++;
                closeLoding();
            }

            @Override
            protected void OnExecuteFailed() {
                adapter.setEmptyView(noData);
                closeLoding();
            }
        };
        getListTask.execute();
    }

    private View getFooterView() {
        View view = getLayoutInflater().inflate(R.layout.main_end_view, (ViewGroup) recyclerView.getParent(), false);

        return view;
    }


    private class MyAdapter extends BaseQuickAdapter<CommodityListModel> {

        public MyAdapter() {
            super(R.layout.item_goods_grid, list);
        }


        @Override
        protected void convert(BaseViewHolder baseViewHolder, CommodityListModel commodityListModel) {
            baseViewHolder.setVisible(R.id.item_goods_grid_money_old, false);
            baseViewHolder.setText(R.id.goods_title, StringUtils.deletaFirst(commodityListModel.getTitle()))
                    .setText(R.id.money, "￥" + Utils.formatFee(commodityListModel.getCostMoney() + ""));
            if (GoodsModel.TOP_FLG_LINGYUAN.equals(commodityListModel.getTopFlg())) {
                baseViewHolder.setText(R.id.item_goods_grid_money_old, "￥" + Utils.formatFee(commodityListModel.getMarketPrice() + ""));
                baseViewHolder.setVisible(R.id.item_goods_grid_money_old, true);
            }

//            SimpleDraweeView goods_img = baseViewHolder.getView(R.id.item_goods_grid_sdv);
            ImageView goods_img = baseViewHolder.getView(R.id.item_goods_grid_sdv);
            if (StringUtils.isEmpty(commodityListModel.getImgPath())) {
            } else {
                Picasso.with(mContext).load(Uri.parse(AppConfig.IMAGE_PATH_LJT + commodityListModel.getImgPath())).resize(RESIZE, RESIZE).placeholder(R.mipmap.zhanweitu).into(goods_img);
            }

            /**
             * 活动小图标
             */
            baseViewHolder.setVisible(R.id.item_goods_huodong_te, false);
            baseViewHolder.setVisible(R.id.item_goods_huodong_zeng, false);
            if (GoodsModel.HUODONG_TEJIA.equals(commodityListModel.getTopFlg())) {
                baseViewHolder.setVisible(R.id.item_goods_huodong_te, true);
            } else if (GoodsModel.HUODONG_MANZENG.equals(commodityListModel.getTopFlg())) {
                baseViewHolder.setVisible(R.id.item_goods_huodong_zeng, true);
            }

        }
    }


    private void reset() {
        paixu1_tv.setTextColor(getResources().getColor(R.color.font_black2));
        paixu1_iv.setImageDrawable(getResources().getDrawable(R.mipmap.paixu_2));
        paixu2_tv.setTextColor(getResources().getColor(R.color.font_black2));
        paixu3_tv.setTextColor(getResources().getColor(R.color.font_black2));
        paixu3_iv.setImageDrawable(getResources().getDrawable(R.mipmap.paixu_3));
        paixu4_tv.setTextColor(getResources().getColor(R.color.font_black2));
        isRise = true;
    }

    private void setLocation() {
        if (SingleCurrentUser.location != null) {
            tvLocation.setText(SingleCurrentUser.location.getAddrStr());
        }
    }

}
