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

/**
 * Created by Administrator on 2016/12/8 0008.
 */

public abstract class BaseActivityFragment extends BaseActivity {
    public interface IconBtnManager {
        void initIconBtn(View v, int position);
    }

    private Fragment fragment;

    protected abstract Fragment createFragment();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);


    }


    @Override
    protected void onResume() {
        super.onResume();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction bt = fm.beginTransaction();
        if (fragment == null) {
            fragment = createFragment();
            bt.add(R.id.fragment_con, fragment);

            TextView tvTitle = (TextView)findViewById(R.id.activity_fragment_title);
            tvTitle.setText(getTitle());
            //set back button
            LinearLayout btnBack = (LinearLayout) findViewById(R.id.back);
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   BaseActivityFragment.this.finish();
                }
            });
            //set icon click event
            LinearLayout llIconBtnlist = (LinearLayout) findViewById(R.id.ll_icon_btnlist);
            for (int i = 0; i < llIconBtnlist.getChildCount(); i++) {
                View v = llIconBtnlist.getChildAt(i);
                //  icon button 's status,visible stat and click event,need fragment instance interface IconBtnManager
                Log.e(AppConfig.ERR_TAG, "instanceof:" + (fragment instanceof IconBtnManager));
                if (fragment instanceof IconBtnManager) {
                    ((IconBtnManager) fragment).initIconBtn(v, i);
                }
            }
        } else {
            bt.show(fragment);
        }
        bt.commit();
    }
}
