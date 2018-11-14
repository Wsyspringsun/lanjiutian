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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
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
import com.wyw.ljtds.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * Created by wsy on 17-7-28.
 */

@ContentView(R.layout.fragment_order_list)
public class DaiJinQuanListFragment extends BaseFragment {
    @ViewInject(R.id.reclcyer)
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter1;
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
                return UserBiz.readTicket("2,5,6");
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
        if (list == null) return;
        if (adapter1 == null) {
            adapter1 = new RecyclerView.Adapter<TicketItemHolder>() {
                @Override
                public TicketItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = getActivity().getLayoutInflater().inflate(R.layout.item_daijinquan, parent, false);
                    return new TicketItemHolder(view);
                }

                @Override
                public void onBindViewHolder(TicketItemHolder holder, int position) {
                    Ticket itemData = list.get(position);
                    holder.itemData = itemData;
                    holder.tvAmount.setText("￥" + Utils.formatFee("" + itemData.getSINGLE_AMOUNT()));
                    holder.tvDesc.setText("满" + Utils.formatFee(itemData.getMINIMUM_TENDER_AMOUNT()) + "可用");
                    holder.tvName.setText(itemData.getRED_PACKET_TEMPLET_NAME());
                    holder.tvRule.setText(itemData.getREMARK());
                    holder.tvTerm.setText("有限日期：" + itemData.getTERM());
//                    Picasso.with(PointShopActivity.this).load(AppConfig.IMAGE_PATH_LJT + itemData.getIMG_PATH()).into(holder.ivItem);
                }

                @Override
                public int getItemCount() {
                    if (list == null)
                        return 0;
                    return list.size();
                }
            };

            recyclerView.setAdapter(adapter1);
        }
    }

    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(getActivity());//必须有
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置水平方向滑动
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private class TicketItemHolder extends RecyclerView.ViewHolder {
        ImageView ivCode;
        TextView tvAmount;
        TextView tvDesc;
        TextView tvShowBarcode;
        TextView tvName;
        TextView tvTerm;
        TextView tvRule;

        Ticket itemData;

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.item_daijinquan_btn_barcode:
                        if (ivCode.getDrawable() == null) {
                            Bitmap bitmap = QRCodeEncoder.syncEncodeBarcode(itemData.getRED_PACKET_LOG_ID(), 400, 120, 0);
                            ivCode.setImageBitmap(bitmap);
                        }
                        if (ivCode.getVisibility() == View.VISIBLE) {
                            ivCode.setVisibility(View.GONE);
                        } else {
                            ivCode.setVisibility(View.VISIBLE);
                        }
                        break;
                }

            }
        };

        public TicketItemHolder(View itemView) {
            super(itemView);
            ivCode = (ImageView) itemView.findViewById(R.id.item_daijinquan_qrcode);


            tvAmount = (TextView) itemView.findViewById(R.id.item_daijinquan_amount);
            tvDesc = (TextView) itemView.findViewById(R.id.item_daijinquan_desc);
            tvName = (TextView) itemView.findViewById(R.id.item_daijinquan_name);
            tvTerm = (TextView) itemView.findViewById(R.id.item_daijinquan_term);
            tvRule = (TextView) itemView.findViewById(R.id.item_daijinquan_rule);
            tvShowBarcode = (TextView) itemView.findViewById(R.id.item_daijinquan_btn_barcode);
            tvShowBarcode.setOnClickListener(onClickListener);

        }
    }
}
