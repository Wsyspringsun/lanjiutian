package com.wyw.ljtds.ui.user.manage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.wyw.ljtds.R;
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
public class QrCodeActivity extends BaseActivityFragment {
    private static final String TAG_USER_DATA = "com.wyw.ljtds.ui.user.manage.tag_user_data";
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
    protected Fragment createFragment() {
        return QrCodeFragment.newInstance(getIntent().getStringExtra(TAG_USER_DATA));
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvTitle.setText(R.string.myqrcode);
    }

    public static Intent getIntent(Context context, String jsonUser) {
        Intent it = new Intent(context, QrCodeActivity.class);

        it.putExtra(TAG_USER_DATA, jsonUser);
        return it;
    }

}
