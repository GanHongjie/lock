package com.yiyun.lockcontroller.net.lock;


import android.util.Log;

import com.yiyun.lockcontroller.bean.lock.LockDownBean;
import com.yiyun.lockcontroller.utils.SPUtil;
import com.yiyun.lockcontroller.utils.rc4.AES;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.orhanobut.logger.Logger;

import static com.yiyun.lockcontroller.utils.ConstantsUtil.USER_AES;
import static com.yiyun.lockcontroller.utils.ConstantsUtil.USER_NAME;

/**
 * 得到的字符串转Json数据
 * Created by Layo on 2017-7-17.
 */
public class PublicGetJsonObject {
    private LockDownBean.UnEncryptBean unEncrypt;       //明文参数
    private EncryptBean encrypt;         //密文参数

    public PublicGetJsonObject(LockDownBean lockDownBean) {
        getJsonData(lockDownBean);
    }

    private void getJsonData(LockDownBean lockDownBean) {
        unEncrypt = lockDownBean.getUnEncrypt();
        try {
            String encryptStr = lockDownBean.getEncrypt();
            String username = SPUtil.getInstance().getString(USER_NAME);
            String aes = SPUtil.getInstance().getString( USER_AES);

            if (!encryptStr.equals("")) {
                encrypt = new Gson().fromJson(AES.decrypt(aes, encryptStr), EncryptBean.class);
                Log.i("lock", "解密数据部分");
                Logger.json(AES.decrypt(aes, encryptStr));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("协议格式有误");
        }
    }

    /**
     * @return 明文区的数据
     */
    public JsonObject getUnData() {
        return unEncrypt.getData();
    }

    /**
     * @return 密文区的数据
     */
    public JsonObject getEnData() {
        return encrypt.getData();
    }

    /**
     * @return 密文区的list
     */
    public JsonArray getList() {
        return encrypt.getList();
    }

    private class EncryptBean {
        private JsonObject data;
        private JsonArray list = new JsonArray();
        private String r;

        public JsonObject getData() {
            return data;
        }

        public void setData(JsonObject data) {
            this.data = data;
        }

        public JsonArray getList() {
            return list;
        }

        public void setList(JsonArray list) {
            this.list = list;
        }

        public String getR() {
            return r;
        }

        public void setR(String r) {
            this.r = r;
        }
    }

}
