package com.wyw.ljtmgr.model;

/**
 * Created by Administrator on 2017/8/1.
 */
public class LoginModel extends ServerResponse {
    public static final String VALIDFLG_DIANZHANG = "1";
    private String adminUserId; //登录的帐号 手机号码

    private String oidGroupId;

    private String userName;

    private String passWord;

    private String token;

    private String adminUserName;

    private String mos;

    private String validFlg; //是否是店长1:是 0:不是

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAdminUserName() {
        return adminUserName;
    }

    public void setAdminUserName(String adminUserName) {
        this.adminUserName = adminUserName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getMos() {
        return mos;
    }

    public void setMos(String mos) {
        this.mos = mos;
    }

    public String getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(String adminUserId) {
        this.adminUserId = adminUserId;
    }

    public String getOidGroupId() {
        return oidGroupId;
    }

    public void setOidGroupId(String oidGroupId) {
        this.oidGroupId = oidGroupId;
    }

    public String getValidFlg() {
        return validFlg;
    }

    public void setValidFlg(String validFlg) {
        this.validFlg = validFlg;
    }
}
