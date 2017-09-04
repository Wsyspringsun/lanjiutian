package com.wyw.ljtds.ui.user.order;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.WriteEvaluateModel;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.StarBarView;

import org.kobjects.base64.Base64;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2017/1/16 0016.
 */

@ContentView(R.layout.activity_evaluate)
public class ActivityEvaluate extends BaseActivity implements BGASortableNinePhotoLayout.Delegate, EasyPermissions.PermissionCallbacks {
    @ViewInject(R.id.snpl_moment_add_photos)
    private BGASortableNinePhotoLayout mPhotosSnpl;
    @ViewInject(R.id.et_moment_add_content)
    private EditText mContentEt;
    @ViewInject(R.id.haoping)
    private ImageView haoping;
    @ViewInject(R.id.zhongping)
    private ImageView zhongping;
    @ViewInject(R.id.chaping)
    private ImageView chaping;
    @ViewInject(R.id.header_return_text)
    private TextView title;
    @ViewInject(R.id.header_edit)
    private TextView submit;
    @ViewInject(R.id.group_name)
    private TextView group_name;
    @ViewInject(R.id.star1)
    private StarBarView starBarView1;
    @ViewInject(R.id.star2)
    private StarBarView starBarView2;
    @ViewInject(R.id.star3)
    private StarBarView starBarView3;
    @ViewInject(R.id.star4)
    private StarBarView starBarView4;
    @ViewInject(R.id.star5)
    private StarBarView starBarView5;
    @ViewInject(R.id.iv_adapter_list_pic)
    private SimpleDraweeView iv_adapter_list_pic;


    private static final int REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    private static final int REQUEST_CODE_PHOTO_PREVIEW = 2;

    private int index = 1;//好评度
    private WriteEvaluateModel model;
    private String[] image;//上传的图片数组


    @Event(value = {R.id.header_return, R.id.header_edit, R.id.haoping, R.id.zhongping, R.id.chaping})
    private void onClick(View view) {
        rest();
        switch (view.getId()) {
            case R.id.header_return:
                finish();
                break;

            case R.id.header_edit:
                submit.setEnabled(false);
                ArrayList list = mPhotosSnpl.getData();
//                for (int i = 0; i < list.size(); i++) {
//                    Log.e( "***", list.get( i ).toString() );
//                }

                image = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    Bitmap bitmap = Utils.getSmallBitmap(list.get(i).toString());
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
                    byte[] b = outputStream.toByteArray();
                    String str = new String(Base64.encode(b));  //进行Base64编码
                    image[i] = str;
                }

                model.setBUSINESS_DELIVERY((int) starBarView4.getStarRating() + "");
                model.setBUSINESS_SERVE((int) starBarView3.getStarRating() + "");
                model.setDESCRIBE_IDENTICAL((int) starBarView1.getStarRating() + "");
                model.setEVALUATE_GRADE(index + "");
                model.setLOGISTICS_DELIVERY((int) starBarView5.getStarRating() + "");
                model.setLOGISTICS_SERVE((int) starBarView2.getStarRating() + "");
                model.setVALID_FLG("0");
                model.setEVALUATE_CONTGENT(mContentEt.getText().toString().trim());
                model.setIMG(image);
                String json = GsonUtils.Bean2Json(model);

                writeEvaluate(json, "create");


                break;

            case R.id.haoping:
                index = 1;

                break;

            case R.id.zhongping:
                index = 2;

                break;

            case R.id.chaping:
                index = 3;

                break;
        }

        select(index);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText("商品评价");
        submit.setText(getString(R.string.tijiao));
        submit.setTextColor(getResources().getColor(R.color.base_bar));


        // 设置拖拽排序控件的代理
        mPhotosSnpl.setDelegate(this);

        model = new WriteEvaluateModel();
        model.setCOMMODITY_ORDER_ID(getIntent().getStringExtra(FragmentOrderList.TAG_GROUP_ORDER_ID));
        if (!StringUtils.isEmpty(getIntent().getStringExtra("name"))) {
            group_name.setText(getIntent().getStringExtra("name"));
        }
        if (!StringUtils.isEmpty(getIntent().getStringExtra("image"))) {
            iv_adapter_list_pic.setImageURI(Uri.parse(getIntent().getStringExtra("image")));
        }


    }


    //添加照片
    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        choicePhotoWrapper();
    }

    //删除照片
    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mPhotosSnpl.removeItem(position);
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        startActivityForResult(BGAPhotoPickerPreviewActivity.newIntent(this, mPhotosSnpl.getMaxItemCount(), models, models, position, false), REQUEST_CODE_PHOTO_PREVIEW);
    }


    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        Log.e("***", "是否有权限" + EasyPermissions.hasPermissions(this, perms));
        if (EasyPermissions.hasPermissions(this, perms)) {
            //拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
            File takePhotoDir = new File(AppConfig.EXT_STORAGE_ROOT, AppConfig.CACHE_PIC_ROOT_NAME);
            startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, mPhotosSnpl.getMaxItemCount() - mPhotosSnpl.getItemCount(), null, false), REQUEST_CODE_CHOOSE_PHOTO);
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:拍照,浏览本地图片。", REQUEST_CODE_PERMISSION_PHOTO_PICKER, perms);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_PHOTO) {
            mPhotosSnpl.addMoreData(BGAPhotoPickerActivity.getSelectedImages(data));
        } else if (requestCode == REQUEST_CODE_PHOTO_PREVIEW) {
            mPhotosSnpl.setData(BGAPhotoPickerPreviewActivity.getSelectedImages(data));
        }
    }

    /**
     * EasyPermissions所需
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.e("camera", "成功获取权限");
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CODE_PERMISSION_PHOTO_PICKER) {
            ToastUtil.show(this, "您拒绝了「图片选择」所需要的相关权限!");
            Log.e("*********", perms.toString());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    private void select(int index) {
        switch (index) {
            case 1:
                haoping.setImageDrawable(getResources().getDrawable(R.mipmap.pingjia_hao1));
                break;

            case 2:
                zhongping.setImageDrawable(getResources().getDrawable(R.mipmap.pingjia_zhong1));
                break;

            case 3:
                chaping.setImageDrawable(getResources().getDrawable(R.mipmap.pingjia_cha1));
                break;
        }
    }


    //重置
    private void rest() {
        haoping.setImageDrawable(getResources().getDrawable(R.mipmap.pingjia_hao2));
        zhongping.setImageDrawable(getResources().getDrawable(R.mipmap.pingjia_zhong2));
        chaping.setImageDrawable(getResources().getDrawable(R.mipmap.pingjia_cha2));
    }

    BizDataAsyncTask<String> writeTask;

    private void writeEvaluate(final String data, final String op) {
        writeTask = new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                Log.e(AppConfig.ERR_TAG, data + ":" + op);
                return GoodsBiz.writeEvaluate(data, op);
            }

            @Override
            protected void onExecuteSucceeded(String s) {
                Log.e(AppConfig.ERR_TAG, "onExecuteSucceeded:" + s);
//                Intent intent = new Intent(ActivityEvaluate.this,ActivityOrder.class);
//                intent.putExtra( "index", 0 );
                ToastUtil.show(ActivityEvaluate.this,"评价成功");
                finish();
//                startActivity( intent );
            }

            @Override
            protected void OnExecuteFailed() {

            }
        };
        writeTask.execute();
    }


}
