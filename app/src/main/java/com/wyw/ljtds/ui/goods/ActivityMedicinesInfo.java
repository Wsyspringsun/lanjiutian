package com.wyw.ljtds.ui.goods;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gxz.PagerSlidingTabStrip;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.weixin.uikit.MMAlert;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.goodsinfo.ItemTitlePagerAdapter;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.model.GoodSubmitModel1;
import com.wyw.ljtds.model.GoodSubmitModel2;
import com.wyw.ljtds.model.GoodSubmitModel3;
import com.wyw.ljtds.model.MedicineDetailsModel;
import com.wyw.ljtds.model.OrderCommDto;
import com.wyw.ljtds.model.OrderGroupDto;
import com.wyw.ljtds.model.OrderTradeDto;
import com.wyw.ljtds.model.ShoppingCartAddModel;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.cart.CartActivity;
import com.wyw.ljtds.ui.user.manage.ActivityManage;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.goodsinfo.NoScrollViewPager;

import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

import cn.xiaoneng.coreapi.TrailActionBody;
import cn.xiaoneng.uiapi.Ntalker;
import cn.xiaoneng.utils.MyUtil;

/**
 * Created by Administrator on 2016/12/26 0026.
 */
@ContentView(R.layout.activity_goods_info)
public class ActivityMedicinesInfo extends BaseActivity {
    /**
     * 微信分享使用
     **/
    private IWXAPI wxApi;
    private static final int MMAlertSelect1 = 0; //选择分享的渠道
    private static final int MMAlertSelect2 = 1;
    private static final int MMAlertSelect3 = 2;

    public static String VAL_INFO_SOURCE = "ActivityMedicinesInfo";
    @ViewInject(R.id.vp_content)
    public NoScrollViewPager nsvpContent;
    @ViewInject(R.id.tv_title)
    public TextView tv_title;
    @ViewInject(R.id.activity_goods_info_tv_zixungoumai)
    private TextView tvZixunGoumai;
    @ViewInject(R.id.activity_goods_info_tv_addcart)
    private TextView tvAddcart;
    @ViewInject(R.id.activity_goods_info_tv_goumai)
    private TextView tvGoumai;
    @ViewInject(R.id.activity_goods_info_tv_shop)
    private TextView tvShop;
    @ViewInject(R.id.activity_goods_info_tv_kefu)
    private TextView tvKefu;
    @ViewInject(R.id.activity_goods_info_tv_shoucang)
    private TextView tvShoucang;


    private static final int THUMB_SIZE = 150;

    //客服
    String groupName = "蓝九天医师";// 客服组默认名称
    String settingid2 = "lj_1000_1495596285901";
    TrailActionBody trailparams = null;// 轨迹信息实例
    // 轨迹（专有参数）
    String sellerid = "lj_1001";// 商户id,平台版企业(B2B2C企业)使用此参数，B2C企业此参数传""
    String orderid = "";// 订单id
    String orderprice = "";// 订单价格
    int isvip = 0;// 是否为vip会员
    int userlevel = 1;// 用户级别,级别分为1-5,5为最高级,默认为0 非会员


    //    public MedicineDetailsModel model;
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentMedcinesInfo fragmentGoodsInfo;
    private FragmentGoodsPagerDetails fragmentGoodsDetail;
    private FragmentGoodsPagerEvaluate fragmentGoodsEvaluate;
    private Dialog dialogConsult;
    private MedicineDetailsModel medicineModel;


    public static Intent getIntent(Context ctx, String commId) {
        Intent it = new Intent(ctx, ActivityMedicinesInfo.class);
        it.putExtra(AppConfig.IntentExtraKey.MEDICINE_INFO_ID, commId);
        return it;
    }

    @Event(value = {R.id.activity_goods_info_tv_shoucang, R.id.activity_goods_info_tv_shop, R.id.activity_goods_info_tv_kefu, R.id.activity_goods_info_tv_goumai, R.id.activity_goods_info_tv_addcart, R.id.activity_goods_info_tv_zixungoumai, R.id.shopping_cart})
    private void onClick(View v) {
        Intent it;
        switch (v.getId()) {
            case R.id.activity_goods_info_tv_shoucang:
                if (!UserBiz.isLogined()) {
                    ToastUtil.show(this, "请先登录");
                    return;
                }
                setLoding(this, false);
                if (medicineModel.getFavorited().equals("0")) {
//                    Favorites(medicineModel.getWAREID(), "1", "add");
                } else {
//                    Favorites(medicineModel.getWAREID(), "1", "del");
                }
                break;
            case R.id.activity_goods_info_tv_goumai:
                if (!UserBiz.isLogined()) {
                    ToastUtil.show(this, "请先登录");
                    return;
                }
                if (medicineModel != null && !medicineModel.getPRESCRIPTION_FLG().equals("1")) {
                    if (!validData()) {
                        return;
                    }

                    OrderTradeDto order = info2Order();
                    it = ActivityGoodsSubmit.getIntent(this, order, sellerid);
                    startActivity(it);
//                    it.putExtra(ActivityGoodsSubmit.TAG_INFO_SOURCE, ActivityMedicinesInfo.VAL_INFO_SOURCE);
//                    GoodSubmitModel1 goodSubmitModel = new GoodSubmitModel1();
//                    GoodSubmitModel2 goodSubmitModel2 = new GoodSubmitModel2();
//                    List<GoodSubmitModel3> goodList = new ArrayList<>();
//                    List<GoodSubmitModel2> groupList = new ArrayList<>();
//                    GoodSubmitModel3 goods = new GoodSubmitModel3();
//                    goods.setEXCHANGE_QUANLITY(goodsInfo.numberButton.getNumber());
//                    goods.setCOMMODITY_COLOR(model.getPROD_ADD());
//                    goods.setCOMMODITY_SIZE(model.getWARESPEC());
//                    goods.setCOMMODITY_ID(model.getWAREID());
//                    goods.setCOMMODITY_NAME(model.getWARENAME());
//                    goodList.add(goods);
//
//                    goodSubmitModel2.setDETAILS(goodList);
//                    goodSubmitModel2.setOID_GROUP_ID(model.getGROUPID());
//                    goodSubmitModel2.setOID_GROUP_NAME(model.getGROUPNAME());
//
//                    groupList.add(goodSubmitModel2);
//                    goodSubmitModel.setDETAILS(groupList);
//
//                    it.putExtra("data", GsonUtils.Bean2Json(goodSubmitModel));
                }
                break;

            case R.id.activity_goods_info_tv_addcart:
                if (!UserBiz.isLogined()) {
                    ToastUtil.show(this, "请先登录");
                    return;
                }
//                if (medicineModel != null && !medicineModel.getPRESCRIPTION_FLG().equals("1")) {
//                    ShoppingCartAddModel shoppingCartAddModel = new ShoppingCartAddModel();
//                    shoppingCartAddModel.setCOMMODITY_ID(medicineModel.getWAREID());
//                    shoppingCartAddModel.setCOMMODITY_SIZE(medicineModel.getWARESPEC());
//                    shoppingCartAddModel.setCOMMODITY_COLOR(medicineModel.getPROD_ADD());
//                    shoppingCartAddModel.setEXCHANGE_QUANLITY(goodsInfo.numberButton.getNumber() + "");
//                    shoppingCartAddModel.setINS_USER_ID(medicineModel.getGROUPID());
//                    String str = GsonUtils.Bean2Json(shoppingCartAddModel);
//                    addCart(str);
//                }
                break;

            case R.id.activity_goods_info_tv_zixungoumai:
                if (!UserBiz.isLogined()) {
                    ToastUtil.show(this, "请先登录");
                    return;
                }
                //咨询药师购买
                if (medicineModel != null && medicineModel.getPRESCRIPTION_FLG().equals("1")) {
                    openChat(medicineModel.getWARENAME(), "", settingid2, groupName, true, medicineModel.getWAREID());
                }
                break;

            case R.id.activity_goods_info_tv_kefu:
                showConsultImtes((int) v.getX(), (int) v.getY());
                break;

            case R.id.activity_goods_info_tv_shop:
//                if (model == null) return;
//                String groupId = model.getGROUPID();
//                if (StringUtils.isEmpty(groupId)) {
//                    ToastUtil.show(this, "对不起，漂亮商铺页面还没完工。给程序员哥哥加油！");
//                }
//                it = ShopActivity.getIntent(this, groupId);
//                startActivity(it);
                break;
//            case R.id.activity_goods_info_ll_share:
//
//                break;

            case R.id.fragment_goods_info_changeaddr:
                break;
            case R.id.fragment_goods_info_changeshop:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //不是详情页面时组织后退
        if (nsvpContent.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            nsvpContent.setCurrentItem(0);
        }
    }

    /**
     * 将商品放入
     *
     * @return
     */
    private OrderTradeDto info2Order() {
        OrderCommDto o = new OrderCommDto();
        o.setEXCHANGE_QUANLITY(fragmentGoodsInfo.numberButton.getNumber());
        o.setCOMMODITY_COLOR(medicineModel.getPROD_ADD());
        o.setCOMMODITY_SIZE(medicineModel.getWARESPEC());
        o.setCOMMODITY_ID(medicineModel.getWAREID());
        o.setCOMMODITY_NAME(medicineModel.getWARENAME());
        List<OrderCommDto> oList = new ArrayList<>();
        oList.add(o);
        OrderGroupDto og = new OrderGroupDto();
        og.setOID_GROUP_ID(medicineModel.getGROUPID());
        og.setDETAILS(oList);

        List<OrderGroupDto> ogList = new ArrayList<>();
        ogList.add(og);

        OrderTradeDto ot = new OrderTradeDto();
        ot.setDETAILS(ogList);

        return ot;
    }

    private boolean validData() {
        return true;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        wxApi = ((MyApplication) getApplication()).wxApi;

        initIconBtnStat();

        fragmentGoodsInfo = new FragmentMedcinesInfo();

        fragmentGoodsDetail = new FragmentGoodsPagerDetails();
        fragmentGoodsEvaluate = new FragmentGoodsPagerEvaluate();
        fragmentList.add(fragmentGoodsInfo);
        fragmentList.add(fragmentGoodsDetail);
        fragmentList.add(fragmentGoodsEvaluate);

        nsvpContent.setAdapter(new ItemTitlePagerAdapter(getSupportFragmentManager(),
                fragmentList, new String[]{"商品", "详情", "评价"}));
        nsvpContent.setOffscreenPageLimit(3);


        fragmentGoodsInfo.setItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fragment_goods_info_loadevalist:
                        nsvpContent.setCurrentItem(2);
                        break;

                }
            }
        });
        //设置图标
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(60);
        int i = 0, j = 2;//i:起始 j:字数
        Utils.setIconText(this, tvShop, "\ue623\n店铺");
        SpannableString sbs1 = new SpannableString(tvShop.getText());
        sbs1.setSpan(ass, i, j, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvShop.setText(sbs1);
//
        Utils.setIconText(this, tvKefu, "\ue60e\n客服");
        SpannableString sbs2 = new SpannableString(tvKefu.getText());
        sbs2.setSpan(ass, i, j, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvKefu.setText(sbs2);
//
        Utils.setIconText(this, tvShoucang, "\ue62e\n收藏");
        SpannableString sbs3 = new SpannableString(tvShoucang.getText());
        sbs3.setSpan(ass, i, j, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvShoucang.setText(sbs3);
        tvGoumai.setText("提交需求");
        //从网络加载数据
//        String medicineId = getIntent().getStringExtra(AppConfig.IntentExtraKey.MEDICINE_INFO_ID);
        getMedicine("001504");
    }

    private void initIconBtnStat() {
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.back);
        TextView tvTitle = (TextView) findViewById(R.id.activity_fragment_title);
        tvTitle.setText(getTitle());
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityMedicinesInfo.this.finish();
            }
        });

        LinearLayout llIconBtnlist = (LinearLayout) findViewById(R.id.ll_icon_btnlist);
        for (int i = 0; i < llIconBtnlist.getChildCount(); i++) {
            View v = llIconBtnlist.getChildAt(i);
            switch (i) {
                case 0:
                    v.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    //进入购物车
                    v.setVisibility(View.VISIBLE);
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent it = new Intent(ActivityMedicinesInfo.this, CartActivity.class);
                            startActivity(it);
                        }
                    });
                    break;
                case 3:
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MMAlert.showAlert(ActivityMedicinesInfo.this, "分享", ActivityMedicinesInfo.this.getResources().getStringArray(R.array.send_webpage_item),
                                    null, new MMAlert.OnAlertSelectId() {
                                        @Override
                                        public void onClick(int whichButton) {
                                            WXMediaMessage msg = null;
                                            SendMessageToWX.Req req = null;

                                            WXWebpageObject webpage = new WXWebpageObject();
                                            webpage.webpageUrl = AppConfig.WEB_APP_URL + "medicineDetail.html?commodityId=" + medicineModel.getWAREID();
                                            msg = new WXMediaMessage(webpage);
                                            msg.title = medicineModel.getWARENAME();
                                            msg.description = medicineModel.getWARENAME() + " 价格:" + medicineModel.getSALEPRICE() + "";
                                            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.send_music_thumb);
                                            String imgUrl = "";
                                            if (medicineModel.getIMAGES() != null && medicineModel.getIMAGES().length > 0) {
                                                imgUrl = AppConfig.IMAGE_PATH_LJT + medicineModel.getIMAGES()[0];
                                            }
                                            int fixIdx = imgUrl.lastIndexOf('.');
                                            Bitmap.CompressFormat cprsFormat = Bitmap.CompressFormat.PNG;
                                            if (fixIdx > 0) {
                                                String fix = imgUrl.substring(fixIdx);
                                                if ("jpg".equals(fix.toLowerCase())) {
                                                    cprsFormat = Bitmap.CompressFormat.JPEG;
                                                }
                                                try {
                                                    bmp = BitmapFactory.decodeStream(new URL(imgUrl).openStream());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                }
                                            }

                                            if (bmp != null) {
                                                Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                                                bmp.recycle();
                                                msg.thumbData = Utils.bmpToByteArray(cprsFormat, thumbBmp, true);
                                                Log.e(AppConfig.ERR_TAG, "加载图片200:" + imgUrl);
                                            } else {
                                                Log.e(AppConfig.ERR_TAG, "无法加载图片:" + imgUrl);
                                            }

                                            req = new SendMessageToWX.Req();
                                            req.transaction = buildTransaction("webpage");
                                            req.message = msg;


                                            switch (whichButton) {
                                                case MMAlertSelect1:
                                                    req.scene = SendMessageToWX.Req.WXSceneSession;
                                                    break;
                                                case MMAlertSelect2:
                                                    req.scene = SendMessageToWX.Req.WXSceneTimeline;
                                                    break;
                                                case MMAlertSelect3:
                                                    req.scene = SendMessageToWX.Req.WXSceneFavorite;
                                                    break;
                                            }

                                            wxApi.sendReq(req);
                                        }
                                    });
                        }
                    });

                    break;
            }
        }
    }

    void setGouMaiBtn(String stat) {
        if ("1".equals(stat)) {
            //处方药
            tvZixunGoumai.setVisibility(View.VISIBLE);

            tvAddcart.setVisibility(View.GONE);
            tvGoumai.setVisibility(View.GONE);
        } else {
            tvZixunGoumai.setVisibility(View.GONE);

            tvAddcart.setVisibility(View.VISIBLE);
            tvGoumai.setVisibility(View.VISIBLE);
        }
    }


    private void getMedicine(final String medicineId) {
        setLoding(this, false);
        new BizDataAsyncTask<MedicineDetailsModel>() {
            @Override
            protected MedicineDetailsModel doExecute() throws ZYException, BizFailure {
                if (StringUtils.isEmpty(medicineId)) {
                    return null;
                }
                return GoodsBiz.getMedicine(medicineId);
            }

            @Override
            protected void onExecuteSucceeded(MedicineDetailsModel medicineDetailsModel) {
                closeLoding();

                if (medicineDetailsModel == null)
                    return;
                ActivityMedicinesInfo.this.medicineModel = medicineDetailsModel;
                bindData2View();
//                model = medicineDetailsModel;
//                goodsInfo.updeta(model);
//                goodsEvaluate.update(model);
//                goodsDetail.fragmentGoodsParameter.getmodel(model);
//                goodsDetail.fragmentGoodsDetails.getmodel(model);
//                if (model.getPRESCRIPTION_FLG().equals("1")) {
//                    yao_ll.setVisibility(View.VISIBLE);
//                    shang_ll.setVisibility(View.GONE);
//                    kefu_call.setVisibility(View.GONE);
//                } else {
//                    yao_ll.setVisibility(View.GONE);
//                    shang_ll.setVisibility(View.VISIBLE);
//                    kefu_call.setVisibility(View.VISIBLE);
//                }
//
//
//                //------商品浏览轨迹
//                trailparams = new TrailActionBody();
//
//                trailparams.ttl = medicineDetailsModel.getWARENAME();
//                trailparams.url = "";
//                trailparams.sellerid = sellerid;
//                trailparams.ref = "";
//                trailparams.orderid = orderid;
//                trailparams.orderprice = orderprice;
//                if (StringUtils.isEmpty(PreferenceCache.getToken())) {//是否登陆来判断会员vip
//                    isvip = 0;
//                    userlevel = 0;
//                } else {
//                    isvip = 1;
//                    userlevel = 1;
//                }
//                trailparams.isvip = isvip;
//                trailparams.userlevel = userlevel;
//
//                JSONObject item = MyUtil.getRequiredNtalkerParams(sellerid, medicineDetailsModel.getWAREID(), medicineDetailsModel.getWARENAME(),
//                        "", "");
//
//                JSONObject itemOptional = MyUtil.getOptionalNtalkerParams(item, medicineDetailsModel.getSALEPRICE() + "", medicineDetailsModel.getSALEPRICE() + "", null, medicineDetailsModel.getWARESPEC(), null, null, null);
//
//                JSONObject itemSelfDefine = MyUtil.getSelfDefineNtalkerParams(itemOptional);
//
//                String ntalkerparamStr = MyUtil.getNtalkerParam(itemSelfDefine);
//
//                trailparams.ntalkerparam = ntalkerparamStr; // 商品信息、购物车商品JSON字符串数据
//
//                int action = Ntalker.getInstance().startAction(trailparams);
////                Log.e( "action",action+";  "+MyUtil.getNtalkerParam(itemSelfDefine) );
//                //-------
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    private void bindData2View() {
        Log.e(AppConfig.ERR_TAG, "bindData2View.............");
        fragmentGoodsInfo.bindData2View(medicineModel);
        setGouMaiBtn(medicineModel.getPRESCRIPTION_FLG());

        fragmentGoodsDetail.bindData2View(medicineModel);

        fragmentGoodsEvaluate.bindData2View(medicineModel);
    }

//    //向下级fragment传递数据
//    public MedicineDetailsModel getmodel() {
//        return model;
//    }


    //添加购物车
    BizDataAsyncTask<String> cartTask;

    public void addCart(final String str) {
        cartTask = new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                Log.e("************", str);
                return GoodsBiz.shoppingCart(str, "create");
            }

            @Override
            protected void onExecuteSucceeded(String s) {
//                if (s.equals( "1" )){
                ToastUtil.show(ActivityMedicinesInfo.this, "添加购物车成功！");
//                }else {
//
//                }
            }

            @Override
            protected void OnExecuteFailed() {

            }
        };
        cartTask.execute();
    }

//    private void addDb() {
//
//        DbManager dbManager = x.getDb( SqlUtils.getDaoConfig() );
//
//            SqlFavoritesModel sqlFavoritesModel = new SqlFavoritesModel();
//            sqlFavoritesModel.setId( model.getWAREID() );
//            sqlFavoritesModel.setMoney( model.getSALEPRICE() + "" );
//            sqlFavoritesModel.setImage( "http://img4.hqbcdn.com/product/79/f3/79f3ef1b0b2283def1f01e12f21606d4.jpg" );
//            sqlFavoritesModel.setName( model.getWARENAME() );
//
//        try {
//            dbManager.save( sqlFavoritesModel );//添加数据
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//
//    }


//    BizDataAsyncTask<List<CommodityDetailsModel>> detalisTask;
//    private void getDeatlis(){
//        detalisTask=new BizDataAsyncTask<List<CommodityDetailsModel>>() {
//            @Override
//            protected List<CommodityDetailsModel> doExecute() throws ZYException, BizFailure {
//                if (StringUtils.isEmpty(PreferenceCache.getToken())){
//                    return GoodsBiz.getDetails(0,getIntent().getStringExtra(AppConfig.IntentExtraKey.GOODS_INFO_ID));
//                }else {
//                    return GoodsBiz.getDetails(1,getIntent().getStringExtra(AppConfig.IntentExtraKey.GOODS_INFO_ID));
//                }
//            }
//
//            @Override
//            protected void onExecuteSucceeded(List<CommodityDetailsModel> commodityDetailsModels) {
//
//                goodsInfo.getFragmentManager().findFragmentById(R.id.tv_goods_title);
//                goodsInfo.update(commodityDetailsModels);
//
//
//                closeLoding();
//            }
//
//            @Override
//            protected void OnExecuteFailed() {
//                closeLoding();
//            }
//        };
//        detalisTask.execute();
//    }

    //显示咨询的模窗口
    public void showConsultImtes(int viewX, int viewY) {
        if (dialogConsult == null) {
            dialogConsult = new Dialog(this, R.style.Theme_AppCompat_Dialog);
            LayoutInflater inflater = this.getLayoutInflater();

            View layout = inflater.inflate(R.layout.fragment_consult_items, (ViewGroup) findViewById(R.id.vp_content), false);
            View itemKefu = layout.findViewById(R.id.fragment_consult_ll_kefu);
            itemKefu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //开启客服
                    if (medicineModel != null && !medicineModel.getPRESCRIPTION_FLG().equals("1")) {
                        openChat(medicineModel.getWARENAME(), "", settingid2, groupName, true, medicineModel.getWAREID());
                    }


                    dialogConsult.dismiss();
                }
            });
            View itemTel = layout.findViewById(R.id.fragment_consult_ll_tel);
            itemTel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogConsult.dismiss();
                    String mobile = AppConfig.LJG_TEL;
                    if (medicineModel != null) {
                        if (!StringUtils.isEmpty(medicineModel.getCONTACT_TEL())) {
                            mobile = medicineModel.getCONTACT_TEL();
                        } else if (!StringUtils.isEmpty(medicineModel.getCONTACT_MOBILE())) {
                            mobile = medicineModel.getCONTACT_MOBILE();
                        }
                    }
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });

            dialogConsult.setContentView(layout);
            dialogConsult.setCancelable(true);


            Window dialogWindow = dialogConsult.getWindow();
//        dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
            //显示的坐标
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();

            lp.y = viewY + 150;
            lp.x = viewX;
            Log.e(AppConfig.ERR_TAG, lp.x + "//" + lp.y);
            //内容 透明度
//        lp.alpha = 0.2f;
            //遮罩 透明度
            lp.dimAmount = 0.2f;
//        //dialog的大小
//            lp.width = width;
//            lp.verticalMargin = 20f;
            dialogWindow.setAttributes(lp);

        }
        dialogConsult.show();

    }


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private void favorites(final String id, final String flg, final String type) {
        new BizDataAsyncTask<Boolean>() {
            @Override
            protected Boolean doExecute() throws ZYException, BizFailure {
                if (type.equals("add")) {
                    return UserBiz.addFavoritesGoods(id, flg);
                } else {
                    return UserBiz.deleteFavoritesGoods(id);
                }

            }

            @Override
            protected void onExecuteSucceeded(Boolean aBoolean) {
                if (aBoolean) {
                    if (type.equals("add")) {
                        medicineModel.setFavorited("1");
//                        shoucang_img.setImageDrawable(getResources().getDrawable(R.mipmap.icon_shoucang_xuanzhong));
                    } else {
                        medicineModel.setFavorited("0");
//                        shoucang_img.setImageDrawable(getResources().getDrawable(R.mipmap.icon_shoucang_weixuan));
                    }
                }
                closeLoding();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

}
