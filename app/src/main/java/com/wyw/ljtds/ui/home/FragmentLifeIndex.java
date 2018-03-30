package com.wyw.ljtds.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.wyw.ljtds.MainActivity;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.LifeIndexAdapter;
import com.wyw.ljtds.biz.biz.HomeBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.model.AddressModel;
import com.wyw.ljtds.model.HomePageModel1;
import com.wyw.ljtds.model.MyLocation;
import com.wyw.ljtds.model.SingleCurrentUser;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.ui.category.ActivityScan;
import com.wyw.ljtds.ui.goods.ActivityGoodsList;
import com.wyw.ljtds.ui.user.ActivityMessage;
import com.wyw.ljtds.ui.user.address.AddressSelActivity;
import com.wyw.ljtds.utils.ToastUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import static android.R.attr.y;
import static com.wyw.ljtds.R.id.header;

/**
 * Created by Administrator on 2016/12/9 0009.
 */

@ContentView(R.layout.fragment_life_index)
public class FragmentLifeIndex extends BaseFragment {

    @ViewInject(R.id.fragment_life_sr)
    SwipeRefreshLayout srf;
    @ViewInject(R.id.fragment_life_sv)
    NestedScrollView sv;
    @ViewInject(R.id.fragment_life_index_main)
    RecyclerView rylvIndexMain;
    @ViewInject(R.id.fragment_life_index_header)
    LinearLayout llLifeHeader;
    @ViewInject(R.id.main_header_location)
    TextView tvLocation;

    //存储首页数据
    private HomePageModel1 homePageModel;
    BDLocationListener locationListner = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                MyLocation loc = MyLocation.newInstance(location.getLatitude(), location.getLongitude(), location.getAddrStr());
                SingleCurrentUser.updateLocation(loc);
                ((MyApplication) getActivity().getApplication()).locationService.unregisterListener(locationListner); //注销掉监听
                tvLocation.setText(SingleCurrentUser.location.getAddrStr());
            }
        }
    };

    private static final int REQUEST_CHANGE_LOCATION = 1;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //init all view
        initView();
        //加载服务器数据
        //Location
        loadhomeData();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Event(value = {R.id.ll_search, R.id.zxing, R.id.right_img, R.id.img_home_active_goods})
    private void onclick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.ll_search:
                it = new Intent(getActivity(), ActivitySearch.class);
                it.putExtra("from", 1);
                startActivity(it);
                break;
            //扫描二维码
            case R.id.zxing:
                //生活馆
                it = new Intent(getActivity(), ActivityScan.class);
                it.putExtra(AppConfig.IntentExtraKey.CAT_FROM, AppConfig.LIFE);
                startActivity(it);
                break;

            case R.id.img_home_active_goods:
                it = new Intent(getActivity(), ActivityGoodsList.class);
                it.putExtra("typeid", "");
                startActivity(it);
                break;
            case R.id.right_img:
                startActivity(new Intent(getActivity(), ActivityMessage.class));
                break;
            default:
                break;
        }
    }

    public static FragmentLifeIndex newInstance() {
        FragmentLifeIndex fragment = new FragmentLifeIndex();
        Bundle args = new Bundle();
//        args.putInt(ARG_CAT, resId);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        srf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadhomeData();
                srf.setRefreshing(false);
            }
        });
        //location info
        tvLocation.setText("定位中...");
        setLocation();
        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = AddressSelActivity.getIntent(getActivity(), true);
                startActivityForResult(it, REQUEST_CHANGE_LOCATION);
            }
        });

        //12 columns grid
        GridLayoutManager layoutManger = new GridLayoutManager(getActivity(), 12);
        rylvIndexMain.setLayoutManager(layoutManger);
        rylvIndexMain.setItemAnimator(new DefaultItemAnimator());
        sv.setOnScrollChangeListener(new View.OnScrollChangeListener() {

            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                int ivHeight = (int) getResources().getDimension(R.dimen.x150);
                if (scrollY <= 0) {
                    llLifeHeader.setBackgroundColor(Color.TRANSPARENT);
                } else if (scrollY < ivHeight) {
                    float scale = (float) scrollY / (float) ivHeight;
                    float alpha = 255 * scale;
                    // TODO: 2016/9/3 注释里面的方法也可以实现
                    //先设置一个背景，然后在让背景乘以透明度
//                    header.setBackgroundColor(getResources().getColor(R.color.base_bar));
                    llLifeHeader.setBackgroundColor(getResources().getColor(R.color.base_bar));
                    llLifeHeader.getBackground().setAlpha((int) alpha);

                } else if (scrollY >= ivHeight) {
                    llLifeHeader.setBackgroundColor(getResources().getColor(R.color.base_bar));
                }
//                Log.e(AppConfig.ERR_TAG, "ivHeight:" + ivHeight + "int scrollX:" + scrollX + "int scrollY:" + scrollY + "int oldScrollX:" + oldScrollX + "int oldScrollY:" + oldScrollY);
            }
        });

        /*rylvIndexMain.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (0 == newState) {
                    //0标识到达了
//                    int fp = ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                    //完成滑动
                    *//*if (fp == 0) {
                        llLifeHeader.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        llLifeHeader.setBackgroundColor(getResources().getColor(R.color.base_bar));
                    }*//*
                }
//                Log.e(AppConfig.ERR_TAG, "newState:" + newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//
            }
        });*/
    }

    //从网络加载首页所有数据
    private void loadhomeData() {
        new BizDataAsyncTask<HomePageModel1>() {
            @Override
            protected HomePageModel1 doExecute() throws ZYException, BizFailure {
                return HomeBiz.getHome1();
            }

            @Override
            protected void onExecuteSucceeded(HomePageModel1 data) {
//                close
                homePageModel = data;
                bindData2View();
            }

            @Override
            protected void OnExecuteFailed() {

            }
        }.execute();
    }

    /**
     * 将数据在试图中展现
     */
    private void bindData2View() {
        LifeIndexAdapter adapter = new LifeIndexAdapter(getActivity(), homePageModel);
        rylvIndexMain.setAdapter(adapter);
        //广播新闻
//        data = new ArrayList<>();
//        for (int i = 0; i < homePageModel.getNews().size(); i++) {
//            data.add(homePageModel.getNews().get(i).getTITLE());
//        }
//        marqueeView.startWithList(data);
//
//
//        //推荐
//        list1 = new ArrayList<>();
//        list1 = homePageModel.getRecommendComms();
//        for (int j = 0; j < list1.size(); j++) {
//            list1.get(j).setFlg(false);
//        }
//        adapter.setNewData(list1);
//        adapter.notifyDataSetChanged();
//
//        updIconRecyview(homePageModel);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CHANGE_LOCATION:
                    if (data != null) {
                        Parcelable paddr = data.getParcelableExtra(AddressSelActivity.TAG_SELECTED_ADDRESS);
                        if (paddr != null) {
                            AddressModel addr = (AddressModel) paddr;
                            if (addr != null) {
                                String sLoc = addr.getADDRESS_LOCATION();
                                StringBuilder err = new StringBuilder();
                                MyLocation loc = AddressModel.parseLocation(err, sLoc);
                                loc.setADDRESS_ID(addr.getADDRESS_ID() + "");
                                if (err.length() > 0) {
                                    ToastUtil.show(getActivity(), err.toString());
                                    return;
                                }
                                SingleCurrentUser.location = loc;
                            }
                        }
                    }

                    tvLocation.setText(SingleCurrentUser.location.getAddrStr());
                    break;
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setLocation();
        }
    }

    public void setLocation() {
        if (SingleCurrentUser.location != null && !SingleCurrentUser.location.getAddrStr().equals(tvLocation.getText())) {
            tvLocation.setText(SingleCurrentUser.location.getAddrStr());
        }

    }
}
