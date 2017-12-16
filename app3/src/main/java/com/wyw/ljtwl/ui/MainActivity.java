package com.wyw.ljtwl.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gxz.PagerSlidingTabStrip;
import com.wyw.ljtwl.R;
import com.wyw.ljtwl.config.AppConfig;
import com.wyw.ljtwl.config.PreferenceCache;
import com.wyw.ljtwl.model.BaseJson;
import com.wyw.ljtwl.model.Header;
import com.wyw.ljtwl.model.LogOutModel;
import com.wyw.ljtwl.model.UserInfoModel;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivitry {
    @ViewInject(R.id.tabs)
    private PagerSlidingTabStrip tabs;
    @ViewInject(R.id.viewPager)
    private ViewPager viewPager;
    @ViewInject(R.id.shop_title)
    private TextView tvShopTitle;
    @ViewInject(R.id.money_freeze)
    private TextView tvMoneyFreeze;
    @ViewInject(R.id.money_avaliable)
    private TextView tvMoneyAvaliable;
    private ArrayList<String> titles = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentOrder fragmentOrder1;
    private FragmentOrder fragmentOrder2;
    private FragmentOrder fragmentOrder3;
    private FragmentOrder fragmentOrder4;
    private FragmentOrder fragmentOrder5;
    //    private FragmentShop fragmentShop;
    private String jsonData;

    @Event(value = {R.id.setting})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting:
                startActivity(new Intent(this, ActivitySetting.class));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentOrder1 = FragmentOrder.newInstance("E");
        fragmentOrder2 = FragmentOrder.newInstance("A");
        fragmentOrder3 = FragmentOrder.newInstance("B");
        fragmentOrder4 = FragmentOrder.newInstance("C");
        fragmentOrder5 = FragmentOrder.newInstance("D");
////        fragmentList.add(fragmentShop);
        fragmentList.add(fragmentOrder1);
        fragmentList.add(fragmentOrder2);
        fragmentList.add(fragmentOrder3);
        fragmentList.add(fragmentOrder4);
        fragmentList.add(fragmentOrder5);

        titles.add(getResources().getString(R.string.finish));
        titles.add(getResources().getString(R.string.unpay));
        titles.add(getResources().getString(R.string.toshipped));
        titles.add(getResources().getString(R.string.unreject));
        titles.add(getResources().getString(R.string.returned));

        viewPager.setAdapter(new MyFrPagerAdapter(getSupportFragmentManager(), titles, fragmentList));
        tabs.setViewPager(viewPager);
        viewPager.setCurrentItem(getIntent().getIntExtra(AppConfig.IntentExtraKey.MAIN_GO_TO, 0));

//        Intent intent = getIntent();
//        jsonData = intent.getStringExtra("jsonData");
//        Log.e("jsonData----", jsonData);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String userId = PreferenceCache.getUserId();
        String token = PreferenceCache.getToken();
        Log.e("jsonData", userId);
        BaseJson<UserInfoModel> baseJson = new BaseJson<UserInfoModel>();
        UserInfoModel userInfoModel = new UserInfoModel();
        userInfoModel.setBusinessesId(userId);
        Header head = new Header();
        head.setToken(token);
        baseJson.setHead(head);
        baseJson.setBody(userInfoModel);
        Gson gson = new Gson();
        String data = gson.toJson(baseJson);
        Log.e("jsondata", data);
        doAccount(data);
//        fragmentOrder1 = new FragmentOrder("E");
//        fragmentOrder2 = new FragmentOrder("A");
//        fragmentOrder3 = new FragmentOrder("B");
//        fragmentOrder4 = new FragmentOrder("C");
//        fragmentOrder5 = new FragmentOrder("D");
//        fragmentList.add(fragmentShop);
//        fragmentList.add(fragmentOrder1);
//        fragmentList.add(fragmentOrder2);
//        fragmentList.add(fragmentOrder3);
//        fragmentList.add(fragmentOrder4);
//        fragmentList.add(fragmentOrder5);
//        titles.add(getResources().getString(R.string.finish));
//        titles.add(getResources().getString(R.string.unpay));
//        titles.add(getResources().getString(R.string.toshipped));
//        titles.add(getResources().getString(R.string.unreject));
//        titles.add(getResources().getString(R.string.returned));


//        viewPager.setCurrentItem(0);
    }

    class MyFrPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<String> titles;

        private List<Fragment> fragments;

        public MyFrPagerAdapter(FragmentManager fm, ArrayList<String> list, List<Fragment> fragments) {
            super(fm);
            this.titles = list;
            this.fragments = fragments;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            Log.e("title", position + "");
            return titles.get(position);
        }

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public Fragment getItem(int position) {
            Log.e("itemposition", position + "");
            return fragments.get(position);
        }
    }

    public void doAccount(String data) {
        RequestParams params = new RequestParams("http://www.lanjiutian.com/ljt_mobile_store/v/user/groupAccount");

        params.setAsJsonContent(true);
        params.setBodyContent(data);
//                setLoding(getActivity(), false);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("err", result);
                try {
                    JSONObject jsonData = new JSONObject(result);
                    String success = jsonData.getString("success");
                    String msg = jsonData.getString("msg");
                    Log.e("success", success);
                    if (success.equals("0")) {
                        Log.e("success---", "success");
                        tvShopTitle.setText(PreferenceCache.getUsername());
                        tvMoneyFreeze.setText(jsonData.getString("grpFrozeAmount"));
                        tvMoneyAvaliable.setText(jsonData.getString("grpUsableAmount"));
                    } else if (success.equals("2")) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    } else {
                        if ("身份信息过期,请重新登录".equals(msg)) {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            Intent it = ActivityLogin.getIntent(MainActivity.this);
                            startActivity(it);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //解析result
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("err", ex.getMessage());
                Log.e("err", "error.........");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                closeLoding();
                Log.e("err", "cancel.........");
            }

            @Override
            public void onFinished() {
                Log.e("err", "finished.........");
            }
        });
    }
}
