package com.wyw.ljtds.ui.goods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gxz.PagerSlidingTabStrip;
import com.wyw.ljtds.MainActivity;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.goodsinfo.ItemTitlePagerAdapter;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.CommodityDetailsModel;
import com.wyw.ljtds.model.GoodSubmitModel1;
import com.wyw.ljtds.model.GoodSubmitModel2;
import com.wyw.ljtds.model.GoodSubmitModel3;
import com.wyw.ljtds.model.ShoppingCartAddModel;
import com.wyw.ljtds.model.XiaoNengData;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.widget.goodsinfo.NoScrollViewPager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.xiaoneng.coreapi.TrailActionBody;

/**
 * Created by Administrator on 2016/12/26 0026.
 */
@ContentView(R.layout.activity_goods_info)
public class ActivityGoodsInfo extends BaseActivity {
    @ViewInject(R.id.psts_tabs)
    public PagerSlidingTabStrip psts_tabs;
    @ViewInject(R.id.vp_content)
    public NoScrollViewPager vp_content;
    @ViewInject(R.id.tv_title)
    public TextView tv_title;
    @ViewInject(R.id.yao_ll)
    private TextView yao_ll;
    @ViewInject(R.id.shang_ll)
    private LinearLayout shang_ll;
    @ViewInject(R.id.kefu_call)
    private RelativeLayout kefu_call;
    @ViewInject(R.id.activity_goods_info_shop)
    private RelativeLayout rlShop;

    @ViewInject(R.id.goumai)
    private TextView tvGoumai;


    public static final String VAL_INFO_SOURCE = "ActivityGoodsInfo";
    // 轨迹（专有参数）
//    String sellerid = "lj_1001";// 商户id,平台版企业(B2B2C企业)使用此参数，B2C企业此参数传""
    String sellerid = "";// 商户id,平台版企业(B2B2C企业)使用此参数，B2C企业此参数传""
    //客服
    String settingid1 = "";
    //    String settingid1 = "lj_1001_1496308413541";
    String groupName = "";// 客服组默认名称
    //    String groupName = "蓝九天商户平台";// 客服组默认名称
    TrailActionBody trailparams = null;// 轨迹信息实例
    String ttl = "";// 当前页标题，如首页、购物车、订单、支付
    String url = "";// 当前页地址
    String ref = "";// 前一页面的地址
    String orderid = "";// 订单id
    String orderprice = "";// 订单价格
    int isvip = 0;// 是否为vip会员
    int userlevel = 1;// 用户级别,级别分为1-5,5为最高级,默认为0 非会员


    public CommodityDetailsModel model;
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentGoodsInfo goodsInfo;
    private FragmentGoodsPagerDetails goodsDetail;
    private FragmentGoodsPagerEvaluate goodsEvaluate;

    public static Intent getIntent(Context ctx, String commId) {
        Intent it = new Intent(ctx, ActivityGoodsInfo.class);
        it.putExtra(AppConfig.IntentExtraKey.MEDICINE_INFO_ID, commId);
        return it;
    }

    @Event(value = {R.id.activity_goods_info_shop, R.id.goumai, R.id.add_cart, R.id.yao_ll, R.id.shopping_cart, R.id.kefu_call, R.id.ll_back,})
    private void onClick(View v) {
        Intent it;
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;

            case R.id.goumai:
                if (model != null) {
                    it = new Intent(this, ActivityGoodsSubmit.class);
                    it.putExtra(ActivityGoodsSubmit.TAG_INFO_SOURCE, ActivityGoodsInfo.VAL_INFO_SOURCE);

                    GoodSubmitModel1 goodSubmitModel = new GoodSubmitModel1();
                    GoodSubmitModel2 goodSubmitModel2 = new GoodSubmitModel2();
                    List<GoodSubmitModel3> goodList = new ArrayList<>();
                    List<GoodSubmitModel2> groupList = new ArrayList<>();
                    GoodSubmitModel3 goods = new GoodSubmitModel3();
                    goods.setEXCHANGE_QUANLITY(goodsInfo.checkInchModel.getNum());
                    goods.setCOMMODITY_COLOR(goodsInfo.checkInchModel.getCololr());
                    goods.setCOMMODITY_SIZE(goodsInfo.checkInchModel.getSize());
                    goods.setCOMMODITY_COLOR_ID(goodsInfo.checkInchModel.getColor_id());
                    goods.setCOMMODITY_SIZE_ID(goodsInfo.checkInchModel.getSize_id());
                    goods.setCOMMODITY_ID(model.getCommodityId());
                    goods.setCOMMODITY_NAME(model.getTitle());
                    goodList.add(goods);

                    goodSubmitModel2.setDETAILS(goodList);
                    goodSubmitModel2.setOID_GROUP_ID(model.getOidGroupId());
                    goodSubmitModel2.setOID_GROUP_NAME(model.getGroupName());

                    groupList.add(goodSubmitModel2);
                    goodSubmitModel.setDETAILS(groupList);

                    it.putExtra("data", GsonUtils.Bean2Json(goodSubmitModel));
                    startActivity(it);
                }
                break;

            case R.id.add_cart:
                if (model != null) {
                    ShoppingCartAddModel shoppingCartAddModel = new ShoppingCartAddModel();
                    shoppingCartAddModel.setCOMMODITY_ID(model.getCommodityId());
                    shoppingCartAddModel.setCOMMODITY_SIZE(goodsInfo.checkInchModel.getSize());
                    shoppingCartAddModel.setCOMMODITY_COLOR(goodsInfo.checkInchModel.getCololr());
                    shoppingCartAddModel.setEXCHANGE_QUANLITY(goodsInfo.checkInchModel.getNum() + "");
                    shoppingCartAddModel.setINS_USER_ID(model.getOidGroupId());
                    shoppingCartAddModel.setCOMMODITY_COLOR_ID(goodsInfo.checkInchModel.getColor_id());
                    shoppingCartAddModel.setCOMMODITY_SIZE_ID(goodsInfo.checkInchModel.getSize_id());
                    String str = GsonUtils.Bean2Json(shoppingCartAddModel);
                    addCart(str);
                }

                break;

            case R.id.yao_ll:
//                if (model != null && model.getPRESCRIPTION_FLG().equals( "1" )) {
//                    openChat( model.getWARENAME(), "", settingid2, groupName, true, model.getWAREID() );
//                }
                break;

            case R.id.activity_goods_info_shop:
                String groupId = model.getOidGroupId();
                if (StringUtils.isEmpty(groupId)) {
                    ToastUtil.show(this, "对不起，漂亮商铺页面还没完工。给程序员哥哥加油！");
                }
                it = ShopActivity.getIntent(this, groupId);
                startActivity(it);
                break;

            case R.id.kefu_call:
                if (StringUtils.isEmpty(settingid1)) {
                    ToastUtil.show(this, "抱歉，客服丢了！");
                }

//                addDb();
                openChat(model.getTitle(), "", settingid1, groupName, true, model.getCommodityId());


                break;

            case R.id.shopping_cart:
                it = new Intent(this, MainActivity.class);
                AppConfig.currSel = 3;
                it.putExtra(AppConfig.IntentExtraKey.BOTTOM_MENU_INDEX, AppConfig.currSel);
                finish();
                startActivity(it);
                break;

//            case R.id.yao_ll:
//                openChat(model.getWARENAME(),"",settingid1,groupName,true );
//                break;

        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvGoumai.setText(R.string.goumai);

        goodsInfo = new FragmentGoodsInfo();
        goodsDetail = new FragmentGoodsPagerDetails();
        goodsEvaluate = new FragmentGoodsPagerEvaluate();
        fragmentList.add(goodsInfo);
        fragmentList.add(goodsDetail);
        fragmentList.add(goodsEvaluate);

        vp_content.setAdapter(new ItemTitlePagerAdapter(getSupportFragmentManager(),
                fragmentList, new String[]{"商品", "详情", "评价"}));
        vp_content.setOffscreenPageLimit(3);
        psts_tabs.setViewPager(vp_content);

        getGoods();


    }


    BizDataAsyncTask<CommodityDetailsModel> medicineTask;

    private void getGoods() {
        setLoding(this, false);
        medicineTask = new BizDataAsyncTask<CommodityDetailsModel>() {
            @Override
            protected CommodityDetailsModel doExecute() throws ZYException, BizFailure {
                return GoodsBiz.getGoods(getIntent().getStringExtra(AppConfig.IntentExtraKey.MEDICINE_INFO_ID));
            }

            @Override
            protected void onExecuteSucceeded(CommodityDetailsModel commodityDetailsModel) {
                closeLoding();
                model = commodityDetailsModel;

                Log.e(AppConfig.ERR_TAG, "model:" + GsonUtils.Bean2Json(model));

                XiaoNengData xnd = model.getXiaonengData();
                if (xnd != null) {
                    sellerid = xnd.getSellerid();
                    settingid1 = xnd.getSettingid1();
                } else {
                    Log.e(AppConfig.ERR_EXCEPTION, "XiaoNengData is null");
                }
                groupName = model.getGroupName();

                goodsInfo.updeta(model);
                goodsEvaluate.update(model);
                goodsDetail.fragmentGoodsParameter.getmodel(model);
                goodsDetail.fragmentGoodsDetails.getmodel(model);


                //------商品浏览轨迹
//                trailparams = new TrailActionBody();
//
//                trailparams.ttl = commodityDetailsModel.getTitle();
//                trailparams.url = "";
//                trailparams.sellerid = sellerid;
//                trailparams.ref = "";
//                trailparams.orderid = orderid;
//                trailparams.orderprice = orderprice;
//                if (StringUtils.isEmpty( PreferenceCache.getToken() )) {//是否登陆来判断会员vip
//                    isvip = 0;
//                    userlevel = 0;
//                } else {
//                    isvip = 1;
//                    userlevel = 1;
//                }
//                trailparams.isvip = isvip;
//                trailparams.userlevel = userlevel;
//
//                JSONObject item = MyUtil.getRequiredNtalkerParams( sellerid, commodityDetailsModel.getWAREID(), medicineDetailsModel.getWARENAME(),
//                        "", "" );
//
//                JSONObject itemOptional = MyUtil.getOptionalNtalkerParams( item, medicineDetailsModel.getSALEPRICE() + "", medicineDetailsModel.getSALEPRICE() + "", null, medicineDetailsModel.getWARESPEC(), null, null, null );
//
//                JSONObject itemSelfDefine = MyUtil.getSelfDefineNtalkerParams( itemOptional );
//
//                String ntalkerparamStr = MyUtil.getNtalkerParam( itemSelfDefine );
//
//                trailparams.ntalkerparam = ntalkerparamStr; // 商品信息、购物车商品JSON字符串数据
//
//                int action = Ntalker.getInstance().startAction( trailparams );
//                Log.e( "action",action+";  "+MyUtil.getNtalkerParam(itemSelfDefine) );
                //-------
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        };
        medicineTask.execute();
    }

    //向下级fragment传递数据
    public CommodityDetailsModel getmodel() {
        return model;
    }


    //添加购物车
    BizDataAsyncTask<String> cartTask;

    private void addCart(final String str) {
        cartTask = new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                return GoodsBiz.shoppingCart(str, "create");
            }

            @Override
            protected void onExecuteSucceeded(String s) {
                ToastUtil.show(ActivityGoodsInfo.this, "添加购物车成功！");
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

}
