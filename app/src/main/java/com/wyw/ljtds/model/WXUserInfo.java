package com.wyw.ljtds.model;

/**
 * Created by wsy on 17-8-7.
 * 微信认证后获取的微信用户信息
 */
public class WXUserInfo {
    private String wxId;//微信id
    private String nickName; // 昵称
    private String sex; //性别

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
