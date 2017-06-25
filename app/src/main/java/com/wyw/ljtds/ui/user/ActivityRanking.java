package com.wyw.ljtds.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppManager;
import com.wyw.ljtds.model.FavoriteModel;
import com.wyw.ljtds.model.FootprintModel;
import com.wyw.ljtds.ui.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

@ContentView(R.layout.activity_collect)
public class ActivityRanking extends BaseActivity {
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
    private List<FootprintModel> list;
    private int index = 0;
    private List<Integer> date;

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
                AppManager.getAppManager().finishActivity();
                break;

            case R.id.shanchu:
                if(date!=null&&!date.isEmpty()){

                    for (int i=0;i<date.size();i++){
                        Log.e( "shuku",date.get( i )+"" );
                        delete( date.get( i ));
                    }
                    finish();
                    startActivity( new Intent( ActivityRanking.this,ActivityRanking.class ) );
                }
                break;
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        title.setText( R.string.user_zuji );

        setLoding( this, false );
        getfoot();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );//必须有
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );//设置方向滑动
        recyclerView.setLayoutManager( linearLayoutManager );

        adapter = new MyAdapter();
        adapter.openLoadAnimation( BaseQuickAdapter.ALPHAIN );
        recyclerView.setAdapter( adapter );

    }

    BizDataAsyncTask<List<FootprintModel>> footprintTask;

    private void getfoot(){
        footprintTask=new BizDataAsyncTask<List<FootprintModel>>() {
            @Override
            protected List<FootprintModel> doExecute() throws ZYException, BizFailure {
                return UserBiz.getFootprint( "1","20" );
            }

            @Override
            protected void onExecuteSucceeded(List<FootprintModel> footprintModels) {
                list=footprintModels;
                adapter.addData( footprintModels );
                closeLoding();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        };
        footprintTask.execute(  );
    }

    BizDataAsyncTask<Integer> deleteTask;

    private void delete(final int id){
        deleteTask=new BizDataAsyncTask<Integer>() {
            @Override
            protected Integer doExecute() throws ZYException, BizFailure {
                return UserBiz.deleteClicksRanking( id );
            }

            @Override
            protected void onExecuteSucceeded(Integer integer) {

            }

            @Override
            protected void OnExecuteFailed() {
            }
        };
        deleteTask.execute(  );
    }


    private class MyAdapter extends BaseQuickAdapter<FootprintModel> {
        public MyAdapter() {
            super( R.layout.item_user_collect, list );
        }

        @Override
        protected void convert(final BaseViewHolder baseViewHolder, FootprintModel messageModel) {
            if (index==0){
                date=null;
                baseViewHolder.setVisible( R.id.check,false )
                        .setChecked( R.id.check,false );
            }else {
                baseViewHolder.setVisible( R.id.check,true );
                date=new ArrayList(  );
            }
            baseViewHolder.setText( R.id.goods_title,messageModel.getTITLE())
                    .setText( R.id.money,"￥"+messageModel.getCOST_MONEY()+"" )
                    .setOnCheckedChangeListener( R.id.check, new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            Log.e("----",baseViewHolder.getPosition()+";"+b+"");
                            if (b){
                                date.add(adapter.getData().get( baseViewHolder.getPosition() ).getCLICKS_RANKING_ID());
                            }
                        }
                    } );
        }
    }
}
