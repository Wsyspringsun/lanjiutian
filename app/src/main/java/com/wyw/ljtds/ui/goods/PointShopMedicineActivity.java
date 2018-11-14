package com.wyw.ljtds.ui.goods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.list.MedicineLinearListAdapter;
import com.wyw.ljtds.biz.biz.CategoryBiz;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.GoodsModel;
import com.wyw.ljtds.model.MedicineListModel;
import com.wyw.ljtds.model.SingleCurrentUser;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.widget.ClearEditText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * create by wsy on 2017-07-28
 */
@ContentView(R.layout.activity_point_shop_goods)
public class PointShopMedicineActivity extends BaseActivity {
    @ViewInject(R.id.fragment_toolbar_common_white_title)
    private TextView tvTitle;
    @ViewInject(R.id.activity_point_shop_goods_iv_search)
    ImageView imgSou;
    @ViewInject(R.id.activity_point_shop_goods_search_keyword)
    ClearEditText etSearch;

    @ViewInject(R.id.activity_point_shop_goods_data)
    private RecyclerView ryvShopGoods;
    CategoryBiz categoryBiz;

    private MedicineLinearListAdapter adapter;
    private boolean end = false;
    String lat = "", lng = "";
    private String keyword = "";//搜索的关键字
    private int pageIndex = 1;
    private List<MedicineListModel> list;
    private View noData;

    LinearLayoutManager llm;

    @Event(value = {R.id.fragment_toolbar_common_white_return})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_toolbar_common_white_return:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryBiz = CategoryBiz.getInstance(this);

        lat = "" + SingleCurrentUser.defaultLat;
        lng = "" + SingleCurrentUser.defaultLng;

        tvTitle.setText(getTitle());

        llm = new LinearLayoutManager(this);
        ryvShopGoods.setLayoutManager(llm);
        ryvShopGoods.setItemAnimator(new DefaultItemAnimator());

        adapter = new MedicineLinearListAdapter(this, list);
        noData = getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) ryvShopGoods.getParent(), false);
        adapter.setEmptyView(noData);
        ryvShopGoods.setAdapter(adapter);
        ryvShopGoods.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                MedicineListModel detail = adapter.getData().get(i);
                Intent it = ActivityMedicinesInfo.getIntent(PointShopMedicineActivity.this, detail.getWAREID(), detail.getLOGISTICS_COMPANY_ID());
                startActivity(it);
            }
        });
        ryvShopGoods.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    loadData();
                }

            }
        });


        //搜索
        imgSou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch();
            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
        keyword = etSearch.getText().toString();
        pageIndex = 1;
        loadData();
    }

    public void loadData() {
        setLoding(this, false);
        new BizDataAsyncTask<List<MedicineListModel>>(this) {
            @Override
            protected List<MedicineListModel> doExecute() throws ZYException, BizFailure {
                return categoryBiz.getMedicineList("0", GoodsModel.HUODONG_JIFEN, "", "", keyword, pageIndex + "", AppConfig.DEFAULT_PAGE_COUNT + "", lat, lng);
            }

            @Override
            protected void onExecuteSucceeded(List<MedicineListModel> medicineListModel) {
                closeLoding();
                list = medicineListModel;
                bindData2View();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    private void bindData2View() {
        if (pageIndex == 1) {
            adapter.setNewData(list);
        } else {
            adapter.addData(list);
        }
        if (list == null || list.size() <= 0) {
            end = true;
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public static Intent getIntent(Context ctx) {
        Intent it = new Intent(ctx, PointShopMedicineActivity.class);
        return it;
    }
}
