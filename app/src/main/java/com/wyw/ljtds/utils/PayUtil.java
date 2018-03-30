package com.wyw.ljtds.utils;

import android.app.Activity;
import android.os.Handler;
import android.content.Context;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.PayResult;
import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.utils.Log;
import com.unionpay.UPPayAssistEx;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.model.OnlinePayModel;
import com.wyw.ljtds.ui.goods.ActivityGoodsSubmit;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by wsy on 18-3-16.
 */
public class PayUtil {
    public static final int ALI_PAY = 1;

    private Activity context;
    private Map data;

    public PayUtil(Activity context, Map data) {
        this.context = context;
        this.data = data;
    }

    /**
     * 支付宝支付
     */
    public void pay4Ali(final Handler mHandler) {
        try {
            Map aliData = this.data;
            //支付宝
            final OnlinePayModel pm = GsonUtils.Json2Bean(GsonUtils.Bean2Json(aliData), OnlinePayModel.class);
            final String orderInfo = pm.getPay();
            if (!StringUtils.isEmpty(orderInfo)) {
                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        //支付宝
                        PayTask alipay = new PayTask(context);
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
        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.log("JsonSyntaxException:" + ex.getMessage());
            ToastUtil.show(context, "服务器数据格式有错误");
        }
    }

    public void pay4Union() {
        try {
            //银联
            final OnlinePayModel pm = GsonUtils.Json2Bean(GsonUtils.Bean2Json(data), OnlinePayModel.class);
            final String orderInfo = pm.getPay();
            //调用银联控件
            if (!StringUtils.isEmpty(orderInfo)) {
                Map m = GsonUtils.Json2Bean(orderInfo, Map.class);
                String tn = (String) m.get("tn");
                Log.e(AppConfig.ERR_TAG, tn);
                UPPayAssistEx.startPay(context, null, null, tn, "00");
            }
//                        }
//                    };
//
//                    Thread payThread = new Thread( payRunnable );
//                    payThread.start();
        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.log("JsonSyntaxException:" + ex.getMessage());
            ToastUtil.show(context, "服务器数据格式有错误");
        }
    }

    public void pay4Wechat() {
        try {
            IWXAPI wxApi = ((MyApplication) context.getApplication()).wxApi;
            Map wechatData = this.data;
            PayReq request = new PayReq();
            request.appId = AppConfig.WEIXIN_APP_ID;
            request.partnerId = wechatData.get("partnerid").toString();
            request.prepayId = wechatData.get("prepayid").toString();
            request.packageValue = "Sign=WXPay";
            request.nonceStr = "" + wechatData.get("noncestr");
            request.timeStamp = wechatData.get("timestamp").toString();
            request.sign = "" + wechatData.get("sign");

            wxApi.sendReq(request);
        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.log("JsonSyntaxException:" + ex.getMessage());
            ToastUtil.show(context, "服务器数据格式有错误");
        }
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
