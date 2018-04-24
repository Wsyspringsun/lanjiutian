package com.wyw.ljtds.ui.base;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.AppManager;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.model.MessageLib;
import com.wyw.ljtds.ui.goods.ActivityLifeGoodsInfo;
import com.wyw.ljtds.ui.goods.ActivityMedicinesInfo;
import com.wyw.ljtds.ui.user.ActivityLoginOfValidCode;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.widget.dialog.LoadingDialogUtils;

import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.xiaoneng.coreapi.ChatParamsBody;
import cn.xiaoneng.uiapi.Ntalker;
import cn.xiaoneng.uiapi.OnMsgUrlClickListener;
import cn.xiaoneng.uiapi.XNClickGoodsListener;
import cn.xiaoneng.uiapi.XNSDKListener;
import cn.xiaoneng.utils.CoreData;


/**
 * Created by Administrator on 2016/12/8 0008.
 */

public class BaseActivity extends AppCompatActivity implements XNSDKListener {
    BroadcastReceiver receiver;

    private final String HTML_MEDICINE = "medicineDetail.html";
    private final String HTML_LIFE = "lifeDetail.html";

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
        x.view().inject(this);//xutils初始化视图
        //聊窗链接点击事件的监听
        Ntalker.getExtendInstance().message().setOnMsgUrlClickListener(new OnMsgUrlClickListener() {
            @Override
            public void onClickUrlorEmailorNumber(int contentType, String s) {
                if (contentType == 1) {
                    // 网址;
                    Uri uri = Uri.parse(s);

                    String seg = uri.getLastPathSegment();
                    String commodityId = uri.getQueryParameter("commodityId");
                    if (!StringUtils.isEmpty(commodityId)) {
                        if (HTML_MEDICINE.equals(seg)) {
                            startActivity(ActivityMedicinesInfo.getIntent(BaseActivity.this, commodityId, ""));
                        } else if (HTML_LIFE.equals(seg)) {
                            startActivity(ActivityLifeGoodsInfo.getIntent(BaseActivity.this, commodityId));
                        }
                    }
                }
            }
        });

        Ntalker.getExtendInstance().message().onClickShowGoods(new XNClickGoodsListener() {
            @Override
            public void onClickShowGoods(int appgoodsinfo_type, int clientgoodsinfo_type, String goods_id, String goods_name, String goods_price, String goods_image, String goods_url, String goods_showurl) {
                Ntalker.getExtendInstance().chatHeadBar().setFinishButtonFunctions();
            }
        });
//        Utils.log(this.getClass().getName() + "................");

        AppManager.getAppManager().addActivity(this);//activity管理
        Ntalker.getInstance().setSDKListener(this);// 小能监听接口

        //注册广播接收器
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (AppConfig.AppAction.ACTION_TOKEN_EXPIRE.equals(action)) {// token过期
                    context.startActivity(ActivityLoginOfValidCode.getIntent(context));
                } else if (action.equals(AppConfig.AppAction.ACTION_RESETMAIN)) {
                }
            }
        };

    }


    @Override
    protected void onResume() {
        super.onResume();
//        Log.e(AppConfig.ERR_TAG, "onResume:" + this.getClass().getName());
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConfig.AppAction.ACTION_TOKEN_EXPIRE); // token过期
        filter.addAction(AppConfig.AppAction.ACTION_RESETMAIN); // token过期
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
//        Log.e(AppConfig.ERR_TAG, "onPause:" + this.getClass().getName());
        closeLoding();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        Log.e(AppConfig.ERR_TAG, "onStop:" + this.getClass().getName());
        closeLoding();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeLoding();
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

//        if (keyCode == event.KEYCODE_BACK) {
//            AppManager.getAppManager().finishActivity();
//        }
        return super.onKeyDown(keyCode, event);
    }

    public void setLoding(Context context, boolean settime) {
        if (loading) return;
        loading = true;
        if (settime) {
            mDialog = LoadingDialogUtils.createLoadingDialog(context, "...");
            mHandler.sendEmptyMessageDelayed(AppConfig.IntentExtraKey.LODING_CONTEXT, 1500);
        } else {
            mDialog = LoadingDialogUtils.createLoadingDialog(context, "...");
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = AppConfig.IntentExtraKey.LODING_CONTEXT;
                mHandler.sendMessage(message);
            }
        }, new Date(new Date().getTime() + AppConfig.OUT_TIME));
    }

    public void closeLoding() {
        if (!loading) return;
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

        /*if (EasyPermissions.hasPermissions(this, perms)) {

        } else {
            EasyPermissions.requestPermissions(this, "需要以下权限:", REQUEST_CODE_PERMISSION_XIAONENG, perms);
        }*/

        ChatParamsBody chatparams = new ChatParamsBody();
        // 咨询发起页（专有参数）
        chatparams.startPageTitle = startPageTitle;// 咨询发起页标题(必填)
        chatparams.startPageUrl = startPageUrl;//咨询发起页URL，必须以"http://"开头 （必填）

        //**** 域名匹配,企业特殊需求********
//				chatparams.matchstr = "www.lanjiutian.com";

        // erp参数
        chatparams.erpParam = "";

        chatparams.clickurltoshow_type = CoreData.CLICK_TO_APP_COMPONENT;

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
            Ntalker.getBaseInstance().login(PreferenceCache.getPhoneNum(), PreferenceCache.getPhoneNum(), 0);
        } else {
            Ntalker.getBaseInstance().logout();
        }

        Ntalker.getBaseInstance().startChat(getApplicationContext(), settingid, groupName, chatparams);
        MessageLib.getInstance(getApplicationContext()).saveXnGroup(settingid, groupName);

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
    public void onChatMsg(boolean b, String s, String s1, String s2, long l, boolean b1, int i, String s3) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isNeedRequestPermissions(List<String> permissions) {
        // 定位精确位置
//        addPermission(permissions, Manifest.permission.WRITE_SETTINGS);
        // 定位精确位置
        addPermission(permissions, android.Manifest.permission.ACCESS_FINE_LOCATION);
        // 存储权限
        addPermission(permissions, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        // 读取手机状态
        addPermission(permissions, android.Manifest.permission.READ_PHONE_STATE);
        return permissions.size() > 0;
    }

    private void addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
        }
    }

}
