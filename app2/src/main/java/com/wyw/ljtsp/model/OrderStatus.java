package com.wyw.ljtsp.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/1.
 */

public class OrderStatus {

    public static final String ALL = "";//全部

    public static final String UNPAY = "0";//未付款

    public static final String TOSHIPPED = "1";//待发货

    public static final String SHIPPED = "2";//已发货

    public static final String RECEIVED = "3";//已签收

    public static final String LOGISTICSALLOCATED = "4";//物流已分配

    public static final String LOGISTICSSHIPPED = "5";//物流已发货

    public static final String LOGISTICSSERVICE = "6";//物流已送达
    public static final String TRADECANCEL = "7";//物流已取消

    public static final String UNCONFIRM = "8";//未确认

    public static final String APPLYRETURNED = "9";//申请换退货

    public static final String AGREERETURNED = "10";//同意换退货

    public static final String REFUSERETURNED = "11";//拒绝换退货

    public static final String RETURNED = "12";//已退货

    public static final String REFUNDED = "13";//已退款

    public static final String REJECTEDGOODS = "15";//客户拒收

    public static final String COMPLETED = "S";//已完成

    public static String getStatus(String status){
        String cStatus = "";
        switch (status){
            case UNPAY:
                cStatus="未付款";
                break;
            case TOSHIPPED:
                cStatus="待发货";
                break;
            case SHIPPED:
                cStatus="已发货";
                break;
            case RECEIVED:
                cStatus="已签收";
                break;
            case LOGISTICSALLOCATED:
                cStatus="物流已分配";
                break;
            case LOGISTICSSHIPPED:
                cStatus="物流已发货";
                break;
            case LOGISTICSSERVICE:
                cStatus="物流已送达";
                break;
            case TRADECANCEL:
                cStatus="物流已取消";
                break;
            case UNCONFIRM:
                cStatus="未确认";
                break;
            case APPLYRETURNED:
                cStatus="申请换退货";
                break;
            case AGREERETURNED:
                cStatus="同意换退货";
                break;
            case REFUSERETURNED:
                cStatus="拒绝换退货";
                break;
            case RETURNED:
                cStatus="已退货";
                break;
            case REFUNDED:
                cStatus="已退款";
                break;
            case REJECTEDGOODS:
                cStatus="客户拒收";
                break;
            case COMPLETED:
                cStatus="已完成";
                break;
        }
        return  cStatus;
    }
}
