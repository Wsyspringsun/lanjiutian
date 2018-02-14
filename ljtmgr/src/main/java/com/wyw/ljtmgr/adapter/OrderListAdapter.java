package com.wyw.ljtmgr.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.springsun.appbase.adapter.DataListAdapter;
import com.wyw.ljtmgr.R;
import com.wyw.ljtmgr.config.AppConfig;
import com.wyw.ljtmgr.model.OrderInfoModel;
import com.wyw.ljtmgr.ui.OrderDetailActivity;

import utils.CommonUtil;

public class OrderListAdapter extends DataListAdapter<OrderInfoModel, RecyclerView.ViewHolder> {
    private final Activity context;
    private String mstat;

    public String getMstat() {
        return mstat;
    }

    public void setMstat(String mstat) {
        this.mstat = mstat;
    }

    public OrderListAdapter(Activity context, String stat) {
        this.context = context;
        this.mstat = stat;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_EMPTY) {
            return new EmptyViewHolder(context.getLayoutInflater().inflate(R.layout.main_empty_view, parent, false));
        }
        View v = context.getLayoutInflater().inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OrderViewHolder) {
            OrderViewHolder h = (OrderViewHolder) holder;
            OrderInfoModel data = list.get(position);
            StringBuilder sb = new StringBuilder("");
            String postage = "", dat = data.getUpdDate();
            sb.append(postage).append(dat);
//            h.tvOrder.setText(buildOrderSpannable(data));
            h.tvOrder.setText(sb.toString());
//            h.tvShuliang.setText(buildUserSpannable(data));
            h.tvShuliang.setText("数量\n" + data.getExchangeQuanlity());
            h.tvPrice.setText("￥" + CommonUtil.formatFee("" + data.getGroupPayAmount()));
            h.tvOrderId.setText("订单:" + data.getOrderGroupId());
            h.data = data;
        }


    }

    /*private CharSequence buildOrderSpannable(OrderInfoModel data) {
        String priceAll = "￥" + CommonUtil.formatFee("" + data.getGroupPayAmount()) + "\n",
                postage = "",
                dat = data.getUpdDate();
        String[] strOrders = new String[]{priceAll, postage, dat};
        int[] colorArr = {R.color.font_1, R.color.font_1, R.color.font_2};
        int[] sizeArr = {R.dimen.font_3, R.dimen.font_4, R.dimen.font_4};
        StringBuilder sb = new StringBuilder("");
        sb.append(priceAll).append(postage).append(dat);
        SpannableString ssOrder = new SpannableString(sb.toString());
        int start = 0;
        for (int i = 0; i < strOrders.length; i++) {
            ssOrder.setSpan(new AbsoluteSizeSpan((int) context.getResources().getDimension(sizeArr[i])), start, start + strOrders[i].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssOrder.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(context, colorArr[i])), start, start + strOrders[i].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            start += strOrders[i].length();
        }
        return ssOrder;
    }*/

    /*private CharSequence buildUserSpannable(OrderInfoModel data) {
        String[] strUsers = new String[]{"", " ", "订单:" + data.getOrderGroupId() + "\n", "数量\n", data.getExchangeQuanlity() + ""};
        int[] color4UserArr = {R.color.font_3, R.color.font_3, R.color.font_1, R.color.font_1, R.color.font_4, R.color.font_1};
        int[] size4UserArr = {R.dimen.font_4, R.dimen.font_3, R.dimen.font_4, R.dimen.font_4, R.dimen.font_4, R.dimen.font_2};
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < strUsers.length; i++) {
            sb.append(strUsers[i]);
        }
        SpannableString ssUser = new SpannableString(sb.toString());
        int start = 0;
        for (int i = 0; i < strUsers.length; i++) {
            ssUser.setSpan(new AbsoluteSizeSpan((int) context.getResources().getDimension(size4UserArr[i])), start, start + strUsers[i].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssUser.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(context, color4UserArr[i])), start, start + strUsers[i].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            start += strUsers[i].length();
        }
        return ssUser;
    }*/

    private class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvOrder;
        private final TextView tvShuliang;
        private final TextView tvOrderId;
        private final TextView tvPrice;
        public OrderInfoModel data;

        public OrderViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvOrder = (TextView) itemView.findViewById(R.id.item_order_orderinfo);
            tvShuliang = (TextView) itemView.findViewById(R.id.item_order_shuliang);
            tvOrderId = (TextView) itemView.findViewById(R.id.item_order_orderid);
            tvPrice = (TextView) itemView.findViewById(R.id.item_order_orderprice);
        }

        @Override
        public void onClick(View v) {
            Log.e(AppConfig.TAG_ERR, "adapter stat:" + mstat);
            Intent it = OrderDetailActivity.getIntent(context, mstat, data.getOrderGroupId());
            context.startActivity(it);
        }
    }
}

