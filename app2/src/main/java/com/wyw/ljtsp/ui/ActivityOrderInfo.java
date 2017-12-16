package com.wyw.ljtsp.ui;

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
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.wyw.ljtsp.R;
import com.wyw.ljtsp.config.AppConfig;
import com.wyw.ljtsp.config.PreferenceCache;
import com.wyw.ljtsp.model.BaseJson;
import com.wyw.ljtsp.model.Header;
import com.wyw.ljtsp.model.OrderDetail;
import com.wyw.ljtsp.model.OrderDetailGetModel;
import com.wyw.ljtsp.model.OrderDetailModel;
import com.wyw.ljtsp.model.OrderSendModel;
import com.wyw.ljtsp.model.OrderStatus;
import com.wyw.ljtsp.model.PayStatus;
import com.wyw.ljtsp.utils.StringUtils;

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

    @Event(value = {R.id.send, R.id.header_return})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.send:
                BaseJson<OrderSendModel> baseJson = new BaseJson<OrderSendModel>();
                String token = PreferenceCache.getToken();
                Header head = new Header();
                head.setToken(token);
                OrderSendModel orderSendModel = new OrderSendModel();
                orderSendModel.setOrderTradeId(orderTradeId);
                baseJson.setHead(head);
                baseJson.setBody(orderSendModel);
                Gson gson = new Gson();
                String data = gson.toJson(baseJson);
                Log.e("jsondata", data);
                doSend(data);
                break;
//            case R.id.button2:
//
//                break;

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
        Fresco.initialize(this);
        Intent intent = getIntent();
        orderTradeId = intent.getStringExtra("orderTradeId");

        Log.e("orderTradeId----", "" + orderTradeId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String token = PreferenceCache.getToken();
        String oidGroupId = PreferenceCache.getOidGroupId();
        BaseJson<OrderDetailGetModel> baseJson = new BaseJson<OrderDetailGetModel>();
        OrderDetailGetModel orderDetailGetModel = new OrderDetailGetModel();
        Header head = new Header();
        head.setToken(token);
        orderDetailGetModel.setOrderTradeId(orderTradeId);
        orderDetailGetModel.setOidGroupId(oidGroupId);
        baseJson.setHead(head);
        baseJson.setBody(orderDetailGetModel);
        Gson gson = new Gson();
        String data = gson.toJson(baseJson);
        Log.e(AppConfig.TAG_ERR, data);
        doList(data);
        title.setText(R.string.order_details);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);//必须有
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置水平方向滑动
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MyAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    //商品adapter
    private class MyAdapter extends BaseQuickAdapter<OrderDetail> {
        public MyAdapter(List<OrderDetail> list) {
            super(R.layout.item_oder_goods, list);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, OrderDetail orderDetail) {
            baseViewHolder.setText(R.id.checkListTitle, orderDetail.getTitle());
            baseViewHolder.setText(R.id.checkListSpecification, orderDetail.getCommoditySize());
            baseViewHolder.setText(R.id.checkListCount, "x" + orderDetail.getExchangeQuanlity());
            baseViewHolder.setText(R.id.checkListSum, "￥" + orderDetail.getCostMoneyAll());

            SimpleDraweeView simpleDraweeView = baseViewHolder.getView(R.id.imagePath);
            if (!StringUtils.isEmpty(orderDetail.getImgPath())) {
                simpleDraweeView.setImageURI(Uri.parse(AppConfig.IMAGE_PATH+orderDetail.getImgPath()));
            }
        }
    }

    public void doList(String data) {
        RequestParams params = new RequestParams("http://www.lanjiutian.com/ljt_mobile_store/v/order/orderDetail");

        params.setAsJsonContent(true);
        params.setBodyContent(data);
//                setLoding(getActivity(), false);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(AppConfig.TAG_ERR, result);
                try {
                    JSONObject jsonData = new JSONObject(result);
                    String success = jsonData.getString("success");
                    String msg = jsonData.getString("msg");
                    Log.e("success", success);
                    if (success.equals("0")) {
                        Log.e("success---", "success");
                        list = new ArrayList<OrderDetail>();
                        OrderDetailModel orderDetailModel = new OrderDetailModel();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String insDate = jsonData.get("insDate").toString();
                        String updDate = jsonData.get("updDate").toString();
                        String _insDate = sdf.format(new Date(Long.parseLong(insDate)));
                        String _updDate = "";
                        if (updDate == "null") {
                            updDate = "";
                        } else {
                            _updDate = sdf.format(new Date(Long.parseLong(updDate)));
                        }
                        tvOrderId.setText("订单编号：" + orderTradeId);
                        tvInsDate.setText("订单生成时间：" + _insDate);
                        tvUpdDate.setText("订单完成时间：" + _updDate);
                        tvOrderStatus.setText("订单状态：" + OrderStatus.getStatus(jsonData.getString("groupStatus")));
                        tvPayStatus.setText("支付方式：" + PayStatus.getStatus(jsonData.getString("paymentMethod")));
                        String logisticsInsDate = jsonData.getString("logisticsInsDate");
                        if (logisticsInsDate == "null") {
                            logisticsInsDate = "";
                        }
                        tvSendTime.setText("物流发货时间：" + logisticsInsDate);
                        String courier = jsonData.getString("courier");
                        if (courier == "null") {
                            courier = "";
                        }
                        tvCourier.setText("快递员：" + courier);
                        tvCourierMobile.setText(jsonData.getString("courierMobile") != "null" ? jsonData.getString("courierMobile") : "");
                        tvAddressId.setText(jsonData.getString("userAddressId"));
                        if(jsonData.get("")!=null||jsonData.get("")!=JSONObject.NULL){
                            JSONArray jsonArray = jsonData.getJSONArray("detailList");
                            Log.e("length---", jsonArray.length() + "");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                Gson gson = new Gson();
                                OrderDetail orderDetail = gson.fromJson(jsonObject.toString(), OrderDetail.class);
                                list.add(orderDetail);
                            }
                        }
                        adapter.setNewData(list);
                        adapter.notifyDataSetChanged();
                    } else if (success.equals("2")) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //解析result
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("err", ex.getMessage());
                Log.e("err", "error.........");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                closeLoding();
                Log.e("err", "cancel.........");
            }

            @Override
            public void onFinished() {
                Log.e("err", "finished.........");
            }
        });
    }

    public void doSend(String data) {
        RequestParams params = new RequestParams("http://www.lanjiutian.com/ljt_mobile_store/v/order/orderSend");

        params.setAsJsonContent(true);
        params.setBodyContent(data);
//                setLoding(getActivity(), false);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("err", result);
                try {
                    JSONObject jsonData = new JSONObject(result);
                    String success = jsonData.getString("success");
                    String msg = jsonData.getString("msg");
                    Log.e("success", success);
                    if (success.equals("0")) {
                        Log.e(AppConfig.TAG_ERR, "success");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    } else if (success.equals("2")) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //解析result
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("err", ex.getMessage());
                Log.e("err", "error.........");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                closeLoding();
                Log.e("err", "cancel.........");
            }

            @Override
            public void onFinished() {
                Log.e("err", "finished.........");
            }
        });
    }
}
