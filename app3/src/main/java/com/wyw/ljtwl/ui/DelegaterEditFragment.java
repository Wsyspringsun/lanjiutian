package com.wyw.ljtwl.ui;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.wyw.ljtwl.R;
import com.wyw.ljtwl.biz.CommonBiz;
import com.wyw.ljtwl.biz.task.AbstractCommonCallback;
import com.wyw.ljtwl.config.AppConfig;
import com.wyw.ljtwl.config.PreferenceCache;
import com.wyw.ljtwl.model.CuriorInfo;
import com.wyw.ljtwl.utils.StringUtils;

import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by wsy on 17-9-1.
 */

@ContentView(R.layout.fragment_delegater_edit)
public class DelegaterEditFragment extends BaseFragment {
    private static final String ARG_ORDER_ID = "com.wyw.ljtwl.ui.arg_order_id";

    @ViewInject(R.id.fragment_delegater_et_courier)
    EditText edCourier;
    @ViewInject(R.id.fragment_delegater_et_courier_mobile)
    EditText edCorierMobile;
    @ViewInject(R.id.fragment_delegater_et_courier_car)
    EditText etCourierCar;

    CuriorInfo curiorModel = null;

    @Event(value = {R.id.fragment_delegater_submit})
    private void onclick(View view) {
        Intent it = null;
        switch (view.getId()) {
            case R.id.fragment_delegater_submit:
                //验证用户 数据
                if (!validInput()) return;

                curiorModel = new CuriorInfo();
                setModelData();
                //保存
                saveCourier();
                break;

            default:
                break;

        }
    }

    private void setModelData() {
        curiorModel.setOrderTradeId(getArguments().getString(ARG_ORDER_ID));
        curiorModel.setCourier(edCourier.getText().toString());
        curiorModel.setCourierCar(etCourierCar.getText().toString());
        curiorModel.setCourierMobile(edCorierMobile.getText().toString());

        String couriorData = JSON.toJSONString(curiorModel);
        Log.e(AppConfig.TAG_ERR, "............." + couriorData);
        PreferenceCache.putCouriorData(couriorData);
    }

    private void saveCourier() {
        String url = "/v/order/orderTake";
        CommonBiz.handle(url, curiorModel, new AbstractCommonCallback() {
            @Override
            public void handleSuccess(String result) throws Exception {
                Log.e(AppConfig.TAG_ERR, "orderTake:" + result);
                Toast.makeText(getActivity(), "拦件成功", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }

            @Override
            public BaseActivitry getContextActivity() {
                return (BaseActivitry) DelegaterEditFragment.this.getActivity();
            }
        });
    }

    private boolean validInput() {
        String courierName = edCourier.getText().toString();
        if (StringUtils.isEmpty(courierName) || courierName.length() > 10) {
            Toast.makeText(getActivity(), getString(R.string.delegater_edit_courierName_valid), Toast.LENGTH_LONG).show();
            return false;
        }
        String courierMobile = edCorierMobile.getText().toString();
        if (StringUtils.isEmpty(courierMobile)) {
            Toast.makeText(getActivity(), getString(R.string.delegater_edit_courierMobile_valid), Toast.LENGTH_LONG).show();
            return false;
        }
        String courierCar = etCourierCar.getText().toString();
        if (StringUtils.isEmpty(courierCar)) {
            Toast.makeText(getActivity(), getString(R.string.delegater_edit_courierCar_valid), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * @param cat 余额类型
     * @return
     */
    public static DelegaterEditFragment newInstance(String orderId) {
        Log.e(AppConfig.TAG_ERR, "DelegaterEditFragment");
        DelegaterEditFragment fragment = new DelegaterEditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ORDER_ID, orderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String curiorData = PreferenceCache.getCouriorData();
        if (!StringUtils.isEmpty(curiorData)) {
            CuriorInfo model = JSON.parseObject(curiorData, CuriorInfo.class);
            edCorierMobile.setText(model.getCourierMobile());
            edCourier.setText(model.getCourier());
            etCourierCar.setText(model.getCourierCar());
        }
    }

}
