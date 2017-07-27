package com.wyw.ljtds.ui.category;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.category.MyListAdapter;
import com.wyw.ljtds.adapter.category.Recycler_rightAdapter;
import com.wyw.ljtds.biz.biz.CategoryBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.model.CommodityTypeFirstModel;
import com.wyw.ljtds.model.CommodityTypeSecondModel;
import com.wyw.ljtds.ui.base.BaseFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/29 0029.
 */

@ContentView(R.layout.fragment_category_item)
public class FragmentLife extends BaseFragment {
    @ViewInject(R.id.left_lv)
    private ListView mListView;
    @ViewInject(R.id.right_recycler)
    private RecyclerView mRight_recycler;

    private MyListAdapter mAdapter;
    private Recycler_rightAdapter recycler_rightAdapter;
    private List<CommodityTypeFirstModel> data;//一级菜单所需

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mAdapter = new MyListAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                mAdapter.setCurPositon(position);
                mAdapter.notifyDataSetChanged();

                getCommodity();
                mListView.smoothScrollToPositionFromTop(position, (parent.getHeight() - view.getHeight()) / 2);

            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mAdapter.isEmpty())
                getType();
        } else {


        }
    }

    BizDataAsyncTask<List<CommodityTypeFirstModel>> getTypeTask;

    private void getType() {
        getTypeTask = new BizDataAsyncTask<List<CommodityTypeFirstModel>>() {
            @Override
            protected List<CommodityTypeFirstModel> doExecute() throws ZYException, BizFailure {
                return CategoryBiz.getCommodityType();
            }

            @Override
            protected void onExecuteSucceeded(List<CommodityTypeFirstModel> commodityTypeFirstModels) {
                data = new ArrayList<>();
                data = commodityTypeFirstModels;
                mAdapter.addItems(data);
                mAdapter.notifyDataSetChanged();

                getCommodity();
            }

            @Override
            protected void OnExecuteFailed() {

            }
        };
        getTypeTask.execute();
    }


    BizDataAsyncTask<List<CommodityTypeSecondModel>> getCommidityTask;

    private void getCommodity() {
        getCommidityTask = new BizDataAsyncTask<List<CommodityTypeSecondModel>>() {
            @Override
            protected List<CommodityTypeSecondModel> doExecute() throws ZYException, BizFailure {
                if (data == null) return null;
                return CategoryBiz.getCommodityTypeList(data.get(mAdapter.getCurPositon()).getCommodityTypeId());
            }

            @Override
            protected void onExecuteSucceeded(List<CommodityTypeSecondModel> commodityTypeSecondModels) {
                recycler_rightAdapter = new Recycler_rightAdapter(getActivity(), commodityTypeSecondModels);
                mRight_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRight_recycler.setAdapter(recycler_rightAdapter);
                recycler_rightAdapter.notifyDataSetChanged();

                closeLoding();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        };
        setLoding(getActivity(), false);
        getCommidityTask.execute();
    }
}
