package com.wyw.ljtsp.model;

/**
 * Created by Administrator on 2017/8/29.
 */

public class PayStatus {
    public static final String ONLINE = "0";

    public static final String COD = "C";

    public static final String BALANCE = "2";

    public static final String ALIPAY = "3";

    public static final String UNION = "4";

    public static final String MIDLine = "5";

    public static final String OTHERLine = "6";

    public static String getStatus(String status){
        String returnStatus = "";
        switch (status){
            case ONLINE :
                returnStatus = "在线支付";
                break;
            case COD:
                returnStatus = "货到付款";
                break;
            case BALANCE:
                returnStatus = "余额支付";
                break;
            case ALIPAY:
                returnStatus = "支付宝";
                break;
            case UNION:
                returnStatus = "银联";
                break;
            case MIDLine:
                returnStatus = "线下医保卡";
                break;
            case OTHERLine:
                returnStatus = "线下其他";
                break;
        }
        return  returnStatus;
    }
}
