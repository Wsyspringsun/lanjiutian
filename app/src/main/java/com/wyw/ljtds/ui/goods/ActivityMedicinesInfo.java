package com.wyw.ljtds.ui.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
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
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.model.GoodSubmitModel1;
import com.wyw.ljtds.model.GoodSubmitModel2;
import com.wyw.ljtds.model.GoodSubmitModel3;
import com.wyw.ljtds.model.MedicineDetailsModel;
import com.wyw.ljtds.model.ShoppingCartAddModel;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.widget.goodsinfo.NoScrollViewPager;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import java.util.ArrayList;
import java.util.List;
import cn.xiaoneng.coreapi.TrailActionBody;
import cn.xiaoneng.uiapi.Ntalker;
import cn.xiaoneng.utils.MyUtil;

/**
 * Created by Administrator on 2016/12/26 0026.
 */
@ContentView(R.layout.activity_goods_info)
public class ActivityMedicinesInfo extends BaseActivity {
    public static String VAL_INFO_SOURCE="ActivityMedicinesInfo";
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


    //客服
    String groupName = "蓝九天医师";// 客服组默认名称
    String settingid2 = "lj_1000_1495596285901";
    TrailActionBody trailparams = null;// 轨迹信息实例
    // 轨迹（专有参数）
    String sellerid = "lj_1001";// 商户id,平台版企业(B2B2C企业)使用此参数，B2C企业此参数传""
    String ttl = "";// 当前页标题，如首页、购物车、订单、支付
    String url = "";// 当前页地址
    String ref = "";// 前一页面的地址
    String orderid = "";// 订单id
    String orderprice = "";// 订单价格
    int isvip = 0;// 是否为vip会员
    int userlevel = 1;// 用户级别,级别分为1-5,5为最高级,默认为0 非会员


    public MedicineDetailsModel model;
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentMedcinesInfo goodsInfo;
    private FragmentGoodsPagerDetails goodsDetail;
    private FragmentGoodsPagerEvaluate goodsEvaluate;

    @Event(value = {R.id.goumai, R.id.add_cart, R.id.yao_ll, R.id.shopping_cart, R.id.kefu_call, R.id.ll_back,})
    private void onClick(View v) {
        Intent it;
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;

            case R.id.goumai:
                if (model != null && !model.getPRESCRIPTION_FLG().equals( "1" )) {
                    it = new Intent( this, ActivityGoodsSubmit.class );

                    it.putExtra(ActivityGoodsSubmit.TAG_INFO_SOURCE,ActivityMedicinesInfo.VAL_INFO_SOURCE);

                    GoodSubmitModel1 goodSubmitModel = new GoodSubmitModel1();
                    GoodSubmitModel2 goodSubmitModel2 = new GoodSubmitModel2();
                    List<GoodSubmitModel3> goodList = new ArrayList<>();
                    List<GoodSubmitModel2> groupList = new ArrayList<>();
                    GoodSubmitModel3 goods = new GoodSubmitModel3();
                    goods.setEXCHANGE_QUANLITY( goodsInfo.numberButton.getNumber() );
                    goods.setCOMMODITY_COLOR( model.getPROD_ADD() );
                    goods.setCOMMODITY_SIZE( model.getWARESPEC() );
                    goods.setCOMMODITY_ID( model.getWAREID() );
                    goods.setCOMMODITY_NAME( model.getWARENAME() );
                    goodList.add( goods );

                    goodSubmitModel2.setDETAILS( goodList );
                    goodSubmitModel2.setOID_GROUP_ID( model.getGROUPID() );
                    goodSubmitModel2.setOID_GROUP_NAME( model.getGROUPNAME() );

                    groupList.add( goodSubmitModel2 );
                    goodSubmitModel.setDETAILS( groupList );

                    it.putExtra( "data", GsonUtils.Bean2Json( goodSubmitModel ) );
                    startActivity( it );
                }
                break;

            case R.id.add_cart:
                if (model != null && !model.getPRESCRIPTION_FLG().equals( "1" )) {
                    ShoppingCartAddModel shoppingCartAddModel = new ShoppingCartAddModel();
                    shoppingCartAddModel.setCOMMODITY_ID( model.getWAREID() );
                    shoppingCartAddModel.setCOMMODITY_SIZE( model.getWARESPEC() );
                    shoppingCartAddModel.setCOMMODITY_COLOR( model.getPROD_ADD() );
                    shoppingCartAddModel.setEXCHANGE_QUANLITY( goodsInfo.numberButton.getNumber() + "" );
                    shoppingCartAddModel.setINS_USER_ID( model.getGROUPID() );
                    String str = GsonUtils.Bean2Json( shoppingCartAddModel );
                    addCart( str );
                }
                break;

            case R.id.yao_ll:
                if (model != null && model.getPRESCRIPTION_FLG().equals( "1" )) {
                    openChat( model.getWARENAME(), "", settingid2, groupName, true, model.getWAREID() );
                }
                break;

            case R.id.kefu_call:

//                addDb();
                if (model != null && !model.getPRESCRIPTION_FLG().equals( "1" )) {
                    openChat( model.getWARENAME(), "", settingid2, groupName, true, model.getWAREID() );
                }


                break;

            case R.id.shopping_cart:
                it = new Intent( this, MainActivity.class );
                AppConfig.currSel = 3;
                it.putExtra( AppConfig.IntentExtraKey.BOTTOM_MENU_INDEX, AppConfig.currSel );
                finish();
                startActivity( it );
                break;

//            case R.id.yao_ll:
//                openChat(model.getWARENAME(),"",settingid1,groupName,true );
//                break;

        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        goodsInfo = new FragmentMedcinesInfo();
        goodsDetail = new FragmentGoodsPagerDetails();
        goodsEvaluate = new FragmentGoodsPagerEvaluate();
        fragmentList.add( goodsInfo );
        fragmentList.add( goodsDetail );
        fragmentList.add( goodsEvaluate );

        vp_content.setAdapter( new ItemTitlePagerAdapter( getSupportFragmentManager(),
                fragmentList, new String[]{"商品", "详情", "评价"} ) );
        vp_content.setOffscreenPageLimit( 3 );
        psts_tabs.setViewPager( vp_content );


        setLoding( this, false );
//        getDeatlis();
        getMedicine();


    }


    BizDataAsyncTask<MedicineDetailsModel> medicineTask;

    private void getMedicine() {
        medicineTask = new BizDataAsyncTask<MedicineDetailsModel>() {
            @Override
            protected MedicineDetailsModel doExecute() throws ZYException, BizFailure {
                Log.e( "goods_id=", getIntent().getStringExtra( AppConfig.IntentExtraKey.MEDICINE_INFO_ID ) );
                return GoodsBiz.getMedicine( getIntent().getStringExtra( AppConfig.IntentExtraKey.MEDICINE_INFO_ID ) );
//                return GoodsBiz.getMedicine( "000000" );
            }

            @Override
            protected void onExecuteSucceeded(MedicineDetailsModel medicineDetailsModel) {

                model = medicineDetailsModel;
                goodsInfo.updeta( model );
                goodsEvaluate.update( model );
                goodsDetail.fragmentGoodsParameter.getmodel( model );
                goodsDetail.fragmentGoodsDetails.getmodel( model );
                if (model.getPRESCRIPTION_FLG().equals( "1" )) {
                    yao_ll.setVisibility( View.VISIBLE );
                    shang_ll.setVisibility( View.GONE );
                    kefu_call.setVisibility( View.GONE );
                } else {
                    yao_ll.setVisibility( View.GONE );
                    shang_ll.setVisibility( View.VISIBLE );
                    kefu_call.setVisibility( View.VISIBLE );
                }


                //------商品浏览轨迹
                trailparams = new TrailActionBody();

                trailparams.ttl = medicineDetailsModel.getWARENAME();
                trailparams.url = "";
                trailparams.sellerid = sellerid;
                trailparams.ref = "";
                trailparams.orderid = orderid;
                trailparams.orderprice = orderprice;
                if (StringUtils.isEmpty( PreferenceCache.getToken() )) {//是否登陆来判断会员vip
                    isvip = 0;
                    userlevel = 0;
                } else {
                    isvip = 1;
                    userlevel = 1;
                }
                trailparams.isvip = isvip;
                trailparams.userlevel = userlevel;

                JSONObject item = MyUtil.getRequiredNtalkerParams( sellerid, medicineDetailsModel.getWAREID(), medicineDetailsModel.getWARENAME(),
                        "", "" );

                JSONObject itemOptional = MyUtil.getOptionalNtalkerParams( item, medicineDetailsModel.getSALEPRICE() + "", medicineDetailsModel.getSALEPRICE() + "", null, medicineDetailsModel.getWARESPEC(), null, null, null );

                JSONObject itemSelfDefine = MyUtil.getSelfDefineNtalkerParams( itemOptional );

                String ntalkerparamStr = MyUtil.getNtalkerParam( itemSelfDefine );

                trailparams.ntalkerparam = ntalkerparamStr; // 商品信息、购物车商品JSON字符串数据

                int action = Ntalker.getInstance().startAction( trailparams );
//                Log.e( "action",action+";  "+MyUtil.getNtalkerParam(itemSelfDefine) );
                //-------
                closeLoding();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        };
        medicineTask.execute();
    }

    //向下级fragment传递数据
    public MedicineDetailsModel getmodel() {
        return model;
    }


    //添加购物车
    BizDataAsyncTask<String> cartTask;

    private void addCart(final String str) {
        cartTask = new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                Log.e( "************",str );
                return GoodsBiz.shoppingCart( str, "create" );
            }

            @Override
            protected void onExecuteSucceeded(String s) {
//                if (s.equals( "1" )){
                ToastUtil.show( ActivityMedicinesInfo.this, "添加购物车成功！" );
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

}
