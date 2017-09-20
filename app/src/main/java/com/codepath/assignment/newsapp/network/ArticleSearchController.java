package com.codepath.assignment.newsapp.network;



import com.codepath.assignment.newsapp.models.Stories;

import retrofit2.Call;
import retrofit2.Retrofit;


/**
 * Created by saip92 on 9/19/2017.
 */

public class ArticleSearchController {


    private static final String ARTICLE_SEARCH_API = "https://api.nytimes.com/svc/search/v2/";
    private ArticleSearchAPI mArticleSearchAPI;

    public ArticleSearchController(){
        Retrofit retrofit = RetrofitClient.getClient(ARTICLE_SEARCH_API);
        mArticleSearchAPI = retrofit.create(ArticleSearchAPI.class);
    }

    public Call<Stories> getStories(String query){
         return mArticleSearchAPI.getStories(query);
    }


}
