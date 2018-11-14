package com.wyw.ljtds.ui.user.wallet;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.wyw.ljtds.utils.StringUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * Created by wsy on 17-7-28.
 */

@ContentView(R.layout.fragment_list_dianzibi)
public class DianZiBiListFragment extends BaseFragment {
    @ViewInject(R.id.fragment_list_dianzibi_ryv_data)
    private RecyclerView recyclerView;
    @ViewInject(R.id.dianzibi_tv_total)
    private TextView dianzibiTvTotal;
    @ViewInject(R.id.dianzibi_iv_barcode)
    private ImageView dianzibiIvBarcode;

    private boolean end = false;

    private List<DianZiBiLog> list;
    private MyAdapter1 adapter1;

    private int page = 1;
    private LinearLayoutManager linearLayoutManager;

    /**
     * @param cat 余额类型
     * @return
     */
    public static DianZiBiListFragment newInstance() {
        DianZiBiListFragment fragment = new DianZiBiListFragment();
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

    private void loadDianzibiTotal() {
        setLoding(getActivity(), false);
        new BizDataAsyncTask<List<Ticket>>() {
            @Override
            protected List<Ticket> doExecute() throws ZYException, BizFailure {
                return UserBiz.readTicket("0");
            }

            @Override
            protected void onExecuteSucceeded(List<Ticket> dianZiBiLogs) {
                closeLoding();
                if (dianZiBiLogs != null && dianZiBiLogs.size() > 0) {
                    dianzibiTvTotal.setText("￥" + dianZiBiLogs.get(0).getUSED_AMOUNT());

                    Log.e(AppConfig.ERR_TAG, "getRED_PACKET_ID:" + dianZiBiLogs.get(0).getRED_PACKET_ID());
                    String barcode = dianZiBiLogs.get(0).getRED_PACKET_LOG_ID();
                    if (!StringUtils.isEmpty(barcode)) {
                        barcode = barcode.trim();
                        Bitmap bitmap = QRCodeEncoder.syncEncodeBarcode(barcode, 300, 60, 0);
                        dianzibiIvBarcode.setImageBitmap(bitmap);
                    }
                }
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

//
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
        loadDianzibiTotal();

        loadDianzibiLog();
    }


    private void loadDianzibiLog() {
        setLoding(getActivity(), false);
        new BizDataAsyncTask<List<DianZiBiLog>>() {
            @Override
            protected List<DianZiBiLog> doExecute() throws ZYException, BizFailure {
                return UserBiz.readDianZiBiLog();
            }

            @Override
            protected void onExecuteSucceeded(List<DianZiBiLog> dianZiBiLogs) {
                closeLoding();
                list = dianZiBiLogs;
                updAdapter();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    private void updAdapter() {
        if (!isAdded()) return;
        if (adapter1 == null) {
            adapter1 = new MyAdapter1();
            View noData = getActivity().getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recyclerView.getParent(), false);
            adapter1.setEmptyView(noData);
//            View header = getActivity().getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recyclerView.getParent(), false);
//            adapter1.addHeaderView(header);
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
        Log.e(AppConfig.ERR_TAG, GsonUtils.List2Json(list));

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
                    loadDianzibiLog();
                }
            }
        });
    }

    private class MyAdapter1 extends BaseQuickAdapter<DianZiBiLog> {
        public MyAdapter1() {
            super(R.layout.item_dianzibi_list, list);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, DianZiBiLog s) {
            baseViewHolder.setText(R.id.item_dianzibi_electronic_money, "-" + s.getELECTRONIC_MONEY())
                    .setText(R.id.item_dianzibi_electronic_orderid, s.getORDER_TRADE_ID())
                    .setText(R.id.item_dianzibi_electronic_insdate, s.getINS_DATE());
        }
    }
}
