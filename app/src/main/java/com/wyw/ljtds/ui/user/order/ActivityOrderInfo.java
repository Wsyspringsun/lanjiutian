package com.wyw.ljtds.ui.user.order;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.AppManager;
import com.wyw.ljtds.model.AddressModel;
import com.wyw.ljtds.model.MyLocation;
import com.wyw.ljtds.model.OrderCommDto;
import com.wyw.ljtds.model.OrderModelInfoMedicine;
import com.wyw.ljtds.model.OrderTrade;
import com.wyw.ljtds.model.OrderTradeDto;
import com.wyw.ljtds.model.RecommendModel;
import com.wyw.ljtds.model.TradeOrder;
import com.wyw.ljtds.model.XiaoNengData;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.goods.ActivityGoodsInfo;
import com.wyw.ljtds.ui.goods.ActivityLifeGoodsInfo;
import com.wyw.ljtds.ui.goods.ActivityMedicinesInfo;
import com.wyw.ljtds.utils.DateUtils;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.DividerGridItemDecoration;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2017/1/10 0010.
 * ding dan xiangqing
 * order details
 */

@ContentView(R.layout.activity_order_info)
public class ActivityOrderInfo extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    @ViewInject(R.id.header_return_text)
    private TextView title;
    @ViewInject(R.id.reclcyer)
    private RecyclerView recyclerView;
    @ViewInject(R.id.order_fuwu)
    private TextView tvShouhou;
    @ViewInject(R.id.activity_order_info_tv_shopaddr)
    private TextView tvShopAddr;
    @ViewInject(R.id.activity_order_info_tv_orderremark)
    private TextView tvOrderRemark;
    @ViewInject(R.id.item_order_submit_address_info)
    private TextView address_dizhi;
    @ViewInject(R.id.activity_order_info_dianpu)
    private TextView dianpu;
    @ViewInject(R.id.yunfei)
    private TextView yunfei;
    @ViewInject(R.id.activity_order_info_discount_youhuiquan)
    private TextView tvOrderinfoYohuiquan;
    @ViewInject(R.id.tv_orderinfo_dianzibi)
    private TextView tvOrderinfoDianzibi;
    @ViewInject(R.id.tv_orderinfo_rand_money)
    private TextView tvOrderinfoRandMoney;

    @ViewInject(R.id.activity_order_info_ll_orderqrcode)
    private LinearLayout llOrderQrcode;
    @ViewInject(R.id.activity_order_info_tv_orderqrcode)
    private ImageView ivOrderQrcode;


    @ViewInject(R.id.zongjia)
    private TextView zongjia;
    @ViewInject(R.id.dingdan)
    private TextView dingdan;
    @ViewInject(R.id.dingdan2)
    private TextView dingdan2;
    @ViewInject(R.id.dingdan1)
    private TextView dingdan1;
    @ViewInject(R.id.fapiao)
    private TextView fapiao;
    @ViewInject(R.id.shangp_z)
    private TextView shangp_z;
    @ViewInject(R.id.jifen_d)
    private TextView jifen_d;
    @ViewInject(R.id.images)
    LinearLayout images;
    @ViewInject(R.id.order_info_stat)
    TextView orderInfoStat;

    private MyAdapter adapter;
    private List<RecommendModel> recommends;
    private static final int REQUEST_CODE_PERMISSION_CALL_PHONE = 1;
    private String order_status = "";
    private String phone = "";
    //客服
//    String settingid0 = "lj_1000_1493167191869";
    String settingid1 = "lj_1001_1496308413541";
    String groupName = "蓝九天";// 客服组默认名称
    private List<OrderCommDto> list;
    private OrderModelInfoMedicine model;

    @Event(value = {R.id.header_return, R.id.order_fuwu, R.id.lianxi, R.id.boda})
    private void onClick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.header_return:
                finish();
                break;

//            case R.id.order_quxiao:
//                if (order_status.equals("D")) {
//                    it = new Intent(ActivityOrderInfo.this, ActivityEvaluate.class);
//                    it.putExtra("order_id", orderId);
//                    it.putExtra("name", name);
//                    it.putExtra("image", image);
//                    startActivity(it);
//                }
//
//                break;

            case R.id.order_fuwu:
                //店铺单退款
                it = ActivityAfterMarket.getIntent(this, model.getORDER_GROUP_ID(), "", model.getLOGISTICS_COMPANY_ID());
                startActivity(it);
                break;
//
            case R.id.lianxi:
                if (model == null) return;
                String trade_id = model.getORDER_TRADE_ID();
                openChat("交易订单号：" + trade_id, "", model.getXiaonengData().getSettingid1(), model.getLOGISTICS_COMPANY(), false, "");
                if (model != null && !AppConfig.GROUP_LJT.equals(model.getINS_USER_ID())) {
                    openChat("交易订单号：" + trade_id, "", model.getXiaonengData().getSettingid1(), model.getLOGISTICS_COMPANY(), false, "");
                } else {
                    openChat("交易订单号：" + trade_id, "", AppConfig.CHAT_XN_LJT_SETTINGID1, AppConfig.CHAT_XN_LJT_TITLE1, false, "");
                }
                break;

            case R.id.boda:
                if (StringUtils.isEmpty(phone)) {
                } else {
                    call();
                }

                break;

            default:
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText(R.string.order_xiangqing);


        setLoding(this, false);
        getInfo(getIntent().getStringExtra("id"), "orderDetail");

        adapter = new MyAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (model == null) return;
                Intent it;
                if (AppConfig.GROUP_LJT.equals(model.getINS_USER_ID())) {
                    it = ActivityMedicinesInfo.getIntent(ActivityOrderInfo.this, adapter.getData().get(i).getCOMMODITY_ID(), model.getLOGISTICS_COMPANY_ID());
                } else {
                    it = ActivityLifeGoodsInfo.getIntent(ActivityOrderInfo.this, adapter.getData().get(i).getCOMMODITY_ID());
                }
                startActivity(it);
            }
        });

    }


    private void getInfo(final String data, final String op) {
        new BizDataAsyncTask<OrderModelInfoMedicine>() {
            @Override
            protected OrderModelInfoMedicine doExecute() throws ZYException, BizFailure {
//                return GoodsBiz.getOrderInfo(data, op);
                return GoodsBiz.getOrderInfo(data);
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            protected void onExecuteSucceeded(OrderModelInfoMedicine orderModelInfoMedicine) {
                closeLoding();
                model = orderModelInfoMedicine;
                StringBuilder err = new StringBuilder();
                MyLocation loc = AddressModel.parseLocation(err, orderModelInfoMedicine.getUSER_ADDRESS_LOCATION());

                //分别显示 地址信息
                address_dizhi.setText(orderModelInfoMedicine.getUSER_ADDRESS_ID());

                dianpu.setText(orderModelInfoMedicine.getLOGISTICS_COMPANY());
                tvShopAddr.setText(getString(R.string.shop_addr) + orderModelInfoMedicine.getORG_ADDRESS());
                tvOrderRemark.setText(orderModelInfoMedicine.getGROUP_REMARKS());

                String postageFlg = orderModelInfoMedicine.getPOSTAGE_FLG();
                if ("1".equals(postageFlg)) {
                    yunfei.setText("￥" + Utils.formatFee(orderModelInfoMedicine.getPOSTAGE() + ""));
                } else {
                    yunfei.setText("￥" + Utils.formatFee("0"));
                }
                tvOrderinfoDianzibi.setText("￥" + Utils.formatFee(orderModelInfoMedicine.getELECTRONIC_MONEY() + ""));
                if (orderModelInfoMedicine.getRAND_SUB_MONEY() != null) {
                    tvOrderinfoRandMoney.setText("￥" + Utils.formatFee(orderModelInfoMedicine.getRAND_SUB_MONEY() + ""));
                } else {
                    tvOrderinfoRandMoney.setText("￥" + "0");
                }
                tvOrderinfoYohuiquan.setText("￥" + Utils.formatFee(orderModelInfoMedicine.getCOUPON_MONEY()));
                dingdan.setText("订单号：" + orderModelInfoMedicine.getORDER_TRADE_ID());
                dingdan1.setText("创建时间：" + DateUtils.parseTime(orderModelInfoMedicine.getCREATE_DATE() + ""));
                shangp_z.setText("(包含邮费" + Utils.formatFee(orderModelInfoMedicine.getPOSTAGE() + "") + "元)￥" + orderModelInfoMedicine.getGROUP_MONEY_ALL() + "");
                zongjia.setText("￥" + orderModelInfoMedicine.getGROUP_PAY_AMOUNT());
                jifen_d.setText(Utils.formatFee(orderModelInfoMedicine.getCOST_POINT() + "") + "积分");
                if (orderModelInfoMedicine.getCOMPLETE_DATE() != null) {
                    dingdan2.setVisibility(View.VISIBLE);
                    dingdan2.setText("完成时间：" + DateUtils.parseTime(orderModelInfoMedicine.getCOMPLETE_DATE() + ""));
                } else {
                    dingdan2.setVisibility(View.GONE);
                }
                if (!StringUtils.isEmpty(orderModelInfoMedicine.getINVOICE_TITLE())) {
                    fapiao.setVisibility(View.VISIBLE);
                    fapiao.setText("发票抬头：" + orderModelInfoMedicine.getINVOICE_TITLE());
                } else {
                    fapiao.setVisibility(View.GONE);
                }

                XiaoNengData xnd = orderModelInfoMedicine.getXiaonengData();
                if (xnd != null) {
                    settingid1 = xnd.getSettingid1();
                    groupName = orderModelInfoMedicine.getOID_GROUP_NAME();
                } else {
                    Utils.log("没有获取小能信息");
                }
                llOrderQrcode.setVisibility(View.GONE);

                String orderId = orderModelInfoMedicine.getORDER_GROUP_ID();
                ActivityOrderInfo.this.phone = orderModelInfoMedicine.getORG_MOBILE();


                list = orderModelInfoMedicine.getDETAILS();
                adapter.setNewData(list);
                adapter.notifyDataSetChanged();


                if (AppConfig.GROUP_LJT.equals(orderModelInfoMedicine.getINS_USER_ID())) {
                    tvShouhou.setVisibility(View.GONE);
                } else {
                    tvShouhou.setVisibility(View.VISIBLE);
                }

                /*String strC = "";
                if (OrderTrade.PAYMTD_MONEY.equals(orderModelInfoMedicine.getPAYMENT_METHOD())) {
                    strC = "(货到付款)";
                }*/
                String statDesc = "未知";
                switch (orderModelInfoMedicine.getSTATUS()) {
                    case "A":
                        images.setBackground(getResources().getDrawable(R.mipmap.daifukuan));
                        statDesc = getString(R.string.daifu);
                        tvShouhou.setVisibility(View.GONE);
                        break;

                    case "B":
                        images.setBackground(getResources().getDrawable(R.mipmap.daifahuo));
                        statDesc = getString(R.string.daifa);
                        tvShouhou.setVisibility(View.GONE);
                        if (!OrderTrade.PAYMTD_MONEY.equals(orderModelInfoMedicine.getPAYMENT_METHOD())) {
                            Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode("2#" + orderId, 150, Color.BLACK, null);
                            ivOrderQrcode.setImageBitmap(bitmap);
                            llOrderQrcode.setVisibility(View.VISIBLE);
                        }
                        break;

                    case "C":
                        images.setBackground(getResources().getDrawable(R.mipmap.daishouhuo));
                        statDesc = getString(R.string.daishou);
                        break;
                    case "S":
                        images.setBackground(getResources().getDrawable(R.mipmap.daipingjia));
                        statDesc = getString(R.string.complete);
                        break;
                    case "D":
                        images.setBackground(getResources().getDrawable(R.mipmap.daipingjia));
                        statDesc = getString(R.string.daiping);
                        break;

                    case "E":
                        images.setBackground(getResources().getDrawable(R.mipmap.guanbi));
                        statDesc = getString(R.string.order_style7);
                        break;

                }

                String payMtd = "";
                switch (orderModelInfoMedicine.getPAYMENT_METHOD()) {
                    case OrderTrade.PAYMTD_ONLINE:
                        payMtd = "在线支付";
                        break;
                    case OrderTrade.PAYMTD_MONEY:
                        payMtd = "货到付款";
                        break;
                    case OrderTrade.PAYMTD_WECHAT:
                        payMtd = "微信支付";
                        break;
                    case OrderTrade.PAYMTD_ALI:
                        payMtd = "支付宝支付";
                        break;
                    case OrderTrade.PAYMTD_UNION:
                        payMtd = "银联支付";
                        break;
                    case OrderTrade.PAYMTD_ACCOUNT:
                        payMtd = "余额支付";
                        break;
                }
                if (!StringUtils.isEmpty(payMtd)) {
                    payMtd = "(" + payMtd + ")";
                }

                orderInfoStat.setText(statDesc + payMtd);
                /*
                Bitmap logoBitmap = ((BitmapDrawable) getDrawable(R.drawable.ic_biaozhi)).getBitmap();
                int start = 3;
                Bitmap logo = Bitmap.createBitmap(logoBitmap.getWidth() + start * 2, logoBitmap.getHeight() + start * 2, Bitmap.Config.RGB_565);
                logo.eraseColor(Color.WHITE);
                Canvas canvas = new Canvas(logo);
                Paint p = new Paint();
                p.setColor(Color.WHITE);
                canvas.drawBitmap(logoBitmap, start, start, p);
                canvas.save(Canvas.ALL_SAVE_FLAG);
                canvas.restore();
*/

            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    BizDataAsyncTask<Integer> deleteTask;

    private void delete(final String data) {
        deleteTask = new BizDataAsyncTask<Integer>() {
            @Override
            protected Integer doExecute() throws ZYException, BizFailure {
                Log.e("trade_id", data);
                return GoodsBiz.orderOperation(data, "delOrder");
            }

            @Override
            protected void onExecuteSucceeded(Integer integer) {
                if (integer == 3) {
                    ToastUtil.show(ActivityOrderInfo.this, getResources().getString(R.string.delete_succeed));
                    Intent it = new Intent(ActivityOrderInfo.this, ActivityOrder.class);
                    if (order_status.equals("A")) {
                        it.putExtra("index", 1);
                    } else if (order_status.equals("B")) {
                        it.putExtra("index", 2);
                    } else if (order_status.equals("C")) {
                        it.putExtra("index", 3);
                    } else if (order_status.equals("D")) {
                        it.putExtra("index", 4);
                    } else {
                        it.putExtra("index", 0);
                    }
                    Log.e("asdasdasda", order_status);
                    finish();
                    AppManager.destoryActivity("order");
                    startActivity(it);
                }
            }

            @Override
            protected void OnExecuteFailed() {

            }
        };
        deleteTask.execute();
    }


    BizDataAsyncTask<List<RecommendModel>> recTask;

//    private void getrecommend(final String token, final String orderid) {
//        recTask = new BizDataAsyncTask<List<RecommendModel>>() {
//            @Override
//            protected List<RecommendModel> doExecute() throws ZYException, BizFailure {
//                return HomeBiz.getRecommend( token, orderid );
//            }
//
//            @Override
//            protected void onExecuteSucceeded(List<RecommendModel> recommendModels) {
//                recommends = recommendModels;
//                adapter1.setNewData( recommends );
//                adapter1.notifyDataSetChanged();
//            }
//
//            @Override
//            protected void OnExecuteFailed() {
//
//            }
//        };
//        recTask.execute();
//    }


    private class MyAdapter1 extends BaseQuickAdapter<RecommendModel> {

        public MyAdapter1() {
            super(R.layout.item_goods_grid, recommends);
        }


        @Override
        protected void convert(BaseViewHolder baseViewHolder, RecommendModel recommendModel) {
            baseViewHolder.setText(R.id.goods_title, StringUtils.deletaFirst(recommendModel.getWARENAME()))
                    .setText(R.id.money, recommendModel.getSALEPRICE() + "");

            SimpleDraweeView goods_img = baseViewHolder.getView(R.id.item_head_img);
            if (StringUtils.isEmpty(recommendModel.getIMG_PATH())) {
                goods_img.setImageURI(Uri.parse(""));
            } else {
                goods_img.setImageURI(Uri.parse(recommendModel.getIMG_PATH()));
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //权限成功
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.e("camera", "成功获取权限");
    }

    //权限失败
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this, "需要开启此权限")
                    .setTitle(getString(R.string.alert_tishi))
                    .setPositiveButton("设置")
                    .setNegativeButton("取消", null)
                    .setRequestCode(100)
                    .build()
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Log.e("*****", "权限获得");
        }
    }


    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_CALL_PHONE)
    private void call() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CALL_PHONE)) {
            Log.e(AppConfig.ERR_TAG, "是否有权限" + EasyPermissions.hasPermissions(this, Manifest.permission.CALL_PHONE) + "");
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityOrderInfo.this);
            builder.setMessage("是否拨打电话：" + phone);
            builder.setNegativeButton("取消", null);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + phone));
                    //开启系统拨号器
                    startActivity(intent);
                }
            });
            builder.create().show();
        } else {
            EasyPermissions.requestPermissions(this, "需要以下权限:拨打电话。", REQUEST_CODE_PERMISSION_CALL_PHONE, Manifest.permission.CALL_PHONE);
        }
    }

    //商品adapter
    private class MyAdapter extends BaseQuickAdapter<OrderCommDto> {
        public MyAdapter() {
            super(R.layout.item_orderinfor_goods, list);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, final OrderCommDto goods) {
            if (StringUtils.isEmpty(goods.getCOMMODITY_COLOR())) {
                baseViewHolder.setText(R.id.size, " 规格：" + goods.getCOMMODITY_SIZE());
            } else {
                baseViewHolder.setText(R.id.size, "产地：" + goods.getCOMMODITY_COLOR() + " ;规格：" + goods.getCOMMODITY_SIZE());
            }
            if (AppConfig.GROUP_LJT.equals(model.getINS_USER_ID()) && "1".equals(goods.getIS_RETURNGOODS())) {
                baseViewHolder.setVisible(R.id.shouhou, true);
            } else {
                baseViewHolder.setVisible(R.id.shouhou, false);
            }
            String title = StringUtils.deletaFirst(goods.getCOMMODITY_NAME());
            if (!StringUtils.isEmpty(goods.getGIVEAWAY())) {
                title += "(" + goods.getGIVEAWAY() + ")";
            }
            baseViewHolder.setText(R.id.title, title)
                    .setText(R.id.money, "￥" + Utils.formatFee("" + goods.getCOST_MONEY()))
                    .setText(R.id.number, "X" + goods.getEXCHANGE_QUANLITY())
                    .setOnClickListener(R.id.shouhou, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent it = ActivityAfterMarket.getIntent(ActivityOrderInfo.this, model.getORDER_GROUP_ID(), goods.getCOMMODITY_ORDER_ID(), model.getLOGISTICS_COMPANY_ID());
                            startActivity(it);
                        }
                    });

            SimpleDraweeView simpleDraweeView = baseViewHolder.getView(R.id.item_orderinfo_goods_pic);
            if (!StringUtils.isEmpty(goods.getIMG_PATH())) {
                simpleDraweeView.setImageURI(Uri.parse(goods.getIMG_PATH()));
            }
        }
    }
}
