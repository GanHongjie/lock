package com.yiyun.lockcontroller.bean.lock;

/**
 * Created by Layo on 2017-7-11.
 */
public class LockBean {
    private String lockNo;  //锁编号
    private String cipher;  //锁具产生的会话密文

    public LockBean(String lockNo, String cipher) {
        this.lockNo = lockNo;
        this.cipher = cipher;
    }

    public String getLockNo() {
        return lockNo;
    }

    public void setLockNo(String lockNo) {
        this.lockNo = lockNo;
    }

    public String getCipher() {
        return cipher;
    }

    public void setCipher(String cipher) {
        this.cipher = cipher;
    }
}
