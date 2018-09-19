package com.yiyun.lockcontroller.net;

import com.yiyun.lockcontroller.bean.lock.LockDownBean;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


/**
 * Created by joker on 2017/7/12.
 */

public interface NetService {
    String LOCK_URL_PREFIX = "rest/app/";

    //eidSMS身份识别
    @POST(LOCK_URL_PREFIX + "eIDSMSRegistry")
    @FormUrlEncoded
    Observable<JsonObject> realNameWithSMS(@Field("data") String aes);

    //eidSMS匿名认证
    @POST(LOCK_URL_PREFIX + "eIDSMSLogin")
    @FormUrlEncoded
    Observable<HTTPResult<LockDownBean>> loginWithSMS(@Field("data") String aes);

    //eidOMA身份识别
    @POST(LOCK_URL_PREFIX + "eIDOMARegistry")
    @FormUrlEncoded
    Observable<JsonObject> realNameWithOMA(@Field("data") String aes);

    //OMA请求签名
    @POST(LOCK_URL_PREFIX + "signapduOfOMA")
    @FormUrlEncoded
    Observable<HTTPResult<LockDownBean>> getAPDUWithOMA(@Field("data") String aes);

    //eidOMA匿名认证
    @POST(LOCK_URL_PREFIX + "eIDOMALogin")
    @FormUrlEncoded
    Observable<HTTPResult<LockDownBean>> loginWithOMA(@Field("data") String aes);

    //获取PKI公钥
    @POST(LOCK_URL_PREFIX + "getPki")
    Observable<HTTPResult<LockDownBean>> getPki(@Body String aes);

    //判断版本号
    @POST(LOCK_URL_PREFIX + "judgeVersion")
    @FormUrlEncoded
    Observable<HTTPResult<LockDownBean>> judgeVersion(@Field("data") String aes);

    //登录并同步AES
    @POST(LOCK_URL_PREFIX + "appLogin")
    @FormUrlEncoded
    Observable<HTTPResult<LockDownBean>> appLogin(@Field("data") String aes);

    //注册用户
    @POST(LOCK_URL_PREFIX + "bindStuInfo")
    @FormUrlEncoded
    Observable<HTTPResult<LockDownBean>> bindStuInfo(@Field("data") String aes);

    //注册用户
    @POST(LOCK_URL_PREFIX + "appRegistry")
    @FormUrlEncoded
    Observable<HTTPResult<LockDownBean>> appRegistry(@Field("data") String aes);

    //开锁
    @POST(LOCK_URL_PREFIX + "openLock")
    @FormUrlEncoded
    Observable<HTTPResult<LockDownBean>> openLock(@Field("data") String aes);

    //查询寝室更换状态
    @POST(LOCK_URL_PREFIX + "searchReplace")
    @FormUrlEncoded
    Observable<HTTPResult<LockDownBean>> queryDorm(@Field("data") String aes);

    //申请更换寝室
    @POST(LOCK_URL_PREFIX + "replaceDep")
    @FormUrlEncoded
    Observable<HTTPResult<LockDownBean>> replaceDorm(@Field("data") String aes);

    //取消更换寝室
    @POST(LOCK_URL_PREFIX + "cancelReplace")
    @FormUrlEncoded
    Observable<HTTPResult<LockDownBean>> cancelDorm(@Field("data") String aes);

    //查询被授权开锁人信息
    @POST(LOCK_URL_PREFIX + "searchTAuthorLog")
    @FormUrlEncoded
    Observable<HTTPResult<LockDownBean>> searchAuto(@Field("data") String aes);

    //授权他人临时开锁
    @POST(LOCK_URL_PREFIX + "autoUser")
    @FormUrlEncoded
    Observable<HTTPResult<LockDownBean>> autoUser(@Field("data") String aes);

    //删除授权他人临时开锁接口
    @POST(LOCK_URL_PREFIX + "autoDelete")
    @FormUrlEncoded
    Observable<HTTPResult<LockDownBean>> autoDelete(@Field("data") String aes);

    //查询开锁历史
    @POST(LOCK_URL_PREFIX + "searchOpenLog")
    @FormUrlEncoded
    Observable<HTTPResult<LockDownBean>> searchOpenLog(@Field("data") String aes);

    //查询我的钥匙
    @POST(LOCK_URL_PREFIX + "searchMyKeys")
    @FormUrlEncoded
    Observable<HTTPResult<LockDownBean>> searchMyKeys(@Field("data") String aes);

    //学生报修接口
    @POST(LOCK_URL_PREFIX + "repairLock")
    @FormUrlEncoded
    Observable<HTTPResult<LockDownBean>> repairLock(@Field("data") String aes);

    //查询报修状况
    @POST(LOCK_URL_PREFIX + "searchRepair")
    @FormUrlEncoded
    Observable<HTTPResult<LockDownBean>> searchRepair(@Field("data") String aes);

    //取消报修接口
    @POST(LOCK_URL_PREFIX + "cancelRepair")
    @FormUrlEncoded
    Observable<HTTPResult<LockDownBean>> cancelRepair(@Field("data") String aes);
}
