package com.wyw.ljtds.ui.user.address;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.MyRecyclerViewAdapter;
import com.wyw.ljtds.adapter.MyRecyclerViewHolder;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.AppManager;
import com.wyw.ljtds.model.AddressModel;
import com.wyw.ljtds.model.ProvinceModel;
import com.wyw.ljtds.ui.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

@ContentView(R.layout.activity_address_province)
public class ActivityProvince extends BaseActivity {
    @ViewInject(R.id.reclcyer)
    private RecyclerView recyclerView;
    @ViewInject(R.id.header_return_text)
    private TextView title;

    private MyAdapter adapter;
    private List<ProvinceModel> list = new ArrayList<>();


    @Event(value = {R.id.header_return})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_return:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText(R.string.shouhuo_xuanze);

        getprovince();
        setLoding(this, false);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);//必须有
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置方向滑动
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new MyAdapter();
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent(ActivityProvince.this, ActivityCity.class);
                intent.putExtra("id", adapter.getData().get(i).getID());
                intent.putExtra( "address_id",getIntent().getStringExtra( "address_id" ) );
                intent.putExtra("province", adapter.getData().get(i).getNAME());
                intent.putExtra(AppConfig.IntentExtraKey.ADDRESS_FROM, getIntent().getIntExtra(AppConfig.IntentExtraKey.ADDRESS_FROM, 0));
                intent.putExtra("bundle", getIntent().getBundleExtra("bundle"));
                AppManager.addDestoryActivity( ActivityProvince.this,"province" );
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);


    }

//    Intent intent = new Intent(ActivityProvince.this, ActivityCity.class);
//    intent.putExtra("id", provinceModelList.get(position).getID());
//    intent.putExtra("province",provinceModelList.get(position).getNAME());
//    intent.putExtra(AppConfig.IntentExtraKey.ADDRESS_FROM,getIntent().getIntExtra(AppConfig.IntentExtraKey.ADDRESS_FROM,0));
//    intent.putExtra("bundle",getIntent().getBundleExtra("bundle"));
//    startActivity(intent);


    BizDataAsyncTask<List<ProvinceModel>> province;

    private void getprovince() {
        province = new BizDataAsyncTask<List<ProvinceModel>>() {
            @Override
            protected List<ProvinceModel> doExecute() throws ZYException, BizFailure {
                return UserBiz.getProvince();
            }

            @Override
            protected void onExecuteSucceeded(List<ProvinceModel> provinceModels) {
                adapter.addData(provinceModels);
                adapter.notifyDataSetChanged();
                closeLoding();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        };

        province.execute();
    }


    private class MyAdapter extends BaseQuickAdapter<ProvinceModel> {
        public MyAdapter() {
            super(R.layout.item_base_text, list);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, ProvinceModel provinceModel) {
            baseViewHolder.setText(R.id.text, provinceModel.getNAME());
        }
    }
}
