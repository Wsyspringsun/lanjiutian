package com.wyw.ljtmgr.ui;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wyw.ljtmgr.R;
import com.wyw.ljtmgr.biz.OrderBiz;
import com.wyw.ljtmgr.biz.SimpleCommonCallback;
import com.wyw.ljtmgr.biz.UserBiz;
import com.wyw.ljtmgr.config.AppConfig;
import com.wyw.ljtmgr.config.MyApplication;
import com.wyw.ljtmgr.model.OrderDetailModel;
import com.wyw.ljtmgr.model.OrderListResponse;
import com.wyw.ljtmgr.model.ServerResponse;
import com.wyw.ljtmgr.utils.StringUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_setting_index)
public class SettingIndexFragment extends Fragment implements View.OnClickListener {
    @ViewInject(R.id.fragment_setting_index_items)
    LinearLayout llItems;
    @ViewInject(R.id.fragment_setting_index_btnlogout)
    Button btnLogout;

    private static final int REQUEST_SCANQRCODE = 1;


    private static final String ARG_TITLE = "ARG_TITLE";


    public static SettingIndexFragment newInstance(String title) {
        SettingIndexFragment frag = new SettingIndexFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_order_index, null);
        View v = x.view().inject(this, inflater, container);
        initView(v);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        bindData2View();
    }


    void bindData2View() {
        String userId = "";
        if (MyApplication.getCurrentLoginer() != null) {
            userId = MyApplication.getCurrentLoginer().getAdminUserName();
        }
        String[][] itemArr = new String[][]{
                {"帐号", userId},
                {"扫一扫", ">"},
                {"奖品列表", ">"},
                {"修改密码", ">"}
        };
        for (int i = 0; i < itemArr.length; i++) {
            String[] item = itemArr[i];
            RelativeLayout llItem = (RelativeLayout) llItems.getChildAt(i);
            for (int j = 0; j < item.length; j++) {
                TextView tv = (TextView) llItem.getChildAt(j);
                tv.setText(item[j]);
            }
        }
    }

    private void initView(View v) {
        btnLogout.setOnClickListener(this);


        View.OnClickListener llItemListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = (String) v.getTag();
                switch (tag) {
                    case "3":
                        startActivity(UpdatePasswordActivity.getIntent(getActivity()));
                        break;
                    case "2":
                        startActivity(AwardNeedListlActivity.getIntent(getActivity()));
                        break;
                    case "1":
                        Intent itScan = ActivityScan.getIntent(getActivity());
                        startActivityForResult(itScan, REQUEST_SCANQRCODE);
                        break;
                }
            }
        };
        for (int i = 0; i < llItems.getChildCount(); i++) {
            RelativeLayout llItem = (RelativeLayout) llItems.getChildAt(i);
            llItem.setTag("" + i);
            llItem.setOnClickListener(llItemListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_setting_index_btnlogout:
                AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
                alert.setMessage("确认退出？");
                alert.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.alert_queding), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UserBiz.logout(getActivity());

                        Intent it = ActivityLogin.getIntent(getActivity());
                        startActivity(it);
                    }
                });
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.alert_quxiao), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alert.show();


                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_SCANQRCODE:
                final String result = data.getStringExtra(ActivityScan.TAG_QRCODE);
                if (!StringUtils.isEmpty(result)) {
                    int idxSplit = result.indexOf("#");
                    if (idxSplit >= 0) {
                        String flg = result.substring(0, idxSplit);
                        final String scanData = result.substring(idxSplit + 1);
                        switch (flg) {
                            case "1":
                                //礼物
                                startActivity(AwardListlActivity.getIntent(getActivity(), scanData));
                                break;
                            case "2":
                                //订单送达
//                                获取订单
                                ((BaseActivity) getActivity()).setLoding();
                                OrderBiz.loadOrderDetail(scanData, "A", new SimpleCommonCallback<OrderDetailModel>(getActivity()) {
                                    @Override
                                    protected void handleResult(OrderDetailModel orderResult) {
                                        if (orderResult == null) return;
                                        Gson g = new Gson();
                                        String orderId = scanData;
                                        orderResult.setOrderId(orderId);
                                        startActivity(OrderDetailActivity.getIntent(getActivity(), orderResult.getGroupStatus(), orderId));

                                    }
                                });
                                //订单送达
                                /*OrderBiz.orderArrived(orderResult, new SimpleCommonCallback<ServerResponse>(getActivity()) {
                                    @Override
                                    protected void handleResult(ServerResponse result) {
                                        Toast.makeText(getActivity(), "送达成功", Toast.LENGTH_LONG).show();
                                        ((BaseActivity) getActivity()).setLoding();

                                    }
                                });*/

                                break;
                        }
                    }
                }
                break;
        }
    }
}
