package com.wyw.ljtds.model;

/**
 * Created by wsy on 17-8-7.
 */

public class DianZiBiLog {
    private String ELECTRONIC_MONEY;
    private String ELECTRONIC_USEABLE_MONEY;
    private String ORDER_TRADE_ID;
    private String INS_DATE;

    public String getORDER_TRADE_ID() {
        return ORDER_TRADE_ID;
    }

    public void setORDER_TRADE_ID(String ORDER_TRADE_ID) {
        this.ORDER_TRADE_ID = ORDER_TRADE_ID;
    }

    public String getINS_DATE() {
        return INS_DATE;
    }

    public void setINS_DATE(String INS_DATE) {
        this.INS_DATE = INS_DATE;
    }

    public String getELECTRONIC_MONEY() {
        return ELECTRONIC_MONEY;
    }

    public void setELECTRONIC_MONEY(String ELECTRONIC_MONEY) {
        this.ELECTRONIC_MONEY = ELECTRONIC_MONEY;
    }

    public String getELECTRONIC_USEABLE_MONEY() {
        return ELECTRONIC_USEABLE_MONEY;
    }

    public void setELECTRONIC_USEABLE_MONEY(String ELECTRONIC_USEABLE_MONEY) {
        this.ELECTRONIC_USEABLE_MONEY = ELECTRONIC_USEABLE_MONEY;
    }
}
