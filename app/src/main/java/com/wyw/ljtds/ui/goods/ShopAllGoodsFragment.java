package com.wyw.ljtds.ui.goods;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.goodsinfo.MedicineItemAdapter1;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.MedicineListModel;
import com.wyw.ljtds.model.MedicineTypeFirstModel;
import com.wyw.ljtds.model.SingleCurrentUser;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.widget.DividerGridItemDecoration;
import com.wyw.ljtds.widget.MyCallback;

import org.junit.Assert;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShopAllGoodsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShopAllGoodsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@ContentView(R.layout.fragment_shopgoods)
public class ShopAllGoodsFragment extends BaseFragment {

    @ViewInject(R.id.fragment_shopgoods_ll_paixu)
    LinearLayout llPaixu;
    @ViewInject(R.id.fragment_shopgoods_ryv_goods)
    RecyclerView ryvGoods;
    @ViewInject(R.id.paixu1_tv)
    private TextView paixu1_tv;
    @ViewInject(R.id.paixu1_iv)
    private ImageView paixu1_iv;
    @ViewInject(R.id.paixu2_tv)
    private TextView paixu2_tv;
    @ViewInject(R.id.paixu3_tv)
    private TextView paixu3_tv;
    @ViewInject(R.id.paixu3_iv)
    private ImageView paixu3_iv;

    GridLayoutManager glm;
    private List<MedicineListModel> medicineList;

    private static final String ARG_SHOPID = "param1";
    private static final String ARG_TYPE = "param2";

    private String mParam1;
    private String mParam2;
    private boolean end = false;
    //    public List<MedicineListModel> list;
    private int pageIndex = 1;
    private BaseQuickAdapter<MedicineListModel> adapter;
    private View noData;
    private boolean isRise = true;
    private String orderby;
    private String keyword;
    private String category;
    public static final String TYPE_SHOP = "0";//本店产品
    public static final String TYPE_HOT = "2"; //全店推荐

    @Event(value = {R.id.paixu1, R.id.paixu2, R.id.paixu3, R.id.paixu4})
    private void onclick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.paixu1:
                reset();
                paixu1_tv.setTextColor(getResources().getColor(R.color.base_bar));
                paixu1_iv.setImageDrawable(getResources().getDrawable(R.mipmap.paixu_1));
                orderby = "";
                sortData();
                break;
            case R.id.paixu2:
                reset();
                paixu2_tv.setTextColor(getResources().getColor(R.color.base_bar));
                orderby = "1";
                sortData();
                break;
            case R.id.paixu3:
                reset();
                orderby = "2";
                if (isRise) {
                    paixu3_tv.setTextColor(getResources().getColor(R.color.base_bar));
                    paixu3_iv.setImageDrawable(getResources().getDrawable(R.mipmap.paixu_4));
                    isRise = false;
                    orderby = "2";
                } else {
                    paixu3_tv.setTextColor(getResources().getColor(R.color.base_bar));
                    paixu3_iv.setImageDrawable(getResources().getDrawable(R.mipmap.paixu_5));
                    isRise = true;
                    orderby = "3";
                }
                sortData();
                break;
        }
    }

    private void sortData() {
        pageIndex = 1;
        loadData();
    }

    private void reset() {
        paixu1_tv.setTextColor(getResources().getColor(R.color.font_black2));
        paixu1_iv.setImageDrawable(getResources().getDrawable(R.mipmap.paixu_2));
        paixu2_tv.setTextColor(getResources().getColor(R.color.font_black2));
        paixu3_tv.setTextColor(getResources().getColor(R.color.font_black2));
        paixu3_iv.setImageDrawable(getResources().getDrawable(R.mipmap.paixu_3));
//        paixu4_tv.setTextColor(getResources().getColor(R.color.font_black2));
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BalanceFragment.
     */
    public static ShopAllGoodsFragment newInstance(String shopid, String medType) {
        ShopAllGoodsFragment fragment = new ShopAllGoodsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SHOPID, shopid);
        args.putString(ARG_TYPE, medType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String type = getArguments().getString(ARG_TYPE);
        if (TYPE_HOT.equals(type)) {
            llPaixu.setVisibility(View.GONE);
        } else {
            llPaixu.setVisibility(View.VISIBLE);
        }

        ((ShopActivity) getActivity()).addSearchCallBack(new MyCallback() {
            @Override
            public void callback(Object... params) {
                pageIndex = 1;
                keyword = (String) params[0];
                loadData();
            }
        }).addCatSelCallBack(new MyCallback() {
            @Override
            public void callback(Object... params) {
                MedicineTypeFirstModel mfm = (MedicineTypeFirstModel) params[0];
                pageIndex = 1;
                category = mfm.getCLASSCODE();
                loadData();
            }
        });

        glm = new GridLayoutManager(getActivity(), 2);
        ryvGoods.setLayoutManager(glm);

        ryvGoods.setItemAnimator(new DefaultItemAnimator());
//        ryvGoods.addItemDecoration(new DividerGridItemDecoration(getActivity()));

        ryvGoods.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (adapter == null) return;
                int cnt = adapter.getItemCount() - 1;
                int lastVisibleItem = glm.findLastVisibleItemPosition();
                Log.e(AppConfig.ERR_TAG, "scroll:l/cnt:" + lastVisibleItem + "/" + cnt);
                if (!end && !loading && (lastVisibleItem >= cnt)) {
                    pageIndex = pageIndex + 1;
//                    loadData();
                }
            }
        });

        ryvGoods.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                MedicineListModel detail = adapter.getData().get(i);
                Assert.assertNotEquals(detail.getWAREID(), "");
                Assert.assertNotEquals(detail.getLOGISTICS_COMPANY_ID(), "");
                String shopId = getArguments().getString(ARG_SHOPID);
                Intent it = ActivityMedicinesInfo.getIntent(getActivity(), detail.getWAREID(), shopId);
                startActivity(it);
            }
        });
        noData = getActivity().getLayoutInflater().inflate(R.layout.main_empty_view, null);
        adapter = new MedicineItemAdapter1(getActivity(), medicineList);
        adapter.setEmptyView(noData);
        ryvGoods.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    private void bindData2View() {
        if (medicineList == null || medicineList.size() <= 0) {
            end = true;
            return;
        }
        if (pageIndex == 1) {
            adapter.setNewData(medicineList);
        } else {
            adapter.addData(medicineList);
        }
        adapter.notifyDataSetChanged();
    }

    private void loadData() {
        setLoding(getActivity(), false);
        new BizDataAsyncTask<List<MedicineListModel>>() {
            @Override
            protected List<MedicineListModel> doExecute() throws ZYException, BizFailure {
                closeLoding();
//                String shopId = "001";
                String shopId = getArguments().getString(ARG_SHOPID);
                Map<String, String> data = new HashMap<>();
                data.put("lat", SingleCurrentUser.location.getLatitude() + "");
                data.put("lng", SingleCurrentUser.location.getLongitude() + "");

                String type = getArguments().getString(ARG_TYPE);

                if (TYPE_HOT.equals(type)) {
                    String strData = GsonUtils.Bean2Json(data);
                    return GoodsBiz.loadGoodsOfHot(strData);
                } else if (TYPE_SHOP.equals(type)) {
                    data.put("sort", orderby);
                    data.put("pageIdx", "" + pageIndex);
                    data.put("pageSize", "" + AppConfig.DEFAULT_PAGE_COUNT);
                    data.put("busno", shopId);
                    if (!StringUtils.isEmpty(category)) {
                        data.put("wareType", category);
                    }
                    if (!StringUtils.isEmpty(keyword)) {
                        data.put("txtFinds", keyword);
                    }
                    return GoodsBiz.loadGoodsOfShop(GsonUtils.Bean2Json(data));
                }
                return null;
            }

            @Override
            protected void onExecuteSucceeded(List<MedicineListModel> medicineListModels) {
                closeLoding();
                medicineList = medicineListModels;
                bindData2View();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }
}
