package com.yiyun.lockcontroller.net;

import com.yiyun.lockcontroller.BuildConfig;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Layo on 2018-1-2.
 */

public class EidNetHelper {
    // 超时时间
    private static final int DEFAULT_TIMEOUT = 30;

    private Retrofit mRetrofit;

    private EidNetHelper() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(EidNetConstant.ROOT_TEST)
                .client(initOkHttp())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static EidNetService getInstance() {
        return EidNetHelper.SingletonHolder.INSTANCE.create();
    }

    private EidNetService create() {
        return mRetrofit.create(EidNetService.class);
    }

    private OkHttpClient initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 网络请求日志
        if (BuildConfig.DEBUG) {
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
        private static final EidNetHelper INSTANCE = new EidNetHelper();
    }
}
