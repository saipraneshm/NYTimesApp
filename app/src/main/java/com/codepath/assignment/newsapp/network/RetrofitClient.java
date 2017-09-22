package com.codepath.assignment.newsapp.network;

import android.app.Application;

import com.codepath.assignment.newsapp.utils.MCApplication;

import okhttp3.Cache;
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
    //Limiting the no.of fields in the output
    private static final String FL_PARAMETERS = "headline,web_url,multimedia,lead_paragraph,_id,pub_date,byline,snippet,news_desk";


    public static Retrofit getClient(String baseUrl){
        if(sRetrofit == null){
            synchronized (RetrofitClient.class){
                  if(sRetrofit == null){
                      OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                      int cacheSize = 10 * 1024 * 1024; // 10 MB
                      Cache cache = new Cache(MCApplication.getAppContext().getCacheDir(), cacheSize);
                      httpClient.addInterceptor(chain -> {

                          Request original =  chain.request();
                          HttpUrl originalUrl = original.url();
                          HttpUrl url = originalUrl.newBuilder()
                                  .addQueryParameter(API_KEY_QUERY, API_KEY)
                                  .addQueryParameter("fl",FL_PARAMETERS)
                                  .build();

                          // Request customization: add request headers
                          Request.Builder requestBuilder = original.newBuilder()
                                  .url(url);

                          Request request = requestBuilder.build();
                          return chain.proceed(request);
                      });

                      httpClient.cache(cache);
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
