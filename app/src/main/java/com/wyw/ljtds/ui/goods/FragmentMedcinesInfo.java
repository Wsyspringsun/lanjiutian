package com.wyw.ljtds.ui.goods;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.gestures.GestureDetector;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gxz.PagerSlidingTabStrip;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.wyw.ljtds.MainActivity;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.DataListAdapter;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.model.AddressModel;
import com.wyw.ljtds.model.MedicineDetailsEvaluateModel;
import com.wyw.ljtds.model.MedicineDetailsModel;
import com.wyw.ljtds.model.MedicineShop;
import com.wyw.ljtds.model.MyLocation;
import com.wyw.ljtds.model.ShoppingCartAddModel;
import com.wyw.ljtds.model.SingleCurrentUser;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.ui.user.address.ActivityAddress;
import com.wyw.ljtds.ui.user.address.ActivityAddressEdit;
import com.wyw.ljtds.ui.user.address.AddressActivity;
import com.wyw.ljtds.utils.DateUtils;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.widget.MyCallback;
import com.wyw.ljtds.widget.NumberButton;
import com.wyw.ljtds.widget.RecycleViewDivider;
import com.wyw.ljtds.widget.dialog.BottomDialog;
import com.wyw.ljtds.widget.dialog.ShopListDialog;
import com.wyw.ljtds.widget.goodsinfo.SlideDetailsLayout;
import com.ysnows.page.Page;
import com.ysnows.page.PageBehavior;
import com.ysnows.page.PageContainer;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Inflater;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;

/**
 * Created by Administrator on 2017/3/12 0012.
 */

@ContentView(R.layout.fragment_medicine_info)
public class FragmentMedcinesInfo extends BaseFragment implements PageBehavior.OnPageChanged {

    private UserBiz bizUser;

    private static final int REQUEST_CHANGE_ADDR = 1;
    private static final int REQUEST_ADD_ADDRESS = 2;
    View.OnClickListener itemClickListener;
    //送至地址变更监听
    List<MyCallback> sendToAddrChangeListeners = new ArrayList<>();

    @ViewInject(R.id.fragment_goods_info_sumqty)
    private TextView tvSumqty;
    @ViewInject(R.id.fab_up_slide)
    private FloatingActionButton fab_up_slide;
    @ViewInject(R.id.fragment_medicine_info_container)
    private PageContainer pageContainer;
    @ViewInject(R.id.vp_item_goods_img)
    public ConvenientBanner cbGoodImages;
    @ViewInject(R.id.fragment_medicine_info_flag_otc)
    public ImageView imgChuFang;
    @ViewInject(R.id.fragment_goods_info_content)
    public TextView tvContent;
    @ViewInject(R.id.fragment_medicine_info_tv_busflg)
    public TextView tvBusFlg;
    @ViewInject(R.id.fragment_goods_info_heji)
    public TextView tvHeji;
    @ViewInject(R.id.tv_goods_detail)
    private TextView tv_goods_detail;
    @ViewInject(R.id.tv_goods_config)
    private TextView tv_goods_config;
    @ViewInject(R.id.v_tab_cursor)
    private View v_tab_cursor;
    //    @ViewInject(R.id.ll_recommend)
//    public LinearLayout ll_recommend;
    @ViewInject(R.id.fragment_medicine_info_number_button)
    public NumberButton numberButton;
    @ViewInject(R.id.eva_list)
    private RecyclerView evaList;
    @ViewInject(R.id.fragment_goods_info_loadevalist)
    public TextView tvLoadEva;
    @ViewInject(R.id.fragment_medicine_info_tv_address)
    public TextView tvAddress;
    @ViewInject(R.id.fragment_goods_info_changeaddr)
    public ImageView imgMoreAddress;
    @ViewInject(R.id.fragment_medicine_info_tv_shopaddress)
    public TextView tvShopAddress;
    @ViewInject(R.id.fragment_medicine_info_tv_shopextra)
    public TextView tvShopExtra;

    public FragmentGoodsParameter fragmentGoodsParameter;
    public FragmentGoodsDetails fragmentGoodsDetails;
    private Fragment nowFragment;//当前是哪个fragment
    private int nowIndex;//fragment判定下标
    private float fromX;
    private List<TextView> tabTextList;
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private MedicineDetailsModel medicineModel;
    private boolean isTop = true;

    public static FragmentMedcinesInfo getInstance(View.OnClickListener itemClickListener) {
        FragmentMedcinesInfo instance = new FragmentMedcinesInfo();
        instance.itemClickListener = itemClickListener;
        return instance;
    }

    @Event(value = {R.id.fragment_goods_info_loadevalist, R.id.fragment_medicine_info_changeshop, R.id.fab_up_slide, R.id.ll_comment, R.id.ll_pull_up, R.id.ll_goods_detail, R.id.ll_goods_config})
    private void onClick(View v) {
        Intent it;
        switch (v.getId()) {
            case R.id.ll_pull_up:
                //上拉查看图文详情
                pageContainer.scrollToBottom();
                break;

            case R.id.fab_up_slide:
                //点击滑动到顶部
                pageContainer.backToTop();
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

            default:
                Log.e(AppConfig.ERR_TAG, "Click default");
                if (itemClickListener != null) {
                    Log.e(AppConfig.ERR_TAG, "itemClickListener Click default v:" + v.getId());
                    itemClickListener.onClick(v);
                }
                break;
        }
    }

    /**
     * 添加送至地址更改监听器
     *
     * @param callback
     */
    public void addSendToAddrChangeListeners(MyCallback callback) {
        sendToAddrChangeListeners.add(callback);
    }

    // 开始自动翻页
    @Override
    public void onResume() {
        super.onResume();

        Log.e(AppConfig.ERR_TAG, "FragmentMedcinesInfo onResume this.medicineModel:" + this.medicineModel);
        if (this.medicineModel != null) {
            bindData2View(this.medicineModel);
        }
        //开始自动翻页
        cbGoodImages.startTurning(3000);
    }

    // 停止自动翻页
    @Override
    public void onPause() {
        super.onPause();
        //停止翻页
        cbGoodImages.stopTurning();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        bizUser = UserBiz.getInstance(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        //商品详情页面
        fragmentGoodsDetails = new FragmentGoodsDetails();
        fragmentList.add(fragmentGoodsDetails);
        //规格参数页面
        fragmentGoodsParameter = new FragmentGoodsParameter();
        fragmentList.add(fragmentGoodsParameter);

        nowFragment = fragmentGoodsDetails;
        fragmentManager = getChildFragmentManager();
        //默认显示商品详情tab
        fragmentManager.beginTransaction().replace(R.id.fl_content, nowFragment).commitAllowingStateLoss();
        //pagecoontainer listen toTop or toBottom
        pageContainer.setOnPageChanged(new PageBehavior.OnPageChanged() {

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

        //ScrollView和edittext焦点冲突问题
        /*        todo back
        sv_goods_info.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        sv_goods_info.setFocusable(true);
        sv_goods_info.setFocusableInTouchMode(true);*/
        //设置文字中间一条横线
//        tv_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        //设置默认未选择商品规格
//        tv_current_goods.setText(R.string.qingxuanze);

        //浮标隐藏
        fab_up_slide.hide();

        //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
        cbGoodImages.setPageIndicator(new int[]{R.mipmap.index_white, R.mipmap.index_red});
        cbGoodImages.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);

        tabTextList = new ArrayList<>();
        tabTextList.add(tv_goods_detail);
        tabTextList.add(tv_goods_config);

        numberButton.setBuyMax(999).
                setInventory(999).
                setCurrentNumber(1).setOnNumberListener(new NumberButton.OnNumberListener() {
            @Override
            public void OnNumberChange(int num) {
//                        BigDecimal b1=new BigDecimal( "11.11" );
                setHejiVal(num);
            }
        });
        //评论列表
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());//必须有
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置方向滑动
        evaList.setLayoutManager(linearLayoutManager);
        evaList.setItemAnimator(new DefaultItemAnimator());
        evaList.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, 10, getResources().getColor(R.color.font_black2)));

        imgMoreAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = AddressActivity.getIntent(getActivity(), true);
                startActivityForResult(it, REQUEST_CHANGE_ADDR);
            }
        });


        //初始化送至
        if (SingleCurrentUser.location != null) {
            String addrText = SingleCurrentUser.location.getAddrStr();
            tvAddress.setText("送至:" + addrText);
        }
        return v;
    }

    private void setHejiVal(int num) {
        if (medicineModel == null) return;
        BigDecimal b2 = new BigDecimal(medicineModel.getSALEPRICE());
        tvHeji.setText("合计: ￥" + new BigDecimal(num).multiply(b2) + "");
    }

    /**
     * 给商品轮播图设置图片路径
     */
    public void setLoopView(String[] images) {
        //初始化商品图片轮播
        cbGoodImages.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new Holder<String>() {
                    private ImageView iv;

                    @Override
                    public View createView(Context context) {
                        iv = new ImageView(context);
                        iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        return iv;
                    }

                    @Override
                    public void UpdateUI(Context context, int position, String data) {
                        String imgSrc = data;
                        if (!StringUtils.isEmpty(imgSrc)) {
                            Picasso.with(context).load(Uri.parse(data)).fit().memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(iv);
                        }
                    }
                };
            }
        }, Arrays.asList(images));
    }


    /**
     * 滑动游标
     */
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

    /**
     * 切换Fragment
     * <p>(hide、show、add)
     *
     * @param fromFragment
     * @param toFragment
     */
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


    //添加收藏
    BizDataAsyncTask<Boolean> addTask;


    public void bindData2View(MedicineDetailsModel model) {
        if (!isAdded()) return;
        if (model == null) return;
        this.medicineModel = model;
        if (model.getSUMQTY() != null) {
            tvSumqty.setText("(本店数量：" + model.getSUMQTY() + ")");
        }
//        tvAddress.setText("送至:" + model.getUSER_ADDRESS());


        fragmentGoodsDetails.bindData2View(model.getHTML_PATH());
        fragmentGoodsParameter.bindData2View(model);

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
//        Log.e(AppConfig.ERR_TAG, "treatment:" + treatment);
        StringBuilder sb = new StringBuilder().append(detailFlg).append(brand).append(wareName).append(size).append(postage).append(price).append(prodAdd).append(treatment);
        if (MedicineDetailsModel.PRESCRIPTION_FLG_RX.equals(medicineModel.getPRESCRIPTION_FLG())) {
            imgChuFang.setImageDrawable(ActivityCompat.getDrawable(getActivity(), R.mipmap.chufang));
            sb.append("\n" + getString(R.string.warning_chufang));
        } else {
            imgChuFang.setImageDrawable(ActivityCompat.getDrawable(getActivity(), R.mipmap.feichufang));
        }

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
        /*if (model.getUSER_ADDRESS() != null) {
            //default address
//            AddressModel addr = model.getUSER_ADDRESS();
//            addrText = addr.getCONSIGNEE_ADDRESS();

            addrText = model.getUSER_ADDRESS();

        } else {
            //default location
            addrText = SingleCurrentUser.location.getAddrStr();
        }
*/

        //default shop address
        String shopname = model.getLOGISTICS_COMPANY(),
                distanceText = model.getDISTANCE_TEXT(),
                durationText = model.getDURATION_TEXT(),
                qisong = "" + model.getQISONG() + "元起送",
                baoyou = model.getBAOYOU() + "元包邮",
                busFlg = model.getBUSAVLID_FLGText();
        tvBusFlg.setText(busFlg);

        StringBuilder sbShop = new StringBuilder().append(shopname).append(" " + postage);
        SpannableString sbsShop = new SpannableString(sbShop.toString());
        /*int busFlgStart = sbShop.toString().indexOf(busFlg);
        int busFlgEnd = busFlgStart + busFlg.length();
        if (busFlgStart >= 0) {
            sbsShop.setSpan(new BackgroundColorSpan(Color.RED), busFlgStart, busFlgEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            sbsShop.setSpan(new ForegroundColorSpan(Color.WHITE), busFlgStart, busFlgEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            sbsShop.setSpan(new RelativeSizeSpan(0.8f), busFlgStart, busFlgEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }*/
        tvShopAddress.setText(sbsShop);

        StringBuilder sbShopExtra = new StringBuilder();
        sbShopExtra.append(distanceText).append("|" + durationText).append("   ").append(qisong).append("|" + baoyou);
        tvShopExtra.setText(sbShopExtra.toString());

        //门店信息
        //评论
        setEvaluateData();

    }

    /**
     * * 评价信息
     */
    private void setEvaluateData() {
        if (medicineModel == null) return;
        if (medicineModel.getEVALUATE() == null || medicineModel.getEVALUATE().size() <= 0) {
            tvLoadEva.setVisibility(View.GONE);
        } else {
            tvLoadEva.setVisibility(View.VISIBLE);
        }
        List<MedicineDetailsEvaluateModel> lsEva = medicineModel.getEVALUATE();
        if (lsEva != null && lsEva.size() > 0) {
            List<MedicineDetailsEvaluateModel> evas = new ArrayList<>();
            evas.add(lsEva.get(0));
            MyAdapter adapter = new MyAdapter(evas);
            evaList.setAdapter(adapter);
        }
    }

    @Override
    public void toTop() {
        isTop = true;
        Toast.makeText(getContext(), "Top", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toBottom() {
        isTop = false;
        Toast.makeText(getContext(), "Bottom", Toast.LENGTH_SHORT).show();
    }


    //推荐商品
    public static class RecommendGoodsBean {
        private String title;
        private String imag;
        private BigDecimal price;
        private String currentPrice;

        public RecommendGoodsBean() {
        }

        public RecommendGoodsBean(String title, String imag, BigDecimal price, String currentPrice) {
            this.title = title;
            this.imag = imag;
            this.price = price;
            this.currentPrice = currentPrice;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImag() {
            return imag;
        }

        public void setImag(String imag) {
            this.imag = imag;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public String getCurrentPrice() {
            return currentPrice;
        }

        public void setCurrentPrice(String currentPrice) {
            this.currentPrice = currentPrice;
        }
    }

    private class MyAdapter extends BaseQuickAdapter<MedicineDetailsEvaluateModel> {

        public MyAdapter(List<MedicineDetailsEvaluateModel> list_eva) {
            super(R.layout.item_goods_evaluate, list_eva);
        }


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


            /*ArrayList arrayList = new ArrayList();
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
                arrayList.add(AppConfig.IMAGE_PATH_LJT + models.getIMG_PATH4());
            }
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
            });*/

        }
    }

    public View.OnClickListener getItemClickListener() {
        return itemClickListener;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case REQUEST_CHANGE_ADDR:
                String cmd = data.getStringExtra(AddressActivity.TAG_CMD);
                if (AddressActivity.CMD_CREATE.equals(cmd)) {
                    startActivityForResult(ActivityAddressEdit.getIntent(getActivity(), null), REQUEST_ADD_ADDRESS);
                } else {
                    Parcelable paddr = data.getParcelableExtra(ActivityAddress.TAG_SELECTED_ADDRESS);
                    if (paddr != null) {
                        AddressModel addr = (AddressModel) paddr;
                        StringBuilder err = new StringBuilder();
                        MyLocation addrLoc = AddressModel.parseLocation(err, addr.getADDRESS_LOCATION());
                        String addrText = addrLoc.getAddrStr();
                        tvAddress.setText("送至:" + addrText);

                        for (MyCallback cb : sendToAddrChangeListeners) {
                            cb.callback(addr.getADDRESS_ID(), "" + addrLoc.getLatitude(), "" + addrLoc.getLongitude());
                        }
                    }
                }

                break;
            case REQUEST_ADD_ADDRESS:
                new BizDataAsyncTask<List<AddressModel>>() {
                    @Override
                    protected List<AddressModel> doExecute() throws ZYException, BizFailure {
                        return bizUser.selectUserAddress();
                    }

                    @Override
                    protected void onExecuteSucceeded(List<AddressModel> addressModels) {
                        closeLoding();
                        if (addressModels == null) return;
                        if (addressModels.size() <= 0) return;
                        AddressModel addr = addressModels.get(0);
                        int addrId = addr.getADDRESS_ID();

                        StringBuilder err = new StringBuilder();
                        MyLocation addrLoc = AddressModel.parseLocation(err, addr.getADDRESS_LOCATION());
                        String addrText = addrLoc.getAddrStr();
                        tvAddress.setText("送至:" + addrText);

                        for (MyCallback cb : sendToAddrChangeListeners) {
                            cb.callback(addr.getADDRESS_ID(), "" + addrLoc.getLatitude(), "" + addrLoc.getLongitude());
                        }
                    }

                    @Override
                    protected void OnExecuteFailed() {
                        closeLoding();
                    }
                }.execute();
                break;
            default:
                break;
        }
    }

}
