package com.wyw.ljtds.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.wyw.ljtds.MainActivity;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.HomeBiz;
import com.wyw.ljtds.biz.biz.SoapProcessor;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.model.HomePageModel;
import com.wyw.ljtds.ui.user.ActivityLogin;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_splash)
public class ActivitySplash extends AppCompatActivity {

    private boolean finished = false;

    private void getHome() {

        new BizDataAsyncTask<HomePageModel>() {
            @Override
            protected HomePageModel doExecute() throws ZYException, BizFailure {
                try {
                    SoapProcessor ksoap = new SoapProcessor("Service", "homePage", false);
                    JsonElement element = ksoap.request();
                    Gson gson = new GsonBuilder().create();
                    return gson.fromJson(element, HomePageModel.class);
                } catch (Exception ex) {

                } finally {
                    return null;
                }

            }

            @Override
            protected void onExecuteSucceeded(HomePageModel homePageModel) {
                ((MyApplication) getApplication()).isServerOk = true;
            }

            @Override
            protected void OnExecuteFailed() {
                ((MyApplication) getApplication()).isServerOk = false;
            }
        }.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(UserBiz.isLogined()){
            MyApplication.initLoginer();
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (finished) {
                    return;
                }
                getHome();
                Intent intent = new Intent(ActivitySplash.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        finished = true;
        super.onBackPressed();
    }
}
