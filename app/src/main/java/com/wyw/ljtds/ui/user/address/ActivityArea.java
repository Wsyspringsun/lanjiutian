package com.wyw.ljtds.ui.user.address;

import android.app.Activity;
import android.content.Context;
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
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.model.AreaModel;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

@ContentView(R.layout.activity_address_area)
public class ActivityArea extends BaseActivity {
    private static final String TAG_PARENT_AREA = "com.wyw.ljtds.ui.user.address.ActivityArea.tag_parent_area";
    public static final String TAG_SELECTED_AREA = "com.wyw.ljtds.ui.user.address.ActivityArea.tag_selected_area";
    private static final int REQUEST_CITY = 0;
    private static final int REQUEST_DISTRICT = 1;
    @ViewInject(R.id.activity_address_area_reclcyer)
    private RecyclerView recyclerView;
    @ViewInject(R.id.header_return_text)
    private TextView title;

    private MyAdapter adapter;
    private List<AreaModel> list = new ArrayList<>();
    private AreaModel pModel;
    private List<AreaModel> models;
    private AreaModel seledModel;


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
        String jsonPModel = getIntent().getStringExtra(TAG_PARENT_AREA);
        this.pModel = AreaModel.fromJson(jsonPModel);
        Utils.log(GsonUtils.Bean2Json(this.pModel));
        loadData();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);//必须有
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置方向滑动
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new MyAdapter();
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                seledModel = models.get(i);
                seledModel.setParentModel(pModel);
                int lvl = seledModel.getAREA_LEVEL();
                Intent intent = ActivityArea.getIntent(ActivityArea.this, seledModel);
                switch (lvl) {
                    case 1:
                        ActivityArea.this.startActivityForResult(intent, REQUEST_CITY);
                        break;
                    case 2:
                        ActivityArea.this.startActivityForResult(intent, REQUEST_DISTRICT);
                        break;
                    default:
                        Utils.log(GsonUtils.Bean2Json(seledModel));
                        Intent rlt = new Intent();
                        rlt.putExtra(TAG_SELECTED_AREA, seledModel.toJson());
                        ActivityArea.this.setResult(Activity.RESULT_OK, rlt);
                        finish();
                        break;
                }

            }
        });
        recyclerView.setAdapter(adapter);
    }

    public static Intent getIntent(Context ctx, AreaModel pArea) {
        Intent it = new Intent(ctx, ActivityArea.class);
        Utils.log(GsonUtils.Bean2Json(pArea));
        it.putExtra(TAG_PARENT_AREA, pArea == null ? "" : pArea.toJson());
        return it;
    }

    private void loadData() {
        new BizDataAsyncTask<List<AreaModel>>() {
            @Override
            protected List<AreaModel> doExecute() throws ZYException, BizFailure {
                Utils.log(GsonUtils.Bean2Json(pModel));
                if (pModel == null) {
                    return UserBiz.getProvince();
                } else if (1 == pModel.getAREA_LEVEL()) {
                    return UserBiz.getcity(pModel.getID());
                } else if (2 == pModel.getAREA_LEVEL()) {
                    return UserBiz.getDistrict(pModel.getID());
                }
                return null;
            }

            @Override
            protected void onExecuteSucceeded(List<AreaModel> areaModels) {
                ActivityArea.this.models = areaModels;
                adapter.addData(areaModels);
                adapter.notifyDataSetChanged();
                closeLoding();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    private class MyAdapter extends BaseQuickAdapter<AreaModel> {
        public MyAdapter() {
            super(R.layout.item_base_text, list);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, AreaModel model) {
            baseViewHolder.setText(R.id.text, model.getNAME());
        }
    }
}
