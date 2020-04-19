package com.example.butymovaloftcoin.data.api;

import com.example.butymovaloftcoin.data.api.model.RateResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    @GET("listings/latest")
    Single<RateResponse> listings_latest(@Query("convert") String convert);
}
