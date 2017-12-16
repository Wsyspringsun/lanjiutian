package com.wyw.ljtds.ui.goods;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.alipay.PayResult;
import com.alipay.sdk.app.PayTask;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.unionpay.UPPayAssistEx;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.biz.OrderBiz;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.AppManager;
import com.wyw.ljtds.model.AddressModel;
import com.wyw.ljtds.model.Business;
import com.wyw.ljtds.model.CreatOrderModel;
import com.wyw.ljtds.model.Good;
import com.wyw.ljtds.model.GoodCreatModel1;
import com.wyw.ljtds.model.GoodCreatModel2;
import com.wyw.ljtds.model.GoodCreatModel3;
import com.wyw.ljtds.model.GoodSubmitModel1;
import com.wyw.ljtds.model.MedicineDetailsModel;
import com.wyw.ljtds.model.OnlinePayModel;
import com.wyw.ljtds.model.OrderCommDto;
import com.wyw.ljtds.model.OrderGroupDto;
import com.wyw.ljtds.model.OrderTrade;
import com.wyw.ljtds.model.OrderTradeDto;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.user.address.ActivityAddress;
import com.wyw.ljtds.ui.user.address.ActivityAddressEdit;
import com.wyw.ljtds.ui.user.order.ActivityOrder;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.PayDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    public static final String TAG_INFO_SOURCE = "com.wyw.ljtds.ui.goods.ActivityGoodsSubmit.TAG_INFO_SOURCE";
    private static final int REQUEST_FAPIAO = 1;
    private static final int REQUEST_SELECT_ADDRESS = 2;
    private static final String TAG_SELLERID = "com.wyw.ljtds.ui.goods.tag_sellerid";
    private static final String TAG_ORDER_DATA = "com.wyw.ljtds.ui.goods.ActivityGoodsSubmit.TAG_ORDER_DATA" ;
    private String flgInfoSrc = "";
    private String PAYMTD_C = "C";

    @ViewInject(R.id.header_return_text)
    private TextView title;
    @ViewInject(R.id.group)
    private RecyclerView recyclerView;//gr order
    @ViewInject(R.id.money)
    private TextView money_all;
    private EditText edCustomerNote;


    private GoodCreatModel1 model;
    private List<Business> list_business;
    private MyAdapter adapter;
    private String zhifu_s = "";//支付方式
    private String jifen_money = "";//积分抵用金额
    private String jifen = "";
    private String disDateStart = "";
    private String disDateEnd = "";
    private String data = "";//从购物车和商品详情页传递来的数据
    private String peisong = "送货上门";
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


    @Event(value = {R.id.header_return, R.id.submit})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_return:
                finish();
                break;

            case R.id.submit:
                final AlertDialog alert = new AlertDialog.Builder(this).create();
                alert.setTitle(R.string.alert_tishi);
                alert.setMessage(getResources().getString(R.string.confirm_order_submit));
                alert.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.alert_queding), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!StringUtils.isEmpty(edCustomerNote.getText().toString())) {
                            model.setREMARKS(edCustomerNote.getText().toString());
                            Log.e(AppConfig.ERR_TAG, "getREMARKS:" + model.getREMARKS());
                        }
                        submitOrder(GsonUtils.Bean2Json(model), "create");
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

        title.setText(R.string.order_queren);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new MyAdapter();
        adapter.addHeaderView(getHeaderView(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = ActivityAddress.getIntent(ActivityGoodsSubmit.this, true);
                startActivityForResult(it, REQUEST_SELECT_ADDRESS);
            }
        }));
        adapter.addFooterView(getFooterView(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.zzhifu:
                        Intent it = new Intent(ActivityGoodsSubmit.this, ActivityGoodsSubmitChoice.class);
                        String json = GsonUtils.Bean2Json(cOrderModel);
//                        old
                        it.putExtra(ActivityGoodsSubmit.TAG_INFO_SOURCE, ActivityGoodsSubmit.this.flgInfoSrc);
                        it.putExtra(ActivityGoodsSubmitChoice.TAG_CREATE_ORDER_MODEL, json);
//                it.putExtra("pay", zhifu_s);
//                it.putExtra("jifen_money", jifen_money);
//                it.putExtra("jifen", jifen);

                        startActivityForResult(it, 0);
                        break;
                    default:
                        break;
                }

            }
        }));
        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Log.e(AppConfig.ERR_TAG, "position:" + i);
                switch (view.getId()) {
                    case R.id.select:
                        Intent it = ActivityGoodsSubmitBill.getIntent(ActivityGoodsSubmit.this, adapter.getData().get(i));
                        startActivityForResult(it, REQUEST_FAPIAO);
                }
            }
        });

        recyclerView.setAdapter(adapter);

        sellerid = getIntent().getStringExtra(TAG_SELLERID);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void loadData() {
        Intent it = getIntent();
//        flgInfoSrc = it.getStringExtra(TAG_INFO_SOURCE);

        data = it.getStringExtra(TAG_ORDER_DATA);
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

        view.setOnClickListener(listener);

        return view;
    }

    private View getFooterView(View.OnClickListener listener) {
//        Log.e(AppConfig.ERR_TAG, "getFooterView");
        View view = getLayoutInflater().inflate(R.layout.item_order_submit_bottom, (ViewGroup) recyclerView.getParent(), false);

        View zzhifu = view.findViewById(R.id.zzhifu);

        zzhifu.setOnClickListener(listener);

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
        return view;
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

                if (PAYMTD_C.equals(model.getPAYMENT_METHOD())) {
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

    private void loadOrder(String tradeOrderId) {


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
        orderTask = new BizDataAsyncTask<CreatOrderModel>() {
            @Override
            protected CreatOrderModel doExecute() throws ZYException, BizFailure {
                CreatOrderModel rlt = GoodsBiz.getOrderShow(str, op);
                return rlt;
            }

            @Override
            protected void onExecuteSucceeded(CreatOrderModel creatOrderModel) {
                closeLoding();
                ActivityGoodsSubmit.this.cOrderModel = creatOrderModel;
                list_business = new ArrayList<>();
                model = new GoodCreatModel1();
                model.setADDRESS_ID(creatOrderModel.getADDRESS_ID());
                model.setCOIN_FLG(creatOrderModel.getCOIN_FLG());
                model.setPOSTAGE_FLG(creatOrderModel.getPOSTAGE_FLG());
                model.setCOST_POINT(creatOrderModel.getCOST_POINT());
                model.setPAYMENT_METHOD(creatOrderModel.getPAYMENT_METHOD());
                model.setDISTRIBUTION_DATE_START(creatOrderModel.getDISTRIBUTION_DATE_START());
                model.setDISTRIBUTION_DATE_END(creatOrderModel.getDISTRIBUTION_DATE_END());

                List<GoodCreatModel2> lists_group = new ArrayList<>();

                for (int i = 0; i < creatOrderModel.getDETAILS().size(); i++) {
                    GoodCreatModel2 goodCreatModel2 = new GoodCreatModel2();
                    Business business = creatOrderModel.getDETAILS().get(i);
                    //表明 是否 是 医药馆
                    ActivityGoodsSubmit.this.flgInfoSrc = business.getOID_GROUP_ID();
                    goodCreatModel2.setOID_GROUP_NAME(business.getOID_GROUP_NAME());
                    goodCreatModel2.setOID_GROUP_ID(business.getOID_GROUP_ID());
                    goodCreatModel2.setDISTRIBUTION_MODE(business.getDISTRIBUTION_MODE());
                    goodCreatModel2.setINVOICE_CONTENT(business.getINVOICE_CONTENT());
                    goodCreatModel2.setINVOICE_FLG(business.getINVOICE_FLG());
                    goodCreatModel2.setINVOICE_TITLE(business.getINVOICE_TITLE());
                    goodCreatModel2.setINVOICE_TYPE(business.getINVOICE_TYPE());
                    goodCreatModel2.setPOSTAGE(business.getPOSTAGE());
                    goodCreatModel2.setPOST_NUM(business.getPOST_NUM());
                    goodCreatModel2.setELECTRONIC_MONEY(business.getELECTRONIC_MONEY());
                    goodCreatModel2.setELECTRONIC_USEABLE_MONEY(business.getELECTRONIC_USEABLE_MONEY());

                    List<GoodCreatModel3> lists_good = new ArrayList<>();
                    List<Good> goods = business.getDETAILS();
                    for (int j = 0; j < goods.size(); j++) {
                        Good good = goods.get(j);
                        GoodCreatModel3 goodCreatModel3 = new GoodCreatModel3();
                        goodCreatModel3.setCOMMODITY_COLOR(good.getCOMMODITY_COLOR());
                        goodCreatModel3.setCOMMODITY_ID(good.getCOMMODITY_ID());
                        goodCreatModel3.setCOMMODITY_COLOR_ID(good.getCOMMODITY_COLOR_ID());
                        goodCreatModel3.setCOMMODITY_SIZE_ID(good.getCOMMODITY_SIZE_ID());
                        goodCreatModel3.setCOMMODITY_NAME(good.getCOMMODITY_NAME());
                        goodCreatModel3.setCOMMODITY_SIZE(good.getCOMMODITY_SIZE());
                        goodCreatModel3.setEXCHANGE_QUANLITY(Integer.parseInt(good.getEXCHANGE_QUANLITY()));
                        goodCreatModel3.setCOMMODITY_ORDER_ID(good.getCOMMODITY_ORDER_ID());

                        lists_good.add(goodCreatModel3);
                    }

                    goodCreatModel2.setDETAILS(lists_good);
                    lists_group.add(goodCreatModel2);
                }

                model.setDETAILS(lists_group);


                list_business = creatOrderModel.getDETAILS();
                if (creatOrderModel.getPAYMENT_METHOD().equals("0")) {
                    zhifu_s = "在线支付";
                } else if (creatOrderModel.getPAYMENT_METHOD().equals(PAYMTD_C)) {
                    zhifu_s = "货到付款";
                }
                model.setPAYMENT_METHOD(creatOrderModel.getPAYMENT_METHOD());
                //--记录发送数据
                jifen_money = creatOrderModel.getPOINT_MONEY();
                jifen = creatOrderModel.getUSER_POINT();
                address_id = creatOrderModel.getUSER_ADDRESS().getADDRESS_ID() + "";
                bundle = new Bundle();
                bundle.putString("id_p", creatOrderModel.getUSER_ADDRESS().getCONSIGNEE_PROVINCE() + "");
                bundle.putString("id_c", creatOrderModel.getUSER_ADDRESS().getCONSIGNEE_CITY() + "");
                bundle.putString("name", creatOrderModel.getUSER_ADDRESS().getCONSIGNEE_NAME());
                bundle.putString("phone", creatOrderModel.getUSER_ADDRESS().getCONSIGNEE_MOBILE());
                bundle.putString("xiangxi", creatOrderModel.getUSER_ADDRESS().getCONSIGNEE_ADDRESS());
                bundle.putString("shengshi", creatOrderModel.getUSER_ADDRESS().getPROVINCE() + creatOrderModel.getUSER_ADDRESS().getCITY());


                updFooter();

                addressInfo = creatOrderModel.getUSER_ADDRESS();
                ((TextView) adapter.getHeaderLayout().findViewById(R.id.name)).setText(creatOrderModel.getUSER_ADDRESS().getCONSIGNEE_NAME());
                ((TextView) adapter.getHeaderLayout().findViewById(R.id.phone)).setText(creatOrderModel.getUSER_ADDRESS().getCONSIGNEE_MOBILE());
                ((TextView) adapter.getHeaderLayout().findViewById(R.id.shouhuo_dizhi)).setText(creatOrderModel.getUSER_ADDRESS().getADDRESS_DETAIL());
                ((TextView) adapter.getFooterLayout().findViewById(R.id.zhifu_jieguo)).setText(zhifu_s);
                ((TextView) adapter.getFooterLayout().findViewById(R.id.jifen_jieguo)).setText(jifen_money);
                money_all.setText("￥" + creatOrderModel.getPAY_AMOUNT());

                adapter.setNewData(list_business);
                adapter.notifyDataSetChanged();

            }

            @Override
            protected void OnExecuteFailed() {
                Log.e(AppConfig.ERR_TAG, ActivityGoodsSubmit.this.getClass().getName() + "OnExecuteFailed");
                closeLoding();
            }
        };
        setLoding(this, false);
        orderTask.execute();
    }

    private void updFooter() {
        isUpdFooter = true;
        Log.e(AppConfig.ERR_TAG, "updFooter");
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
        CheckBox tbDianZiBi = (CheckBox) adapter.getFooterLayout().findViewById(R.id.chk_use_dianzibi);
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


        String postnum = firstGroupOrder.getPOST_NUM();
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
        }
        isUpdFooter = false;
    }

    //店铺adapter
    private class MyAdapter extends BaseQuickAdapter<Business> {

        public MyAdapter() {
            super(R.layout.item_order_submit_group, list_business);
        }


        @Override
        protected void convert(BaseViewHolder baseViewHolder, Business group) {
            String fapiao1 = "";
            String peisong = "";
            Log.e(AppConfig.ERR_TAG, "group:" + GsonUtils.Bean2Json(group));
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
            }

            baseViewHolder.addOnClickListener(R.id.select)
                    .setText(R.id.textView3, group.getOID_GROUP_NAME())
                    .setText(R.id.peisong_xuanze, peisong)
                    .setText(R.id.shuliang, "共计" + group.getGROUP_EXCHANGE_QUANLITY() + "件商品")
//                    .setText( R.id.fapiao_xuanze, group.getFapiao1()+group.getFapiao2()+group.getFapiao3() )
                    .setText(R.id.fapiao_xuanze, fapiao1)
                    .setText(R.id.xiaoji_money, "￥" + group.getGROUP_MONEY_ALL());


            RecyclerView goods = baseViewHolder.getView(R.id.goods);
            goods.setLayoutManager(new LinearLayoutManager(ActivityGoodsSubmit.this));
            goods.setItemAnimator(new DefaultItemAnimator());
            goods.setAdapter(new MyAdapter1(group.getDETAILS()));

        }
    }

    //商品adapter
    private class MyAdapter1 extends BaseQuickAdapter<Good> {
        public MyAdapter1(List<Good> lists) {
            super(R.layout.item_order_submit_goods, lists);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, Good goods) {
            if (StringUtils.isEmpty(goods.getCOMMODITY_COLOR())) {
                baseViewHolder.setText(R.id.size, " 规格：" + goods.getCOMMODITY_SIZE());
            } else {
                baseViewHolder.setText(R.id.size, "产地：" + goods.getCOMMODITY_COLOR() + " ;规格：" + goods.getCOMMODITY_SIZE());
            }
            baseViewHolder.setText(R.id.title, StringUtils.deletaFirst(goods.getCOMMODITY_NAME()))
                    .setText(R.id.money, "￥" + goods.getCOST_MONEY())
                    .setText(R.id.number, "X" + goods.getEXCHANGE_QUANLITY());

            SimpleDraweeView simpleDraweeView = baseViewHolder.getView(R.id.iv_adapter_list_pic);
            if (!StringUtils.isEmpty(goods.getIMG_PATH())) {
                simpleDraweeView.setImageURI(Uri.parse(goods.getIMG_PATH()));
            }
        }
    }

    BizDataAsyncTask<OnlinePayModel> payTask;

    /**
     * 进行支付
     *
     * @param data online pay data
     */
    private void goPay(final String data) {
        payTask = new BizDataAsyncTask<OnlinePayModel>() {
            @Override
            protected OnlinePayModel doExecute() throws ZYException, BizFailure {
                return GoodsBiz.onlinePay(data);
            }

            @Override
            protected void onExecuteSucceeded(OnlinePayModel aliPay) {
                closeLoding();
                final String orderInfo = aliPay.getPay();
                if (payModel.getPAYMENT_METHOD().equals(OrderTrade.PAYMTD_ALI)) {
                    //支付宝
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
        };
        setLoding(ActivityGoodsSubmit.this, false);
        payTask.execute();
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
                        model.setADDRESS_ID(addrId + "");
                        String str = GsonUtils.Bean2Json(model);
                        showOrder(str, "changeOrder");
                    }
                    break;
            }
        } else if (resultCode == AppConfig.IntentExtraKey.RESULT_OK) {
            switch (requestCode) {
                case 0:
                    zhifu_s = data.getStringExtra("pay");
                    jifen_money = data.getStringExtra("jifen_money");
                    jifen = data.getStringExtra("jifen");
                    model.setCOST_POINT(jifen);
                    if (zhifu_s.equals("在线支付")) {
                        model.setPAYMENT_METHOD("0");
                    } else if (zhifu_s.equals("货到付款")) {
                        model.setPAYMENT_METHOD(PAYMTD_C);
                    }

                    String strCOrderModel = data.getStringExtra(ActivityGoodsSubmitChoice.TAG_CREATE_ORDER_MODEL);
                    cOrderModel = GsonUtils.Json2Bean(strCOrderModel, CreatOrderModel.class);
                    model.setDISTRIBUTION_DATE_START(cOrderModel.getDISTRIBUTION_DATE_START());
                    model.setDISTRIBUTION_DATE_END(cOrderModel.getDISTRIBUTION_DATE_END());
                    String str = GsonUtils.Bean2Json(model);
                    showOrder(str, "changeOrder");

                    break;

                case REQUEST_FAPIAO:
                    //配送信息 和 发票信息
                    //int index = data.getIntExtra("possition", 0);
                    int index = 0;
//                    peisong = data.getStringExtra("peisong");
//                    fapiao_flg1 = data.getStringExtra("fapiao_flg1");
//                    fapiao_flg2 = data.getStringExtra("fapiao_flg2");
//                    fapiao_flg3 = data.getStringExtra("fapiao_flg3");
//                    fapiao_flg4 = data.getStringExtra("fapiao_flg4");
                    String jsonStr = data.getStringExtra(TAG_ORDER);
                    Business fapiaoModel = GsonUtils.Json2Bean(jsonStr, Business.class);
                    Log.e(AppConfig.ERR_TAG, "jsonStr:" + jsonStr);

//                    Business bizFirst = list_business.get(index);
//                    bizFirst.setINVOICE_FLG(fapiaoModel.getINVOICE_FLG());
                    //model.getDETAILS().get(index).setINVOICE_FLG(fapiaoModel.getINVOICE_FLG());
                    if (!"0".equals(fapiaoModel.getINVOICE_FLG())) {
                        //如果开发票
//                        bizFirst.setINVOICE_TYPE(fapiaoModel.getINVOICE_TYPE());
//                        bizFirst.setINVOICE_TITLE(fapiaoModel.getINVOICE_TITLE());
//                        bizFirst.setINVOICE_CONTENT(fapiaoModel.getINVOICE_CONTENT());
//                        bizFirst.setINVOICE_TAX(fapiaoModel.getINVOICE_CONTENT());

                        model.getDETAILS().get(index).setINVOICE_FLG(fapiaoModel.getINVOICE_FLG());
                        model.getDETAILS().get(index).setINVOICE_TYPE(fapiaoModel.getINVOICE_TYPE());
                        model.getDETAILS().get(index).setINVOICE_TITLE(fapiaoModel.getINVOICE_TITLE());
                        model.getDETAILS().get(index).setINVOICE_CONTENT(fapiaoModel.getINVOICE_CONTENT());
                        model.getDETAILS().get(index).setINVOICE_TAX(fapiaoModel.getINVOICE_TAX());
//                        model.getDETAILS().get(index).setDISTRIBUTION_MODE(fapiaoModel.get);
                    }
                    //adapter.setNewData(list_business);
                    adapter.notifyDataSetChanged();
//                    String str1 = GsonUtils.Bean2Json(model);
//                    Log.e(AppConfig.ERR_TAG, "str1:" + str1);
//                    showOrder(str1, "changeOrder");
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
                        Log.e(AppConfig.ERR_TAG, "payresult .....2:");
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


    public static Intent getIntent(Context context, OrderTradeDto order, String sellerid) {
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

