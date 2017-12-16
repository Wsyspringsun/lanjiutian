package com.wyw.ljtds.ui.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jauker.widget.BadgeView;
import com.wyw.ljtds.MainActivity;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.AbstractListViewAdapter;
import com.wyw.ljtds.adapter.AbstractViewHolder;
import com.wyw.ljtds.biz.biz.HomeBiz;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.model.RecommendModel;
import com.wyw.ljtds.model.UserDataModel;
import com.wyw.ljtds.model.UserGridleModel;
import com.wyw.ljtds.model.UserModel;
import com.wyw.ljtds.ui.base.ActivityWebView;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.ui.base.LazyLoadFragment;
import com.wyw.ljtds.ui.goods.ActivityMedicinesInfo;
import com.wyw.ljtds.ui.user.help.ActivityHelp;
import com.wyw.ljtds.ui.user.manage.ActivityManage;
import com.wyw.ljtds.ui.user.order.ActivityAfterMarket;
import com.wyw.ljtds.ui.user.order.ActivityOrder;
import com.wyw.ljtds.ui.user.order.ReturnGoodsOrderListActivity;
import com.wyw.ljtds.ui.user.wallet.BalanceActivity;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.DividerGridItemDecoration;
import com.wyw.ljtds.widget.MyGridView;
import com.wyw.ljtds.widget.MyScrollView;
import com.wyw.ljtds.widget.ScrollGridLayoutManager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import static com.wyw.ljtds.ui.user.FragmentUser.TAG_MY_ZUJI;

/**
 * Created by Administrator on 2016/12/9 0009.
 */

@ContentView(R.layout.fragment_user)
public class FragmentUser extends BaseFragment {
    @ViewInject(R.id.gridview)
    private MyGridView gridView; //收藏，足迹 等
    @ViewInject(R.id.name)
    private TextView name;
    @ViewInject(R.id.jifen)
    private TextView jifen;
    @ViewInject(R.id.user_img)
    private SimpleDraweeView user_img;
    @ViewInject(R.id.tuijian)
    private RecyclerView tuijian;
    @ViewInject(R.id.scroll_view)
    private MyScrollView scroll_view;
    @ViewInject(R.id.ll_sc)
    private LinearLayout ll_sc;
    @ViewInject(R.id.fragment_user_nologin)
    private LinearLayout layoutNologin;
    @ViewInject(R.id.fragment_user_logined)
    private RelativeLayout layoutlogin;
    //    @ViewInject( R.id.user )
//    private RecyclerView user_rec;
    public static String TAG_MY = "com.wyw.ljtds.ui.user.FragmentUser.tag_my";
    public static String TAG_MY_SHOUCANG = "tag_shoucang";
    public static String TAG_MY_ZUJI = "tag_zuji";

    private AbstractListViewAdapter<UserGridleModel> adapter;
    private UserModel user;
    //推荐商品
    private List<RecommendModel> list;
    //    private List<UserModel> list_user;
    private MyAdapter myAdapter;
//    private UserAdapter userAdapter;

    @Event(value = {R.id.fragment_user_rl_daifu, R.id.user_img, R.id.order_daifu, R.id.order_daifa, R.id.order_daishou,
            R.id.order_daiping, R.id.order_shouhou, R.id.zhanghuguanli, R.id.user_xiaoxi, R.id.user_shezhi})
    private void onclick(View view) {
        if (!UserBiz.isLogined()) {
            ActivityLogin.goLogin(getActivity());
            return;
        }

        if (user != null) {
            Intent it = null;
            switch (view.getId()) {
                case R.id.fragment_user_rl_daifu://全部
                    it = ActivityOrder.getIntent(getActivity(), 0);
                    startActivity(it);
                    break;
                case R.id.order_daifu://待付
                    it = ActivityOrder.getIntent(getActivity(), 1);
                    startActivity(it);
                    break;

                case R.id.order_daifa://待发
                    it = ActivityOrder.getIntent(getActivity(), 2);
                    startActivity(it);
                    break;

                case R.id.order_daishou://待收
                    it = ActivityOrder.getIntent(getActivity(), 3);
                    startActivity(it);
                    break;
                case R.id.order_daiping://待评
                    it = ActivityOrder.getIntent(getActivity(), 4);
                    startActivity(it);
                    break;

                case R.id.order_shouhou://售后
                    it = new Intent(getActivity(), ReturnGoodsOrderListActivity.class);
                    startActivity(it);
                    break;

                case R.id.user_img://用户头像
                case R.id.user_shezhi://设置
                case R.id.zhanghuguanli://账户管理
                    it = new Intent(getActivity(), ActivityManage.class);
                    it.putExtra("user", user);
                    startActivity(it);
                    break;

                case R.id.user_xiaoxi://消息中心
                    startActivity(new Intent(getActivity(), ActivityMessage.class));
                    break;
                default:
                    break;
            }
        }
    }

    private void initGridView() {


        List<UserGridleModel> list = new ArrayList<UserGridleModel>();
        list.add(new UserGridleModel(R.mipmap.icon_user_shoucang, R.string.user_shoucang));
        list.add(new UserGridleModel(R.mipmap.icon_user_zuji, R.string.user_zuji));
        //list.add(new UserGridleModel(R.mipmap.icon_user_shoucang,R.string.user_dizhi));
        list.add(new UserGridleModel(R.mipmap.icon_user_qianbao, R.string.user_qianbao));
        //list.add(new UserGridleModel(R.mipmap.icon_user_shoucang,R.string.user_jifen));
        list.add(new UserGridleModel(R.mipmap.icon_user_bangzhu, R.string.user_bangzhu));

        adapter = new AbstractListViewAdapter<UserGridleModel>(getActivity(), R.layout.fragment_user_gridview) {
            @Override
            public void initListViewItem(AbstractViewHolder viewHolder, UserGridleModel item) {
                TextView tv = viewHolder.getView(R.id.title);
                tv.setText(item.getStr());
                ImageView img = viewHolder.getView(R.id.img);
                img.setImageResource(item.getImgId());

            }
        };
        adapter.addItems(list);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it;
                if (i == 3) {
                    //case 3://帮助中心
                    it = new Intent(getActivity(), ActivityHelp.class);
                    it = new Intent(getActivity(), ActivityWebView.class);
                    startActivity(it);
                    return;
                }

                if (!UserBiz.isLogined()) {
                    //去登录
                    ActivityLogin.goLogin(getActivity());
                    return;
                }

                switch (i) {
                    case 0://我的收藏
                        it = new Intent(getActivity(), ActivityCollect.class);
                        it.putExtra(TAG_MY, TAG_MY_SHOUCANG);
                        startActivity(it);
                        break;

                    case 1://足迹
                        it = new Intent(getActivity(), ActivityCollect.class);
                        it.putExtra(TAG_MY, TAG_MY_ZUJI);
                        startActivity(it);
                        break;

                    case 2://我的钱包 qianbao
                        it = new Intent(getActivity(), ActivityWallet.class);
                        startActivity(it);
                        break;
                    default:
                        break;


                }
            }
        });

    }

    private void initTuijian() {
        ScrollGridLayoutManager glm = new ScrollGridLayoutManager(getActivity(), 2);
        glm.setScroll(false);
        tuijian.setLayoutManager(glm);
        tuijian.setItemAnimator(new DefaultItemAnimator());
        tuijian.addItemDecoration(new DividerGridItemDecoration(getActivity()));

        tuijian.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent it = new Intent(getActivity(), ActivityMedicinesInfo.class);
                it.putExtra(AppConfig.IntentExtraKey.MEDICINE_INFO_ID, myAdapter.getData().get(i).getWAREID());
                startActivity(it);
            }
        });

        myAdapter = new MyAdapter();
//        myAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        tuijian.setAdapter(myAdapter);
//        tuijian.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() == null) return;

        //添加登录事件
        layoutNologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityLogin.goLogin(getActivity());
            }
        });

        initGridView();
        initTuijian();
//        if (user == null)
//            getUser();
//        if (list == null)
//            getrecommend(PreferenceCache.getToken(), "");
    }


    @Override
    public void onResume() {
        super.onResume();

        //判断是否登录
        if (UserBiz.isLogined()) {
            layoutlogin.setVisibility(View.VISIBLE);
            layoutNologin.setVisibility(View.GONE);
            getUser();
        } else {
            layoutlogin.setVisibility(View.GONE);
            layoutNologin.setVisibility(View.VISIBLE);
        }
        ;

        if (list == null)
            getrecommend(PreferenceCache.getToken(), "");

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    private void userData() {
//        userData
        setLoding(getActivity(), false);
        new BizDataAsyncTask<UserDataModel>() {
            @Override
            protected UserDataModel doExecute() throws ZYException, BizFailure {
                return UserBiz.userData();
            }

            @Override
            protected void onExecuteSucceeded(UserDataModel model) {
                closeLoding();
                updUserDataView(model);
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    private void addBadge(View v, int cnt) {
        if (cnt <= 0) {
            return;
        }
        BadgeView badge = new BadgeView(getActivity());
        badge.setTargetView(v);
        badge.setBadgeCount(cnt);
    }

    private void updUserDataView(UserDataModel model) {
        RelativeLayout rlDaifu = (RelativeLayout) findViewById(R.id.order_daifu);
        addBadge(rlDaifu.getChildAt(0), model.getDaiFu());

        RelativeLayout rlDaifa = (RelativeLayout) findViewById(R.id.order_daifa);
        addBadge(rlDaifa.getChildAt(0), model.getDaiFa());

        RelativeLayout rlDaishou = (RelativeLayout) findViewById(R.id.order_daishou);
        addBadge(rlDaishou.getChildAt(0), model.getDaiShou());

        RelativeLayout rlDaiping = (RelativeLayout) findViewById(R.id.order_daiping);
        addBadge(rlDaiping.getChildAt(0), model.getDaiPing());

        RelativeLayout rlShouhou = (RelativeLayout) findViewById(R.id.order_shouhou);
        addBadge(rlShouhou.getChildAt(0), model.getShouHou());

    }

    private void getUser() {
        setLoding(getActivity(), false);
        new BizDataAsyncTask<UserModel>() {
            @Override
            protected UserModel doExecute() throws ZYException, BizFailure {
                return UserBiz.getUser();
            }

            @Override
            protected void onExecuteSucceeded(UserModel userModel) {
                closeLoding();

                userData();

                user = userModel;

                jifen.setText("积分：" + userModel.getUSER_POINT() + "分");
                name.setText(userModel.getMOBILE());
                if (StringUtils.isEmpty(userModel.getUSER_ICON_FILE_ID())) {
                    user_img.setImageURI(Uri.parse(""));
                    user.setUSER_ICON_FILE_ID("");
                } else {
                    user_img.setImageURI(Uri.parse(AppConfig.IMAGE_PATH + userModel.getUSER_ICON_FILE_ID()));
                    user.setUSER_ICON_FILE_ID(userModel.getUSER_ICON_FILE_ID());
                }

                if (user.getBIRTHDAY() == null) {
                    user.setBIRTHDAY(new Long(0));
                }
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    private void getrecommend(final String token, final String orderid) {
        setLoding(getActivity(), false);
        new BizDataAsyncTask<List<RecommendModel>>() {
            @Override
            protected List<RecommendModel> doExecute() throws ZYException, BizFailure {
                return HomeBiz.getRecommend(token, orderid);
            }

            @Override
            protected void onExecuteSucceeded(List<RecommendModel> recommendModels) {
                closeLoding();
                list = recommendModels;
                myAdapter.setNewData(list);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }


    private class MyAdapter extends BaseQuickAdapter<RecommendModel> {

        public MyAdapter() {
            super(R.layout.item_goods_grid, list);
        }


        @Override
        protected void convert(BaseViewHolder baseViewHolder, RecommendModel recommendModel) {
            baseViewHolder.setText(R.id.goods_title, StringUtils.deletaFirst(recommendModel.getWARENAME()))
                    .setText(R.id.money, recommendModel.getSALEPRICE() + "");

            SimpleDraweeView goods_img = baseViewHolder.getView(R.id.item_head_img);
            if (StringUtils.isEmpty(recommendModel.getIMG_PATH())) {
                goods_img.setImageURI(Uri.parse(""));
            } else {
                goods_img.setImageURI(Uri.parse(recommendModel.getIMG_PATH()));
            }

        }
    }


}
