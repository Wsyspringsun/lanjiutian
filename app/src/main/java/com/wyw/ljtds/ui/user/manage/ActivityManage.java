package com.wyw.ljtds.ui.user.manage;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.AppManager;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.model.SingleCurrentUser;
import com.wyw.ljtds.model.UserModel;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.user.address.AddressActivity;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/13 0013.
 */

@ContentView(R.layout.activity_manage)
public class ActivityManage extends BaseActivity {
    private static String TAG_USERINFO = "user";
    @ViewInject(R.id.header_title)
    private TextView title;
    @ViewInject(R.id.activity_manage_iv_photo)
    private ImageView ivHeadPhoto;
    @ViewInject(R.id.name)
    private TextView name;
    @ViewInject(R.id.nicheng)
    private TextView nicheng;
    @ViewInject(R.id.shiming_xin)
    private TextView shiming_xin;
    @ViewInject(R.id.activity_manage_tv_referrer)
    private TextView tvReferrer;
    @ViewInject(R.id.activity_manage_mywaiter)
    private TextView tv;


    public static final String TAG_USER = "COM.WYW.LJTDS.UI.USER.MANAGE.TAG_USER";

//    private UserModel user;

    private final int REQUEST_SHIMING = 1;
    private EditText etReferrer; //推荐人输入框
    private final String hintEtReferrer = "请输入推荐人工号";

    UserBiz userBiz = null;


    @Event(value = {R.id.activity_manage_mywaiter, R.id.activity_manage_bindreferrer, R.id.address, R.id.activity_manage_userinfo, R.id.touxiang, R.id.header_return, R.id.guanyu, R.id.qiehuan, R.id.anquan, R.id.activity_manage_iv_photo, R.id.activity_manage_rl_qrcode})
    private void onClick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.header_return:
                finish();
                break;

            case R.id.address:
                Log.e(AppConfig.ERR_TAG, "click...................");
                it = new Intent(ActivityManage.this, AddressActivity.class);
                startActivity(it);
                break;

            case R.id.activity_manage_userinfo:
                it = UserInfoExtraActivity.getIntent(ActivityManage.this);
                if (SingleCurrentUser.userInfo == null) return;
                startActivityForResult(it, REQUEST_SHIMING);
                /*if (StringUtils.isEmpty(SingleCurrentUser.userInfo.getID_CARD()) || StringUtils.isEmpty(SingleCurrentUser.userInfo.getUSER_NAME())) {
//                    it = new Intent(ActivityManage.this, ActivityRealName.class);
                    it = UserInfoExtraActivity.getIntent(ActivityManage.this);
                    startActivityForResult(it, REQUEST_SHIMING);
                } else {
                    ToastUtil.show(this, "完善信息成功");
                }*/
                break;

            case R.id.activity_manage_bindreferrer:
                if (tvReferrer.getText().length() > 0) {
                    ToastUtil.show(ActivityManage.this, "您已经绑定过推荐人!不能重复绑定");
                    return;
                }
                etReferrer = (EditText) LayoutInflater.from(ActivityManage.this).inflate(R.layout.item_edittext, null);
//                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(etReferrer.getLayoutParams());
//                lp.setMargins(50, 20, 50, 20);
//                etReferrer.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
//                etReferrer.setLayoutParams(lp);
                etReferrer.setHint(hintEtReferrer);
                new AlertDialog.Builder(this).setTitle("请输入推荐人工号")
                        .setView(etReferrer).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (StringUtils.isEmpty(etReferrer.getText().toString().trim())) {
                            ToastUtil.show(ActivityManage.this, hintEtReferrer);
                        } else {
                            bindReferrer(etReferrer.getText().toString());
                        }

                    }
                }).setNegativeButton("取消", null).show();

                break;

            case R.id.activity_manage_mywaiter:
                if (StringUtils.isEmpty(SingleCurrentUser.userInfo.getINTRODUCER_OID_USER_ID())) {
                    ToastUtil.show(ActivityManage.this, "还没有绑定推荐人！");
                    return;
                }
                startActivity(ActivityWaitter.getIntent(ActivityManage.this, SingleCurrentUser.userInfo.getINTRODUCER_OID_USER_ID()));
                break;

            case R.id.activity_manage_rl_qrcode:
                it = QrCodeActivity.getIntent(this);
                startActivity(it);
                break;

            case R.id.touxiang:
                it = new Intent(ActivityManage.this, ActivityInformation.class);
                startActivityForResult(it, REQUEST_SHIMING);
                break;

            case R.id.guanyu:
                startActivity(new Intent(ActivityManage.this, ActivityUpdate.class));
                break;

            case R.id.activity_manage_iv_photo:
                if (SingleCurrentUser.userInfo == null || StringUtils.isEmpty(SingleCurrentUser.userInfo.getUSER_ICON_FILE_ID()))
                    return;
                it = ActivityBigImage.getIntent(this, AppConfig.IMAGE_PATH_LJT + SingleCurrentUser.userInfo.getUSER_ICON_FILE_ID());
                startActivity(it);
                break;

            case R.id.qiehuan:
                AlertDialog alert = new AlertDialog.Builder(this).create();
                alert.setMessage(getResources().getString(R.string.exit_account));
                alert.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.alert_queding), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PreferenceCache.putToken("");
                        PreferenceCache.putUsername("");
                        PreferenceCache.putPhoneNum("");
                        SingleCurrentUser.userInfo = null;
                        if (SingleCurrentUser.bdLocation != null) {
                            SingleCurrentUser.updateLocation(SingleCurrentUser.bdLocation.getLatitude(), SingleCurrentUser.bdLocation.getLongitude(), SingleCurrentUser.bdLocation.getAddrStr());
                        }
                        AppConfig.currSel = AppConfig.DEFAULT_INDEX_FRAGMENT;

                        finish();
                        Intent it = new Intent(AppConfig.AppAction.ACTION_TOKEN_EXPIRE);
                        MyApplication.getAppContext().sendBroadcast(it);
                    }
                });
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.alert_quxiao), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alert.show();

                Button button1 = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                Button button2 = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                button1.setTextColor(getResources().getColor(R.color.base_bar));
                button2.setTextColor(getResources().getColor(R.color.base_bar));

                break;

            case R.id.anquan:
                AppManager.addDestoryActivity(this, "222");
                startActivity(new Intent(ActivityManage.this, ActivitySecurity.class));
                break;

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText(R.string.user_zhanghu);

        userBiz = UserBiz.getInstance(this);

        updView();
    }

    private void updView() {
        if (SingleCurrentUser.userInfo == null) return;
        name.setText("用户名：" + SingleCurrentUser.userInfo.getMOBILE());
//        sdv_item_head_img.setImageURI(Uri.parse(AppConfig.IMAGE_PATH_LJT + user.getUSER_ICON_FILE_ID()));
        Picasso.with(this).load(Uri.parse(AppConfig.IMAGE_PATH_LJT + SingleCurrentUser.userInfo.getUSER_ICON_FILE_ID())).into(ivHeadPhoto);

        if (StringUtils.isEmpty(SingleCurrentUser.userInfo.getNICKNAME())) {
            nicheng.setText("昵称：");
        } else {
            nicheng.setText("昵称：" + SingleCurrentUser.userInfo.getNICKNAME());
        }

        if (StringUtils.isEmpty(SingleCurrentUser.userInfo.getID_CARD()) || StringUtils.isEmpty(SingleCurrentUser.userInfo.getUSER_NAME())) {
            shiming_xin.setText("");
        } else {
            shiming_xin.setText("已实名认证");
        }

        if (StringUtils.isEmpty(SingleCurrentUser.userInfo.getINTRODUCER_OID_USER_ID())) {
            tvReferrer.setText("");
        } else {
            tvReferrer.setText("推荐人:" + SingleCurrentUser.userInfo.getINTRODUCER_OID_USER_ID());
        }
    }

    private void getUser() {
        setLoding(this, false);
        new BizDataAsyncTask<UserModel>() {
            @Override
            protected UserModel doExecute() throws ZYException, BizFailure {
                return userBiz.getUser();
            }

            @Override
            protected void onExecuteSucceeded(UserModel data) {
                closeLoding();
                SingleCurrentUser.userInfo = data;
                updView();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_SHIMING) {
            getUser();
        }
    }

    /**
     * 绑定推荐人
     *
     * @param referrer
     * @return
     */
    public void bindReferrer(final String referrer) {
        setLoding(this, false);
        new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                return userBiz.bindReferrer(referrer);
            }

            @Override
            protected void onExecuteSucceeded(String data) {
                closeLoding();
                Utils.log("bindReferrer:" + data);
                try {
                    Utils.parseResponse(data);
                    getUser();
                } catch (BizFailure ex) {
                    ToastUtil.show(ActivityManage.this, ex.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    /**
     * 加载推荐人
     *
     * @param referrer
     */
    /*public void loadReferrer() {
        setLoding(this, false);
        new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException {
                return userBiz.getReferrer();
            }

            @Override
            protected void onExecuteSucceeded(String data) {
                closeLoding();
                Utils.log("getReferrer:" + data);
                try {
                    Utils.parseResponse(data);
                } catch (BizFailure ex) {
                    ToastUtil.show(ActivityManage.this, ex.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }*/
    public static Intent getIntent(Context ctx, UserModel data) {
        Intent it = new Intent(ctx, ActivityManage.class);
        it.putExtra(TAG_USERINFO, data);
        return it;
    }

}
