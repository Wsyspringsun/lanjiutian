package com.wyw.ljtds.ui.goods;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.CommodityDetailsModel;
import com.wyw.ljtds.model.GoodsModel;
import com.wyw.ljtds.model.OrderCommDto;
import com.wyw.ljtds.model.OrderGroupDto;
import com.wyw.ljtds.model.OrderTradeDto;
import com.wyw.ljtds.model.ShoppingCartAddModel;
import com.wyw.ljtds.model.SingleCurrentUser;
import com.wyw.ljtds.model.XiaoNengData;
import com.wyw.ljtds.ui.user.ActivityLoginOfValidCode;
import com.wyw.ljtds.ui.user.order.ActivityOrder;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.MyCallback;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.xiaoneng.uiapi.Ntalker;


/**
 * Created by Administrator on 2016/12/26 0026.
 */
@ContentView(R.layout.activity_goods_info)
public class ActivityLifeGoodsInfo extends ActivityGoodsInfo {
    GoodsBiz goodsBiz = null;

    private static final String TAG_LIFE_GOODS_ID = "com.wyw.ljtds.ui.goods.ActivityLifeGoodsInfo.TAG_LIFE_GOODS_ID";
    private FragmentGoodsInfo fragmentCommodityInfo = new FragmentGoodsInfo();
    private Dialog dialogConsult;
    private CommodityDetailsModel commodityModel;
    private String comId;

    @ViewInject(R.id.activity_goods_info_tv_goumai)
    TextView tvGoumai;
    @ViewInject(R.id.activity_goods_info_tv_addcart)
    TextView tvShopcart;


    @Event(value = {R.id.activity_goods_info_rl_shoucang, R.id.activity_goods_info_rl_shop, R.id.activity_goods_info_rl_kefu, R.id.activity_goods_info_tv_goumai, R.id.activity_goods_info_tv_addcart, R.id.shopping_cart})
    private void onClick(View v) {
//        super.onClick(v);
        Log.e(AppConfig.ERR_TAG, "shop");
        if (commodityModel == null) return;
        switch (v.getId()) {
            case R.id.activity_goods_info_tv_goumai:
                if (!UserBiz.isLogined()) {
//                    ToastUtil.show(this, "请先登录");
                    startActivity(ActivityLoginOfValidCode.getIntent(this));
                    return;
                }

                if (commodityModel == null) return;
                if (commodityModel.getColorList() == null || commodityModel.getColorList().size() <= 0) {
                    return;
                }
                final CommodityDetailsModel.ColorList selcolor = commodityModel.getColorList().get(0);
                if (selcolor.getSizeList() == null || selcolor.getSizeList().size() <= 0) {
                    return;
                }
                final CommodityDetailsModel.SizeList selSize = selcolor.getSizeList().get(0);

                if (GoodsModel.TOP_FLG_LINGYUAN.equals(commodityModel.getTopFlg())) {
                    setLoding(this, false);
                    new BizDataAsyncTask<String>() {
                        @Override
                        protected String doExecute() throws ZYException, BizFailure {
                            Map<String, Object> map = new HashMap<>();
                            map.put("SHOP_NAME", commodityModel.getGroupName());
                            map.put("OID_GROUP_ID", commodityModel.getOidGroupId());
                            map.put("COMMODITY_ID", commodityModel.getCommodityId());
                            commodityModel.getColorList().get(0).getColorName();
                            map.put("COMMODITY_COLOR", selcolor.getColorName());
                            map.put("COMMODITY_SIZE", selSize.getCommoditySize());

                            String data = GsonUtils.Bean2Json(map);
                            return goodsBiz.createOrderByLing(data);
                        }

                        @Override
                        protected void onExecuteSucceeded(String s) {
                            Utils.log("createOrderByLing:" + s);
                            ToastUtil.show(ActivityLifeGoodsInfo.this, "领取成功，请在订单列表查看");
                            startActivity(ActivityOrder.getIntent(ActivityLifeGoodsInfo.this, 0));
                            closeLoding();
                        }

                        @Override
                        protected void OnExecuteFailed() {
                            closeLoding();
                        }
                    }.execute();
                } else {
                    if (fragmentCommodityInfo.seledSize == null) {
                        fragmentCommodityInfo.showSelDialog(new MyCallback() {
                            @Override
                            public void callback(Object... params) {
                                fragmentCommodityInfo.tv_current_goods.setText(fragmentCommodityInfo.selDialog.tvSeled.getText().toString());
                                if (params == null || params.length <= 0) return;
                                fragmentCommodityInfo.seledColor = (CommodityDetailsModel.ColorList) params[0];
                                fragmentCommodityInfo.seledSize = (CommodityDetailsModel.SizeList) params[1];
                                fragmentCommodityInfo.seledNum = (int) params[2];
                                fragmentCommodityInfo.selDialog.dismiss();

                                if (commodityModel != null) {
                                    if (!validData()) return;
                                    OrderTradeDto order = info2Order();
                                    Intent it = null;
                                    if (GoodsModel.TOP_FLG_ZHIJIEGOU.equals(commodityModel.getTopFlg())) {
                                        it = ActivityGoodsSubmit.getIntent(ActivityLifeGoodsInfo.this, order, GoodsModel.TOP_FLG_ZHIJIEGOU);
                                    } else {
                                        it = ActivityGoodsSubmit.getIntent(ActivityLifeGoodsInfo.this, order);
                                    }
                                    startActivity(it);
                                }

                            }
                        });
                        return;
                    }
//
                    if (commodityModel != null) {
                        if (!validData()) return;
                        OrderTradeDto order = info2Order();
                        Intent it = null;
                        if (GoodsModel.TOP_FLG_ZHIJIEGOU.equals(commodityModel.getTopFlg())) {
                            it = ActivityGoodsSubmit.getIntent(ActivityLifeGoodsInfo.this, order, GoodsModel.TOP_FLG_ZHIJIEGOU);
                        } else {
                            it = ActivityGoodsSubmit.getIntent(this, order);
                        }
                        startActivity(it);
                    }
                }


                break;
            case R.id.activity_goods_info_rl_shoucang:
                if (!UserBiz.isLogined()) {
                    ToastUtil.show(this, "请先登录");
                    return;
                }
                if (commodityModel == null) return;
                doFavorite(commodityModel.getCommodityId(), commodityModel);
                break;

            case R.id.activity_goods_info_rl_kefu:
//                showConsultImtes((int) v.getX(), (int) v.getY());
                int[] point = new int[2];
                v.getLocationOnScreen(point);
                showConsultImtes(point);
                break;
            case R.id.activity_goods_info_tv_addcart:
                if (!UserBiz.isLogined()) {
                    ToastUtil.show(this, "请先登录");
                    return;
                }

                if (fragmentCommodityInfo.seledSize == null) {
                    fragmentCommodityInfo.showSelDialog(new MyCallback() {
                        @Override
                        public void callback(Object... params) {
                            fragmentCommodityInfo.tv_current_goods.setText(fragmentCommodityInfo.selDialog.tvSeled.getText().toString());
                            if (params == null || params.length <= 0) return;
                            fragmentCommodityInfo.seledColor = (CommodityDetailsModel.ColorList) params[0];
                            fragmentCommodityInfo.seledSize = (CommodityDetailsModel.SizeList) params[1];
                            fragmentCommodityInfo.seledNum = (int) params[2];
                            fragmentCommodityInfo.selDialog.dismiss();

                            if (commodityModel != null) {
                                addCart(view2ShopcartData());
                            }

                        }
                    });
                    return;
                }
//
                if (commodityModel != null) {
                    addCart(view2ShopcartData());
                }
                break;


            case R.id.activity_goods_info_rl_shop:
                Intent it = LifeShopActivity.getIntent(this, commodityModel.getOidGroupId());
                startActivity(it);
                break;

        }

    }

    private String view2ShopcartData() {
        ShoppingCartAddModel shoppingCartAddModel = new ShoppingCartAddModel();
        shoppingCartAddModel.setCOMMODITY_ID(commodityModel.getCommodityId());
        shoppingCartAddModel.setCOMMODITY_SIZE(fragmentCommodityInfo.seledSize.getCommoditySizeId());
        shoppingCartAddModel.setCOMMODITY_COLOR(fragmentCommodityInfo.seledSize.getCommodityColorId());
        shoppingCartAddModel.setEXCHANGE_QUANLITY("" + fragmentCommodityInfo.seledNum);
        shoppingCartAddModel.setINS_USER_ID(AppConfig.GROUP_LIFE);
        shoppingCartAddModel.setBUSNO(commodityModel.getOidGroupId());
        shoppingCartAddModel.setBUSNAME(commodityModel.getGroupName());
        String str = GsonUtils.Bean2Json(shoppingCartAddModel);
        Log.e(AppConfig.ERR_TAG, "addCart:" + str);
        return str;
    }

    //
    public static Intent getIntent(Context ctx, String commId) {
        Intent it = new Intent(ctx, ActivityLifeGoodsInfo.class);
        it.putExtra(TAG_LIFE_GOODS_ID, commId);
        return it;
    }

    @Override
    protected OrderTradeDto info2Order() {
        OrderCommDto o = new OrderCommDto();
        o.setEXCHANGE_QUANLITY(fragmentCommodityInfo.seledNum);
        o.setCOMMODITY_COLOR(fragmentCommodityInfo.seledColor.getColorName());
        o.setCOMMODITY_SIZE(fragmentCommodityInfo.seledSize.getCommoditySize());
        o.setCOMMODITY_ID(commodityModel.getCommodityId());
        o.setCOMMODITY_NAME(commodityModel.getTitle());
        List<OrderCommDto> oList = new ArrayList<>();
        oList.add(o);

        OrderGroupDto og = new OrderGroupDto();
        og.setLOGISTICS_COMPANY_ID(commodityModel.getOidGroupId());
        og.setLOGISTICS_COMPANY(commodityModel.getGroupName());
        og.setDETAILS(oList);
        List<OrderGroupDto> ogList = new ArrayList<>();
        ogList.add(og);

        OrderTradeDto ot = new OrderTradeDto();
        ot.setDETAILS(ogList);
        ot.setLAT(SingleCurrentUser.location.getLatitude() + "");
        ot.setLNG(SingleCurrentUser.location.getLongitude() + "");
        ot.setINS_USER_ID(AppConfig.GROUP_LIFE);
        if (SingleCurrentUser.location != null && !StringUtils.isEmpty(SingleCurrentUser.location.getADDRESS_ID())) {
            ot.setADDRESS_ID(SingleCurrentUser.location.getADDRESS_ID() + "");
        }

        return ot;
    }

    @Override
    protected Fragment getMainFragment() {
        return fragmentCommodityInfo;
    }

    @Override
    protected void handleClickEvent(View v) {
        if (commodityModel == null) return;
        switch (v.getId()) {
            case R.id.fragment_consult_ll_kefu:
                //开启客服
                if (commodityModel != null) {
//                    openChat(commodityModel.getTitle(), "", AppConfig.CHAT_XN_LJT_SETTINGID2, AppConfig.CHAT_XN_LJT_TITLE, true, medicineModel.getWAREID());
                    XiaoNengData xnd = commodityModel.getXiaonengData();
                    if (xnd != null) {
                        openChat(commodityModel.getTitle(), "", xnd.getSettingid1(), commodityModel.getGroupName(), true, commodityModel.getCommodityId());
                    } else {
                        ToastUtil.show(ActivityLifeGoodsInfo.this, getString(R.string.xn_kefu_leave));
                    }
                }
                break;
            case R.id.fragment_consult_ll_tel:
                String mobile = AppConfig.LJG_TEL;
                if (commodityModel != null) {
                    if (!StringUtils.isEmpty(commodityModel.getContactTel())) {
                        mobile = commodityModel.getContactTel();
                    }
                }
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goodsBiz = GoodsBiz.getInstance(this);
        tvGoumai.setText(R.string.goumai);
        initFragmentList(getIntent().getStringExtra(TAG_LIFE_GOODS_ID));
//
//
        initIconBtnStat();
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
                            if (commodityModel == null) return;
                            String price = "";
                            if (commodityModel.getColorList() != null && commodityModel.getColorList().size() > 0 && commodityModel.getColorList().get(0).getSizeList() != null && commodityModel.getColorList().get(0).getSizeList().size() > 0) {
                                price = commodityModel.getColorList().get(0).getSizeList().get(0).getMarketPrice() + "";
                            }
//                            String description = commodityModel.getTitle() + "  " + price;
                            String description = commodityModel.getShareDesc();
                            wechatShare(commodityModel.getShareTitle(), description, AppConfig.IMAGE_PATH_LJT + commodityModel.getImgPath(), AppConfig.WEB_APP_URL + "/lifeDetail.html?commodityId=" + commodityModel.getCommodityId());
                        }
                    });
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        comId = getIntent().getStringExtra(TAG_LIFE_GOODS_ID);
        getGoods(comId);
    }

    //
    private void getGoods(final String id) {
        setLoding(this, false);
        new BizDataAsyncTask<CommodityDetailsModel>() {
            @Override
            protected CommodityDetailsModel doExecute() throws ZYException, BizFailure {
                return GoodsBiz.getGoods(id);
            }

            @Override
            protected void onExecuteSucceeded(CommodityDetailsModel commodityDetailsModel) {
                closeLoding();
                commodityModel = commodityDetailsModel;
                bindData2View();
//
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    private boolean validData() {
        return true;
    }

    private void bindData2View() {
        if (commodityModel != null)
            setToolMainEnable(true);
        else
            setToolMainEnable(false);

        if (GoodsModel.TOP_FLG_LINGYUAN.equals(commodityModel.getTopFlg())) {
            tvGoumai.setText(R.string.huodong_lingyuangou);
            tvShopcart.setVisibility(View.GONE);
        }

        if (GoodsModel.TOP_FLG_ZHIJIEGOU.equals(commodityModel.getTopFlg())) {
            tvShopcart.setVisibility(View.GONE);
        }


        XiaoNengData xnd = commodityModel.getXiaonengData();
        if (xnd != null) {
            String startPageUrl = AppConfig.WEB_APP_URL + "/lifeDetail.html?commodityId=" + commodityModel.getCommodityId();
            Ntalker.getBaseInstance().startAction_goodsDetail(commodityModel.getTitle(), startPageUrl, xnd.getSellerid(), "");
        }

        fragmentCommodityInfo.updeta(commodityModel);
        fragmentGoodsDetail.bindData2View(commodityModel);
    }

}
