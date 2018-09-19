package com.yiyun.lockcontroller.net;

/**
 * Created by layo on 2017/7/17.
 */

public class HTTPResult<T> {
    private int errCode;
    private int state;
    private T data;

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
