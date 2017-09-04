package com.wyw.ljtds.ui.base;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.AppManager;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.ui.user.ActivityLogin;
import com.wyw.ljtds.ui.user.address.ActivityAddress;
import com.wyw.ljtds.ui.user.address.ActivityAddressEdit;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.widget.dialog.LoadingDialogUtils;

import org.xutils.x;

import java.util.List;

import cn.xiaoneng.coreapi.ChatParamsBody;
import cn.xiaoneng.uiapi.Ntalker;
import cn.xiaoneng.uiapi.XNSDKListener;
import cn.xiaoneng.utils.CoreData;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by Administrator on 2016/12/8 0008.
 */

public class BaseActivity extends AppCompatActivity implements XNSDKListener, EasyPermissions.PermissionCallbacks {
    //广播
    protected BroadcastReceiver receiver;

    protected boolean canShowLogin = true;
    protected boolean canShowAddress = true;
    private static final int REQUEST_CODE_PERMISSION_XIAONENG = 11;

    //等待动画
    private Dialog mDialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case AppConfig.IntentExtraKey.LODING_CONTEXT:
                    LoadingDialogUtils.closeDialog(mDialog);
                    break;
            }
        }
    };
    protected boolean loading = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getAppManager().addActivity(this);//activity管理
        x.view().inject(this);//xutils初始化视图
        Ntalker.getInstance().setSDKListener(this);// 小能监听接口


        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                onReceiveBroadcast(intent);
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConfig.AppAction.ACTION_TOKEN_EXPIRE); // token过期
        filter.addAction(AppConfig.AppAction.ACTION_ADDRESS_EXPIRE);//没有地址
        filter.addAction(AppConfig.AppAction.Base_ACTION_PREFIX + "refresh");
        registerReceiver(receiver, filter);
    }

    protected void onReceiveBroadcast(Intent intent) {
        String action = intent.getAction();
        Log.e("action", action);
//        Log.e("-------",AppManager.getAppManager().currentActivity()+"");
        if (action.equals(AppConfig.AppAction.ACTION_TOKEN_EXPIRE)) {// token过期
            /**
             * MainActivity 与 其他 Activity 都继承与BaseActivity ，当超时时 会显示出多个Login页面
             * 这里block住所有onPause的Activity(意在只保留当前显示的Activity) 防止重复出现多个login
             */
            if (!(BaseActivity.this instanceof ActivityLogin) && canShowLogin) {
                Intent it = new Intent(BaseActivity.this, ActivityLogin.class);
//                if ((BaseActivity.this instanceof MainActivity)) {
//                    it.putExtra( AppConfig.IntentExtraKey.LOGIN_FROM_MAIN, true );
//                }else {
//                    it.putExtra( AppConfig.IntentExtraKey.LOGIN_FROM_MAIN, false );
//                }
                BaseActivity.this.startActivity(it);
            }
        } else if (action.equals(AppConfig.AppAction.ACTION_ADDRESS_EXPIRE)) {
            if (!(BaseActivity.this instanceof ActivityAddress) && canShowAddress) {
                Intent it = new Intent(BaseActivity.this, ActivityAddressEdit.class);
//                if ((BaseActivity.this instanceof MainActivity)) {
//                    it.putExtra( AppConfig.IntentExtraKey.LOGIN_FROM_MAIN, true );
//                }else {
//                    it.putExtra( AppConfig.IntentExtraKey.LOGIN_FROM_MAIN, false );
//                }
                it.putExtra(AppConfig.IntentExtraKey.ADDRESS_FROM, 4);
                BaseActivity.this.startActivity(it);
            }
        } else if (action.equals(AppConfig.AppAction.Base_ACTION_PREFIX + "refresh")) {
//            resumeFromOnResume();
            resumeFromOther();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        canShowLogin = true;
        canShowAddress = true;
////        refreshForMain=true;
//
//        Log.e( "797979",((BaseActivity.this instanceof MainActivity)+";"+AppManager.getAppManager().currentActivity())+"");
//        if ((BaseActivity.this instanceof MainActivity)) {
//            /**
//             * 刷新主页 4个tab
//             */
//            resumeFromOnResume();
//        } else if (!(BaseActivity.this instanceof MainActivity)
//                && !(BaseActivity.this instanceof ActivityLogin)
//                &&!(BaseActivity.this instanceof MainActivity)){
////                && refreshForOther ){
//            /**
//             * 当token=null 或者token过期， 登录后刷新当前页面
//             */
//            resumeFromOther();
//
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        canShowLogin = false;
        canShowAddress = false;
//        refreshForMain=false;

    }

    @Override
    protected void onStop() {
        super.onStop();
        closeLoding();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        closeLoding();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

//        if (keyCode == event.KEYCODE_BACK) {
//            AppManager.getAppManager().finishActivity();
//        }
        return super.onKeyDown(keyCode, event);
    }

    public void setLoding(Context context, boolean settime) {
        loading = true;
        if (settime) {
            mDialog = LoadingDialogUtils.createLoadingDialog(context, "加载中...");
            mHandler.sendEmptyMessageDelayed(AppConfig.IntentExtraKey.LODING_CONTEXT, 1500);
        } else {
            mDialog = LoadingDialogUtils.createLoadingDialog(context, "加载中...");
        }

//        new TimerTask() {
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                Message message = new Message();
//                message.what = 1;
//                handler.sendMessage(message);
//            }
//        };
    }

    public void closeLoding() {
        loading = false;
        LoadingDialogUtils.closeDialog(mDialog);
    }

    /**
     * 用于刷新主页面4个tab ,调用首页中Fragment的onResume方法
     */
    protected void resumeFromOnResume() {

    }

    /**
     * 用于非MainActivity的Activity ， 当请求接口返回token过期 则显示登录页面， 登录成功后 返回当前Activity 并调用
     * resumeFromLogin
     */
    protected void resumeFromOther() {

    }


    /**
     * 打开客服
     *
     * @param startPageTitle 咨询发起页标题
     * @param startPageUrl   咨询发起页URL
     * @param settingid      聊天组id
     * @param groupName      聊天组默认名
     * @param showGood       是否展示商品信息
     * @param goods_id       商品id
     */
    public void openChat(String startPageTitle, String startPageUrl, String settingid, String groupName, boolean showGood, String goods_id) {
        String[] perms = {Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        if (EasyPermissions.hasPermissions(this, perms)) {
            ChatParamsBody chatparams = new ChatParamsBody();

            // 咨询发起页（专有参数）
            chatparams.startPageTitle = startPageTitle;// 咨询发起页标题(必填)
            chatparams.startPageUrl = startPageUrl;//咨询发起页URL，必须以"http://"开头 （必填）

            //**** 域名匹配,企业特殊需求********
//				chatparams.matchstr = "www.lanjiutian.com";

            // erp参数
            chatparams.erpParam = "";


            if (showGood) {
                // 商品展示（专有参数）
                chatparams.itemparams.appgoodsinfo_type = CoreData.SHOW_GOODS_BY_ID;//sdk显示商品信息模式. 建议使用id模式
                chatparams.itemparams.clientgoodsinfo_type = CoreData.SHOW_GOODS_BY_ID;//客服是否显示商品信息
                chatparams.itemparams.clicktoshow_type = CoreData.CLICK_TO_APP_COMPONENT;// 点击SDK商品详情,

                chatparams.itemparams.itemparam = "";

                chatparams.itemparams.goods_id = goods_id;//SHOW_GOODS_BY_ID方式的 商品id
            }

            //判断是否登陆   是否为游客模式
            if (!StringUtils.isEmpty(PreferenceCache.getToken())) {
                Ntalker.getInstance().login(PreferenceCache.getPhoneNum(), PreferenceCache.getPhoneNum(), 0);
            } else {
                Ntalker.getInstance().logout();
            }
            int code = Ntalker.getInstance().startChat(getApplicationContext(), settingid, groupName, null, null, chatparams);
            Log.e("xiaoneng_code", code + "");
        } else {
            EasyPermissions.requestPermissions(this, "需要以下权限:", REQUEST_CODE_PERMISSION_XIAONENG, perms);
        }

    }


    /**
     * 所有聊天消息监听
     */
    @Override
    public void onChatMsg(boolean isSelfMsg, String settingid, String uname, String msgcontent, long msgtime, boolean hasunread) {

    }

    /**
     * 未读聊天消息监听
     */
    @Override
    public void onUnReadMsg(final String settingid, String username, final String msgcontent, final int messagecount) {

    }

    /**
     * 匹配字符串点击事件监听
     */
    @Override
    public void onClickMatchedStr(String returnstr, String matchstr) {

    }

    /**
     * 消息体点击事件监听
     */
    @Override
    public void onClickUrlorEmailorNumber(int contentType, String urlorEmailorNumber) {

    }

    /**
     * 商品信息展示点击事件监听
     */
    @Override
    public void onClickShowGoods(int appgoodsinfo_type, int clientgoodsinfo_type, String goods_id, String goods_name, String goods_price, String goods_image, String goods_url, String goods_showurl) {

    }

    /**
     * 集成错误监听
     */
    @Override
    public void onError(int errorcode) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Log.e("****", "****");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.e("camera", "成功获取权限");
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this, "需要开启此权限")
                    .setTitle(getString(R.string.alert_tishi))
                    .setPositiveButton("设置")
                    .setNegativeButton("取消", null)
                    .setRequestCode(100)
                    .build()
                    .show();
        }
    }
}
