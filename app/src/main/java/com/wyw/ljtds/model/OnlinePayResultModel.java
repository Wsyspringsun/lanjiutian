package com.wyw.ljtds.model;

/**
 * Created by Administrator on 2017/4/28 0028.
 */

public class OnlinePayResultModel extends BaseModel {
    private String ORDER_TRADE_ID;
    private String alipay_trade_app_pay_response;

    public String getORDER_TRADE_ID() {
        return ORDER_TRADE_ID;
    }

    public void setORDER_TRADE_ID(String ORDER_TRADE_ID) {
        this.ORDER_TRADE_ID = ORDER_TRADE_ID;
    }

    public String getAlipay_trade_app_pay_response() {
        return alipay_trade_app_pay_response;
    }

    public void setAlipay_trade_app_pay_response(String alipay_trade_app_pay_response) {
        this.alipay_trade_app_pay_response = alipay_trade_app_pay_response;
    }
}
