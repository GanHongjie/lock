package com.yiyun.lockcontroller.bean.lock;

import com.yiyun.lockcontroller.utils.StringUtil;

/**
 * Created by Layo on 2017-8-6.
 */

public class LockInitKeyBean {
    private String cipher;
    private String sessionKey;
    private String initKey;

    public LockInitKeyBean(String cipher, String sessionKey, String initKey) {
        this.cipher = cipher;
        this.sessionKey = sessionKey;
        this.initKey = initKey;
    }

    public String getCipher() {
        return cipher;
    }

    public void setCipher(String cipher) {
        this.cipher = cipher;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getInitKey() {
        return initKey;
    }

    public void setInitKey(String initKey) {
        this.initKey = initKey;
    }

    public String getAssicInitKey() {
        return StringUtil.str2ASSStr16(getInitKey());
    }

    public String getAssicSessionKey() {
        return StringUtil.str2ASSStr16(getSessionKey());
    }
}
