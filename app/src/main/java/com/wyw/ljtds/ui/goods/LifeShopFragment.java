package com.wyw.ljtds.ui.goods;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.goodsinfo.NetworkImageHolderView;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.model.ShopImg;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.widget.DividerGridItemDecoration;
import com.wyw.ljtds.widget.GridItemDecoration;
import com.wyw.ljtds.widget.logistics.item.LinearDividerItemDecoration;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShopFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@ContentView(R.layout.fragment_life_shop)
public class LifeShopFragment extends BaseFragment {
    private static final String ARG_SHOPID = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @ViewInject(R.id.fragment_lifeshop_ry_data)
    RecyclerView ryData;

//    @ViewInject(R.id.fragment_shopimg_iv_shopname)
//    TextView tvShopName;
//    @ViewInject(R.id.fragment_shopimg_iv_logo)
//    SimpleDraweeView sdvShopLogo;

//    @ViewInject(R.id.fragment_shopimg_iv_banner)
//    SimpleDraweeView ivShopBanner;

//    @ViewInject(R.id.fragment_shopimg_rv_activity)
//    ConvenientBanner bnrActivity;

//    @ViewInject(R.id.fragment_shopimg_rv_nearest)
//    RecyclerView ryvNearest;

//    @ViewInject(R.id.fragment_shopimg_rv_types)
//    RecyclerView ryvTypes;

    private View noData;
    private List<ShopImg> goodsActivity;
    private List<ShopImg> goodsNearest;
    private List<ShopImg> goodsTypes;

    public LifeShopFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BalanceFragment.
     */
    public static LifeShopFragment newInstance(String shopid, int type) {
        LifeShopFragment fragment = new LifeShopFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SHOPID, shopid);
        args.putInt(ARG_PARAM2, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!isAdded())
            return;
        noData = getActivity().getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) ryData.getParent(), false);
        SimpleClickListener commLsnr = new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                ShopImg goods = (ShopImg) baseQuickAdapter.getItem(i);
                if (goods == null) return;
                toCommActivity(goods);
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, final int i) {
            }

            @Override
            public void onItemChildLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }
        };

//        bnrActivity.setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused});
//        bnrActivity.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
/*        bnrActivity.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                ToastUtil.show(getActivity(), "pos:" + position);
                ShopImg goods = goodsActivity.get(position);
                if (goods == null) return;
                toCommActivity(goods);

            }
        });*/

        int type = getArguments().getInt(ARG_PARAM2);
        ryData.addOnItemTouchListener(commLsnr);
        switch (type) {
            case 0:
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                ryData.setLayoutManager(llm);
                ryData.addItemDecoration(new LinearDividerItemDecoration(new LinearDividerItemDecoration.Builder()));
                break;
            case 1:
                GridLayoutManager glm = new GridLayoutManager(getActivity(), 2);
                ryData.setLayoutManager(glm);
                ryData.addItemDecoration(new DividerGridItemDecoration(getActivity()));
                break;
            case 2:
                LinearLayoutManager llm2 = new LinearLayoutManager(getActivity());
                ryData.setLayoutManager(llm2);
                ryData.addItemDecoration(new LinearDividerItemDecoration(new LinearDividerItemDecoration.Builder()));
                break;
        }
        loadData();
    }

    private void toCommActivity(ShopImg goods) {
        Intent it = null;
        it = ActivityLifeGoodsInfo.getIntent(getActivity(), goods.getURL());
        startActivity(it);
    }


    public void bindData2View(List<ShopImg> data) {
        if (!isAdded()) return;
        NearestAdapter adapter = new NearestAdapter(data);
        if (data == null) {
            adapter.setEmptyView(noData);
        }
        ryData.setAdapter(adapter);
    }

    public void loadData() {
        final String shopId = getArguments().getString(ARG_SHOPID);
        new BizDataAsyncTask<List<ShopImg>>() {
            @Override
            protected List<ShopImg> doExecute() throws ZYException, BizFailure {
                return GoodsBiz.readShopPage(shopId);
            }

            @Override
            protected void onExecuteSucceeded(List<ShopImg> shopImgs) {
                closeLoding();
                updView(shopImgs);
            }


            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    public void updView(List<ShopImg> shopImgs) {
        if (shopImgs == null || shopImgs.isEmpty())
            return;
        //活动
        goodsActivity = new ArrayList<>();
        //最新
        goodsNearest = new ArrayList<>();
        //分类
        goodsTypes = new ArrayList<>();
        for (ShopImg si : shopImgs) {
            if ("0".equals(si.getTYPE())) {
                goodsNearest.add(si);
            } else if ("1".equals(si.getTYPE())) {
                //类别
                goodsTypes.add(si);
            } else if ("2".equals(si.getTYPE())) {
                //活动
                goodsActivity.add(si);
            }
        }

//        bindData2View(goodsNearest);
        bindData2View(goodsActivity);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private class NearestAdapter extends BaseQuickAdapter<ShopImg> {
        public NearestAdapter(List<ShopImg> list) {
            super(R.layout.item_shopimg, list);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, ShopImg o) {
            if (o == null)
                return;

            ImageView ivItem = baseViewHolder.getView(R.id.item_shopimg_iv_shopimgitem);


            Picasso.with(getActivity()).load(Uri.parse(o.getIMG_PATH())).resizeDimen(R.dimen.x100, R.dimen.x32).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(ivItem);
        }
    }
}
