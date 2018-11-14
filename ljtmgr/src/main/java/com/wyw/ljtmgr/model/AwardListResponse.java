package com.wyw.ljtmgr.model;

import java.util.List;

/**
 * Created by wsy on 18-1-9.
 */

public class AwardListResponse extends ServerResponse {
    private String mOrderStat;
    private List<AwardModel> listRet;

    public String getmOrderStat() {
        return mOrderStat;
    }

    public void setmOrderStat(String mOrderStat) {
        this.mOrderStat = mOrderStat;
    }

    public List<AwardModel> getListRet() {
        return listRet;
    }

    public void setListRet(List<AwardModel> listRet) {
        this.listRet = listRet;
    }
}
