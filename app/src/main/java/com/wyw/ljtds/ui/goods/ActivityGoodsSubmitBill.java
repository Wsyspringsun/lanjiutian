package com.wyw.ljtds.ui.goods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.Business;
import com.wyw.ljtds.model.CreatOrderModel;
import com.wyw.ljtds.model.OrderGroupDto;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.widget.dialog.BottomDialog;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

@ContentView(R.layout.dialog_pay_bill)
public class ActivityGoodsSubmitBill extends BaseActivity {
    public static final String TAG_ORDER = "com.wyw.ljtds.ui.goods.tag_order";
    @ViewInject(R.id.dialog_pay_bill_ll_xiangqing)
    private LinearLayout llXiangQing; //发票详情

    @ViewInject(R.id.header_title)
    private TextView title;
    //是否开发票
    @ViewInject(R.id.fapiao_rg)
    private RadioGroup rbgFapiao;
    //公司 或 个人
    @ViewInject(R.id.dialog_pay_bill_rg_orglvl)
    private RadioGroup rgOrgLvl;
    @ViewInject(R.id.activity_orderbill_rb_fapiao_yes)
    private RadioButton rbFapiaoYes;
    @ViewInject(R.id.dialog_bill_ed_title)
    private EditText edTitle;
    @ViewInject(R.id.dialog_bill_ed_company_id)
    private EditText edCompanyId;


    private Map<RadioButton, String> mapFapiaoRbList;
//    Business orderModel;
    private CreatOrderModel orderModel;

    @Event(value = {R.id.queding, R.id.header_return, R.id.activity_orderbill_rb_fapiao_no, R.id.activity_orderbill_rb_fapiao_yes})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_return:
                finish();
                break;
            case R.id.queding:
                Intent mIntent = new Intent();
                if (rbgFapiao.getCheckedRadioButtonId() == R.id.activity_orderbill_rb_fapiao_no) {
                    orderModel.setINVOICE_TAX("");
                    orderModel.setINVOICE_TITLE("");
                    orderModel.setINVOICE_CONTENT("");
                    orderModel.setINVOICE_TYPE("");
                    orderModel.setINVOICE_ORG("");
                    mIntent.putExtra(TAG_ORDER, (Parcelable) orderModel);
                    setResult(AppConfig.IntentExtraKey.RESULT_OK, mIntent);
                    finish();
                } else {
                    orderModel.setINVOICE_TYPE("0");
                    RadioButton rbOrg = (RadioButton) rgOrgLvl.findViewById(rgOrgLvl.getCheckedRadioButtonId());
                    orderModel.setINVOICE_ORG((String) rbOrg.getTag());
                    orderModel.setINVOICE_TAX(edCompanyId.getText().toString());
                    if (StringUtils.isEmpty(edTitle.getText().toString().trim())) {
                        ToastUtil.show(this, "请填写发票抬头信息");
                    } else {
                        orderModel.setINVOICE_TITLE(edTitle.getText().toString().trim());
                        mIntent.putExtra(TAG_ORDER, (Parcelable) orderModel);
                        setResult(AppConfig.IntentExtraKey.RESULT_OK, mIntent);
                        finish();
                    }
                }


                break;

            case R.id.activity_orderbill_rb_fapiao_no:
                //不开发票
                rbFapiaoYes.setText(R.string.fapiao_yes);
                llXiangQing.setVisibility(View.GONE);
                orderModel.setINVOICE_FLG("0");
                break;

            case R.id.activity_orderbill_rb_fapiao_yes:
                orderModel.setINVOICE_FLG("1");

                orderModel.setINVOICE_CONTENT("0");
                llXiangQing.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        edCompanyId.setVisibility(View.GONE);
        //监听是个人发票还是公司发票
        rgOrgLvl.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                Log.e(AppConfig.ERR_TAG, "changge");
                switch (checkedId) {
                    case R.id.dialog_pay_bill_gongsi:
                        edCompanyId.setVisibility(View.VISIBLE);
                        orderModel.setINVOICE_ORG("1");
                        break;
                    case R.id.dialog_pay_bill_gren:
                        edCompanyId.setVisibility(View.GONE);
                        orderModel.setINVOICE_ORG("0");
                        break;
                }
            }
        });


        title.setText("选择发票");
        rgOrgLvl.check(R.id.dialog_pay_bill_gren);


        //bind initial data to views
        mapFapiaoRbList = new HashMap<>();
        orderModel = getIntent().getParcelableExtra(TAG_ORDER);

        if ("0".equals(orderModel.getINVOICE_FLG()) || StringUtils.isEmpty(orderModel.getINVOICE_FLG())) {
            //不开发票
            llXiangQing.setVisibility(View.GONE);
            rbgFapiao.check(R.id.activity_orderbill_rb_fapiao_no);
        } else {
            //初始化 数据
            llXiangQing.setVisibility(View.VISIBLE);
            //开发票
            rbgFapiao.check(R.id.activity_orderbill_rb_fapiao_yes);

            rbFapiaoYes.setText(Business.mapFapiaoCatText.get(orderModel.getINVOICE_CONTENT()));

            //发票抬头
            if (!StringUtils.isEmpty(orderModel.getINVOICE_TITLE())) {
                edTitle.setText(orderModel.getINVOICE_TITLE());
            }

            //init view event
            if ("1".equals(orderModel.getINVOICE_ORG())) {
                rgOrgLvl.check(R.id.dialog_pay_bill_gongsi);
                edCompanyId.setText(orderModel.getINVOICE_TAX());
            } else {
                rgOrgLvl.check(R.id.dialog_pay_bill_gren);
            }

        }
    }

    public static Intent getIntent(Context context, Parcelable invoiceInfo) {
        Intent it = new Intent(context, ActivityGoodsSubmitBill.class);
        it.putExtra(TAG_ORDER, invoiceInfo);
        return it;
    }
}
