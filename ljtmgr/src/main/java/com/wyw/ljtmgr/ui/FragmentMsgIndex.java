package com.wyw.ljtmgr.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wyw.ljtmgr.R;
import com.wyw.ljtmgr.config.AppConfig;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2017/7/3 0003.
 */

@ContentView(R.layout.fragment_msg_index)
public class FragmentMsgIndex extends BaseFragment {
    private static final String ARG_TITLE = "ARG_TITLE";

    @ViewInject(R.id.fragment_msg_index_btn_start)
    Button btn;

    public static FragmentMsgIndex newInstance(String title) {
        FragmentMsgIndex frag = new FragmentMsgIndex();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }



    @Override
    public void onStart() {
        super.onStart();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(AppConfig.TAG_ERR,"msgfrag click");

//                RealTimeBackgroundService.setServiceStat(getActivity(), true);

            }
        });

        /*LoginModel loginer = MyApplication.getCurrentLoginer();
        if (loginer == null) return;
        tvShop.setText(loginer.getOidGroupId());*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
