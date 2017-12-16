package com.wyw.ljtds.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.ui.base.BaseActivityFragment;
import com.wyw.ljtds.ui.user.ActivityMessage;
import com.wyw.ljtds.ui.user.wallet.DianZiBiListFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * create by wsy on 2017-07-28
 */
@ContentView(R.layout.activity_fragment)
public class HuoDongActivity extends BaseActivityFragment {
    private static final String TAG_HUODONG_FLG = "com.wyw.ljtds.ui.home.HuoDongActivity";
    @ViewInject(R.id.activity_fragment_title)
    private TextView tvTitle;

    @Event(value = {R.id.back})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected Fragment createFragment() {
        String flg = getIntent().getStringExtra(TAG_HUODONG_FLG);
        return HuoDongFragment.newInstance(flg);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String flg = getIntent().getStringExtra(TAG_HUODONG_FLG);

        switch (flg) {
            case "1":
                tvTitle.setText(getString(R.string.huodong_choujiang));
                break;
            case "2":
                tvTitle.setText(getString(R.string.huodong_maisong));
                break;
            case "3":
                tvTitle.setText(getString(R.string.huodong_tejia));
                break;
            default:
                tvTitle.setText(getString(R.string.huodong_xianshiqiang));
                break;
        }
    }

    public static Intent getIntent(Context ctx, String flg) {
        Intent it = new Intent(ctx, HuoDongActivity.class);
        it.putExtra(TAG_HUODONG_FLG, flg);
        return it;
    }
}
