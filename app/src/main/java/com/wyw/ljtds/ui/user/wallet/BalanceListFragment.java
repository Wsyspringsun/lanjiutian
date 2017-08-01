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
import com.wyw.ljtds.model.BalanceRecord;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.utils.Utils;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by wsy on 17-7-28.
 */

@ContentView(R.layout.fragment_order_list)
public class BalanceListFragment extends BaseFragment {
    private static final String METHOD_IN = "0";
    private static final String METHOD_OUT = "1";

    private static final String ARG_CAT = "com.wyw.ljtds.ui.user.wallet.BalanceListFragment.ARG_CAT";
    private String cat_val = "";
    @ViewInject(R.id.reclcyer)
    private RecyclerView recyclerView;
    private boolean end = false;

    private List<BalanceRecord> list;
    private MyAdapter1 adapter1;

    public String method;
    private int page = 1;
    private LinearLayoutManager linearLayoutManager;

    /**
     * @param cat 余额类型
     * @return
     */
    public static BalanceListFragment newInstance(int resId) {
        BalanceListFragment fragment = new BalanceListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CAT, resId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.page = 1;
        String val_cat_in = getResources().getString(R.string.balance_in);
//        String cat_out = getResources().getString(R.string.balance_out);
        cat_val = getResources().getString(getArguments().getInt(ARG_CAT));
        if (val_cat_in.equals(cat_val)) {
            this.method = METHOD_IN;
        } else {
            this.method = METHOD_OUT;
        }
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
        new BizDataAsyncTask<List<BalanceRecord>>() {
            @Override
            protected List<BalanceRecord> doExecute() throws ZYException, BizFailure {
//                return UserBiz.readBalance(method, page + "",  "3");
                return UserBiz.readBalance(method, page + "", AppConfig.DEFAULT_PAGE_COUNT + "");
            }

            @Override
            protected void onExecuteSucceeded(List<BalanceRecord> balanceRecords) {
                closeLoding();
                list = balanceRecords;

                BalanceListFragment.this.updAdapter();
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


    private class MyAdapter1 extends BaseQuickAdapter<BalanceRecord> {
        public MyAdapter1() {
            super(R.layout.item_balance_list, list);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, BalanceRecord s) {
            if (METHOD_IN.equals(BalanceListFragment.this.method)) {
                baseViewHolder.setText(R.id.balance_item_addmoney_notes, s.getADDMONEY_NOTES())
                        .setText(R.id.balance_item_addamount, Utils.formatFee(s.getADDAMOUNT()))
                        .setText(R.id.balance_item_addmoney_cardbalance, Utils.formatFee(s.getADDMONEY_CARDBALANCE()))
                        .setText(R.id.balance_item_cardinvalidate, s.getCARDINVALIDATE());
            } else {
                baseViewHolder.setText(R.id.balance_item_addmoney_notes, s.getSALE_NOTES())
                        .setText(R.id.balance_item_addamount, Utils.formatFee(s.getSALEAMOUNT()))
                        .setText(R.id.balance_item_addmoney_cardbalance, Utils.formatFee(s.getSALE_CARDBALANCE()))
                        .setText(R.id.balance_item_cardinvalidate, s.getSALE_CREATETIME());
            }

        }
    }
}
