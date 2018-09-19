package com.yiyun.lockcontroller.net;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.yiyun.lockcontroller.App;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by joker on 2017/7/12.
 */

public class NetHelper {
    // 超时时间
    private static final int DEFAULT_TIMEOUT = 90;
//    private static final String BASE_URL = "http://123.57.150.91:9999/LockClientServer/";
//    private static final String BASE_URL = "http://192.168.0.105:8080/"; // FloatPopulaManager/
    private static final String BASE_URL ="http://kingeid.iego.cn/FloatPopulaManager/"; // "http://kingeid.nat300.top/FloatPopulaManager/";


    private Retrofit mRetrofit;

    private NetHelper() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(initOkHttp())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static NetService getInstance() {
        return SingletonHolder.INSTANCE.create();
    }

    private NetService create() {
        return mRetrofit.create(NetService.class);
    }

    private OkHttpClient initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 网络请求日志
        if (App.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }
        return builder
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    private static class SingletonHolder {
        private static final NetHelper INSTANCE = new NetHelper();
    }
}
