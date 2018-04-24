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
import com.squareup.picasso.Picasso;
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
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.MyCallback;
import com.wyw.ljtds.widget.RecycleViewDivider;
import com.wyw.ljtds.widget.commodity.CheckInchModel;
import com.wyw.ljtds.widget.commodity.CheckInchPopWindow;
import com.wyw.ljtds.widget.dialog.LifeGoodsSelectDialog;
import com.wyw.ljtds.widget.goodsinfo.SlideDetailsLayout;
import com.ysnows.page.PageBehavior;
import com.ysnows.page.PageContainer;

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

import static com.squareup.picasso.MemoryPolicy.NO_CACHE;
import static com.squareup.picasso.MemoryPolicy.NO_STORE;

/**
 * Created by Administrator on 2017/3/12 0012.
 */

@ContentView(R.layout.fragment_goods_info)
public class FragmentGoodsInfo extends BaseFragment implements SlideDetailsLayout.OnSlideDetailsListener {
    @ViewInject(R.id.fragment_goods_info_container)
    private PageContainer container;

    @ViewInject(R.id.fab_up_slide)
    private FloatingActionButton fab_up_slide;
    @ViewInject(R.id.vp_item_goods_img)
    public ConvenientBanner vp_item_goods_img;
    //    @ViewInject(R.id.vp_recommend)
//    public ConvenientBanner vp_recommend;
    @ViewInject(R.id.ll_goods_detail)
    private LinearLayout ll_goods_detail;
    @ViewInject(R.id.ll_goods_config)
    private LinearLayout ll_goods_config;
    @ViewInject(R.id.tv_goods_detail)
    private TextView tv_goods_detail;
    @ViewInject(R.id.tv_goods_config)
    private TextView tv_goods_config;
    @ViewInject(R.id.v_tab_cursor)
    private View v_tab_cursor;
    //    @ViewInject(R.id.flag_otc)
//    private ImageView flag_otc;
    @ViewInject(R.id.goods_changjia)
    private TextView goods_changjia;
    @ViewInject(R.id.ll_comment)
    public LinearLayout ll_comment;
    @ViewInject(R.id.ll_pull_up)
    public LinearLayout ll_pull_up;
    @ViewInject(R.id.tv_goods_title)
    public TextView tv_goods_title;
    @ViewInject(R.id.shoucang_img)
    private ImageView shoucang_img;
    @ViewInject(R.id.eva_list)
    private RecyclerView eva_list;
    @ViewInject(R.id.tv_current_goods)
    public TextView tv_current_goods;
    @ViewInject(R.id.tv_new_price)
    private TextView tv_new_price;
    @ViewInject(R.id.tv_old_price)
    private TextView tv_old_price;

    public LifeGoodsSelectDialog selDialog;
    public FragmentGoodsParameter fragmentGoodsParameter;
    public FragmentGoodsDetails fragmentGoodsDetails;
    private Fragment nowFragment;//当前是哪个fragment
    private int nowIndex;//fragment判定下标
    private float fromX;
    private List<TextView> tabTextList;
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private ActivityGoodsInfo activity;//父级activity
    private boolean isCollect = false;//是否点击收藏
    private CommodityDetailsModel model;
    public CheckInchModel checkInchModel;

    public CommodityDetailsModel.ColorList seledColor;
    public CommodityDetailsModel.SizeList seledSize;
    public int seledNum;


    ////    private List<MedicineDetailsModel.EVALUATEs> list_eva;
////    private MyAdapter adapter;
//
//
    @Event(value = {R.id.fab_up_slide, R.id.ll_comment, R.id.ll_pull_up, R.id.ll_goods_detail, R.id.ll_goods_config, R.id.shoucang,
            R.id.vp_item_goods_img, R.id.tv_current_goods})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_pull_up:
                //上拉查看图文详情
                container.scrollToBottom();
                break;

            case R.id.fab_up_slide:
                //点击滑动到顶部
                container.backToTop();
                break;

            case R.id.ll_goods_detail:
                //商品详情tab
                nowIndex = 0;
                scrollCursor();
                switchFragment(nowFragment, fragmentGoodsDetails);
                nowFragment = fragmentGoodsDetails;
                break;

            case R.id.ll_goods_config:
                //规格参数tab
                nowIndex = 1;
                scrollCursor();
                switchFragment(nowFragment, fragmentGoodsParameter);
                nowFragment = fragmentGoodsParameter;
                break;
            case R.id.tv_current_goods:
                //选择规格
                showSelDialog(new MyCallback() {
                    @Override
                    public void callback(Object... params) {
                        tv_current_goods.setText(selDialog.tvSeled.getText().toString());
                        if (params == null || params.length <= 0) return;
                        seledColor = (CommodityDetailsModel.ColorList) params[0];
                        seledSize = (CommodityDetailsModel.SizeList) params[1];
                        seledNum = (int) params[2];
                        selDialog.dismiss();
                    }
                });

                break;

            case R.id.vp_item_goods_img:
                break;

            default:
                break;
        }
    }

    public void showSelDialog(MyCallback callback) {
        if (selDialog == null) {
            selDialog = new LifeGoodsSelectDialog(getActivity(), R.style.AllScreenWidth_Dialog);
            selDialog.bindData2View(model);
            selDialog.setCallback(callback);

        }
        selDialog.show();
    }

    //
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (ActivityGoodsInfo) context;
    }

    //
//    // 开始自动翻页
    @Override
    public void onResume() {
        super.onResume();
        //开始自动翻页
        vp_item_goods_img.startTurning(3000);
    }

    //
//    // 停止自动翻页
    @Override
    public void onPause() {
        super.onPause();
        //停止翻页
        vp_item_goods_img.stopTurning();
    }

    //
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        flag_otc.setVisibility(View.GONE);

        fragmentGoodsDetails = new FragmentGoodsDetails();
        fragmentGoodsParameter = new FragmentGoodsParameter();
        fragmentList.add(fragmentGoodsParameter);
        fragmentList.add(fragmentGoodsDetails);

        nowFragment = fragmentGoodsDetails;
        fragmentManager = getChildFragmentManager();
        //默认显示商品详情tab
        fragmentManager.beginTransaction().replace(R.id.fl_content, nowFragment).commitAllowingStateLoss();

        //ScrollView和edittext焦点冲突问题

        //设置文字中间一条横线
//        tv_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//        tv_old_price.getPaint().setAntiAlias(true);//抗锯齿

        //设置默认未选择商品规格
        tv_current_goods.setText(R.string.qingxuanze);

        //浮标隐藏
        fab_up_slide.hide();
        container.setOnPageChanged(new PageBehavior.OnPageChanged() {

            @Override
            public void toTop() {
                //位于第一页
                fab_up_slide.hide();
            }

            @Override
            public void toBottom() {
                //位于第二页
                fab_up_slide.show();
            }
        });


        //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
        vp_item_goods_img.setPageIndicator(new int[]{R.mipmap.index_white, R.mipmap.index_red});
        vp_item_goods_img.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);

        fragmentList = new ArrayList<>();
        tabTextList = new ArrayList<>();
        tabTextList.add(tv_goods_detail);
        tabTextList.add(tv_goods_config);

        //添加删除线
//        tv_new_price.getPaint().setStrokeWidth(3.0f);

    }

    //
//    /**
//     * 给商品轮播图设置图片路径
//     */
    public void setLoopView(String[] images) {
        List<String> imgUrls = new ArrayList<>();
        if (images.length == 0 || images == null) {
            imgUrls.add("");
            imgUrls.add("");
            imgUrls.add("");
            imgUrls.add("");
            imgUrls.add("");
        } else {
            for (int i = 0; i < images.length; i++) {
                imgUrls.add(images[i]);
            }
        }


        //初始化商品图片轮播
        vp_item_goods_img.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new NetworkImageHolderView();
            }
        }, imgUrls);
    }

/*    public void bindData2View(CommodityDetailsModel model) {
        if (model == null) return;
        this.medicineModel = model;

        //轮播图
        if (model.getIMAGES() != null) {
            setLoopView(model.getIMAGES());
        }
        //内容
        String wareName = StringUtils.deletaFirst(model.getWARENAME()),
                detailFlg = model.getCOMMODITY_PARAMETER() == null ? "" : model.getCOMMODITY_PARAMETER(),
                size = model.getWARESPEC(),
                brand = model.getCOMMODITY_BRAND() + "  ",
                price = "\n￥" + model.getSALEPRICE(),
                treatment = model.getTREATMENT() == null ? "\n\n" : "\n\n" + StringUtils.sub(model.getTREATMENT(), 0, 50),
                postage = model.getPOSTAGE(),
                prodAdd = "\n\n生产企业: " + model.getPRODUCER();
//                detailFlg = model.getFLG_DETAIL()"[买而送一]",
        StringBuilder sb = new StringBuilder().append(detailFlg).append(brand).append(wareName).append(size).append(postage).append(price).append(prodAdd).append(treatment);
        int priceStart = sb.indexOf(price), priceEnd = priceStart + price.length();
        int prodaddStart = sb.indexOf(prodAdd), prodaddEnd = prodaddStart + prodAdd.length();
        int treatmentStart = sb.indexOf(treatment), treatmentEnd = treatmentStart + treatment.length();
        SpannableString sbs = new SpannableString(sb.toString());
        sbs.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(getActivity(), R.color.base_red)), priceStart, priceEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sbs.setSpan(new RelativeSizeSpan(1.2f), priceStart, priceEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sbs.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(getActivity(), R.color.font_3)), prodaddStart, prodaddEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sbs.setSpan(new RelativeSizeSpan(0.8f), prodaddStart, prodaddEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sbs.setSpan(new RelativeSizeSpan(0.8f), treatmentStart, treatmentEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvContent.setText(sbs);
        //合计
        setHejiVal(numberButton.getNumber());
        //default address or location
        *//*if (model.getUSER_ADDRESS() != null) {
            //default address
//            AddressModel addr = model.getUSER_ADDRESS();
//            addrText = addr.getCONSIGNEE_ADDRESS();

            addrText = model.getUSER_ADDRESS();

        } else {
            //default location
            addrText = SingleCurrentUser.location.getAddrStr();
        }
*//*

        //default shop address
        String shopname = model.getLOGISTICS_COMPANY(),
                distanceText = model.getDISTANCE_TEXT(),
                durationText = model.getDURATION_TEXT(),
                qisong = "￥" + model.getQISONG() + "起送",
                baoyou = model.getBAOYOU() + "包邮",
                busFlg = model.getBUSAVLID_FLGText();
        tvBusFlg.setText(busFlg);

        StringBuilder sbShop = new StringBuilder().append(shopname).append(" " + postage);
        SpannableString sbsShop = new SpannableString(sbShop.toString());
        *//*int busFlgStart = sbShop.toString().indexOf(busFlg);
        int busFlgEnd = busFlgStart + busFlg.length();
        if (busFlgStart >= 0) {
            sbsShop.setSpan(new BackgroundColorSpan(Color.RED), busFlgStart, busFlgEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            sbsShop.setSpan(new ForegroundColorSpan(Color.WHITE), busFlgStart, busFlgEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            sbsShop.setSpan(new RelativeSizeSpan(0.8f), busFlgStart, busFlgEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }*//*
        tvShopAddress.setText(sbsShop);

        StringBuilder sbShopExtra = new StringBuilder();
        sbShopExtra.append(distanceText).append("|" + durationText).append("   ").append(qisong).append("|" + baoyou);
        tvShopExtra.setText(sbShopExtra.toString());

        //门店信息
        //评论
        setEvaluateData();

    }*/

    public void updeta(CommodityDetailsModel commodityDetailsModel) {
        model = commodityDetailsModel;

        CommodityDetailsModel.SizeList firstDetail = model.getColorList().get(0).getSizeList().get(0);
        tv_goods_title.setText(model.getTitle());
        if (CommodityDetailsModel.CUXIAOFLG_YES.equals(model.getCuxiaoFlg())) {
            tv_new_price.setText(getString(R.string.money_renminbi, Utils.formatFee(model.getPromprice())));
            tv_old_price.setText(getString(R.string.money_renminbi, Utils.formatFee("" + model.getYuanJia())));
            tv_old_price.setVisibility(View.VISIBLE);
        } else {
            tv_new_price.setText(getString(R.string.money_renminbi, Utils.formatFee("" + model.getCostMoney())));
            tv_old_price.setVisibility(View.GONE);
        }
//        tv_old_price.setText(model.getColorList().get(0).getSizeList().get(0).getMarketPrice() + "");

        List<String> imgList = new ArrayList<String>();
        CommodityDetailsModel.SizeList size = model.getColorList().get(0).getSizeList().get(0);
        if (!StringUtils.isEmpty(size.getImgPath())) {
            imgList.add(AppConfig.IMAGE_PATH_LJT + size.getImgPath());
        }
        if (!StringUtils.isEmpty(size.getImgPath2())) {
            imgList.add(AppConfig.IMAGE_PATH_LJT + size.getImgPath2());
        }
        if (!StringUtils.isEmpty(size.getImgPath3())) {
            imgList.add(AppConfig.IMAGE_PATH_LJT + size.getImgPath3());
        }
        if (!StringUtils.isEmpty(size.getImgPath4())) {
            imgList.add(AppConfig.IMAGE_PATH_LJT + size.getImgPath4());
        }
        if (!StringUtils.isEmpty(size.getImgPath5())) {
            imgList.add(AppConfig.IMAGE_PATH_LJT + size.getImgPath5());
        }
        if (!StringUtils.isEmpty(size.getImgPath6())) {
            imgList.add(AppConfig.IMAGE_PATH_LJT + size.getImgPath6());
        }
        String[] images = new String[imgList.size()];
        imgList.toArray(images);
        //设置默认值
        checkInchModel = new CheckInchModel();
        checkInchModel.setNum(1);
        checkInchModel.setCololr(model.getColorList().get(0).getColorName());
        checkInchModel.setSize(model.getColorList().get(0).getSizeList().get(0).getCommoditySize());
        checkInchModel.setColor_id(model.getColorList().get(0).getCommodityColorId());
        checkInchModel.setSize_id(model.getColorList().get(0).getSizeList().get(0).getCommoditySizeId());
        checkInchModel.setImage(images);
        checkInchModel.setNew_money(model.getColorList().get(0).getSizeList().get(0).getCostMoney());

        //轮播
        setLoopView(images);

        //评论数量
        Log.e(AppConfig.ERR_TAG, "eva count:" + model.getEVALUATE_CNT() + "");
//        tvGoodInfoComment.setText(model.getEVALUATE_CNT() + "");
        //评价信息
        if (!model.getEvaluateList().isEmpty() && model.getEvaluateList() != null) {
//            no_eva.setVisibility(View.GONE);
            eva_list.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());//必须有
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置方向滑动
            eva_list.setLayoutManager(linearLayoutManager);
            eva_list.setItemAnimator(new DefaultItemAnimator());
            eva_list.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, 10, getResources().getColor(R.color.font_black2)));

            eva_list.setAdapter(new MyAdapter(model.getEvaluateList()));
        } else {
            eva_list.setVisibility(View.GONE);
        }


        //是否收藏
        if (model.getFavorited().equals("1")) {
            shoucang_img.setImageDrawable(getResources().getDrawable(R.mipmap.icon_shoucang_xuanzhong));
        } else {
            shoucang_img.setImageDrawable(getResources().getDrawable(R.mipmap.icon_shoucang_weixuan));
        }

        fragmentGoodsDetails.bindData2View(model.getHtmlPath());
        fragmentGoodsParameter.bindData2View(model);
    }

    //

    @Override
    public void onStatucChanged(SlideDetailsLayout.Status status) {
        if (status == SlideDetailsLayout.Status.OPEN) {
            //当前为图文详情页
            fab_up_slide.show();
        } else {
            //当前为商品详情页
            fab_up_slide.hide();
        }
    }

    //
//    /**
//     * 滑动游标
//     */
    private void scrollCursor() {
        TranslateAnimation anim = new TranslateAnimation(fromX, nowIndex * v_tab_cursor.getWidth(), 0, 0);
        anim.setFillAfter(true);//设置动画结束时停在动画结束的位置
        anim.setDuration(50);
        //保存动画结束时游标的位置,作为下次滑动的起点
        fromX = nowIndex * v_tab_cursor.getWidth();
        v_tab_cursor.startAnimation(anim);

        //设置Tab切换颜色
        for (int i = 0; i < tabTextList.size(); i++) {
            tabTextList.get(i).setTextColor(i == nowIndex ? getResources().getColor(R.color.base_bar) : getResources().getColor(R.color.font_black2));
        }
    }

    //
//    /**
//     * 切换Fragment
//     * <p>(hide、show、add)
//     *
//     * @param fromFragment
//     * @param toFragment
//     */
    private void switchFragment(Fragment fromFragment, Fragment toFragment) {
        if (nowFragment != toFragment) {
            fragmentTransaction = fragmentManager.beginTransaction();
            if (!toFragment.isAdded()) {    // 先判断是否被add过
                fragmentTransaction.hide(fromFragment).add(R.id.fl_content, toFragment).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到activity中
            } else {
                fragmentTransaction.hide(fromFragment).show(toFragment).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    //    /**
//     * 图片轮播适配器
//     */
    public class NetworkImageHolderView implements Holder<String> {
        private View rootview;
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            rootview = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.goods_item_head_img1, null);
            imageView = (ImageView) rootview.findViewById(R.id.sdv_item_head_img);
            /*final ArrayList<String> arrayList = new ArrayList();
            for (int i = 0; i < checkInchModel.getImage().length; i++) {
                arrayList.add(checkInchModel.getImage()[i]);
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it = new Intent(getActivity(), ActivityGoodsImages.class);
                    it.putStringArrayListExtra("imgs", arrayList);
                    startActivity(it);
                }
            });*/
            return rootview;
        }

        @Override
        public void UpdateUI(Context context, int position, final String data) {
//        if (!StringUtils.isEmpty( data )){
//            imageView.setImageURI(Uri.parse(data));
            Log.e(AppConfig.ERR_TAG, "lifegoods data:" + data);
            Picasso.with(context).load(Uri.parse(data)).memoryPolicy(NO_CACHE, NO_STORE).into(imageView);
//        }

        }

    }

    //
//    //添加购物车
//    BizDataAsyncTask<String> cartTask;
//
    private void addCart(final String str) {
        new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                Log.e("asdasdasd", str);
                return GoodsBiz.shoppingCart(str, "create");
            }

            @Override
            protected void onExecuteSucceeded(String s) {
                ToastUtil.show(getActivity(), "添加购物车成功！");
            }

            @Override
            protected void OnExecuteFailed() {

            }
        }.execute();
    }

    //
//
    private class MyAdapter extends BaseQuickAdapter<MedicineDetailsEvaluateModel> {
        //
        public MyAdapter(List<MedicineDetailsEvaluateModel> list_eva) {
            super(R.layout.item_goods_evaluate, list_eva);
        }

        //
//
        @Override
        protected void convert(BaseViewHolder baseViewHolder, MedicineDetailsEvaluateModel models) {
            String str = models.getMOBILE();
            baseViewHolder.setText(R.id.time, DateUtils.parseTime(models.getINS_DATE() + ""))
                    .setText(R.id.name, str.substring(0, str.length() - (str.substring(3)).length()) + "****" + str.substring(7))
                    .setText(R.id.context, models.getEVALUATE_CONTGENT());

            SimpleDraweeView user_img = baseViewHolder.getView(R.id.user_img);
            if (StringUtils.isEmpty(models.getUSER_ICON_FILE_ID())) {
                user_img.setImageURI(Uri.parse(""));
            } else {
                user_img.setImageURI(Uri.parse(AppConfig.IMAGE_PATH_LJT + models.getUSER_ICON_FILE_ID()));
            }


            ArrayList arrayList = new ArrayList();
            if (!StringUtils.isEmpty(models.getIMG_PATH1())) {
                arrayList.add(AppConfig.IMAGE_PATH_LJT + models.getIMG_PATH1());
            }
            if (!StringUtils.isEmpty(models.getIMG_PATH2())) {
                arrayList.add(AppConfig.IMAGE_PATH_LJT + models.getIMG_PATH2());
            }
            if (!StringUtils.isEmpty(models.getIMG_PATH3())) {
                arrayList.add(AppConfig.IMAGE_PATH_LJT + models.getIMG_PATH3());
            }
            if (!StringUtils.isEmpty(models.getIMG_PATH4())) {
                arrayList.add(AppConfig.IMAGE_PATH_LJT + models.getIMG_PATH4());
            }
            if (!StringUtils.isEmpty(models.getIMG_PATH5())) {
                arrayList.add(AppConfig.IMAGE_PATH_LJT + models.getIMG_PATH5());
            }
            Log.e("size****", arrayList.size() + "");
            final BGANinePhotoLayout mCurrentClickNpl = baseViewHolder.getView(R.id.item_moment_photos);
            mCurrentClickNpl.setData(arrayList);
            mCurrentClickNpl.setDelegate(new BGANinePhotoLayout.Delegate() {
                @Override
                public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
                    if (mCurrentClickNpl.getItemCount() == 1) {
                        // 预览单张图片
                        startActivity(BGAPhotoPreviewActivity.newIntent(getActivity(), null, mCurrentClickNpl.getCurrentClickItem()));
                    } else if (mCurrentClickNpl.getItemCount() > 1) {
                        // 预览多张图片
                        startActivity(BGAPhotoPreviewActivity.newIntent(getActivity(), null, mCurrentClickNpl.getData(), mCurrentClickNpl.getCurrentClickItemPosition()));
                    }
                }
            });

        }
    }
}
