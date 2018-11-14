package com.wyw.ljtmgr.ui;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewTreeObserver;

import com.baidu.trace.Trace;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.wyw.ljtmgr.R;
import com.wyw.ljtmgr.biz.UserBiz;
import com.wyw.ljtmgr.config.AppConfig;
import com.wyw.ljtmgr.config.MyApplication;
import com.wyw.ljtmgr.config.PreferenceCache;
import com.wyw.ljtmgr.service.BackgroundService;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    public static final String ACTION_REFRESH = "com.wyw.ljtmgr.ui.BaseActivity.action.ACTION_REFRESH";

    /*BroadcastReceiver refReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(viewPager.getCurrentItem()==0){
                OrderIndexFragment  cf = (OrderIndexFragment) ((FragmentStatePagerAdapter)vpAdapter).getItem(0);
                cf.loadOrder();
            }
        }
    };*/;

    private MyApplication myApp;
    @ViewInject(R.id.activity_main_vp)
    private ViewPager viewPager;
    @ViewInject(R.id.activity_main_tab)
    private TabLayout tabLayout;

    private PagerAdapter vpAdapter;
    int[] drawNor = {R.drawable.dingdan_nor, R.drawable.dianpu_nor, R.drawable.xiaoxi_nor, R.drawable.shezhi_nor};
    int[] drawSel = {R.drawable.dingdan_sel, R.drawable.dianpu_sel, R.drawable.xiaoxi_sel, R.drawable.shezhi_sel};
    final String[] titles = new String[]{"订单", "店铺", "消息", "设置"};

    //鹰眼服务
    private OnTraceListener traceListener = new OnTraceListener() {
        @Override
        public void onBindServiceCallback(int errorNo, String message) {
            Log.e(AppConfig.TAG_ERR, "onBindServiceCallback:" + errorNo + ":" + message);
        }

        @Override
        public void onStartTraceCallback(int errorNo, String message) {
            Log.e(AppConfig.TAG_ERR, "onStartTraceCallback:" + errorNo + ":" + message);
            if (errorNo == 0) {
                myApp.mTraceClient.startGather(traceListener);
            }
        }

        @Override
        public void onStopTraceCallback(int errorNo, String message) {
            Log.e(AppConfig.TAG_ERR, "onStopTraceCallback:" + errorNo + ":" + message);
        }

        @Override
        public void onStartGatherCallback(int errorNo, String message) {
            Log.e(AppConfig.TAG_ERR, "onStartGatherCallback:" + errorNo + ":" + message);
        }

        @Override
        public void onStopGatherCallback(int errorNo, String message) {
            Log.e(AppConfig.TAG_ERR, "onStopGatherCallback:" + errorNo + ":" + message);
        }

        @Override
        public void onPushCallback(byte messageType, PushMessage pushMessage) {
            Log.e(AppConfig.TAG_ERR, "onPushCallback:" + pushMessage);
        }

        @Override
        public void onInitBOSCallback(int errorNo, String message) {
            Log.e(AppConfig.TAG_ERR, "onInitBOSCallback:" + errorNo + ":" + message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceCache.putActived(true); // 持久化缓存token


        tabLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tabLayout.removeAllTabs();
                for (int i = 0; i < titles.length; i++) {
                    Drawable image = ActivityCompat.getDrawable(MainActivity.this, drawNor[i]);
//                    TabLayout.Tab tab = tabLayout.newTab();
//                    SpannableString ss = new SpannableString(titles[i]);
//                    ss.setSpan(new RelativeSizeSpan(0.2f), 0, titles[i].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tabLayout.addTab(tabLayout.newTab().setText(titles[i]).setIcon(image));
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    //防止丢失点击事件
                    tabLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        vpAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
//                return super.getPageTitle(position);
                return titles[position];
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return OrderIndexFragment.newInstance(titles[position]);
                    case 1:
                        return FragmentShop.newInstance(titles[position]);
                    case 2:
                        return FragmentMsgIndex.newInstance(titles[position]);
                    case 3:
                        return SettingIndexFragment.newInstance(titles[position]);
                    default:
                        return OrderIndexFragment.newInstance(titles[position]);
                }
            }
        };
        viewPager.setAdapter(vpAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceCache.putActived(false); // 持久化缓存token
    }

    @Override
    protected void onStart() {
        super.onStart();
        PreferenceCache.putActived(true); // 持久化缓存token
        //保持程序不停止
        BackgroundService.setServiceStat(this,true);
        if (!UserBiz.isLogined()) {
            startActivity(ActivityLogin.getIntent(this));
            finish();
            return;
        }
        //set jiguang tag
        myApp = (MyApplication) getApplication();
        Set<String> tags = new HashSet<>();
        tags.add(myApp.getCurrentLoginer().getOidGroupId());
        JPushInterface.setTags(this, AppConfig.SEQUENCE, tags);

        ((MyApplication) getApplication()).locationService.start();

        Trace mTrace = new Trace(myApp.serviceId, myApp.getCurrentLoginer().getAdminUserId());
        myApp.mTraceClient.startTrace(mTrace, traceListener);
//        BackgroundService.setServiceStat(MainActivity.this, true);

        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        /*for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("cn.jpush.android.service.PushService".equals(service.service.getClassName())) {
                Log.e("ljt test", "test PushService run.. running...");
            } else {
                Log.e("ljt test", "test service run.." + service.service.getClassName() + " running...");
            }
        }*/

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (myApp.getCurrentLoginer() == null) return;
                Trace mTrace = new Trace(myApp.serviceId, myApp.getCurrentLoginer().getAdminUserId());
                myApp.mTraceClient.startTrace(mTrace, traceListener);
            }
        }, 1000);*/
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ((MyApplication) getApplication()).locationService.stop();
    }


    public static Intent getIntent(Context ctx) {
        Intent it = new Intent(ctx, MainActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return it;
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }*/
}
