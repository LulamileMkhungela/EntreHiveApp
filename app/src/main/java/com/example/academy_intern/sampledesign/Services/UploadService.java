package com.example.academy_intern.sampledesign.Services;

import com.example.academy_intern.sampledesign.ApiConnection.Api;
import com.example.academy_intern.sampledesign.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadService {
    private ApiInterface uploadInterface;

    public UploadService() {
        OkHttpClient.Builder okhttpBuilder = new OkHttpClient().newBuilder();
        okhttpBuilder.connectTimeout(60, TimeUnit.SECONDS);
        okhttpBuilder.writeTimeout(60, TimeUnit.SECONDS);
        okhttpBuilder.readTimeout(60, TimeUnit.SECONDS);
        okhttpBuilder.retryOnConnectionFailure(true);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okhttpBuilder.addInterceptor(interceptor);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(okhttpBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        uploadInterface = retrofit.create(ApiInterface.class);
    }

    public void uploadFileMultipart(int userId, RequestBody action, MultipartBody.Part file, Callback callback)
    {
        uploadInterface.uploadFile(userId ,file).enqueue(callback);
    }

    public void uploadFileEventMultipart(int userId, RequestBody action, MultipartBody.Part file, Callback callback)
    {
        uploadInterface.uploadEventFile(userId ,file).enqueue(callback);
    }

    public void uploadPhotoMultipart(int userId, RequestBody action, MultipartBody.Part photo, Callback callback)
    {
        uploadInterface.uploadProfilePic(userId, photo).enqueue(callback);
    }


}
