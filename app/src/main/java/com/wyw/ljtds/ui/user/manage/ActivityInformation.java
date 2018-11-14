package com.wyw.ljtds.ui.user.manage;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigButton;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.ButtonParams;
import com.mylhyl.circledialog.params.DialogParams;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.SingleCurrentUser;
import com.wyw.ljtds.model.UserModel;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.DateUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.widget.CutImage;
import com.wyw.ljtds.widget.picker.TimePickerDialog;

import org.kobjects.base64.Base64;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2017/5/25 0025.
 */

@ContentView(R.layout.activity_user_information)
public class ActivityInformation extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    private static final String BIRTHDAY_TIP = "点击设置生日";
    private static final int CHANGE_SEX = 0;
    private static final int CHANGE_BIRTHDAY = 1;
    private static final int CHANGE_NICKNAME = 2;
    @ViewInject(R.id.birthday_tv)
    private TextView birthday_tv;
    @ViewInject(R.id.activity_user_infomation_sdv_headphoto)
    private SimpleDraweeView headPhoto;
    @ViewInject(R.id.header_title)
    private TextView title;
    @ViewInject(R.id.name)
    private TextView name;
    @ViewInject(R.id.nicheng_tv)
    private TextView nicheng_tv;
    @ViewInject(R.id.sex_tv)
    private TextView sex_tv;
    private EditText et;
    private int index = 0;
    private CutImage cutImage = null;//选择剪切头像
    private static final int REQUEST_CODE_PERMISSION_CAMERA = 1;
    private Bitmap bitmap;

    @Event(value = {R.id.birthday, R.id.touxiang, R.id.nicheng, R.id.header_return, R.id.sex})
    private void onClick(View view) {

        switch (view.getId()) {
            case R.id.header_return:
                setResult(AppConfig.IntentExtraKey.RESULT_OK);
                finish();
                break;

            case R.id.birthday:
                final TimePickerDialog time_Dialog = new TimePickerDialog(this);
                time_Dialog.setCallback(new TimePickerDialog.OnClickCallback() {
                    @Override
                    public void onCancel() {
                        time_Dialog.dismiss();
                    }

                    @Override
                    public void onSure(int year, int month, int day, long time) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String date = dateFormat.format(time);
                        add(date, CHANGE_BIRTHDAY);
                    }
                });
                time_Dialog.show();
                break;

            case R.id.touxiang:
                camera();
                break;

            case R.id.nicheng:
                et = new EditText(this);

                new AlertDialog.Builder(this).setTitle("请输入昵称")
                        .setView(et).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (StringUtils.isEmpty(et.getText().toString().trim())) {
                            ToastUtil.show(ActivityInformation.this, "昵称不能为空");
                        } else {
                            add(et.getText().toString(), CHANGE_NICKNAME);
                        }

                    }
                }).setNegativeButton("取消", null).show();
                break;

            case R.id.sex:
                final String[] items = {"男", "女"};
                new AlertDialog.Builder(this).setTitle("请选择性别")
                        .setSingleChoiceItems(items, index, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                index = i;
                                add(items[i], CHANGE_SEX);
                            }
                        }).show();


        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText(R.string.gerenxinxi);
        cutImage = new CutImage(this);

        UserModel user = SingleCurrentUser.userInfo;
        if (user == null) return;
        name.setText(user.getMOBILE());
        headPhoto.setImageURI(Uri.parse(AppConfig.IMAGE_PATH_LJT + user.getUSER_ICON_FILE_ID()));
        nicheng_tv.setText(user.getNICKNAME());
        if (user.getBIRTHDAY()!=null && user.getBIRTHDAY() != 0) {
            birthday_tv.setText(DateUtils.parseTime(user.getBIRTHDAY() + ""));
        } else {
            birthday_tv.setText(BIRTHDAY_TIP);
        }
        if (StringUtils.isEmpty(user.getSEX())) {
            sex_tv.setText("未知");
            index = 0;
        } else {
            if (user.getSEX().equals("男") || user.getSEX().equals("0")) {
                sex_tv.setText("男");
                index = 0;
            } else {
                sex_tv.setText("女");
                index = 1;
            }
        }

    }

    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_CAMERA)
    private void camera() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            opimgs();
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:拍照,浏览本地图片。", REQUEST_CODE_PERMISSION_CAMERA, perms);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Log.e("****", "****");
        }
        bitmap = cutImage.onResult(requestCode, resultCode, data);
        if (bitmap != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream);
            byte[] b = outputStream.toByteArray();
            String str = new String(Base64.encode(b));  //进行Base64编码
            setLoding(this, false);
            puticon(str);
        }


    }

    BizDataAsyncTask<Integer> addTask;

    /**
     * @param nc 暱称
     * @param sr 生日
     * @param xb 性别
     */
    private void add(final String val, final int type) {
        setLoding(ActivityInformation.this, false);
        new BizDataAsyncTask<Integer>() {
            @Override
            protected Integer doExecute() throws ZYException, BizFailure {
                if (SingleCurrentUser.userInfo == null) return -1;
                String birthday = "";
                if (SingleCurrentUser.userInfo.getBIRTHDAY() != null && SingleCurrentUser.userInfo.getBIRTHDAY() != 0) {
                    birthday = DateUtils.parseTime(SingleCurrentUser.userInfo.getBIRTHDAY() + "");
                }
                switch (type) {
                    case CHANGE_SEX:
                        return UserBiz.addPerInfomation(SingleCurrentUser.userInfo.getNICKNAME(), birthday, val);
                    case CHANGE_BIRTHDAY:
                        return UserBiz.addPerInfomation(SingleCurrentUser.userInfo.getNICKNAME(), val, SingleCurrentUser.userInfo.getSEX());
                    case CHANGE_NICKNAME:
                        return UserBiz.addPerInfomation(val, birthday, SingleCurrentUser.userInfo.getSEX());
                    default:
                        return -1;
                }
            }

            @Override
            protected void onExecuteSucceeded(Integer s) {
                if (SingleCurrentUser.userInfo == null) return;
                if (s == 1) {
                    //更新成功
                    switch (type) {
                        case CHANGE_SEX:
                            SingleCurrentUser.userInfo.setSEX(val);
                            sex_tv.setText(val);
                            break;
                        case CHANGE_BIRTHDAY:
                            long dateBirthday = 0;
                            try {
                                SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");
                                dateBirthday = sdf.parse(val).getTime();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            SingleCurrentUser.userInfo.setBIRTHDAY(dateBirthday);
                            birthday_tv.setText(val);
                            break;
                        case CHANGE_NICKNAME:
                            SingleCurrentUser.userInfo.setNICKNAME(val);
                            nicheng_tv.setText(val);
                            break;
                        default:
                            break;
                    }
                }
                closeLoding();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bitmap = null;
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


    BizDataAsyncTask<String> writeTask;

    private void puticon(final String data) {
        writeTask = new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                return UserBiz.putIcon(data);
            }

            @Override
            protected void onExecuteSucceeded(final String s) {
                SingleCurrentUser.userInfo.setUSER_ICON_FILE_ID(s);
                headPhoto.setImageURI(Uri.parse(AppConfig.IMAGE_PATH_LJT + s));
                closeLoding();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        };
        writeTask.execute();
    }

    private void opimgs() {
        final String[] items = {"打开相机", "本地相册"};
        new CircleDialog.Builder(this)
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        //增加弹出动画
                        params.animStyle = R.style.popWindow_anim_style;
                    }
                })
                .setItems(items, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            cutImage.openCamera();
                        } else if (position == 1) {
                            cutImage.openAlbums();
                        }

                    }
                })
                .setNegative("取消", null)
                .configNegative(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        //取消按钮字体颜色
                        params.textColor = getResources().getColor(R.color.base_bar);
                    }
                })
                .show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(AppConfig.IntentExtraKey.RESULT_OK);
            finish();

        }
        return super.onKeyDown(keyCode, event);
    }
}
