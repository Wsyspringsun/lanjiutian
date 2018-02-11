package com.wyw.ljtds.ui.goods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.CreatOrderModel;
import com.wyw.ljtds.model.OrderTrade;
import com.wyw.ljtds.model.OrderTradeDto;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

@ContentView(R.layout.dialog_pay_choice)
public class ActivityGoodsSubmitChoice extends BaseActivity {
    public static final String TAG_CREATE_ORDER_MODEL = "cOrderModel";
    public static final String TAG_ZHIFU = "TAG_ZHIFU";
    public static final String TAG_PEISONG = "TAG_PEISONG";
    public static final String TAG_IIME_START = "TAG_IIME_START";
    public static final String TAG_IIME_END = "TAG_IIME_END";
    @ViewInject(R.id.header_title)
    private TextView title;
    @ViewInject(R.id.time1)
    private Spinner spinner1;
    @ViewInject(R.id.time2)
    private Spinner spinner2;
    @ViewInject(R.id.dialog_pay_choice_zhifu)
    RadioGroup rgZhifu;
    @ViewInject(R.id.dialog_pay_choice_peisong)
    RadioGroup rgPeisong;

    private ArrayAdapter adapter1;

    @Event(value = {R.id.queding, R.id.header_return})
    private void onClick(View v) {
        Intent mIntent = new Intent();

        if (rgZhifu.getCheckedRadioButtonId() == R.id.zhifu_rb1) {
            //在线支付
            mIntent.putExtra(TAG_ZHIFU, "0");
        } else {
            //货到付款
            mIntent.putExtra(TAG_ZHIFU, "C");
        }

        if (rgPeisong.getCheckedRadioButtonId() == R.id.dialog_pay_choice_peisong_send) {
            //送货上门
            mIntent.putExtra(TAG_PEISONG, "0");
        } else {
            //门店自取
            mIntent.putExtra(TAG_PEISONG, "1");
        }

        mIntent.putExtra(TAG_IIME_START, spinner1.getSelectedItem().toString());
        mIntent.putExtra(TAG_IIME_END, spinner2.getSelectedItem().toString());

//        cOrderModel.setDISTRIBUTION_DATE_START(spinner1.getSelectedItem().toString());
//        cOrderModel.setDISTRIBUTION_DATE_END(spinner2.getSelectedItem().toString());
//        mIntent.putExtra(TAG_CREATE_ORDER_MODEL, GsonUtils.Bean2Json(cOrderModel));
        setResult(AppConfig.IntentExtraKey.RESULT_OK, mIntent);
        finish();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText("设置页面");


        adapter1 = ArrayAdapter.createFromResource(this, R.array.time, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner2.setAdapter(adapter1);


//        Intent myIntent = getIntent();
//        String cOrderModelJSON = myIntent.getStringExtra(TAG_CREATE_ORDER_MODEL);
//        cOrderModel = GsonUtils.Json2Bean(cOrderModelJSON, CreatOrderModel.class);

//        String strStartDate = cOrderModel.getDISTRIBUTION_DATE_START();
//        String strEndDate = cOrderModel.getDISTRIBUTION_DATE_END();
//        int posStart = adapter1.getPosition(strStartDate);
//        int posEnd = adapter1.getPosition(strEndDate);
//        spinner1.setSelection(posStart, true);
//        spinner2.setSelection(posEnd, true);

        /** wsy c **/
//        if (cOrderModel.getPAYMENT_METHOD().equals("0")) {
//            zhifu_s = "在线支付";
//        } else if (cOrderModel.getPAYMENT_METHOD().equals(OrderTrade.PAYMTD_MONEY)) {
//            zhifu_s = "货到付款";
//        }

//        if (zhifu_s.equals("在线支付")) {
//            radioGroup1.check(R.id.zhifu_rb1);
//        } else {
//            radioGroup1.check(R.id.zhifu_rb2);
//        }

    }


    public static Intent getIntent(Context context) {
        Intent it = new Intent(context, ActivityGoodsSubmitChoice.class);
        return it;
    }
}
