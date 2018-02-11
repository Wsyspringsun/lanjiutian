package com.wyw.ljtds.ui.user.order;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.GoodsEvaluateModel;
import com.wyw.ljtds.model.GroupEvaluateModel;
import com.wyw.ljtds.model.MyLocation;
import com.wyw.ljtds.model.OrderCommDto;
import com.wyw.ljtds.model.OrderGroupDto;
import com.wyw.ljtds.model.OrderModelInfoMedicine;
import com.wyw.ljtds.model.OrderTradeDto;
import com.wyw.ljtds.model.WriteEvaluateModel;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.MyCallback;
import com.wyw.ljtds.widget.StarBarView;

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
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2017/1/16 0016.
 */

@ContentView(R.layout.activity_evaluate_edit)
public class ActivityEvaluateEdit extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    public static final String TAG_GROUP_ORDER_ID = "com.wyw.ljtds.ui.user.order.BaseActivity.tag_group_order_id";

    @ViewInject(R.id.activity_evaluate_edit_goods)
    RecyclerView ryvGoods;
    @ViewInject(R.id.activity_evaluate_edit_publish)
    TextView tvPublish;
    @ViewInject(R.id.activity_evaluate_edit_star_info)
    RatingBar rbInfo;
    @ViewInject(R.id.activity_evaluate_edit_star_logistic)
    RatingBar rbLogistic;
    @ViewInject(R.id.activity_evaluate_edit_star_service)
    RatingBar rbService;
    @ViewInject(R.id.activity_evaluate_edit_back)
    ImageView imgBack;


    private GroupEvaluateModel modelGroupEvaluate;
    private GoodsEvaluateAdapter adapter;

    private static final int REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    private static final int REQUEST_CODE_PHOTO_PREVIEW = 2;

    PhotoSelectedCallback photoSelectedCallback;
    private boolean isResult = false;

    interface PhotoSelectedCallback {
        void setPhoto(int requestCode, Intent data);
    }

    public static Intent getIntent(Context context, String id) {
        Intent it = new Intent(context, ActivityEvaluateEdit.class);

        it.putExtra(TAG_GROUP_ORDER_ID, id);
        return it;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayoutManager lm = new LinearLayoutManager(this);
        ryvGoods.setLayoutManager(lm);

        tvPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (GoodsEvaluateModel gm : modelGroupEvaluate.getDETAILS()) {
                    gm.setDESCRIBE_IDENTICAL((int) (rbInfo.getRating()) + "");
                    gm.setLOGISTICS_SERVE((int) (rbLogistic.getRating()) + "");
                    gm.setBUSINESS_SERVE((int) (rbService.getRating()) + "");
                }
                modelGroupEvaluate.setGROUP_STATUS("6");
                doPublishEvaluate();
            }
        });
    }

    private void doPublishEvaluate() {
        setLoding(this, false);
        new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                String data = GsonUtils.Bean2Json(modelGroupEvaluate);
                return GoodsBiz.publishEvaluate(data);
            }

            @Override
            protected void onExecuteSucceeded(String s) {
                closeLoding();
                ToastUtil.show(ActivityEvaluateEdit.this, "评价成功");
                finish();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isResult) {
            loadOrderInfo();
        } else {
            isResult = false;
        }
    }

    private void loadOrderInfo() {
        new BizDataAsyncTask<OrderModelInfoMedicine>() {
            @Override
            protected OrderModelInfoMedicine doExecute() throws ZYException, BizFailure {
                String id = getIntent().getStringExtra(TAG_GROUP_ORDER_ID);
                return GoodsBiz.getOrderInfo(id);
            }

            @Override
            protected void onExecuteSucceeded(OrderModelInfoMedicine orderModelInfoMedicine) {
                closeLoding();
                if (orderModelInfoMedicine != null) {
                    modelGroupEvaluate = new GroupEvaluateModel();
                    if (orderModelInfoMedicine.getDETAILS() != null) {
                        List<GoodsEvaluateModel> goodsEvas = new ArrayList<>();
                        for (OrderCommDto m : orderModelInfoMedicine.getDETAILS()) {
                            GoodsEvaluateModel ge = new GoodsEvaluateModel();
                            ge.setCOMMODITY_ID(m.getCOMMODITY_ID());
                            ge.setIMG_PATH(m.getIMG_PATH());
                            goodsEvas.add(ge);
                        }
                        modelGroupEvaluate.setDETAILS(goodsEvas);

                        modelGroupEvaluate.setGROUP_STATUS(orderModelInfoMedicine.getGROUP_STATUS());
                        modelGroupEvaluate.setORDER_GROUP_ID(orderModelInfoMedicine.getORDER_GROUP_ID());
                    }

                }
                bindData2View();
            }

            @Override
            protected void OnExecuteFailed() {

            }
        }.execute();
    }

    private void bindData2View() {
        if (modelGroupEvaluate == null) return;
        if (adapter == null) {
            adapter = new GoodsEvaluateAdapter(this);
            ryvGoods.setAdapter(adapter);
        }
        adapter.models = modelGroupEvaluate.getDETAILS();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    class GoodsEvaluateHolder extends RecyclerView.ViewHolder implements BGASortableNinePhotoLayout.Delegate {
        SimpleDraweeView pic;
        EditText edContent;
        LinearLayout grade;
        BGASortableNinePhotoLayout photo;
        public GoodsEvaluateModel modelGoodsEvaluate;

        @Override
        public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
            String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
            if (EasyPermissions.hasPermissions(ActivityEvaluateEdit.this, perms)) {
                //拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
                File takePhotoDir = new File(AppConfig.EXT_STORAGE_ROOT, AppConfig.CACHE_PIC_ROOT_NAME);
                photoSelectedCallback = new PhotoSelectedCallback() {
                    @Override
                    public void setPhoto(int requestCode, Intent data) {
                        if (requestCode == REQUEST_CODE_CHOOSE_PHOTO) {
                            photo.addMoreData(BGAPhotoPickerActivity.getSelectedImages(data));
                        } else if (requestCode == REQUEST_CODE_PHOTO_PREVIEW) {
                            photo.setData(BGAPhotoPickerPreviewActivity.getSelectedImages(data));
                        }
                        if (photo.getData() != null) {
                            List<String> images = new ArrayList<>();
                            for (int i = 0; i < photo.getData().size(); i++) {
                                String img = photo.getData().get(i);
                                Bitmap bitmap = Utils.getSmallBitmap(img);
                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
                                byte[] b = outputStream.toByteArray();
                                String str = new String(Base64.encode(b));  //进行Base64编码
                                images.add(str);
                                Utils.log(str);
                            }
                            modelGoodsEvaluate.setIMG(images);
                        }

                    }
                };
                startActivityForResult(BGAPhotoPickerActivity.newIntent(ActivityEvaluateEdit.this, takePhotoDir, photo.getMaxItemCount() - photo.getItemCount(), null, false), REQUEST_CODE_CHOOSE_PHOTO);
            } else {
                EasyPermissions.requestPermissions(ActivityEvaluateEdit.this, "图片选择需要以下权限:拍照,浏览本地图片。", REQUEST_CODE_PERMISSION_PHOTO_PICKER, perms);
            }
        }

        @Override
        public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
            photo.removeItem(position);
        }

        @Override
        public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
            startActivityForResult(BGAPhotoPickerPreviewActivity.newIntent(ActivityEvaluateEdit.this, photo.getMaxItemCount(), models, models, position, false), REQUEST_CODE_PHOTO_PREVIEW);
        }

        class RadioChkListener implements View.OnClickListener {
            int index = 0;
            CheckedTextView itemView;
            ViewGroup container;

            public RadioChkListener(ViewGroup container, int index, CheckedTextView gradeItem) {
                this.index = index;
                this.itemView = gradeItem;
                this.container = container;
            }

            @Override
            public void onClick(View view) {
                if (!itemView.isChecked()) {
                    for (int i = 0; i < container.getChildCount(); i++) {
                        if (i == index) continue;
                        CheckedTextView gradeItem = (CheckedTextView) container.getChildAt(i);
                        gradeItem.setChecked(false);
                    }
                    itemView.setChecked(true);
                    GoodsEvaluateHolder.this.modelGoodsEvaluate.setEVALUATE_GRADE(index + "");
                }
            }
        }

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                modelGoodsEvaluate.setEVALUATE_CONTGENT(editable.toString());
            }
        };

        public GoodsEvaluateHolder(View itemView) {
            super(itemView);
            pic = (SimpleDraweeView) itemView.findViewById(R.id.item_goods_evaluate_edit_img);
            grade = (LinearLayout) itemView.findViewById(R.id.item_goods_evaluate_edit_grade);
            photo = (BGASortableNinePhotoLayout) itemView.findViewById(R.id.item_goods_evaluate_edit_addphoto);
            photo.setDelegate(this);
            for (int i = 0; i < grade.getChildCount(); i++) {
                CheckedTextView gradeItem = (CheckedTextView) grade.getChildAt(i);
                gradeItem.setOnClickListener(new RadioChkListener(grade, i, gradeItem));
            }
            edContent = (EditText) itemView.findViewById(R.id.item_goods_evaluate_edit_et_content);
            edContent.addTextChangedListener(textWatcher);
        }

        public void bindData2View() {
            pic.setImageURI(Uri.parse(modelGoodsEvaluate.getIMG_PATH()));
        }
    }

    class GoodsEvaluateAdapter extends RecyclerView.Adapter<GoodsEvaluateHolder> {
        Context context;
        List<GoodsEvaluateModel> models;

        public GoodsEvaluateAdapter(Context context) {
            this.context = context;
        }

        @Override
        public GoodsEvaluateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_goods_evaluate_edit, parent, false);
            return new GoodsEvaluateHolder(v);
        }

        @Override
        public void onBindViewHolder(GoodsEvaluateHolder holder, int position) {
            GoodsEvaluateModel model = models.get(position);
            holder.modelGoodsEvaluate = model;
            holder.bindData2View();
        }

        @Override
        public int getItemCount() {
            if (models == null) return 0;
            return models.size();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        isResult = true;
        if (photoSelectedCallback != null)
            photoSelectedCallback.setPhoto(requestCode, data);
    }

}







