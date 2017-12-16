package com.ljt.www.temp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ljt.www.temp.service.PullService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int SDK_PERMISSION_REQUEST = 127;
    private TextView mTextMessage;
    private MediaPlayer mediaplayer;


    private String permissionInfo = "";

    private void startOtherActivity(int i) {
        if (startActivities == null || startActivities.size() < 0) {
            Log.i(AppConfig.TAG, "startActivities is null or empty!");
            return;
        }
        ResolveInfo infoi = startActivities.get(i);
        Log.e(AppConfig.TAG, "StartOtherActivity:" + infoi.activityInfo.packageName + "----" + infoi.activityInfo.name);
        //调用隐式Activity
        Intent it = new Intent(Intent.ACTION_MAIN).setClassName(infoi.activityInfo.packageName, infoi.activityInfo.name);
        startActivity(it);
    }

    private List<ResolveInfo> startActivities;

    private void loadHomeData() {
        //启动列表
        PackageManager pm = MainActivity.this.getPackageManager();
        Intent itStartActivityIntent = new Intent(Intent.ACTION_MAIN);
        itStartActivityIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        //获取符合Intent条件的所有 Activity
        startActivities = pm.queryIntentActivities(itStartActivityIntent, 0);
//                    Log.i(AppConfig.TAG, JSON.toJSONString(startActivities));

        //获取Activity的Label
        if (startActivities != null && startActivities.size() > 0) {
            String info0 = startActivities.get(0).loadLabel(pm).toString();
            Log.i(AppConfig.TAG, "Label0:" + info0);
        } else {
            Log.i(AppConfig.TAG, "startActivity size:" + 0);
        }
        mTextMessage.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadHomeData();

        getPersimmions();

        PullService.setServiceNotifyStat(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        PullService.setServiceNotifyStat(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction(PullService.BROADCAST_NOTIFICATION);
        registerReceiver(broadcastReciver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver(broadcastReciver);
    }

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    loadHomeData();
                    return true;
                case R.id.navigation_dashboard:
                    startOtherActivity(0);
                    return true;
                case R.id.navigation_notifications:
                    if (item.getTitle().equals(getString(R.string.title_notifications_on))) {
                        //关闭服务
                        PullService.setServiceStat(MainActivity.this, true);

//                        HandlerThread ht = new HandlerThread("PullHandlerThread");
//                        ht.start();
//                        ht.getLooper();

                        item.setTitle(getString(R.string.title_notifications_off));
                    } else {
                        //开启服务
                        PullService.setServiceStat(MainActivity.this, false);
                        item.setTitle(getString(R.string.title_notifications_on));
                    }
                    return true;
            }
            return false;
        }


    };

    private BroadcastReceiver broadcastReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(MainActivity.this, "BroadcastReceiver:" + intent.getAction(), Toast.LENGTH_LONG).show();
            if (mediaplayer == null) {
                mediaplayer = MediaPlayer.create(getApplicationContext(), R.raw.prompt);
            }
            mediaplayer.start();
        }
    };

}
