package com.wyw.ljtds.ui.user.wallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.PointRecord;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by wsy on 17-7-28.
 */

@ContentView(R.layout.fragment_order_list)
public class PointRecordListFragment extends BaseFragment {
    @ViewInject(R.id.reclcyer)
    private RecyclerView recyclerView;
    private boolean end = false;

    private List<PointRecord> list;
    private MyAdapter1 adapter1;

    private int page = 1;
    private LinearLayoutManager linearLayoutManager;

    /**
     * @param cat 余额类型
     * @return
     */
    public static PointRecordListFragment newInstance() {
        PointRecordListFragment fragment = new PointRecordListFragment();
        Bundle args = new Bundle();
//        args.putInt(ARG_CAT, resId);
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
        new BizDataAsyncTask<List<PointRecord>>() {
            @Override
            protected List<PointRecord> doExecute() throws ZYException, BizFailure {
//                return UserBiz.readBalance(method, page + "",  "3");
//                return UserBiz.readBalance(method, page + "", AppConfig.DEFAULT_PAGE_COUNT + "");
                return UserBiz.readPointRecord(page + "", AppConfig.DEFAULT_PAGE_COUNT + "");
            }

            @Override
            protected void onExecuteSucceeded(List<PointRecord> balanceRecords) {
                closeLoding();
                list = balanceRecords;

                PointRecordListFragment.this.updAdapter();
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


    private class MyAdapter1 extends BaseQuickAdapter<PointRecord> {
        public MyAdapter1() {
            super(R.layout.item_point_list, list);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, PointRecord s) {
            baseViewHolder.setText(R.id.point_item_notes, s.getNOTES())
                    .setText(R.id.point_item_integrala, Utils.formatFee(s.getINTEGRALA()))
                    .setText(R.id.point_item_integral, Utils.formatFee(s.getINTEGRAL()))
                    .setText(R.id.point_item_execdate, s.getEXECDATE());
        }
    }
}
