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
import com.wyw.ljtds.widget.RecylerViewPagenator;
import com.wyw.ljtds.widget.SpaceItemDecoration;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Arrays;
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
    RecylerViewPagenator pageNater;
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

                orderby = "";
                getlist();
                break;

            case R.id.paixu2:
                reset();

                paixu2_tv.setTextColor(getResources().getColor(R.color.base_bar));

                orderby = "SALE_NUM";
                getlist();
                break;

            case R.id.paixu3:
                if (isRise) {
                    reset();
                    paixu3_tv.setTextColor(getResources().getColor(R.color.base_bar));
                    paixu3_iv.setImageDrawable(getResources().getDrawable(R.mipmap.paixu_4));
                    isRise = false;
                    orderby = "COST_MONEY";
                    getlist();
                } else {
                    reset();
                    paixu3_tv.setTextColor(getResources().getColor(R.color.base_bar));
                    paixu3_iv.setImageDrawable(getResources().getDrawable(R.mipmap.paixu_5));
                    isRise = true;
                    orderby = "COST_MONEY desc";
                    getlist();
                }
                break;

            case R.id.paixu4:
                reset();
                paixu4_tv.setTextColor(getResources().getColor(R.color.base_bar));
                orderby = "EVALUATE";
                getlist();
                break;
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reset();
                getlist();
                srl.setRefreshing(false);
            }
        });
        keyword = getIntent().getStringExtra(TAG_TYPE_KEYWORD);
        if (!StringUtils.isEmpty(keyword)) {
            search.setText(keyword);
        }

        classId = getIntent().getStringExtra(TAG_TYPE_ID);


//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);//必须有
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置方向滑动
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        noData = getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recyclerView.getParent(), false);
        recyclerView.addItemDecoration(new SpaceItemDecoration(10));
        //分页
        pageNater = new RecylerViewPagenator(recyclerView, new RecylerViewPagenator.NextPageListner() {
            @Override
            public void nextPage() {
                getlist();
            }
        });


        adapter = new MyAdapter();
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

        getlist();
    }

    private void getlist() {
        setLoding(this, false);
        pageNater.setLoading(loading);
        new BizDataAsyncTask<List<CommodityListModel>>() {
            @Override
            protected List<CommodityListModel> doExecute() throws ZYException, BizFailure {
                Utils.log("goodslist:" + classId + "-" + keyword + "-" + "" + pageNater.getPage());

//                return CategoryBiz.getCommodityList("", classId, orderby, keyword, "" + pageNater.getPage(), AppConfig.DEFAULT_PAGE_COUNT + "");
                return CategoryBiz.getCommodityList("", classId, orderby, keyword, "" + (pageNater.getPage() - 1), AppConfig.DEFAULT_PAGE_COUNT + "");
//                return CategoryBiz.getCommodityList(GoodsModel.HUODONG_JIFEN, classid, orderby, keyword, pageIndex + "", AppConfig.DEFAULT_PAGE_COUNT + "");
            }

            @Override
            protected void onExecuteSucceeded(List<CommodityListModel> commodityListModels) {
                closeLoding();
                pageNater.setLoading(loading);
                list = commodityListModels;
                adapter.removeAllFooterView();
                if (1 == pageNater.getPage()) {
                    if (list == null || list.size() <= 0) {
                        adapter.setEmptyView(noData);
                        pageNater.setEnd(true);
                        return;
                    }
                    adapter.setNewData(list);
                } else {
                    if (list == null || list.size() <= 0) {
                        pageNater.setEnd(true);
                        return;
                    }
                    adapter.addData(list);
                    adapter.addFooterView(getFooterView());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
                pageNater.setLoading(loading);
            }
        }.execute();
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
                    .setText(R.id.item_goods_grid_money, "￥" + Utils.formatFee(commodityListModel.getCostMoney() + ""));

//            SimpleDraweeView goods_img = baseViewHolder.getView(R.id.item_goods_grid_sdv);
            ImageView goods_img = baseViewHolder.getView(R.id.item_goods_grid_sdv);
            if (StringUtils.isEmpty(commodityListModel.getImgPath())) {
            } else {
                Picasso.with(mContext).load(Uri.parse(AppConfig.IMAGE_PATH_LJT + commodityListModel.getImgPath())).resize(RESIZE, RESIZE).placeholder(R.mipmap.zhanweitu).into(goods_img);
            }

            /**
             * 活动小图标
             */
            if (GoodsModel.TOP_FLG_LINGYUAN.equals(commodityListModel.getTopFlg())) {
                baseViewHolder.setText(R.id.item_goods_grid_money_old, "￥" + Utils.formatFee(commodityListModel.getMarketPrice() + ""));
                baseViewHolder.setVisible(R.id.item_goods_grid_money_old, true);
            }
            baseViewHolder.setVisible(R.id.item_goods_huodong_te, false);
            baseViewHolder.setVisible(R.id.item_goods_huodong_zeng, false);
            if (GoodsModel.HUODONG_TEJIA.equals(commodityListModel.getTopFlg())) {
                baseViewHolder.setVisible(R.id.item_goods_huodong_te, true);
                baseViewHolder.setText(R.id.item_goods_grid_money_old, "￥" + Utils.formatFee(commodityListModel.getMarketPrice() + ""));
                baseViewHolder.setVisible(R.id.item_goods_grid_money_old, true);
            } else if (Arrays.asList(GoodsModel.HUODONG_MANZENG).contains(commodityListModel.getTopFlg())) {
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
        pageNater.setPage(1);
        pageNater.setEnd(false);
        pageNater.setLoading(false);
    }

    private void setLocation() {
        if (SingleCurrentUser.location != null) {
            tvLocation.setText(SingleCurrentUser.location.getAddrStr());
        }
    }

}
