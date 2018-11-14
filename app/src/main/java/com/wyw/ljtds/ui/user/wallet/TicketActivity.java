package com.wyw.ljtds.ui.user.wallet;

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
import com.wyw.ljtds.ui.base.BaseActivityFragment;
import com.wyw.ljtds.ui.user.ActivityMessage;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * create by wsy on 2017-07-28
 */
@ContentView(R.layout.fragment_tickets)
public class TicketActivity extends BaseActivity {
    @ViewInject(R.id.activity_fragment_title)
    private TextView tvTitle;

    @Event(value = {R.id.back,R.id.message,R.id.sel_ticket_dianzibi, R.id.sel_ticket_mianyouquan, R.id.sel_ticket_daijinquan})
    private void onClick(View v) {
        Intent it = null;
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.message:
                startActivity(new Intent(this, ActivityMessage.class));
                break;
            case R.id.sel_ticket_dianzibi:
                it = new Intent(this, DianZiBiActivity.class);
                startActivity(it);
                break;
            case R.id.sel_ticket_mianyouquan:
                //抵用邮费
                it = new Intent(this, YouHuiQuanActivity.class);
                startActivity(it);
                break;
            case R.id.sel_ticket_daijinquan:
                it = new Intent(this, DaiJinQuanListActivity.class);
                startActivity(it);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText(R.string.user_youhuiquan);
    }
}
