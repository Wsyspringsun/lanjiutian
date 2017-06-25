package com.wyw.ljtds.ui.goods;

import android.app.ProgressDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.PayResult;
import com.alipay.sdk.app.PayTask;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.unionpay.UPPayAssistEx;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.AppManager;
import com.wyw.ljtds.model.Business;
import com.wyw.ljtds.model.CreatOrderModel;
import com.wyw.ljtds.model.Good;
import com.wyw.ljtds.model.GoodCreatModel1;
import com.wyw.ljtds.model.GoodCreatModel2;
import com.wyw.ljtds.model.GoodCreatModel3;
import com.wyw.ljtds.model.OnlinePayModel;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.user.address.ActivityAddressEdit;
import com.wyw.ljtds.ui.user.order.ActivityOrder;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.dialog.BottomDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/24 0024.
 */

@ContentView(R.layout.activity_order_submit)
public class ActivityGoodsSubmit extends BaseActivity {
    @ViewInject(R.id.header_return_text)
    private TextView title;
    @ViewInject(R.id.group)
    private RecyclerView recyclerView;
    @ViewInject(R.id.money)
    private TextView money_all;


    private GoodCreatModel1 model;
    private List<Business> list_business;
    private MyAdapter adapter;
    private String zhifu_s = "";//支付方式
    private String jifen_money = "";//积分抵用金额
    private String jifen = "";
    private String data = "";//从购物车和商品详情页传递来的数据
    private String peisong = "送货上门";
    private String fapiao_flg1 = "不开发票";
    private String fapiao_flg2 = "";
    private String fapiao_flg3 = "";
    private String fapiao_flg4 = "";
    private String address_id = "";
    private Bundle bundle;//地址信息  用于传递到地址编辑activity
    private int choice = 1;//记录在线支付  选中的是哪个选项  默认支付宝
    private PayModel payModel = new PayModel();//在线支付所需参数封装java类
    private ProgressDialog mLoadingDialog = null;

    private static final int ALI_PAY = 1;
    private static final int UP_PAY = 2;


    @Event(value = {R.id.header_return, R.id.submit})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_return:
                finish();
                break;

            case R.id.submit:
                if (model.getPAYMENT_METHOD().equals( "1" )) {
                    setLoding( this, false );
                    payModel.setPAYMENT_METHOD( "1" );
                    submitOrder( GsonUtils.Bean2Json( model ), "create" );
                } else {
                    View view2 = LayoutInflater.from( this ).inflate( R.layout.dialog_pay_select, null );
                    final BottomDialog dialog_pay_select = new BottomDialog( this, view2 ).setCancelable( false ).show();
                    ImageView quxiao = (ImageView) view2.findViewById( R.id.quxiao );
                    TextView money = (TextView) view2.findViewById( R.id.money );
                    money.setText( "需付款：" + money_all.getText() + "元" );
                    LinearLayout select1 = (LinearLayout) view2.findViewById( R.id.select1 );
                    LinearLayout select2 = (LinearLayout) view2.findViewById( R.id.select2 );
                    LinearLayout select3 = (LinearLayout) view2.findViewById( R.id.select3 );
                    final CheckBox checkBox1 = (CheckBox) view2.findViewById( R.id.check1 );
                    final CheckBox checkBox2 = (CheckBox) view2.findViewById( R.id.check2 );
                    final CheckBox checkBox3 = (CheckBox) view2.findViewById( R.id.check3 );
                    Button button = (Button) view2.findViewById( R.id.zhifu );


                    quxiao.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog_pay_select.dissmiss();
                        }
                    } );

                    select1.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkBox1.setChecked( true );
                            checkBox2.setChecked( false );
                            checkBox3.setChecked( false );
                            choice = 0;
                        }
                    } );

                    select2.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkBox1.setChecked( false );
                            checkBox2.setChecked( true );
                            checkBox3.setChecked( false );
                            choice = 1;
                        }
                    } );

                    select3.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkBox1.setChecked( false );
                            checkBox2.setChecked( false );
                            checkBox3.setChecked( true );
                            choice = 2;
                        }
                    } );

                    button.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            switch (choice) {
                                case 0:
                                    payModel.setPAYMENT_METHOD( "2" );
                                    break;

                                case 1:
                                    payModel.setPAYMENT_METHOD( "3" );
                                    break;

                                case 2:
                                    payModel.setPAYMENT_METHOD( "4" );
                                    break;
                            }
                            dialog_pay_select.dissmiss();
                            setLoding( ActivityGoodsSubmit.this, false );
                            submitOrder( GsonUtils.Bean2Json( model ), "create" );

                        }
                    } );
                }

                break;


        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        AppManager.addDestoryActivity( this,"submit" );

        title.setText( R.string.order_queren );

        data = getIntent().getStringExtra( "data" );


        if (!StringUtils.isEmpty( data )) {
            setLoding( this, false );
            showOrder( data, "showOrder" );
        }


        recyclerView.setLayoutManager( new LinearLayoutManager( this ) );
        recyclerView.setItemAnimator( new DefaultItemAnimator() );
        adapter = new MyAdapter();
        adapter.addHeaderView( getHeaderView( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent( ActivityGoodsSubmit.this, ActivityAddressEdit.class );
                it.putExtra( AppConfig.IntentExtraKey.ADDRESS_FROM, 3 );

                if (!StringUtils.isEmpty( address_id )) {
                    it.putExtra( "address_id", address_id );
                }
                if (bundle != null) {
                    it.putExtra( "bundle", bundle );
                }
                Log.e( "address_id", address_id );
                startActivityForResult( it, 2 );
            }
        } ) );
        adapter.addFooterView( getFooterView( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent( ActivityGoodsSubmit.this, ActivityGoodsSubmitChoice.class );
                it.putExtra( "pay", zhifu_s );
                it.putExtra( "jifen_money", jifen_money );
                it.putExtra( "jifen", jifen );

                startActivityForResult( it, 0 );
            }
        } ) );
        recyclerView.addOnItemTouchListener( new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                switch (view.getId()) {
                    case R.id.select:
                        Intent it = new Intent( ActivityGoodsSubmit.this, ActivityGoodsSubmitBill.class );
                        it.putExtra( "possition", i );
                        it.putExtra( "fapiao_flg1", adapter.getData().get( i ).getINVOICE_FLG() );
                        it.putExtra( "fapiao_flg2", adapter.getData().get( i ).getINVOICE_TYPE() );
                        it.putExtra( "fapiao_flg3", adapter.getData().get( i ).getINVOICE_TITLE() );
                        it.putExtra( "fapiao_flg4", adapter.getData().get( i ).getINVOICE_CONTENT() );
                        it.putExtra( "peisong", adapter.getData().get( i ).getDISTRIBUTION_MODE() );

                        startActivityForResult( it, 1 );
                }
            }
        } );

        recyclerView.setAdapter( adapter );

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void resumeFromOther() {
        setLoding( this, false );
        showOrder( data, "showOrder" );
    }

    private View getHeaderView(View.OnClickListener listener) {
        View view = getLayoutInflater().inflate( R.layout.item_order_submit_address, (ViewGroup) recyclerView.getParent(), false );

        view.setOnClickListener( listener );

        return view;
    }

    private View getFooterView(View.OnClickListener listener) {
        View view = getLayoutInflater().inflate( R.layout.item_order_submit_bottom, (ViewGroup) recyclerView.getParent(), false );

        view.setOnClickListener( listener );

        return view;
    }

    private BizDataAsyncTask<String> submitTask;

    private void submitOrder(final String str, final String op) {
        submitTask = new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                Log.e( "*****", str );
                return GoodsBiz.submitOrder( str, op );
            }

            @Override
            protected void onExecuteSucceeded(String s) {
                Log.e( "***", payModel.getPAYMENT_METHOD() );
                if (payModel.getPAYMENT_METHOD().equals( "1" )) {
                    Intent it = new Intent( ActivityGoodsSubmit.this, ActivityOrder.class );
                    finish();
                    startActivity( it );
                } else if (payModel.getPAYMENT_METHOD().equals( "3" ) || payModel.getPAYMENT_METHOD().equals( "4" )) {
                    if (!StringUtils.isEmpty( s )) {
                        payModel.setORDER_TRADE_ID( s );

                        goPay( GsonUtils.Bean2Json( payModel ) );
                    }
                }
                closeLoding();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        };
        submitTask.execute();
    }

    private BizDataAsyncTask<CreatOrderModel> orderTask;

    private void showOrder(final String str, final String op) {
        orderTask = new BizDataAsyncTask<CreatOrderModel>() {
            @Override
            protected CreatOrderModel doExecute() throws ZYException, BizFailure {
                Log.e( "*****", str );
                return GoodsBiz.getOrderShow( str, op );
            }

            @Override
            protected void onExecuteSucceeded(CreatOrderModel creatOrderModel) {
                list_business = new ArrayList<>();

                model = new GoodCreatModel1();
                model.setCOST_POINT( creatOrderModel.getCOST_POINT() );
                model.setPAYMENT_METHOD( creatOrderModel.getPAYMENT_METHOD() );

                List<GoodCreatModel2> lists_group = new ArrayList<>();

                for (int i = 0; i < creatOrderModel.getDETAILS().size(); i++) {
                    GoodCreatModel2 goodCreatModel2 = new GoodCreatModel2();
                    Business business = creatOrderModel.getDETAILS().get( i );
                    goodCreatModel2.setOID_GROUP_NAME( business.getOID_GROUP_NAME() );
                    goodCreatModel2.setOID_GROUP_ID( business.getOID_GROUP_ID() );
                    goodCreatModel2.setDISTRIBUTION_MODE( business.getDISTRIBUTION_MODE() );
                    goodCreatModel2.setINVOICE_CONTENT( business.getINVOICE_CONTENT() );
                    goodCreatModel2.setINVOICE_FLG( business.getINVOICE_FLG() );
                    goodCreatModel2.setINVOICE_TITLE( business.getINVOICE_TITLE() );
                    goodCreatModel2.setINVOICE_TYPE( business.getINVOICE_TYPE() );

                    List<GoodCreatModel3> lists_good = new ArrayList<>();
                    List<Good> goods = business.getDETAILS();
                    for (int j = 0; j < goods.size(); j++) {
                        Good good = goods.get( j );
                        GoodCreatModel3 goodCreatModel3 = new GoodCreatModel3();
                        goodCreatModel3.setCOMMODITY_COLOR( good.getCOMMODITY_COLOR() );
                        goodCreatModel3.setCOMMODITY_ID( good.getCOMMODITY_ID() );
                        goodCreatModel3.setCOMMODITY_COLOR_ID( good.getCOMMODITY_COLOR_ID() );
                        goodCreatModel3.setCOMMODITY_SIZE_ID( good.getCOMMODITY_SIZE_ID() );
                        goodCreatModel3.setCOMMODITY_NAME( good.getCOMMODITY_NAME() );
                        goodCreatModel3.setCOMMODITY_SIZE( good.getCOMMODITY_SIZE() );
                        goodCreatModel3.setEXCHANGE_QUANLITY( Integer.parseInt( good.getEXCHANGE_QUANLITY() ) );
                        goodCreatModel3.setCOMMODITY_ORDER_ID( good.getCOMMODITY_ORDER_ID() );

                        lists_good.add( goodCreatModel3 );
                    }

                    goodCreatModel2.setDETAILS( lists_good );
                    lists_group.add( goodCreatModel2 );
                }

                model.setDETAILS( lists_group );


                list_business = creatOrderModel.getDETAILS();
                if (creatOrderModel.getPAYMENT_METHOD().equals( "0" )) {
                    zhifu_s = "在线支付";
                } else if (creatOrderModel.getPAYMENT_METHOD().equals( "1" )) {
                    zhifu_s = "货到付款";
                }

                //--记录发送数据
                jifen_money = creatOrderModel.getPOINT_MONEY();
                jifen = creatOrderModel.getUSER_POINT();
                address_id = creatOrderModel.getUSER_ADDRESS().getADDRESS_ID() + "";
                bundle = new Bundle();
                bundle.putString( "id_p", creatOrderModel.getUSER_ADDRESS().getCONSIGNEE_PROVINCE() + "" );
                bundle.putString( "id_c", creatOrderModel.getUSER_ADDRESS().getCONSIGNEE_CITY() + "" );
                bundle.putString( "name", creatOrderModel.getUSER_ADDRESS().getCONSIGNEE_NAME() );
                bundle.putString( "phone", creatOrderModel.getUSER_ADDRESS().getCONSIGNEE_MOBILE() );
                bundle.putString( "xiangxi", creatOrderModel.getUSER_ADDRESS().getCONSIGNEE_ADDRESS() );
                bundle.putString( "shengshi", creatOrderModel.getUSER_ADDRESS().getPROVINCE() + creatOrderModel.getUSER_ADDRESS().getCITY() );
                //--


                ((TextView) adapter.getHeaderLayout().findViewById( R.id.name )).setText( creatOrderModel.getUSER_ADDRESS().getCONSIGNEE_NAME() );
                ((TextView) adapter.getHeaderLayout().findViewById( R.id.phone )).setText( creatOrderModel.getUSER_ADDRESS().getCONSIGNEE_MOBILE() );
                ((TextView) adapter.getHeaderLayout().findViewById( R.id.shouhuo_dizhi )).setText( creatOrderModel.getUSER_ADDRESS().getADDRESS_DETAIL() );
                ((TextView) adapter.getFooterLayout().findViewById( R.id.zhifu_jieguo )).setText( zhifu_s );
                ((TextView) adapter.getFooterLayout().findViewById( R.id.jifen_jieguo )).setText( jifen_money );
                money_all.setText( "￥" + creatOrderModel.getPAY_AMOUNT() );

                adapter.setNewData( list_business );
                adapter.notifyDataSetChanged();

                closeLoding();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        };
        orderTask.execute();
    }

    //店铺adapter
    private class MyAdapter extends BaseQuickAdapter<Business> {

        public MyAdapter() {
            super( R.layout.item_order_submit_group, list_business );
        }


        @Override
        protected void convert(BaseViewHolder baseViewHolder, Business group) {
            String fapiao1 = "";
            String peisong = "";
            if (group.getINVOICE_FLG().equals( "0" )) {
                fapiao1 = "不开发票";
            } else {
                if (group.getINVOICE_CONTENT().equals( "0" )) {
                    fapiao1 = "明细";
                } else if (group.getINVOICE_CONTENT().equals( "1" )) {
                    fapiao1 = "办公用品";
                } else if (group.getINVOICE_CONTENT().equals( "2" )) {
                    fapiao1 = "家居用品";
                } else if (group.getINVOICE_CONTENT().equals( "3" )) {
                    fapiao1 = "药品";
                } else {
                    fapiao1 = "耗材";
                }
            }

            if (group.getDISTRIBUTION_MODE().equals( "0" )) {
                peisong = "送货上门（运费：￥" + group.getPOSTAGE() + "）";
            } else if (group.getDISTRIBUTION_MODE().equals( "1" )) {
                peisong = "门店自取";
            }

            baseViewHolder.addOnClickListener( R.id.select )
                    .setText( R.id.textView3, group.getOID_GROUP_NAME() )
                    .setText( R.id.peisong_xuanze, peisong )
                    .setText( R.id.shuliang, "共计" + group.getGROUP_EXCHANGE_QUANLITY() + "件商品" )
//                    .setText( R.id.fapiao_xuanze, group.getFapiao1()+group.getFapiao2()+group.getFapiao3() )
                    .setText( R.id.fapiao_xuanze, fapiao1 )
                    .setText( R.id.xiaoji_money, "￥" + group.getGROUP_MONEY_ALL() );


            RecyclerView goods = baseViewHolder.getView( R.id.goods );
            goods.setLayoutManager( new LinearLayoutManager( ActivityGoodsSubmit.this ) );
            goods.setItemAnimator( new DefaultItemAnimator() );
            goods.setAdapter( new MyAdapter1( group.getDETAILS() ) );

        }
    }

    //商品adapter
    private class MyAdapter1 extends BaseQuickAdapter<Good> {
        public MyAdapter1(List<Good> lists) {
            super( R.layout.item_order_submit_goods, lists );
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, Good goods) {
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
        }
    }

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
                                PayTask alipay = new PayTask( ActivityGoodsSubmit.this );
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
//                    Runnable payRunnable = new Runnable() {
//
//                        @Override
//                        public void run() {
                    Log.e( "+++++", orderInfo );
//                    mLoadingDialog = ProgressDialog.show(ActivityGoodsSubmit.this, // context
//                            "", // title
//                            "正在努力加载,请稍候...", // message
//                            true); // 进度是否是不确定的，这只和创建进度条有关

                    UPPayAssistEx.startPay( ActivityGoodsSubmit.this, null, null, orderInfo, "00" );

//                        }
//                    };
//
//                    Thread payThread = new Thread( payRunnable );
//                    payThread.start();
                }

            }

            @Override
            protected void OnExecuteFailed() {
            }
        };
        payTask.execute();
    }

    //更改数据  刷新页面
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == AppConfig.IntentExtraKey.RESULT_OK) {
            switch (requestCode) {
                case 0:
                    zhifu_s = data.getStringExtra( "pay" );
                    jifen_money = data.getStringExtra( "jifen_money" );
                    jifen = data.getStringExtra( "jifen" );


                    model.setCOST_POINT( jifen );
                    if (zhifu_s.equals( "在线支付" )) {
                        model.setPAYMENT_METHOD( "0" );
                    } else if (zhifu_s.equals( "货到付款" )) {
                        model.setPAYMENT_METHOD( "1" );
                    }

                    String str = GsonUtils.Bean2Json( model );
                    setLoding( this, false );
                    showOrder( str, "changeOrder" );

                    break;

                case 1:
                    int index = data.getIntExtra( "possition", 0 );
                    peisong = data.getStringExtra( "peisong" );
                    fapiao_flg1 = data.getStringExtra( "fapiao_flg1" );
                    fapiao_flg2 = data.getStringExtra( "fapiao_flg2" );
                    fapiao_flg3 = data.getStringExtra( "fapiao_flg3" );
                    fapiao_flg4 = data.getStringExtra( "fapiao_flg4" );

                    model.getDETAILS().get( index ).setINVOICE_FLG( fapiao_flg1 );
                    model.getDETAILS().get( index ).setINVOICE_TYPE( fapiao_flg2 );
                    model.getDETAILS().get( index ).setINVOICE_TITLE( fapiao_flg3 );
                    model.getDETAILS().get( index ).setINVOICE_CONTENT( fapiao_flg4 );
                    model.getDETAILS().get( index ).setDISTRIBUTION_MODE( peisong );

                    String str1 = GsonUtils.Bean2Json( model );
                    setLoding( this, false );
                    showOrder( str1, "changeOrder" );

                    break;

                case 2:
                    Log.e( "bbbbbbbbbbbbbbb","bbbbbbbbbbbbbbbb" );
                    boolean is = data.getBooleanExtra( "refresh", false );
                    if (is){
                        String str2 = GsonUtils.Bean2Json( model );
                        setLoding( this, false );
                        showOrder( str2, "changeOrder" );
                    }
                    break;
            }
        } else {
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
                                ToastUtil.show( ActivityGoodsSubmit.this, getResources().getString( R.string.pay_success ) );
                            } else {
                                // 验证不通过后的处理
                                // 建议通过商户后台查询支付结果
                                ToastUtil.show( ActivityGoodsSubmit.this, getResources().getString( R.string.pay_fail ) );
                            }
                        } catch (JSONException e) {
                        }
                    } else {
                        // 未收到签名信息
                        // 建议通过商户后台查询支付结果
                        ToastUtil.show( ActivityGoodsSubmit.this, getResources().getString( R.string.pay_success ) );
                    }

                    if (!StringUtils.isEmpty( result )) {
                        UnionResult unionResult = GsonUtils.Json2Bean( result, UnionResult.class );
                        unionResult.setORDER_TRADE_ID( payModel.getORDER_TRADE_ID() );

                        String json = GsonUtils.Bean2Json( unionResult );
                        sendResult( json );
                    }

                } else if (str.equalsIgnoreCase( "fail" )) {
                    ToastUtil.show( ActivityGoodsSubmit.this, getResources().getString( R.string.pay_fail ) );
                    toOrder();
                } else if (str.equalsIgnoreCase( "cancel" )) {
                    ToastUtil.show( ActivityGoodsSubmit.this, getResources().getString( R.string.pay_cancel ) );
                    toOrder();
                }

            }
        }


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
                        ToastUtil.show( ActivityGoodsSubmit.this, getResources().getString( R.string.pay_success ) );
                        Log.e( "reslut", resultInfo );
                        if (!StringUtils.isEmpty( resultInfo )) {
                            AliResult aliResult = GsonUtils.Json2Bean( resultInfo, AliResult.class );
                            aliResult.setORDER_TRADE_ID( payModel.getORDER_TRADE_ID() );

                            String json = GsonUtils.Bean2Json( aliResult );
                            sendResult( json );
                        }

                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtil.show( ActivityGoodsSubmit.this, getResources().getString( R.string.pay_fail ) );
                        toOrder();
                    }


                    break;

                case UP_PAY:


            }

        }
    };

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
                toOrder();
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


    //跳转到我的订单
    private void toOrder() {
        Intent it = new Intent( ActivityGoodsSubmit.this, ActivityOrder.class );
        finish();
        startActivity( it );
    }


    //银联支付回执
    public class UnionResult {
        private String sign;
        private String data;
        private String ORDER_TRADE_ID;

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getORDER_TRADE_ID() {
            return ORDER_TRADE_ID;
        }

        public void setORDER_TRADE_ID(String ORDER_TRADE_ID) {
            this.ORDER_TRADE_ID = ORDER_TRADE_ID;
        }
    }


    //支付宝支付回执
    public class AliResult {
        private alipay_trade alipay_trade_app_pay_response;
        private String sign;
        private String ORDER_TRADE_ID;
        private String sign_type;

        public alipay_trade getAlipay_trade_app_pay_response() {
            return alipay_trade_app_pay_response;
        }

        public void setAlipay_trade_app_pay_response(alipay_trade alipay_trade_app_pay_response) {
            this.alipay_trade_app_pay_response = alipay_trade_app_pay_response;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getORDER_TRADE_ID() {
            return ORDER_TRADE_ID;
        }

        public void setORDER_TRADE_ID(String ORDER_TRADE_ID) {
            this.ORDER_TRADE_ID = ORDER_TRADE_ID;
        }

        public String getSign_type() {
            return sign_type;
        }

        public void setSign_type(String sign_type) {
            this.sign_type = sign_type;
        }

        private class alipay_trade {
            private String code;
            private String msg;
            private String app_id;
            private String auth_app_id;
            private String charset;
            private String timestamp;
            private String total_amount;
            private String trade_no;
            private String seller_id;
            private String out_trade_no;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public String getApp_id() {
                return app_id;
            }

            public void setApp_id(String app_id) {
                this.app_id = app_id;
            }

            public String getAuth_app_id() {
                return auth_app_id;
            }

            public void setAuth_app_id(String auth_app_id) {
                this.auth_app_id = auth_app_id;
            }

            public String getCharset() {
                return charset;
            }

            public void setCharset(String charset) {
                this.charset = charset;
            }

            public String getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(String timestamp) {
                this.timestamp = timestamp;
            }

            public String getTotal_amount() {
                return total_amount;
            }

            public void setTotal_amount(String total_amount) {
                this.total_amount = total_amount;
            }

            public String getOut_trade_no() {
                return out_trade_no;
            }

            public void setOut_trade_no(String out_trade_no) {
                this.out_trade_no = out_trade_no;
            }

            public String getSeller_id() {
                return seller_id;
            }

            public void setSeller_id(String seller_id) {
                this.seller_id = seller_id;
            }

            public String getTrade_no() {
                return trade_no;
            }

            public void setTrade_no(String trade_no) {
                this.trade_no = trade_no;
            }
        }

    }

}

