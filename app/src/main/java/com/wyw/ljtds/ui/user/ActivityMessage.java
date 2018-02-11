package com.wyw.ljtds.ui.user;

import android.content.Context;
import android.content.Intent;
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
import com.wyw.ljtds.ui.find.FragmentFind;
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
public class ActivityMessage extends BaseActivity {
    @ViewInject(R.id.reclcyer)
    private RecyclerView recyclerView;
    @ViewInject(R.id.header_title)
    private TextView title;
//    @ViewInject(R.id.swipeLayout)
//    private SwipeRefreshLayout swipeLayout;

    private TextView viewEnd;
    //无数据时的界面
    private View noData;
    private List<MessageModel> list = new ArrayList<>();
    private MyAdapter adapter;
    private int pageIndex = 1;
    private boolean end = false;
    //客服
    String settingid1 = "lj_1000_1493167191869";// 客服组id示例kf_9979_1452750735837
    String groupName = "蓝九天";// 客服组默认名称
    private List<MessageModel> data;

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
//        setLoding(this, false);
//        getMsg(true, true);

        final LinearLayoutManager llm = new LinearLayoutManager(this);//必须有
        llm.setOrientation(LinearLayoutManager.VERTICAL);//设置方向滑动
        recyclerView.setLayoutManager(llm);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (adapter == null) return;
                int cnt = adapter.getItemCount() - 1;
                int lastVisibleItem = llm.findLastVisibleItemPosition();
                Log.e(AppConfig.ERR_TAG, "l/cnt:" + lastVisibleItem + "/" + cnt);
                if (!end && !loading && (lastVisibleItem >= cnt)) {
                    pageIndex = pageIndex + 1;
                    getMsg();
                }
            }
        });
//        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                Log.e(AppConfig.ERR_TAG, "onRefresh");
//                pageIndex = 1;
//                getMsg();
//            }
//        });
        initAdapter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        pageIndex = 1;
        end = false;
        getMsg();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void updAdapter() {
        if (adapter == null) {
            initAdapter();
        }
        if (data == null || data.size() <= 0) {
            end = true;
            return;
        }
        if (pageIndex <= 1) {
            adapter.setNewData(data);
        } else {
            adapter.addData(data);
        }
        adapter.notifyDataSetChanged();
    }

    private void initAdapter() {
        noData = getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recyclerView.getParent(), false);
        adapter = new MyAdapter();
        adapter.isFirstOnly(true);//是否只有第一次加载有特效   默认true
        adapter.addHeaderView(getHeaderView(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChat("首页", "", settingid1, groupName, false, "");
            }
        }));
        recyclerView.setAdapter(adapter);
    }

//    @Override
//    protected void resumeFromOther() {
//        super.resumeFromOther();
//        getMsg( true,true );
//    }


    private void getMsg() {
        setLoding(this, false);
        new BizDataAsyncTask<List<MessageModel>>() {
            @Override
            protected List<MessageModel> doExecute() throws ZYException, BizFailure {
                return UserBiz.getMessage("0", PreferenceCache.getToken(), "" + pageIndex, AppConfig.DEFAULT_PAGE_COUNT + "");
            }

            @Override
            protected void onExecuteSucceeded(List<MessageModel> messageModels) {
                closeLoding();
//                swipeLayout.setRefreshing(false);
                ActivityMessage.this.data = messageModels;
                updAdapter();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
//                swipeLayout.setRefreshing(false);
            }
        }.execute();
    }

    //添加headerview  用于客服
    private View getHeaderView(View.OnClickListener listener) {
        View view = getLayoutInflater().inflate(R.layout.item_message, (ViewGroup) recyclerView.getParent(), false);

        view.setOnClickListener(listener);
        return view;
    }

    private TextView getFooterView() {
        if (viewEnd == null) {
            viewEnd = new TextView(this);
            viewEnd.setGravity(Gravity.CENTER);
        }
        return viewEnd;
    }

    public static Intent getIntent(Context context) {
        Intent it = new Intent(context, ActivityMessage.class);
        return it;
    }


    private class MyAdapter extends BaseQuickAdapter<MessageModel> {
        public MyAdapter() {
            super(R.layout.item_message, list);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, MessageModel messageModel) {
            baseViewHolder.setText(R.id.msg_time, messageModel.getINS_DATE() == null ? "" : messageModel.getINS_DATE().substring(0, messageModel.getINS_DATE().indexOf(" ")));
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
