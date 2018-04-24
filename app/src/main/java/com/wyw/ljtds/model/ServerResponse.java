package com.wyw.ljtds.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/5 0005.
 * 服务器返回的数据
 */

public class ServerResponse extends OrderGroupDto {
    /**
     * 成功
     */
    public static final String STAT_OK = "0";
    public static final String STAT_USER_MSG = "3";
    /**
     * 微信登录,未完成手机号绑定 ???
     */
    public static final String STAT_LOGINWX_ERR = "ZC001";

    private Boolean status; //成功状态
    private String message; //错误信息
    private String result; //数据结果
    private String statusCode; //错误标识吗

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}