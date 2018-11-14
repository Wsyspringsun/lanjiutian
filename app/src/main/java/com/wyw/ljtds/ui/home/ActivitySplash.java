package com.wyw.ljtds.ui.home;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
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
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.model.HomePageModel;
import com.wyw.ljtds.model.UpdateAppModel;
import com.wyw.ljtds.ui.user.ActivityLogin;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.ToastUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import java.io.File;

@ContentView(R.layout.activity_splash)
public class ActivitySplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);//xutils初始化视图

    }

    @Override
    protected void onResume() {
        super.onResume();
        callback();

        if (UserBiz.isLogined()) {
            MyApplication.initLoginer();
        }
    }

    /**
     * 更新版本
     */
    private void callback() {
        RequestParams params = new RequestParams(AppConfig.APP_UPDATE_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                final UpdateAppModel updateAppModel = GsonUtils.Json2Bean(result, UpdateAppModel.class);
                PackageManager packageManager = getPackageManager();
                // getPackageName()是你当前类的包名，0代表是获取版本信息
                PackageInfo packInfo = null;
                try {
                    packInfo = packageManager.getPackageInfo(getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                String version = packInfo.versionName;
                if (version.compareTo(updateAppModel.getAndroid()) < 0) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(
                            ActivitySplash.this).setTitle("下载更新").setMessage(updateAppModel.getAndroid_update_message())
                            .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    downloadFile(updateAppModel.getAndroid_download_link(), AppConfig.CACHE_ROOT_NAME);
                                }
                            });
                    if (!updateAppModel.getAndroid_force_update().equals("1")) {
                        dialog.setNegativeButton("稍后更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                goMain();
                                dialogInterface.dismiss();
                            }
                        });
                    }
                    dialog.setCancelable(false).show();

                } else {
                    goMain();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtil.show(ActivitySplash.this, "本地网络异常或服务器异常");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        });
    }

    private void goMain() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ActivitySplash.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 900);
    }

    private void downloadFile(final String url, String path) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        RequestParams requestParams = new RequestParams(url);
        requestParams.setSaveFilePath(path);
        x.http().get(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMessage("亲，努力下载中。。。");
                progressDialog.show();
                progressDialog.setMax((int) total);
                progressDialog.setProgress((int) current);
            }

            @Override
            public void onSuccess(File result) {

                progressDialog.dismiss();
                File apkFile = new File(AppConfig.CACHE_ROOT_NAME);
                if (!apkFile.exists()) {
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.parse("file://" + apkFile.toString()),
                        "application/vnd.android.package-archive");
                startActivity(intent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                ToastUtil.show(ActivitySplash.this, "下载失败，请检查网络和SD卡");
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

}
