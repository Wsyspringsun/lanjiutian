package com.wyw.ljtds.ui.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.AbstractListViewAdapter;
import com.wyw.ljtds.adapter.AbstractViewHolder;
import com.wyw.ljtds.biz.biz.HomeBiz;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.model.RecommendModel;
import com.wyw.ljtds.model.UserGridleModel;
import com.wyw.ljtds.model.UserModel;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.ui.goods.ActivityMedicinesInfo;
import com.wyw.ljtds.ui.user.help.ActivityHelp;
import com.wyw.ljtds.ui.user.manage.ActivityManage;
import com.wyw.ljtds.ui.user.order.ActivityAfterMarket;
import com.wyw.ljtds.ui.user.order.ActivityOrder;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.widget.DividerGridItemDecoration;
import com.wyw.ljtds.widget.MyGridView;
import com.wyw.ljtds.widget.MyScrollView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/9 0009.
 */

@ContentView(R.layout.fragment_user)
public class FragmentUser extends BaseFragment {
    @ViewInject(R.id.gridview)
    private MyGridView gridView;
    @ViewInject(R.id.name)
    private TextView name;
    @ViewInject(R.id.jifen)
    private TextView jifen;
    @ViewInject(R.id.user_img)
    private SimpleDraweeView user_img;
    @ViewInject(R.id.tuijian)
    private RecyclerView tuijian;
    @ViewInject(R.id.scroll_view)
    private MyScrollView scroll_view;
    @ViewInject(R.id.ll_sc)
    private LinearLayout ll_sc;
//    @ViewInject( R.id.user )
//    private RecyclerView user_rec;

    private AbstractListViewAdapter<UserGridleModel> adapter;
    private UserModel user;
    private List<RecommendModel> list;
    //    private List<UserModel> list_user;
    private MyAdapter myAdapter;
//    private UserAdapter userAdapter;

    @Event(value = {R.id.user_img, R.id.order_all, R.id.order_daifu, R.id.order_daishou,
            R.id.order_daiping, R.id.order_shouhou, R.id.zhanghuguanli, R.id.user_xiaoxi, R.id.user_shezhi,
            R.id.all_order})
    private void onclick(View view) {
        if (user!=null){
            Intent it = null;
            switch (view.getId()) {
                case R.id.all_order://所有订单
                    it = new Intent( getActivity(), ActivityOrder.class );
                    it.putExtra( "index", 0 );
                    startActivity( it );
                    break;

                case R.id.order_all://待付
                    it = new Intent( getActivity(), ActivityOrder.class );
                    it.putExtra( "index", 1 );
                    startActivity( it );
                    break;

                case R.id.order_daifu://待发
                    it = new Intent( getActivity(), ActivityOrder.class );
                    it.putExtra( "index", 2 );
                    startActivity( it );
                    break;

                case R.id.order_daishou://待收
                    it = new Intent( getActivity(), ActivityOrder.class );
                    it.putExtra( "index", 3 );
                    startActivity( it );
                    break;
                case R.id.order_daiping://待评
                    it = new Intent( getActivity(), ActivityOrder.class );
                    it.putExtra( "index", 4 );
                    startActivity( it );
                    break;

                case R.id.order_shouhou://售后
                    it = new Intent( getActivity(), ActivityAfterMarket.class );
                    startActivity( it );
                    break;

                case R.id.user_img://用户头像
                case R.id.user_shezhi://设置
                case R.id.zhanghuguanli://账户管理
                    it = new Intent( getActivity(), ActivityManage.class );
                    it.putExtra( "user", user );
                    startActivity( it );
                    break;

                case R.id.user_xiaoxi://消息中心
                    startActivity( new Intent( getActivity(), ActivityMessage.class ) );
                    break;


                default:
                    break;
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );

        setLoding( getActivity(), false );
        getUser();

        List<UserGridleModel> list = new ArrayList<UserGridleModel>();
        for (int i = 0; i < 6; i++) {

        }
        list.add( new UserGridleModel( R.mipmap.icon_user_shoucang, R.string.user_shoucang ) );
        list.add( new UserGridleModel( R.mipmap.icon_user_zuji, R.string.user_zuji ) );
        //list.add(new UserGridleModel(R.mipmap.icon_user_shoucang,R.string.user_dizhi));
        list.add( new UserGridleModel( R.mipmap.icon_user_qianbao, R.string.user_qianbao ) );
        //list.add(new UserGridleModel(R.mipmap.icon_user_shoucang,R.string.user_jifen));
        list.add( new UserGridleModel( R.mipmap.icon_user_bangzhu, R.string.user_bangzhu ) );

        adapter = new AbstractListViewAdapter<UserGridleModel>( getActivity(), R.layout.fragment_user_gridview ) {
            @Override
            public void initListViewItem(AbstractViewHolder viewHolder, UserGridleModel item) {
                TextView tv = viewHolder.getView( R.id.title );
                tv.setText( item.getStr() );
                ImageView img = viewHolder.getView( R.id.img );
                img.setImageResource( item.getImgId() );

            }
        };
        adapter.addItems( list );
        gridView.setAdapter( adapter );
        gridView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it;
                switch (i) {
                    case 0://我的收藏
                        it = new Intent( getActivity(), ActivityCollect.class );
                        startActivity( it );
                        break;

                    case 1://足迹
//                        it = new Intent( getActivity(), ActivityCollect.class );
//                        startActivity( it );
                        break;

                    case 2://我的钱包
                        it = new Intent( getActivity(), ActivityWallet.class );
                        startActivity( it );
                        break;

                    case 3://帮助中心
                        it = new Intent( getActivity(), ActivityHelp.class );
                        startActivity( it );
                        break;
                }
            }
        } );

        tuijian.setLayoutManager( new GridLayoutManager( getActivity(), 2 ) );
        tuijian.setItemAnimator( new DefaultItemAnimator() );
        tuijian.addItemDecoration( new DividerGridItemDecoration( getActivity() ) );

        myAdapter = new MyAdapter();
        tuijian.addOnItemTouchListener( new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent it = new Intent( getActivity(), ActivityMedicinesInfo.class );
                it.putExtra( AppConfig.IntentExtraKey.MEDICINE_INFO_ID, myAdapter.getData().get( i ).getWAREID() );
                startActivity( it );
            }
        } );
        tuijian.setAdapter( myAdapter );

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged( hidden );
        if (hidden) {

        } else {
            if (AppConfig.currSel == 4) {
                getUser();
            }
        }
    }

    BizDataAsyncTask<UserModel> userTask;

    private void getUser() {
        userTask = new BizDataAsyncTask<UserModel>() {
            @Override
            protected UserModel doExecute() throws ZYException, BizFailure {
                return UserBiz.getUser();
            }

            @Override
            protected void onExecuteSucceeded(UserModel userModel) {
                user = userModel;

                jifen.setText( "积分：" + userModel.getUSER_POINT() + "分" );
                name.setText( userModel.getMOBILE() );
                if (StringUtils.isEmpty( userModel.getUSER_ICON_FILE_ID() )) {
                    user_img.setImageURI( Uri.parse( "" ) );
                    user.setUSER_ICON_FILE_ID( "" );
                } else {
                    user_img.setImageURI( Uri.parse( AppConfig.IMAGE_PATH + userModel.getUSER_ICON_FILE_ID() ) );
                    user.setUSER_ICON_FILE_ID( userModel.getUSER_ICON_FILE_ID() );
                }


                getrecommend( PreferenceCache.getToken(), "" );
                closeLoding();

                if (user.getBIRTHDAY()==null){
                    user.setBIRTHDAY( new Long( 0 ) );
                }
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        };
        userTask.execute();
    }

    BizDataAsyncTask<List<RecommendModel>> recTask;

    private void getrecommend(final String token, final String orderid) {
        recTask = new BizDataAsyncTask<List<RecommendModel>>() {
            @Override
            protected List<RecommendModel> doExecute() throws ZYException, BizFailure {
                return HomeBiz.getRecommend( token, orderid );
            }

            @Override
            protected void onExecuteSucceeded(List<RecommendModel> recommendModels) {
                list = recommendModels;
                myAdapter.setNewData( list );
                myAdapter.notifyDataSetChanged();
            }

            @Override
            protected void OnExecuteFailed() {

            }
        };
        recTask.execute();
    }


    private class MyAdapter extends BaseQuickAdapter<RecommendModel> {

        public MyAdapter() {
            super( R.layout.item_goods_grid, list );
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

}
