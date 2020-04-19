package com.example.butymovaloftcoin.data.api;

import com.google.gson.Gson;
import com.example.butymovaloftcoin.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiInitializer {
    private static final String BASE_URL = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/";

    private SignInterceptor signInterceptor;

    public ApiInitializer(SignInterceptor signInterceptor) {
        this.signInterceptor = signInterceptor;
    }

    public Api init(){
        Gson gson = createGson();
        OkHttpClient client = createHttpClient();
        Retrofit retrofit = createRetrofit(client, gson);
        return createApi(retrofit);
    }

    private Gson createGson(){
     return new Gson().newBuilder().create();
  }

    private OkHttpClient createHttpClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG?HttpLoggingInterceptor.Level.BASIC:HttpLoggingInterceptor.Level. NONE);
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(signInterceptor)
                .build();
    }

    private Retrofit createRetrofit(OkHttpClient client, Gson gson){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private Api createApi(Retrofit retrofit){
      return retrofit.create(Api.class);
  }
}
