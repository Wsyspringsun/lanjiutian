package com.wyw.ljtds.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.IntegerRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.model.MedicineShop;
import com.wyw.ljtds.model.OrderGroupDto;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.MyCallback;

import java.util.List;

/**
 * Created by wsy on 17-12-26.
 */

public class DiscountDialog extends Dialog {
    private final OrderGroupDto groupData;
    MyCallback callback;

    public void setCallback(MyCallback callback) {
        this.callback = callback;
    }

    private Context context;
    private final ImageView btnQuxiao;
    private final Button btnConfirm;

    public DiscountDialog(@NonNull final Context context, OrderGroupDto group) {
        super(context, R.style.AllScreenWidth_Dialog);
        this.context = context;
        this.groupData = group;
        View dialog = LayoutInflater.from(context).inflate(R.layout.dialog_discount, null);

        final CheckBox youfeiquan = (CheckBox) dialog.findViewById(R.id.dialog_discount_chk_use_youfeiquan);
        if (group.getPOST_NUM() <= 0 || Integer.parseInt(group.getPOSTAGE()) <= 0) {
            youfeiquan.setEnabled(false);
            youfeiquan.setText("不可用");
        } else {
            youfeiquan.setEnabled(true);
            youfeiquan.setText(" ");

            if ("1".equals(group.getPOSTAGE_FLG())) {
                youfeiquan.setChecked(true);
            } else {
                youfeiquan.setChecked(false);
            }
        }


        final CheckBox dianzibi = (CheckBox) dialog.findViewById(R.id.dialog_discount_chk_use_dianzibi);
        if (group.getELECTRONIC_USEABLE_MONEY() <= 0) {
            dianzibi.setEnabled(false);
            dianzibi.setText("不可用");
        } else {
            dianzibi.setEnabled(true);
            dianzibi.setText(" ");
            if ("1".equals(group.getCOIN_FLG())) {
                dianzibi.setChecked(true);
            } else {
                dianzibi.setChecked(false);
                dianzibi.setEnabled(false);
            }
        }


        final CheckBox youhuiquan = (CheckBox) dialog.findViewById(R.id.dialog_discount_chk_use_youhuiquan);
        if (group.getPREFERENTIAL_NUM() <= 0) {
            youhuiquan.setEnabled(false);
            youhuiquan.setText("不可用");
        } else {
            youhuiquan.setEnabled(true);
            youhuiquan.setText(" ");
            if ("1".equals(group.getPREFERENTIAL_FLG())) {
                youhuiquan.setChecked(true);
            } else {
                youhuiquan.setChecked(false);
                youhuiquan.setEnabled(false);
            }
        }

        final RadioGroup rgPoint = (RadioGroup) dialog.findViewById(R.id.dialog_discount_rg_point);
        switch (group.getCOST_POINT()) {
            case 100:
                rgPoint.check(R.id.dialog_discount_jifen_rb2);
                break;
            case 200:
                rgPoint.check(R.id.dialog_discount_jifen_rb3);
                break;
            default:
                rgPoint.check(R.id.dialog_discount_jifen_rb1);
                break;
        }
        int dUserPoint = (int) (Double.parseDouble(group.getUSER_POINT()));
        if (dUserPoint <= 0) {
            Log.e(AppConfig.ERR_TAG, "dUserPoint:" + dUserPoint + " enable false");
            rgPoint.check(R.id.dialog_discount_jifen_rb1);

            rgPoint.getChildAt(0).setEnabled(true);
            rgPoint.getChildAt(1).setEnabled(false);
            rgPoint.getChildAt(2).setEnabled(false);

        } else if (dUserPoint <= 100) {
            rgPoint.getChildAt(0).setEnabled(true);
            rgPoint.getChildAt(1).setEnabled(false);
            rgPoint.getChildAt(2).setEnabled(false);
        } else if (dUserPoint <= 200) {
            rgPoint.getChildAt(0).setEnabled(true);
            rgPoint.getChildAt(1).setEnabled(true);
            rgPoint.getChildAt(2).setEnabled(false);
        } else {
            rgPoint.getChildAt(0).setEnabled(true);
            rgPoint.getChildAt(1).setEnabled(true);
            rgPoint.getChildAt(2).setEnabled(true);
        }


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
