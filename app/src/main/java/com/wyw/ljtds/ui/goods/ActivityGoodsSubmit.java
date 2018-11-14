package com.wyw.ljtds.ui.goods;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alipay.PayResult;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.OrderSubmitAdapter;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.AppManager;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.model.AddressModel;
import com.wyw.ljtds.model.CreatOrderModel;
import com.wyw.ljtds.model.GoodSubmitModel1;
import com.wyw.ljtds.model.GoodsModel;
import com.wyw.ljtds.model.OrderCommDto;
import com.wyw.ljtds.model.OrderGroupDto;
import com.wyw.ljtds.model.OrderTrade;
import com.wyw.ljtds.model.OrderTradeDto;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.user.address.ActivityAddress;
import com.wyw.ljtds.ui.user.address.ActivityAddressEdit;
import com.wyw.ljtds.ui.user.address.AddressActivity;
import com.wyw.ljtds.ui.user.order.ActivityOrder;
import com.wyw.ljtds.ui.user.order.WXShare4OrderDialogFragment;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.PayUtil;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.MyCallback;
import com.wyw.ljtds.widget.PayDialog;
import com.wyw.ljtds.wxapi.WXEntryActivity;

import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.xiaoneng.uiapi.Ntalker;

import static com.wyw.ljtds.ui.goods.ActivityGoodsSubmitBill.TAG_ORDER;
import static com.wyw.ljtds.utils.PayUtil.ALI_PAY;


/**
 * Created by Administrator on 2017/1/24 0024.
 * order submit
 */
@ContentView(R.layout.activity_order_submit)
public class ActivityGoodsSubmit extends BaseActivity {
    private static final int REQUEST_SETTING = 0; //设置支付方式
    private static final int REQUEST_FAPIAO = 1; //设置发票
    private static final int REQUEST_SELECT_ADDRESS = 2; //选择配送地址
    private static final int REQUEST_ADD_ADDRESS = 3; //添加配送地址
    private static final int REQUEST_WXSHARE = 4; //显示微信分享

    private static final String TAG_SELLERID = "com.wyw.ljtds.ui.goods.tag_sellerid";
    private static final String TAG_ORDER_DATA = "com.wyw.ljtds.ui.goods.ActivityGoodsSubmit.TAG_ORDER_DATA";
    private static final String TAG_ZHIGOU_FLG = "com.wyw.ljtds.ui.goods.ActivityGoodsSubmit.TAG_ZHIGOU_FLG";

    private static final String DIALOG_WXSHARE = "DIALOG_WXSHARE";

    boolean hasShow = false;

    @ViewInject(R.id.header_return_text)
    private TextView title;
    @ViewInject(R.id.group)
    private RecyclerView recyclerView;//gr order
    @ViewInject(R.id.activity_order_submit_money)
    private TextView money_all;
    @ViewInject(R.id.activity_order_submit_money_discount)
    private TextView moneyAllDiscount;


    private OrderSubmitAdapter adapter;
    private PayModel payModel = new PayModel();//在线支付所需参数封装java类

    private CreatOrderModel cOrderModel;
    private boolean isResult = false;
    //小能
    String sellerid = "";
    private UserBiz bizUser;
    /**
     * 微信分享使用的数据
     */
    private String wxTitle;
    private String wxDesc;
    private String wxImgUrl;
    private String wxUrl;


    @Event(value = {R.id.header_return, R.id.submit})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_return:
                finish();
                break;

            case R.id.submit:
                if (cOrderModel == null) {
                    ToastUtil.show(this, "抱歉,生成订单失败");
                    return;
                }
                final AlertDialog alert = new AlertDialog.Builder(this).create();
                alert.setTitle(R.string.alert_tishi);
                alert.setMessage(getResources().getString(R.string.confirm_order_submit));
                alert.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.alert_queding), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        view2Data();
                        submitOrder(GsonUtils.Bean2Json(cOrderModel), "create");
                    }
                });
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.alert_quxiao), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alert.dismiss();
                    }
                });
                alert.show();

                break;

        }
    }

    private void view2Data() {
        if (cOrderModel == null) return;
        for (OrderGroupDto group : cOrderModel.getDETAILS()) {
            if ("1".equals(cOrderModel.getINVOICE_FLG())) {
                group.setINVOICE_FLG(cOrderModel.getINVOICE_FLG());
                group.setINVOICE_TYPE(cOrderModel.getINVOICE_TYPE());
                group.setINVOICE_TITLE(cOrderModel.getINVOICE_TITLE());
                group.setINVOICE_CONTENT(cOrderModel.getINVOICE_CONTENT());
                group.setINVOICE_TAX(cOrderModel.getINVOICE_TAX());
            }

            group.setDISTRIBUTION_MODE(cOrderModel.getDISTRIBUTION_MODE());
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bizUser = UserBiz.getInstance(this);
        //注册广播接收器
        AppManager.addDestoryActivity(this, "submit");

        initView();

        sellerid = getIntent().getStringExtra(TAG_SELLERID);

        initDataFromServer();
    }

    private void initView() {
        title.setText(R.string.order_queren);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 从服务器加载订单数据
     */
    private void initDataFromServer() {
        Intent it = getIntent();
        final String data = it.getStringExtra(TAG_ORDER_DATA);
        if (!StringUtils.isEmpty(data)) {
            /*setLoding(this, false);
            new BizDataAsyncTask<CreatOrderModel>() {
                @Override
                protected CreatOrderModel doExecute() throws ZYException, BizFailure {
                    CreatOrderModel rlt = GoodsBiz.getOrderShow(data, "showOrder");
                    return rlt;
                }

                @Override
                protected void onExecuteSucceeded(CreatOrderModel creatOrderModel) {
                    closeLoding();
                    cOrderModel = creatOrderModel;
                    for(OrderGroupDto g : cOrderModel.getDETAILS()){
                        //全部默认选中
                        g.setCOIN_FLG("1");
                        g.setPOSTAGE_FLG("1");
                        g.setPREFERENTIAL_FLG("1");
                    }
                    showOrder(GsonUtils.Bean2Json(cOrderModel), "changeOrder");
                }

                @Override
                protected void OnExecuteFailed() {
                    finish();
                    closeLoding();
                }
            }.execute();*/
            showOrder(data, "showOrder");
        }
    }

    private void refreshData() {
        if (cOrderModel == null) return;
        //判断是否进行了微信分享
        String wxShareRlt = PreferenceCache.getWXShareResult();
        if (!StringUtils.isEmpty(wxShareRlt)) {
            //清除微信分享标识，防止重复使用
            PreferenceCache.putWXShareResult("");
            if (WXEntryActivity.SHARE_RESULT_OK.equals(wxShareRlt)) {
                cOrderModel.setSHARE_FLG(OrderTradeDto.SHARE_FLG_OK);
            }
        }
        showOrder(GsonUtils.Bean2Json(cOrderModel), "changeOrder");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isResult) {
            refreshData();
        }
        isResult = false;

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void submitOrder(final String str, final String op) {
        setLoding(ActivityGoodsSubmit.this, false);
        new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                return GoodsBiz.submitOrder(str, op);
            }

            @Override
            protected void onExecuteSucceeded(final String tradeOrderId) {
                closeLoding();

                //集成小能下单数量功能
                // 订单页轨迹标准接口 * @param title 订单页的标题, 必传字段 * @param url 订单页的url, 必传字段，需要保证每个订单页的url唯一 * @param sellerid 商户id, B2C企业传空, B2B企业需要传入商户的id * @param ref 上一页url, 如没有可传空 * @param orderid 订单id, 必传字段 * @param orderprice 订单价格, 必传字段 */
                String url = "http://www.lanjiutian.com/user/medicine/orderDetail.html?&wareid=002204&paramOrderGroupId=" + tradeOrderId;
                Ntalker.getBaseInstance().startAction_order("蓝九天电商", url, sellerid, "", tradeOrderId, cOrderModel.getPAY_AMOUNT());

                if (OrderTrade.PAYMTD_MONEY.equals(cOrderModel.getPAYMENT_METHOD())) {
                    ActivityGoodsSubmit.this.goOrderList();
                } else {
                    OrderTrade orderTrade = new OrderTrade();
                    orderTrade.setPayAmount(cOrderModel.getPAY_AMOUNT());
                    PayDialog.showDialog(ActivityGoodsSubmit.this, orderTrade, new PayDialog.PayMethodSelectedListener() {
                        @Override
                        public void onItemSelected(OrderTrade order) {
//                            Log.e(AppConfig.ERR_TAG, order.getPaymentMethod());
                            payModel.setPAYMENT_METHOD(order.getPaymentMethod());
                            payModel.setORDER_TRADE_ID(tradeOrderId);
                            payModel.setIp(Utils.getIPAddress(ActivityGoodsSubmit.this));
                            payModel.setDevice(Utils.getImei(ActivityGoodsSubmit.this));
                            goPay(GsonUtils.Bean2Json(payModel));
                        }

                        @Override
                        public void onDialogClose() {
                            ActivityGoodsSubmit.this.toOrder();
                        }
                    });
                }
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    /**
     * 进入 rlist
     */
    private void goOrderList() {
        Intent it = new Intent(ActivityGoodsSubmit.this, ActivityOrder.class);
        finish();
        startActivity(it);
    }

    /**
     * 显示订单
     *
     * @param str
     * @param op
     */
    private void showOrder(final String str, final String op) {
        Utils.log("showOrder:" + str);
        setLoding(this, false);
        new BizDataAsyncTask<CreatOrderModel>() {
            @Override
            protected CreatOrderModel doExecute() throws ZYException, BizFailure {
                CreatOrderModel rlt = GoodsBiz.getOrderShow(str, op);
                return rlt;
            }

            @Override
            protected void onExecuteSucceeded(CreatOrderModel creatOrderModel) {
                closeLoding();
                cOrderModel = creatOrderModel;

                String zhigouFlg = getIntent().getStringExtra(TAG_ZHIGOU_FLG);
                if (GoodsModel.TOP_FLG_ZHIJIEGOU.equals(zhigouFlg)) {
                    //直接购买的默认为门店自取
                    cOrderModel.setDISTRIBUTION_MODE("1");
                }

                bindData2View();
            }

            @Override
            protected void OnExecuteFailed() {
                finish();
                closeLoding();
            }
        }.execute();
    }

    /**
     * 将数据显示到界面视图
     */
    private void bindData2View() {
        if (cOrderModel == null) return;

        adapter = new OrderSubmitAdapter(this, cOrderModel);
        //设置点击事件监听
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it;
                Utils.log("setOnClickListener " + v.getClass().getName() + " --" + v.getId());
                switch (v.getId()) {
                    case R.id.item_order_submit_bottom_invoice:
                        it = ActivityGoodsSubmitBill.getIntent(ActivityGoodsSubmit.this, cOrderModel);
                        startActivityForResult(it, REQUEST_FAPIAO);
                        break;
                    case R.id.item_order_submit_bottom_paymethod:
                        String zhigouFlg = getIntent().getStringExtra(TAG_ZHIGOU_FLG);
                        it = ActivityGoodsSubmitChoice.getIntent(ActivityGoodsSubmit.this, cOrderModel.getDISTRIBUTION_MODE(), cOrderModel.getPAYMENT_METHOD(), cOrderModel.getINS_USER_ID(), zhigouFlg);
                        startActivityForResult(it, REQUEST_SETTING);
                        break;
                    case R.id.item_order_submit_address_ll_container:
                        it = AddressActivity.getIntent(ActivityGoodsSubmit.this, true);
                        ActivityGoodsSubmit.this.startActivityForResult(it, REQUEST_SELECT_ADDRESS);
                        break;
                    default:
                        break;
                }

            }
        });

        //店铺优惠选择后进行服务器数据计算和加载
        adapter.setCompleteDiscountCallback(new MyCallback() {
            @Override
            public void callback(Object... params) {
                showOrder(GsonUtils.Bean2Json(cOrderModel), "changeOrder");
            }
        });
        recyclerView.setAdapter(adapter);


        //显示整个订单的费用情况
        String postageInfo = "运费￥" + Utils.formatFee(cOrderModel.getPOSTAGE_ALL()) + "";
        if ("1".equals(cOrderModel.getPOSTAGE_FLG())) {
            postageInfo += "已抵扣";
        }
        postageInfo = "(" + postageInfo + ")";
        money_all.setText("合计: ￥" + Utils.formatFee(cOrderModel.getPAY_AMOUNT()) + postageInfo);
        moneyAllDiscount.setText(cOrderModel.getDEDUCTIBLE_ALL_MONEY());

        // 分享订单获取优惠的功能
        if (needShowShareDialog()) {
            showShareDialog();
        }

    }


    private void showShareDialog() {
        WXShare4OrderDialogFragment frag = WXShare4OrderDialogFragment.newInstance();
        frag.setMyCallback(new MyCallback() {
            @Override
            public void callback(Object... params) {
                hasShow = true;
                if (params == null || params.length <= 0) return;
                boolean rlt = (boolean) params[0];
                if (rlt) {
                    openWXShareSelDialog();
                }
            }
        });
        frag.show(this.getSupportFragmentManager(), DIALOG_WXSHARE);
    }

    private void openWXShareSelDialog() {
        Utils.log("WX Share" + wxTitle + "-" + wxDesc + "-" + wxImgUrl + "-" + wxUrl);
        Utils.wechatShare(this, wxTitle, wxDesc, wxImgUrl, wxUrl);
    }

    private boolean needShowShareDialog() {
        //是否有80 标识 的商品
        boolean hasFlg = false;
        if (cOrderModel == null) return false;
        for (OrderGroupDto groupItem : cOrderModel.getDETAILS()) {
            if (hasFlg) break;
            if (groupItem == null) return false;
            List<OrderCommDto> goodsList = groupItem.getDETAILS();
            if (goodsList == null) return false;
            for (OrderCommDto goods : goodsList) {
                if (hasFlg) break;
                if (OrderCommDto.SHARE_FLG_YES.equals(goods.getSHARE_FLG())) {
                    wxTitle = goods.getSHARE_TITLE();
                    wxDesc = goods.getSHARE_DESCRIPTION();
                    wxImgUrl = goods.getIMG_PATH();
//                    wxUrl = AppConfig.WEB_APP_URL + "/lifeDetail.html?commodityId=" + goods.getCOMMODITY_ID();
                    wxUrl = AppConfig.WEB_APP_URL;

                    hasFlg = true;
                    break;
                }
            }
        }
        return !hasShow && hasFlg;
    }

    /**
     * 进行支付
     *
     * @param data online pay data
     */
    private void goPay(final String data) {
        setLoding(ActivityGoodsSubmit.this, false);
        new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                return GoodsBiz.onlinePay(data);
            }

            @Override
            protected void onExecuteSucceeded(String rltData) {
                closeLoding();
                Map dataMap;
                Map daraResult = null;
                try {
                    dataMap = GsonUtils.Json2Bean(rltData, HashMap.class);
                    daraResult = (Map) dataMap.get("result");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ToastUtil.show(ActivityGoodsSubmit.this, "服务器数据格式有错误");
                }
                PayUtil payUtil = Utils.getPayUtilInstance(ActivityGoodsSubmit.this, daraResult);
                if (payModel.getPAYMENT_METHOD().equals(OrderTrade.PAYMTD_WECHAT)) {
                    payUtil.pay4Wechat();
                } else if (payModel.getPAYMENT_METHOD().equals(OrderTrade.PAYMTD_ALI)) {
                    payUtil.pay4Ali(mHandler);
                } else if (payModel.getPAYMENT_METHOD().equals(OrderTrade.PAYMTD_UNION)) {
                    payUtil.pay4Union();
                } else {
                }

            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }


    //更改数据  刷新页面
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isResult = true;
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
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
                            cOrderModel.setADDRESS_ID(addrId + "");
                            cOrderModel.setLAT(addr.getLAT());
                            cOrderModel.setLNG(addr.getLNG());
                            String str = GsonUtils.Bean2Json(cOrderModel);
                            showOrder(str, "changeOrder");
                        }

                        @Override
                        protected void OnExecuteFailed() {
                            closeLoding();
                        }
                    }.execute();

                    break;
                case REQUEST_SELECT_ADDRESS:
                    String cmd = data.getStringExtra(AddressActivity.TAG_CMD);
                    if (AddressActivity.CMD_CREATE.equals(cmd)) {
                        startActivityForResult(ActivityAddressEdit.getIntent(this, null), REQUEST_ADD_ADDRESS);
                    } else {
                        Parcelable addr = data.getParcelableExtra(ActivityAddress.TAG_SELECTED_ADDRESS);
                        if (addr != null) {
                            AddressModel addrM = (AddressModel) addr;
                            int addrId = addrM.getADDRESS_ID();
                            cOrderModel.setADDRESS_ID(addrId + "");
                            cOrderModel.setLAT(addrM.getLAT());
                            cOrderModel.setLNG(addrM.getLNG());
                            String str = GsonUtils.Bean2Json(cOrderModel);
                            showOrder(str, "changeOrder");
                        }
                    }
                    break;
                case REQUEST_SETTING:
                    String zhifu_s = data.getStringExtra(ActivityGoodsSubmitChoice.TAG_ZHIFU);
                    cOrderModel.setPAYMENT_METHOD(zhifu_s);
                    String peisong = data.getStringExtra(ActivityGoodsSubmitChoice.TAG_PEISONG);
                    cOrderModel.setDISTRIBUTION_MODE(peisong);
                    String dateStart = data.getStringExtra(ActivityGoodsSubmitChoice.TAG_IIME_START);
                    String dateEnd = data.getStringExtra(ActivityGoodsSubmitChoice.TAG_IIME_END);
                    cOrderModel.setDISTRIBUTION_DATE_START(dateStart);
                    cOrderModel.setDISTRIBUTION_DATE_END(dateEnd);
                    if (cOrderModel.getDETAILS() != null && cOrderModel.getDETAILS().size() > 0) {
                        for (OrderGroupDto group : cOrderModel.getDETAILS()) {
                            group.setDISTRIBUTION_MODE(cOrderModel.getDISTRIBUTION_MODE());
                        }
                    }
                    showOrder(GsonUtils.Bean2Json(cOrderModel), "changeOrder");
                    break;
                case REQUEST_WXSHARE:
                    break;
            }
        } else if (resultCode == AppConfig.IntentExtraKey.RESULT_OK) {
            switch (requestCode) {

                case REQUEST_FAPIAO:
                    //配送信息 和 发票信息
                    CreatOrderModel fapiaoModel = data.getParcelableExtra(TAG_ORDER);

                    if (!"0".equals(fapiaoModel.getINVOICE_FLG())) {
                        //如果开发票
                        cOrderModel.setINVOICE_FLG(fapiaoModel.getINVOICE_FLG());
                        cOrderModel.setINVOICE_TYPE(fapiaoModel.getINVOICE_TYPE());
                        cOrderModel.setINVOICE_TITLE(fapiaoModel.getINVOICE_TITLE());
                        cOrderModel.setINVOICE_CONTENT(fapiaoModel.getINVOICE_CONTENT());
                        cOrderModel.setINVOICE_ORG(fapiaoModel.getINVOICE_ORG());
                        cOrderModel.setINVOICE_TAX(fapiaoModel.getINVOICE_TAX());
                        for (OrderGroupDto groupItem : cOrderModel.getDETAILS()) {
                            groupItem.setINVOICE_FLG(fapiaoModel.getINVOICE_FLG());
                            groupItem.setINVOICE_ORG(fapiaoModel.getINVOICE_ORG());
                            groupItem.setINVOICE_TYPE(fapiaoModel.getINVOICE_TYPE());
                            groupItem.setINVOICE_TITLE(fapiaoModel.getINVOICE_TITLE());
                            groupItem.setINVOICE_CONTENT(fapiaoModel.getINVOICE_CONTENT());
                            groupItem.setINVOICE_TAX(fapiaoModel.getINVOICE_TAX());
                        }
                    } else {
                        //如果not开发票
                        cOrderModel.setINVOICE_FLG(fapiaoModel.getINVOICE_FLG());
                        cOrderModel.setINVOICE_TYPE("");
                        cOrderModel.setINVOICE_TITLE("");
                        cOrderModel.setINVOICE_CONTENT("");
                        cOrderModel.setINVOICE_TAX("");
                        for (OrderGroupDto groupItem : cOrderModel.getDETAILS()) {
                            groupItem.setINVOICE_FLG("");
                            groupItem.setINVOICE_TYPE("");
                            groupItem.setINVOICE_TITLE("");
                            groupItem.setINVOICE_CONTENT("");
                            groupItem.setINVOICE_TAX("");
                        }
                    }
                    bindData2View();
                    break;
            }
        } else {
            if (requestCode == 10) {
                //支付宝支付回调
                Log.e(AppConfig.ERR_TAG, "requestCode.......10");
                String pay_result = data.getExtras().getString("pay_result");
                if (pay_result.equalsIgnoreCase("success")) {
                    // 支付成功后，extra中如果存在result_data，取出校验
                    // result_data结构见c）result_data参数说明
                    String result = "";
                    if (data.hasExtra("result_data")) {
//                        for (String k : data.getExtras().keySet()) {
//                            Object v = data.getExtras().get(k);
//                            Log.e(AppConfig.ERR_TAG, String.valueOf(v));
//                        }
                        result = data.getExtras().getString("result_data");
                        try {
                            JSONObject resultJson = new JSONObject(result);
                            String sign = resultJson.getString("sign");
                            String dataOrg = resultJson.getString("data");
                            // 验签证书同后台验签证书
                            // 此处的verify，商户需送去商户后台做验签
                            boolean ret = Utils.verify(dataOrg, sign, AppConfig.UPPAY_MODE);
                            if (ret) {
                                Log.e(AppConfig.ERR_TAG, "payresult .....1:");
                                // 验证通过后，显示支付结果
                                ToastUtil.show(ActivityGoodsSubmit.this, getResources().getString(R.string.pay_success));
                            } else {
                                // 验证不通过后的处理
                                // 建议通过商户后台查询支付结果
                                ToastUtil.show(ActivityGoodsSubmit.this, getResources().getString(R.string.pay_fail));
                            }
                        } catch (Exception e) {
                            Log.e(AppConfig.ERR_TAG, "payresult:" + e.getMessage());
                        }
                    } else {
                        // 未收到签名信息
                        // 建议通过商户后台查询支付结果
                        ToastUtil.show(ActivityGoodsSubmit.this, getResources().getString(R.string.pay_success));
                    }

                    if (!StringUtils.isEmpty(result)) {
                        PayUtil.UnionResult unionResult = GsonUtils.Json2Bean(result, PayUtil.UnionResult.class);
                        unionResult.setORDER_TRADE_ID(payModel.getORDER_TRADE_ID());
                        String strUnionResult = GsonUtils.Bean2Json(unionResult);
                        sendResult(strUnionResult);
                    }

                } else if (pay_result.equalsIgnoreCase("fail")) {
                    ToastUtil.show(ActivityGoodsSubmit.this, getResources().getString(R.string.pay_fail));
                    toOrder();
                } else if (pay_result.equalsIgnoreCase("cancel")) {
                    ToastUtil.show(ActivityGoodsSubmit.this, getResources().getString(R.string.pay_cancel));
                    toOrder();
                }
            }
        }
    }


    //支付结束结果
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ALI_PAY:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ToastUtil.show(ActivityGoodsSubmit.this, ActivityGoodsSubmit.this.getResources().getString(R.string.pay_success));
                        Log.e(AppConfig.ERR_TAG, "alipay result....:" + resultInfo);
                        if (!StringUtils.isEmpty(resultInfo)) {
                            PayUtil.AliResult aliResult = GsonUtils.Json2Bean(resultInfo, PayUtil.AliResult.class);
                            aliResult.setORDER_TRADE_ID(payModel.getORDER_TRADE_ID());

                            String json = GsonUtils.Bean2Json(aliResult);
                            sendResult(json);
                        }

                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtil.show(ActivityGoodsSubmit.this, getResources().getString(R.string.pay_fail));
                        toOrder();
                    }


                    break;
            }

        }


    };

    BizDataAsyncTask<String> payResultTask;

    private void sendResult(final String data) {
        payResultTask = new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                Log.e(AppConfig.ERR_TAG, "alipay send result....:" + data);
                return GoodsBiz.onlinePayResult(data);
            }

            @Override
            protected void onExecuteSucceeded(String s) {
                toOrder();
            }

            @Override
            protected void OnExecuteFailed() {

            }
        };
        payResultTask.execute();
    }


    //提交在线支付的订单
    public class PayModel {
        private String ORDER_TRADE_ID;
        private String PAYMENT_METHOD;
        private String device;
        private String ip;

        public String getDevice() {
            return device;
        }

        public void setDevice(String device) {
            this.device = device;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getORDER_TRADE_ID() {
            return ORDER_TRADE_ID;
        }

        public void setORDER_TRADE_ID(String ORDER_TRADE_ID) {
            this.ORDER_TRADE_ID = ORDER_TRADE_ID;
        }

        public String getPAYMENT_METHOD() {
            return PAYMENT_METHOD;
        }

        public void setPAYMENT_METHOD(String PAYMENT_METHOD) {
            this.PAYMENT_METHOD = PAYMENT_METHOD;
        }
    }


    //跳转到我的订单
    private void toOrder() {
        Log.e(AppConfig.ERR_TAG, ".........toOrder");
        Intent it = new Intent(ActivityGoodsSubmit.this, ActivityOrder.class);
        finish();
        startActivity(it);
    }

    public static Intent getIntent(Context context, GoodSubmitModel1 goodSubmitModel, String sellerid) {
        Intent it = new Intent(context, ActivityGoodsSubmit.class);
        it.putExtra("data", GsonUtils.Bean2Json(goodSubmitModel));
        it.putExtra(TAG_SELLERID, GsonUtils.Bean2Json(goodSubmitModel));
        return it;
    }


    public static Intent getIntent(Context context, OrderTradeDto order) {
        Intent it = new Intent(context, ActivityGoodsSubmit.class);
        it.putExtra(TAG_ORDER_DATA, GsonUtils.Bean2Json(order));
        it.putExtra(TAG_SELLERID, GsonUtils.Bean2Json(order));
        return it;
    }

    public static Intent getIntent(Context context, OrderTradeDto order, String zhigouFlg) {
        Intent it = ActivityGoodsSubmit.getIntent(context, order);
        it.putExtra(TAG_ZHIGOU_FLG, zhigouFlg);
        return it;
    }


}

