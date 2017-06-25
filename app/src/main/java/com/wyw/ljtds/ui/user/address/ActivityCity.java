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
import com.wyw.ljtds.model.CityModel;
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
public class ActivityCity extends BaseActivity {
    @ViewInject(R.id.reclcyer)
    private RecyclerView recyclerView;
    @ViewInject(R.id.header_return_text)
    private TextView title;

    private MyAdapter adapter;
    private List<CityModel> list=new ArrayList<>();
    private int id;//省id
    private String name;//省name
    private int from;


    @Event(value = {R.id.header_return})
    private void onClick(View view){
        switch (view.getId()){
            case R.id.header_return:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText(R.string.shouhuo_xuanze);
        id=getIntent().getIntExtra("id",4);
        name=getIntent().getStringExtra("province");
        from=getIntent().getIntExtra(AppConfig.IntentExtraKey.ADDRESS_FROM,0);


        getCity();
        setLoding(this,false);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);//必须有
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置方向滑动
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter=new MyAdapter();
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent(ActivityCity.this,ActivityAddressEdit.class);
//                if (getIntent().getIntExtra( AppConfig.IntentExtraKey.ADDRESS_FROM ,3)==2){
//                    intent.putExtra(AppConfig.IntentExtraKey.ADDRESS_FROM, 2);
//                }else {
                    intent.putExtra(AppConfig.IntentExtraKey.ADDRESS_FROM, getIntent().getIntExtra( AppConfig.IntentExtraKey.ADDRESS_FROM ,3));
//                }
                intent.putExtra( "address_id",getIntent().getStringExtra( "address_id" ) );
                intent.putExtra("id_c", adapter.getData().get(i).getID()+"");
                intent.putExtra("name_c",adapter.getData().get(i).getNAME());
                intent.putExtra("id_p",id+"");
                intent.putExtra("name_p",name);
                Bundle bundle=getIntent().getBundleExtra("bundle");
                bundle.putString( "shengshi",name+adapter.getData().get(i).getNAME() );
                intent.putExtra("bundle",bundle);
                AppManager.destoryActivity( "province" );
                AppManager.destoryActivity( "addressEdit" );

                finish();

                startActivity(intent);
            }
        });

//        Intent intent = new Intent(ActivityCity.this,ActivityAddressEdit.class);
//        intent.putExtra("id_c", cityModelList.get(position).getID()+"");
//        intent.putExtra("name_c",cityModelList.get(position).getNAME());
//        intent.putExtra("id_p",id+"");
//        intent.putExtra("name_p",name);
//        intent.putExtra("bundle",getIntent().getBundleExtra("bundle"));
//        finish();
//        startActivity(intent);
        recyclerView.setAdapter(adapter);
    }

    BizDataAsyncTask<List<CityModel>> city;
    private void getCity(){
        city=new BizDataAsyncTask<List<CityModel>>() {
            @Override
            protected List<CityModel> doExecute() throws ZYException, BizFailure {
                return UserBiz.getcity(id);
            }

            @Override
            protected void onExecuteSucceeded(List<CityModel> CityModel) {
                adapter.addData(CityModel);
                adapter.notifyDataSetChanged();

                closeLoding();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        };

        city.execute();
    }

    private class MyAdapter extends BaseQuickAdapter<CityModel> {
        public MyAdapter() {
            super(R.layout.item_base_text, list);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, CityModel cityModel) {
            baseViewHolder.setText(R.id.text, cityModel.getNAME());
        }
    }
}
