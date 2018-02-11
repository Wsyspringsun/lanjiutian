package com.wyw.ljtwl.ui;


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

import com.wyw.ljtwl.R;
import com.wyw.ljtwl.adapter.OrderListAdapter;
import com.wyw.ljtwl.biz.OrderBiz;
import com.wyw.ljtwl.biz.SimpleCommonCallback;
import com.wyw.ljtwl.config.AppConfig;
import com.wyw.ljtwl.model.OrderListResponse;
import com.wyw.ljtwl.utils.ActivityUtil;

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
        Log.e(AppConfig.TAG_ERR, getArguments().getString(ARG_TITLE) + "...curPhase:" + ((BaseActivity) getActivity()).curPhase);


    }

    private void loadOrder(String stat) {
        int needPhase = AppConfig.PHASE_LOGIN_IN;
        if (!ActivityUtil.isComplete(getActivity(), needPhase)) {
            return;
        }
        Log.e(AppConfig.TAG_ERR, "stat:" + stat);
        ((BaseActivity) getActivity()).setLoding();
        OrderBiz.loadOrder(stat, new SimpleCommonCallback<OrderListResponse>(getActivity()) {
            @Override
            protected void handleResult(OrderListResponse result) {
                orderListRes = result;
                bindData2View();
            }
        });
    }

    private void bindData2View() {
        if (orderListRes == null) {
            return;
        }
        if (orderListRes.getData() == null || orderListRes.getData().size() <= 0) {
            return;
        }

        if (adapter == null) {
            adapter = new OrderListAdapter(getActivity());
            ryvOrder.setAdapter(adapter);
        }
        if (pageIdx <= 1) {
            adapter.list = orderListRes.getData();
        } else {
            adapter.list.addAll(orderListRes.getData());
        }
        adapter.notifyDataSetChanged();

    }

    private void initView(View v) {
        tabLayout = (TabLayout) v.findViewById(R.id.fragment_order_index_tab);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                String stat = "A";
                switch (pos) {
                    case 0:
                        stat = "A";
                        break;
                    case 1:
                        stat = "B";
                        break;
                    case 2:
                        stat = "C";
                        break;
                    case 3:
                        stat = "D";
                        break;
                    case 4:
                        stat = "E";
                        break;
                    default:
                        stat = "A";
                        break;
                }

                loadOrder(stat);
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


    }

}
