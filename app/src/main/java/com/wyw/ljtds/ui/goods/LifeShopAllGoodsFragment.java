package com.wyw.ljtds.ui.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.commodity.CommodityAdapter;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.model.CommodityListModel;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.SpaceItemDecoration;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * create by wsy
 * 全部宝贝
 */
@ContentView(R.layout.fragment_life_shop)
public class LifeShopAllGoodsFragment extends BaseFragment {
    private static final java.lang.String ARG_SHOPID = "com.wyw.ljtds.ui.goods.LifeShopAllGoodsFragment.ARG_SHOPID";
    private static final java.lang.String ARG_TYPE = "com.wyw.ljtds.ui.goods.LifeShopAllGoodsFragment.ARG_TYPE";

    @ViewInject(R.id.fragment_lifeshop_ry_data)
    RecyclerView ryData;
    private View noData;

    private String storeId;//店铺id
    private String keyword; //查询关键字
    private int startIdx; //起始索引
    private boolean end; //是否到达页尾

    private List<CommodityListModel> commodities;
    private CommodityAdapter adapter;
    private GridLayoutManager glm;
    private String type;

    public LifeShopAllGoodsFragment() {
    }

    public static LifeShopAllGoodsFragment newInstance(String shopid, String type) {
        LifeShopAllGoodsFragment fragment = new LifeShopAllGoodsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SHOPID, shopid);
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        glm = new GridLayoutManager(getActivity(), 2);
        ryData.setLayoutManager(glm);
        ryData.setItemAnimator(new DefaultItemAnimator());
        noData = LayoutInflater.from(getActivity()).inflate(R.layout.main_empty_view, (ViewGroup) ryData.getParent(), false);
        ryData.addItemDecoration(new SpaceItemDecoration(10));
        ryData.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                CommodityListModel goods = adapter.getData().get(i);
                Intent it = ActivityLifeGoodsInfo.getIntent(getActivity(), goods.getCommodityId());
                startActivity(it);
            }
        });
        ryData.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (adapter == null) return;
                int cnt = adapter.getItemCount() - 1;
                int lastVisibleItem = glm.findLastVisibleItemPosition();
                if (!end && !loading && (lastVisibleItem >= cnt)) {
                    startIdx = startIdx + 1;
                    loadData();
                }
            }
        });

        storeId = getArguments().getString(ARG_SHOPID);
        type = getArguments().getString(ARG_TYPE);
        keyword = "";
        startIdx = 0;

        loadData();
    }

    public void bindData2View() {
        if (!isAdded()) return;

        if (adapter == null) {
            adapter = new CommodityAdapter(null);
            ryData.setAdapter(adapter);
        }
        if (startIdx == 0) {
            adapter.setNewData(commodities);
            if (commodities == null || commodities.size() <= 0) {
                adapter.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.main_empty_view, null));
            }
        } else {
            adapter.addData(commodities);
        }
        adapter.notifyDataSetChanged();
    }

    public void loadData() {
        final String shopId = getArguments().getString(ARG_SHOPID);
        Utils.log("getShopCommodities shopId:" + shopId);
        new BizDataAsyncTask<List<CommodityListModel>>() {

            @Override
            protected List<CommodityListModel> doExecute() throws ZYException, BizFailure {
                return GoodsBiz.getShopCommodities(type, storeId, keyword, startIdx + "");
            }

            @Override
            protected void onExecuteSucceeded(List<CommodityListModel> commodityListModels) {
                commodities = commodityListModels;
                bindData2View();
            }

            @Override
            protected void OnExecuteFailed() {

            }
        }.execute();
    }

}
