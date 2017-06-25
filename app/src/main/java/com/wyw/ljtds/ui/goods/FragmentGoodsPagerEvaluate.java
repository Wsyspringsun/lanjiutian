package com.wyw.ljtds.ui.goods;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.CommodityDetailsModel;
import com.wyw.ljtds.model.MedicineDetailsEvaluateModel;
import com.wyw.ljtds.model.MedicineDetailsModel;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.utils.DateUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.widget.RecycleViewDivider;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;

/**
 * Created by Administrator on 2017/3/12 0012.
 */

@ContentView(R.layout.fragment_goods_evaluate)
public class FragmentGoodsPagerEvaluate extends BaseFragment {
    @ViewInject( R.id.recycler )
    private RecyclerView recycler;

    private int pageIndex = 1;
    private boolean end = false;
    private MyAdapter adapter;
    private List<MedicineDetailsEvaluateModel> list_eva;
    private View noData;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void update(final MedicineDetailsModel model){
        Log.e( "---------",model.getWAREID() );
        geteva( model.getWAREID(), true, true );

        noData = getActivity().getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recycler.getParent(), false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getActivity() );//必须有
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );//设置方向滑动
        recycler.setLayoutManager( linearLayoutManager );
        recycler.setItemAnimator( new DefaultItemAnimator() );
        recycler.addItemDecoration( new RecycleViewDivider( getActivity(),LinearLayoutManager.VERTICAL, 10,getResources().getColor( R.color.font_black2 ) ) );

        adapter = new MyAdapter();
        adapter.setOnLoadMoreListener( new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                geteva(model.getWAREID(), true, false );
            }
        } );
        adapter.openLoadAnimation( BaseQuickAdapter.ALPHAIN );
        recycler.setAdapter( adapter );
    }

    public void update(final CommodityDetailsModel model){
        Log.e( "---------",model.getCommodityId() );
        geteva( model.getCommodityId(), true, true );

        noData = getActivity().getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recycler.getParent(), false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getActivity() );//必须有
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );//设置方向滑动
        recycler.setLayoutManager( linearLayoutManager );
        recycler.setItemAnimator( new DefaultItemAnimator() );
        recycler.addItemDecoration( new RecycleViewDivider( getActivity(),LinearLayoutManager.VERTICAL, 10,getResources().getColor( R.color.font_black2 ) ) );

        adapter = new MyAdapter();
        adapter.setOnLoadMoreListener( new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                geteva(model.getCommodityId(), true, false );
            }
        } );
        adapter.openLoadAnimation( BaseQuickAdapter.ALPHAIN );
        recycler.setAdapter( adapter );
    }

    BizDataAsyncTask<List<MedicineDetailsEvaluateModel>> evaTask;
    private void geteva(final String classid, final boolean loadmore, final boolean refresh){
        evaTask=new BizDataAsyncTask<List<MedicineDetailsEvaluateModel>>() {
            @Override
            protected List<MedicineDetailsEvaluateModel> doExecute() throws ZYException, BizFailure {
                if (refresh) {
                    return GoodsBiz.getEvaluate( classid, "0", AppConfig.DEFAULT_PAGE_COUNT + "" );
                } else {
                    return GoodsBiz.getEvaluate( classid, pageIndex + "", AppConfig.DEFAULT_PAGE_COUNT + "" );
                }
            }

            @Override
            protected void onExecuteSucceeded(List<MedicineDetailsEvaluateModel> medicineDetailsEvaluateModels) {
                if (medicineDetailsEvaluateModels.size() < AppConfig.DEFAULT_PAGE_COUNT) {
                    end = true;
                    //可以加入emptyview
                    if (loadmore && medicineDetailsEvaluateModels.size() == 0) {
                        adapter.setEmptyView( noData );
                    }
                } else {
                    end = false;

                }

                list_eva = medicineDetailsEvaluateModels;
                if (loadmore){
                    adapter.addData( list_eva );
                    adapter.notifyDataSetChanged();
                }
                if (refresh){
                    adapter.setNewData( list_eva );
                    adapter.notifyDataSetChanged();
                }


                if (end) {
                    adapter.addFooterView( getFooterView() );
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.removeAllFooterView();
                    adapter.notifyDataSetChanged();
                }

                Log.e( "****", medicineDetailsEvaluateModels.size() + ";    " + pageIndex + ";  " + adapter.getData().size() + "" );

                pageIndex++;
            }

            @Override
            protected void OnExecuteFailed() {

            }
        };
        evaTask.execute(  );
    }

    private View getFooterView() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.main_end_view, (ViewGroup) recycler.getParent(), false);

        return view;
    }

    private class MyAdapter extends BaseQuickAdapter<MedicineDetailsEvaluateModel> {

        public MyAdapter() {
            super( R.layout.item_goods_evaluate, list_eva );
        }


        @Override
        protected void convert(BaseViewHolder baseViewHolder, MedicineDetailsEvaluateModel models) {
            String str = models.getMOBILE();
            Log.e( "+++++++++++",str );
            baseViewHolder.setText( R.id.time, DateUtils.parseTime( models.getINS_DATE() + "" ) )
                    .setText( R.id.name, str.substring( 0, str.length() - (str.substring( 3 )).length() ) + "****" + str.substring( 7 ) )
                    .setText( R.id.context, models.getEVALUATE_CONTGENT() );

            SimpleDraweeView user_img = baseViewHolder.getView( R.id.user_img );
            if (StringUtils.isEmpty( models.getUSER_ICON_FILE_ID() )) {
                user_img.setImageURI( Uri.parse( "" ) );
            } else {
                user_img.setImageURI( Uri.parse( AppConfig.IMAGE_PATH + models.getUSER_ICON_FILE_ID() ) );
            }


            ArrayList arrayList = new ArrayList();
            if (!StringUtils.isEmpty( models.getIMG_PATH1() )) {
                arrayList.add( AppConfig.IMAGE_PATH + models.getIMG_PATH1() );
            }
            if (!StringUtils.isEmpty( models.getIMG_PATH2() )) {
                arrayList.add( AppConfig.IMAGE_PATH + models.getIMG_PATH2() );
            }
            if (!StringUtils.isEmpty( models.getIMG_PATH3() )) {
                arrayList.add( AppConfig.IMAGE_PATH + models.getIMG_PATH3() );
            }
            if (!StringUtils.isEmpty( models.getIMG_PATH4() )) {
                arrayList.add( AppConfig.IMAGE_PATH + models.getIMG_PATH4() );
            }
            if (!StringUtils.isEmpty( models.getIMG_PATH5() )) {
                arrayList.add( AppConfig.IMAGE_PATH + models.getIMG_PATH4() );
            }
            final BGANinePhotoLayout mCurrentClickNpl = baseViewHolder.getView( R.id.item_moment_photos );
            mCurrentClickNpl.setData( arrayList );
            mCurrentClickNpl.setDelegate( new BGANinePhotoLayout.Delegate() {
                @Override
                public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
                    if (mCurrentClickNpl.getItemCount() == 1) {
                        // 预览单张图片
                        startActivity( BGAPhotoPreviewActivity.newIntent( getActivity(), null, mCurrentClickNpl.getCurrentClickItem() ) );
                    } else if (mCurrentClickNpl.getItemCount() > 1) {
                        // 预览多张图片
                        startActivity( BGAPhotoPreviewActivity.newIntent( getActivity(), null, mCurrentClickNpl.getData(), mCurrentClickNpl.getCurrentClickItemPosition() ) );
                    }
                }
            } );

        }
    }
}
