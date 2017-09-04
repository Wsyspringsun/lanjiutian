package com.wyw.ljtds.ui.goods;

import android.content.Context;
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
public class ShopActivity extends BaseActivity {
    private static final String TAG_SHOP_ID = "com.wyw.ljtds.ui.goods.tag_shop_id";
    private Fragment fragment;

    @ViewInject(R.id.activity_fragment_title)
    private TextView tvTitle;

    public static Intent getIntent(Context ctx, String shopId){
        Intent it = new Intent(ctx,ShopActivity.class);
        it.putExtra(TAG_SHOP_ID,shopId);
        return it;
    }
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
        tvTitle.setText(R.string.shop);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction bt = fm.beginTransaction();
        if (fragment == null) {
            fragment = ShopFragment.newInstance(getIntent().getStringExtra(TAG_SHOP_ID),"");
            bt.add(R.id.fragment_con, fragment);
        } else {
            bt.show(fragment);
        }
        bt.commit();
    }
}
