package com.yiyun.lockcontroller.bean.lock;

import com.google.gson.JsonObject;

/**
 * lock接受网络数据的时候
 * Created by layo on 2017/7/21.
 */

public class LockDownBean {

    private String encrypt;
    private UnEncryptBean unEncrypt;

    public String getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(String encrypt) {
        this.encrypt = encrypt;
    }

    public UnEncryptBean getUnEncrypt() {
        return unEncrypt;
    }

    public void setUnEncrypt(UnEncryptBean unEncrypt) {
        this.unEncrypt = unEncrypt;
    }

    public static class UnEncryptBean {

        private JsonObject data;

        public JsonObject getData() {
            return data;
        }

        public void setData(JsonObject data) {
            this.data = data;
        }

    }

    @Override
    public String toString() {
        return "LockDownBean{" +
                "encrypt='" + encrypt + '\'' +
                ", unEncrypt=" + unEncrypt +
                '}';
    }
}

