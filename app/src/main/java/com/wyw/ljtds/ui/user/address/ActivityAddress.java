package com.wyw.ljtds.ui.user.address;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.MyRecyclerViewAdapter;
import com.wyw.ljtds.adapter.MyRecyclerViewHolder;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.model.AddressModel;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.ToastUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/6 0006.
 */

@ContentView(R.layout.activity_address_list)
public class ActivityAddress extends BaseActivity {
    @ViewInject(R.id.reclcyer)
    private RecyclerView recyclerView;
    @ViewInject(R.id.header_return_text)
    private TextView title;
    @ViewInject(R.id.tianjia)
    private TextView add;

    //无数据时的界面
    private View noData;
    private MyAdapter adapter;
    private List<AddressModel> list = new ArrayList<>();

    @Event(value = {R.id.tianjia, R.id.header_return})
    private void onClick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.tianjia:
                Log.e(AppConfig.ERR_TAG, "edit................");
                it = new Intent(this, ActivityAddressEdit.class);
                it.putExtra(AppConfig.IntentExtraKey.ADDRESS_FROM, 1);
                startActivity(it);
                break;
            case R.id.header_return:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText(R.string.address_guanli);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);//必须有
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置方向滑动
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        noData = getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recyclerView.getParent(), false);

        adapter = new MyAdapter();
        recyclerView.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }

            @Override
            public void onItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                switch (view.getId()) {
                    case R.id.bianji:
                        Intent it = new Intent(ActivityAddress.this, ActivityAddressEdit.class);
                        it.putExtra("address_id", adapter.getData().get(i).getADDRESS_ID() + "");
                        it.putExtra(AppConfig.IntentExtraKey.ADDRESS_FROM, 2);
                        Bundle bundle = new Bundle();
                        bundle.putString("name", adapter.getData().get(i).getCONSIGNEE_NAME());
                        bundle.putString("phone", adapter.getData().get(i).getCONSIGNEE_MOBILE());
                        bundle.putString("xiangxi", adapter.getData().get(i).getCONSIGNEE_ADDRESS());
                        bundle.putString("shengshi", adapter.getData().get(i).getPROVINCE() + adapter.getData().get(i).getCITY());
                        it.putExtra("bundle", bundle);
                        startActivity(it);
                        break;

                    case R.id.shanchu:
                        delete(adapter.getData().get(i).getADDRESS_ID() + "");
                        finish();
                        startActivity(new Intent(ActivityAddress.this, ActivityAddress.class));
                        break;
                }
            }

            @Override
            public void onItemChildLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }
        });

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLoding(this, false);
        getAddress();
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult( requestCode, resultCode, data );
//        if (resultCode==AppConfig.IntentExtraKey.RESULT_OK){
//            if (requestCode==1){
//                setLoding(this, false);
//                getAddress();
//            }
//        }
//    }

    BizDataAsyncTask<List<AddressModel>> addressTask;

    private void getAddress() {
        addressTask = new BizDataAsyncTask<List<AddressModel>>() {
            @Override
            protected List<AddressModel> doExecute() throws ZYException, BizFailure {
                return UserBiz.selectUserAddress();
            }

            @Override
            protected void onExecuteSucceeded(List<AddressModel> addressModels) {
                list = addressModels;

                adapter.setNewData(addressModels);
                adapter.notifyDataSetChanged();

                if (addressModels != null && addressModels.size() > 0)
                    add.setVisibility(View.GONE);
                else
                    add.setVisibility(View.VISIBLE);

                closeLoding();
            }

            @Override
            protected void OnExecuteFailed() {
                adapter.setEmptyView(noData);
                adapter.notifyDataSetChanged();

                closeLoding();
            }
        };
        addressTask.execute();
    }

    BizDataAsyncTask<Integer> deleteTask;

    private void delete(final String str) {
        deleteTask = new BizDataAsyncTask<Integer>() {
            @Override
            protected Integer doExecute() throws ZYException, BizFailure {
                return UserBiz.deleteUserAddress(str);
            }

            @Override
            protected void onExecuteSucceeded(Integer integer) {
                ToastUtil.show(ActivityAddress.this, getResources().getString(R.string.delete_succeed));
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        };
        deleteTask.execute();
    }


    private class MyAdapter extends BaseQuickAdapter<AddressModel> {
        public MyAdapter() {
            super(R.layout.item_address, list);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, AddressModel addressModel) {
            baseViewHolder.setText(R.id.name, addressModel.getCONSIGNEE_NAME())
                    .setText(R.id.phone, addressModel.getCONSIGNEE_MOBILE())
                    .setText(R.id.xiangxi, addressModel.getPROVINCE() + addressModel.getCITY() + " " + addressModel.getCONSIGNEE_ADDRESS())
                    .addOnClickListener(R.id.bianji)
                    .addOnClickListener(R.id.shanchu);

        }
    }


}
