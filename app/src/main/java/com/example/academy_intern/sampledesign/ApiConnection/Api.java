package com.example.academy_intern.sampledesign.ApiConnection;

import com.example.academy_intern.sampledesign.Services.ApiInterface;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Api {
    private static Retrofit retrofit = null;
    public static String BASE_URL = "http://10.1.0.88:8080/";

    public static ApiInterface getClient()
    {
        if (retrofit == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        ApiInterface api = retrofit.create(ApiInterface.class);
        return api;
    }
}

