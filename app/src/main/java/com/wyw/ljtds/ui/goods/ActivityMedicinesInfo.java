package com.wyw.ljtds.ui.goods;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.AddressModel;
import com.wyw.ljtds.model.CurrentLocation;
import com.wyw.ljtds.model.MedicineDetailsModel;
import com.wyw.ljtds.model.MedicineShop;
import com.wyw.ljtds.model.OrderCommDto;
import com.wyw.ljtds.model.OrderGroupDto;
import com.wyw.ljtds.model.OrderTradeDto;
import com.wyw.ljtds.model.ShoppingCartAddModel;
import com.wyw.ljtds.model.SingleCurrentUser;
import com.wyw.ljtds.ui.cart.CartActivity;
import com.wyw.ljtds.ui.user.ActivityLogin;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.MyCallback;
import com.wyw.ljtds.widget.dialog.ShopListDialog;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.xiaoneng.coreapi.TrailActionBody;

/**
 * Created by Administrator on 2016/12/26 0026.
 */
@ContentView(R.layout.activity_goods_info)
public class ActivityMedicinesInfo extends ActivityGoodsInfo {
    private static final String LOGISTIC_ID = "LOGISTIC_ID";
    public static final String MEDICINE_INFO_ID = "medicine_info_id";//进入商品详情页时的id   生活馆 医药馆
    @ViewInject(R.id.tv_title)
    public TextView tv_title;
    private static final int THUMB_SIZE = 150;
    ShopListDialog dialogShopList; //shop list dialog


    String commId = null;
    String logisticId = "001";
    String addrId = "";
    String lat = "", lng = "";

    TrailActionBody trailparams = null;// 轨迹信息实例
    // 轨迹（专有参数）
    String sellerid = "lj_1001";// 商户id,平台版企业(B2B2C企业)使用此参数，B2C企业此参数传""
    View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_goods_info_loadevalist:
                    Log.e(AppConfig.ERR_TAG, "nsvpContent itemClickListener" + R.id.fragment_goods_info_loadevalist + "==:" + v.getId());
                    nsvpContent.setCurrentItem(2);
                    break;
                case R.id.fragment_medicine_info_changeshop:
                    showShopListDialog();
                    break;
            }
        }
    };
    private FragmentMedcinesInfo fragmentGoodsInfo = FragmentMedcinesInfo.getInstance(itemClickListener);

    private MedicineDetailsModel medicineModel;


    public static Intent getIntent(Context ctx, String commId, String logisticId) {
        Intent it = new Intent(ctx, ActivityMedicinesInfo.class);
        it.putExtra(MEDICINE_INFO_ID, commId);
        it.putExtra(LOGISTIC_ID, logisticId);
        return it;
    }

    @Event(value = {R.id.activity_goods_info_rl_shoucang, R.id.activity_goods_info_rl_shop, R.id.activity_goods_info_rl_kefu, R.id.activity_goods_info_tv_goumai, R.id.activity_goods_info_tv_addcart, R.id.shopping_cart})
    private void onClick(View v) {
//        super.onClick(v);
        switch (v.getId()) {
            case R.id.activity_goods_info_tv_goumai:
                if (medicineModel == null) return;
                if (!UserBiz.isLogined()) {
                    startActivity(ActivityLogin.getIntent(this));
                    return;
                }
                if (medicineModel.getPRESCRIPTION_FLG().equals("1")) {
                    //处方
                    openChat(medicineModel.getWARENAME(), "", AppConfig.CHAT_XN_LJT_SETTINGID2, AppConfig.CHAT_XN_LJT_TITLE, true, medicineModel.getWAREID());
                    return;
                }
                //非处方
                if (!validData()) return;
                OrderTradeDto order = info2Order();
                Intent it = ActivityGoodsSubmit.getIntent(this, order);
                startActivity(it);

                break;

            case R.id.activity_goods_info_rl_shoucang:
                if (!UserBiz.isLogined()) {
                    ToastUtil.show(this, "请先登录");
                    return;
                }
                doFavorite(medicineModel.getWAREID(), medicineModel);
                break;

            case R.id.activity_goods_info_rl_kefu:
                int[] point = new int[2];
                v.getLocationOnScreen(point);
                showConsultImtes(point);
                break;
            case R.id.activity_goods_info_tv_addcart:
                if (!UserBiz.isLogined()) {
//                    ToastUtil.show(this, "请先登录");
                    startActivity(ActivityLogin.getIntent(this));
                    return;
                }
                if (medicineModel != null && !medicineModel.getPRESCRIPTION_FLG().equals("1")) {
                    ShoppingCartAddModel shoppingCartAddModel = new ShoppingCartAddModel();
                    shoppingCartAddModel.setCOMMODITY_ID(medicineModel.getWAREID());
                    shoppingCartAddModel.setCOMMODITY_SIZE(medicineModel.getWARESPEC());
//                    shoppingCartAddModel.setCOMMODITY_COLOR(Utils.trim(medicineModel.getPROD_ADD()));
                    shoppingCartAddModel.setCOMMODITY_COLOR(Utils.trim(medicineModel.getCOMMODITY_BRAND()));
                    shoppingCartAddModel.setEXCHANGE_QUANLITY("" + fragmentGoodsInfo.numberButton.getNumber());
                    shoppingCartAddModel.setINS_USER_ID(AppConfig.GROUP_LJT);
                    shoppingCartAddModel.setBUSNO(medicineModel.getLOGISTICS_COMPANY_ID());
                    shoppingCartAddModel.setBUSNAME(medicineModel.getLOGISTICS_COMPANY());
                    String str = GsonUtils.Bean2Json(shoppingCartAddModel);
                    Log.e(AppConfig.ERR_TAG, "addCart:" + str);
                    addCart(str);
                }
                break;

            case R.id.activity_goods_info_rl_shop:
                it = ShopActivity.getIntent(this, medicineModel.getLOGISTICS_COMPANY_ID());
                startActivity(it);
//                if (model == null) return;
//                String groupId = model.getGROUPID();
//                if (StringUtils.isEmpty(groupId)) {
//                    ToastUtil.show(this, "对不起，漂亮商铺页面还没完工。给程序员哥哥加油！");
//                }
//                it = ShopActivity.getIntent(this, groupId);
//                startActivity(it);
                break;

        }

    }

    /**
     * 将商品放入
     *
     * @return
     */
    @Override
    protected OrderTradeDto info2Order() {
        OrderCommDto o = new OrderCommDto();
        o.setEXCHANGE_QUANLITY(fragmentGoodsInfo.numberButton.getNumber());
        o.setCOMMODITY_COLOR(Utils.trim(medicineModel.getPROD_ADD()));
        o.setCOMMODITY_SIZE(medicineModel.getWARESPEC());
        o.setCOMMODITY_ID(medicineModel.getWAREID());
        o.setCOMMODITY_NAME(medicineModel.getWARENAME());
        List<OrderCommDto> oList = new ArrayList<>();
        oList.add(o);

        OrderGroupDto og = new OrderGroupDto();
        og.setLOGISTICS_COMPANY_ID(medicineModel.getLOGISTICS_COMPANY_ID());
        og.setLOGISTICS_COMPANY(medicineModel.getLOGISTICS_COMPANY());
        og.setDETAILS(oList);
        List<OrderGroupDto> ogList = new ArrayList<>();
        ogList.add(og);

        OrderTradeDto ot = new OrderTradeDto();
        ot.setDETAILS(ogList);
        ot.setLAT(SingleCurrentUser.location.getLatitude() + "");
        ot.setLNG(SingleCurrentUser.location.getLongitude() + "");
        ot.setINS_USER_ID(AppConfig.GROUP_LJT);

        return ot;
    }


    @Override
    protected Fragment getMainFragment() {
        return fragmentGoodsInfo;
    }

    @Override
    protected void handleClickEvent(View v) {
        switch (v.getId()) {
            case R.id.fragment_consult_ll_kefu:
                //开启客服
                if (medicineModel != null && !medicineModel.getPRESCRIPTION_FLG().equals("1")) {
                    openChat(medicineModel.getWARENAME(), "", AppConfig.CHAT_XN_LJT_SETTINGID2, AppConfig.CHAT_XN_LJT_TITLE, true, medicineModel.getWAREID());
                }
                break;
            case R.id.fragment_consult_ll_tel:
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
                break;
        }

    }

    private boolean validData() {
        return true;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvGoumai.setText(getString(R.string.goumai_medicine));

        commId = getIntent().getStringExtra(MEDICINE_INFO_ID);
        initFragmentList(commId);
        initIconBtnStat();


        fragmentGoodsInfo.addItemCallback(new MyCallback() {
            @Override
            public void callback(Object... params) {
                if (params[0] == null) return;
                AddressModel addr = (AddressModel) params[0];
                addrId = "" + addr.getADDRESS_ID();
                getMedicine();
            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent it = getIntent();
        lat = "" + SingleCurrentUser.defaultLat;
        lng = "" + SingleCurrentUser.defaultLng;
        if (SingleCurrentUser.location != null) {

            lat = StringUtils.formatLatlng(SingleCurrentUser.location.getLatitude());
            lng = StringUtils.formatLatlng(SingleCurrentUser.location.getLongitude());
        } else {
            Utils.log("SingleCurrentUser.location is null ..............");
        }
        commId = it.getStringExtra(MEDICINE_INFO_ID);
        logisticId = it.getStringExtra(LOGISTIC_ID);

        getMedicine();
//        getMedicine(commId, logistic);
    }

    /**
     * 针对不同药品设置是否可以直接购买
     *
     * @param stat
     */
    void setGouMaiBtn(String stat) {
        if ("1".equals(stat)) {
            //处方药
            tvAddcart.setVisibility(View.GONE);
            tvGoumai.setText(getString(R.string.goumai_zixun));
        } else {
            tvAddcart.setVisibility(View.VISIBLE);
            tvGoumai.setText(getString(R.string.goumai_medicine));
        }
    }


    private void getMedicine() {
        setLoding(this, false);
        new BizDataAsyncTask<MedicineDetailsModel>() {
            @Override
            protected MedicineDetailsModel doExecute() throws ZYException, BizFailure {
//                Utils.log(":" + commId + "-" + logisticId + "-" + lat + "-" + lng + "-" + addrId);
                return GoodsBiz.getMedicine(commId, logisticId, lat, lng, addrId);
            }

            @Override
            protected void onExecuteSucceeded(MedicineDetailsModel medicineDetailsModel) {
                closeLoding();
                if (medicineDetailsModel == null) {
                    ToastUtil.show(ActivityMedicinesInfo.this, "没有符合条件的商品");
                    return;
                }
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
        if (medicineModel == null) return;

        setShoucangText(medicineModel.getFavorited());
        setToolMainEnable(true);
        fragmentGoodsInfo.bindData2View(medicineModel);
        setGouMaiBtn(medicineModel.getPRESCRIPTION_FLG());

        fragmentGoodsDetail.bindData2View(medicineModel);
//        fragmentGoodsEvaluate.bindData2View(medicineModel);
    }


    private void showShopListDialog() {
        dialogShopList = new ShopListDialog(this, R.style.AllScreenWidth_Dialog);
        dialogShopList.setCallback(new MyCallback() {
            @Override
            public void callback(Object... params) {
                //selected item from dialog
                MedicineShop data = (MedicineShop) params[1];
                ActivityMedicinesInfo.this.logisticId = data.getLOGISTICS_COMPANY_ID();
                getMedicine();
                dialogShopList.dismiss();
            }
        });
        setLoding(this, false);
        new BizDataAsyncTask<List<MedicineShop>>() {
            @Override
            protected List<MedicineShop> doExecute() throws ZYException, BizFailure {
//                return GsonUtils.Json2ArrayList("[{ isChecked : false, LOGISTICS_COMPANY:'中村1', LOGISTICS_COMPANY_ID:'001', ORG_MOBILE:'', ORG_PRINCIPAL:'', AREA:'', LNG:'', LAT:'', DISTANCE_TEXT:'中村xx', DURATION_TEXT:'aa', QISONG:'', BAOYOU:'' },{ isChecked : false, LOGISTICS_COMPANY:'中村2', LOGISTICS_COMPANY_ID:'001', ORG_MOBILE:'', ORG_PRINCIPAL:'', AREA:'', LNG:'', LAT:'', DISTANCE_TEXT:'中村xx', DURATION_TEXT:'aa', QISONG:'', BAOYOU:'' }]", MedicineShop.class);
                String lat = SingleCurrentUser.defaultLat + "", lng = "" + SingleCurrentUser.defaultLng;
                if (SingleCurrentUser.location != null) {
                    lat = SingleCurrentUser.location.getLatitude() + "";
                    lng = SingleCurrentUser.location.getLongitude() + "";
                }
                return GoodsBiz.readMedicineShop(medicineModel.getWAREID(), lat, lng);
            }

            @Override
            protected void onExecuteSucceeded(List<MedicineShop> medicineShopList) {
                closeLoding();
                dialogShopList.bindData2View(medicineShopList);
                dialogShopList.show();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    private void initIconBtnStat() {
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.back);
        TextView tvTitle = (TextView) findViewById(R.id.activity_fragment_title);
        tvTitle.setText(getTitle());

        LinearLayout llIconBtnlist = (LinearLayout) findViewById(R.id.ll_icon_btnlist);
        for (int i = 0; i < llIconBtnlist.getChildCount(); i++) {
            View v = llIconBtnlist.getChildAt(i);
            switch (i) {
                case 3:
                    //wechat share
                    v.setVisibility(View.VISIBLE);
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utils.log("getIMG_PATH:" + medicineModel.getIMG_PATH());
                            String description = medicineModel.getWARENAME() + "  " + medicineModel.getSALEPRICE();
                            wechatShare(medicineModel.getWARENAME(), description, medicineModel.getIMG_PATH(), AppConfig.WEB_APP_URL + "/medicineDetail.html?commodityId=" + medicineModel.getWAREID());
                        }
                    });
                    break;
            }
        }
    }

}
