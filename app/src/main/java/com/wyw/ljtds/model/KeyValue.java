package com.wyw.ljtds.model;

/**
 * Created by wsy on 17-8-11.
 */

public class KeyValue {
    public KeyValue(String k, Object v) {
        this.k = k;
        this.v = v;
    }

    private String k;
    private Object v;

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public Object getV() {
        return v;
    }

    public void setV(Object v) {
        this.v = v;
    }

    @Override
    public String toString() {
        return String.valueOf(v);
    }
}
