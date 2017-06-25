package com.wyw.ljtds.ui.user.order;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.model.OrderModelMedicine;
import com.wyw.ljtds.ui.base.LazyLoadFragment;
import com.wyw.ljtds.utils.StringUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.xiaoneng.coreapi.TrailActionBody;
import cn.xiaoneng.uiapi.Ntalker;

/**
 * Created by Administrator on 2017/1/5 0005.
 */

@ContentView(R.layout.fragment_order_list)
public class FragmentOrderList3 extends LazyLoadFragment {
    @ViewInject(R.id.reclcyer)
    private RecyclerView recyclerView;


    //无数据时的界面
    private View noData;
    private MyAdapter1 adapter1;
    private List<OrderModelMedicine> list = new ArrayList<>();
    //父级activity
    private ActivityOrder activity;
    TrailActionBody trailparams = null;// 轨迹信息实例
    //客服
    String settingid1 = "lj_1000_1493167191869";// 客服组id示例kf_9979_1452750735837
    String groupName = "蓝九天";// 客服组默认名称

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach( context );
        activity = (ActivityOrder) context;
    }

    @Override
    protected void lazyLoad() {
        setLoding( getActivity(), false );
        getOrder( "B", "myOrders" );

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
                switch (view.getId()) {
                    case R.id.button1:
//                        Intent it=new Intent( getActivity(),ActivityEvaluate.class );
//                        it.putExtra( "order_id", adapter1.getData().get( i ).getORDER_GROUP_ID() );
//                        it.putExtra( "name", adapter1.getData().get( i ).getOID_GROUP_NAME() );
//                        startActivity( it );
//                        trailparams = new TrailActionBody();
//
//                        trailparams.ttl = "订单页";
//                        trailparams.url = "";
//                        trailparams.sellerid = "lj_1000";
//                        trailparams.ref = "";
//                        trailparams.orderid = adapter1.getData().get( i ).getORDER_GROUP_ID();
//                        trailparams.orderprice = adapter1.getData().get( i ).getPAY_AMOUNT() + "";
//                        trailparams.isvip = 0;
//                        trailparams.userlevel = 1;
//                        trailparams.ntalkerparam = "";
//
//                        int action = Ntalker.getInstance().startAction( trailparams );
//                        Log.e("action  ",action+"; "+adapter1.getData().get( i ).getDETAILS().get( 0 ).getCOMMODITY_ID());
                        activity.openChat( "订单：" + adapter1.getData().get( i ).getORDER_GROUP_ID(), "", settingid1, groupName,
                                true, adapter1.getData().get( i ).getDETAILS().get( 0 ).getCOMMODITY_ID() );

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

    private class MyAdapter1 extends BaseQuickAdapter<OrderModelMedicine> {

        public MyAdapter1() {
            super( R.layout.item_order_submit_group, list );
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, OrderModelMedicine orderModelMedicine) {

            baseViewHolder.setVisible( R.id.select, false )
                    .setVisible( R.id.style_order, true )
                    .setVisible( R.id.anniu, true )
                    .setText( R.id.textView3, orderModelMedicine.getOID_GROUP_NAME() )
                    .setText( R.id.shuliang, "共计" + orderModelMedicine.getGROUP_EXCHANGE_QUANLITY() + "件商品" )
                    .setText( R.id.xiaoji_money, "￥" + orderModelMedicine.getPAY_AMOUNT() )
                    .setText( R.id.style_order, getResources().getString( R.string.daifa ) )
                    .setVisible( R.id.button1, true )
                    .setVisible( R.id.button2, true )
                    .setVisible( R.id.button3,false )
                    .setText( R.id.button1, R.string.order_lianxi1 )
                    .setText( R.id.button2, R.string.order_chakan )
                    .addOnClickListener( R.id.button1 );

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
        }
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

}
