package com.wyw.ljtds.ui.user.order;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.ReturnGoodsBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.GoodsHandingModel;
import com.wyw.ljtds.model.MedicineOrder;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by wsy on 17-7-28.
 */

@ContentView(R.layout.fragment_order_list)
public class ReturnGoodsOrderListFragment extends BaseFragment {
    private static final String METHOD_IN = "0";
    private static final String METHOD_OUT = "1";
    @ViewInject(R.id.reclcyer)
    private RecyclerView recyclerView;
    private boolean end = false;

    private List<GoodsHandingModel> list;
    private MyAdapter1 adapter1;

    private int page = 1;
    private LinearLayoutManager linearLayoutManager;

    /**
     * @param cat 余额类型
     * @return
     */
    public static ReturnGoodsOrderListFragment newInstance() {
        ReturnGoodsOrderListFragment fragment = new ReturnGoodsOrderListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.page = 1;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initRecyclerView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }


    private void loadData() {
        setLoding(getActivity(), false);
        new BizDataAsyncTask<List<GoodsHandingModel>>() {

            @Override
            protected List<GoodsHandingModel> doExecute() throws ZYException, BizFailure {
                return ReturnGoodsBiz.read();
            }

            @Override
            protected void onExecuteSucceeded(List<GoodsHandingModel> goodsHandingModels) {
                closeLoding();
                Log.e(AppConfig.ERR_TAG, GsonUtils.Bean2Json(goodsHandingModels));
                list = goodsHandingModels;
                ReturnGoodsOrderListFragment.this.updAdapter();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    private void updAdapter() {
        if (adapter1 == null) {
            adapter1 = new MyAdapter1();
            View noData = getActivity().getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recyclerView.getParent(), false);
            adapter1.setEmptyView(noData);
            recyclerView.setAdapter(adapter1);
        }
        if (list == null || list.size() <= 0) {
            end = true;
            return;
        }
        if (page <= 1) {
            adapter1.setNewData(list);
        } else {
            adapter1.addData(list);
        }
        adapter1.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(getActivity());//必须有
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置水平方向滑动
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (adapter1 == null)
                    return;
                int cnt = adapter1.getItemCount();
//                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!end && !loading && (lastVisibleItem) >= cnt) {
                    page = page + 1;
                    loadData();
                }
            }
        });
    }


    private class MyAdapter1 extends BaseQuickAdapter<GoodsHandingModel> {
        public MyAdapter1() {
            super(R.layout.item_list_returngoodsorder, list);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, GoodsHandingModel s) {
            String statDisplay = MedicineOrder.StatData.get(s.getReturnStatus());
            if (StringUtils.isEmpty(statDisplay))
                statDisplay = getString(R.string.nil);
            Log.e(AppConfig.ERR_TAG, "statDisplay:" + statDisplay);
            baseViewHolder.setText(R.id.tv_group_name, s.getGroupName())
                    .setText(R.id.tv_returngoodsorder_stat, statDisplay)
                    .setText(R.id.tv_returngoodsorder_show, "订单号:" + s.getOrderTradeId() + "\r\n退款单号: " + s.getReturnGoodsHandingId() + "\r\n申请日期：" + s.getInsDate().split(" ")[0] + "\r\n受理日期：" + s.getUpdDate().split(" ")[0])
                    .setText(R.id.tv_returngoodsorder_returnmoney, "退款额度：￥" + s.getReturnMoney());

            RecyclerView goods = baseViewHolder.getView(R.id.rv_orlderlist);
            goods.setLayoutManager(new LinearLayoutManager(getActivity()));
            goods.setItemAnimator(new DefaultItemAnimator());
            goods.setAdapter(new MyAdapter2(s.getCommodityOrderList()));

        }
    }

    //商品adapter
    private class MyAdapter2 extends BaseQuickAdapter<MedicineOrder> {
        public MyAdapter2(List<MedicineOrder> lists) {
            super(R.layout.item_order_submit_goods, lists);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, MedicineOrder goods) {
            baseViewHolder.setText(R.id.size, " 规格：" + goods.getCommodityColor() + " " + goods.getCommoditySize());
            baseViewHolder.setText(R.id.title, goods.getCommodityName())
                    .setText(R.id.money, "￥" + goods.getCostMoney())
                    .setText(R.id.number, "X" + goods.getExchangeQuanlity());

            SimpleDraweeView simpleDraweeView = baseViewHolder.getView(R.id.iv_adapter_list_pic);
            Log.e(AppConfig.ERR_TAG, AppConfig.IMAGE_PATH_LJT + goods.getImgPath());
            if (!StringUtils.isEmpty(goods.getImgPath())) {
                simpleDraweeView.setImageURI(Uri.parse(AppConfig.IMAGE_PATH_LJT + goods.getImgPath()))
                ;
            }
//
        }
    }
}
