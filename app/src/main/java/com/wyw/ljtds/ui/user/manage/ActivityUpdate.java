package com.wyw.ljtds.ui.user.manage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.UpdateAppModel;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.ToastUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

/**
 * Created by Administrator on 2017/2/20 0020.
 */

@ContentView(R.layout.activity_update)
public class ActivityUpdate extends BaseActivity {
    @ViewInject(R.id.header_title)
    private TextView title;
    @ViewInject(R.id.banben1)
    private TextView banben1;
    @ViewInject(R.id.gengxin_name)
    private TextView gengxin_name;
    @ViewInject(R.id.gengxin_flg)
    private TextView gengxin_flg;


    private UpdateAppModel updateAppModel;
    private String version;

    @Event(value = {R.id.header_return, R.id.update})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_return:
                finish();
                break;

            case R.id.update:
                if (!version.equals( updateAppModel.getAndroid() )) {

                    downloadFile( updateAppModel.getAndroid_download_link(), AppConfig.CACHE_ROOT_NAME );

                }
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        title.setText( R.string.app_update );

        callback();
    }

    private void callback() {
        RequestParams params = new RequestParams( AppConfig.APP_UPDATE_URL );
        Callback.Cancelable cancelable = x.http().get( params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                updateAppModel = GsonUtils.Json2Bean( result, UpdateAppModel.class );

                PackageManager packageManager = getPackageManager();
                // getPackageName()是你当前类的包名，0代表是获取版本信息
                PackageInfo packInfo = null;
                try {
                    packInfo = packageManager.getPackageInfo( getPackageName(), 0 );
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                version = packInfo.versionName;
                Log.e( "********",version+"; "+updateAppModel.getAndroid() );

                banben1.setText( "蓝九天  Android-"+version );
                if (version.equals( updateAppModel.getAndroid() )) {
                    gengxin_flg.setText( "暂无更新" );
                    gengxin_name.setText( "蓝九天（" + version + "）" );
                } else {
                    gengxin_flg.setText( "一键更新" );
                    gengxin_name.setText( "蓝九天（" + updateAppModel.getAndroid() + "）" );
                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        } );
    }

    private void downloadFile(final String url, String path) {

        final ProgressDialog progressDialog = new ProgressDialog( this );
        RequestParams requestParams = new RequestParams( url );
        requestParams.setSaveFilePath( path );
        x.http().get( requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                progressDialog.setProgressStyle( ProgressDialog.STYLE_HORIZONTAL );
                progressDialog.setMessage( "亲，努力下载中。。。" );
                progressDialog.show();
                progressDialog.setMax( (int) total );
                progressDialog.setProgress( (int) current );
            }

            @Override
            public void onSuccess(File result) {

                progressDialog.dismiss();
                File apkFile = new File( AppConfig.CACHE_ROOT_NAME );
                if (!apkFile.exists()) {
                    return;
                }
                Intent intent = new Intent( Intent.ACTION_VIEW );
                intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                intent.setDataAndType( Uri.parse( "file://" + apkFile.toString() ),
                        "application/vnd.android.package-archive" );
                startActivity( intent );
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                ToastUtil.show( ActivityUpdate.this, "下载失败，请检查网络和SD卡" );
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        } );
    }
}
