package com.wyw.ljtmgr.ui;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager.LayoutParams;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.wyw.ljtmgr.R;
import com.wyw.ljtmgr.biz.OrderBiz;
import com.wyw.ljtmgr.biz.SimpleCommonCallback;
import com.wyw.ljtmgr.config.AppConfig;
import com.wyw.ljtmgr.config.MyApplication;
import com.wyw.ljtmgr.model.Header;
import com.wyw.ljtmgr.model.LoginModel;
import com.wyw.ljtmgr.model.LogisticInfo;
import com.wyw.ljtmgr.model.OrderDetail;
import com.wyw.ljtmgr.model.OrderDetailModel;
import com.wyw.ljtmgr.model.OrderStat;
import com.wyw.ljtmgr.model.OrderStatus;
import com.wyw.ljtmgr.model.ReturnGoodsInfo;
import com.wyw.ljtmgr.model.ServerResponse;
import com.wyw.ljtmgr.utils.StringUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.LinkedList;
import java.util.List;

import cn.lemon.multi.MultiView;
import utils.CommonUtil;


/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_order_detail)
public class OrderDetailFragment extends Fragment {
    private static final String ARG_ORDER_ID = "ARG_ORDER_ID";
    private static final String ARG_ORDER_STAT = "ARG_ORDER_STAT";
    private static final int REQUEST_DELEGATE_LOGISTIC = 1;
    private static final int REQUEST_REJECTRETURN_MSG = 2;
    private String DIALOG_REJECTRETURN = "DIALOG_REJECTRETURN";
    private String orderStat = "";

    MyApplication myApp = null;

    @ViewInject(R.id.fragment_order_detail_ryv)
    RecyclerView ryvOrder;
    @ViewInject(R.id.fragment_order_detail_btn_delegate)
    Button btnDelegate;
    @ViewInject(R.id.fragment_order_detail_btn_submit)
    Button btnSubmit;
    @ViewInject(R.id.fragment_order_detail_btn_songda)
    Button btnSongda;
    @ViewInject(R.id.fragment_order_detail_btn_jushou)
    Button btnJuShou;
    @ViewInject(R.id.fragment_order_detail_btn_shouhoutongyi)
    Button btnShouhouTongyi;
    @ViewInject(R.id.fragment_order_detail_btn_shouhoujujue)
    Button btnShouhouJujue;
    @ViewInject(R.id.fragment_order_detail_ll_logistic)
    LinearLayout llLogistic;
    @ViewInject(R.id.fragment_order_detail_tv_logistic_info)
    TextView tvLogiInfo;
    @ViewInject(R.id.fragment_order_detail_tv_logistic_name)
    TextView tvLogiUsername;


    public OrderDetailModel orderDetailModel;
    DelegateAdapter delegateAdapter;
    private int logisticStat;
    private SimpleCommonCallback sendOrderCallback;

    public static OrderDetailFragment newInstance(String orderId, String stat) {
        OrderDetailFragment frag = new OrderDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ORDER_ID, orderId);
        args.putString(ARG_ORDER_STAT, stat);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = x.view().inject(this, inflater, container);

        myApp = (MyApplication) (getActivity().getApplication());

        initData();

        initView(v);

        //发货处理
        sendOrderCallback = new SimpleCommonCallback<ServerResponse>(getActivity()) {
            @Override
            protected void handleResult(ServerResponse result) {
                Toast.makeText(getActivity(), "成功", Toast.LENGTH_LONG).show();
                sendOK();
            }
        };

        return v;
    }

    private void initData() {
        orderStat = getArguments().getString(ARG_ORDER_STAT);
    }

    @Override
    public void onStart() {
        super.onStart();

        loadData();
    }

    private void loadData() {
        String orderId = getArguments().getString(ARG_ORDER_ID);
        ((BaseActivity) getActivity()).setLoding();
        OrderBiz.loadOrderDetail(orderId, orderStat, new SimpleCommonCallback<OrderDetailModel>(getActivity()) {

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

        Double distance = 0d;
        if (orderDetailModel.getDistance() != null) {
            try {
                distance = Double.parseDouble(orderDetailModel.getDistance());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (!StringUtils.isEmpty(orderDetailModel.getDistributionDate())) {
            tvLogiInfo.setText("预计" + orderDetailModel.getDistributionDate() + "送达");
        } else {
            tvLogiInfo.setText("难以预计送达时间");
        }
        if (!StringUtils.isEmpty(orderDetailModel.getCourier())) {
            tvLogiUsername.setText("派送员：" + orderDetailModel.getCourier());
        } else {
            tvLogiUsername.setText("派送员：未知 ");
        }


//        A”:新订单“B”:进行中“C”:已取消 “D”:售后 “E”：已完成
//        控制按钮显示状态
        resetBtn();

        String stat = orderDetailModel.getGroupStatus();
        switch (stat) {
            case OrderStatus.TOSHIPPED:
                //只有店长可以发货
                String vF = myApp.getCurrentLoginer().getValidFlg();

                Log.e(AppConfig.TAG_ERR, "dianzhang " + vF + " distance:" + distance);
                if (LoginModel.VALIDFLG_DIANZHANG.equals(vF)) {
                    btnDelegate.setVisibility(View.VISIBLE);
                    btnSubmit.setVisibility(View.VISIBLE);
                } else {
                    if (distance > 10) {
                        btnDelegate.setVisibility(View.GONE);
                        btnSubmit.setVisibility(View.GONE);
                    } else {
                        btnSubmit.setVisibility(View.VISIBLE);
                    }
                }
//                btnSubmit.setVisibility(View.VISIBLE);
                llLogistic.setVisibility(View.GONE);
                break;
            case OrderStatus.SHIPPED:
            case OrderStatus.LOGISTICSALLOCATED:
            case OrderStatus.LOGISTICSSHIPPED:
                btnSongda.setVisibility(View.VISIBLE);
                btnJuShou.setVisibility(View.VISIBLE);
//                btnSongda.setVisibility(View.VISIBLE);
                break;

            case OrderStatus.TRADECANCEL:
                btnSongda.setVisibility(View.GONE);
                btnJuShou.setVisibility(View.GONE);
//                llLogistic.setVisibility(View.GONE);
                if (!StringUtils.isEmpty(orderDetailModel.getUpdDate())) {
                    try {
                        tvLogiInfo.setText(CommonUtil.formatTime(Long.parseLong(orderDetailModel.getUpdDate())) + "已取消");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        tvLogiInfo.setText("已取消");
                    }
                } else {
                    tvLogiInfo.setText("已取消");
                }
                break;
            case OrderStatus.LOGISTICSSERVICE:
                btnSongda.setVisibility(View.GONE);
                btnJuShou.setVisibility(View.GONE);
//                llLogistic.setVisibility(View.GONE);
                if (!StringUtils.isEmpty(orderDetailModel.getUpdDate())) {
                    try {
                        tvLogiInfo.setText(CommonUtil.formatTime(Long.parseLong(orderDetailModel.getUpdDate())) + "已送达");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        tvLogiInfo.setText("已送达");
                    }
                } else {
                    tvLogiInfo.setText("已送达");
                }
                break;
            case OrderStatus.APPLYRETURNED:
                btnShouhouTongyi.setVisibility(View.VISIBLE);
                btnShouhouJujue.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
      /*  针对售后进行判断
        if (orderDetailModel.getReturnGoodsList() != null && !orderDetailModel.getReturnGoodsList().isEmpty()) {
            List<ReturnGoodsInfo> returnInfos = orderDetailModel.getReturnGoodsList();
            String retstat = returnInfos.get(0).getReturnStatus();
            if (OrderStatus.APPLYRETURNED.equals(retstat)) {
                btnShouhouTongyi.setVisibility(View.VISIBLE);
                btnShouhouJujue.setVisibility(View.VISIBLE);
            }
        } else {

        }*/


//

/*        if (OrderStatus.UNPAY.equals(orderDetailModel.getGroupStatus()) || OrderStatus.TOSHIPPED.equals(orderDetailModel.getGroupStatus())) {
            //没有分配 快递元
            btnSubmit.setVisibility(View.VISIBLE);
            llLogistic.setVisibility(View.GONE);
            btnSongda.setVisibility(View.GONE);
        } else {
            //有分配 快递元
            if (OrderStatus.LOGISTICSSHIPPED.equals(orderDetailModel.getGroupStatus())) {
                btnSongda.setVisibility(View.VISIBLE);
            } else {
                //送达
                btnSongda.setVisibility(View.GONE);
            }
            llLogistic.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.GONE);

        }*/


//        btnSubmit.setTag(this.logisticStat);
//        if (OrderStatus.DELEGATE == this.logisticStat) {
//            btnSubmit.setText(getString(R.string.logistic_delegate));
//        } else {
//            btnSubmit.setText(getString(R.string.logistic_ljt));
//        }
        final VirtualLayoutManager layoutManager = new VirtualLayoutManager(getActivity());
        ryvOrder.setLayoutManager(layoutManager);
        final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        ryvOrder.setRecycledViewPool(viewPool);
        delegateAdapter = new DelegateAdapter(layoutManager, true);
        ryvOrder.setAdapter(delegateAdapter);

        List<DelegateAdapter.Adapter> adapters = new LinkedList<>();


        adapters.add(new HeadSubAdapter(getActivity(), new LinearLayoutHelper(), 1, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300)));
        adapters.add(new OrderAdapter(getActivity(), new LinearLayoutHelper(), orderDetailModel.getDetailList() == null ? 0 : orderDetailModel.getDetailList().size(), new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300)));
        if (orderDetailModel.getReturnGoodsInfo() != null) {
            adapters.add(new ReturnGoodsSubAdapter(getActivity(), new LinearLayoutHelper(), 1, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300)));
        }
        adapters.add(new FootSubAdapter(getActivity(), new LinearLayoutHelper(), 1, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300)));

//        adapters.add(new FootSubAdapter(getActivity(), new LinearLayoutHelper(), 5, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300)));

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

    private void resetBtn() {
        btnSubmit.setVisibility(View.GONE);
        btnDelegate.setVisibility(View.GONE);
        btnSongda.setVisibility(View.GONE);
        btnJuShou.setVisibility(View.GONE);
        btnShouhouTongyi.setVisibility(View.GONE);
        btnShouhouJujue.setVisibility(View.GONE);
        llLogistic.setVisibility(View.VISIBLE);
    }

    private void initView(View v) {

        resetBtn();

        //是否同意退换货 0 同意 1 拒绝
        btnShouhouJujue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RejectReturnDialogFragment frag = RejectReturnDialogFragment.newInstance();
                frag.setTargetFragment(OrderDetailFragment.this, REQUEST_REJECTRETURN_MSG);
                frag.show(getActivity().getSupportFragmentManager(), DIALOG_REJECTRETURN);
            }
        });
        btnShouhouTongyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderBiz.orderAfterSale(orderDetailModel.getOrderId(), "0", "", new SimpleCommonCallback<ServerResponse>(getActivity()) {
                    @Override
                    protected void handleResult(ServerResponse result) {
                        Toast.makeText(getActivity(), "同意成功", Toast.LENGTH_LONG).show();
                        sendOK();
                    }
                });
            }
        });
        //发货
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //蓝九天
                LogisticInfo logisticInfo = new LogisticInfo();
                logisticInfo.setOrderGroupId(orderDetailModel.getOrderId());
                logisticInfo.setLogisticsFlg("0");
                logisticInfo.setCourier(myApp.getCurrentLoginer().getAdminUserName());
                logisticInfo.setCourierMobile(myApp.getCurrentLoginer().getAdminUserId());
                //当前设备
                OrderBiz.sendOrder(logisticInfo, sendOrderCallback);
            }
        });

        //委托第三方送货
        btnDelegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //第三方
                startActivityForResult(LogisticInfoActivity.getIntent(getActivity(), orderDetailModel.getOrderId()), REQUEST_DELEGATE_LOGISTIC);
            }
        });


        btnSongda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OrderStatus.RECEIVED.equals(orderDetailModel.getGroupStatus())) {
                    return;
                }

                if (!myApp.getCurrentLoginer().getAdminUserId().equals(orderDetailModel.getCourierMobile())) {
                    Log.e(AppConfig.TAG_ERR, "current:" + myApp.getCurrentLoginer().getAdminUserId() + "---curior:" + orderDetailModel.getCourierMobile());
                    Toast.makeText(getActivity(), "您非本单派送员，请联系本单派送员确认送达", Toast.LENGTH_LONG).show();
                    return;
                }
                OrderBiz.orderArrived(orderDetailModel, new SimpleCommonCallback<ServerResponse>(getActivity()) {
                    @Override
                    protected void handleResult(ServerResponse result) {
                        Toast.makeText(getActivity(), "送达成功", Toast.LENGTH_LONG).show();
                        sendOK();
                    }
                });
            }
        });
        btnJuShou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OrderStatus.RECEIVED.equals(orderDetailModel.getGroupStatus())) {
                    return;
                }
                OrderBiz.orderRefused(orderDetailModel, new SimpleCommonCallback<ServerResponse>(getActivity()) {
                    @Override
                    protected void handleResult(ServerResponse result) {
                        Toast.makeText(getActivity(), "拒收成功", Toast.LENGTH_LONG).show();
                        sendOK();
                    }
                });
            }
        });

    }

    private void sendOK() {
        loadData();
    }


    //    头部订单信息
    class HeadSubAdapter extends DelegateAdapter.Adapter<HeaderViewHolder> {
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
        public HeaderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_orderdetail_header, parent, false);
            return new HeaderViewHolder(v);
        }

        @Override
        public void onBindViewHolder(HeaderViewHolder holder, int position) {
            Log.e(AppConfig.TAG_ERR, "OneColSubAdapter onBindViewHolder:" + position);
            StringBuilder sb = new StringBuilder("");
            sb.append("订单编号:" + orderDetailModel.getOrderId() + "\n");
            sb.append("订单生成时间:" + utils.DateUtils.parseTime(orderDetailModel.getInsDate()) + "\n");
            sb.append("订单完成时间:" + utils.DateUtils.parseTime(orderDetailModel.getUpdDate()) + "\n");
            sb.append("订单状态:" + OrderStatus.getStatus(orderDetailModel.getGroupStatus()));
            String payMtd = "未知";
            switch (orderDetailModel.getPaymentMethod()) {
                case OrderDetailModel.PAYMTD_ONLINE:
                    payMtd = "在线支付";
                    break;
                case OrderDetailModel.PAYMTD_MONEY:
                    payMtd = "货到付款";
                    break;
                case OrderDetailModel.PAYMTD_WECHAT:
                    payMtd = "微信支付";
                    break;
                case OrderDetailModel.PAYMTD_ALI:
                    payMtd = "支付宝支付";
                    break;
                case OrderDetailModel.PAYMTD_UNION:
                    payMtd = "银联支付";
                    break;
                case OrderDetailModel.PAYMTD_ACCOUNT:
                    payMtd = "余额支付";
                    break;
            }
            sb.append("\n支付方式: " + payMtd);
            holder.itemdata = orderDetailModel;
            holder.tvOrderInfo.setText(sb.toString());
            holder.tvName.setText(getString(R.string.order_detail_user_info_name, CommonUtil.noNullString(orderDetailModel.getReceiverName())));
            holder.tvMobile.setText(getString(R.string.order_detail_user_info_mobile, CommonUtil.noNullString(orderDetailModel.getReceiverPhone())));
            holder.tvAddr.setText(getString(R.string.order_detail_user_addr, CommonUtil.noNullString(orderDetailModel.getReceiverAddress())));
            holder.tvDistance.setText(getString(R.string.order_detail_group_distance, CommonUtil.noNullString(orderDetailModel.getDistance())));
        }

        @Override
        public int getItemCount() {
            return mCount;
        }
    }

    //退货信息
    class ReturnGoodsSubAdapter extends DelegateAdapter.Adapter<ReturnGoodsViewHolder> {
        private final LayoutHelper mLayoutHelper;
        private final int mCount;
        private final LayoutParams mLayoutParams;
        private Context mContext;

        public ReturnGoodsSubAdapter(Context context, LayoutHelper layoutHelper, int count, @NonNull LayoutParams layoutParams) {
            this.mContext = context;
            this.mLayoutHelper = layoutHelper;
            this.mCount = count;
            this.mLayoutParams = layoutParams;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return this.mLayoutHelper;
        }

        @Override
        public ReturnGoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_orderdetail_returngoods, parent, false);
            return new ReturnGoodsViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ReturnGoodsViewHolder holder, int position) {
            //退货单详情
            ReturnGoodsInfo r = orderDetailModel.getReturnGoodsInfo();
            if (r == null) return;
            holder.tvReason.setText(getString(R.string.order_detail_return_reason, r.getReturnReason()));
            holder.tvMoney.setText(getString(R.string.order_detail_return_money, CommonUtil.formatFee(r.getReturnMoney() + "")));
            holder.tvRemark.setText(getString(R.string.order_detail_return_remark, r.getReturnRemarks()));
            holder.imgs.setLayoutParams(new LinearLayout.LayoutParams(400, ViewGroup.LayoutParams.WRAP_CONTENT));
            holder.imgs.setImages(r.getImgPathList());
//            设置图片资源
        }

        @Override
        public int getItemCount() {
            return mCount;
        }
    }

    class FootSubAdapter extends DelegateAdapter.Adapter<FooterViewHolder> {
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
        public FooterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_orderdetail_footer, parent, false);
            return new FooterViewHolder(v);
        }

        @Override
        public void onBindViewHolder(FooterViewHolder holder, int position) {
            holder.tvDiscount.setText(orderDetailModel.getDiscount() + "元");
            holder.tvPayamount.setText(getString(R.string.order_detail_group_payamount, CommonUtil.formatFee(orderDetailModel.getPayAmount())));
            holder.tvCostMoney.setText(getString(R.string.order_detail_group_costmoney, CommonUtil.formatFee(orderDetailModel.getCostMoneyAll()), CommonUtil.formatFee(orderDetailModel.getPostageMoneyAll())));
            String invoiceTitle = orderDetailModel.getInvoiceTitle() == null ? "" : orderDetailModel.getInvoiceTitle();
            String invoiceTax = orderDetailModel.getInvoiceTax() == null ? "" : orderDetailModel.getInvoiceTax();
            holder.tvInvoice.setText(getString(R.string.order_detail_group_invoice, invoiceTitle, invoiceTax));
            String remark = orderDetailModel.getRemark() == null ? "" : orderDetailModel.getRemark() + "";
            holder.tvRemark.setText(getString(R.string.order_detail_group_remark, remark));


        }

        @Override
        public int getItemCount() {
            return mCount;
        }
    }

    /*class ReturnInfoSubAdapter extends DelegateAdapter.Adapter<TitleTextViewHolder> {
        private final LayoutHelper mLayoutHelper;
        private final int mCount;
        private final LayoutParams mLayoutParams;
        private Context mContext;

        public ReturnInfoSubAdapter(Context context, LayoutHelper layoutHelper, int count, @NonNull LayoutParams layoutParams) {
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
                    String invoiceTitle = orderDetailModel.getInvoiceTitle() == null ? "" : orderDetailModel.getInvoiceTitle();
                    String invoiceTax = orderDetailModel.getInvoiceTax() == null ? "" : orderDetailModel.getInvoiceTax();
                    holder.tvTitle.setText("发票信息: " + invoiceTitle + " " + invoiceTax);
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
                    String remark = orderDetailModel.getRemark() == null ? "" : orderDetailModel.getRemark() + "";
                    holder.tvTitle.setText("备注：" + remark);
                    holder.tvContent.setText("");
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return mCount;
        }
    }

    class ReturnInfoViewHolder extends RecyclerView.ViewHolder {
        public final View view;

        public ReturnInfoViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
    }*/

    class FooterViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        TextView tvDiscount;
        TextView tvPayamount;
        TextView tvCostMoney;
        TextView tvInvoice;
        TextView tvRemark;


        public FooterViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            tvDiscount = (TextView) itemView.findViewById(R.id.fragment_orderdetail_tv_groupdiscount);
            tvPayamount = (TextView) itemView.findViewById(R.id.fragment_orderdetail_tv_payamount);
            tvCostMoney = (TextView) itemView.findViewById(R.id.fragment_orderdetail_tv_costmoney);
            tvInvoice = (TextView) itemView.findViewById(R.id.fragment_orderdetail_tv_invoice);
            tvRemark = (TextView) itemView.findViewById(R.id.fragment_orderdetail_tv_remark);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        RelativeLayout rlLoc;
        TextView tvOrderInfo;
        TextView tvDistance;
        TextView tvMobile;
        TextView tvName;
        TextView tvAddr;
        public OrderDetailModel itemdata;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            tvOrderInfo = (TextView) itemView.findViewById(R.id.fragment_orderdetail_tv_orderinfo);
            tvDistance = (TextView) itemView.findViewById(R.id.fragment_orderdetail_distance);
            tvName = (TextView) itemView.findViewById(R.id.fragment_orderdetail_tv_user_name);
            tvMobile = (TextView) itemView.findViewById(R.id.fragment_orderdetail_tv_user_mobile);
            tvAddr = (TextView) itemView.findViewById(R.id.fragment_orderdetail_tv_user_addr);
            rlLoc = (RelativeLayout) itemView.findViewById(R.id.fragment_orderdetail_rl_loc);

            tvMobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemdata != null && !StringUtils.isEmpty(itemdata.getReceiverPhone())) {
                        com.wyw.ljtmgr.utils.CommonUtil.call(getActivity(), itemdata.getReceiverPhone());
                    }
                }
            });
            rlLoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = LogisticMapActivity.getIntent(getActivity(), "35.504755", "112.839483", orderDetailModel.getLat(), orderDetailModel.getLng());
                    startActivity(it);
                }
            });
        }
    }

    class ReturnGoodsViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public OrderDetailModel itemdata;
        TextView tvReason;
        TextView tvMoney;
        TextView tvRemark;
        MultiView imgs;

        public ReturnGoodsViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            tvReason = (TextView) view.findViewById(R.id.fragment_orderdetail_return_reason);
            tvMoney = (TextView) view.findViewById(R.id.fragment_orderdetail_return_money);
            tvRemark = (TextView) view.findViewById(R.id.fragment_orderdetail_return_remark);
            imgs = (MultiView) view.findViewById(R.id.fragment_orderdetail_return_imgs);
        }
    }

    /*class TitleTextViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        TextView tvTitle;
        TextView tvContent;

        public TitleTextViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            tvTitle = (TextView) itemView.findViewById(R.id.textview_title);
            tvContent = (TextView) itemView.findViewById(R.id.textview_content);
        }
    }*/


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
//            item_order_goods_price
            StringBuilder sbInfo = new StringBuilder();
            if (!StringUtils.isEmpty(item.getCommodityParameter())) {
                sbInfo.append("(" + item.getCommodityParameter() + ")");
            }
            sbInfo.append(StringUtils.deletaFirst(item.getTitle())).append("\n");
            String size = "";
            if ("0".equals(orderDetailModel.getFlag())) {
                String barCode = getString(R.string.title_code) + ":" + item.getBarCode() + "\n";
                sbInfo.append(barCode);
                size = getString(R.string.title_factory) + ":" + item.getCommodityColor() + "\n" + getString(R.string.title_size) + ": " + item.getCommoditySize() + "\n";
            } else {
                sbInfo.append("\n");
                size = getString(R.string.title_cat) + ":" + item.getCommodityColor() + "," + getString(R.string.title_size) + ": " + item.getCommoditySize();
            }
            sbInfo.append(size);
            holder.tvInfo.setText(sbInfo.toString());
            holder.tvPrice.setText("￥" + CommonUtil.formatFee("" + item.getCostMoney()));
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
        private final TextView tvPrice;
        private final TextView tvNum;

        public OrderViewHolder(View itemView) {
            super(itemView);
            imv = (ImageView) itemView.findViewById(R.id.item_order_goods_img);
            tvInfo = (TextView) itemView.findViewById(R.id.item_order_goods_info);
            tvNum = (TextView) itemView.findViewById(R.id.item_order_goods_num);
            tvPrice = (TextView) itemView.findViewById(R.id.item_order_goods_price);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_REJECTRETURN_MSG:
                String rejectMsg = data.getStringExtra(RejectReturnDialogFragment.TAG_RETURN_MSG);
                OrderBiz.orderAfterSale(orderDetailModel.getOrderId(), "1", rejectMsg, new SimpleCommonCallback<ServerResponse>(getActivity()) {
                    @Override
                    protected void handleResult(ServerResponse result) {
                        Toast.makeText(getActivity(), "拒绝成功", Toast.LENGTH_LONG).show();
                        sendOK();
                    }
                });
                break;
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
