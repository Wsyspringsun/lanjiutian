package com.wyw.ljtds.ui.user.wallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.DianZiBiLog;
import com.wyw.ljtds.model.Ticket;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.utils.GsonUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by wsy on 17-7-28.
 */

@ContentView(R.layout.fragment_order_list)
public class DaiJinQuanListFragment extends BaseFragment {
    @ViewInject(R.id.reclcyer)
    private RecyclerView recyclerView;
    private MyAdapter1 adapter1;
    private List<Ticket> list;
    private LinearLayoutManager linearLayoutManager;

    /**
     * @param cat 余额类型
     * @return
     */
    public static DaiJinQuanListFragment newInstance() {
        DaiJinQuanListFragment fragment = new DaiJinQuanListFragment();
        Bundle args = new Bundle();
//        args.putInt(ARG_CAT, resId);
        fragment.setArguments(args);
        return fragment;
    }

    private void loadDaiJinQuanList() {
        setLoding(getActivity(), false);
        new BizDataAsyncTask<List<Ticket>>() {
            @Override
            protected List<Ticket> doExecute() throws ZYException, BizFailure {
                return UserBiz.readTicket("2");
            }

            @Override
            protected void onExecuteSucceeded(List<Ticket> daijinquan) {
                list = daijinquan;
                updAdapter();
                closeLoding();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
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
        loadDaiJinQuanList();
    }

    private void updAdapter() {
        if (adapter1 == null) {
            adapter1 = new MyAdapter1();
            View noData = getActivity().getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recyclerView.getParent(), false);
            adapter1.setEmptyView(noData);
            recyclerView.setAdapter(adapter1);
        }
        if (list == null || list.size() <= 0) {
            return;
        }

        adapter1.setNewData(list);
        Log.e(AppConfig.ERR_TAG, GsonUtils.List2Json(list));

        adapter1.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(getActivity());//必须有
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置水平方向滑动
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private class MyAdapter1 extends BaseQuickAdapter<Ticket> {
        public MyAdapter1() {
            super(R.layout.item_daijinquan_list, list);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, Ticket s) {
//            baseViewHolder.setText(R.id.item_daijinquan_list_tv_cost, )
//                    .setText(R.id.item_dianzibi_electronic_orderid, s.getORDER_TRADE_ID())
//                    .setText(R.id.item_dianzibi_electronic_insdate, s.getINS_DATE());
        }
    }
}
