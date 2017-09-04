package com.wyw.ljtds.ui.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.wyw.ljtds.R;

/**
 * Created by Administrator on 2016/12/8 0008.
 */

public abstract class BaseActivityFragment extends BaseActivity {
    private Fragment fragment;

    protected abstract Fragment createFragment();

      @Override
    protected void onResume() {
        super.onResume();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction bt = fm.beginTransaction();
        if (fragment == null) {
            fragment = createFragment();
            bt.add(R.id.fragment_con, fragment);
        } else {
            bt.show(fragment);
        }
        bt.commit();
    }
}
