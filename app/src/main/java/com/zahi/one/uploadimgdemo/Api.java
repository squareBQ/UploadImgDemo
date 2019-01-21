package com.zahi.one.uploadimgdemo;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zahi on 2019/1/19.
 */
public class Api {

    public static Service SERVICE;
    public static final String BASE_URL = "";

    public static Service getDefault() {
        if (SERVICE == null) {
            synchronized (Api.class) {
                if (SERVICE == null) {
                    OkHttpClient client = new OkHttpClient.Builder().build();
                    SERVICE = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(client)
                            .build()
                            .create(Service.class);
                }
            }
        }
        return SERVICE;
    }
}
