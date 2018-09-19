package com.yiyun.lockcontroller.net;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;


/**
 * Created by Layo on 2018-1-2.
 */

public interface EidNetService {

    //eidSMS身份识别
    @POST(EidNetConstant.URLContextSMS.REAL_NAME)
    Observable<JsonObject> realNameWithSMS(@Body JsonObject jsonObject);

    //eidSMS匿名认证
    @POST(EidNetConstant.URLContextSMS.DIRECT_LOGIN)
    Observable<JsonObject> loginWithSMS(@Body JsonObject jsonObject);

    //eidOMA身份识别
    @POST(EidNetConstant.URLContextOMA.REAL_NAME)
    Observable<JsonObject> realNameWithOMA(@Body JsonObject jsonObject);

    //OMA请求签名
    @POST(EidNetConstant.URLContextOMA.GET_APDU)
    Observable<JsonObject> getAPDUWithOMA(@Body JsonObject jsonObject);

    //eidOMA匿名认证
    @POST(EidNetConstant.URLContextOMA.DIRECT_LOGIN)
    Observable<JsonObject> loginWithOMA(@Body JsonObject jsonObject);

}
