package com.codepath.assignment.newsapp.network;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by saip92 on 9/19/2017.
 */

public class RetrofitClient {

    private volatile static Retrofit sRetrofit = null;
    private static final String API_KEY = "2aee84f7ad064b8b8d30590b1b72d895";
    private static final String API_KEY_QUERY = "api-key";

    public static Retrofit getClient(String baseUrl){
        if(sRetrofit == null){
            synchronized (RetrofitClient.class){
                  if(sRetrofit == null){
                      OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                      httpClient.addInterceptor(chain -> {
                          Request original =  chain.request();
                          HttpUrl originalUrl = original.url();
                          HttpUrl url = originalUrl.newBuilder()
                                  .addQueryParameter(API_KEY_QUERY, API_KEY)
                                  .build();

                          // Request customization: add request headers
                          Request.Builder requestBuilder = original.newBuilder()
                                  .url(url);

                          Request request = requestBuilder.build();
                          return chain.proceed(request);
                      });
                      sRetrofit = new Retrofit.Builder()
                              .baseUrl(baseUrl)
                              .callFactory(httpClient.build())
                              .addConverterFactory(GsonConverterFactory.create())
                              .build();
                  }
            }

        }
        return sRetrofit;
    }
}
