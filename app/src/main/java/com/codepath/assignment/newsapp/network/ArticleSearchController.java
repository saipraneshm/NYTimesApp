package com.codepath.assignment.newsapp.network;



import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.codepath.assignment.newsapp.models.Stories;
import com.codepath.assignment.newsapp.utils.QueryPreferences;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
    private static String BEGIN_DATE = "begin_date";
    private static String FQ_PARAM ="fq";
    private static String Q_PARAM = "q";
    private static String SORT_PARAM = "sort";
    private static String PAGE_PARAM = "page";



    public ArticleSearchController(Context context){
        Retrofit retrofit = RetrofitClient.getClient(ARTICLE_SEARCH_API);
        mArticleSearchAPI = retrofit.create(ArticleSearchAPI.class);
        mContext = context;

    }

    public Call<Stories> getStories(String query, int page){
        Map<String, String> queries = new HashMap<>();
        if(getNewsDeskQueryValue() != null){
            queries.put(FQ_PARAM,getNewsDeskQueryValue());
        }

        if(getBeginDatePreference() != null){
            Log.d(TAG,getBeginDatePreference() + " Date preference");
            queries.put(BEGIN_DATE,getBeginDatePreference());
        }

        queries.put(SORT_PARAM,QueryPreferences.getSortPref(mContext).toLowerCase());

        if(query != null && !TextUtils.isEmpty(query)){
            queries.put(Q_PARAM, query);
        }

        if(page >= 0){
            queries.put(PAGE_PARAM,String.valueOf(page));
        }

        Log.d(TAG,queries.toString() + " hash map values");

        return mArticleSearchAPI.getStories(queries);
    }

    private String getBeginDatePreference(){
        DateFormat fromFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
        try {
            Date value = fromFormat.parse(QueryPreferences.getBeginDatePref(mContext));
            DateFormat toFormat = new SimpleDateFormat("yyyyMMdd");
            return toFormat.format(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
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
        if(TextUtils.isEmpty(String.valueOf(sbr))){
            return null;
        }


        return NEWS_DESK_VALUE + "(" + String.valueOf(sbr).trim() + ")";

    }


}
