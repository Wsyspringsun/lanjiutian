package com.wyw.ljtmgr.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wyw.ljtmgr.R;
import com.wyw.ljtmgr.biz.OrderBiz;
import com.wyw.ljtmgr.biz.SimpleCommonCallback;
import com.wyw.ljtmgr.biz.UserBiz;
import com.wyw.ljtmgr.config.AppConfig;
import com.wyw.ljtmgr.config.MyApplication;
import com.wyw.ljtmgr.model.OrderListResponse;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

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
                UserBiz.logout(getActivity());

                Intent it = ActivityLogin.getIntent(getActivity());
                startActivity(it);

                break;
        }
    }
}
