package com.codepath.assignment.newsapp.network;



import android.content.Context;
import android.util.Log;

import com.codepath.assignment.newsapp.models.Stories;
import com.codepath.assignment.newsapp.utils.QueryPreferences;

import retrofit2.Call;
import retrofit2.Retrofit;


/**
 * Created by saip92 on 9/19/2017.
 */

public class ArticleSearchController {


    private static final String ARTICLE_SEARCH_API = "https://api.nytimes.com/svc/search/v2/";
    private static final String TAG = ArticleSearchController.class.getSimpleName();
    private ArticleSearchAPI mArticleSearchAPI;
    private Context mContext;
    private static String ARTS_VALUE = " \"Arts\" ";
    private static String FASHION_AND_STYLE_VALUE = " \"Fashion & Style\" ";
    private static String SPORTS_VALUE = " \"Sports\" ";
    private static String NEWS_DESK_VALUE = "news_desk:";

    public ArticleSearchController(Context context){
        Retrofit retrofit = RetrofitClient.getClient(ARTICLE_SEARCH_API);
        mArticleSearchAPI = retrofit.create(ArticleSearchAPI.class);
        mContext = context;

    }

    public Call<Stories> getStories(String query, int page){
        Log.d(TAG,getNewsDeskQueryValue());
        return mArticleSearchAPI.getStories(query);
    }

    private String getNewsDeskQueryValue(){
        boolean isArtsSubscribed = QueryPreferences.getArtsPref(mContext);
        boolean isSportsSubscribed = QueryPreferences.getSportsPref(mContext);
        boolean isFashionSubscribed = QueryPreferences.getFashionPref(mContext);
        StringBuilder sbr = new StringBuilder();
        if(isArtsSubscribed){
            sbr.append(ARTS_VALUE);
        }

        if(isSportsSubscribed){
            sbr.append(SPORTS_VALUE);
        }

        if(isFashionSubscribed){
            sbr.append(FASHION_AND_STYLE_VALUE);
        }

        return NEWS_DESK_VALUE + "(" + String.valueOf(sbr).trim() + ")";

    }


}
