package com.wyw.ljtds.ui.category;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.category.MyListAdapter2;
import com.wyw.ljtds.adapter.category.Recycler_rightAdapter2;
import com.wyw.ljtds.biz.biz.CategoryBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.MedicineTypeFirstModel;
import com.wyw.ljtds.model.MedicineTypeSecondModel;
import com.wyw.ljtds.ui.base.BaseFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/29 0029.
 */

@ContentView(R.layout.fragment_category_item)
public class FragmentMedicine extends BaseFragment {
    @ViewInject(R.id.left_lv)
    private ListView mListView;
    @ViewInject(R.id.right_recycler)
    private RecyclerView mRight_recycler;

    private MyListAdapter2 mAdapter;
    private Recycler_rightAdapter2 recycler_rightAdapter;
    private List<MedicineTypeFirstModel> data;//一级菜单所需

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        mAdapter = new MyListAdapter2( getActivity() );
        mListView.setAdapter( mAdapter );
        mListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                mAdapter.setCurPositon( position );
                mAdapter.notifyDataSetChanged();

                setLoding( getActivity(), false );
                getChildren();
                mListView.smoothScrollToPositionFromTop( position, (parent.getHeight() - view.getHeight()) / 2 );

            }
        } );
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint( isVisibleToUser );
        if (isVisibleToUser){
            getType();
        }else {

        }
    }


    BizDataAsyncTask<List<MedicineTypeFirstModel>> getTypeTask;

    private void getType() {
        getTypeTask = new BizDataAsyncTask<List<MedicineTypeFirstModel>>() {
            @Override
            protected List<MedicineTypeFirstModel> doExecute() throws ZYException, BizFailure {
                return CategoryBiz.getMedicineType();
            }

            @Override
            protected void onExecuteSucceeded(List<MedicineTypeFirstModel> medicineTypeFirstModels) {
                data = medicineTypeFirstModels;
                mAdapter.addItems( data );
                mAdapter.notifyDataSetChanged();


                getChildren();
            }

            @Override
            protected void OnExecuteFailed() {

            }
        };
        getTypeTask.execute();
    }


    BizDataAsyncTask<List<MedicineTypeSecondModel>> getchildrenTask;

    private void getChildren() {
        getchildrenTask = new BizDataAsyncTask<List<MedicineTypeSecondModel>>() {
            @Override
            protected List<MedicineTypeSecondModel> doExecute() throws ZYException, BizFailure {
                Log.e(AppConfig.ERR_TAG,data.size()+"/"+mAdapter.getCurPositon());
                return CategoryBiz.getMedicineTypeList( data.get( mAdapter.getCurPositon() ).getCLASSCODE(), "1" );
            }

            @Override
            protected void onExecuteSucceeded(List<MedicineTypeSecondModel> medicineTypeSecondModels) {
                recycler_rightAdapter = new Recycler_rightAdapter2( getActivity(), medicineTypeSecondModels );
                mRight_recycler.setLayoutManager( new LinearLayoutManager( getActivity() ) );
                mRight_recycler.setAdapter( recycler_rightAdapter );
                recycler_rightAdapter.notifyDataSetChanged();

                closeLoding();
            }

            @Override
            protected void OnExecuteFailed() {
            }
        };
        getchildrenTask.execute();
    }
}
