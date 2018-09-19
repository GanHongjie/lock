package com.yiyun.lockcontroller.net.lock;

import android.content.Context;

import com.yiyun.lockcontroller.utils.SPUtil;
import com.yiyun.lockcontroller.utils.rc4.AES;
import com.yiyun.lockcontroller.utils.rc4.RSAUtil;
import com.google.gson.JsonObject;
import com.orhanobut.logger.Logger;

import static com.yiyun.lockcontroller.net.lock.PublicPutParameter.APP_R;
import static com.yiyun.lockcontroller.net.lock.PublicPutParameter.APP_USER_EID;
import static com.yiyun.lockcontroller.net.lock.PublicPutParameter.APP_VERSION;
import static com.yiyun.lockcontroller.utils.ConstantsUtil.USER_AES;
import static com.yiyun.lockcontroller.utils.ConstantsUtil.USER_PKI;

/**
 * 包含两个jsonObject unencrypt和encrypt
 * Created by Layo on 2017-7-10.
 */
public class PublicPutJsonObject {
    private JsonObject unEncrypt = new JsonObject();       //明文参数
    private JsonObject encrypt = new JsonObject();         //密文参数
    private JsonObject unData = new JsonObject();
    private JsonObject enData = new JsonObject();

    public PublicPutJsonObject(Context context) {
        PublicPutParameter publicParameter = new PublicPutParameter();
        encrypt.addProperty(APP_R, publicParameter.getR());
        unEncrypt.addProperty(APP_VERSION, publicParameter.getVersion(context));
        unEncrypt.addProperty(APP_USER_EID, publicParameter.getStuEid());
    }


    public JsonObject getUnData() {
        return unData;
    }

    public void setUnData(JsonObject unData) {
        this.unData = unData;
    }

    public JsonObject getEnData() {
        return enData;
    }

    public void setEnData(JsonObject enData) {
        this.enData = enData;
    }

    public JsonObject getUnEncrypt() {
        return unEncrypt;
    }

    public void setUnEncrypt(JsonObject unEncrypt) {
        this.unEncrypt = unEncrypt;
    }

    public JsonObject getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(JsonObject encrypt) {
        this.encrypt = encrypt;
    }

    /**
     * 返回将unencrypt和encrypt合成到一个jsonObject
     *
     * @return jsonObject.toString()
     */
    @Override
    public String toString() {
        JsonObject jsonObject = new JsonObject();
        unEncrypt.add("data", unData);
        encrypt.add("data", enData);
        jsonObject.add("unEncrypt", unEncrypt);
        jsonObject.add("encrypt", encrypt);
        return jsonObject.toString();
    }

    /**
     * 返回将unencrypt和encrypt合成到一个jsonObject
     * RSA加密后返回
     *
     * @return jsonObject.toString()
     */
    public String toStringRSA() {
        JsonObject jsonObject = new JsonObject();
        unEncrypt.add("data", unData);
        encrypt.add("data", enData);
        jsonObject.add("unEncrypt", unEncrypt);
        try {
            String pki = SPUtil.getInstance().getString(USER_PKI);
            jsonObject.addProperty("encrypt", RSAUtil.encrypt(pki, encrypt.toString()));
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("RSA错误");
        }

        return jsonObject.toString();
    }

    /**
     * 返回将unencrypt和encrypt合成到一个jsonObject
     * AES加密后返回
     *
     * @return jsonObject.toString()
     */
    public String toStringAES() {
        JsonObject jsonObject = new JsonObject();
        unEncrypt.add("data", unData);
        encrypt.add("data", enData);
        jsonObject.add("unEncrypt", unEncrypt);
        try {
            jsonObject.addProperty("encrypt", AES.encrypt(SPUtil.getInstance().getString(USER_AES), encrypt.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
}
