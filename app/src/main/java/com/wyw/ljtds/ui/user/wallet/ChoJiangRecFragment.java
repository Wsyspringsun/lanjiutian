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
import com.wyw.ljtds.model.ChoJiangRec;
import com.wyw.ljtds.model.DianZiBiLog;
import com.wyw.ljtds.model.Ticket;
import com.wyw.ljtds.ui.base.BaseActivityFragment;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.utils.DateUtils;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import static android.R.id.list;
import static android.R.id.switchInputMethod;

/**
 * Created by wsy on 17-7-28.
 */

@ContentView(R.layout.fragment_order_list)
public class ChoJiangRecFragment extends BaseFragment {
    @ViewInject(R.id.reclcyer)
    private RecyclerView recyclerView;
    ChoJiangRecAdapter adapter;

    /**
     * @param cat 余额类型
     * @return
     */
    public static ChoJiangRecFragment newInstance() {
        ChoJiangRecFragment fragment = new ChoJiangRecFragment();
        Bundle args = new Bundle();
//        args.putInt(ARG_CAT, resId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());//必须有
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置水平方向滑动
        recyclerView.setLayoutManager(linearLayoutManager);
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

    //return UserBiz.
    private void loadData() {
        setLoding(getActivity(), false);
        new BizDataAsyncTask<List<ChoJiangRec>>() {

            @Override
            protected List<ChoJiangRec> doExecute() throws ZYException, BizFailure {
                closeLoding();
                return UserBiz.chojiangData();
            }

            @Override
            protected void onExecuteSucceeded(List<ChoJiangRec> choJiangRecs) {
                closeLoding();
                updAdapter(choJiangRecs);
            }


            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    private void updAdapter(List<ChoJiangRec> list) {
        if (adapter == null) {
            adapter = new ChoJiangRecAdapter(list);
            View noData = getActivity().getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recyclerView.getParent(), false);
            adapter.setEmptyView(noData);
//            View header = getActivity().getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recyclerView.getParent(), false);
//            adapter.addHeaderView(header);
            recyclerView.setAdapter(adapter);
        }
        if (list == null || list.size() <= 0) {
            return;
        }

        adapter.setNewData(list);
        adapter.notifyDataSetChanged();
    }

    private class ChoJiangRecAdapter extends BaseQuickAdapter<ChoJiangRec> {
        public ChoJiangRecAdapter(List<ChoJiangRec> list) {
            super(R.layout.item_chojiangrecord_list, list);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, ChoJiangRec choJiangRec) {
            baseViewHolder.setText(R.id.item_chojiang_msg, choJiangRec.getAWARD_NAME());
            String offdate = "抽奖时间：" + choJiangRec.getUPD_DATE() + "";
            if (!StringUtils.isEmpty(offdate)) {
                baseViewHolder.setText(R.id.item_chojiang_date, offdate);
            }

            String addr = "联系方式：";

            if (ChoJiangRec.AWARD_TYPE_SHIWU.equals(choJiangRec.getAWARD_TYPE())) {
                if (!StringUtils.isEmpty(choJiangRec.getBUSNAME())) {
                    addr += choJiangRec.getBUSNAME() + " ";
                }

            }
            if (!StringUtils.isEmpty(choJiangRec.getORG_MOBILE())) {
                addr += choJiangRec.getORG_MOBILE();
            }

            baseViewHolder.setText(R.id.item_chojiang_addr, addr);
            if ("0".equals(choJiangRec.getVALID_FLG())) {
                baseViewHolder.setImageDrawable(R.id.item_chojiang_stat, getResources().getDrawable(R.drawable.jiang_wait));
            } else if ("1".equals(choJiangRec.getVALID_FLG())) {
                baseViewHolder.setImageDrawable(R.id.item_chojiang_stat, getResources().getDrawable(R.drawable.jiang_gain));
            } else {
                baseViewHolder.setImageDrawable(R.id.item_chojiang_stat, getResources().getDrawable(R.drawable.jiang_disable));
            }

        }
    }

}
