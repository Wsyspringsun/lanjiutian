package com.wyw.ljtds.ui.user.manage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.SingleCurrentUser;
import com.wyw.ljtds.model.UserModel;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.StringUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * create by wsy on 2017-07-28
 */
@ContentView(R.layout.fragment_user_qrcode)
public class QrCodeActivity extends BaseActivity {
    @ViewInject(R.id.fragment_user_qrcode_photo)
    ImageView imgPhoto;
    @ViewInject(R.id.fragment_user_qrcode_idcode)
    ImageView imgIdcode;
    @ViewInject(R.id.fragment_user_barcode_idcode)
    ImageView imgIdcodeBarcode;
    @ViewInject(R.id.fragment_user_qrcode_name)
    TextView txtName;
    @ViewInject(R.id.header_title)
    private TextView title;

    private UserModel user;
    Bitmap bitmap;
    Bitmap bitmapBarcode;

    @Event(value = {R.id.header_return})
    private void onClick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.header_return:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText(R.string.myqrcode);


        user = SingleCurrentUser.userInfo;
        if (user == null) {
            return;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
        if (!StringUtils.isEmpty(user.getUSER_NAME())) {
            txtName.setText(user.getNICKNAME());
        }
        //头像图片
        if (!StringUtils.isEmpty(user.getUSER_ICON_FILE_ID())) {
            Picasso.with(this).load(Uri.parse(AppConfig.IMAGE_PATH_LJT + user.getUSER_ICON_FILE_ID())).placeholder(R.mipmap.zhanweitu).into(imgPhoto);
        }
        String id = user.getOID_USER_ID();
        imgIdcode.setDrawingCacheEnabled(true);
//        Bitmap logo = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_biaozhi)).getBitmap();
        Bitmap photoBitmap = ((BitmapDrawable) imgPhoto.getDrawable()).getBitmap();
        int start = 3;
        Bitmap logo = Bitmap.createBitmap(photoBitmap.getWidth() + start * 2, photoBitmap.getHeight() + start * 2, Bitmap.Config.RGB_565);
        logo.eraseColor(Color.WHITE);
        Canvas canvas = new Canvas(logo);
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        canvas.drawBitmap(photoBitmap, start, start, p);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        bitmap = QRCodeEncoder.syncEncodeQRCode("1#" + id, 150, Color.BLACK, logo);

        imgIdcode.setImageBitmap(bitmap);

        //条形码
        bitmapBarcode = QRCodeEncoder.syncEncodeBarcode(id, 1000, 120, 0);
        imgIdcodeBarcode.setImageBitmap(bitmapBarcode);
    }


    public static Intent getIntent(Context ctx) {
        Intent it = new Intent(ctx, QrCodeActivity.class);
        return it;
    }

}
