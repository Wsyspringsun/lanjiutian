package com.wyw.ljtds.ui.goods;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.squareup.picasso.Picasso;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.CategoryBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.CommodityListModel;
import com.wyw.ljtds.model.GoodsModel;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.ClearEditText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

@ContentView(R.layout.activity_point_shop_goods)
public class PointShopLifeGoodsListActivity extends BaseActivity {
    private static final String TAG_TYPE_ID = "com.wyw.ljtds.ui.goods.PointShopLifeGoodsListActivity.TAG_TYPE_ID";
    @ViewInject(R.id.activity_point_shop_goods_data)
    private RecyclerView recyclerView;
    @ViewInject(R.id.activity_point_shop_goods_search_keyword)
    private ClearEditText search;
    @ViewInject(R.id.activity_point_shop_goods_iv_search)
    private ImageView imgSou;

    @ViewInject(R.id.fragment_toolbar_common_white_title)
    private TextView tvTitle;

    //无数据时的界面
    private View noData;
    private List<CommodityListModel> list;
    private MyAdapter adapter;
    private int pageIndex = 0;
    private boolean end = false;
    private String keyword = "";//搜索的关键字
    private String classId = "";//分类的typeid
    private String orderby = "";
    private LinearLayoutManager llm;

    public static Intent getIntent(Context ctx) {
        Intent it = new Intent(ctx, PointShopLifeGoodsListActivity.class);
        return it;
    }

    @Event(value = {R.id.fragment_toolbar_common_white_return})
    private void onclick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.fragment_toolbar_common_white_return:
                finish();
                break;
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText(getTitle());
        llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        noData = getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recyclerView.getParent(), false);
        adapter = new MyAdapter();
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                String id = adapter.getItem(i).getCommodityId();
                Intent it = ActivityLifeGoodsInfo.getIntent(PointShopLifeGoodsListActivity.this, id);
//                it.putExtra(AppConfig.IntentExtraKey.MEDICINE_INFO_ID, adapter.getData().get(i).getCommodityId());
                startActivity(it);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (adapter == null) return;
                int cnt = adapter.getItemCount() - 1;
                int lastVisibleItem = llm.findLastVisibleItemPosition();
                if (!end && !loading && (lastVisibleItem >= cnt)) {
                    pageIndex = pageIndex + 1;
                    getlist();
                }
            }
        });

        recyclerView.setAdapter(adapter);

        //搜索
        imgSou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch();
            }
        });
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //如果actionId是搜索的id，则进行下一步的操作
                    doSearch();
                }
                return false;
            }
        });
    }

    private void doSearch() {
        keyword = search.getText().toString();
        pageIndex = 1;
        getlist();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getlist();
    }


    private void getlist() {
        setLoding(this, false);
        new BizDataAsyncTask<List<CommodityListModel>>() {
            @Override
            protected List<CommodityListModel> doExecute() throws ZYException, BizFailure {
                return CategoryBiz.getCommodityList(GoodsModel.HUODONG_JIFEN, "", orderby, keyword, pageIndex + "", AppConfig.DEFAULT_PAGE_COUNT + "");
            }

            @Override
            protected void onExecuteSucceeded(List<CommodityListModel> commodityListModels) {
                closeLoding();
                list = commodityListModels;
                bindData2View();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    private void bindData2View() {
        if (pageIndex == 0) {
            adapter.setNewData(list);
        } else {
            adapter.addData(list);
        }
        if (list == null || list.size() <= 0) {
            end = true;
        }
        adapter.notifyDataSetChanged();
    }


    private View getFooterView() {
        View view = getLayoutInflater().inflate(R.layout.main_end_view, (ViewGroup) recyclerView.getParent(), false);
        return view;
    }


    private class MyAdapter extends BaseQuickAdapter<CommodityListModel> {

        public MyAdapter() {
            super(R.layout.item_shopgoods, list);
        }


        @Override
        protected void convert(BaseViewHolder baseViewHolder, CommodityListModel commodityListModel) {
            baseViewHolder.setText(R.id.item_shopgoods_tv_title, StringUtils.deletaFirst(commodityListModel.getTitle()))
                    .setText(R.id.item_shopgoods_tv_money, "￥" + Utils.formatFee(commodityListModel.getCostMoney() + "") + "+" + Utils.formatFee(commodityListModel.getCostPoint()) + "积分");
            /*if (GoodsModel.TOP_FLG_LINGYUAN.equals(commodityListModel.getTopFlg())) {
                baseViewHolder.setText(R.id.item_goods_grid_money_old, "￥" + Utils.formatFee(commodityListModel.getMarketPrice() + ""));
                baseViewHolder.setVisible(R.id.item_goods_grid_money_old, true);
            }*/

//            SimpleDraweeView goods_img = baseViewHolder.getView(R.id.item_goods_grid_sdv);
            ImageView goods_img = baseViewHolder.getView(R.id.item_shopgoods_iv_thumb);
            if (!StringUtils.isEmpty(commodityListModel.getImgPath())) {
                Picasso.with(mContext).load(Uri.parse(AppConfig.IMAGE_PATH_LJT + commodityListModel.getImgPath())).placeholder(R.drawable.img_adv_zhanwei).into(goods_img);
            }

            /**
             * 活动小图标
             baseViewHolder.setVisible(R.id.item_goods_huodong_te, false);
             baseViewHolder.setVisible(R.id.item_goods_huodong_zeng, false);
             if (GoodsModel.HUODONG_TEJIA.equals(commodityListModel.getTopFlg())) {
             baseViewHolder.setVisible(R.id.item_goods_huodong_te, true);
             } else if (GoodsModel.HUODONG_MANZENG.equals(commodityListModel.getTopFlg())) {
             baseViewHolder.setVisible(R.id.item_goods_huodong_zeng, true);
             }
             */

        }
    }
}
