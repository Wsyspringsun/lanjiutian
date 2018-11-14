package com.wyw.ljtds.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.model.OrderGroupDto;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.MyCallback;

/**
 * Created by wsy on 17-12-26.
 */

public class DiscountDialog extends Dialog {
    private final OrderGroupDto groupData;
    private final View jifenDiv;
    MyCallback callback;

    private CheckBox youfeiquan; //邮费券选择框
    private CheckBox dianzibi; //电子币选择
    private CheckBox youhuiquan; //优惠券选择

    private RadioGroup rgPoint;

    public void setCallback(MyCallback callback) {
        this.callback = callback;
    }

    private Context context;
    private final ImageView btnQuxiao;
    private final Button btnConfirm;

    public DiscountDialog(@NonNull final Context context, final OrderGroupDto group) {
        super(context, R.style.AllScreenWidth_Dialog);
        this.context = context;
        this.groupData = group;

        View dialog = LayoutInflater.from(context).inflate(R.layout.dialog_discount, null);
        jifenDiv = dialog.findViewById(R.id.dialog_discount_ll_point);

        youfeiquan = (CheckBox) dialog.findViewById(R.id.dialog_discount_chk_use_youfeiquan);
        dianzibi = (CheckBox) dialog.findViewById(R.id.dialog_discount_chk_use_dianzibi);
        youhuiquan = (CheckBox) dialog.findViewById(R.id.dialog_discount_chk_use_youhuiquan);
        rgPoint = (RadioGroup) dialog.findViewById(R.id.dialog_discount_rg_point);

        btnConfirm = (Button) dialog.findViewById(R.id.dialog_discount_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (youfeiquan.isEnabled()) {
                    if (youfeiquan.isChecked()) {
                        groupData.setPOSTAGE_FLG("1");
                    } else {
                        groupData.setPOSTAGE_FLG("0");
                    }
                } else {
                    groupData.setPOSTAGE_FLG("2");
                }


                if (dianzibi.isEnabled()) {
                    if (dianzibi.isChecked()) {
                        groupData.setCOIN_FLG("1");
                    } else {
                        groupData.setCOIN_FLG("0");
                    }
                } else {
                    groupData.setCOIN_FLG("2");
                }


                if (youhuiquan.isEnabled()) {
                    if (youhuiquan.isChecked()) {
                        groupData.setPREFERENTIAL_FLG("1");
                    } else {
                        groupData.setPREFERENTIAL_FLG("0");
                    }
                }else{
                    groupData.setPREFERENTIAL_FLG("2");
                }

                switch (rgPoint.getCheckedRadioButtonId()) {
                    case R.id.dialog_discount_jifen_rb2:
                        groupData.setCOST_POINT(100);
                        break;
                    case R.id.dialog_discount_jifen_rb3:
                        groupData.setCOST_POINT(200);
                        break;
                    default:
                        groupData.setCOST_POINT(0);
                        break;
                }

                if (callback != null) {
                    callback.callback();
                    DiscountDialog.this.dismiss();
                }
            }
        });
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

    @Override
    protected void onStart() {
        super.onStart();

        if (AppConfig.GROUP_LJT.equals(groupData.getINS_USER_ID())) {
            //医药馆不适用积分
            jifenDiv.setVisibility(View.GONE);
        } else {
            //生活馆使用积分
            jifenDiv.setVisibility(View.GONE);
        }

//        if (groupData.getPOST_NUM() <= 0 || Integer.parseInt(groupData.getPOSTAGE()) <= 0) {
        youfeiquan.setEnabled(true);
        youfeiquan.setText(" ");

        if ("1".equals(groupData.getPOSTAGE_FLG())) {
            youfeiquan.setChecked(true);
        } else if ("0".equals(groupData.getPOSTAGE_FLG())) {
            youfeiquan.setChecked(false);
        } else {
            youfeiquan.setEnabled(false);
            youfeiquan.setText("不可用");
        }

//        if (groupData.getELECTRONIC_USEABLE_MONEY() <= 0) {

        dianzibi.setEnabled(true);
        dianzibi.setText(" ");
        if ("1".equals(groupData.getCOIN_FLG())) {
            dianzibi.setChecked(true);
        } else if ("0".equals(groupData.getCOIN_FLG())) {
            dianzibi.setChecked(false);
        } else {
            dianzibi.setEnabled(false);
            dianzibi.setText("不可用");
        }

//        if (groupData.getPREFERENTIAL_NUM() <= 0) {

        youhuiquan.setEnabled(true);
        youhuiquan.setText(" ");
        if ("1".equals(groupData.getPREFERENTIAL_FLG())) {
            youhuiquan.setChecked(true);
        } else if ("0".equals(groupData.getPREFERENTIAL_FLG())) {
            youhuiquan.setChecked(false);
        } else {
            youhuiquan.setEnabled(false);
            youhuiquan.setText("不可用");
        }


        /*
        switch (groupData.getCOST_POINT()) {
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
        int dUserPoint = (int) (Double.parseDouble(groupData.getUSER_POINT()));
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
        }*/
    }
}
