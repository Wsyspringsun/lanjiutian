package com.wyw.ljtwl.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.wyw.ljtwl.R;
import com.wyw.ljtwl.config.AppConfig;
import com.wyw.ljtwl.config.PreferenceCache;
import com.wyw.ljtwl.model.BaseJson;
import com.wyw.ljtwl.model.Header;
import com.wyw.ljtwl.model.OrderInfoModel;
import com.wyw.ljtwl.model.OrderStatus;
import com.wyw.ljtwl.model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/3 0003.
 */

@ContentView(R.layout.fragment_order)
public class FragmentOrder extends BaseFragment {
    @ViewInject(R.id.recycler)
    private RecyclerView recyclerView;
    @ViewInject(R.id.fragment_order_tv_show)
    private TextView tvShow;

    private MyAdapter adapter;
    //无数据时的界面
    private View noData;
    private List<OrderInfoModel> list;
    private String status;

    public FragmentOrder(String status) {
        this.status = status;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        recyclerView.setVisibility(View.VISIBLE);
        noData = getActivity().getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recyclerView.getParent(), false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());//必须有
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置水平方向滑动
        recyclerView.setLayoutManager(linearLayoutManager);
//        list = new ArrayList<>();
//                for (int i = 0; i < 10; i++) {
//                    list.add(i);
//                }
        adapter = new MyAdapter(list);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                OrderInfoModel data = adapter.getData().get(i);
                Intent it = ActivityOrderInfo.getIntent(getActivity(), data.getOidGroupId(), data.getOrderTradeId());
                startActivity(it);
            }
        });
        tvShow.setText("状态:" + status);
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (adapter == null)
//                    return;
//                int cnt = adapter.getItemCount();
////                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
//                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
//                if (!end && !loading && (lastVisibleItem) >= cnt) {
//                    page = page + 1;
//                    loadData();
//                }
//            }
//        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setLoding(getActivity(), true);
        String userId = PreferenceCache.getUserId();
        String token = PreferenceCache.getToken();
        BaseJson<UserModel> baseJson = new BaseJson<UserModel>();
        UserModel userModel = new UserModel();
        Header head = new Header();
        head.setToken(token);
        userModel.setLogisticsCompanyId(userId);
        userModel.setPageNum("1");
        userModel.setPageSize("10");
//        Log.e("status", status);
        userModel.setClassify(status);
        baseJson.setHead(head);
        baseJson.setBody(userModel);
        Gson gson = new Gson();
        String data = gson.toJson(baseJson);
//        Log.e("jsondata", data);
        doList(data);
    }

//    @Override
//    protected void lazyLoad() {
//        recyclerView.setVisibility(View.VISIBLE);
//        noData = getActivity().getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recyclerView.getParent(), false);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());//必须有
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置水平方向滑动
//        recyclerView.setLayoutManager(linearLayoutManager);
////        list = new ArrayList<>();
////                for (int i = 0; i < 10; i++) {
////                    list.add(i);
////                }
//                adapter = new MyAdapter(list);
//                recyclerView.setAdapter(adapter);
//                recyclerView.addOnItemTouchListener(new OnItemClickListener() {
//                    @Override
//                    public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
//                        Intent it=new Intent(getActivity(),ActivityOrderInfor.class);
//                        startActivity(it);
//            }
//        });
//    }
//
//    @Override
//    protected void stopLoad() {
//        super.stopLoad();
//        list = null;
//        recyclerView.setVisibility(View.GONE);
//    }


    //商品adapter
    private class MyAdapter extends BaseQuickAdapter<OrderInfoModel> {

        public MyAdapter(List<OrderInfoModel> data) {
            super(R.layout.item_order, data);
            if (data == null) {
                Log.e("datalength", "11111");
            }
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, OrderInfoModel orderInfoModel) {

            baseViewHolder.setText(R.id.orderId, orderInfoModel.getOrderTradeId());
            baseViewHolder.setText(R.id.orderState, OrderStatus.getStatus(orderInfoModel.getGroupStatus()));
            baseViewHolder.setText(R.id.orderBuyCount, orderInfoModel.getExchangeQuanlity().toString());
            baseViewHolder.setText(R.id.orderMoneySum, orderInfoModel.getPayAmount().toString());
            baseViewHolder.setText(R.id.orderUpdateTime, orderInfoModel.getUpdDate());
//                        if (StringUtils.isEmpty( orderInfoModel.getOrderTradeId())) {
//                baseViewHolder.setText( R.id.size, " 规格：" + goods.getCOMMODITY_SIZE() );
//            } else {
//                baseViewHolder.setText( R.id.size, "产地：" + goods.getCOMMODITY_COLOR() + " ;规格：" + goods.getCOMMODITY_SIZE() );
//            }
//            baseViewHolder.setText( R.id.title, StringUtils.deletaFirst( goods.getCOMMODITY_NAME() ) )
//                    .setText( R.id.money, "￥" + goods.getCOST_MONEY() )
//                    .setText( R.id.number, "X" + goods.getEXCHANGE_QUANLITY() );
//
//            SimpleDraweeView simpleDraweeView = baseViewHolder.getView( R.id.iv_adapter_list_pic );
//            if (!StringUtils.isEmpty( goods.getIMG_PATH() )) {
//                simpleDraweeView.setImageURI( Uri.parse( goods.getIMG_PATH() ) );
//        }

        }
    }

    public void doList(String data) {
        Log.e(AppConfig.TAG_ERR, "orderList:" + AppConfig.WEB_DOMAIN + "/v/order/orderList");
//        RequestParams params = new RequestParams("http://www.lanjiutian.com/ljt_mobile_logistics/v/order/orderList");
        RequestParams params = new RequestParams(AppConfig.WEB_DOMAIN + "/v/order/orderList");

        params.setAsJsonContent(true);
        params.setBodyContent(data);
//                setLoding(getActivity(), false);
        Log.e(AppConfig.TAG_ERR, "state:" + status + "request:" + data);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(AppConfig.TAG_ERR, "state:" + status + "response:" + result);
                try {
                    JSONObject jsonData = new JSONObject(result);
                    String success = jsonData.getString("success");
                    String msg = jsonData.getString("msg");
                    if (success.equals("0")) {
                        List<OrderInfoModel> list = new ArrayList<OrderInfoModel>();
                        JSONArray array = jsonData.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = (JSONObject) array.get(i);
                            Gson gson = new Gson();
                            OrderInfoModel orderInfoModel = gson.fromJson(jsonObject.toString(), OrderInfoModel.class);
                            list.add(orderInfoModel);
                        }
                        if (list.size() <= 0) {
                            adapter.setEmptyView(noData);
                        } else {
                            adapter.setNewData(list);
                            adapter.notifyDataSetChanged();
                        }
                    } else if (success.equals("2")) {
                        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //解析result
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(AppConfig.TAG_ERR, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                closeLoding();
            }

            @Override
            public void onFinished() {
                Log.e("err", "finished.........");
            }
        });
    }
}
