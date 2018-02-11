package com.wyw.ljtds.ui.user.address;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.AddressModel;
import com.wyw.ljtds.model.AreaModel;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.InputMethodUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/1/7 0007.
 */

@ContentView(R.layout.activity_address_add)
public class ActivityAddressEdit extends BaseActivity {
    public static final String TAG_ADDRESS = "com.wyw.ljtds.ui.user.address.ActivityAddressEdit.tag_address";
    final int REQUEST_PROVICE = 0;

    @ViewInject(R.id.header_return_text)
    private TextView title;
    @ViewInject(R.id.header_edit)
    private TextView edit;
    @ViewInject(R.id.shanchu)
    private RelativeLayout shanchu;
    @ViewInject(R.id.xiangxi)
    private EditText xiangxi;
    @ViewInject(R.id.shengshi)
    private TextView shengshi;
    @ViewInject(R.id.phone)
    private EditText phone;
    @ViewInject(R.id.name)
    private EditText name;


    private int from;//判断是从哪个activity过来的  1新增 2编辑 3订单修改地址 4未填写地址
    private static String address_id = "";
    private AddressModel addrModel;
    private boolean creatNew = true;

    @Event(value = {R.id.select_shengshi, R.id.header_return, R.id.header_edit, R.id.shanchu})
    private void onClick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.header_return:
                finish();
                break;

            case R.id.select_shengshi:
                it = AddressMapActivity.getIntent(this);
                startActivityForResult(it, REQUEST_PROVICE);
                break;

            case R.id.header_edit:
                if (StringUtils.isEmpty(name.getText())) {
                    ToastUtil.show(this, getResources().getString(R.string.realname_error1));
                } else if (StringUtils.isEmpty(phone.getText().toString().trim()) || !Utils.validPhoneNum(phone.getText().toString().trim())) {
                    ToastUtil.show(this, getResources().getString(R.string.phone_error));
                } else if (StringUtils.isEmpty(shengshi.getText())) {
                    ToastUtil.show(this, getResources().getString(R.string.shengshi_error));
                } else if (StringUtils.isEmpty(xiangxi.getText())) {
                    ToastUtil.show(this, getResources().getString(R.string.address_error));
                } else {
                    //将经过验证的数据放入模型
                    view2Model();
                    if (creatNew) {
                        add();
                    } else {
                        update();
                    }
                }
                break;

            case R.id.shanchu:
                delete();
                break;
        }
    }

    public static Intent getIntent(Context ctx, AddressModel addrModel) {
        Intent it = new Intent(ctx, ActivityAddressEdit.class);
        it.putExtra(TAG_ADDRESS, addrModel);
        return it;
    }

    private void view2Model() {
        addrModel.setCONSIGNEE_NAME(name.getText().toString().trim());
        addrModel.setCONSIGNEE_MOBILE(phone.getText().toString().trim());
        addrModel.setCONSIGNEE_ADDRESS(xiangxi.getText().toString().trim());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addrModel = new AddressModel();
        title.setText("新增收货地址");
        edit.setText("保存");
        edit.setTextColor(getResources().getColor(R.color.base_bar));

        Intent it = getIntent();
        Parcelable editAddr = it.getParcelableExtra(TAG_ADDRESS);
        if (editAddr != null) {
            //编辑
            title.setText("编辑收货地址");
            creatNew = false;
            addrModel = (AddressModel) editAddr;
            bindData();
        }
    }

    private void bindData() {
        name.setText(addrModel.getCONSIGNEE_NAME());
        phone.setText(addrModel.getCONSIGNEE_MOBILE());
        shengshi.setText(addrModel.getLOCATION());
        xiangxi.setText(addrModel.getCONSIGNEE_ADDRESS());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.canShowAddress = true;
    }


    private void add() {
        setLoding(this, false);
        new BizDataAsyncTask<Integer>() {
            @Override
            protected Integer doExecute() throws ZYException, BizFailure {
                return UserBiz.addUserAddress(addrModel);
            }

            @Override
            protected void onExecuteSucceeded(Integer s) {
                closeLoding();
                ToastUtil.show(ActivityAddressEdit.this, getResources().getString(R.string.add_succeed));
                finish();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }


    private void update() {
        setLoding(this, false);
        new BizDataAsyncTask<Integer>() {
            @Override
            protected Integer doExecute() throws ZYException, BizFailure {
                Log.e(AppConfig.ERR_TAG, "uodate=====" + GsonUtils.Bean2Json(addrModel));
                return UserBiz.updateUserAddress(addrModel);
            }

            @Override
            protected void onExecuteSucceeded(Integer s) {
                closeLoding();
                ToastUtil.show(ActivityAddressEdit.this, getResources().getString(R.string.update_succeed));
                finish();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    BizDataAsyncTask<Integer> deleteTask;

    private void delete() {
        deleteTask = new BizDataAsyncTask<Integer>() {
            @Override
            protected Integer doExecute() throws ZYException, BizFailure {
                return UserBiz.deleteUserAddress(address_id);
            }

            @Override
            protected void onExecuteSucceeded(Integer integer) {
                ToastUtil.show(ActivityAddressEdit.this, getResources().getString(R.string.delete_succeed));
                finish();
                startActivity(new Intent(ActivityAddressEdit.this, ActivityAddress.class));
            }

            @Override
            protected void OnExecuteFailed() {

            }
        };
        deleteTask.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        if (REQUEST_PROVICE == requestCode) {
            String addr = data.getStringExtra(AddressMapActivity.TAG_ADDRESS_POI_ADDRESS);
            String lat = data.getStringExtra(AddressMapActivity.TAG_ADDRESS_POI_LAT);
            shengshi.setText(addr);

            addrModel.setLOCATION(addr);
            addrModel.setADDRESS_LOCATION(addr + lat);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void finshThis() {
        InputMethodUtils.keyBoxIsShow(this);
        AlertDialog alert = new AlertDialog.Builder(this).create();
        if (from == 4) {
            alert.setTitle(R.string.alert_tishi);
            alert.setMessage(getResources().getString(R.string.alert_dizhi));
            alert.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.alert_queding), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alert.show();

        } else {
            alert.setTitle(R.string.alert_tishi);
            alert.setMessage(getResources().getString(R.string.alert_xiugai));
            alert.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.alert_queding), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityAddressEdit.this.finish();
                }
            });
            alert.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.alert_quxiao), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alert.show();

        }
        Button button1 = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        Button button2 = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        button1.setTextColor(getResources().getColor(R.color.base_bar));
        button2.setTextColor(getResources().getColor(R.color.base_bar));
    }
}
