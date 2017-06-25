package com.wyw.ljtds.ui.user.order;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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
import com.unionpay.UPPayAssistEx;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.OnlinePayModel;
import com.wyw.ljtds.model.OrderModelMedicine;
import com.wyw.ljtds.ui.base.LazyLoadFragment;
import com.wyw.ljtds.ui.goods.ActivityGoodsSubmit;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/5 0005.
 */

@ContentView(R.layout.fragment_order_list)
public class FragmentOrderList extends LazyLoadFragment {
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach( context );
        activityOrder = (ActivityOrder) context;
    }

    @Override
    protected void lazyLoad() {

        setLoding( getActivity(), false );
        getOrder( "", "myOrders" );

        recyclerView.setVisibility( View.VISIBLE );

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getActivity() );//必须有
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );//设置水平方向滑动
        recyclerView.setLayoutManager( linearLayoutManager );

        noData = getActivity().getLayoutInflater().inflate( R.layout.main_empty_view, (ViewGroup) recyclerView.getParent(), false );
        ImageView imageView_nodata = (ImageView) noData.findViewById( R.id.empty_img );
        imageView_nodata.setImageDrawable( getResources().getDrawable( R.mipmap.dingdan_kong ) );
        TextView textView_nodata = (TextView) noData.findViewById( R.id.empty_text );
        textView_nodata.setText( R.string.empty_order );

        adapter1 = new MyAdapter1();
        adapter1.openLoadAnimation( BaseQuickAdapter.ALPHAIN );
        recyclerView.addOnItemTouchListener( new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent it = new Intent( getActivity(), ActivityOrderInfo.class );
                it.putExtra( "id", adapter1.getItem( i ).getORDER_GROUP_ID() );
                startActivity( it );
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent it;
                switch (adapter1.getData().get( i ).getSTATUS()) {
                    case "A":
                        if (view.getId() == R.id.button1) {
                            payModel = new PayModel();
                            payModel.setORDER_TRADE_ID( adapter1.getData().get( i ).getORDER_TRADE_ID() );
                            payModel.setPAYMENT_METHOD( adapter1.getData().get( i ).getPAYMENT_METHOD() );
                            goPay( GsonUtils.Bean2Json( payModel ) );
                        } else if (view.getId() == R.id.button2) {
                            cancelOrder( adapter1.getData().get( i ).getORDER_GROUP_ID() );
                        } else if (view.getId() == R.id.button3) {
                            activityOrder.openChat( "交易订单号：" + adapter1.getData().get( i ).getORDER_TRADE_ID(), "", settingid1, groupName, false, "" );
                        }
                        break;

                    case "B":
                        if (view.getId() == R.id.button1) {
                            activityOrder.openChat( "交易订单号：" + adapter1.getData().get( i ).getORDER_TRADE_ID(), "", settingid1, groupName, false, "" );
                        }
                        break;

                    case "C":
                        if (view.getId() == R.id.button1) {
                            getSignFor( adapter1.getData().get( i ).getORDER_GROUP_ID() );
                        } else if (view.getId() == R.id.button2) {
                            it = new Intent( getActivity(), ActivityLogistics.class );
                            it.putExtra( "order_id", adapter1.getData().get( i ).getORDER_GROUP_ID() );
                            startActivity( it );
                        } else if (view.getId() == R.id.button3) {

                        }
                        break;

                    case "D":
                        if (view.getId() == R.id.button1) {
                            it = new Intent( getActivity(), ActivityEvaluate.class );
                            it.putExtra( "order_id", adapter1.getData().get( i ).getORDER_GROUP_ID() );
                            startActivity( it );
                        }
                        break;

                    case "S":
                        if (view.getId() == R.id.button1) {
                            it = new Intent( getActivity(), ActivityLogistics.class );
                            it.putExtra( "order_id", adapter1.getData().get( i ).getORDER_GROUP_ID() );
                            startActivity( it );
                        }
                        break;

                    case "E":
                        delete( adapter1.getData().get( i ).getORDER_TRADE_ID() );
                        break;

                }
            }

            @Override
            public void onItemChildLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }
        } );
        recyclerView.setAdapter( adapter1 );

    }

    @Override
    protected void stopLoad() {
        super.stopLoad();
        adapter1 = null;
        recyclerView.setVisibility( View.GONE );
    }

    BizDataAsyncTask<List<OrderModelMedicine>> orderTask;

    private void getOrder(final String data, final String op) {
        orderTask = new BizDataAsyncTask<List<OrderModelMedicine>>() {
            @Override
            protected List<OrderModelMedicine> doExecute() throws ZYException, BizFailure {
                return GoodsBiz.getUserOrder( data, op );
            }

            @Override
            protected void onExecuteSucceeded(List<OrderModelMedicine> orderModelMedicines) {
                if (orderModelMedicines.size() < 1) {
                    adapter1.setEmptyView( noData );
                }

                list = new ArrayList<>();
                list = orderModelMedicines;
                adapter1.setNewData( list );
                adapter1.notifyDataSetChanged();
                closeLoding();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        };
        orderTask.execute();
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
            super( R.layout.item_order_submit_group, list );
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, OrderModelMedicine orderModelMedicine) {
            String status = "";
            if (orderModelMedicine.getSTATUS().equals( "A" )) {
                status = getResources().getString( R.string.daifu );
                baseViewHolder.setVisible( R.id.button1, true )
                        .setVisible( R.id.button2, true )
                        .setVisible( R.id.button3, true )
                        .setText( R.id.button1, R.string.fukuan )
                        .setText( R.id.button2, R.string.order_quxiao )
                        .setText( R.id.button3, R.string.order_lianxi1 )
                        .addOnClickListener( R.id.button1 )
                        .addOnClickListener( R.id.button2 );
            } else if (orderModelMedicine.getSTATUS().equals( "B" )) {
                status = getResources().getString( R.string.daifa );
                baseViewHolder.setVisible( R.id.button1, true )
                        .setVisible( R.id.button2, true )
                        .setVisible( R.id.button3, false )
                        .setText( R.id.button1, R.string.order_lianxi1 )
                        .setText( R.id.button2, R.string.order_chakan )
                        .addOnClickListener( R.id.button1 );
            } else if (orderModelMedicine.getSTATUS().equals( "C" )) {
                status = getResources().getString( R.string.daishou );
                baseViewHolder.setVisible( R.id.button1, true )
                        .setVisible( R.id.button2, true )
                        .setVisible( R.id.button3, true )
                        .setText( R.id.button1, R.string.querenshou )
                        .setText( R.id.button2, R.string.wuliu_chakan )
                        .setText( R.id.button3, R.string.order_lianxi1 )
                        .addOnClickListener( R.id.button1 )
                        .addOnClickListener( R.id.button2 );
            } else if (orderModelMedicine.getSTATUS().equals( "D" )) {
                status = getResources().getString( R.string.daiping );
                baseViewHolder.setVisible( R.id.button1, true )
                        .setVisible( R.id.button2, true )
                        .setVisible( R.id.button3, false )
                        .setText( R.id.button1, R.string.order_pingjia )
                        .setText( R.id.button2, R.string.order_chakan )
                        .addOnClickListener( R.id.button1 );
            } else if (orderModelMedicine.getSTATUS().equals( "S" )) {
                status = getResources().getString( R.string.order_style6 );
                baseViewHolder.setVisible( R.id.button1, true )
                        .setVisible( R.id.button2, true )
                        .setVisible( R.id.button3, false )
                        .setText( R.id.button1, R.string.wuliu_chakan )
                        .setText( R.id.button2, R.string.order_chakan )
                        .addOnClickListener( R.id.button1 );
            } else if (orderModelMedicine.getSTATUS().equals( "E" )) {
                status = getResources().getString( R.string.order_style7 );
                baseViewHolder.setVisible( R.id.button1, true )
                        .setVisible( R.id.button2, false )
                        .setVisible( R.id.button3, false )
                        .setText( R.id.button1, R.string.shanchu )
                        .addOnClickListener( R.id.button1 );
            }

            baseViewHolder.setVisible( R.id.select, false )
                    .setVisible( R.id.style_order, true )
                    .setVisible( R.id.anniu, true )
                    .setText( R.id.textView3, orderModelMedicine.getOID_GROUP_NAME() )
                    .setText( R.id.shuliang, "共计" + orderModelMedicine.getGROUP_EXCHANGE_QUANLITY() + "件商品" )
                    .setText( R.id.xiaoji_money, "￥" + orderModelMedicine.getPAY_AMOUNT() )
                    .setText( R.id.style_order, status );

            RecyclerView goods = baseViewHolder.getView( R.id.goods );
            goods.setLayoutManager( new LinearLayoutManager( getActivity() ) );
            goods.setItemAnimator( new DefaultItemAnimator() );
            goods.setAdapter( new MyAdapter2( orderModelMedicine.getDETAILS() ) );

        }
    }

    //商品adapter
    private class MyAdapter2 extends BaseQuickAdapter<OrderModelMedicine.Goods> {
        public MyAdapter2(List<OrderModelMedicine.Goods> lists) {
            super( R.layout.item_order_submit_goods, lists );
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, OrderModelMedicine.Goods goods) {
            if (StringUtils.isEmpty( goods.getCOMMODITY_COLOR() )) {
                baseViewHolder.setText( R.id.size, " 规格：" + goods.getCOMMODITY_SIZE() );
            } else {
                baseViewHolder.setText( R.id.size, "产地：" + goods.getCOMMODITY_COLOR() + " ;规格：" + goods.getCOMMODITY_SIZE() );
            }
            baseViewHolder.setText( R.id.title, StringUtils.deletaFirst( goods.getCOMMODITY_NAME() ) )
                    .setText( R.id.money, "￥" + goods.getCOST_MONEY() )
                    .setText( R.id.number, "X" + goods.getEXCHANGE_QUANLITY() );

            SimpleDraweeView simpleDraweeView = baseViewHolder.getView( R.id.iv_adapter_list_pic );
            if (!StringUtils.isEmpty( goods.getIMG_PATH() )) {
                simpleDraweeView.setImageURI( Uri.parse( goods.getIMG_PATH() ) );
            }
//
        }
    }

    //在线支付
    BizDataAsyncTask<OnlinePayModel> payTask;

    private void goPay(final String str) {
        payTask = new BizDataAsyncTask<OnlinePayModel>() {
            @Override
            protected OnlinePayModel doExecute() throws ZYException, BizFailure {
                Log.e( "****", str );
                return GoodsBiz.onlinePay( str );
            }

            @Override
            protected void onExecuteSucceeded(OnlinePayModel aliPay) {
                final String orderInfo = aliPay.getPay();
                Log.e( "****", orderInfo );
                if (payModel.getPAYMENT_METHOD().equals( "3" )) {
                    if (!StringUtils.isEmpty( orderInfo )) {
                        Runnable payRunnable = new Runnable() {

                            @Override
                            public void run() {
                                PayTask alipay = new PayTask( getActivity() );
                                Map<String, String> result = alipay.payV2( orderInfo, true );

                                Message msg = new Message();
                                msg.what = ALI_PAY;
                                msg.obj = result;
                                mHandler.sendMessage( msg );
                            }
                        };

                        Thread payThread = new Thread( payRunnable );
                        payThread.start();


                    }
                } else if (payModel.getPAYMENT_METHOD().equals( "4" )) {
                    UPPayAssistEx.startPay( getActivity(), null, null, orderInfo, "01" );
                }

            }

            @Override
            protected void OnExecuteFailed() {
            }
        };
        payTask.execute();
    }


    //取消订单
    BizDataAsyncTask<Integer> cancelTask;

    private void cancelOrder(final String data) {
        cancelTask = new BizDataAsyncTask<Integer>() {
            @Override
            protected Integer doExecute() throws ZYException, BizFailure {
                return GoodsBiz.orderOperation( data, "cancelOrder" );
            }

            @Override
            protected void onExecuteSucceeded(Integer integer) {
                ToastUtil.show( getActivity(), "取消成功" );
                setLoding( getActivity(), false );
                getOrder( "", "myOrders" );
            }

            @Override
            protected void OnExecuteFailed() {

            }
        };
        cancelTask.execute();
    }


    //订单签收
    BizDataAsyncTask<Integer> signTask;

    private void getSignFor(final String data) {
        signTask = new BizDataAsyncTask<Integer>() {
            @Override
            protected Integer doExecute() throws ZYException, BizFailure {
                return GoodsBiz.orderOperation( data, "checkinOrder" );
            }

            @Override
            protected void onExecuteSucceeded(Integer integer) {
                if (integer != 0) {
                    ToastUtil.show( getActivity(), "签收成功" );
                    setLoding( getActivity(), false );
                    getOrder( "", "myOrders" );
                }
            }

            @Override
            protected void OnExecuteFailed() {

            }
        };
        signTask.execute();
    }

    //删除订单
    BizDataAsyncTask<Integer> deleteTask;

    private void delete(final String data) {
        deleteTask = new BizDataAsyncTask<Integer>() {
            @Override
            protected Integer doExecute() throws ZYException, BizFailure {
                return GoodsBiz.orderOperation( data, "delOrder" );
            }

            @Override
            protected void onExecuteSucceeded(Integer integer) {
                ToastUtil.show( getActivity(), "删除成功" );
                setLoding( getActivity(), false );
                getOrder( "", "myOrders" );
            }

            @Override
            protected void OnExecuteFailed() {

            }
        };
        deleteTask.execute();
    }

    //支付结束结果
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage( msg );
            switch (msg.what) {
                case ALI_PAY:
                    PayResult payResult = new PayResult( (Map<String, String>) msg.obj );
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals( resultStatus, "9000" )) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ToastUtil.show( getActivity(), getResources().getString( R.string.pay_success ) );
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtil.show( getActivity(), getResources().getString( R.string.pay_fail ) );
                    }
                    Log.e( "reslut", resultInfo );

                    if (!StringUtils.isEmpty( resultInfo )) {
                        ActivityGoodsSubmit.AliResult aliResult = GsonUtils.Json2Bean( resultInfo, ActivityGoodsSubmit.AliResult.class );
                        aliResult.setORDER_TRADE_ID( payModel.getORDER_TRADE_ID() );

                        String json = GsonUtils.Bean2Json( aliResult );
                        sendResult( json );
                    }

                    break;

                case UP_PAY:


            }

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (data == null) {
            return;
        } else {
            String str = data.getExtras().getString( "pay_result" );
            if (str.equalsIgnoreCase( "success" )) {
                // 支付成功后，extra中如果存在result_data，取出校验
                // result_data结构见c）result_data参数说明
                String result = "";
                if (data.hasExtra( "result_data" )) {
                    result = data.getExtras().getString( "result_data" );
                    Log.e( "result_xxxxx", result );
                    try {
                        JSONObject resultJson = new JSONObject( result );
                        String sign = resultJson.getString( "sign" );
                        String dataOrg = resultJson.getString( "data" );
                        // 验签证书同后台验签证书
                        // 此处的verify，商户需送去商户后台做验签
                        boolean ret = Utils.verify( dataOrg, sign, AppConfig.UPPAY_MODE );
                        if (ret) {
                            // 验证通过后，显示支付结果
                            ToastUtil.show( getActivity(), getResources().getString( R.string.pay_success ) );
                        } else {
                            // 验证不通过后的处理
                            // 建议通过商户后台查询支付结果
                            ToastUtil.show( getActivity(), getResources().getString( R.string.pay_fail ) );
                        }
                    } catch (JSONException e) {
                    }
                } else {
                    // 未收到签名信息
                    // 建议通过商户后台查询支付结果
                    ToastUtil.show( getActivity(), getResources().getString( R.string.pay_success ) );
                }

                if (!StringUtils.isEmpty( result )) {
                    ActivityGoodsSubmit.UnionResult unionResult = GsonUtils.Json2Bean( result, ActivityGoodsSubmit.UnionResult.class );
                    unionResult.setORDER_TRADE_ID( payModel.getORDER_TRADE_ID() );

                    String json = GsonUtils.Bean2Json( unionResult );
                    sendResult( json );
                }

            } else if (str.equalsIgnoreCase( "fail" )) {
                ToastUtil.show( getActivity(), getResources().getString( R.string.pay_fail ) );
            } else if (str.equalsIgnoreCase( "cancel" )) {
                ToastUtil.show( getActivity(), getResources().getString( R.string.pay_cancel ) );
            }


        }
    }


    BizDataAsyncTask<String> payResultTask;

    private void sendResult(final String data) {
        payResultTask = new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                Log.e( "*****", data );
                return GoodsBiz.onlinePayResult( data );
            }

            @Override
            protected void onExecuteSucceeded(String s) {
//                Intent it = new Intent( getActivity(), ActivityOrder.class );
//                getActivity().finish();
//                startActivity( it );
                setLoding( getActivity(), false );
                getOrder( "", "myOrders" );
            }

            @Override
            protected void OnExecuteFailed() {

            }
        };
        payResultTask.execute();
    }

    //提交在线支付的订单
    public class PayModel {
        private String ORDER_TRADE_ID;
        private String PAYMENT_METHOD;

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
    }


}
