package com.wyw.ljtwl.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.wyw.ljtwl.R;
import com.wyw.ljtwl.biz.CommonBiz;
import com.wyw.ljtwl.biz.task.AbstractCommonCallback;
import com.wyw.ljtwl.config.AppConfig;
import com.wyw.ljtwl.config.PreferenceCache;
import com.wyw.ljtwl.model.BaseJson;
import com.wyw.ljtwl.model.Header;
import com.wyw.ljtwl.model.OrderDetail;
import com.wyw.ljtwl.model.OrderDetailGetModel;
import com.wyw.ljtwl.model.OrderDetailModel;
import com.wyw.ljtwl.model.OrderSendModel;
import com.wyw.ljtwl.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/4 0004.
 */

@ContentView(R.layout.activity_order_info)
public class ActivityOrderInfo extends BaseActivitry {
    private static String TAG_GROUP_ID = "com.wyw.ljtwl.ui.ActivityOrderInfo.TEG_GROUP_ID";
    private static String TAG_ORDER_TRADE_ID = "com.wyw.ljtwl.ui.ActivityOrderInfo.TAG_ORDER_TRADE_ID";
    @ViewInject(R.id.recycler)
    private RecyclerView recyclerView;
    @ViewInject(R.id.header_title)
    private TextView title;
    @ViewInject(R.id.orderId)
    private TextView tvOrderId;
    @ViewInject(R.id.insDate)
    private TextView tvInsDate;
    @ViewInject(R.id.updDate)
    private TextView tvUpdDate;
    @ViewInject(R.id.orderStatus)
    private TextView tvOrderStatus;
    @ViewInject(R.id.payStatus)
    private TextView tvPayStatus;
    @ViewInject(R.id.sendTime)
    private TextView tvSendTime;
    @ViewInject(R.id.courier)
    private TextView tvCourier;
    @ViewInject(R.id.courierMobile)
    private TextView tvCourierMobile;
    @ViewInject(R.id.addressId)
    private TextView tvAddressId;
    private MyAdapter adapter;
    //无数据时的界面
    private View noData;
//    private List<Integer> list ;

    private List<OrderDetail> list;
    private String orderTradeId;
    private String oidGroupId;


    @Event(value = {R.id.acvitiy_order_info_btn_delegate, R.id.acvitiy_order_info_btn_arrived, R.id.header_return})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.acvitiy_order_info_btn_delegate:
                Intent it = DelegaterEditActivity.getIntent(this, orderTradeId);
                startActivity(it);
                break;

            case R.id.acvitiy_order_info_btn_arrived:
                break;

            case R.id.header_return:
                finish();
                break;

            default:
                break;

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        orderTradeId = intent.getStringExtra(TAG_ORDER_TRADE_ID);
        oidGroupId = intent.getStringExtra(TAG_GROUP_ID);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);//必须有
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置水平方向滑动
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public static Intent getIntent(Context ctx, String groupId, String orderTradeId) {
        Intent it = new Intent(ctx, ActivityOrderInfo.class);
        it.putExtra(TAG_GROUP_ID, groupId);
        it.putExtra(TAG_ORDER_TRADE_ID, orderTradeId);
        return it;
    }

    @Override
    protected void onResume() {
        super.onResume();
        title.setText(R.string.order_details);


        doList();
    }

    //商品adapter
    private class MyAdapter extends BaseQuickAdapter<OrderDetail> {
        public MyAdapter(List<OrderDetail> list) {
            super(R.layout.item_oder_goods, list);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, OrderDetail orderDetail) {
            if (baseViewHolder == null) return;
            if (orderDetail == null) return;
            Gson gson = new Gson();
            Log.e(AppConfig.TAG_ERR, "item.:." + gson.toJson(orderDetail));
            SimpleDraweeView simpleDraweeView = baseViewHolder.getView(R.id.imagePath);
//            simpleDraweeView.setImageURI(Uri.parse("http://www.lanjiutian.com/upload/images/62542b1842d64108ba104424152e2125/943e6d3324693188a3f1955a7f921a69.png"));
            Log.e(AppConfig.TAG_ERR, "1:" + orderDetail.getImgPath());

            simpleDraweeView.setImageURI(Uri.parse(AppConfig.IMAGE_PATH_LJT + orderDetail.getImgPath()));
//            Log.e(AppConfig.TAG_ERR, "2");
            baseViewHolder.setText(R.id.checkListTitle, orderDetail.getTitle())
                    .setText(R.id.checkListSpecification, orderDetail.getCommodityColor() + " " + orderDetail.getCommoditySize())
                    .setText(R.id.checkListCount, "x" + orderDetail.getExchangeQuanlity())
                    .setText(R.id.checkListSum, "￥" + orderDetail.getCostMoneyAll());

//            Log.e(AppConfig.TAG_ERR, "3");
        }
    }

    public void doList() {
        OrderDetailGetModel orderDetailGetModel = new OrderDetailGetModel();
        orderDetailGetModel.setOrderTradeId(orderTradeId);
        orderDetailGetModel.setOidGroupId(oidGroupId);

        CommonBiz.handle("/v/order/orderDetail", orderDetailGetModel, new AbstractCommonCallback<String>() {
            @Override
            public void handleSuccess(String result) throws Exception {
                JSONObject jsonData = new JSONObject(result);
                String success = jsonData.getString("success");
                String msg = jsonData.getString("msg");
                Log.e(AppConfig.TAG_ERR, "success:" + success);
                if (success.equals("0")) {
                    Log.e(AppConfig.TAG_ERR, "success---" + "success");
                    list = new ArrayList<>();
                    OrderDetailModel orderDetailModel = new OrderDetailModel();
                    Long insDate = null;
                    Log.e(AppConfig.TAG_ERR, "insDate:" + jsonData.get("insDate"));
                    Log.e(AppConfig.TAG_ERR, "updDate:" + jsonData.get("updDate"));
                    if (jsonData.get("insDate") != null && jsonData.get("insDate") != JSONObject.NULL) {
                        insDate = jsonData.getLong("insDate");
                    }
                    Long updDate = null;
                    if (jsonData.get("updDate") != null && jsonData.get("updDate") != JSONObject.NULL) {
                        updDate = jsonData.getLong("updDate");
                    }
                    Log.e(AppConfig.TAG_ERR, insDate + "/" + updDate);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        Date date = new Date(insDate);
//                        String res = sdf.format(insDate);
                    tvOrderId.setText("订单编号：" + orderTradeId);
                    Log.e(AppConfig.TAG_ERR, "lll:" + insDate + "/" + updDate);
                    String dateHolder = "---/----";
                    if (insDate == null) {
                        tvInsDate.setText("订单生成时间：" + dateHolder);
                    } else {
                        tvInsDate.setText("订单生成时间：" + sdf.format(new Date(insDate)));
                    }
                    if (updDate == null) {
                        tvUpdDate.setText("订单完成时间：" + dateHolder);
                    } else {
                        tvUpdDate.setText("订单完成时间：" + sdf.format(new Date(updDate)));
                    }
                    tvOrderStatus.setText("订单状态：" + jsonData.getString("groupStatus"));
                    tvPayStatus.setText("支付方式：" + jsonData.getString("paymentMethod"));
                    tvSendTime.setText("物流发货时间：" + jsonData.getString("logisticsInsDate"));
                    tvCourier.setText("快递员：" + jsonData.getString("courier"));
                    tvCourierMobile.setText(jsonData.getString("courierMobile"));
                    tvAddressId.setText(jsonData.getString("userAddressId"));

                    if (jsonData.get("detailList") != null && !JSONObject.NULL.equals(jsonData.get("detailList"))) {
                        JSONArray jsonArray = jsonData.getJSONArray("detailList");
                        Log.e(AppConfig.TAG_ERR, "length---" + jsonArray.length() + "");
                        Gson gson = new Gson();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            OrderDetail orderDetail = gson.fromJson(jsonObject.toString(), OrderDetail.class);
                            list.add(orderDetail);
                        }
                        Log.e(AppConfig.TAG_ERR, "list:" + gson.toJson(list));
                    }

                    if (adapter == null) {
                        adapter = new MyAdapter(list);
                        recyclerView.setAdapter(adapter);
                    }
                    adapter.setNewData(list);
                    adapter.notifyDataSetChanged();

                } else if (success.equals("2")) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public BaseActivitry getActivity() {
                return ActivityOrderInfo.this;
            }
        });


////        RequestParams params = new RequestParams("http://www.lanjiutian.com/ljt_mobile_store/v/order/orderDetail");
//        Log.e(AppConfig.TAG_ERR, "orderDetail:" + AppConfig.WEB_DOMAIN + "/v/order/orderDetail");
//        RequestParams params = new RequestParams(AppConfig.WEB_DOMAIN + "/v/order/orderDetail");
//
//        params.setAsJsonContent(true);
//        params.setBodyContent(data);
////                setLoding(getActivity(), false);
//        x.http().post(params, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                Log.e(AppConfig.TAG_ERR, result);
//                try {
//                    JSONObject jsonData = new JSONObject(result);
//                    String success = jsonData.getString("success");
//                    String msg = jsonData.getString("msg");
//                    Log.e(AppConfig.TAG_ERR, "success:" + success);
//                    if (success.equals("0")) {
//                        Log.e(AppConfig.TAG_ERR, "success---" + "success");
//                        list = new ArrayList<OrderDetail>();
//                        OrderDetailModel orderDetailModel = new OrderDetailModel();
//                        Long insDate = null;
//                        Log.e(AppConfig.TAG_ERR, "insDate:" + jsonData.get("insDate"));
//                        Log.e(AppConfig.TAG_ERR, "updDate:" + jsonData.get("updDate"));
//                        if (jsonData.get("insDate") != null && jsonData.get("insDate") != JSONObject.NULL) {
//                            insDate = jsonData.getLong("insDate");
//                        }
//                        Long updDate = null;
//                        if (jsonData.get("updDate") != null && jsonData.get("updDate") != JSONObject.NULL) {
//                            updDate = jsonData.getLong("updDate");
//                        }
//                        Log.e(AppConfig.TAG_ERR, insDate + "/" + updDate);
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////                        Date date = new Date(insDate);
////                        String res = sdf.format(insDate);
//                        tvOrderId.setText("订单编号：" + orderTradeId);
//                        Log.e(AppConfig.TAG_ERR, "lll:" + insDate + "/" + updDate);
//                        String dateHolder = "---/----";
//                        if (insDate == null) {
//                            tvInsDate.setText("订单生成时间：" + dateHolder);
//                        } else {
//                            tvInsDate.setText("订单生成时间：" + sdf.format(new Date(insDate)));
//                        }
//                        if (updDate == null) {
//                            tvUpdDate.setText("订单完成时间：" + dateHolder);
//                        } else {
//                            tvUpdDate.setText("订单完成时间：" + sdf.format(new Date(updDate)));
//                        }
//                        tvOrderStatus.setText("订单状态：" + jsonData.getString("groupStatus"));
//                        tvPayStatus.setText("支付方式：" + jsonData.getString("paymentMethod"));
//                        tvSendTime.setText("物流发货时间：" + jsonData.getString("logisticsInsDate"));
//                        tvCourier.setText("快递员：" + jsonData.getString("courier"));
//                        tvCourierMobile.setText(jsonData.getString("courierMobile"));
//                        tvAddressId.setText(jsonData.getString("userAddressId"));
//
//                        if (jsonData.get("detailList") != null && !JSONObject.NULL.equals(jsonData.get("detailList"))) {
//                            JSONArray jsonArray = jsonData.getJSONArray("detailList");
//                            Log.e(AppConfig.TAG_ERR, "length---" + jsonArray.length() + "");
//                            Gson gson = new Gson();
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//                                OrderDetail orderDetail = gson.fromJson(jsonObject.toString(), OrderDetail.class);
//                                list.add(orderDetail);
//                            }
//                            Log.e(AppConfig.TAG_ERR, "list:" + gson.toJson(list));
//                        }
//
//                        adapter.setNewData(list);
//                        adapter.notifyDataSetChanged();
//                    } else if (success.equals("2")) {
//                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Log.e(AppConfig.TAG_ERR, "err:" + e.getMessage());
//                }
//                //解析result
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                Log.e(AppConfig.TAG_ERR, ex.getMessage());
//                Log.e(AppConfig.TAG_ERR, "error.........");
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//                closeLoding();
//                Log.e(AppConfig.TAG_ERR, "cancel.........");
//            }
//
//            @Override
//            public void onFinished() {
//                Log.e(AppConfig.TAG_ERR, "finished.........");
//            }
//        });
    }

    public void doDelegate() {
        CommonBiz.handle("/v/order/orderDetail", null, new AbstractCommonCallback<String>() {
            @Override
            public void handleSuccess(String result) throws Exception {
                JSONObject jsonData = new JSONObject(result);
                String success = jsonData.getString("success");
                String msg = jsonData.getString("msg");
                if ("0".equals(success)) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                } else if (success.equals("2")) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public BaseActivitry getActivity() {
                return ActivityOrderInfo.this;
            }
        });
    }
}
