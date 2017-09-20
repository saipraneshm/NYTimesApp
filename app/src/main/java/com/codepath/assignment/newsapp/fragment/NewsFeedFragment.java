package com.codepath.assignment.newsapp.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
public class NewsFeedFragment extends VisibleFragment implements SharedPreferences.OnSharedPreferenceChangeListener{


    private static final String TAG = NewsFeedFragment.class.getSimpleName();
    private static final String ARGS_SEARCH_QUERY = "ARGS_SEARCH_QUERY";

    public NewsFeedFragment() {
        // Required empty public constructor
    }

    public static NewsFeedFragment newInstance(String query){
        Bundle args = new Bundle();
        args.putString(ARGS_SEARCH_QUERY, query);

        NewsFeedFragment fragment = new NewsFeedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news_feed, container, false);

        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .registerOnSharedPreferenceChangeListener(this);
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


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.pref_article_key))){
            Log.d(TAG,sharedPreferences
                    .getBoolean(key,getResources().getBoolean(R.bool.pref_default_article_value))
                    +" ");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}