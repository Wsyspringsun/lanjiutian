package com.wyw.ljtds.ui.user;

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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.AppManager;
import com.wyw.ljtds.model.SqlFavoritesModel;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.goods.ActivityGoodsInfo;
import com.wyw.ljtds.ui.goods.ActivityMedicinesInfo;
import com.wyw.ljtds.utils.SqlUtils;
import com.wyw.ljtds.widget.RecycleViewDivider;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/28 0028.
 */
@ContentView(R.layout.activity_collect)
public class ActivityFavorite extends BaseActivity {
    @ViewInject(R.id.reclcyer)
    private RecyclerView recyclerView;
    @ViewInject(R.id.jiantou)
    private ImageView jianTou;
    @ViewInject(R.id.header_title)
    private TextView title;
    @ViewInject(R.id.header_image_right)
    private ImageView header_image;
    @ViewInject(R.id.header_edit)
    private TextView header_edit;
    @ViewInject(R.id.footer)
    private LinearLayout footer;

    private MyAdapter adapter;
    private List<SqlFavoritesModel> list;
    private int index = 0;
    private List<String> date;
    //无数据时的界面
    private View noData;

    @Event(value = {R.id.header_edit, R.id.header_return,R.id.shanchu})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_edit:
                if (index == 0) {
                    jianTou.setVisibility( View.GONE );
                    title.setText( "编辑" );
                    header_image.setVisibility( View.GONE );
                    header_edit.setText( "完成" );
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) header_edit.getLayoutParams();
                    params.addRule( RelativeLayout.ALIGN_PARENT_RIGHT, 1 );//0为true,1为维false
                    header_edit.setLayoutParams( params );
                    footer.setVisibility( View.VISIBLE );
                    index = 1;
                } else {
                    jianTou.setVisibility( View.VISIBLE );
                    title.setText( R.string.user_shoucang );
                    header_image.setVisibility( View.VISIBLE );
                    header_edit.setText( R.string.bianji );
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) header_edit.getLayoutParams();
                    params.addRule( RelativeLayout.ALIGN_PARENT_RIGHT, 0 );
                    params.addRule( RelativeLayout.LEFT_OF, R.id.header_image_right );//相当于xml中的to_left_of
                    header_edit.setLayoutParams( params );
                    footer.setVisibility( View.GONE );
                    index = 0;
                }
                adapter.notifyDataSetChanged();
                break;

            case R.id.header_return:
                AppManager.getAppManager().finishActivity( );
                break;

            case R.id.shanchu:
                if(date!=null&&!date.isEmpty()){
                    for (int i=0;i<date.size();i++){
                        SqlUtils.delete( date.get( i ));
                    }

                }
                finish();
                startActivity( new Intent( ActivityFavorite.this,ActivityFavorite.class ) );
                break;
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        noData = getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recyclerView.getParent(), false);

        recyclerView.setLayoutManager( new LinearLayoutManager( this ) );
        recyclerView.setItemAnimator( new DefaultItemAnimator() );
        recyclerView.addItemDecoration( new RecycleViewDivider( this,LinearLayoutManager.VERTICAL, 5,getResources().getColor( R.color.gray1 ) ) );
        recyclerView.addOnItemTouchListener( new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (index==0){
                    if (adapter.getItem( i ).getGroup().equals( "sxljt" )){
                        Intent it=new Intent( ActivityFavorite.this,ActivityMedicinesInfo.class );
                        it.putExtra( AppConfig.IntentExtraKey.MEDICINE_INFO_ID,adapter.getItem( i ).getId() );
                        startActivity( it );
                    }else {
                        Intent it=new Intent( ActivityFavorite.this,ActivityGoodsInfo.class );
                        it.putExtra( AppConfig.IntentExtraKey.MEDICINE_INFO_ID,adapter.getItem( i ).getId() );
                        startActivity( it );
                    }

                }
            }
        } );
    }

    @Override
    protected void onResume() {
        super.onResume();

        DbManager dbManager = x.getDb( SqlUtils.getDaoConfig() );
        list= new ArrayList<>(  );
        try {
            list = dbManager.findAll( SqlFavoritesModel.class );
        } catch (DbException e) {
            e.printStackTrace();
        }

        adapter = new MyAdapter(list);
        adapter.openLoadAnimation( BaseQuickAdapter.ALPHAIN );
        recyclerView.setAdapter( adapter );

        if (list==null||list.isEmpty()){
            adapter.setEmptyView( noData );
        }
    }


    private class MyAdapter extends BaseQuickAdapter<SqlFavoritesModel> {
        public MyAdapter(List<SqlFavoritesModel> lists) {
            super( R.layout.item_user_collect, lists );
        }

        @Override
        protected void convert(final BaseViewHolder baseViewHolder, final SqlFavoritesModel messageModel) {
            final Intent it=new Intent( ActivityFavorite.this,ActivityMedicinesInfo.class );
            it.putExtra( AppConfig.IntentExtraKey.MEDICINE_INFO_ID,messageModel.getId() );
            if (index==0){
                date=null;
                baseViewHolder.setVisible( R.id.check,false );
            }else {
                baseViewHolder.setVisible( R.id.check,true );
                date=new ArrayList(  );
            }

            //加载图片
            SimpleDraweeView imageView=baseViewHolder.getView( R.id.goods_img );
            imageView.setImageURI( Uri.parse(messageModel.getImage()) );

            baseViewHolder.setText( R.id.goods_title,messageModel.getName())
                    .setText( R.id.money,"￥"+messageModel.getMoney() )
                    .setOnCheckedChangeListener( R.id.check, new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            Log.e("----",baseViewHolder.getPosition()+";"+b+"");
                            if (b){
                                date.add(adapter.getData().get( baseViewHolder.getPosition() ).getId());
                            }
                        }
                    } );
        }
    }
}
