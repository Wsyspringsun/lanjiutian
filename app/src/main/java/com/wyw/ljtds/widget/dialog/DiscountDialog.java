package com.wyw.ljtds.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.DataListAdapter;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.model.MedicineShop;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.widget.MyCallback;

import java.util.List;

/**
 * Created by wsy on 17-12-26.
 */

public class DiscountDialog extends Dialog {
    MyCallback callback;

    public void setCallback(MyCallback callback) {
        this.callback = callback;
    }

    private Context context;
    private final ImageView btnQuxiao;
    private final Button btnConfirm;

    public DiscountDialog(@NonNull final Context context) {
        super(context, R.style.AllScreenWidth_Dialog);

        this.context = context;
        View dialog = LayoutInflater.from(context).inflate(R.layout.dialog_discount, null);

        final CheckBox youfeiquan = (CheckBox) dialog.findViewById(R.id.dialog_discount_chk_use_youfeiquan);
        final CheckBox dianzibi = (CheckBox) dialog.findViewById(R.id.dialog_discount_chk_use_dianzibi);
        final CheckBox youhuiquan = (CheckBox) dialog.findViewById(R.id.dialog_discount_chk_use_youhuiquan);
        final RadioGroup rgPoint = (RadioGroup) dialog.findViewById(R.id.dialog_discount_rg_point);


        btnConfirm = (Button) dialog.findViewById(R.id.dialog_discount_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.callback(youfeiquan, dianzibi, youhuiquan, rgPoint);
                }
            }
        });
        ;
        btnQuxiao = (ImageView) dialog.findViewById(R.id.dialog_discount_quxiao);
        btnQuxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiscountDialog.this.dismiss();
            }
        });

        this.setContentView(dialog);
        this.setCancelable(true);
        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        //显示的坐标
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //内容 透明度
        //        lp.alpha = 0.2f;
        lp.width = MyApplication.screenWidth;
//        lp.height = MyApplication.screenHeight / 2;
        //遮罩 透明度
//        lp.dimAmount = 0.2f;
        dialogWindow.setAttributes(lp);
    }


}
