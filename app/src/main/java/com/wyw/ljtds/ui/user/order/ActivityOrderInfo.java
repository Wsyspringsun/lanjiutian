package com.wyw.ljtds.ui.user.order;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.AppManager;
import com.wyw.ljtds.model.OrderModelInfoMedicine;
import com.wyw.ljtds.model.RecommendModel;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.goods.ActivityMedicinesInfo;
import com.wyw.ljtds.utils.DateUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.widget.DividerGridItemDecoration;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2017/1/10 0010.
 */

@ContentView(R.layout.activity_order_info)
public class ActivityOrderInfo extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    @ViewInject(R.id.header_return_text)
    private TextView title;
    @ViewInject(R.id.reclcyer)
    private RecyclerView recyclerView;
    @ViewInject(R.id.name)
    private TextView address_name;
    @ViewInject(R.id.phone)
    private TextView address_phone;
    @ViewInject(R.id.shouhuo_dizhi)
    private TextView address_dizhi;
    @ViewInject(R.id.dianpu)
    private TextView dianpu;
    @ViewInject(R.id.yunfei)
    private TextView yunfei;
    @ViewInject(R.id.zongjia)
    private TextView zongjia;
    @ViewInject(R.id.dingdan)
    private TextView dingdan;
    @ViewInject(R.id.dingdan2)
    private TextView dingdan2;
    @ViewInject(R.id.dingdan1)
    private TextView dingdan1;
    @ViewInject(R.id.fapiao)
    private TextView fapiao;
    @ViewInject(R.id.shangp_z)
    private TextView shangp_z;
    @ViewInject(R.id.jifen_d)
    private TextView jifen_d;
    @ViewInject(R.id.images)
    private ImageView images;
    @ViewInject(R.id.order_wuliu)
    private TextView order_wuliu;
    @ViewInject(R.id.order_quxiao)
    private TextView order_quxiao;
    @ViewInject(R.id.tuijian)
    private RecyclerView tuijian;
    @ViewInject( R.id.tuijian_ll )
    private LinearLayout tuijian_ll;

    private List<OrderModelInfoMedicine.Goods> list;
    private MyAdapter adapter;
    private List<RecommendModel> recommends;
    private MyAdapter1 adapter1;
    private String orderId = "";
    private String name = "";
    private String image="";
    private String trade_id = "";
    private String group_id="";
    private static final int REQUEST_CODE_PERMISSION_CALL_PHONE = 1;
    private String order_status = "";
    private String phone = "";
    //客服
    String settingid0 = "lj_1000_1493167191869";
    String settingid1="lj_1001_1496308413541";
    String groupName = "蓝九天";// 客服组默认名称

    @Event(value = {R.id.header_return, R.id.order_quxiao, R.id.order_fuwu, R.id.order_wuliu, R.id.lianxi, R.id.boda,
            R.id.order_shanchu})
    private void onClick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.header_return:
                finish();
                break;

            case R.id.order_quxiao:
                if (order_status.equals( "D" )) {
                    it = new Intent( ActivityOrderInfo.this, ActivityEvaluate.class );
                    it.putExtra( "order_id", orderId );
                    it.putExtra( "name", name );
                    it.putExtra( "image", image );
                    startActivity( it );
                }

                break;

            case R.id.order_fuwu:
                startActivity( new Intent( ActivityOrderInfo.this, ActivityAfterMarket.class ) );
                break;

            case R.id.order_wuliu:
                if (order_status.equals( "C" ) || order_status.equals( "D" ) || order_status.equals( "S" )) {
                    it = new Intent( ActivityOrderInfo.this, ActivityLogistics.class );
                    it.putExtra( "order_id", orderId );
                    startActivity( it );
                }

                break;

            case R.id.lianxi:
//                openChat( model.getTitle(), "", settingid1, groupName, true, model.getCommodityId() );
                if (!StringUtils.isEmpty( group_id )&&group_id.equals( "sxljt" )){
                    openChat( "交易订单号：" + trade_id, "", settingid0, groupName, false, "" );
                }else if (!StringUtils.isEmpty( group_id )){
                    openChat( "交易订单号：" + trade_id, "", settingid1, groupName, false, "" );
                }

                break;

            case R.id.boda:
                if (StringUtils.isEmpty( phone )) {

                } else {
                    call();
                }


                break;

            case R.id.order_shanchu:
                if (!StringUtils.isEmpty( trade_id )) {
                    delete( trade_id );
                }
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        title.setText( R.string.order_xiangqing );

        tuijian_ll.setVisibility( View.GONE );

        setLoding( this, false );
        getInfo( getIntent().getStringExtra( "id" ), "orderDetail" );

        adapter = new MyAdapter();
        recyclerView.setLayoutManager( new LinearLayoutManager( this ) );
        recyclerView.setItemAnimator( new DefaultItemAnimator() );
        recyclerView.setAdapter( adapter );

        tuijian.setLayoutManager( new GridLayoutManager( this, 2 ) );
        tuijian.setItemAnimator( new DefaultItemAnimator() );
        tuijian.addItemDecoration( new DividerGridItemDecoration( this ) );

        adapter1 = new MyAdapter1();
        tuijian.addOnItemTouchListener( new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent it = new Intent( ActivityOrderInfo.this, ActivityMedicinesInfo.class );
                it.putExtra( AppConfig.IntentExtraKey.MEDICINE_INFO_ID, adapter1.getData().get( i ).getWAREID() );
                startActivity( it );
            }
        } );
        tuijian.setAdapter( adapter1 );

    }


    BizDataAsyncTask<OrderModelInfoMedicine> infoTask;

    private void getInfo(final String data, final String op) {
        infoTask = new BizDataAsyncTask<OrderModelInfoMedicine>() {
            @Override
            protected OrderModelInfoMedicine doExecute() throws ZYException, BizFailure {
                Log.e( "****", data );
                return GoodsBiz.getOrderInfo( data, op );
            }

            @Override
            protected void onExecuteSucceeded(OrderModelInfoMedicine orderModelInfoMedicine) {
                address_name.setText( orderModelInfoMedicine.getUSER_ADDRESS().getCONSIGNEE_NAME() );
                address_phone.setText( orderModelInfoMedicine.getUSER_ADDRESS().getCONSIGNEE_MOBILE() );
                address_dizhi.setText( orderModelInfoMedicine.getUSER_ADDRESS_ID() );

                dianpu.setText( orderModelInfoMedicine.getOID_GROUP_NAME() );
                yunfei.setText( "￥" + orderModelInfoMedicine.getPOSTAGE() + "" );
                dingdan.setText( "订单号：" + orderModelInfoMedicine.getORDER_TRADE_ID() );
                dingdan1.setText( "创建时间：" + DateUtils.parseTime( orderModelInfoMedicine.getCREATE_DATE() + "" ) );
                shangp_z.setText( "￥" + orderModelInfoMedicine.getGROUP_MONEY_ALL() + "" );
                zongjia.setText( "￥" + orderModelInfoMedicine.getPAY_AMOUNT() );
                jifen_d.setText( "￥" + orderModelInfoMedicine.getPOINT_MONEY() );
                if (orderModelInfoMedicine.getCOMPLETE_DATE() != null) {
                    dingdan2.setVisibility( View.VISIBLE );
                    dingdan2.setText( "完成时间：" + DateUtils.parseTime( orderModelInfoMedicine.getCOMPLETE_DATE() + "" ) );
                } else {
                    dingdan2.setVisibility( View.GONE );
                }
                if (!StringUtils.isEmpty( orderModelInfoMedicine.getINVOICE_TITLE() )) {
                    fapiao.setVisibility( View.VISIBLE );
                    fapiao.setText( "发票抬头：" + orderModelInfoMedicine.getINVOICE_TITLE() );
                } else {
                    fapiao.setVisibility( View.GONE );
                }

                switch (orderModelInfoMedicine.getSTATUS()) {
                    case "A":
                        images.setImageDrawable( getResources().getDrawable( R.mipmap.daifukuan ) );
                        order_wuliu.setVisibility( View.GONE );
                        break;

                    case "B":
                        images.setImageDrawable( getResources().getDrawable( R.mipmap.daifahuo ) );
                        order_wuliu.setVisibility( View.GONE );
                        break;

                    case "C":
                        images.setImageDrawable( getResources().getDrawable( R.mipmap.daishouhuo ) );
                        break;

                    case "S":
                        images.setImageDrawable( getResources().getDrawable( R.mipmap.daipingjia ) );
                        order_quxiao.setVisibility( View.GONE );
                        break;
                    case "D":
                        images.setImageDrawable( getResources().getDrawable( R.mipmap.daipingjia ) );
                        break;

                    case "E":
                        images.setImageDrawable( getResources().getDrawable( R.mipmap.guanbi ) );
                        break;

                }

                list = orderModelInfoMedicine.getDETAILS();
                adapter.setNewData( list );
                adapter.notifyDataSetChanged();

                closeLoding();


                orderId = orderModelInfoMedicine.getORDER_GROUP_ID();
                name = orderModelInfoMedicine.getOID_GROUP_NAME();
                trade_id = orderModelInfoMedicine.getORDER_TRADE_ID();
                order_status = orderModelInfoMedicine.getSTATUS();
                phone = orderModelInfoMedicine.getCONTACT_TEL();
                image=orderModelInfoMedicine.getDETAILS().get( 0 ).getIMG_PATH();
                group_id=orderModelInfoMedicine.getORDER_GROUP_ID();

//                getrecommend( PreferenceCache.getToken(), trade_id );

            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        };
        infoTask.execute();
    }

    BizDataAsyncTask<Integer> deleteTask;

    private void delete(final String data) {
        deleteTask = new BizDataAsyncTask<Integer>() {
            @Override
            protected Integer doExecute() throws ZYException, BizFailure {
                Log.e( "trade_id", data );
                return GoodsBiz.orderOperation( data, "delOrder" );
            }

            @Override
            protected void onExecuteSucceeded(Integer integer) {
                if (integer == 3) {
                    ToastUtil.show( ActivityOrderInfo.this, getResources().getString( R.string.delete_succeed ) );
                    Intent it = new Intent( ActivityOrderInfo.this, ActivityOrder.class );
                    if (order_status.equals( "A" )) {
                        it.putExtra( "index", 1 );
                    } else if (order_status.equals( "B" )) {
                        it.putExtra( "index", 2 );
                    } else if (order_status.equals( "C" )) {
                        it.putExtra( "index", 3 );
                    } else if (order_status.equals( "D" )) {
                        it.putExtra( "index", 4 );
                    } else {
                        it.putExtra( "index", 0 );
                    }
                    Log.e( "asdasdasda", order_status );
                    finish();
                    AppManager.destoryActivity( "order" );
                    startActivity( it );
                }
            }

            @Override
            protected void OnExecuteFailed() {

            }
        };
        deleteTask.execute();
    }


    BizDataAsyncTask<List<RecommendModel>> recTask;

//    private void getrecommend(final String token, final String orderid) {
//        recTask = new BizDataAsyncTask<List<RecommendModel>>() {
//            @Override
//            protected List<RecommendModel> doExecute() throws ZYException, BizFailure {
//                return HomeBiz.getRecommend( token, orderid );
//            }
//
//            @Override
//            protected void onExecuteSucceeded(List<RecommendModel> recommendModels) {
//                recommends = recommendModels;
//                adapter1.setNewData( recommends );
//                adapter1.notifyDataSetChanged();
//            }
//
//            @Override
//            protected void OnExecuteFailed() {
//
//            }
//        };
//        recTask.execute();
//    }


    private class MyAdapter1 extends BaseQuickAdapter<RecommendModel> {

        public MyAdapter1() {
            super( R.layout.item_goods_grid, recommends );
        }


        @Override
        protected void convert(BaseViewHolder baseViewHolder, RecommendModel recommendModel) {
            baseViewHolder.setText( R.id.goods_title, StringUtils.deletaFirst( recommendModel.getWARENAME() ) )
                    .setText( R.id.money, recommendModel.getSALEPRICE() + "" );

            SimpleDraweeView goods_img = baseViewHolder.getView( R.id.item_head_img );
            if (StringUtils.isEmpty( recommendModel.getIMG_PATH() )) {
                goods_img.setImageURI( Uri.parse( "" ) );
            } else {
                goods_img.setImageURI( Uri.parse( recommendModel.getIMG_PATH() ) );
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        EasyPermissions.onRequestPermissionsResult( requestCode, permissions, grantResults, this );
    }

    //权限成功
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.e( "camera", "成功获取权限" );
    }

    //权限失败
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

        if (EasyPermissions.somePermissionPermanentlyDenied( this, perms )) {
            new AppSettingsDialog.Builder( this, "需要开启此权限" )
                    .setTitle( getString( R.string.alert_tishi ) )
                    .setPositiveButton( "设置" )
                    .setNegativeButton( "取消", null )
                    .setRequestCode( 100 )
                    .build()
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == 100) {
            Log.e( "*****", "权限获得" );
        }
    }


    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_CALL_PHONE)
    private void call() {
        if (EasyPermissions.hasPermissions( this, Manifest.permission.CALL_PHONE )) {
            Log.e( "****", "是否有权限" + EasyPermissions.hasPermissions( this, Manifest.permission.CALL_PHONE ) + "" );
            AlertDialog.Builder builder = new AlertDialog.Builder( ActivityOrderInfo.this );
            builder.setMessage( "是否拨打电话：" + phone );
            builder.setNegativeButton( "取消", null );
            builder.setPositiveButton( "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent();
                    intent.setAction( Intent.ACTION_CALL );
                    intent.setData( Uri.parse( "tel:" + phone ) );
                    //开启系统拨号器
                    startActivity( intent );
                }
            } );
            builder.create().show();
        } else {
            EasyPermissions.requestPermissions( this, "需要以下权限:拨打电话。", REQUEST_CODE_PERMISSION_CALL_PHONE, Manifest.permission.CALL_PHONE );
        }
    }

    //商品adapter
    private class MyAdapter extends BaseQuickAdapter<OrderModelInfoMedicine.Goods> {
        public MyAdapter() {
            super( R.layout.item_orderinfor_goods, list );
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, final OrderModelInfoMedicine.Goods goods) {
            if (StringUtils.isEmpty( goods.getCOMMODITY_COLOR() )) {
                baseViewHolder.setText( R.id.size, " 规格：" + goods.getCOMMODITY_SIZE() );
            } else {
                baseViewHolder.setText( R.id.size, "产地：" + goods.getCOMMODITY_COLOR() + " ;规格：" + goods.getCOMMODITY_SIZE() );
            }
            baseViewHolder.setText( R.id.title, StringUtils.deletaFirst( goods.getCOMMODITY_NAME() ) )
                    .setText( R.id.money, "￥" + goods.getCOST_MONEY() )
                    .setText( R.id.number, "X" + goods.getEXCHANGE_QUANLITY() )
                    .setOnClickListener(R.id.shouhou, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent it=new Intent(ActivityOrderInfo.this,ActivityAfterMarket.class);
                            it.putExtra("good_id",goods.getCOMMODITY_ORDER_ID());
                            it.putExtra("good_money",goods.getCOST_MONEY()+"");
                            startActivity(it);
                        }
                    });

            SimpleDraweeView simpleDraweeView = baseViewHolder.getView( R.id.iv_adapter_list_pic );
            if (!StringUtils.isEmpty( goods.getIMG_PATH() )) {
                simpleDraweeView.setImageURI( Uri.parse( goods.getIMG_PATH() ) );
            }
        }
    }
}
