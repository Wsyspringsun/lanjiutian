package com.wyw.ljtwl.ui;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wyw.ljtwl.R;
import com.wyw.ljtwl.adapter.OrderListAdapter;
import com.wyw.ljtwl.biz.OrderBiz;
import com.wyw.ljtwl.biz.SimpleCommonCallback;
import com.wyw.ljtwl.biz.UserBiz;
import com.wyw.ljtwl.config.AppConfig;
import com.wyw.ljtwl.config.MyApplication;
import com.wyw.ljtwl.model.OrderListResponse;
import com.wyw.ljtwl.utils.ActivityUtil;
import com.wyw.ljtwl.utils.StringUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_setting_index)
public class SettingIndexFragment extends Fragment implements View.OnClickListener {
    @ViewInject(R.id.fragment_setting_index_items)
    LinearLayout llItems;
    @ViewInject(R.id.fragment_setting_index_btnlogout)
    Button btnLogout;


    private static final String ARG_TITLE = "ARG_TITLE";


    public static SettingIndexFragment newInstance(String title) {
        SettingIndexFragment frag = new SettingIndexFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_order_index, null);
        View v = x.view().inject(this, inflater, container);
        initView(v);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        bindData2View();
    }

    private void loadOrder(String stat) {
        Log.e(AppConfig.TAG_ERR, "stat:" + stat);
        OrderBiz.loadOrder(stat, new SimpleCommonCallback<OrderListResponse>(getActivity()) {
            @Override
            protected void handleResult(OrderListResponse result) {
                Log.e(AppConfig.TAG_ERR, "data:" + new Gson().toJson(result));
            }
        });
    }

    void bindData2View() {
        String userId = "";
        if (MyApplication.getCurrentLoginer() != null) {
            userId = MyApplication.getCurrentLoginer().getAdminUserName();
        }
        String[][] itemArr = new String[][]{
                {"帐号", userId},
                {"修改密码", ">"}
        };
        for (int i = 0; i < itemArr.length; i++) {
            String[] item = itemArr[i];
            RelativeLayout llItem = (RelativeLayout) llItems.getChildAt(i);
            for (int j = 0; j < item.length; j++) {
                TextView tv = (TextView) llItem.getChildAt(j);
                tv.setText(item[j]);
            }
        }
    }

    private void initView(View v) {
        btnLogout.setOnClickListener(this);


        View.OnClickListener llItemListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = (String) v.getTag();
                if ("1".equals(tag)) {
                    Intent it = UpdatePasswordActivity.getIntent(getActivity());
                    startActivity(it);
                }
            }
        };
        for (int i = 0; i < llItems.getChildCount(); i++) {
            RelativeLayout llItem = (RelativeLayout) llItems.getChildAt(i);
            llItem.setTag("" + i);
            llItem.setOnClickListener(llItemListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_setting_index_btnlogout:
                UserBiz.logout();

                Intent it = ActivityLogin.getIntent(getActivity());
                startActivity(it);

                break;
        }
    }
}
