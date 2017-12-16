package com.wyw.ljtds.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.LifeIndexAdapter;
import com.wyw.ljtds.biz.biz.HomeBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.model.HomePageModel1;
import com.wyw.ljtds.service.LocationService;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.ui.category.ActivityScan;
import com.wyw.ljtds.ui.goods.ActivityGoodsList;
import com.wyw.ljtds.ui.user.ActivityMessage;
import com.wyw.ljtds.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2016/12/9 0009.
 */

@ContentView(R.layout.fragment_life_index)
public class FragmentLifeIndex extends BaseFragment {
    @ViewInject(R.id.fragment_life_index_main)
    RecyclerView rylvIndexMain;
    @ViewInject(R.id.fragment_life_index_header)
    LinearLayout llLifeHeader;
    @ViewInject(R.id.main_header_location)
    TextView tvLocation;
    private LocationService locationService;

    //存储首页数据
    private HomePageModel1 homePageModel;



    // Location listener
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                Utils.setIconText(getActivity(), tvLocation, "");
                sb.append("\ue622");
                sb.append(location.getAddrStr());
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
//                    for (int i = 0; i < location.getPoiList().size(); i++) {
//                        Poi poi = location.getPoiList().get(i);
//                        sb.append(poi.getName() + "");
//                    }
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                tvLocation.setText(sb.toString());
//                Log.e(AppConfig.ERR_TAG, "Location Info：" + sb.toString());
            }
        }

    };



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //init all view
        initView();
        //加载服务器数据
        //Location
    }

    @Override
    public void onStart() {
        super.onStart();
        loadhomeData();
        // -----------location config ------------
        locationService = ((MyApplication) getActivity().getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);

        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();// 定位SDK
    }

    @Override
    public void onStop() {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
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

    private void initView() {
        //12 columns grid
        GridLayoutManager layoutManger = new GridLayoutManager(getActivity(), 12);
        rylvIndexMain.setLayoutManager(layoutManger);
        rylvIndexMain.setItemAnimator(new DefaultItemAnimator());
        rylvIndexMain.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (0 == newState) {
                    //0标识到达了
                    int fp = ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                    //完成滑动
                    if (fp == 0) {
                        llLifeHeader.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        llLifeHeader.setBackgroundColor(getResources().getColor(R.color.base_bar));
                    }
                }
//                Log.e(AppConfig.ERR_TAG, "newState:" + newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                Log.e(AppConfig.ERR_TAG, "onScrolled:" + recyclerView.getScrollState() + dy);
//                if (adapter1 == null)
//                    return;
//                int cnt = adapter1.getItemCount();
////                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
//                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
//                if (!end && !loading && (lastVisibleItem) >= cnt) {
//                    page = page + 1;
//                    loadDianzibiLog();
//                }
            }
        });
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

}
