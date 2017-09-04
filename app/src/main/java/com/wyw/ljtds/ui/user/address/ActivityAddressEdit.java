package com.wyw.ljtds.ui.user.address;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wyw.ljtds.MainActivity;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.AppManager;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.InputMethodUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2017/1/7 0007.
 */

@ContentView(R.layout.activity_address_add)
public class ActivityAddressEdit extends BaseActivity {
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

    @Event(value = {R.id.select_shengshi, R.id.header_return, R.id.header_edit, R.id.shanchu})
    private void onClick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.header_return:
                finshThis();
                break;

            case R.id.select_shengshi:
                it = new Intent(this, ActivityProvince.class);
                it.putExtra("address_id", address_id);
                it.putExtra(AppConfig.IntentExtraKey.ADDRESS_FROM, from);

                Bundle bundle = new Bundle();
                bundle.putString("name", name.getText().toString().trim());
                bundle.putString("phone", phone.getText().toString().trim());
                bundle.putString("xiangxi", xiangxi.getText().toString().trim());
                bundle.putString("shengshi", shengshi.getText().toString());
//                if (from==2){
//                    it.putExtra( AppConfig.IntentExtraKey.ADDRESS_FROM, 2 );
//                }
                it.putExtra("bundle", bundle);
                AppManager.addDestoryActivity(ActivityAddressEdit.this, "addressEdit");
                startActivity(it);
                break;

            case R.id.header_edit:
                if (StringUtils.isEmpty(name.getText())) {
                    ToastUtil.show(this, getResources().getString(R.string.realname_error1));
                } else if (StringUtils.isEmpty(phone.getText())) {
                    ToastUtil.show(this, getResources().getString(R.string.phone_error));
                } else if (StringUtils.isEmpty(xiangxi.getText())) {
                    ToastUtil.show(this, getResources().getString(R.string.address_error));
                } else {
                    if (from == 4 || from == 1) {
                        add(from);
                    } else if (from == 3 || from == 2) {
                        update();
                    }
                }
                break;

            case R.id.shanchu:
                delete();
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLoding(this, false);

        title.setText("新增收货地址");
        edit.setText("保存");
        edit.setTextColor(getResources().getColor(R.color.base_bar));

        from = getIntent().getIntExtra(AppConfig.IntentExtraKey.ADDRESS_FROM, 0);
        Log.e("from", from + "");
//        if (from == 1 || from == 4) {
//            shanchu.setVisibility( View.GONE );
//        }

        if (from == 2 || from == 3) {
//            shanchu.setVisibility( View.VISIBLE );
            address_id = getIntent().getStringExtra("address_id");
            Bundle bundle = getIntent().getBundleExtra("bundle");
            if (bundle != null) {
                name.setText(bundle.getString("name"));
                phone.setText(bundle.getString("phone"));
                xiangxi.setText(bundle.getString("xiangxi"));
                shengshi.setText(bundle.getString("shengshi"));
            }
        } else if (from == 1 || from == 4) {
            Bundle bundle = getIntent().getBundleExtra("bundle");
            if (bundle != null) {
                name.setText(bundle.getString("name"));
                phone.setText(bundle.getString("phone"));
                xiangxi.setText(bundle.getString("xiangxi"));
                shengshi.setText(getIntent().getStringExtra("name_p") + getIntent().getStringExtra("name_c"));
            }
        }


        closeLoding();
    }

    BizDataAsyncTask<Integer> addTask;

    private void add(final int from) {
        addTask = new BizDataAsyncTask<Integer>() {
            @Override
            protected Integer doExecute() throws ZYException, BizFailure {
                return UserBiz.addUserAddress(name.getText().toString().trim(), phone.getText().toString().trim(), "048000",
                        getIntent().getStringExtra("id_p"), getIntent().getStringExtra("id_c"), xiangxi.getText().toString().trim());
            }

            @Override
            protected void onExecuteSucceeded(Integer s) {
                ToastUtil.show(ActivityAddressEdit.this, getResources().getString(R.string.add_succeed));
                finish();
                if (from == 1) {
                    InputMethodUtils.keyBoxIsShow(ActivityAddressEdit.this);
                    finish();
                    startActivity(new Intent(ActivityAddressEdit.this, ActivityAddress.class));
                } else if (from == 4) {
                    InputMethodUtils.keyBoxIsShow(ActivityAddressEdit.this);
                    Intent it = new Intent(AppConfig.AppAction.Base_ACTION_PREFIX + "refresh");
                    MyApplication.getAppContext().sendBroadcast(it);
                    finish();
                }
            }

            @Override
            protected void OnExecuteFailed() {

            }
        };
        addTask.execute();
    }

    BizDataAsyncTask<Integer> updateTask;

    private void update() {
        updateTask = new BizDataAsyncTask<Integer>() {
            @Override
            protected Integer doExecute() throws ZYException, BizFailure {
                Log.e("uodate=====", address_id + ";" + getIntent().getStringExtra("id_p") + ";" + getIntent().getStringExtra("id_c"));
                return UserBiz.updateUserAddress(address_id, name.getText().toString().trim(),
                        phone.getText().toString().trim(), "048000", getIntent().getStringExtra("id_p"), getIntent().getStringExtra("id_c"),
                        xiangxi.getText().toString().trim());
            }

            @Override
            protected void onExecuteSucceeded(Integer s) {
                ToastUtil.show(ActivityAddressEdit.this, getResources().getString(R.string.update_succeed));
                if (from == 2) {
                    InputMethodUtils.keyBoxIsShow(ActivityAddressEdit.this);
//                    setResult( AppConfig.IntentExtraKey.RESULT_OK );
                    finish();
                    startActivity(new Intent(ActivityAddressEdit.this, ActivityAddress.class));
                } else if (from == 3) {

                    InputMethodUtils.keyBoxIsShow(ActivityAddressEdit.this);
                    Intent mIntent = new Intent();
                    mIntent.putExtra("refresh", true);
                    setResult(AppConfig.IntentExtraKey.RESULT_OK, mIntent);
                    Log.e("aaaaaaaaaaaaaa", "aaaaaaaaaaaaa");
                    finish();
                }
            }

            @Override
            protected void OnExecuteFailed() {

            }
        };
        updateTask.execute();
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finshThis();
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
