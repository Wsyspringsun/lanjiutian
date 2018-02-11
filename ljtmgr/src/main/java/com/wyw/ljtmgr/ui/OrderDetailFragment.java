package com.wyw.ljtwl.ui;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager.LayoutParams;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.wyw.ljtwl.R;
import com.wyw.ljtwl.biz.OrderBiz;
import com.wyw.ljtwl.biz.SimpleCommonCallback;
import com.wyw.ljtwl.config.AppConfig;
import com.wyw.ljtwl.config.MyApplication;
import com.wyw.ljtwl.config.SingleCurrentUser;
import com.wyw.ljtwl.model.LogisticInfo;
import com.wyw.ljtwl.model.OrderDetail;
import com.wyw.ljtwl.model.OrderDetailModel;
import com.wyw.ljtwl.model.OrderStatus;
import com.wyw.ljtwl.model.ServerResponse;
import com.wyw.ljtwl.utils.StringUtils;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.LinkedList;
import java.util.List;

import cn.bingoogolapple.photopicker.imageloader.BGAPicassoImageLoader;
import utils.CommonUtil;


/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_order_detail)
public class OrderDetailFragment extends Fragment {
    private static final String ARG_ORDER_ID = "ARG_ORDER_ID";
    private static final int REQUEST_DELEGATE_LOGISTIC = 1;

    MyApplication myApp = null;

    @ViewInject(R.id.fragment_order_detail_ryv)
    RecyclerView ryvOrder;
    @ViewInject(R.id.fragment_order_detail_btn_submit)
    Button btnSubmit;
    @ViewInject(R.id.fragment_order_detail_ll_logistic)
    LinearLayout llLogistic;

    public OrderDetailModel orderDetailModel;
    DelegateAdapter delegateAdapter;
    private int logisticStat;
    private SimpleCommonCallback sendOrderCallback;

    public static OrderDetailFragment newInstance(String orderId) {
        OrderDetailFragment frag = new OrderDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ORDER_ID, orderId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = x.view().inject(this, inflater, container);

        myApp = (MyApplication) (getActivity().getApplication());

        initView(v);

        sendOrderCallback = new SimpleCommonCallback<ServerResponse>(getActivity()) {
            @Override
            protected void handleResult(ServerResponse result) {
                Toast.makeText(getActivity(), "成功", Toast.LENGTH_LONG);
                sendOK();
            }
        };

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        loadData();
    }

    private void loadData() {
        String orderId = getArguments().getString(ARG_ORDER_ID);
        Log.e(AppConfig.TAG_ERR, "...orderId:" + orderId);
        OrderBiz.loadOrderDetail(orderId, new SimpleCommonCallback<OrderDetailModel>(getActivity()) {

            @Override
            protected void handleResult(OrderDetailModel result) {
                Gson g = new Gson();
                String orderId = getArguments().getString(ARG_ORDER_ID);
                result.setOrderId(orderId);
                orderDetailModel = result;
                bindData2View();
            }
        });
    }

    private void bindData2View() {
        if (orderDetailModel == null) return;

        int distance = 0;
        if (orderDetailModel.getDistance() != null) {
            try {
                distance = Integer.parseInt(orderDetailModel.getDistance());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (distance > 10) {
            this.logisticStat = OrderStatus.DELEGATE;
        } else {
            this.logisticStat = OrderStatus.LJT;
        }
        if (!OrderStatus.TOSHIPPED.equals(orderDetailModel.getGroupStatus())) {
            //没有分配 快递元
            btnSubmit.setVisibility(View.GONE);
            llLogistic.setVisibility(View.VISIBLE);
        } else {
            //有分配 快递元
            btnSubmit.setVisibility(View.VISIBLE);
            llLogistic.setVisibility(View.GONE);
        }
        btnSubmit.setTag(this.logisticStat);
        if (OrderStatus.DELEGATE == this.logisticStat) {
            btnSubmit.setText(getString(R.string.logistic_delegate));
        } else {
            btnSubmit.setText(getString(R.string.logistic_ljt));
        }
        final VirtualLayoutManager layoutManager = new VirtualLayoutManager(getActivity());
        ryvOrder.setLayoutManager(layoutManager);
        final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        ryvOrder.setRecycledViewPool(viewPool);
        delegateAdapter = new DelegateAdapter(layoutManager, true);
        ryvOrder.setAdapter(delegateAdapter);

        List<DelegateAdapter.Adapter> adapters = new LinkedList<>();


        adapters.add(new HeadSubAdapter(getActivity(), new LinearLayoutHelper(), 1, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300)));
        adapters.add(new OrderAdapter(getActivity(), new LinearLayoutHelper(), orderDetailModel.getDetailList() == null ? 0 : orderDetailModel.getDetailList().size(), new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300)));
        adapters.add(new FootSubAdapter(getActivity(), new LinearLayoutHelper(), 5, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300)));
        delegateAdapter.setAdapters(adapters);

        final Handler mainHandler = new Handler(Looper.getMainLooper());

        Runnable trigger = new Runnable() {
            @Override
            public void run() {
                ryvOrder.requestLayout();
            }
        };
        mainHandler.postDelayed(trigger, 1000);
    }

    private void initView(View v) {


        llLogistic.setVisibility(View.GONE);

        btnSubmit.setVisibility(View.GONE);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag().equals(OrderStatus.DELEGATE)) {
                    //第三方

                    startActivityForResult(LogisticInfoActivity.getIntent(getActivity(), orderDetailModel.getOrderId()), REQUEST_DELEGATE_LOGISTIC);
                } else {

                    //蓝九天
                    LogisticInfo logisticInfo = new LogisticInfo();
                    logisticInfo.setOrderGroupId(orderDetailModel.getOrderId());
                    logisticInfo.setLogisticsFlg("0");
                    logisticInfo.setCourier(myApp.getCurrentLoginer().getAdminUserName());
                    logisticInfo.setCourierMobile(myApp.getCurrentLoginer().getAdminUserId());
                    //当前设备
                    OrderBiz.sendOrder(logisticInfo, sendOrderCallback);
                }

            }
        });
    }

    private void sendOK() {
        loadData();
    }

    class HeadSubAdapter extends DelegateAdapter.Adapter<TitleTextViewHolder> {
        private final LayoutHelper mLayoutHelper;
        private final int mCount;
        private final LayoutParams mLayoutParams;
        private Context mContext;

        public HeadSubAdapter(Context context, LayoutHelper layoutHelper, int count, @NonNull LayoutParams layoutParams) {
            this.mContext = context;
            this.mLayoutHelper = layoutHelper;
            this.mCount = count;
            this.mLayoutParams = layoutParams;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public TitleTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.textview_title, parent, false);
            return new TitleTextViewHolder(v);
        }

        @Override
        public void onBindViewHolder(TitleTextViewHolder holder, int position) {
            Log.e(AppConfig.TAG_ERR, "OneColSubAdapter onBindViewHolder:" + position);
            StringBuilder sb = new StringBuilder("");
            sb.append("订单编号:" + OrderDetailFragment.this.orderDetailModel.getOrderId() + "\n");
            sb.append("订单生成时间:" + utils.DateUtils.parseTime(orderDetailModel.getInsDate()) + "\n");
            sb.append("订单完成时间:" + utils.DateUtils.parseTime(orderDetailModel.getUpdDate()) + "\n");
            sb.append("订单状态:" + OrderStatus.getStatus(orderDetailModel.getGroupStatus()));
            holder.tvTitle.setText(sb.toString());
            holder.tvContent.setText("");
        }

        @Override
        public int getItemCount() {
            return mCount;
        }
    }

    class FootSubAdapter extends DelegateAdapter.Adapter<TitleTextViewHolder> {
        private final LayoutHelper mLayoutHelper;
        private final int mCount;
        private final LayoutParams mLayoutParams;
        private Context mContext;

        public FootSubAdapter(Context context, LayoutHelper layoutHelper, int count, @NonNull LayoutParams layoutParams) {
            this.mContext = context;
            this.mLayoutHelper = layoutHelper;
            this.mCount = count;
            this.mLayoutParams = layoutParams;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public TitleTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.textview_title, parent, false);
            return new TitleTextViewHolder(v);
        }

        @Override
        public void onBindViewHolder(TitleTextViewHolder holder, int position) {
            switch (position) {
                case 0:
                    holder.tvTitle.setText("店铺优惠");
                    holder.tvContent.setText("优惠" + orderDetailModel.getDiscount() + "元");
                    break;
                case 1:
                    holder.tvTitle.setText("实付款: ￥" + orderDetailModel.getPayAmount() + " 运费: ￥" + orderDetailModel.getPostageMoneyAll());
                    holder.tvContent.setText("");
                    break;
                case 2:
                    holder.tvTitle.setText("发票信息: " + orderDetailModel.getInvoiceTitle() + " " + orderDetailModel.getInvoiceTax());
                    holder.tvContent.setText("");

                    break;
                case 3:
                    holder.tvTitle.setText("地址：" + orderDetailModel.getReceiverAddress() + "\n" + orderDetailModel.getReceiverName() + " " + orderDetailModel.getReceiverPhone());
                    holder.tvContent.setText(">");
                    holder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent it = LogisticMapActivity.getIntent(getActivity(), "35.504755", "112.839483", orderDetailModel.getLat(), orderDetailModel.getLng());
                            startActivity(it);
                        }
                    });
                    break;
                case 4:
                    holder.tvTitle.setText("备注：" + orderDetailModel.getRemark() + "");
                    holder.tvContent.setText("");
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return mCount;
        }
    }

    class TitleTextViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        TextView tvTitle;
        TextView tvContent;

        public TitleTextViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            tvTitle = (TextView) itemView.findViewById(R.id.textview_title);
            tvContent = (TextView) itemView.findViewById(R.id.textview_content);
        }
    }


    class OrderAdapter extends DelegateAdapter.Adapter<OrderViewHolder> {
        private final LayoutHelper mLayoutHelper;
        private final int mCount;
        private final LayoutParams mLayoutParams;
        private Context mContext;

        public OrderAdapter(Context context, LayoutHelper layoutHelper, int count, @NonNull LayoutParams layoutParams) {
            this.mContext = context;
            this.mLayoutHelper = layoutHelper;
            this.mCount = count;
            this.mLayoutParams = layoutParams;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_oder_goods, parent, false);
            return new OrderViewHolder(v);
        }

        @Override
        public void onBindViewHolder(OrderViewHolder holder, int position) {
            List<OrderDetail> list = orderDetailModel.getDetailList();
            if (list == null || list.size() <= 0) return;
            OrderDetail item = list.get(position);
            holder.tvNum.setText("x" + item.getExchangeQuanlity());
            holder.tvInfo.setText(StringUtils.deletaFirst(item.getTitle()) + "\n\n" + item.getCommoditySize() + "\n" + (item.getCostMoneyAll() + ""));
            Picasso.with(getActivity()).load(Uri.parse(AppConfig.IMAGE_PATH_LJT + item.getImgPath())).into(holder.imv);
        }

        @Override
        public int getItemCount() {
            return mCount;
        }
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imv;
        private final TextView tvInfo;
        private final TextView tvNum;

        public OrderViewHolder(View itemView) {
            super(itemView);
            imv = (ImageView) itemView.findViewById(R.id.item_order_goods_img);
            tvInfo = (TextView) itemView.findViewById(R.id.item_order_goods_info);
            tvNum = (TextView) itemView.findViewById(R.id.item_order_goods_num);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_DELEGATE_LOGISTIC:
                String delegateName = data.getStringExtra(LogisticInfoActivity.TAG_NAME);
                String delegateCode = data.getStringExtra(LogisticInfoActivity.TAG_CODE);
                //蓝九天
                LogisticInfo logisticInfo = new LogisticInfo();
                logisticInfo.setOrderGroupId(orderDetailModel.getOrderId());
                logisticInfo.setLogisticsFlg("1");
                logisticInfo.setCourier(myApp.getCurrentLoginer().getAdminUserName());
                logisticInfo.setCourierMobile(myApp.getCurrentLoginer().getAdminUserId());
                logisticInfo.setLogisticsCompanyName(delegateName);
                logisticInfo.setLogisticsNumber(delegateCode);
                //当前设备
                OrderBiz.sendOrder(logisticInfo, sendOrderCallback);
                break;
        }
    }
}
