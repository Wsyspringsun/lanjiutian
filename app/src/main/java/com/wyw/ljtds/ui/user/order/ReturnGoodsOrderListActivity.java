package com.wyw.ljtds.ui.user.order;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.user.ActivityMessage;
import com.wyw.ljtds.ui.user.wallet.BalanceFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * create by wsy on 2017-07-28
 */
@ContentView(R.layout.activity_fragment)
public class ReturnGoodsOrderListActivity extends BaseActivity {
    private Fragment fragment;

    @ViewInject(R.id.activity_fragment_title)
    private TextView tvTitle;

    @Event(value = {R.id.back, R.id.message})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.message:
                startActivity(new Intent(this, ActivityMessage.class));
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
    protected void onResume() {
        super.onResume();
        tvTitle.setText(R.string.shouhou);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction bt = fm.beginTransaction();
        if (fragment == null) {
            fragment = ReturnGoodsOrderListFragment.newInstance();
            bt.add(R.id.fragment_con, fragment);
        } else {
            bt.show(fragment);
        }
        bt.commit();
    }
}