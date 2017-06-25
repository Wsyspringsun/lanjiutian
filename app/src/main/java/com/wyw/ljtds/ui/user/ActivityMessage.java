package com.wyw.ljtds.ui.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.model.MessageModel;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.DateUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/16 0016.
 */

@ContentView(R.layout.activity_message)
public class ActivityMessage extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    @ViewInject(R.id.reclcyer)
    private RecyclerView recyclerView;
    @ViewInject(R.id.header_title)
    private TextView title;
    @ViewInject(R.id.swipeLayout)
    private SwipeRefreshLayout swipeLayout;

    //无数据时的界面
    private View noData;
    private List<MessageModel> list = new ArrayList<>();
    private MyAdapter adapter;
    private int pageIndex = 1;
    private boolean end = false;
    //客服
    String settingid1 = "lj_1000_1493167191869";// 客服组id示例kf_9979_1452750735837
    String groupName = "蓝九天";// 客服组默认名称

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

        title.setText(R.string.message1);

        setLoding(this, false);
        getMsg(true, true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);//必须有
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置方向滑动
        recyclerView.setLayoutManager(linearLayoutManager);
        noData = getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recyclerView.getParent(), false);

        adapter = new MyAdapter();
        adapter.isFirstOnly(true);//是否只有第一次加载有特效   默认true
        adapter.addHeaderView(getHeaderView(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChat("首页","", settingid1,groupName ,false,"");
            }
        }));
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getMsg(true, false);
            }

        });


        swipeLayout.setOnRefreshListener(this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void resumeFromOther() {
        super.resumeFromOther();
        getMsg( true,true );
    }

    @Override
    public void onRefresh() {
        getMsg(false, true);
    }


    BizDataAsyncTask<List<MessageModel>> msgTask;

    private void getMsg(final boolean loadmore, final boolean refresh) {
        msgTask = new BizDataAsyncTask<List<MessageModel>>() {
            @Override
            protected List<MessageModel> doExecute() throws ZYException, BizFailure {
                if (refresh) {
                    Log.e("-----", "msg1");
                    return UserBiz.getMessage("0", PreferenceCache.getToken(), "1", AppConfig.DEFAULT_PAGE_COUNT + "");
                } else {
//                    if (end){
//                        return adapter.getData();
//                    }else {
                    Log.e("-----", "msg3");
                    return UserBiz.getMessage("0", PreferenceCache.getToken(), pageIndex + "", AppConfig.DEFAULT_PAGE_COUNT + "");
//                    }
                }
            }

            @Override
            protected void onExecuteSucceeded(List<MessageModel> messageModels) {
                if (messageModels.size() < AppConfig.DEFAULT_PAGE_COUNT) {
                    end = true;
                    //可以加入emptyview
                    if (loadmore && messageModels.size() == 0) {
                        adapter.setEmptyView(noData);
                    }
                } else {
                    end = false;

                }


                if (refresh) {
                    pageIndex = 1;

                    adapter.setNewData(messageModels);
                    adapter.notifyDataSetChanged();

                }else {

                    adapter.addData(messageModels);
                    adapter.notifyDataSetChanged();
                }

                if (end){
                    adapter.addFooterView(getFooterView());
                    adapter.notifyDataSetChanged();
                }else {
                    adapter.removeAllFooterView();
                    adapter.notifyDataSetChanged();
                }


                pageIndex++;
                swipeLayout.setRefreshing(false);


//                adapter.addData(messageModels);
                Log.e("****", messageModels.size() + ";    " + pageIndex + ";  " + adapter.getData().size() + "");
//                adapter.notifyDataSetChanged();

                closeLoding();
            }

            @Override
            protected void OnExecuteFailed() {
                adapter.setEmptyView(noData);
                swipeLayout.setRefreshing(false);
                closeLoding();
            }
        };

        msgTask.execute();
    }

    //添加headerview  用于客服
    private View getHeaderView(View.OnClickListener listener) {
        View view = getLayoutInflater().inflate(R.layout.item_message, (ViewGroup) recyclerView.getParent(), false);

        view.setOnClickListener(listener);
        return view;
    }

    private TextView getFooterView() {
        TextView view = new TextView(this);
        view.setText("end");
        view.setGravity(Gravity.CENTER);

        return view;
    }


    private class MyAdapter extends BaseQuickAdapter<MessageModel> {
        public MyAdapter() {
            super(R.layout.item_message, list);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, MessageModel messageModel) {
            baseViewHolder.setText(R.id.msg_time, messageModel.getINS_DATE() == null ?"":messageModel.getINS_DATE().substring(0,messageModel.getINS_DATE().indexOf(" ")));
            if (messageModel.getMSG_TYPE().equals("1")) {
                baseViewHolder.setText(R.id.msg_title, messageModel.getSUBJECT())
                        .setText(R.id.msg_content, messageModel.getCONTENTS())
                        .setImageDrawable(R.id.msg_type_img, getResources().getDrawable(R.mipmap.msg_xitong));
            } else if (messageModel.getMSG_TYPE().equals("2")) {
                baseViewHolder.setText(R.id.msg_title, messageModel.getSUBJECT())
                        .setText(R.id.msg_content, messageModel.getCONTENTS())
                        .setImageDrawable(R.id.msg_type_img, getResources().getDrawable(R.mipmap.msg_zichan));
            } else {
                baseViewHolder.setText(R.id.msg_title, messageModel.getSUBJECT())
                        .setText(R.id.msg_content, messageModel.getCONTENTS())
                        .setImageDrawable(R.id.msg_type_img, getResources().getDrawable(R.mipmap.msg_wuliu));
            }
        }

    }
}
