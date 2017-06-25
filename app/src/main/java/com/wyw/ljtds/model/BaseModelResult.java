package com.wyw.ljtds.model;

import com.google.gson.JsonObject;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class BaseModelResult extends BaseModel {
    private String message;
    private JsonObject result;
    private boolean status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonObject getResult() {
        return result;
    }

    public void setResult(JsonObject result) {
        this.result = result;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
