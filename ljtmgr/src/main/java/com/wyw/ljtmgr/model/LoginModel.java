package com.wyw.ljtwl.model;

/**
 * Created by Administrator on 2017/8/1.
 */
public class LoginModel extends ServerResponse {
    private String adminUserId;

    private String oidGroupId;

    private String userName;

    private String passWord;

    private String token;

    private String adminUserName;

    private String mos;

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
}
