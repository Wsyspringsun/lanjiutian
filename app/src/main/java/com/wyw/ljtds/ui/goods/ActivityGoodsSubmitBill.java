package com.wyw.ljtds.ui.goods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    Business orderModel;

    @Event(value = {R.id.queding, R.id.header_return, R.id.activity_orderbill_rb_fapiao_no, R.id.activity_orderbill_rb_fapiao_yes})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_return:
                finish();
                break;
            case R.id.queding:
                Intent mIntent = new Intent();
//                mIntent.putExtra("possition", getIntent().getIntExtra("possition", 0));
//                mIntent.putExtra("peisong", "0");
//                mIntent.putExtra("fapiao_flg1", "1");
//                mIntent.putExtra("fapiao_flg2", "0");
//                mIntent.putExtra("fapiao_flg4", fapiao4);
//                mIntent.putExtra("fapiao_flg3", editText.getText().toString().trim());
                if (rbgFapiao.getCheckedRadioButtonId() == R.id.activity_orderbill_rb_fapiao_no) {
                    orderModel.setINVOICE_TAX("");
                    orderModel.setINVOICE_TITLE("");
                    orderModel.setINVOICE_CONTENT("");
                    orderModel.setINVOICE_TYPE("");
                    orderModel.setINVOICE_ORG("");
                    String jsonStr = GsonUtils.Bean2Json(orderModel);
                    mIntent.putExtra(TAG_ORDER, jsonStr);
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
                        String jsonStr = GsonUtils.Bean2Json(orderModel);
                        mIntent.putExtra(TAG_ORDER, jsonStr);
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
                View view = LayoutInflater.from(ActivityGoodsSubmitBill.this).inflate(R.layout.dialog_pay_bill_select, null);
                /* 发票明细选择
                final BottomDialog dialog = new BottomDialog(ActivityGoodsSubmitBill.this, view).setCancelable(false).show();
                final RadioGroup dialogRgFapiao = (RadioGroup) view.findViewById(R.id.dialog_pay_bill_select_fapiao_rg);
                RadioButton rb1 = (RadioButton) view.findViewById(R.id.fapiao_rb1);
                RadioButton rb2 = (RadioButton) view.findViewById(R.id.fapiao_rb2);
                RadioButton rb3 = (RadioButton) view.findViewById(R.id.fapiao_rb3);
                RadioButton rb4 = (RadioButton) view.findViewById(R.id.fapiao_rb4);
                RadioButton rb5 = (RadioButton) view.findViewById(R.id.fapiao_rb5);

                ImageView quxiao = (ImageView) view.findViewById(R.id.quxiao);
                quxiao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dissmiss();
                    }
                });


                CompoundButton.OnCheckedChangeListener fapiaoRbListener = new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            dialogRgFapiao.clearCheck();
                            dialogRgFapiao.check(compoundButton.getId());
                            switch (compoundButton.getId()) {
                                case R.id.fapiao_rb1:
                                    orderModel.setINVOICE_CONTENT("0");
                                    break;
                                case R.id.fapiao_rb2:
                                    orderModel.setINVOICE_CONTENT("1");
                                    break;
                                case R.id.fapiao_rb3:
                                    orderModel.setINVOICE_CONTENT("2");
                                    break;
                                case R.id.fapiao_rb4:
                                    orderModel.setINVOICE_CONTENT("3");
                                    break;
                                case R.id.fapiao_rb5:
                                    orderModel.setINVOICE_CONTENT("4");
                                    break;

                            }
                        }
                    }
                };
                rb1.setOnCheckedChangeListener(fapiaoRbListener);
                rb2.setOnCheckedChangeListener(fapiaoRbListener);
                rb3.setOnCheckedChangeListener(fapiaoRbListener);
                rb4.setOnCheckedChangeListener(fapiaoRbListener);
                rb5.setOnCheckedChangeListener(fapiaoRbListener);
                //确认按钮
                Button button = (Button) view.findViewById(R.id.queding);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dissmiss();
                        rbFapiaoYes.setText(Business.mapFapiaoCatText.get(orderModel.getINVOICE_CONTENT()));
                    }
                });*/
                llXiangQing.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        edCompanyId.setVisibility(View.GONE);
        rgOrgLvl.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                Log.e(AppConfig.ERR_TAG, "changge");
                switch (checkedId) {
                    case R.id.dialog_pay_bill_gongsi:
                        Log.e(AppConfig.ERR_TAG, "changge : gongsi");
                        edCompanyId.setVisibility(View.VISIBLE);
                        orderModel.setINVOICE_ORG("1");
                        break;
                    case R.id.dialog_pay_bill_gren:
                        Log.e(AppConfig.ERR_TAG, "changge : geren");
                        edCompanyId.setVisibility(View.GONE);
                        orderModel.setINVOICE_ORG("0");
                        break;
                }
            }
        });


        title.setText("选择配送方式及发票");
        rgOrgLvl.check(R.id.dialog_pay_bill_gren);


        //bind initial data to views
        mapFapiaoRbList = new HashMap<>();
        String jsonStr = getIntent().getStringExtra(TAG_ORDER);
        if (!StringUtils.isEmpty(jsonStr)) {
            //it data by intent data
            orderModel = GsonUtils.Json2Bean(jsonStr, Business.class);
        } else {
            orderModel = new Business();
        }
        if ("0".equals(orderModel.getINVOICE_FLG())) {
            //不开发票
            llXiangQing.setVisibility(View.GONE);
            rbgFapiao.check(R.id.activity_orderbill_rb_fapiao_no);
        } else {
            //初始化 数据
            llXiangQing.setVisibility(View.VISIBLE);
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

    public static Intent getIntent(Context context, OrderGroupDto biz) {
        Intent it = new Intent(context, ActivityGoodsSubmitBill.class);
        String jsonStr = GsonUtils.Bean2Json(biz);
        it.putExtra(TAG_ORDER, jsonStr);
//        it.putExtra("possition", biz);
//        it.putExtra("fapiao_flg1", biz.getINVOICE_FLG());
//        it.putExtra("fapiao_flg2", biz.getINVOICE_TYPE());
//        it.putExtra("fapiao_flg3", biz.getINVOICE_TITLE());
//        it.putExtra("fapiao_flg4", biz.getINVOICE_CONTENT());
//        it.putExtra("peisong", biz.getDISTRIBUTION_MODE());
        return it;
    }
}
