package com.wyw.ljtds.ui.user.order;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alipay.PayResult;
import com.alipay.sdk.app.PayTask;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.JsonSyntaxException;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.unionpay.UPPayAssistEx;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.CommOrderAdapter;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.model.OnlinePayModel;
import com.wyw.ljtds.model.OrderModelMedicine;
import com.wyw.ljtds.model.OrderTrade;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.ui.goods.ActivityGoodsSubmit;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.PayUtil;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.PayDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/5 0005.
 * handle order list pay
 */

@ContentView(R.layout.fragment_order_list)
public class FragmentOrderList extends BaseFragment {
    public static final String TAG_GROUP_ORDER_ID = "com.wyw.ljtds.ui.user.order.FragmentOrderList.tag_group_order_id";
    public static final int REQ_GO_EVALUATE = 101;
    public static final int RLT_GO_EVALUATE = 101;
    @ViewInject(R.id.reclcyer)
    private RecyclerView recyclerView;

    //无数据时的界面
    private View noData;
    //    private MyAdapter adapter;
//    private List<OrderModel> list = new ArrayList<>();
    private MyAdapter1 adapter1;
    private List<OrderModelMedicine> list;
    private PayModel payModel;
    //客服
    String settingid1 = "lj_1000_1493167191869";
    String groupName = "蓝九天";// 客服组默认名称
    private ActivityOrder activityOrder;

    private static final int ALI_PAY = 1;
    private static final int UP_PAY = 2;
    private String StateCat = "";
    private String jsonUnionOrder = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(AppConfig.ERR_TAG, "fragment resume");
        updAdapter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(AppConfig.ERR_TAG, "onAttach" + this.getId());
        activityOrder = (ActivityOrder) context;
    }

    public void goEvaluate(String orderId) {
//        Intent it = new Intent(getActivity(), ActivityEvaluateEdit.class);
        Intent it = ActivityEvaluateEdit.getIntent(getActivity(), orderId);
        startActivity(it);
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());//必须有
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置水平方向滑动
        recyclerView.setLayoutManager(linearLayoutManager);

        noData = getActivity().getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recyclerView.getParent(), false);
        ImageView imageView_nodata = (ImageView) noData.findViewById(R.id.empty_img);
        imageView_nodata.setImageDrawable(getResources().getDrawable(R.mipmap.dingdan_kong));
        TextView textView_nodata = (TextView) noData.findViewById(R.id.empty_text);
        textView_nodata.setText(R.string.empty_order);
        recyclerView.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (adapter1 == null)
                    return;
                if (adapter1.getItemCount() <= 0)
                    return;
                Intent it = new Intent(getActivity(), ActivityOrderInfo.class);
                it.putExtra("id", adapter1.getItem(i).getORDER_GROUP_ID());
                startActivity(it);
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, final int i) {
                Intent it;
                final OrderModelMedicine currentModel = adapter1.getData().get(i);
                switch (currentModel.getSTATUS()) {
                    case "A":
                        if (view.getId() == R.id.button1) {
                            //立即支付
                            OrderModelMedicine order = currentModel;
                            OrderTrade orderTrade = new OrderTrade();
                            orderTrade.setPayAmount(String.valueOf(order.getPAY_AMOUNT()));
                            orderTrade.setOrderTradeId(order.getORDER_TRADE_ID());
                            orderTrade.setPaymentMethod(order.getPAYMENT_METHOD());
                            //pay
                            PayDialog.showDialog(activityOrder, orderTrade, new PayDialog.PayMethodSelectedListener() {
                                @Override
                                public void onItemSelected(OrderTrade order) {
                                    payModel = new PayModel();
                                    payModel.setORDER_TRADE_ID(order.getOrderTradeId());
                                    payModel.setPAYMENT_METHOD(order.getPaymentMethod());
                                    payModel.setPAYMENT_METHOD(order.getPaymentMethod());
                                    payModel.setIp(Utils.getIPAddress(getActivity()));
                                    payModel.setDevice(Utils.getImei(getActivity()));
                                    goPay(GsonUtils.Bean2Json(payModel));
                                }

                                @Override
                                public void onDialogClose() {
                                }
                            });

                        } else if (view.getId() == R.id.button2) {
                            //取消
                            final AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
                            alert.setTitle(R.string.alert_tishi);
                            alert.setMessage(getResources().getString(R.string.confirm_order_cancel));
                            alert.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.alert_queding), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    cancelOrder(currentModel.getORDER_GROUP_ID());
                                }
                            });
                            alert.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.alert_quxiao), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alert.dismiss();
                                }
                            });
                            alert.show();
                        }
//                        else if (view.getId() == R.id.button3) {
////                            activityOrder.openChat("交易订单号：" + adapter1.getData().get(i).getORDER_TRADE_ID(), "", settingid1, groupName, false, "");
//                            Log.e(AppConfig.ERR_TAG, "data:" + GsonUtils.Bean2Json(adapter1.getData().get(i)));
////                openChat( model.getTitle(), "", settingid1, groupName, true, model.getCommodityId() );
////                            if (!StringUtils.isEmpty(group_id) && group_id.equals("sxljt")) {
////                                openChat("交易订单号：" + trade_id, "", settingid0, groupName, false, "");
////                            } else if (!StringUtils.isEmpty(group_id)) {
////                                openChat("交易订单号：" + trade_id, "", settingid1, groupName, false, "");
////                            }
//                        }
                        break;

                    case "B":
                        if (view.getId() == R.id.button1) {
//                            activityOrder.openChat("交易订单号：" + adapter1.getData().get(i).getORDER_TRADE_ID(), "", settingid1, groupName, false, "");
                            it = new Intent(getActivity(), ActivityLogistics.class);
                            it.putExtra("order_id", currentModel.getORDER_GROUP_ID());
                            startActivity(it);
                        } else if (view.getId() == R.id.button2) {
                            //取消
                            final AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
                            alert.setTitle(R.string.alert_tishi);
                            alert.setMessage(getResources().getString(R.string.confirm_order_cancel));
                            alert.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.alert_queding), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    cancelOrder(currentModel.getORDER_GROUP_ID());
                                }
                            });
                            alert.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.alert_quxiao), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alert.dismiss();
                                }
                            });
                            alert.show();
                        }
                        break;

                    case "C":
                        if (view.getId() == R.id.button1) {
                            getSignFor(currentModel.getORDER_GROUP_ID());
                        } else if (view.getId() == R.id.button2) {
                            it = new Intent(getActivity(), ActivityLogistics.class);
                            it.putExtra("order_id", currentModel.getORDER_GROUP_ID());
                            startActivity(it);
                        }
                        break;

                    case "D":
                        if (view.getId() == R.id.button1) {
                            String orderId = currentModel.getORDER_GROUP_ID();
                            goEvaluate(orderId);
                        } else if (view.getId() == R.id.button2) {
                            it = new Intent(getActivity(), ActivityLogistics.class);
                            it.putExtra("order_id", currentModel.getORDER_GROUP_ID());
                            startActivity(it);
                        }
                        break;

                    case "S":
                        if (view.getId() == R.id.button1) {
                            it = new Intent(getActivity(), ActivityLogistics.class);
                            it.putExtra("order_id", currentModel.getORDER_GROUP_ID());
                            startActivity(it);
                        } else if (view.getId() == R.id.button2) {
                            final AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
                            alert.setTitle(R.string.alert_tishi);
                            alert.setMessage(getResources().getString(R.string.confirm_order_del));
                            alert.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.alert_queding), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    delete(currentModel.getORDER_TRADE_ID());
                                }
                            });
                            alert.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.alert_quxiao), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alert.dismiss();
                                }
                            });
                            alert.show();
                        }
                        break;

                    case "E":
                        if (view.getId() == R.id.button1) {
                            final AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
                            alert.setTitle(R.string.alert_tishi);
                            alert.setMessage(getResources().getString(R.string.confirm_order_del));
                            alert.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.alert_queding), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    delete(currentModel.getORDER_TRADE_ID());
                                }
                            });
                            alert.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.alert_quxiao), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alert.dismiss();
                                }
                            });
                            alert.show();
                        }
                        break;

                }
            }

            @Override
            public void onItemChildLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }
        });
    }

    private void updAdapter() {
        adapter1 = new MyAdapter1();
        adapter1.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        adapter1.setEmptyView(noData);
        recyclerView.setAdapter(adapter1);
        recyclerView.setVisibility(View.VISIBLE);
        getOrder(StateCat, "myOrders");
    }

//    @Override
//    protected void lazyLoad() {
//        Log.e(AppConfig.ERR_TAG, "lazyLoa");
//        updAdapter();
////        getOrder(StateCat, "myOrders");
//    }

//    @Override
//    protected void stopLoad() {
//        super.stopLoad();
//        Log.e(AppConfig.ERR_TAG, "stopLoad");
//        adapter1 = null;
//        recyclerView.setVisibility(View.GONE);
//    }

    private void getOrder(final String data, final String op) {
        new BizDataAsyncTask<List<OrderModelMedicine>>() {
            @Override
            protected List<OrderModelMedicine> doExecute() throws ZYException, BizFailure {
                return GoodsBiz.getUserOrder(data, op);
                //todo:use load
//                String str = "[{ \"CREATE_DATE\": 1515755562000, \"DETAILS\": [{ \"COMMODITY_COLOR\": \"威海百合生物技术股份有限公司\", \"COMMODITY_ID\": \"005578\", \"COMMODITY_NAME\": \"Y黄芪红景天铬酵母软胶囊（百合康特供）\", \"COMMODITY_ORDER_ID\": \"2018011219100944701001\", \"COMMODITY_SIZE\": \"0.6克*60粒\", \"COST_MONEY\": 96.00, \"EXCHANGE_QUANLITY\": 1, \"IMG_PATH\": \"http://www.lanjiutian.com/upload/images/a81639d4bafa409a8c624352e65ce796/c4ca4238a0b923820dcc509a6f75849b.jpg\", \"ORDER_STATUS\": \"C\" }], \"DISTRIBUTION_DATE\": \"2018/01/12 20:00---2018/01/12 21:00\", \"DISTRIBUTION_MODE\": \"0\", \"ELECTRONIC_MONEY\": 0.00, \"GROUP_EXCHANGE_QUANLITY\": 1, \"GROUP_MONEY_ALL\": 96.00, \"GROUP_STATUS\": \"1\", \"INVOICE_FLG\": \"0\", \"LOGISTICS_COMPANY\": \"市区新市西街店\", \"ORDER_GROUP_ID\": \"2018011219100944701\", \"ORDER_TRADE_ID\": \"20180112191009447\", \"PAYMENT_METHOD\": \"C\", \"PAY_AMOUNT\": 137.80, \"POSTAGE\": 0.00, \"POSTAGE_FLG\": \"0\", \"STATUS\": \"B\", \"USER_ADDRESS_ID\": \"wwww|13122223333|ttttttttt\" }]";
//                return GsonUtils.Json2ArrayList(str,OrderModelMedicine.class);
            }

            @Override
            protected void onExecuteSucceeded(List<OrderModelMedicine> orderModelMedicines) {
                closeLoding();
                adapter1.setNewData(orderModelMedicines);
                adapter1.notifyDataSetChanged();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
        setLoding(getActivity(), false);
    }

    public String getStateCat() {
        return StateCat;
    }

    public void setStateCat(String stateCat) {
        StateCat = stateCat;
    }

//    BizDataAsyncTask<List<OrderModel>> orderTask;
//    private void getOrder(final String status){
//        orderTask=new BizDataAsyncTask<List<OrderModel>>() {
//            @Override
//            protected List<OrderModel> doExecute() throws ZYException, BizFailure {
//                return GoodsBiz.getOrderList(status);
//            }
//
//            @Override
//            protected void onExecuteSucceeded(List<OrderModel> orderModels) {
//                if (orderModels.size() == 0) {
//                    adapter.setEmptyView(noData);
//                }
//                adapter.addData(orderModels);
//
//                closeLoding();
//            }
//
//            @Override
//            protected void OnExecuteFailed() {
//                adapter.setEmptyView(noData);
//                closeLoding();
//            }
//        };
//        orderTask.execute();
//    }

//    private class MyAdapter extends BaseQuickAdapter<OrderModel> {
//        public MyAdapter() {
//            super(R.layout.item_order_info, list);
//        }
//
//        @Override
//        protected void convert(BaseViewHolder baseViewHolder, OrderModel orderModel) {
//            String status="";
//
//            if (orderModel.getSTATUS().equals("1")){ //待付款
//                status=getString(R.string.order_style1);
//
//                baseViewHolder.setVisible(R.id.style_daifu,true)
//                        .setVisible(R.id.style_quxiao,true)
//                        .setVisible(R.id.style_lianxi,true)
//                        .setText(R.id.style_daifu,R.string.fukuan)
//                        .setText(R.id.style_quxiao,R.string.order_quxiao)
//                        .setText(R.id.style_lianxi,R.string.order_lianxi1);
//            }else if (orderModel.getSTATUS().equals("4")){ //发货
//                status=getString(R.string.order_style2);
//
//                baseViewHolder.setVisible(R.id.style_daifu,true)
//                        .setVisible(R.id.style_quxiao,true)
//                        .setVisible(R.id.style_lianxi,false)
//                        .setText(R.id.style_daifu,R.string.order_lianxi1)
//                        .setText(R.id.style_quxiao,R.string.order_chakan);
//            }else if (orderModel.getSTATUS().equals("5")){ //待收货
//                status=getString(R.string.order_style3);
//
//                baseViewHolder.setVisible(R.id.style_daifu,true)
//                        .setVisible(R.id.style_quxiao,true)
//                        .setVisible(R.id.style_lianxi,true)
//                        .setText(R.id.style_daifu,R.string.querenshou)
//                        .setText(R.id.style_quxiao,R.string.wuliu_chakan)
//                        .setText(R.id.style_lianxi,R.string.order_lianxi1);
//            }else if (orderModel.getSTATUS().equals("6")){ //待评级
//                status=getString(R.string.order_style4);
//
//                baseViewHolder.setVisible(R.id.style_daifu,true)
//                        .setVisible(R.id.style_quxiao,true)
//                        .setVisible(R.id.style_lianxi,false)
//                        .setText(R.id.style_daifu,R.string.order_pingjia)
//                        .setText(R.id.style_quxiao,R.string.order_chakan);
//            }else if (orderModel.getSTATUS().equals("0")){ //确定订单
//                status=getString(R.string.order_style0);
//                baseViewHolder.setVisible(R.id.style_daifu,true)
//                        .setVisible(R.id.style_quxiao,true)
//                        .setVisible(R.id.style_lianxi,false)
//                        .setText(R.id.style_daifu,R.string.fukuan)
//                        .setText(R.id.style_quxiao,R.string.order_quxiao);
//            }
//
//
//            baseViewHolder.setText(R.id.name,orderModel.getTITLE())
//                    .setText(R.id.guige,orderModel.getCOMMODITY_COLOR()+orderModel.getCOMMODITY_SIZE())
//                    .setText(R.id.danjia,orderModel.getCOST_MONEY()+"")
//                    .setText(R.id.yuanjia,orderModel.getMARKET_PRICE()+"")
//                    .setText(R.id.shuliang,orderModel.getEXCHANGE_QUANLITY()+"")
//                    .setText(R.id.heji_shuliang,"共"+orderModel.getEXCHANGE_QUANLITY()+"件商品")
//                    .setText(R.id.heji_fukuan,orderModel.getPAY_AMOUNT()+"")
//                    .setText(R.id.yunfei,"（含运费￥0.00）")
//                    .setText(R.id.title_style,status)
//                    .addOnClickListener(R.id.ll2)
//                    .addOnClickListener(R.id.style_daifu)
//                    .addOnClickListener(R.id.style_quxiao)
//                    .addOnClickListener(R.id.style_lianxi);
//
//            TextView textView=baseViewHolder.getView(R.id.yuanjia);
//            textView.getPaint().setAntiAlias(true);//抗锯齿
////
//            textView.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
//
////            textView.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
//        }
//
//    }

    private class MyAdapter1 extends BaseQuickAdapter<OrderModelMedicine> {

        public MyAdapter1() {
            super(R.layout.item_order_group, list);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, OrderModelMedicine orderModelMedicine) {
            baseViewHolder.setText(R.id.item_order_group_groupname, orderModelMedicine.getLOGISTICS_COMPANY());
            String status = "";
            if ("A".equals(orderModelMedicine.getSTATUS())) {
                status = getResources().getString(R.string.daifu);
                baseViewHolder.setVisible(R.id.button1, true)
                        .setVisible(R.id.button2, true)
//                        .setVisible(R.id.button3, true)
                        .setText(R.id.button1, R.string.fukuan)
                        .setText(R.id.button2, R.string.order_quxiao)
//                        .setText(R.id.button3, R.string.order_lianxi1)
                        .addOnClickListener(R.id.button1)
                        .addOnClickListener(R.id.button2);
            } else if ("B".equals(orderModelMedicine.getSTATUS())) {
                status = getResources().getString(R.string.daifa);
                baseViewHolder.setVisible(R.id.button1, true)
                        .setText(R.id.button1, R.string.wuliu_chakan)
                        .addOnClickListener(R.id.button1);
                if (OrderTrade.PAYMTD_MONEY.equals(orderModelMedicine.getPAYMENT_METHOD())) {
                    //货到付款的可以取消
                    baseViewHolder.setVisible(R.id.button2, true)
                            .setText(R.id.button2, R.string.order_quxiao)
                            .addOnClickListener(R.id.button2);
                }
            } else if ("C".equals(orderModelMedicine.getSTATUS())) {
                status = getResources().getString(R.string.daishou);
                baseViewHolder.setVisible(R.id.button1, true)
                        .setVisible(R.id.button2, true)
//                        .setVisible(R.id.button3, true)
                        .setText(R.id.button1, R.string.querenshou)
                        .setText(R.id.button2, R.string.wuliu_chakan)
//                        .setText(R.id.button3, R.string.order_lianxi1)
                        .addOnClickListener(R.id.button1)
                        .addOnClickListener(R.id.button2);
            } else if ("D".equals(orderModelMedicine.getSTATUS())) {
                status = getResources().getString(R.string.daiping);
                baseViewHolder.setVisible(R.id.button1, true)
                        .setVisible(R.id.button2, true)
//                        .setVisible(R.id.button3, false)
                        .setText(R.id.button1, R.string.order_pingjia)
                        .setText(R.id.button2, R.string.wuliu_chakan)
                        .addOnClickListener(R.id.button1)
                        .addOnClickListener(R.id.button2);
            } else if ("S".equals(orderModelMedicine.getSTATUS())) {
                status = getResources().getString(R.string.order_style6);
                baseViewHolder.setVisible(R.id.button1, true)
                        .setVisible(R.id.button2, true)
                        .setText(R.id.button1, R.string.wuliu_chakan)
                        .setText(R.id.button2, R.string.order_shanchu)
                        .addOnClickListener(R.id.button1)
                        .addOnClickListener(R.id.button2);
            } else if ("E".equals(orderModelMedicine.getSTATUS())) {
                status = getResources().getString(R.string.order_style7);
                baseViewHolder.setVisible(R.id.button1, true)
                        .setVisible(R.id.button2, false)
                        .setVisible(R.id.button3, false)
                        .setText(R.id.button1, R.string.shanchu)
                        .addOnClickListener(R.id.button1);
            } else if ("T".equals(orderModelMedicine.getSTATUS())) {
                status = getResources().getString(R.string.order_stat_return);
            } else {
                status = getResources().getString(R.string.stat_unkwon);
            }
            baseViewHolder.setText(R.id.item_order_group_status, status);
//                    .setVisible(R.id.anniu, true)
            baseViewHolder.setText(R.id.item_order_group_tv_cost, "共计" + orderModelMedicine.getGROUP_EXCHANGE_QUANLITY() + "件商品" + "￥" + orderModelMedicine.getGROUP_PAY_AMOUNT());

            RecyclerView goods = baseViewHolder.getView(R.id.goods);
            goods.setLayoutManager(new LinearLayoutManager(getActivity()));
            goods.setItemAnimator(new DefaultItemAnimator());
            goods.setAdapter(new CommOrderAdapter(orderModelMedicine.getDETAILS()));

        }
    }

    //商品adapter
    /*private class MyAdapter2 extends BaseQuickAdapter<OrderModelMedicine.Goods> {
        public MyAdapter2(List<OrderModelMedicine.Goods> lists) {
            super(R.layout.item_order_submit_goods, lists);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, OrderModelMedicine.Goods goods) {
            if (StringUtils.isEmpty(goods.getCOMMODITY_COLOR())) {
                baseViewHolder.setText(R.id.size, " 规格：" + goods.getCOMMODITY_SIZE());
            } else {
                baseViewHolder.setText(R.id.size, "产地：" + goods.getCOMMODITY_COLOR() + " ;规格：" + goods.getCOMMODITY_SIZE());
            }
            baseViewHolder.setText(R.id.title, StringUtils.deletaFirst(goods.getCOMMODITY_NAME()))
                    .setText(R.id.money, "￥" + goods.getCOST_MONEY())
                    .setText(R.id.number, "X" + goods.getEXCHANGE_QUANLITY());

            SimpleDraweeView simpleDraweeView = baseViewHolder.getView(R.id.iv_adapter_list_pic);
            if (!StringUtils.isEmpty(goods.getIMG_PATH())) {
                simpleDraweeView.setImageURI(Uri.parse(goods.getIMG_PATH()));
            }
//
        }
    }*/

    //在线支付
    BizDataAsyncTask<OnlinePayModel> payTask;

    private void goPay(final String data) {
        setLoding(getActivity(), false);
        new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                Utils.log("data:" + data);
                return GoodsBiz.onlinePay(data);
            }

            @Override
            protected void onExecuteSucceeded(String rltData) {
                closeLoding();
                Map dataMap;
                Map daraResult = null;
                try {
                    dataMap = GsonUtils.Json2Bean(rltData, HashMap.class);
                    daraResult = (Map) dataMap.get("result");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Utils.log("JsonSyntaxException:" + ex.getMessage());
                    ToastUtil.show(getActivity(), "服务器数据格式有错误");
                }
                PayUtil payUtil = Utils.getPayUtilInstance(getActivity(), daraResult);
                if (payModel.getPAYMENT_METHOD().equals(OrderTrade.PAYMTD_WECHAT)) {
                    payUtil.pay4Wechat();
                } else if (payModel.getPAYMENT_METHOD().equals(OrderTrade.PAYMTD_ALI)) {
                    payUtil.pay4Ali(mHandler);
                } else if (payModel.getPAYMENT_METHOD().equals(OrderTrade.PAYMTD_UNION)) {
                    payUtil.pay4Union();
                } else {
                }

            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }
/*
    private void goPay(final String str) {
      payTask = new BizDataAsyncTask<OnlinePayModel>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
//                Log.e(AppConfig.ERR_TAG, str);
                return GoodsBiz.onlinePay(str);
            }

            @Override
            protected void onExecuteSucceeded(OnlinePayModel aliPay) {
                closeLoding();
                final String orderInfo = aliPay.getPay();
                if (payModel.getPAYMENT_METHOD().equals(OrderTrade.PAYMTD_ALI)) {
                    //Ali支付方式
                    if (!StringUtils.isEmpty(orderInfo)) {
                        Runnable payRunnable = new Runnable() {

                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(getActivity());
                                Map<String, String> result = alipay.payV2(orderInfo, true);

                                Message msg = new Message();
                                msg.what = ALI_PAY;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        };

                        Thread payThread = new Thread(payRunnable);
                        payThread.start();


                    }
                } else if (payModel.getPAYMENT_METHOD().equals(OrderTrade.PAYMTD_UNION)) {
                    //unionpay银联支付方式
                    jsonUnion = orderInfo;
                    Map<String, Object> mUnion = GsonUtils.Json2Bean(jsonUnion, Map.class);
                    String tn = (String) mUnion.get("tn");
                    UPPayAssistEx.startPay(getActivity(), null, null, tn, "01");
                }

            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        };
        setLoding(activityOrder, false);
        payTask.execute();
    }*/


    //取消订单
    BizDataAsyncTask<Integer> cancelTask;

    private void cancelOrder(final String data) {
        cancelTask = new BizDataAsyncTask<Integer>() {
            @Override
            protected Integer doExecute() throws ZYException, BizFailure {
                return GoodsBiz.orderOperation(data, "cancelOrder");
            }

            @Override
            protected void onExecuteSucceeded(Integer integer) {
                closeLoding();
                ToastUtil.show(getActivity(), "取消成功");
                getOrder(StateCat, "myOrders");
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        };
        setLoding(activityOrder, false);
        cancelTask.execute();
    }


    //订单签收
    private void getSignFor(final String data) {
        BizDataAsyncTask<Integer> signTask = new BizDataAsyncTask<Integer>() {
            @Override
            protected Integer doExecute() throws ZYException, BizFailure {
                return GoodsBiz.orderOperation(data, "checkinOrder");
            }

            @Override
            protected void onExecuteSucceeded(Integer integer) {
                closeLoding();
                if (integer != 0) {
                    ToastUtil.show(getActivity(), "签收成功");
                    goEvaluate(data);
                }
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        };
        setLoding(getActivity(), false);
        signTask.execute();
    }

    //删除订单

    private void delete(final String data) {
        BizDataAsyncTask<Integer> deleteTask = new BizDataAsyncTask<Integer>() {
            @Override
            protected Integer doExecute() throws ZYException, BizFailure {
                return GoodsBiz.orderOperation(data, "delOrder");
            }

            @Override
            protected void onExecuteSucceeded(Integer integer) {
                ToastUtil.show(getActivity(), "删除成功");
                closeLoding();
                getOrder(StateCat, "myOrders");
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        };
        setLoding(getActivity(), false);
        deleteTask.execute();
    }

    //支付结束结果
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ALI_PAY:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ToastUtil.show(getActivity(), getResources().getString(R.string.pay_success));
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtil.show(getActivity(), getResources().getString(R.string.pay_fail));
                    }
                    Log.e("reslut", resultInfo);

                    if (!StringUtils.isEmpty(resultInfo)) {
                        PayUtil.AliResult aliResult = GsonUtils.Json2Bean(resultInfo, PayUtil.AliResult.class);
                        aliResult.setORDER_TRADE_ID(payModel.getORDER_TRADE_ID());

                        String json = GsonUtils.Bean2Json(aliResult);
                        sendResult(json);
                    }

                    break;

                case UP_PAY:


            }

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQ_GO_EVALUATE == requestCode && RLT_GO_EVALUATE == resultCode) {
            updAdapter();
        }
        if (data == null)
            return;
        String str = data.getExtras().getString("pay_result");
        if ("success".equalsIgnoreCase(str)) {
            // 支付成功后，extra中如果存在result_data，取出校验
            // result_data结构见c）result_data参数说明
            String result = "";
            if (data.hasExtra("result_data")) {
                result = data.getExtras().getString("result_data");
                Log.e("result_xxxxx", result);
                try {
                    JSONObject resultJson = new JSONObject(result);
                    String sign = resultJson.getString("sign");
                    String dataOrg = resultJson.getString("data");
                    // 验签证书同后台验签证书
                    // 此处的verify，商户需送去商户后台做验签
                    boolean ret = Utils.verify(dataOrg, sign, AppConfig.UPPAY_MODE);
                    if (ret) {
                        // 验证通过后，显示支付结果
                        ToastUtil.show(getActivity(), getResources().getString(R.string.pay_success));
                    } else {
                        // 验证不通过后的处理
                        // 建议通过商户后台查询支付结果
                        ToastUtil.show(getActivity(), getResources().getString(R.string.pay_fail));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Utils.log("JsonSyntaxException:" + e.getMessage());
                    ToastUtil.show(getActivity(), "服务器数据格式有错误");
                }
            } else {
                // 未收到签名信息
                // 建议通过商户后台查询支付结果
                ToastUtil.show(getActivity(), getResources().getString(R.string.pay_success));
            }

            if (!StringUtils.isEmpty(result)) {
                PayUtil.UnionResult unionResult = GsonUtils.Json2Bean(result, PayUtil.UnionResult.class);
                unionResult.setORDER_TRADE_ID(payModel.getORDER_TRADE_ID());

                String json = GsonUtils.Bean2Json(unionResult);
                sendResult(json);
            }

        } else if (str.equalsIgnoreCase("fail")) {
            ToastUtil.show(getActivity(), getResources().getString(R.string.pay_fail));
        } else if (str.equalsIgnoreCase("cancel")) {
            ToastUtil.show(getActivity(), getResources().getString(R.string.pay_cancel));
        }

    }


    BizDataAsyncTask<String> payResultTask;

    private void sendResult(final String data) {
        payResultTask = new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                return GoodsBiz.onlinePayResult(data);
            }

            @Override
            protected void onExecuteSucceeded(String s) {
                closeLoding();
//                Intent it = new Intent( getActivity(), ActivityOrder.class );
//                getActivity().finish();
//                startActivity( it );
                getOrder(StateCat, "myOrders");
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        };
        setLoding(getActivity(), false);
        payResultTask.execute();
    }

    //提交在线支付的订单
    public class PayModel {
        private String ORDER_TRADE_ID;
        private String PAYMENT_METHOD;
        private String device;
        private String ip;

        public String getORDER_TRADE_ID() {
            return ORDER_TRADE_ID;
        }

        public void setORDER_TRADE_ID(String ORDER_TRADE_ID) {
            this.ORDER_TRADE_ID = ORDER_TRADE_ID;
        }

        public String getPAYMENT_METHOD() {
            return PAYMENT_METHOD;
        }

        public void setPAYMENT_METHOD(String PAYMENT_METHOD) {
            this.PAYMENT_METHOD = PAYMENT_METHOD;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getDevice() {
            return device;
        }

        public void setDevice(String device) {
            this.device = device;
        }
    }


}
