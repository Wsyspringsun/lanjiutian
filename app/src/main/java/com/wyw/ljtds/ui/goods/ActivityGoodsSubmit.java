package com.wyw.ljtds.ui.goods;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.wyw.ljtds.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.PayResult;
import com.alipay.sdk.app.PayTask;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.unionpay.UPPayAssistEx;
import com.wyw.ljtds.adapter.CommOrderAdapter;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.biz.OrderBiz;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.AppManager;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.model.AddressModel;
import com.wyw.ljtds.model.Business;
import com.wyw.ljtds.model.CreatOrderModel;
import com.wyw.ljtds.model.GoodCreatModel1;
import com.wyw.ljtds.model.GoodCreatModel2;
import com.wyw.ljtds.model.GoodCreatModel3;
import com.wyw.ljtds.model.GoodSubmitModel1;
import com.wyw.ljtds.model.MedicineDetailsModel;
import com.wyw.ljtds.model.MyLocation;
import com.wyw.ljtds.model.OnlinePayModel;
import com.wyw.ljtds.model.OrderCommDto;
import com.wyw.ljtds.model.OrderGroupDto;
import com.wyw.ljtds.model.OrderTrade;
import com.wyw.ljtds.model.OrderTradeDto;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.user.address.ActivityAddress;
import com.wyw.ljtds.ui.user.address.ActivityAddressEdit;
import com.wyw.ljtds.ui.user.address.AddressActivity;
import com.wyw.ljtds.ui.user.order.ActivityOrder;
import com.wyw.ljtds.utils.DateUtils;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.MyCallback;
import com.wyw.ljtds.widget.PayDialog;
import com.wyw.ljtds.widget.dialog.DiscountDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.xiaoneng.uiapi.Ntalker;

import static com.wyw.ljtds.ui.goods.ActivityGoodsSubmitBill.TAG_ORDER;


/**
 * Created by Administrator on 2017/1/24 0024.
 * order submit
 */
@ContentView(R.layout.activity_order_submit)
public class ActivityGoodsSubmit extends BaseActivity {
    public IWXAPI wxApi;
    public static final String TAG_INFO_SOURCE = "com.wyw.ljtds.ui.goods.ActivityGoodsSubmit.TAG_INFO_SOURCE";
    private static final int REQUEST_SETTING = 0;
    private static final int REQUEST_FAPIAO = 1;
    private static final int REQUEST_SELECT_ADDRESS = 2;
    private static final String TAG_SELLERID = "com.wyw.ljtds.ui.goods.tag_sellerid";
    private static final String TAG_ORDER_DATA = "com.wyw.ljtds.ui.goods.ActivityGoodsSubmit.TAG_ORDER_DATA";
    private String flgInfoSrc = "";
    private String PAYMTD_C = "C";

    @ViewInject(R.id.header_return_text)
    private TextView title;
    @ViewInject(R.id.group)
    private RecyclerView recyclerView;//gr order
    @ViewInject(R.id.activity_order_submit_money)
    private TextView money_all;
    private EditText edCustomerNote;
    TextView tvAddrInfo;

    RelativeLayout rlInvoice;
    TextView tvInvoiceTitle;
    RelativeLayout rlZhifu;

    //    private GoodCreatModel1 model;
//    private List<Business> list_business;
    private OrderGroupAdapter adapter;
    private String fapiao_flg1 = "不开发票";
    private String fapiao_flg2 = "";
    private String fapiao_flg3 = "";
    private String fapiao_flg4 = "";
    private String address_id = "";
    private Bundle bundle;//地址信息  用于传递到地址编辑activity
    private int choice = 1;//记录在线支付  选中的是哪个选项  默认支付宝
    private PayModel payModel = new PayModel();//在线支付所需参数封装java类
    private ProgressDialog mLoadingDialog = null;

    private static final int ALI_PAY = 1;
    private static final int UP_PAY = 2;
    private String upJson = null;
    private String jsonUnionOrder;
    private CreatOrderModel cOrderModel;
    private boolean isUpdFooter;
    private BroadcastReceiver addrReciver;
    private CreatOrderModel.USER_ADDRESS addressInfo;
    private int REQUEST_ADDRESS_EDIT = 7;
    private boolean isResult = false;

    //小能
    String sellerid = "";
    private TextView tvInvoiceMore;
    private TextView tvInvoiceContent;
    private TextView tvZhifuTitle;
    private TextView tvZhifuMore;
    private TextView tvZhifuContent;


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
                        Utils.log("submit:" + GsonUtils.Bean2Json(cOrderModel));
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

    /**
     * 处理 地址为空 的情况
     *
     * @param intent
     */
    public void handleAddressExpire(Intent intent) {
        String action = intent.getAction();
        Log.e(AppConfig.ERR_TAG, ".....handleAddressExpire");
        if (action.equals(AppConfig.AppAction.ACTION_ADDRESS_EXPIRE)) {
            Log.e(AppConfig.ERR_TAG, ".....handleAddressExpire ACTION_ADDRESS_EXPIRE");
            setLoding(this, false);
            new BizDataAsyncTask<List<AddressModel>>() {
                @Override
                protected List<AddressModel> doExecute() throws ZYException, BizFailure {
                    return UserBiz.selectUserAddress();
                }

                @Override
                protected void onExecuteSucceeded(List<AddressModel> addressModels) {
                    closeLoding();
                    AddressModel defaultAdd = null;
                    if (addressModels == null || addressModels.size() <= 0) {
                    } else {
                        // 假定第一个元素为默认地址
                        defaultAdd = addressModels.get(0);
                        for (AddressModel addr : addressModels) {
                            if ("1".equals(addr.getDEFAULT_FLG())) {
                                //找到默认地址后结束循环
                                defaultAdd = addr;
                                break;
                            }
                        }
                    }

                    Intent it = ActivityAddressEdit.getIntent(ActivityGoodsSubmit.this, defaultAdd);
                    ActivityGoodsSubmit.this.startActivity(it);
                }

                @Override
                protected void OnExecuteFailed() {
                    closeLoding();
                    Intent it = ActivityAddressEdit.getIntent(ActivityGoodsSubmit.this, null);
                    ActivityGoodsSubmit.this.startActivity(it);
                }
            }.execute();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //注册广播接收器
        addrReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                handleAddressExpire(intent);
            }
        };

        AppManager.addDestoryActivity(this, "submit");

        initView();

        sellerid = getIntent().getStringExtra(TAG_SELLERID);

    }

    private void initView() {
        title.setText(R.string.order_queren);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new OrderGroupAdapter();
        adapter.addHeaderView(getHeaderView(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = AddressActivity.getIntent(ActivityGoodsSubmit.this, true);
                startActivityForResult(it, REQUEST_SELECT_ADDRESS);
            }
        }));
        adapter.addFooterView(getFooterView(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it;
                switch (view.getId()) {
                    case R.id.item_order_submit_bottom_invoice:
                        Log.e(AppConfig.ERR_TAG, "item_order_submit_bottom_invoice.......");
                        it = ActivityGoodsSubmitBill.getIntent(ActivityGoodsSubmit.this, adapter.getData().get(0));
                        startActivityForResult(it, REQUEST_FAPIAO);
                        break;
                    case R.id.item_order_submit_bottom_paymethod:
                        it = ActivityGoodsSubmitChoice.getIntent(ActivityGoodsSubmit.this);
//                        old
//                        it.putExtra(ActivityGoodsSubmit.TAG_INFO_SOURCE, ActivityGoodsSubmit.this.flgInfoSrc);
//                        it.putExtra(ActivityGoodsSubmitChoice.TAG_CREATE_ORDER_MODEL, json);
                        startActivityForResult(it, REQUEST_SETTING);
                        break;
                    default:
                        break;
                }

            }
        }));
        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
//                Log.e(AppConfig.ERR_TAG, "position:" + i);
//                switch (view.getId()) {
//                }
            }
        });

        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void loadData() {
        Intent it = getIntent();
//        flgInfoSrc = it.getStringExtra(TAG_INFO_SOURCE);

        String data = it.getStringExtra(TAG_ORDER_DATA);
//        String data = "{\"DETAILS\":[{\"DETAILS\":[{\"COMMODITY_COLOR\":\"百合康\",\"COMMODITY_ID\":\"005578\",\"COMMODITY_NAME\":\"\",\"COMMODITY_SIZE\":\"0.6克*60粒\",\"EXCHANGE_QUANLITY\":1}],\"LOGISTICS_COMPANY\":\"市区新市西街店\",\"LOGISTICS_COMPANY_ID\":\"002\"},{\"DETAILS\":[{\"COMMODITY_COLOR\":\"\",\"COMMODITY_ID\":\"000100\",\"COMMODITY_NAME\":\"\",\"COMMODITY_SIZE\":\"0.3g*24片*3盒\",\"EXCHANGE_QUANLITY\":1}],\"LOGISTICS_COMPANY\":\"市区观巷店\",\"LOGISTICS_COMPANY_ID\":\"040\"}],\"INS_USER_ID\":\"sxljt\",\"LAT\":\"35.489784\",\"LNG\":\"112.865275\",\"ORDER_SOURCE\":\"0\"}";
        Log.e(AppConfig.ERR_TAG, "orderData:" + data);
        if (!StringUtils.isEmpty(data))
            showOrder(data, "showOrder");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isResult) {
            loadData();
        }
        isResult = false;

        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConfig.AppAction.ACTION_ADDRESS_EXPIRE);//没有地址
        registerReceiver(addrReciver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(addrReciver);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * order data
     */
    /*private void loadOrderData() {
        setLoding(this, false);
        new BizDataAsyncTask<OrderTradeDto>() {
            @Override
            protected OrderTradeDto doExecute() throws ZYException, BizFailure {
                Intent it = getIntent();
//        flgInfoSrc = it.getStringExtra(TAG_INFO_SOURCE);
                data = it.getStringExtra("data");
                return OrderBiz.showOrder(data);
            }

            @Override
            protected void onExecuteSucceeded(OrderTradeDto orderTrade) {
                closeLoding();
                //bind data to view
                ActivityGoodsSubmit.orderTradeModel = orderTrade;
                bindData2View();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }*/

    /*private void bindData2View() {

    }*/

//    @Override
//    protected void resumeFromOther() {
//        showOrder(data, "showOrder");
//    }
    private View getHeaderView(View.OnClickListener listener) {
        View view = getLayoutInflater().inflate(R.layout.item_order_submit_address, (ViewGroup) recyclerView.getParent(), false);
        tvAddrInfo = (TextView) view.findViewById(R.id.item_order_submit_address_info);
        view.setOnClickListener(listener);

        return view;
    }

    private View getFooterView(View.OnClickListener listener) {
//        Log.e(AppConfig.ERR_TAG, "getFooterView");
        View view = getLayoutInflater().inflate(R.layout.item_order_submit_bottom, (ViewGroup) recyclerView.getParent(), false);

        rlInvoice = (RelativeLayout) view.findViewById(R.id.item_order_submit_bottom_invoice);
        tvInvoiceTitle = (TextView) view.findViewById(R.id.item_order_submit_bottom_invoice_title);
        tvInvoiceTitle.setText("发票信息");
        tvInvoiceMore = (TextView) view.findViewById(R.id.item_order_submit_bottom_invoice_more);
        tvInvoiceContent = (TextView) view.findViewById(R.id.item_order_submit_bottom_invoice_content);
        setInvoiceData("");
//        initTextViewTItle(rlInvoice, "发票信息");
        rlZhifu = (RelativeLayout) view.findViewById(R.id.item_order_submit_bottom_paymethod);
        tvZhifuTitle = (TextView) view.findViewById(R.id.item_order_submit_bottom_paymethod_title);
        tvZhifuTitle.setText("配送方式\n支付方式");
        tvZhifuMore = (TextView) view.findViewById(R.id.item_order_submit_bottom_paymethod_more);
        tvZhifuContent = (TextView) view.findViewById(R.id.item_order_submit_bottom_paymethod_content);
//        initTextViewTItle(rlZhifu, "配送方式\n支付方式");
        rlZhifu.setOnClickListener(listener);
        rlInvoice.setOnClickListener(listener);
/*
        CheckBox tbDianZiBi = (CheckBox) view.findViewById(R.id.chk_use_dianzibi);
        final TextView tvDianzibi = (TextView) view.findViewById(R.id.tv_dianzibi_show);
        tvDianzibi.setText("");
        tvDianzibi.setVisibility(View.GONE);
        tbDianZiBi.setChecked(false);
        tbDianZiBi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Log.e(AppConfig.ERR_TAG, "chkDianzibi....." + isChecked);

                if (!isUpdFooter) {
                    String pf = isChecked ? "1" : "0";
                    model.setCOIN_FLG(pf);
                    String str = GsonUtils.Bean2Json(model);
                    showOrder(str, "changeOrder");
                }

            }
        });

        CheckBox tbYouFeiQuan = (CheckBox) view.findViewById(R.id.chk_use_youfeiquan);
        final TextView tvYoufei = (TextView) view.findViewById(R.id.tv_youfeiquan_show);
        tbYouFeiQuan.setChecked(false);
        if (cOrderModel != null && "1".equals(cOrderModel.getPOSTAGE_FLG())) {
            tbYouFeiQuan.setChecked(true);
        }
        tbYouFeiQuan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isUpdFooter) {
                    String pf = isChecked ? "1" : "0";
                    model.setPOSTAGE_FLG(pf);
                    String str = GsonUtils.Bean2Json(model);
                    showOrder(str, "changeOrder");

                }
            }
        });

        edCustomerNote = (EditText) view.findViewById(R.id.order_submit_ed_customernote);
        */
        return view;
    }

    private void setInvoiceData(String content) {
        tvInvoiceContent.setText(content);
    }

    private void setTextViewContent(RelativeLayout invoice, String content) {
        TextView tvContent = (TextView) invoice.findViewById(R.id.textview_content);
        tvContent.setText(content);
    }

    private void initTextViewTItle(RelativeLayout rl, String title) {
        TextView tvTitle = (TextView) rl.findViewById(R.id.textview_title);
        tvTitle.setText(title);
        setTextViewContent(rl, "");
    }

    private BizDataAsyncTask<String> submitTask;

    private void submitOrder(final String str, final String op) {
//        Log.e(AppConfig.ERR_TAG, "order create..." + str);
        submitTask = new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                Log.e(AppConfig.ERR_TAG, "submit req:" + str);
                return GoodsBiz.submitOrder(str, op);
            }

            @Override
            protected void onExecuteSucceeded(final String tradeOrderId) {
                closeLoding();

                //集成小能下单数量功能
                // 订单页轨迹标准接口 * @param title 订单页的标题, 必传字段 * @param url 订单页的url, 必传字段，需要保证每个订单页的url唯一 * @param sellerid 商户id, B2C企业传空, B2B企业需要传入商户的id * @param ref 上一页url, 如没有可传空 * @param orderid 订单id, 必传字段 * @param orderprice 订单价格, 必传字段 */
                String url = "http://www.lanjiutian.com/user/medicine/orderDetail.html?&wareid=002204&paramOrderGroupId=" + tradeOrderId;
                Ntalker.getBaseInstance().startAction_order("蓝九天电商", url, sellerid, "", tradeOrderId, cOrderModel.getPAY_AMOUNT());

                if (PAYMTD_C.equals(cOrderModel.getPAYMENT_METHOD())) {
                    ActivityGoodsSubmit.this.goOrderList();
                } else {
                    OrderTrade orderTrade = new OrderTrade();
                    orderTrade.setPayAmount(money_all.getText().toString());
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
                    ;
                    ;
                }
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        };
        setLoding(ActivityGoodsSubmit.this, false);
        submitTask.execute();
    }

    /**
     * 进入 rlist
     */
    private void goOrderList() {
        Intent it = new Intent(ActivityGoodsSubmit.this, ActivityOrder.class);
        finish();
        startActivity(it);
    }

    private BizDataAsyncTask<CreatOrderModel> orderTask;

    private void showOrder(final String str, final String op) {
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
                bindData2View();


            }

            @Override
            protected void OnExecuteFailed() {
                Log.e(AppConfig.ERR_TAG, ActivityGoodsSubmit.this.getClass().getName() + "OnExecuteFailed");
                finish();
                closeLoding();
            }
        }.execute();
    }

    private void bindData2View() {
        //ordertrade data show
        String strrlZhifu = "";
        if ("1".equals(cOrderModel.getDISTRIBUTION_MODE())) {
            strrlZhifu = "门店自取";
        } else {
            strrlZhifu = "送货上门";
        }

        if (PAYMTD_C.equals(cOrderModel.getPAYMENT_METHOD())) {
            strrlZhifu += "\n货到付款";
        } else {
            strrlZhifu += "\n在线支付";
        }
        tvZhifuContent.setText(strrlZhifu);
//        setTextViewContent(rlZhifu, strrlZhifu);

        if ("1".equals(cOrderModel.getINVOICE_FLG())) {
            tvInvoiceContent.setText(cOrderModel.getINVOICE_TITLE());
//            setTextViewContent(rlInvoice, cOrderModel.getINVOICE_TITLE());
        } else {
            tvInvoiceContent.setText("不开发票");
//            setTextViewContent(rlInvoice, "不开发票");
        }

        AddressModel addr = cOrderModel.getUSER_ADDRESS();
        if (addr != null) {
            StringBuilder err = new StringBuilder();
            MyLocation location = AddressModel.parseLocation(err, addr.getADDRESS_LOCATION());
            String addrLocation = "未知";
            if (err.length() <= 0) {
                addrLocation = location.getAddrStr();
            }
            tvAddrInfo.setText(addr.getCONSIGNEE_NAME() + "      " + addr.getCONSIGNEE_MOBILE() + "\n" + addrLocation + addr.getCONSIGNEE_ADDRESS());
        }

        money_all.setText("合计: ￥" + cOrderModel.getPAY_AMOUNT() + "(含运费" + cOrderModel.getPOSTAGE_ALL() + ")");

        adapter.setNewData(cOrderModel.getDETAILS());
        adapter.notifyDataSetChanged();
    }

    /*private void updFooter() {
        isUpdFooter = true;
        if (model == null) return;
        if (model.getDETAILS() == null) return;
        if (model.getDETAILS().size() <= 0) return;
        GoodCreatModel2 firstGroupOrder = model.getDETAILS().get(0);
        String ELECTRONIC_MONEY = firstGroupOrder.getELECTRONIC_MONEY();
        String ELECTRONIC_USEABLE_MONEY = firstGroupOrder.getELECTRONIC_USEABLE_MONEY();
        String postage = firstGroupOrder.getPOSTAGE();
        BigDecimal iPostage = new BigDecimal("0");
        if (!StringUtils.isEmpty(postage)) {
            iPostage = new BigDecimal(postage);
        }
        *//*CheckBox tbDianZiBi = (CheckBox) adapter.getFooterLayout().findViewById(R.id.chk_use_dianzibi);
        TextView tvDianzibi = (TextView) adapter.getFooterLayout().findViewById(R.id.tv_dianzibi_show);
        tvDianzibi.setVisibility(View.VISIBLE);
        tvDianzibi.setText("已用" + ELECTRONIC_MONEY + "剩余" + ELECTRONIC_USEABLE_MONEY);
        if ("0".equals(ELECTRONIC_USEABLE_MONEY)) {
//            tbDianZiBi.setEnabled(false);
        }
        if ("1".equals(model.getCOIN_FLG())) {
            tbDianZiBi.setChecked(true);
            if ("0".equals(ELECTRONIC_MONEY)) {
                tbDianZiBi.setEnabled(false);
            }
        }

*//*
        *//*String postnum = firstGroupOrder.getPOST_NUM();
        BigDecimal iPostnum = new BigDecimal(postnum);
        CheckBox tbYoufeiquan = (CheckBox) adapter.getFooterLayout().findViewById(R.id.chk_use_youfeiquan);
        TextView tvYoufeiquan = (TextView) adapter.getFooterLayout().findViewById(R.id.tv_youfeiquan_show);
        tvYoufeiquan.setText("可用:" + postnum);
        Log.e(AppConfig.ERR_TAG, "iPostage" + iPostage + "/" + iPostnum);
        if (BigDecimal.ZERO.compareTo(iPostage) >= 0 || BigDecimal.ZERO.compareTo(iPostnum) >= 0) {
            tbYoufeiquan.setEnabled(false);
        }

        if ("1".equals(model.getPOSTAGE_FLG())) {
            tbYoufeiquan.setChecked(true);
        }*//*
        isUpdFooter = false;
    }*/

    //店铺adapter
    private class OrderGroupAdapter extends BaseQuickAdapter<OrderGroupDto> {

        public OrderGroupAdapter() {
            super(R.layout.item_order_submit_group, null);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, final OrderGroupDto group) {
            /*String fapiao1 = "";
            String peisong = "";
            if (group.getINVOICE_FLG().equals("0")) {
                fapiao1 = "不开发票";
            } else {
                //获取发票  Content
                fapiao1 = Business.mapFapiaoCatText.get(group.getINVOICE_CONTENT());
            }

            if (group.getDISTRIBUTION_MODE().equals("0")) {
                peisong = "送货上门（运费：￥" + group.getPOSTAGE() + "）";
            } else if (group.getDISTRIBUTION_MODE().equals(PAYMTD_C)) {
                peisong = "门店自取";
            }*/
            View.OnClickListener groupListener = new View.OnClickListener() {
                public DiscountDialog dialog;

                private void showDiscountDialog() {
                    dialog = new DiscountDialog(ActivityGoodsSubmit.this);
                    dialog.setCallback(new MyCallback() {
                        @Override
                        public void callback(Object... params) {
                            final CheckBox youfeiquan = (CheckBox) params[0];
                            if (youfeiquan.isChecked()) {
                                group.setPOSTAGE_FLG("1");
                            } else {
                                group.setPOSTAGE_FLG("0");
                            }
                            final CheckBox dianzibi = (CheckBox) params[1];
                            if (dianzibi.isChecked()) {
                                group.setCOIN_FLG("1");
                            } else {
                                group.setCOIN_FLG("0");
                            }
                            final CheckBox youhuiquan = (CheckBox) params[2];
                            if (youhuiquan.isChecked()) {
                                group.setPREFERENTIAL_FLG("1");
                            } else {
                                group.setPREFERENTIAL_FLG("0");
                            }
                            final RadioGroup rgPoint = (RadioGroup) params[3];
                            switch (rgPoint.getCheckedRadioButtonId()) {
                                case R.id.dialog_discount_jifen_rb2:
                                    group.setCOST_POINT("100");
                                    break;
                                case R.id.dialog_discount_jifen_rb3:
                                    group.setCOST_POINT("200");
                                    break;
                                default:
                                    group.setCOST_POINT("0");
                                    break;
                            }
//                            group.
                            showOrder(GsonUtils.Bean2Json(cOrderModel), "changeOrder");
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }

                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.item_order_submit_group_discount:
                            //show discount
                            showDiscountDialog();
                            break;
                    }
                }
            };

            RelativeLayout vDiscount = baseViewHolder.getView(R.id.item_order_submit_group_discount);
            vDiscount.setOnClickListener(groupListener);
            initTextViewTItle(vDiscount, "店铺优惠");

            baseViewHolder.setText(R.id.item_order_submit_group_tv_cost, "共" + group.getGROUP_EXCHANGE_QUANLITY() + "件商品  小计:￥" + group.getGROUP_MONEY_ALL())
                    .setText(R.id.item_order_submit_group_groupname, group.getLOGISTICS_COMPANY())
                    .setText(R.id.item_order_submit_group_arrivedate, group.getDISTRIBUTION_DATE_END());

            EditText edRemark = baseViewHolder.getView(R.id.item_order_submit_group_edremark);
            edRemark.setText(group.getGROUP_REMARKS());
            edRemark.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    group.setGROUP_REMARKS(s.toString());
                }
            });


            RecyclerView goods = baseViewHolder.getView(R.id.goods);
            goods.setLayoutManager(new LinearLayoutManager(ActivityGoodsSubmit.this));
            goods.setItemAnimator(new DefaultItemAnimator());
            goods.setAdapter(new CommOrderAdapter(group.getDETAILS()));

        }
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
            protected void onExecuteSucceeded(String data) {
                closeLoding();
                final OnlinePayModel aliPay;
                final String orderInfo;
                if (payModel.getPAYMENT_METHOD().equals(OrderTrade.PAYMTD_WECHAT)) {
                    Utils.log(data);
                    setLoding(ActivityGoodsSubmit.this, false);
                    Map dataMap = GsonUtils.Json2Bean(data, HashMap.class);
                    Map wechatData = (Map) dataMap.get("result");
                    wxApi = ((MyApplication) getApplication()).wxApi;

                    PayReq request = new PayReq();
                    request.appId = AppConfig.WEIXIN_APP_ID;
                    request.partnerId = wechatData.get("partnerid").toString();
                    request.prepayId = wechatData.get("prepayid").toString();
                    request.packageValue = "Sign=WXPay";
                    request.nonceStr = "" + wechatData.get("noncestr");
                    request.timeStamp = wechatData.get("timestamp").toString();
                    request.sign = "" + wechatData.get("sign");

                    wxApi.sendReq(request);
                } else if (payModel.getPAYMENT_METHOD().equals(OrderTrade.PAYMTD_ALI)) {
                    //支付宝
                    aliPay = GsonUtils.Json2Bean(data, OnlinePayModel.class);
                    orderInfo = aliPay.getPay();
                    if (!StringUtils.isEmpty(orderInfo)) {
                        Runnable payRunnable = new Runnable() {

                            @Override
                            public void run() {
                                //支付宝
                                PayTask alipay = new PayTask(ActivityGoodsSubmit.this);
                                Map<String, String> result = alipay.payV2(orderInfo, true);

                                Message msg = new Message();
                                msg.what = ALI_PAY;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        };

                        Thread payThread = new Thread(payRunnable);
                        payThread.start();


                    }
                } else if (payModel.getPAYMENT_METHOD().equals(OrderTrade.PAYMTD_UNION)) {
                    //银联
                    //union pay
//                    Runnable payRunnable = new Runnable() {
//
//                        @Override
//                        public void run() {
                    aliPay = GsonUtils.Json2Bean(data, OnlinePayModel.class);
                    orderInfo = aliPay.getPay();
                    Log.e(AppConfig.ERR_TAG, orderInfo);
//                    mLoadingDialog = ProgressDialog.show(ActivityGoodsSubmit.this, // context
//                            "", // title
//                            "正在努力加载,请稍候...", // message
//                            true); // 进度是否是不确定的，这只和创建进度条有关

                    //调用银联控件
                    if (!StringUtils.isEmpty(orderInfo)) {
                        jsonUnionOrder = orderInfo;
                        Map m = GsonUtils.Json2Bean(jsonUnionOrder, Map.class);
                        String tn = (String) m.get("tn");
//                        Log.e("+++++",tn) ;
                        UPPayAssistEx.startPay(ActivityGoodsSubmit.this, null, null, tn, "00");
                    }
//                        }
//                    };
//
//                    Thread payThread = new Thread( payRunnable );
//                    payThread.start();
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
        Log.e(AppConfig.ERR_TAG, "onActivityResult..:" + requestCode + "/" + resultCode);
        isResult = true;
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SELECT_ADDRESS:
                    Parcelable addr = data.getParcelableExtra(ActivityAddress.TAG_SELECTED_ADDRESS);
                    if (addr != null) {
                        int addrId = ((AddressModel) addr).getADDRESS_ID();
                        cOrderModel.setADDRESS_ID(addrId + "");

                        String str = GsonUtils.Bean2Json(cOrderModel);
                        showOrder(str, "changeOrder");
                    }
                    break;
            }
        } else if (resultCode == AppConfig.IntentExtraKey.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SETTING:
                    String zhifu_s = data.getStringExtra(ActivityGoodsSubmitChoice.TAG_ZHIFU);
                    cOrderModel.setPAYMENT_METHOD(zhifu_s);
                    String peisong = data.getStringExtra(ActivityGoodsSubmitChoice.TAG_PEISONG);
                    cOrderModel.setDISTRIBUTION_MODE(peisong);
                    String dateStart = data.getStringExtra(ActivityGoodsSubmitChoice.TAG_IIME_START);
                    String dateEnd = data.getStringExtra(ActivityGoodsSubmitChoice.TAG_IIME_END);
                    cOrderModel.setDISTRIBUTION_DATE_START(dateStart);
                    cOrderModel.setDISTRIBUTION_DATE_END(dateEnd);
                    bindData2View();
                    break;

                case REQUEST_FAPIAO:
                    //配送信息 和 发票信息
                    String jsonStr = data.getStringExtra(TAG_ORDER);
                    Business fapiaoModel = GsonUtils.Json2Bean(jsonStr, Business.class);

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
                    adapter.notifyDataSetChanged();
                    bindData2View();
                    break;
            }
        } else {
            if (requestCode == 10) {
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
                        } catch (JSONException e) {
                            Log.e(AppConfig.ERR_TAG, "payresult:" + e.getMessage());
                        }
                    } else {
                        // 未收到签名信息
                        // 建议通过商户后台查询支付结果
                        ToastUtil.show(ActivityGoodsSubmit.this, getResources().getString(R.string.pay_success));
                    }

                    if (!StringUtils.isEmpty(result)) {
                        UnionResult unionResult = GsonUtils.Json2Bean(result, UnionResult.class);
                        unionResult.setORDER_TRADE_ID(payModel.getORDER_TRADE_ID());
                        unionResult.setTradeOrder(jsonUnionOrder);
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
                        ToastUtil.show(ActivityGoodsSubmit.this, getResources().getString(R.string.pay_success));
                        Log.e(AppConfig.ERR_TAG, "alipay result....:" + resultInfo);
                        if (!StringUtils.isEmpty(resultInfo)) {
                            AliResult aliResult = GsonUtils.Json2Bean(resultInfo, AliResult.class);
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

                case UP_PAY:


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
//        it.putExtra(ActivityGoodsSubmit.TAG_INFO_SOURCE, ActivityGoodsInfo.VAL_INFO_SOURCE);
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

    //银联支付回执
    public class UnionResult {
        private String sign;
        private String data;
        private String tradeOrder;
        private String ORDER_TRADE_ID;

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getORDER_TRADE_ID() {
            return ORDER_TRADE_ID;
        }

        public void setORDER_TRADE_ID(String ORDER_TRADE_ID) {
            this.ORDER_TRADE_ID = ORDER_TRADE_ID;
        }

        public String getTradeOrder() {
            return tradeOrder;
        }

        public void setTradeOrder(String tradeOrder) {
            this.tradeOrder = tradeOrder;
        }
    }


    //支付宝支付回执
    public class AliResult {
        private alipay_trade alipay_trade_app_pay_response;
        private String sign;
        private String ORDER_TRADE_ID;
        private String sign_type;

        public alipay_trade getAlipay_trade_app_pay_response() {
            return alipay_trade_app_pay_response;
        }

        public void setAlipay_trade_app_pay_response(alipay_trade alipay_trade_app_pay_response) {
            this.alipay_trade_app_pay_response = alipay_trade_app_pay_response;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getORDER_TRADE_ID() {
            return ORDER_TRADE_ID;
        }

        public void setORDER_TRADE_ID(String ORDER_TRADE_ID) {
            this.ORDER_TRADE_ID = ORDER_TRADE_ID;
        }

        public String getSign_type() {
            return sign_type;
        }

        public void setSign_type(String sign_type) {
            this.sign_type = sign_type;
        }

        private class alipay_trade {
            private String code;
            private String msg;
            private String app_id;
            private String auth_app_id;
            private String charset;
            private String timestamp;
            private String total_amount;
            private String trade_no;
            private String seller_id;
            private String out_trade_no;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public String getApp_id() {
                return app_id;
            }

            public void setApp_id(String app_id) {
                this.app_id = app_id;
            }

            public String getAuth_app_id() {
                return auth_app_id;
            }

            public void setAuth_app_id(String auth_app_id) {
                this.auth_app_id = auth_app_id;
            }

            public String getCharset() {
                return charset;
            }

            public void setCharset(String charset) {
                this.charset = charset;
            }

            public String getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(String timestamp) {
                this.timestamp = timestamp;
            }

            public String getTotal_amount() {
                return total_amount;
            }

            public void setTotal_amount(String total_amount) {
                this.total_amount = total_amount;
            }

            public String getOut_trade_no() {
                return out_trade_no;
            }

            public void setOut_trade_no(String out_trade_no) {
                this.out_trade_no = out_trade_no;
            }

            public String getSeller_id() {
                return seller_id;
            }

            public void setSeller_id(String seller_id) {
                this.seller_id = seller_id;
            }

            public String getTrade_no() {
                return trade_no;
            }

            public void setTrade_no(String trade_no) {
                this.trade_no = trade_no;
            }
        }

    }

}

