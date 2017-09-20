package com.codepath.assignment.newsapp.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.assignment.newsapp.R;
import com.codepath.assignment.newsapp.models.Stories;
import com.codepath.assignment.newsapp.network.ArticleSearchController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedFragment extends VisibleFragment {


    private static final String TAG = NewsFeedFragment.class.getSimpleName();

    public NewsFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news_feed, container, false);
        ArticleSearchController c = new ArticleSearchController();
        Call<Stories> call = c.getStories("Hurricane");
        call.enqueue(new Callback<Stories>() {
            @Override
            public void onResponse(Call<Stories> call, Response<Stories> response) {
                if(response.isSuccessful()){
                    Stories stories = response.body();
                   // Log.d("RESPONSE",response.body());
                }else{
                    Log.e("RESPONSE", String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<Stories> call, Throwable t) {
                t.printStackTrace();
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void handleSearchQuery(String query) {
        Log.d(TAG,"Got the query: " + query);
    }
}
