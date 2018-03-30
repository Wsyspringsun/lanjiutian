package com.wyw.ljtds.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.model.OrderTrade;
import com.wyw.ljtds.model.OrderTradeDto;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.dialog.BottomDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wsy on 17-7-10.
 */

public class PayDialog {

    public PayDialog(Context activity, OrderTrade orderTrade, PayDialog.PayMethodSelectedListener dialogListener) {
        this.activity = activity;
        this.orderTrade = orderTrade;
        this.dialogListener = dialogListener;
    }

    private Context activity;
    private OrderTrade orderTrade;
    private PayDialog.PayMethodSelectedListener dialogListener;

    public interface PayMethodSelectedListener {
        public void onItemSelected(OrderTrade order);
        public void onDialogClose();
    }

    private Map<String, CheckBox> chkList = new HashMap<>();

    private void resetChk() {
        if (chkList == null || chkList.isEmpty())
            return;
        for (CheckBox cb : chkList.values()) {
            cb.setChecked(false);
        }
        if (!chkList.containsKey(orderTrade.getPaymentMethod())){
            orderTrade.setPaymentMethod(OrderTrade.PAYMTD_ALI);
        }
        CheckBox chk = chkList.get(orderTrade.getPaymentMethod());
        chk.setChecked(true);
    }

    public void initDialog() {
        View viewSelOnlinePayMtd = LayoutInflater.from(activity).inflate(R.layout.dialog_pay_select, null);
        final BottomDialog dialog_pay_select = new BottomDialog(activity, viewSelOnlinePayMtd).setCancelable(false).show();
        ImageView quxiao = (ImageView) viewSelOnlinePayMtd.findViewById(R.id.quxiao);
        TextView money = (TextView) viewSelOnlinePayMtd.findViewById(R.id.money);
        money.setText("需付款：￥" + Utils.formatFee(orderTrade.getPayAmount()) + "");
        LinearLayout select1 = (LinearLayout) viewSelOnlinePayMtd.findViewById(R.id.select1);
        LinearLayout select2 = (LinearLayout) viewSelOnlinePayMtd.findViewById(R.id.select2);
        LinearLayout select3 = (LinearLayout) viewSelOnlinePayMtd.findViewById(R.id.select3);
        LinearLayout select4 = (LinearLayout) viewSelOnlinePayMtd.findViewById(R.id.dialog_pay_select_wechat);
        //账户余额 支付
        final CheckBox checkBox1 = (CheckBox) viewSelOnlinePayMtd.findViewById(R.id.check1);
        chkList.put(OrderTrade.PAYMTD_ACCOUNT, checkBox1);
        //支付宝 支付
        final CheckBox checkBox2 = (CheckBox) viewSelOnlinePayMtd.findViewById(R.id.check2);
        chkList.put(OrderTrade.PAYMTD_ALI, checkBox2);
        //银联 支付
        final CheckBox checkBox3 = (CheckBox) viewSelOnlinePayMtd.findViewById(R.id.check3);
        chkList.put(OrderTrade.PAYMTD_UNION, checkBox3);
        //微信 支付
        final CheckBox checkBox4 = (CheckBox) viewSelOnlinePayMtd.findViewById(R.id.dialog_pay_chk_wechat);
        chkList.put(OrderTrade.PAYMTD_WECHAT, checkBox4);

        resetChk();

        Button btnPay = (Button) viewSelOnlinePayMtd.findViewById(R.id.zhifu);


        //取消支付
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_pay_select.dissmiss();
                dialogListener.onDialogClose();
            }
        });

        select1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                orderTrade.setPaymentMethod(OrderTrade.PAYMTD_ACCOUNT);
                resetChk();
            }
        });

        select2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderTrade.setPaymentMethod(OrderTrade.PAYMTD_ALI);
                resetChk();
            }
        });

        select3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderTrade.setPaymentMethod(OrderTrade.PAYMTD_UNION);
                resetChk();
            }
        });

        select4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderTrade.setPaymentMethod(OrderTrade.PAYMTD_WECHAT);
                resetChk();
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_pay_select.dissmiss();
                dialogListener.onItemSelected(orderTrade);
            }
        });
    }

    public static void showDialog(final BaseActivity activity, final OrderTrade orderTrade, final PayDialog.PayMethodSelectedListener dialogListener) {
        PayDialog signle = new PayDialog(activity, orderTrade, dialogListener);
        signle.initDialog();
    }


}
