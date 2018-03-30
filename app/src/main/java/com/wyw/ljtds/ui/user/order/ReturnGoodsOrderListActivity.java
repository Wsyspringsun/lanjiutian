package com.wyw.ljtds.ui.user.order;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.ReturnGoodsBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.GoodsHandingModel;
import com.wyw.ljtds.model.MedicineOrder;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.goods.ActivityLifeGoodsInfo;
import com.wyw.ljtds.ui.user.ActivityMessage;
import com.wyw.ljtds.ui.user.wallet.BalanceFragment;
import com.wyw.ljtds.utils.StringUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * create by wsy on 2017-07-28
 */
@ContentView(R.layout.activity_returngoodsorder_list)
public class ReturnGoodsOrderListActivity extends BaseActivity {
    private static final String METHOD_IN = "0";
    private static final String METHOD_OUT = "1";

    @ViewInject(R.id.activity_returngoodsorder_srf)
    private SwipeRefreshLayout srf;
    @ViewInject(R.id.activity_returngoodsorder_ryv_data)
    private RecyclerView recyclerView;
    @ViewInject(R.id.activity_returngoodsorder_tv_tousu)
    private TextView tvTousu;


    private boolean end = false;

    private List<GoodsHandingModel> list;
    private MyAdapter1 adapter1;

    private int page = 1;
    private LinearLayoutManager linearLayoutManager;


    private void loadData() {
        setLoding(this, false);
        new BizDataAsyncTask<List<GoodsHandingModel>>() {
            @Override
            protected List<GoodsHandingModel> doExecute() throws ZYException, BizFailure {
                return ReturnGoodsBiz.read();
            }

            @Override
            protected void onExecuteSucceeded(List<GoodsHandingModel> goodsHandingModels) {
                closeLoding();
                list = goodsHandingModels;
                bindData2View();

            }


            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.activity_returngoodsorder_tv_tousu:
                    openChat(getString(R.string.tousu), "", AppConfig.CHAT_XN_LJT_SETTINGID1, AppConfig.CHAT_XN_LJT_TITLE1, false, "");
                    break;
                case R.id.back:
                    finish();
                    break;
            }
        }
    };

    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);//必须有
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置水平方向滑动
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent it = ReturnGoodsOrderInfoActivity.getIntent(ReturnGoodsOrderListActivity.this, adapter1.getItem(i).getReturnGoodsHandingId());
                startActivity(it);
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, final int i) {

            }

            @Override
            public void onItemChildLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

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
            baseViewHolder.setText(R.id.tv_group_name, s.getGroupName())
                    .setText(R.id.tv_returngoodsorder_stat, statDisplay)
                    .setText(R.id.tv_returngoodsorder_show, "订单号:" + s.getOrderTradeId() + "\r\n退款单号: " + s.getReturnGoodsHandingId() + "\r\n申请日期：" + s.getInsDate().split(" ")[0] + "\r\n受理日期：" + s.getUpdDate().split(" ")[0])
                    .setText(R.id.tv_returngoodsorder_returnmoney, "退款额度：￥" + s.getReturnMoney());

            RecyclerView goods = baseViewHolder.getView(R.id.rv_orlderlist);
            goods.setLayoutManager(new LinearLayoutManager(ReturnGoodsOrderListActivity.this));
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
        protected void convert(BaseViewHolder baseViewHolder, final MedicineOrder goods) {
            baseViewHolder.setText(R.id.item_order_submit_goods_size, " 规格：" + goods.getCommodityColor() + " " + goods.getCommoditySize());
            baseViewHolder.setText(R.id.item_order_submit_goods_title, goods.getCommodityName())
                    .setText(R.id.item_order_submit_goods_money, "￥" + goods.getCostMoney())
                    .setVisible(R.id.item_order_submit_goods_money, false)
                    .setText(R.id.item_order_submit_goods_number, "X" + goods.getExchangeQuanlity());

            SimpleDraweeView simpleDraweeView = baseViewHolder.getView(R.id.item_order_submit_goods_pic);
            simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(ActivityLifeGoodsInfo.getIntent(ReturnGoodsOrderListActivity.this, goods.getCommodityId()));
                }
            });
            Log.e(AppConfig.ERR_TAG, AppConfig.IMAGE_PATH_LJT + goods.getImgPath());
            if (!StringUtils.isEmpty(goods.getImgPath())) {
                simpleDraweeView.setImageURI(Uri.parse(AppConfig.IMAGE_PATH_LJT + goods.getImgPath()));
            }
//
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.page = 1;
        initEvents();
        initRecyclerView();
    }

    private void initEvents() {
        tvTousu.setOnClickListener(listener);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.back);
        btnBack.setOnClickListener(listener);
        srf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                srf.setRefreshing(false);
            }
        });
        TextView tvTitle = (TextView) findViewById(R.id.activity_fragment_title);
        tvTitle.setText(getTitle());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void bindData2View() {
        if (adapter1 == null) {
            adapter1 = new MyAdapter1();
            View noData = this.getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recyclerView.getParent(), false);
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
}
