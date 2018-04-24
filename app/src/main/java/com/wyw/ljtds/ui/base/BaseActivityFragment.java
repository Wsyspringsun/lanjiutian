package com.wyw.ljtds.ui.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.mm.opensdk.utils.Log;
import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.utils.ToolbarManager;

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

            ToolbarManager.IconBtnManager mgr = null;
            //实现了 工具栏 管理 的 接口
            if (fragment instanceof ToolbarManager.IconBtnManager)
                mgr = (ToolbarManager.IconBtnManager) fragment;
            ToolbarManager.initialToolbar(this, mgr);
        } else {
            bt.show(fragment);
        }
        bt.commit();
    }
}
