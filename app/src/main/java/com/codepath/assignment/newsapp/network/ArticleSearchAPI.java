package com.codepath.assignment.newsapp.network;

import com.codepath.assignment.newsapp.models.Stories;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by saip92 on 9/19/2017.
 */

public interface ArticleSearchAPI {

    @GET("articlesearch.json")
    Call<Stories> getStories(@Query("q") String query);


}

