package com.wyw.ljtds.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.wyw.ljtds.MainActivity;
import com.wyw.ljtds.R;
import com.wyw.ljtds.config.PreferenceCache;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_splash)
public class ActivitySplash extends AppCompatActivity {

    private boolean finished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        final boolean guidePage = PreferenceCache.getGuidePage();
//        /**
//         * 当设置为不自动登录 并且 程序异常退出（如 360 杀死应用） 进入程序先清空token
//         */
//        if (!PreferenceCache.isAutoLogin()
//                && !PreferenceCache.getToken().equals("")) {
//            PreferenceCache.putToken("");
//        }
        Log.e("guidePage",guidePage+"");
        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {
                if (finished) {
                    return;
                }

//                if (guidePage) {
                    Intent intent = new Intent( ActivitySplash.this, MainActivity.class );
                    finish();
                    startActivity( intent );
//                } else {
//                    Intent intent1 = new Intent( ActivitySplash.this, ActivityGuide.class );
//                    finish();
//                    startActivity( intent1 );
//                }

            }
        }, 2000 );
    }

    @Override
    public void onBackPressed() {
        finished = true;
        super.onBackPressed();
    }
}
