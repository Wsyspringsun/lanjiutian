package com.wyw.ljtwl.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.wyw.ljtwl.R;
import com.wyw.ljtwl.config.AppConfig;

/**
 * Created by wsy on 17-9-1.
 */
public abstract class SingleFragmentActivity extends BaseActivity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e(AppConfig.TAG_ERR, "onCreate");
//        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        FragmentTransaction bt = fm.beginTransaction();
        if (fragment == null) {
            Log.e(AppConfig.TAG_ERR, "fragment null");
            fragment = this.createFragment();
            bt.add(R.id.fragmentContainer, fragment);
        } else {
            Log.e(AppConfig.TAG_ERR, "fragment show");
            bt.show(fragment);
        }
        bt.commit();
    }

}
