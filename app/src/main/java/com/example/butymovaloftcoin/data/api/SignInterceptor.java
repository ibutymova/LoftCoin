package com.example.butymovaloftcoin.data.api;

import com.example.butymovaloftcoin.BuildConfig;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class SignInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request.Builder requestBuilder = chain.request().newBuilder();
        requestBuilder.addHeader("Accepts", "application/json");
        requestBuilder.addHeader("X-CMC_PRO_API_KEY", BuildConfig.API_KEY);
        return chain.proceed(requestBuilder.build());
    }
}
