package com.wyw.ljtds.ui.goods;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.CategoryBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.MedicineListModel;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.find.FragmentFind;
import com.wyw.ljtds.ui.home.ActivitySearch;
import com.wyw.ljtds.ui.user.ActivityMessage;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.widget.DividerGridItemDecoration;
import com.wyw.ljtds.widget.SpaceItemDecoration;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

@ContentView(R.layout.activity_goods_list)
public class ActivityMedicineList extends BaseActivity {
    @ViewInject(R.id.recyclerView)
    private RecyclerView recyclerView;
    @ViewInject(R.id.back)
    private ImageView back;
    @ViewInject(R.id.edHeader)
    private TextView search;
    @ViewInject(R.id.xiaoxi)
    private ImageView image;
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

    //无数据时的界面
    private View noData;
    private boolean isRise = true;
    private List<MedicineListModel> list;
    private MyAdapter adapter;
    private boolean end = false;
    private String mtd_find = "";

    /*8
    params for load data
     */
    private String keyword = "";//搜索的关键字
    private String classId = "";//分类的typeid
    private String orderby = "";
    private int pageIndex = 1;

    @Event(value = {R.id.back, R.id.edHeader, R.id.xiaoxi, R.id.paixu1, R.id.paixu2, R.id.paixu3, R.id.paixu4})
    private void onclick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.edHeader:
                it = new Intent(this, ActivitySearch.class);
                it.putExtra("from", 0);
                finish();
                startActivity(it);
                break;

            case R.id.xiaoxi:
                it = new Intent(this, ActivityMessage.class);
                startActivity(it);
                break;

            case R.id.paixu1:
                reset();
                paixu1_tv.setTextColor(getResources().getColor(R.color.base_bar));
                paixu1_iv.setImageDrawable(getResources().getDrawable(R.mipmap.paixu_1));
                orderby = "";
                sortData();
                break;
            case R.id.paixu2:
                reset();
                paixu2_tv.setTextColor(getResources().getColor(R.color.base_bar));
                orderby = "SALE_NUM";
                sortData();
                break;
            case R.id.paixu3:
                reset();
                if (isRise) {
                    paixu3_tv.setTextColor(getResources().getColor(R.color.base_bar));
                    paixu3_iv.setImageDrawable(getResources().getDrawable(R.mipmap.paixu_4));
                    isRise = false;
                    orderby = "SALEPRICE";
                } else {
                    paixu3_tv.setTextColor(getResources().getColor(R.color.base_bar));
                    paixu3_iv.setImageDrawable(getResources().getDrawable(R.mipmap.paixu_5));
                    isRise = true;
                    orderby = "SALEPRICE desc";
                }
                sortData();
                break;
            case R.id.paixu4:
                reset();
                paixu4_tv.setTextColor(getResources().getColor(R.color.base_bar));
                orderby = "EVALUATE_CNT";
                sortData();
                break;
        }
    }

    private void sortData() {
        Log.e(AppConfig.ERR_TAG,"sortData");
        adapter = new MyAdapter();
        adapter.setNewData(new ArrayList<MedicineListModel>());
        adapter.notifyDataSetChanged();
        Log.e(AppConfig.ERR_TAG,"new adapter:"+adapter.getItemCount());
        recyclerView.setAdapter(adapter);
        Log.e(AppConfig.ERR_TAG,"setAdapter adapter:"+adapter.getItemCount());
        pageIndex = 1;
        loadData();
    }

    private void loadData() {
        if (FragmentFind.FIND_MTD_QUICK.equals(mtd_find)) {
            findlist();
        } else {
            getlist();
        }
    }

    void initParams() {
        this.mtd_find = getIntent().getStringExtra(FragmentFind.TAG_FIND_MTD);
        Log.e(AppConfig.ERR_TAG, "find data mtd:" + mtd_find);
        if (FragmentFind.FIND_MTD_QUICK.equals(mtd_find)) {
            classId = getIntent().getStringExtra(FragmentFind.TAG_MTD_QUICK_PARAM);
        } else if (FragmentFind.FIND_MTD_QUICK2.equals(mtd_find)) {
            classId = getIntent().getStringExtra(FragmentFind.TAG_MTD_QUICK_PARAM);
        } else {
            keyword = getIntent().getStringExtra("search");
            classId = getIntent().getStringExtra("typeid");
        }
        Log.e(AppConfig.ERR_TAG, "params:[" + classId + "," + keyword + "]");
        pageIndex = 1;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final GridLayoutManager glm = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(glm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SpaceItemDecoration(10));
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent it = new Intent(ActivityMedicineList.this, ActivityMedicinesInfo.class);
                it.putExtra(AppConfig.IntentExtraKey.MEDICINE_INFO_ID, adapter.getData().get(i).getWAREID());
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
                int cnt = adapter.getItemCount();
                int totalItemCount = glm.getItemCount();
                int lastVisibleItem = glm.findLastVisibleItemPosition();
                Log.e(AppConfig.ERR_TAG, "itemCnt:" + cnt + "loadMore:[" + totalItemCount + "," + lastVisibleItem + "]");
                if (!end && !loading && (lastVisibleItem ) >= cnt) {
                    pageIndex = pageIndex + 1;
                    loadData();
                }
            }
        });
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);

        noData = getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recyclerView.getParent(), false);

        initParams();
        loadData();
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);//必须有
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置方向滑动

//        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
//            @Override
//            public void onLoadMoreRequested() {
//                getlist(classId, true, false, orderby, keyword);
//            }
//        });

    }

    BizDataAsyncTask<List<MedicineListModel>> getListTask;

    private void getlist() {
        getListTask = new BizDataAsyncTask<List<MedicineListModel>>() {
            @Override
            protected List<MedicineListModel> doExecute() throws ZYException, BizFailure {
                return CategoryBiz.getMedicineList(classId, orderby, keyword, pageIndex + "", AppConfig.DEFAULT_PAGE_COUNT + "");
            }

            @Override
            protected void onExecuteSucceeded(List<MedicineListModel> medicineListModel) {
                Log.e(AppConfig.ERR_TAG, "get data success");
                closeLoding();
                if (medicineListModel == null || medicineListModel.size() <= 0) {
                    end = true;
                    adapter.setEmptyView(noData);
                }

                list = medicineListModel;
                Log.e(AppConfig.ERR_TAG,"before load adapter:"+adapter.getItemCount());
                adapter.addData(list);
                adapter.notifyDataSetChanged();
                Log.e(AppConfig.ERR_TAG,"load adapter:"+adapter.getItemCount());
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
                adapter.setEmptyView(noData);
            }
        };
        setLoding(this, false);
        getListTask.execute();
    }


    private void findlist() {
        BizDataAsyncTask<List<MedicineListModel>> findListTask = new BizDataAsyncTask<List<MedicineListModel>>() {
            @Override
            protected List<MedicineListModel> doExecute() throws ZYException, BizFailure {
                Log.e(AppConfig.ERR_TAG, classId + "/" + orderby + pageIndex);
                return CategoryBiz.findMedicineList(classId, orderby, pageIndex + "", "" + AppConfig.DEFAULT_PAGE_COUNT);
            }

            @Override
            protected void onExecuteSucceeded(List<MedicineListModel> medicineListModel) {
                Log.e(AppConfig.ERR_TAG, "find data success");
                closeLoding();
                Log.e(AppConfig.ERR_TAG, GsonUtils.Bean2Json(medicineListModel));
                if (medicineListModel == null || medicineListModel.size() <= 0) {
                    end = true;
                    adapter.setEmptyView(noData);
                }
                adapter.addData(medicineListModel);
                adapter.notifyDataSetChanged();
            }

            @Override
            protected void OnExecuteFailed() {
                adapter.setEmptyView(noData);
                closeLoding();
            }
        };
        setLoding(this, false);
        findListTask.execute();
    }

    private View getFooterView() {
        View view = getLayoutInflater().inflate(R.layout.main_end_view, (ViewGroup) recyclerView.getParent(), false);
        return view;
    }


    private class MyAdapter extends BaseQuickAdapter<MedicineListModel> {

        public MyAdapter() {
            super(R.layout.item_goods_grid, list);
        }


        @Override
        protected void convert(BaseViewHolder baseViewHolder, MedicineListModel medicineListModel) {
            baseViewHolder.setText(R.id.goods_title, StringUtils.deletaFirst(medicineListModel.getWARENAME()))
                    .setText(R.id.money, medicineListModel.getSALEPRICE());

            SimpleDraweeView goods_img = baseViewHolder.getView(R.id.item_head_img);
            if (StringUtils.isEmpty(medicineListModel.getIMG_PATH())) {
                goods_img.setImageURI(Uri.parse(""));
            } else {
                goods_img.setImageURI(Uri.parse(medicineListModel.getIMG_PATH()));
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
//        isRise = true;
    }

}
