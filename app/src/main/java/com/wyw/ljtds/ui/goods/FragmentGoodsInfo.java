package com.wyw.ljtds.ui.goods;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gxz.PagerSlidingTabStrip;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.CommodityDetailsModel;
import com.wyw.ljtds.model.GoodSubmitModel1;
import com.wyw.ljtds.model.GoodSubmitModel2;
import com.wyw.ljtds.model.GoodSubmitModel3;
import com.wyw.ljtds.model.MedicineDetailsEvaluateModel;
import com.wyw.ljtds.model.ShoppingCartAddModel;
import com.wyw.ljtds.model.SqlFavoritesModel;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.utils.DateUtils;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.SqlUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.widget.RecycleViewDivider;
import com.wyw.ljtds.widget.commodity.CheckInchModel;
import com.wyw.ljtds.widget.commodity.CheckInchPopWindow;
import com.wyw.ljtds.widget.goodsinfo.SlideDetailsLayout;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;

/**
 * Created by Administrator on 2017/3/12 0012.
 */

@ContentView(R.layout.fragment_goods_info)
public class FragmentGoodsInfo extends BaseFragment implements SlideDetailsLayout.OnSlideDetailsListener {
    //    @ViewInject(R.id.psts_tabs)
//    private PagerSlidingTabStrip psts_tabs;
//    @ViewInject(R.id.sv_switch)
//    private SlideDetailsLayout sv_switch;
//    @ViewInject(R.id.sv_goods_info)
//    private ScrollView sv_goods_info;
//    @ViewInject(R.id.fab_up_slide)
//    private FloatingActionButton fab_up_slide;
//    @ViewInject(R.id.vp_item_goods_img)
//    public ConvenientBanner vp_item_goods_img;
//    //    @ViewInject(R.id.vp_recommend)
////    public ConvenientBanner vp_recommend;
//    @ViewInject(R.id.ll_goods_detail)
//    private LinearLayout ll_goods_detail;
//    @ViewInject(R.id.ll_goods_config)
//    private LinearLayout ll_goods_config;
//    @ViewInject(R.id.tv_goods_detail)
//    private TextView tv_goods_detail;
//    @ViewInject(R.id.tv_goods_config)
//    private TextView tv_goods_config;
//    @ViewInject(R.id.v_tab_cursor)
//    private View v_tab_cursor;
//    @ViewInject(R.id.flag_otc)
//    private ImageView flag_otc;
//    @ViewInject(R.id.goods_changjia)
//    private TextView goods_changjia;
//    @ViewInject(R.id.ll_comment)
//    public LinearLayout ll_comment;
//    @ViewInject(R.id.ll_pull_up)
//    public LinearLayout ll_pull_up;
//    @ViewInject(R.id.tv_goods_title)
//    public TextView tv_goods_title;
//    @ViewInject(R.id.shoucang_img)
//    private ImageView shoucang_img;
//    @ViewInject(R.id.eva_list)
//    private RecyclerView eva_list;
//
//
//    public FragmentGoodsParameter fragmentGoodsParameter;
//    public FragmentGoodsDetails fragmentGoodsDetails;
//    private Fragment nowFragment;//当前是哪个fragment
//    private int nowIndex;//fragment判定下标
//    private float fromX;
//    private List<TextView> tabTextList;
//    private List<Fragment> fragmentList = new ArrayList<>();
//    private FragmentTransaction fragmentTransaction;
//    private FragmentManager fragmentManager;
//    private ActivityGoodsInfo activity;//父级activity
//    private boolean isCollect = false;//是否点击收藏
//    private CommodityDetailsModel model;
//    public CheckInchModel checkInchModel;
////    private List<MedicineDetailsModel.EVALUATEs> list_eva;
////    private MyAdapter adapter;
//
//
//    @Event(value = {R.id.fab_up_slide, R.id.ll_comment, R.id.ll_pull_up, R.id.ll_goods_detail, R.id.ll_goods_config, R.id.shoucang,
//            R.id.vp_item_goods_img})
//    private void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.ll_pull_up:
//                //上拉查看图文详情
//                sv_switch.smoothOpen(true);
//                break;
//
//            case R.id.fab_up_slide:
//                //点击滑动到顶部
//                sv_goods_info.smoothScrollTo(0, 0);
//                sv_switch.smoothClose(true);
//                break;
//
//            case R.id.ll_goods_detail:
//                //商品详情tab
//                nowIndex = 0;
//                scrollCursor();
//                switchFragment(nowFragment, fragmentGoodsDetails);
//                nowFragment = fragmentGoodsDetails;
//                break;
//
//            case R.id.ll_goods_config:
//                //规格参数tab
//                nowIndex = 1;
//                scrollCursor();
//                switchFragment(nowFragment, fragmentGoodsParameter);
//                nowFragment = fragmentGoodsParameter;
//                break;
//
//            case R.id.shoucang:
//                Log.e(AppConfig.ERR_TAG, "fav:" + model.getFavorited());
//                //收藏
//                setLoding(getActivity(), false);
//                if (model.getFavorited().equals("0")) {
//                    Favorites(model.getCommodityId(), "0", "add");
//                } else {
//                    Favorites(model.getCommodityId(), "0", "del");
//                }
//                break;
//
////            case R.id.tv_current_goods:
////                //选择规格
////                final CheckInchPopWindow popWindow = new CheckInchPopWindow(getActivity(), model);
////                if (checkInchModel != null) {
////                    popWindow.setSelect(checkInchModel);
////                }
////
////                popWindow.setOnItemClickListener(new CheckInchPopWindow.OnItemClickListener() {
////                    @Override
////                    public void onClickAdd2Car(CheckInchModel result) {
////                        ShoppingCartAddModel shoppingCartAddModel = new ShoppingCartAddModel();
////                        shoppingCartAddModel.setCOMMODITY_ID(model.getCommodityId());
////                        shoppingCartAddModel.setCOMMODITY_SIZE(checkInchModel.getSize());
////                        shoppingCartAddModel.setCOMMODITY_COLOR(checkInchModel.getCololr());
////                        shoppingCartAddModel.setEXCHANGE_QUANLITY(checkInchModel.getNum() + "");
////                        shoppingCartAddModel.setCOMMODITY_COLOR_ID(checkInchModel.getColor_id());
////                        shoppingCartAddModel.setCOMMODITY_SIZE_ID(checkInchModel.getSize_id());
////                        shoppingCartAddModel.setINS_USER_ID(model.getOidGroupId());
////                        String str = GsonUtils.Bean2Json(shoppingCartAddModel);
////                        addCart(str);
////                        popWindow.dissmiss();
////                    }
////
////                    @Override
////                    public void onClickBuyNow(CheckInchModel result) {
////                        checkInchModel = result;
////                        Intent it = new Intent(getActivity(), ActivityGoodsSubmit.class);
////
////                        GoodSubmitModel1 goodSubmitModel = new GoodSubmitModel1();
////                        GoodSubmitModel2 goodSubmitModel2 = new GoodSubmitModel2();
////                        List<GoodSubmitModel3> goodList = new ArrayList<>();
////                        List<GoodSubmitModel2> groupList = new ArrayList<>();
////                        GoodSubmitModel3 goods = new GoodSubmitModel3();
////                        goods.setEXCHANGE_QUANLITY(checkInchModel.getNum());
////                        goods.setCOMMODITY_COLOR(checkInchModel.getCololr());
////                        goods.setCOMMODITY_SIZE(checkInchModel.getSize());
////                        goods.setCOMMODITY_COLOR_ID(checkInchModel.getColor_id());
////                        goods.setCOMMODITY_SIZE_ID(checkInchModel.getSize_id());
////                        goods.setCOMMODITY_ID(model.getCommodityId());
////                        goods.setCOMMODITY_NAME(model.getTitle());
////                        goodList.add(goods);
////
////                        goodSubmitModel2.setDETAILS(goodList);
////                        goodSubmitModel2.setOID_GROUP_ID(model.getOidGroupId());
////                        goodSubmitModel2.setOID_GROUP_NAME(model.getGroupName());
////
////                        groupList.add(goodSubmitModel2);
////                        goodSubmitModel.setDETAILS(groupList);
////
////                        it.putExtra("data", GsonUtils.Bean2Json(goodSubmitModel));
////                        popWindow.dissmiss();
////                        activity.startActivity(it);
////                    }
////                });
////                popWindow.setOnSelectedCompleteListener(new CheckInchPopWindow.OnSelectedCompleteLinstener() {
////                    @Override
////                    public void onComplete(CheckInchModel result) {
////                        checkInchModel = result;
////                        tv_goods_title.setText(model.getTitle());
//////                        tv_new_price.setText(checkInchModel.getNew_money() + "");
//////                        tv_old_price.setText(checkInchModel.getOld_money() + "");
//////                        tv_current_goods.setText("已选择：" + "\"" + checkInchModel.getCololr() + "\"  \"" + checkInchModel.getSize() + "\"");
////
////                        setLoopView(checkInchModel.getImage());
////
////                    }
////                });
////                popWindow.showAsDropDown(v);
////
////                break;
//
//            case R.id.vp_item_goods_img:
//                break;
//
//            default:
//                break;
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        activity = (ActivityGoodsInfo) context;
//    }
//
//    // 开始自动翻页
//    @Override
//    public void onResume() {
//        super.onResume();
//        //开始自动翻页
//        vp_item_goods_img.startTurning(3000);
//    }
//
//    // 停止自动翻页
//    @Override
//    public void onPause() {
//        super.onPause();
//        //停止翻页
//        vp_item_goods_img.stopTurning();
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        flag_otc.setVisibility(View.GONE);
//
//        fragmentGoodsDetails = new FragmentGoodsDetails();
//        fragmentGoodsParameter = new FragmentGoodsParameter();
//        fragmentList.add(fragmentGoodsParameter);
//        fragmentList.add(fragmentGoodsDetails);
//
//        nowFragment = fragmentGoodsDetails;
//        fragmentManager = getChildFragmentManager();
//        //默认显示商品详情tab
//        fragmentManager.beginTransaction().replace(R.id.fl_content, nowFragment).commitAllowingStateLoss();
//
//        sv_switch.setOnSlideDetailsListener(this);
//
//        //ScrollView和edittext焦点冲突问题
//        sv_goods_info.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
//        sv_goods_info.setFocusable(true);
//        sv_goods_info.setFocusableInTouchMode(true);
//        sv_goods_info.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                v.requestFocusFromTouch();
//                return false;
//            }
//        });
//
//        //设置文字中间一条横线
////        tv_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
////        tv_old_price.getPaint().setAntiAlias(true);//抗锯齿
//
//        //设置默认未选择商品规格
////        tv_current_goods.setText(R.string.qingxuanze);
//
//        //浮标隐藏
//        fab_up_slide.hide();
//
//
//        //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//        vp_item_goods_img.setPageIndicator(new int[]{R.mipmap.index_white, R.mipmap.index_red});
//        vp_item_goods_img.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
//
//        fragmentList = new ArrayList<>();
//        tabTextList = new ArrayList<>();
//        tabTextList.add(tv_goods_detail);
//        tabTextList.add(tv_goods_config);
//
//
//    }
//
//    /**
//     * 给商品轮播图设置图片路径
//     */
//    public void setLoopView(String[] images) {
//        List<String> imgUrls = new ArrayList<>();
//        if (images.length == 0 || images == null) {
//            imgUrls.add("");
//            imgUrls.add("");
//            imgUrls.add("");
//            imgUrls.add("");
//            imgUrls.add("");
//        } else {
//            for (int i = 0; i < images.length; i++) {
//                imgUrls.add(images[i]);
//            }
//        }
//
//
//        //初始化商品图片轮播
//        vp_item_goods_img.setPages(new CBViewHolderCreator() {
//            @Override
//            public Object createHolder() {
//                return new NetworkImageHolderView();
//            }
//        }, imgUrls);
//
//
//        List<RecommendGoodsBean> data = new ArrayList<>();
//        data.add(new RecommendGoodsBean("Letv/乐视 LETV体感-超级枪王 乐视TV超级电视产品玩具 体感游戏枪 电玩道具 黑色",
//                "http://img4.hqbcdn.com/product/79/f3/79f3ef1b0b2283def1f01e12f21606d4.jpg", new BigDecimal(599), "799"));
//        data.add(new RecommendGoodsBean("IPEGA/艾派格 幽灵之子 无线蓝牙游戏枪 游戏体感枪 苹果安卓智能游戏手柄 标配",
//                "http://img2.hqbcdn.com/product/00/76/0076cedb0a7d728ec1c8ec149cff0d16.jpg", new BigDecimal(199), "399"));
//        data.add(new RecommendGoodsBean("Letv/乐视 LETV体感-超级枪王 乐视TV超级电视产品玩具 体感游戏枪 电玩道具 黑色",
//                "http://img4.hqbcdn.com/product/79/f3/79f3ef1b0b2283def1f01e12f21606d4.jpg", new BigDecimal(599.5), "799"));
//        data.add(new RecommendGoodsBean("IPEGA/艾派格 幽灵之子 无线蓝牙游戏枪 游戏体感枪 苹果安卓智能游戏手柄 标配",
//                "http://img2.hqbcdn.com/product/00/76/0076cedb0a7d728ec1c8ec149cff0d16.jpg", new BigDecimal("299.9"), "399"));
////        List<List<RecommendGoodsBean>> handledData = handleRecommendGoods( data );
////        //设置如果只有一组数据时不能滑动
////        vp_recommend.setManualPageable( handledData.size() == 1 ? false : true );
////        vp_recommend.setCanLoop( handledData.size() == 1 ? false : true );
////        vp_recommend.setPages( new CBViewHolderCreator() {
////            @Override
////            public Object createHolder() {
////                return new ItemRecommendAdapter();
////            }
////        }, handledData );
//    }
//
//
//    public void updeta(CommodityDetailsModel commodityDetailsModel) {
//        model = commodityDetailsModel;
//
//        CommodityDetailsModel.SizeList firstDetail = model.getColorList().get(0).getSizeList().get(0);
//        tv_goods_title.setText(model.getTitle());
////        tv_new_price.setText(firstDetail.getCostMoney() + "");
//        if (!StringUtils.isEmpty(firstDetail.getFlgDetail())) {
////            tv_new_price.setText(firstDetail.getCostMoney() + "\t\t" + firstDetail.getFlgDetail());
//        }
////        tv_old_price.setText(model.getColorList().get(0).getSizeList().get(0).getMarketPrice() + "");
//
//        List<String> imgList = new ArrayList<String>();
//        CommodityDetailsModel.SizeList size = model.getColorList().get(0).getSizeList().get(0);
//        if (!StringUtils.isEmpty(size.getImgPath())) {
//            imgList.add(AppConfig.IMAGE_PATH_LJT + size.getImgPath());
//        }
//        if (!StringUtils.isEmpty(size.getImgPath2())) {
//            imgList.add(AppConfig.IMAGE_PATH_LJT + size.getImgPath2());
//        }
//        if (!StringUtils.isEmpty(size.getImgPath3())) {
//            imgList.add(AppConfig.IMAGE_PATH_LJT + size.getImgPath3());
//        }
//        if (!StringUtils.isEmpty(size.getImgPath4())) {
//            imgList.add(AppConfig.IMAGE_PATH_LJT + size.getImgPath4());
//        }
//        if (!StringUtils.isEmpty(size.getImgPath5())) {
//            imgList.add(AppConfig.IMAGE_PATH_LJT + size.getImgPath5());
//        }
//        if (!StringUtils.isEmpty(size.getImgPath6())) {
//            imgList.add(AppConfig.IMAGE_PATH_LJT + size.getImgPath6());
//        }
//        String[] images = new String[imgList.size()];
//        imgList.toArray(images);
//        //设置默认值
//        checkInchModel = new CheckInchModel();
//        checkInchModel.setNum(1);
//        checkInchModel.setCololr(model.getColorList().get(0).getColorName());
//        checkInchModel.setSize(model.getColorList().get(0).getSizeList().get(0).getCommoditySize());
//        checkInchModel.setColor_id(model.getColorList().get(0).getCommodityColorId());
//        checkInchModel.setSize_id(model.getColorList().get(0).getSizeList().get(0).getCommoditySizeId());
//        checkInchModel.setImage(images);
//        checkInchModel.setNew_money(model.getColorList().get(0).getSizeList().get(0).getCostMoney());
//
//        //轮播
//        setLoopView(images);
//
//        //评论数量
//        Log.e(AppConfig.ERR_TAG, "eva count:" + model.getEVALUATE_CNT() + "");
////        tvGoodInfoComment.setText(model.getEVALUATE_CNT() + "");
//        //评价信息
//        if (!model.getEvaluateList().isEmpty() && model.getEvaluateList() != null) {
////            no_eva.setVisibility(View.GONE);
//            eva_list.setVisibility(View.VISIBLE);
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());//必须有
//            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置方向滑动
//            eva_list.setLayoutManager(linearLayoutManager);
//            eva_list.setItemAnimator(new DefaultItemAnimator());
//            eva_list.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, 10, getResources().getColor(R.color.font_black2)));
//
//            eva_list.setAdapter(new MyAdapter(model.getEvaluateList()));
//        } else {
//            eva_list.setVisibility(View.GONE);
//        }
//
//
//        //是否收藏
//        if (model.getFavorited().equals("1")) {
//            shoucang_img.setImageDrawable(getResources().getDrawable(R.mipmap.icon_shoucang_xuanzhong));
//        } else {
//            shoucang_img.setImageDrawable(getResources().getDrawable(R.mipmap.icon_shoucang_weixuan));
//        }
//    }
//
//    /**
//     * 处理推荐商品数据(每两个分为一组)
//     *
//     * @param data
//     * @return
//     */
//    public static List<List<RecommendGoodsBean>> handleRecommendGoods(List<RecommendGoodsBean> data) {
//        List<List<RecommendGoodsBean>> handleData = new ArrayList<>();
//        int length = data.size() / 2;
//        if (data.size() % 2 != 0) {
//            length = data.size() / 2 + 1;
//        }
//        for (int i = 0; i < length; i++) {
//            List<RecommendGoodsBean> recommendGoods = new ArrayList<>();
//            for (int j = 0; j < (i * 2 + j == data.size() ? 1 : 2); j++) {
//                recommendGoods.add(data.get(i * 2 + j));
//            }
//            handleData.add(recommendGoods);
//        }
//        return handleData;
//    }
//
//    //状态改变
    @Override
    public void onStatucChanged(SlideDetailsLayout.Status status) {
//        Log.e("****", status + "");
//        fragmentGoodsDetails.getmodel(model);
//        fragmentGoodsParameter.getmodel(model);
//        if (status == SlideDetailsLayout.Status.OPEN) {
//            //当前为图文详情页
//            fab_up_slide.show();
//            activity.vp_content.setNoScroll(true);
//            activity.tv_title.setVisibility(View.VISIBLE);
//            activity.psts_tabs.setVisibility(View.GONE);
//        } else {
//            //当前为商品详情页
//            fab_up_slide.hide();
//            activity.vp_content.setNoScroll(false);
//            activity.tv_title.setVisibility(View.GONE);
//            activity.psts_tabs.setVisibility(View.VISIBLE);
//        }
    }
//
//    /**
//     * 滑动游标
//     */
//    private void scrollCursor() {
//        TranslateAnimation anim = new TranslateAnimation(fromX, nowIndex * v_tab_cursor.getWidth(), 0, 0);
//        anim.setFillAfter(true);//设置动画结束时停在动画结束的位置
//        anim.setDuration(50);
//        //保存动画结束时游标的位置,作为下次滑动的起点
//        fromX = nowIndex * v_tab_cursor.getWidth();
//        v_tab_cursor.startAnimation(anim);
//
//        //设置Tab切换颜色
//        for (int i = 0; i < tabTextList.size(); i++) {
//            tabTextList.get(i).setTextColor(i == nowIndex ? getResources().getColor(R.color.base_bar) : getResources().getColor(R.color.font_black2));
//        }
//    }
//
//    /**
//     * 切换Fragment
//     * <p>(hide、show、add)
//     *
//     * @param fromFragment
//     * @param toFragment
//     */
//    private void switchFragment(Fragment fromFragment, Fragment toFragment) {
//        if (nowFragment != toFragment) {
//            fragmentTransaction = fragmentManager.beginTransaction();
//            if (!toFragment.isAdded()) {    // 先判断是否被add过
//                fragmentTransaction.hide(fromFragment).add(R.id.fl_content, toFragment).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到activity中
//            } else {
//                fragmentTransaction.hide(fromFragment).show(toFragment).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
//            }
//        }
//    }
//
//
//    //添加收藏
//    BizDataAsyncTask<Boolean> addTask;
//
//    private void Favorites(final String id, final String flg, final String type) {
//        addTask = new BizDataAsyncTask<Boolean>() {
//            @Override
//            protected Boolean doExecute() throws ZYException, BizFailure {
//                Log.e(AppConfig.ERR_TAG, "type:" + type);
//                if (type.equals("add")) {
//                    return UserBiz.addFavoritesGoods(id, flg);
//                } else {
//                    return UserBiz.deleteFavoritesGoods(id);
//                }
//
//            }
//
//            @Override
//            protected void onExecuteSucceeded(Boolean aBoolean) {
//                if (aBoolean) {
//                    if (type.equals("add")) {
//                        model.setFavorited("1");
//                        shoucang_img.setImageDrawable(getResources().getDrawable(R.mipmap.icon_shoucang_xuanzhong));
//                    } else {
//                        model.setFavorited("0");
//                        shoucang_img.setImageDrawable(getResources().getDrawable(R.mipmap.icon_shoucang_weixuan));
//                    }
//                }
//                closeLoding();
//            }
//
//            @Override
//            protected void OnExecuteFailed() {
//                closeLoding();
//            }
//        };
//        addTask.execute();
//    }
//
//
//    //推荐商品
//    public static class RecommendGoodsBean {
//        private String title;
//        private String imag;
//        private BigDecimal price;
//        private String currentPrice;
//
//        public RecommendGoodsBean() {
//        }
//
//        public RecommendGoodsBean(String title, String imag, BigDecimal price, String currentPrice) {
//            this.title = title;
//            this.imag = imag;
//            this.price = price;
//            this.currentPrice = currentPrice;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public void setTitle(String title) {
//            this.title = title;
//        }
//
//        public String getImag() {
//            return imag;
//        }
//
//        public void setImag(String imag) {
//            this.imag = imag;
//        }
//
//        public BigDecimal getPrice() {
//            return price;
//        }
//
//        public void setPrice(BigDecimal price) {
//            this.price = price;
//        }
//
//        public String getCurrentPrice() {
//            return currentPrice;
//        }
//
//        public void setCurrentPrice(String currentPrice) {
//            this.currentPrice = currentPrice;
//        }
//    }
//
//
//    /**
//     * 图片轮播适配器
//     */
//    public class NetworkImageHolderView implements Holder<String> {
//        private View rootview;
//        private SimpleDraweeView imageView;
//
//        @Override
//        public View createView(Context context) {
//            rootview = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.goods_item_head_img1, null);
//            imageView = (SimpleDraweeView) rootview.findViewById(R.id.sdv_item_head_img);
//            final ArrayList<String> arrayList = new ArrayList();
//            for (int i = 0; i < checkInchModel.getImage().length; i++) {
//                arrayList.add(checkInchModel.getImage()[i]);
//            }
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent it = new Intent(getActivity(), ActivityGoodsImages.class);
//                    it.putStringArrayListExtra("imgs", arrayList);
//                    startActivity(it);
//                }
//            });
//            return rootview;
//        }
//
//        @Override
//        public void UpdateUI(Context context, int position, final String data) {
////        if (!StringUtils.isEmpty( data )){
//            imageView.setImageURI(Uri.parse(data));
//
////        }
//
//        }
//
//    }
//
//    //添加购物车
//    BizDataAsyncTask<String> cartTask;
//
//    private void addCart(final String str) {
//        cartTask = new BizDataAsyncTask<String>() {
//            @Override
//            protected String doExecute() throws ZYException, BizFailure {
//                Log.e("asdasdasd", str);
//                return GoodsBiz.shoppingCart(str, "create");
//            }
//
//            @Override
//            protected void onExecuteSucceeded(String s) {
//                ToastUtil.show(getActivity(), "添加购物车成功！");
//            }
//
//            @Override
//            protected void OnExecuteFailed() {
//
//            }
//        };
//        cartTask.execute();
//    }
//
//
//    private class MyAdapter extends BaseQuickAdapter<MedicineDetailsEvaluateModel> {
//
//        public MyAdapter(List<MedicineDetailsEvaluateModel> list_eva) {
//            super(R.layout.item_goods_evaluate, list_eva);
//        }
//
//
//        @Override
//        protected void convert(BaseViewHolder baseViewHolder, MedicineDetailsEvaluateModel models) {
//            String str = models.getMOBILE();
//            baseViewHolder.setText(R.id.time, DateUtils.parseTime(models.getINS_DATE() + ""))
//                    .setText(R.id.name, str.substring(0, str.length() - (str.substring(3)).length()) + "****" + str.substring(7))
//                    .setText(R.id.context, models.getEVALUATE_CONTGENT());
//
//            SimpleDraweeView user_img = baseViewHolder.getView(R.id.user_img);
//            if (StringUtils.isEmpty(models.getUSER_ICON_FILE_ID())) {
//                user_img.setImageURI(Uri.parse(""));
//            } else {
//                user_img.setImageURI(Uri.parse(AppConfig.IMAGE_PATH + models.getUSER_ICON_FILE_ID()));
//            }
//
//
//            ArrayList arrayList = new ArrayList();
//            if (!StringUtils.isEmpty(models.getIMG_PATH1())) {
//                arrayList.add(AppConfig.IMAGE_PATH + models.getIMG_PATH1());
//            }
//            if (!StringUtils.isEmpty(models.getIMG_PATH2())) {
//                arrayList.add(AppConfig.IMAGE_PATH + models.getIMG_PATH2());
//            }
//            if (!StringUtils.isEmpty(models.getIMG_PATH3())) {
//                arrayList.add(AppConfig.IMAGE_PATH + models.getIMG_PATH3());
//            }
//            if (!StringUtils.isEmpty(models.getIMG_PATH4())) {
//                arrayList.add(AppConfig.IMAGE_PATH + models.getIMG_PATH4());
//            }
//            if (!StringUtils.isEmpty(models.getIMG_PATH5())) {
//                arrayList.add(AppConfig.IMAGE_PATH + models.getIMG_PATH5());
//            }
//            Log.e("size****", arrayList.size() + "");
//            final BGANinePhotoLayout mCurrentClickNpl = baseViewHolder.getView(R.id.item_moment_photos);
//            mCurrentClickNpl.setData(arrayList);
//            mCurrentClickNpl.setDelegate(new BGANinePhotoLayout.Delegate() {
//                @Override
//                public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
//                    if (mCurrentClickNpl.getItemCount() == 1) {
//                        // 预览单张图片
//                        startActivity(BGAPhotoPreviewActivity.newIntent(getActivity(), null, mCurrentClickNpl.getCurrentClickItem()));
//                    } else if (mCurrentClickNpl.getItemCount() > 1) {
//                        // 预览多张图片
//                        startActivity(BGAPhotoPreviewActivity.newIntent(getActivity(), null, mCurrentClickNpl.getData(), mCurrentClickNpl.getCurrentClickItemPosition()));
//                    }
//                }
//            });
//
//        }
//    }
}
