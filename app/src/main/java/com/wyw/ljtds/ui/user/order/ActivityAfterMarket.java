package com.wyw.ljtds.ui.user.order;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.biz.ReturnGoodsBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.GoodsHandingModel;
import com.wyw.ljtds.model.KeyValue;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.Utils;

import org.kobjects.base64.Base64;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2017/1/23 0023.
 */

@ContentView(R.layout.activity_aftermarket)
public class ActivityAfterMarket extends BaseActivity implements BGASortableNinePhotoLayout.Delegate, EasyPermissions.PermissionCallbacks {
    @ViewInject(R.id.header_return_text)
    private TextView title;
    @ViewInject(R.id.header_edit)
    private TextView submit;
    @ViewInject(R.id.tv_returnorder_money)
    private TextView tvReturnOrderMoney;
    @ViewInject(R.id.et_return_msg)
    EditText etReturnMsg;
    @ViewInject(R.id.sp_return_reason)
    private Spinner spReturnReason;
    @ViewInject(R.id.sp_return_cat)
    private Spinner spReturnCat;
    /**
     * 拖拽排序九宫格控件
     */
    @ViewInject(R.id.snpl_moment_add_photos)
    private BGASortableNinePhotoLayout mPhotosSnpl;


    private static final int REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    private static final int REQUEST_CODE_PHOTO_PREVIEW = 2;

    private static final String EXTRA_MOMENT = "EXTRA_MOMENT";

    private int index = 1;
    private GoodsHandingModel goodsHandingModel;
    private String orderId;

    @Event(value = {R.id.header_return, R.id.header_edit})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_return:
                finish();
                break;

            case R.id.header_edit:
                ArrayList list = mPhotosSnpl.getData();
//                for (int i = 0; i < list.size(); i++) {
//                    Log.e( "***", list.get( i ).toString() );
//                }

                String[] image = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    Bitmap bitmap = Utils.getSmallBitmap(list.get(i).toString());
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
                    byte[] b = outputStream.toByteArray();
                    String str = new String(Base64.encode(b));  //进行Base64编码
                    image[i] = str;
                }

                goodsHandingModel = new GoodsHandingModel();
                goodsHandingModel.setImgs(image);
//                goodsHandingModel.setCommodityOrderId(getIntent().getStringExtra("good_id"));
                goodsHandingModel.setOrderGroupId(orderId);
                String rOc = ((KeyValue) spReturnCat.getSelectedItem()).getK();
                goodsHandingModel.setReturnOrChange(rOc);
                String returnReason = ((KeyValue) spReturnReason.getSelectedItem()).getK();
                goodsHandingModel.setReturnReason(returnReason);
                goodsHandingModel.setReturnRemarks(etReturnMsg.getText().toString());
                String json = GsonUtils.Bean2Json(goodsHandingModel);
                Log.e(AppConfig.ERR_TAG, json);
                read(json);
                break;

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        orderId = getIntent().getStringExtra(ActivityOrderInfo.TAG_ORDER_INFO_ID);

        title.setText("申请售后");
        submit.setText(getString(R.string.tijiao));
        submit.setTextColor(getResources().getColor(R.color.base_bar));
        // 设置拖拽排序控件的代理
        mPhotosSnpl.setDelegate(this);

        List<KeyValue> keyValues = new ArrayList<>();
        keyValues.add(new KeyValue("0", "退货退款"));
        keyValues.add(new KeyValue("1", "退货换货"));
        ArrayAdapter<KeyValue> spRCAdapter = new ArrayAdapter<>(ActivityAfterMarket.this, android.R.layout.simple_spinner_item, keyValues);
        spRCAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spReturnCat.setAdapter(spRCAdapter);

        initData();
    }

    private void initData() {
        new BizDataAsyncTask<List<KeyValue>>() {
            @Override
            protected List<KeyValue> doExecute() throws ZYException, BizFailure {
                return GoodsBiz.readReturnReasonList();
            }

            @Override
            protected void onExecuteSucceeded(List<KeyValue> keyValues) {
                ArrayAdapter<KeyValue> adapter = new ArrayAdapter<>(ActivityAfterMarket.this, android.R.layout.simple_spinner_item, keyValues);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spReturnReason.setAdapter(adapter);
            }

            @Override
            protected void OnExecuteFailed() {

            }
        }.execute();
        new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                return ReturnGoodsBiz.getReturnMoney(orderId);
            }

            @Override
            protected void onExecuteSucceeded(String s) {
                tvReturnOrderMoney.setText(s);
            }

            @Override
            protected void OnExecuteFailed() {

            }
        }.execute();
    }


    BizDataAsyncTask<Object> task;

    private void read(final String data) {
        task = new BizDataAsyncTask<Object>() {
            @Override
            protected Object doExecute() throws ZYException, BizFailure {
                return GoodsBiz.returnGoodsHanding(data, "create");
            }

            @Override
            protected void onExecuteSucceeded(Object o) {
                Log.e(AppConfig.ERR_TAG, "........returngoodsSubmit:" + o);
                if ("1".equals(o + "")) {
                    Intent it = new Intent(ActivityAfterMarket.this, ReturnGoodsOrderListActivity.class);
                    startActivity(it);
                    ActivityAfterMarket.this.finish();
                }
            }

            @Override
            protected void OnExecuteFailed() {

            }
        };
        task.execute();
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        choicePhotoWrapper();
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mPhotosSnpl.removeItem(position);
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        startActivityForResult(BGAPhotoPickerPreviewActivity.newIntent(this, mPhotosSnpl.getMaxItemCount(), models, models, position, false), REQUEST_CODE_PHOTO_PREVIEW);
    }

    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_PHOTO_PICKER)
    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        Log.e("-------", EasyPermissions.hasPermissions(this, perms) + "");
        if (EasyPermissions.hasPermissions(this, perms)) {
            //拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
            File takePhotoDir = new File(AppConfig.EXT_STORAGE_ROOT, AppConfig.CACHE_ROOT_NAME);

            startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, mPhotosSnpl.getMaxItemCount() - mPhotosSnpl.getItemCount(), null, false), REQUEST_CODE_CHOOSE_PHOTO);
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:拍照,浏览本地图片。", REQUEST_CODE_PERMISSION_PHOTO_PICKER, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


}
