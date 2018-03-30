package com.wyw.ljtds.ui.user.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.CommOrderAdapter;
import com.wyw.ljtds.adapter.MedicineOrderAdapter;
import com.wyw.ljtds.biz.biz.ReturnGoodsBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.GoodsHandingModel;
import com.wyw.ljtds.model.OrderTrade;
import com.wyw.ljtds.model.XiaoNengData;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;


/**
 * Created by Administrator on 2017/1/10 0010.
 * ding dan xiangqing
 * order details
 */

@ContentView(R.layout.activity_return_order_info)
public class ReturnGoodsOrderInfoActivity extends BaseActivity {
    private static final String TAG_ORDER_ID = "com.wyw.ljtds.ui.user.order.ReturnGoodsOrderInfoActivity.TAG_ORDER_ID";
    private MedicineOrderAdapter adpGoods;
    String orderId;
    GoodsHandingModel returnGoodsModel;//退货详情*


    @ViewInject(R.id.activity_return_order_info_shengqing)
    LinearLayout llInfoShengqing;
    @ViewInject(R.id.activity_return_order_info_tongyi)
    LinearLayout llInfoTongyi;
    @ViewInject(R.id.activity_return_order_info_jujue)
    LinearLayout llInfoJujue;
    @ViewInject(R.id.activity_return_order_info_tuikuan)
    LinearLayout llInfoTuikuan;

    @ViewInject(R.id.activity_return_order_info_addr)
    LinearLayout llAddr;
    @ViewInject(R.id.jiantou)
    TextView orderInfoStat;
    @ViewInject(R.id.activity_return_order_info_tv_stat)
    TextView tvStat;
    @ViewInject(R.id.activity_return_order_tv_orderinfo)
    TextView tvReturnInfo;
    @ViewInject(R.id.activity_return_order_ryc_goods)
    RecyclerView ryvGoods;


    @Event(value = {R.id.activity_return_order_kefu, R.id.header_return, R.id.order_fuwu, R.id.lianxi, R.id.boda})
    private void onClick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.header_return:
                finish();
                break;
            case R.id.activity_return_order_kefu:
                if (returnGoodsModel == null) return;
                if (returnGoodsModel.getXiaonengData() == null) return;
                XiaoNengData xn = returnGoodsModel.getXiaonengData();
                openChat("订单号：" + returnGoodsModel.getReturnGoodsHandingId(), "", xn.getSettingid1(), returnGoodsModel.getGroupName(), false, "");
                break;
            case R.id.boda:
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParams();
        initView();
        initEvents();
        loadData();
    }

    public static Intent getIntent(Context ctx, String orderId) {
        Intent it = new Intent(ctx, ReturnGoodsOrderInfoActivity.class);
        it.putExtra(TAG_ORDER_ID, orderId);
        return it;
    }

    private void loadData() {
        setLoding(this, false);
        new BizDataAsyncTask<GoodsHandingModel>() {

            @Override
            protected GoodsHandingModel doExecute() throws ZYException, BizFailure {
                return ReturnGoodsBiz.readById(orderId);
            }

            @Override
            protected void onExecuteSucceeded(GoodsHandingModel model) {
                closeLoding();
                returnGoodsModel = model;
                bindData();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    private void initEvents() {
    }

    private void initView() {
        ryvGoods.setLayoutManager(new LinearLayoutManager(this));
        ryvGoods.setItemAnimator(new DefaultItemAnimator());
    }

    private void initParams() {
        orderId = getIntent().getStringExtra(TAG_ORDER_ID);
    }

    private void bindData() {
        if (returnGoodsModel == null) {
            returnGoodsModel = new GoodsHandingModel();
        }
        TextView tvAddr = (TextView) llAddr.findViewById(R.id.item_order_submit_address_info);
        tvAddr.setText(returnGoodsModel.getGroupName() + " " + returnGoodsModel.getOrgMobile() + "\n" + returnGoodsModel.getOrgAddress());
//        tvStat.setText(returnGoodsModel);
        resetLlInfo();
        switch (returnGoodsModel.getReturnStatus()) {
            case OrderTrade.APPLYRETURNED: //"9"; 申请换退货
                tvStat.setText(R.string.order_stat_applyreturned);
                llInfoShengqing.setVisibility(View.VISIBLE);
                break;
            case OrderTrade.AGREERETURNED: //"10";同意换退货
                tvStat.setText(R.string.order_stat_agreereturned);
                llInfoTongyi.setVisibility(View.VISIBLE);
                break;
            case OrderTrade.REFUSERETURNED: //"11";//拒绝换退货
                tvStat.setText(R.string.order_stat_refusereturned);
                llInfoJujue.setVisibility(View.VISIBLE);
                TextView tvJujue = (TextView) llInfoJujue.findViewById(R.id.activity_return_order_tv_jujue);
                llAddr.setVisibility(View.GONE);
                tvJujue.setText("拒绝退货");
                break;
            case OrderTrade.RETURNED: //"12";//已退货
                tvStat.setText(R.string.order_stat_returned);
                break;
            case OrderTrade.REFUNDED: //"13";//已退款
                tvStat.setText(R.string.order_stat_refunded);
                llInfoTuikuan.setVisibility(View.VISIBLE);
                TextView tvMoney = (TextView) llInfoTuikuan.findViewById(R.id.activity_return_order_tv_returnmoney);
                tvMoney.setText(getString(R.string.money_renminbi, returnGoodsModel.getReturnMoney()));
                break;
            default:
                tvStat.setText(R.string.order_stat_return);
                break;
        }
        String rtReason = "退款原因：" + returnGoodsModel.getReturnReasonText();
        String rtMoney = "\n退款金额：￥" + Utils.formatFee(returnGoodsModel.getReturnMoney());
        String rtRegDate = "\n申请时间：" + returnGoodsModel.getInsDate();
        String rtCode = "\n退款编号：" + returnGoodsModel.getReturnGoodsHandingId();
        tvReturnInfo.setText(rtReason + rtMoney + rtRegDate + rtCode);
        if (adpGoods == null) {
            adpGoods = new MedicineOrderAdapter(this);
            ryvGoods.setAdapter(adpGoods);
        }
        adpGoods.setData(returnGoodsModel.getCommodityOrderList());
        adpGoods.notifyDataSetChanged();
    }

    private void resetLlInfo() {
        llInfoShengqing.setVisibility(View.GONE);
        llInfoTongyi.setVisibility(View.GONE);
        llInfoJujue.setVisibility(View.GONE);
        llInfoTuikuan.setVisibility(View.GONE);
    }

}
