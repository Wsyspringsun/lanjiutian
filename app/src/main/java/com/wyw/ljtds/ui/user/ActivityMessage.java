package com.wyw.ljtds.ui.user;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jauker.widget.BadgeView;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.UserIndexAdapter;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.model.MessageLib;
import com.wyw.ljtds.model.MessageModel;
import com.wyw.ljtds.model.XiaoNengData;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.find.FragmentFind;
import com.wyw.ljtds.ui.goods.ActivityMedicineList;
import com.wyw.ljtds.ui.user.manage.ActivityManage;
import com.wyw.ljtds.utils.DateUtils;
import com.wyw.ljtds.utils.GsonUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.xiaoneng.uiapi.Ntalker;
import cn.xiaoneng.uiapi.OnCustomMsgListener;

/**
 * Created by Administrator on 2017/2/16 0016.
 */

@ContentView(R.layout.activity_message)
public class ActivityMessage extends BaseActivity {
    @ViewInject(R.id.reclcyer)
    private RecyclerView recyclerView;
    @ViewInject(R.id.activity_message_ryv_kefu)
    private RecyclerView ryvKefu;
    @ViewInject(R.id.header_title)
    private TextView title;
    BadgeView badge = null;
    @ViewInject(R.id.activity_message_img_yaoshi)
    ImageView iconYaoshi;
//    @ViewInject(R.id.swipeLayout)
//    private SwipeRefreshLayout swipeLayout;

    private TextView viewEnd;
    //无数据时的界面
    private View noData;
    private List<MessageModel> list = new ArrayList<>();
    private MyAdapter adapter;
    private KefuMsgAdapter adapter4Kefu;
    private int pageIndex = 1;
    private boolean end = false;
    //客服
    String settingid1 = "lj_1000_1493167191869";// 客服组id示例kf_9979_1452750735837
    String groupName = "蓝九天";// 客服组默认名称
    private List<MessageModel> data;

    MessageLib msgLib;

    @Event(value = {R.id.header_return, R.id.activity_message_yaoshi})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_return:
                finish();
                break;
            case R.id.activity_message_yaoshi:
                openChat("首页", "", AppConfig.CHAT_XN_LJT_SETTINGID2, AppConfig.CHAT_XN_LJT_TITLE2, false, "");
                msgLib.clearUnreadMsgCount(AppConfig.CHAT_XN_LJT_SETTINGID2);
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        msgLib = MessageLib.getInstance(getApplicationContext());
        badge = new BadgeView(this);
        badge.setTargetView(iconYaoshi);
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
//                Log.e(AppConfig.ERR_TAG, "l/cnt:" + lastVisibleItem + "/" + cnt);
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


        final LinearLayoutManager llm4Kefu = new LinearLayoutManager(this);//必须有
        llm4Kefu.setOrientation(LinearLayoutManager.VERTICAL);//设置方向滑动
        ryvKefu.setLayoutManager(llm4Kefu);
        noData = getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recyclerView.getParent(), false);
        initAdapter();
        initKefuAdapter();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!UserBiz.isLogined()) {
            finish();
            return;
        }
        pageIndex = 1;
        end = false;

        Integer cnt = msgLib.readUnreadMsgCount(AppConfig.CHAT_XN_LJT_SETTINGID2);
        if (cnt != null && cnt > 0) {
            badge.setBadgeCount(cnt);
        } else {
            badge.setText(null);
        }

        getMsg();

        loadUnReadMsgList();
    }

    private void loadUnReadMsgList() {
        Collection<XiaoNengData> ls = msgLib.readXnGroupList();
        List<XiaoNengData> dataOfGroups = new ArrayList<>();
        dataOfGroups.addAll(ls);
        adapter4Kefu.setData(dataOfGroups);
        adapter4Kefu.notifyDataSetChanged();
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
        adapter = new MyAdapter();
        adapter.isFirstOnly(true);//是否只有第一次加载有特效   默认true
        /*adapter.addHeaderView(getHeaderView(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChat("首页", "", settingid1, groupName, false, "");
            }
        }));*/
        recyclerView.setAdapter(adapter);
    }

    private void initKefuAdapter() {
        if (adapter4Kefu == null) {
            adapter4Kefu = new KefuMsgAdapter(this, null);
            ryvKefu.setAdapter(adapter4Kefu);
        }
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
            if (messageModel == null) return;
            if (baseViewHolder == null) return;
//            baseViewHolder.setText(R.id.activity_message_msg_time, messageModel.getINS_DATE() == null ? "" : messageModel.getINS_DATE().substring(0, messageModel.getINS_DATE().indexOf(" ")));
            if (messageModel.getMSG_TYPE().equals("1")) {
                baseViewHolder.setText(R.id.fragment_message_msg_title, messageModel.getSUBJECT())
                        .setText(R.id.fragment_message_msg_content, messageModel.getCONTENTS());
            } else if (messageModel.getMSG_TYPE().equals("2")) {
                baseViewHolder.setText(R.id.fragment_message_msg_title, messageModel.getSUBJECT())
                        .setText(R.id.fragment_message_msg_content, messageModel.getCONTENTS());
            } else {
                baseViewHolder.setText(R.id.fragment_message_msg_title, messageModel.getSUBJECT())
                        .setText(R.id.fragment_message_msg_content, messageModel.getCONTENTS())
                        .setImageDrawable(R.id.fragment_message_msg_type_img, getResources().getDrawable(R.mipmap.msg_wuliu));
            }
        }

    }

    /**
     * 客服消息列表
     */
    private class KefuMsgViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvName;
        private ImageView icon;
        private XiaoNengData itemData;
        public final BadgeView badge;

        public KefuMsgViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.activity_message_tv_kefu_name);
            icon = (ImageView) findViewById(R.id.activity_message_kefu_img_kefu);
            badge = new BadgeView(ActivityMessage.this);
            badge.setTargetView(icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                default:
                    if (itemData == null) return;
                    msgLib.clearUnreadMsgCount(itemData.getSettingid1());
                    openChat("聊天", "", itemData.getSettingid1(), itemData.getGroupName(), false, "");
            }
        }
    }

    private class KefuMsgAdapter extends RecyclerView.Adapter<KefuMsgViewHolder> {
        private final Context context;
        private List<XiaoNengData> data;

        public void setData(List<XiaoNengData> data) {
            this.data = data;
        }

        public KefuMsgAdapter(Context context, List<XiaoNengData> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public KefuMsgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_kefu, parent, false);
            return new KefuMsgViewHolder(view);
        }

        @Override
        public void onBindViewHolder(KefuMsgViewHolder holder, int position) {
            if (data == null) return;
            XiaoNengData xn = data.get(position);
            if (xn == null) return;
            holder.itemData = xn;
            holder.tvName.setText(xn.getGroupName());
            Integer cnt = msgLib.readUnreadMsgCount(xn.getSettingid1());
            if (cnt != null && cnt > 0) {
                holder.badge.setBadgeCount(cnt);
            } else {
                holder.badge.setText(null);
            }

        }

        @Override
        public int getItemCount() {
            if (data == null)
                return 0;
            return data.size();
        }
    }
}
