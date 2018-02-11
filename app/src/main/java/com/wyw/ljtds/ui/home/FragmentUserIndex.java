package com.wyw.ljtds.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.LifeIndexAdapter;
import com.wyw.ljtds.adapter.UserIndexAdapter;
import com.wyw.ljtds.biz.biz.HomeBiz;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.model.AddressModel;
import com.wyw.ljtds.model.HomePageModel1;
import com.wyw.ljtds.model.IconText;
import com.wyw.ljtds.model.MyLocation;
import com.wyw.ljtds.model.RecommendModel;
import com.wyw.ljtds.model.SingleCurrentUser;
import com.wyw.ljtds.model.UserDataModel;
import com.wyw.ljtds.model.UserIndexModel;
import com.wyw.ljtds.model.UserModel;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.ui.category.ActivityScan;
import com.wyw.ljtds.ui.goods.ActivityGoodsList;
import com.wyw.ljtds.ui.user.ActivityLogin;
import com.wyw.ljtds.ui.user.ActivityMessage;
import com.wyw.ljtds.ui.user.address.AddressSelActivity;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/9 0009.
 */

@ContentView(R.layout.fragment_user_index)
public class FragmentUserIndex extends BaseFragment {
    @ViewInject(R.id.fragment_user_index_main)
    private RecyclerView rylvIndexMain;

    private UserIndexModel model = new UserIndexModel();
    private UserIndexAdapter adapter;
    private final int MYINDEX = 4;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //init all view
        initView();

        initData();

    }

    private void initData() {
        List<IconText> orderIcons = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            IconText item = new IconText();
            switch (i) {
                case 0:
                    item.setImgId(R.drawable.icon_daifu);
                    item.setText(getString(R.string.daifu));
                    break;
                case 1:
                    item.setImgId(R.drawable.icon_daifa);
                    item.setText(getString(R.string.daifa));
                    break;
                case 2:
                    item.setImgId(R.drawable.icon_daishou);
                    item.setText(getString(R.string.daishou));
                    break;
                case 3:
                    item.setImgId(R.drawable.icon_daiping);
                    item.setText(getString(R.string.daiping));
                    break;
                case 4:
                    item.setImgId(R.drawable.icon_shouhou);
                    item.setText(getString(R.string.shouhou));
                    break;
            }
            orderIcons.add(item);
        }
        model.setOrderIcons(orderIcons);

        List<IconText> extraIcons = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            IconText item = new IconText();
            switch (i) {
                case 0:
                    item.setImgId(R.mipmap.icon_user_shoucang);
                    item.setText(getString(R.string.user_shoucang));
                    break;
                case 1:
                    item.setImgId(R.mipmap.icon_user_zuji);
                    item.setText(getString(R.string.user_zuji));
                    break;
                case 2:
                    item.setImgId(R.mipmap.icon_user_qianbao);
                    item.setText(getString(R.string.user_qianbao));
                    break;
                case 3:
                    item.setImgId(R.mipmap.icon_user_bangzhu);
                    item.setText(getString(R.string.user_bangzhu));
                    break;
            }
            extraIcons.add(item);
        }
        model.setExtraIcons(extraIcons);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    private void loadData() {
        Utils.log("FragmentUserIndex start " + AppConfig.currSel);
        if (AppConfig.currSel != MYINDEX) {
            return;
        }
        if (!UserBiz.isLogined()) {
            model.setUserModel(null);
            model.setUserOrderNumberModel(null);
            adapter = new UserIndexAdapter(getActivity(), model);
            rylvIndexMain.setAdapter(adapter);
            startActivity(ActivityLogin.getIntent(getActivity()));
            return;
        }
        getUser();
        getRecommend();
    }

    public static FragmentUserIndex newInstance() {
        FragmentUserIndex fragment = new FragmentUserIndex();
        Bundle args = new Bundle();
//        args.putInt(ARG_CAT, resId);
        fragment.setArguments(args);
        return fragment;
    }

    private void initView() {
        //12 columns grid
        GridLayoutManager layoutManger = new GridLayoutManager(getActivity(), 20);
        rylvIndexMain.setLayoutManager(layoutManger);
        rylvIndexMain.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 将数据在试图中展现
     */
    private void bindData2View() {
        if (adapter == null) {
            adapter = new UserIndexAdapter(getActivity(), model);
            rylvIndexMain.setAdapter(adapter);
        }
        adapter.setModel(model);
        adapter.notifyDataSetChanged();

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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            loadData();
        }
    }

    private void getUser() {
        setLoding(getActivity(), false);
        new BizDataAsyncTask<UserModel>() {
            @Override
            protected UserModel doExecute() throws ZYException, BizFailure {
                return UserBiz.getUser();
            }

            @Override
            protected void onExecuteSucceeded(UserModel data) {
                model.setUserModel(data);
                loadUserOrderNumber();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    private void loadUserOrderNumber() {
//        userData
        new BizDataAsyncTask<UserDataModel>() {
            @Override
            protected UserDataModel doExecute() throws ZYException, BizFailure {
                return UserBiz.loadUserOrderNumber();
            }

            @Override
            protected void onExecuteSucceeded(UserDataModel data) {
                closeLoding();
                model.setUserOrderNumberModel(data);
                bindData2View();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    private void getRecommend() {
        new BizDataAsyncTask<List<RecommendModel>>() {
            @Override
            protected List<RecommendModel> doExecute() throws ZYException, BizFailure {
                return HomeBiz.getRecommend();
            }

            @Override
            protected void onExecuteSucceeded(List<RecommendModel> data) {
                closeLoding();
                model.setRecommendModels(data);
                bindData2View();
            }

            @Override
            protected void OnExecuteFailed() {
                loadUserOrderNumber();
            }
        }.execute();
    }
}
