package com.wyw.ljtmgr.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.wyw.ljtmgr.R;
import com.wyw.ljtmgr.biz.OrderBiz;
import com.wyw.ljtmgr.biz.SimpleCommonCallback;
import com.wyw.ljtmgr.biz.UserBiz;
import com.wyw.ljtmgr.model.ServerResponse;

/**
 * Created by wsy on 18-2-1.
 */
public class ActivitySplash extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (UserBiz.isLogined()) {
                    OrderBiz.loadOrder("A", "1", new SimpleCommonCallback(ActivitySplash.this) {
                        @Override
                        protected void handleResult(ServerResponse result) {

                        }

                        @Override
                        public void onSuccess(Object result) {

                        }
                    });
                }

                Intent intent = MainActivity.getIntent(ActivitySplash.this);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
