package com.wyw.ljtds.model;

/**
 * Created by wsy on 17-8-14.
 */

public class XiaoNengData {
    private String settingid1 = "";
    private String groupId = "";// 店铺 id
    private String sellerid = "";// 商户id,平台版企业(B2B2C企业)使用此参数，B2C企业此参数传""

    public String getSettingid1() {
        return settingid1;
    }

    public void setSettingid1(String settingid1) {
        this.settingid1 = settingid1;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSellerid() {
        return sellerid;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid;
    }
}
