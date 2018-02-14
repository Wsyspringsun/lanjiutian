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
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.CommodityDetailsModel;
import com.wyw.ljtds.model.OrderCommDto;
import com.wyw.ljtds.model.OrderGroupDto;
import com.wyw.ljtds.model.OrderTradeDto;
import com.wyw.ljtds.model.ShoppingCartAddModel;
import com.wyw.ljtds.model.SingleCurrentUser;
import com.wyw.ljtds.model.XiaoNengData;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.MyCallback;
import com.wyw.ljtds.widget.goodsinfo.NoScrollViewPager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/12/26 0026.
 */
@ContentView(R.layout.activity_goods_info)
public class ActivityLifeGoodsInfo extends ActivityGoodsInfo {
    private static final String TAG_LIFE_GOODS_ID = "com.wyw.ljtds.ui.goods.ActivityLifeGoodsInfo.TAG_LIFE_GOODS_ID";
    private FragmentGoodsInfo fragmentCommodityInfo = new FragmentGoodsInfo();
    private Dialog dialogConsult;
    private CommodityDetailsModel commodityModel;
    private String comId;

    @Event(value = {R.id.activity_goods_info_rl_shoucang, R.id.activity_goods_info_rl_shop, R.id.activity_goods_info_rl_kefu, R.id.activity_goods_info_tv_goumai, R.id.activity_goods_info_tv_addcart, R.id.shopping_cart})
    private void onClick(View v) {
//        super.onClick(v);
        Log.e(AppConfig.ERR_TAG, "shop");
        switch (v.getId()) {
            case R.id.activity_goods_info_tv_goumai:
                if (!UserBiz.isLogined()) {
                    ToastUtil.show(this, "请先登录");
                    return;
                }

                if (commodityModel == null) return;
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
                                Intent it = ActivityGoodsSubmit.getIntent(ActivityLifeGoodsInfo.this, order);
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
                    Intent it = ActivityGoodsSubmit.getIntent(this, order);
                    startActivity(it);
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

        return ot;
    }

    @Override
    protected Fragment getMainFragment() {
        return fragmentCommodityInfo;
    }

    @Override
    protected void handleClickEvent(View v) {
        switch (v.getId()) {
            case R.id.fragment_consult_ll_kefu:
                //开启客服
                if (commodityModel != null) {
//                    openChat(commodityModel.getTitle(), "", AppConfig.CHAT_XN_LJT_SETTINGID2, AppConfig.CHAT_XN_LJT_TITLE, true, medicineModel.getWAREID());
                    XiaoNengData xnd = commodityModel.getXiaonengData();
                    openChat(commodityModel.getTitle(), "", xnd.getSettingid1(), commodityModel.getGroupName(), true, commodityModel.getCommodityId());
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
//
        tvGoumai.setText(R.string.goumai);
        initFragmentList(getIntent().getStringExtra(TAG_LIFE_GOODS_ID ));
//
//
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

        XiaoNengData xnd = commodityModel.getXiaonengData();
        if (xnd != null) {
//            sellerid = xnd.getSellerid();
//            settingid1 = xnd.getSettingid1();
        } else {
//            Log.e(AppConfig.ERR_EXCEPTION, "XiaoNengData is null");
        }
//        groupName = model.getGroupName();

        fragmentCommodityInfo.updeta(commodityModel);
        fragmentGoodsDetail.bindData2View(commodityModel);
//        fragmentGoodsEvaluate.bindData2View(commodityModel);
    }
//
//    //添加购物车
//    BizDataAsyncTask<String> cartTask;
//
//    private void addCart(final String str) {
//        cartTask = new BizDataAsyncTask<String>() {
//            @Override
//            protected String doExecute() throws ZYException, BizFailure {
//                return GoodsBiz.shoppingCart(str, "create");
//            }
//
//            @Override
//            protected void onExecuteSucceeded(String s) {
//                ToastUtil.show(ActivityGoodsInfo.this, "添加购物车成功！");
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
//    //显示咨询的模窗口
//    public void showConsultImtes(int viewX, int viewY) {
//        if (dialogConsult == null) {
//            dialogConsult = new Dialog(this, R.style.Theme_AppCompat_Dialog);
//            LayoutInflater inflater = this.getLayoutInflater();
//
//            View layout = inflater.inflate(R.layout.fragment_consult_items, (ViewGroup) findViewById(R.id.vp_content), false);
//            View itemKefu = layout.findViewById(R.id.fragment_consult_ll_kefu);
//            itemKefu.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialogConsult.dismiss();
//                    //开启客服
//                    openChat(model.getTitle(), "", settingid1, groupName, true, model.getCommodityId());
//                }
//            });
//            View itemTel = layout.findViewById(R.id.fragment_consult_ll_tel);
//            itemTel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialogConsult.dismiss();
//                    String mobile = AppConfig.LJG_TEL;
//                    if (model != null) {
//                        if (!StringUtils.isEmpty(model.getContactMobile())) {
//                            mobile = model.getContactMobile();
//                        } else if (!StringUtils.isEmpty(model.getContactTel())) {
//                            mobile = model.getContactTel();
//                        }
//                    }
//                    Log.e(AppConfig.ERR_TAG, model.getContactMobile() + " tel:" + model.getContactTel());
//                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile));
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
//            });
//
//            dialogConsult.setContentView(layout);
//            dialogConsult.setCancelable(true);
//
//
//            Window dialogWindow = dialogConsult.getWindow();
////        dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//            dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
//            //显示的坐标
//            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//
//
//            lp.y = viewY + 150;
//            lp.x = viewX;
//            //内容 透明度
////        lp.alpha = 0.2f;
//            //遮罩 透明度
//            lp.dimAmount = 0.2f;
////        //dialog的大小
////            lp.width = width;
////            lp.verticalMargin = 20f;
//            dialogWindow.setAttributes(lp);
//
//        }
//        dialogConsult.show();
//
//    }
//
////    private void addDb() {
////
////        DbManager dbManager = x.getDb( SqlUtils.getDaoConfig() );
////
////            SqlFavoritesModel sqlFavoritesModel = new SqlFavoritesModel();
////            sqlFavoritesModel.setId( model.getWAREID() );
////            sqlFavoritesModel.setMoney( model.getSALEPRICE() + "" );
////            sqlFavoritesModel.setImage( "http://img4.hqbcdn.com/product/79/f3/79f3ef1b0b2283def1f01e12f21606d4.jpg" );
////            sqlFavoritesModel.setName( model.getWARENAME() );
////
////        try {
////            dbManager.save( sqlFavoritesModel );//添加数据
////        } catch (DbException e) {
////            e.printStackTrace();
////        }
////
////    }
//
//
//    private String buildTransaction(final String type) {
//        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
//    }


}
