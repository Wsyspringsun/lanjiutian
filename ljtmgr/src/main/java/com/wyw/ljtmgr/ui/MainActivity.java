package com.wyw.ljtmgr.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.util.Log;
import android.view.ViewTreeObserver;

import com.baidu.trace.Trace;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.wyw.ljtmgr.R;
import com.wyw.ljtmgr.biz.OrderBiz;
import com.wyw.ljtmgr.biz.SimpleCommonCallback;
import com.wyw.ljtmgr.biz.UserBiz;
import com.wyw.ljtmgr.config.AppConfig;
import com.wyw.ljtmgr.config.MyApplication;
import com.wyw.ljtmgr.model.OrderListResponse;
import com.wyw.ljtmgr.utils.CommonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import utils.GsonUtils;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    private MyApplication myApp;
    @ViewInject(R.id.activity_main_vp)
    private ViewPager viewPager;
    @ViewInject(R.id.activity_main_tab)
    private TabLayout tabLayout;

    private int PHASE_PERMISSION = 1 << 0;
    private int PHASE_NET_OK = 1 << 1;
    private int PHASE_LOGIN_IN = 1 << 2;
    private int PHASE_SERVER_OK = 1 << 3;
    private int CUR_PHASE = 0;

    private List<SpannableString> tabSpans;
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

            myApp.mTraceClient.startGather(traceListener);
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


        tabSpans = new ArrayList<>();

        tabLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tabLayout.removeAllTabs();
                for (int i = 0; i < titles.length; i++) {
                    Drawable image = ActivityCompat.getDrawable(MainActivity.this, drawNor[i]);
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
                        return FragmentShop.newInstance(titles[position]);
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
    protected void onStart() {
        super.onStart();
        if (!UserBiz.isLogined()) {
            startActivity(ActivityLogin.getIntent(this));
            return;
        }
        //set jiguang tag
        myApp = (MyApplication) getApplication();
        Set<String> tags = new HashSet<>();
        tags.add(myApp.getCurrentLoginer().getOidGroupId());
        CommonUtil.log(GsonUtils.Bean2Json(tags));
        JPushInterface.setTags(this, AppConfig.SEQUENCE, tags);

        ((MyApplication) getApplication()).locationService.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (myApp.getCurrentLoginer() == null) return;
                Trace mTrace = new Trace(myApp.serviceId, myApp.getCurrentLoginer().getAdminUserId());
                myApp.mTraceClient.startTrace(mTrace, traceListener);
            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MyApplication) getApplication()).locationService.stop();
    }


    public static Intent getIntent(Context ctx) {
        Intent it = new Intent(ctx, MainActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return it;
    }
}
