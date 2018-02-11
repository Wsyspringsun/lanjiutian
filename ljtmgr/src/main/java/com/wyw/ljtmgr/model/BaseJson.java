package com.wyw.ljtwl.model;

/**
 * Created by Administrator on 2017/8/2.
 */

public class BaseJson<T> {

    private Header head;

    private T body;

    public Header getHead() {
        return head;
    }

    public void setHead(Header head) {
        this.head = head;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
