package com.wyw.ljtmgr.model;

/**
 * Created by wsy on 18-1-9.
 */

public class ServerResponse {
    public static final String OK = "0";
    public static final String ERR = "2";
    public static final String TOKEN_ERR = "3";
    public static final String TOKEN_DEP = "3";
    private String success;
    private String msg;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
