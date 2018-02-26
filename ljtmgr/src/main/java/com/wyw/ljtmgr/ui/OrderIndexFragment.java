package com.wyw.ljtmgr.ui;


import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.wyw.ljtmgr.R;
import com.wyw.ljtmgr.adapter.OrderListAdapter;
import com.wyw.ljtmgr.biz.OrderBiz;
import com.wyw.ljtmgr.biz.SimpleCommonCallback;
import com.wyw.ljtmgr.biz.UserBiz;
import com.wyw.ljtmgr.config.AppConfig;
import com.wyw.ljtmgr.model.OrderListResponse;
import com.wyw.ljtmgr.model.OrderStat;
import com.wyw.ljtmgr.utils.ActivityUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderIndexFragment extends Fragment {
    private static final String ARG_TITLE = "ARG_TITLE";
    private int[] titles = {R.string.order_new, R.string.order_running, R.string.order_completed, R.string.order_canceled, R.string.order_refund};
    private int[] drawNor = {R.drawable.ic_indingdan_nor, R.drawable.ic_jiningzhong_nor, R.drawable.ic_yiquiao_nor, R.drawable.ic_yiwancheng_nor, R.drawable.ic_shouhou_nor};

    TabLayout tabLayout;
    RecyclerView ryvOrder;
    private OrderListResponse orderListRes;
    private OrderListAdapter adapter;
    private int pageIdx = 1;
    private boolean end = false;
    private String stat = "A";

    public static OrderIndexFragment newInstance(String title) {
        OrderIndexFragment frag = new OrderIndexFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_order_index, null);

        initView(v);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        pageIdx = 1;
        loadOrder();
    }

    private void loadOrder() {
        Log.e(AppConfig.TAG_ERR, "stat:" + stat);
        if (!UserBiz.isLogined()) return;
        ((BaseActivity) getActivity()).setLoding();
        OrderBiz.loadOrder(stat, pageIdx + "", new SimpleCommonCallback<OrderListResponse>(getActivity()) {
            @Override
            protected void handleResult(OrderListResponse result) {
                orderListRes = result;
                orderListRes.setmOrderStat(stat);
                bindData2View();
            }
        });
    }

    private void bindData2View() {
        if (orderListRes == null) {
            return;
        }

        if (adapter == null) {
            adapter = new OrderListAdapter(getActivity(), orderListRes.getmOrderStat());
            ryvOrder.setAdapter(adapter);
        }
        if (pageIdx <= 1) {
            adapter.list = orderListRes.getData();
        } else {
            adapter.list.addAll(orderListRes.getData());
        }
        if (adapter.list == null || adapter.list.size() <= 0) {
            end = true;
        }
        adapter.setMstat(stat);
        adapter.notifyDataSetChanged();

    }

    private void initView(View v) {
//        A”:新订单“B”:进行中“C”:已取消 “D”:售后 “E”：已完成
        tabLayout = (TabLayout) v.findViewById(R.id.fragment_order_index_tab);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String nStat = "";
                int pos = tab.getPosition();
                switch (pos) {
                    case 0:
                        nStat = OrderStat.NEAREST;
                        break;
                    case 1:
                        nStat = OrderStat.DOING;
                        break;
                    case 2:
                        nStat = OrderStat.CANCELED;
                        break;
                    case 3:
                        nStat = OrderStat.COMPLETED;
                        break;
                    case 4:
                        nStat = OrderStat.RETURNED;
                        break;
                    default:

                        nStat = OrderStat.NEAREST;
                        break;
                }
                if (stat == nStat) return;

                pageIdx = 1;
                end = false;
                stat = nStat;
                loadOrder();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tabLayout.removeAllTabs();
                for (int i = 0; i < titles.length; i++) {
                    Drawable image = ActivityCompat.getDrawable(getActivity(), drawNor[i]);
                    tabLayout.addTab(tabLayout.newTab().setIcon(image));
                }
                tabLayout.getTabAt(0).select();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    //防止丢失点击事件
                    tabLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

            }
        });

        ryvOrder = (RecyclerView) v.findViewById(R.id.fragment_order_index_ryv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());//必须有
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置水平方向滑动
        ryvOrder.setLayoutManager(linearLayoutManager);
        ryvOrder.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                super.onScrollStateChanged(recyclerView, newState);
                if (0 == newState) {
                    //0标识到达了
                    int fp = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    //完成滑动
                    if (fp == 1 && !end) {
                        pageIdx = pageIdx + 1;
                        loadOrder();
                    }
//                    Log.e(AppConfig.TAG_ERR, "newState:" + newState + " fp" + fp);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });
    }

}
