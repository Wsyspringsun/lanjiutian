package com.wyw.ljtmgr.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.wyw.ljtmgr.R;

import org.xutils.view.annotation.ContentView;

/**
 * Created by wsy on 17-9-1.
 */

@ContentView(R.layout.activity_fragment)
public class DelegaterEditActivity extends SingleFragmentActivity {
    final static String TAG_ORDER_ID = "com.wyw.ljtwl.ui.tag_order_id";
    private Fragment fragment;


    public static Intent getIntent(Context ctx, String orderId) {
        Intent it = new Intent(ctx, DelegaterEditActivity.class);
        it.putExtra(TAG_ORDER_ID, orderId);
        return it;
    }

    @Override
    protected Fragment createFragment() {
        String orderId = getIntent().getStringExtra(TAG_ORDER_ID);
        return DelegaterEditFragment.newInstance(orderId);
    }

}
