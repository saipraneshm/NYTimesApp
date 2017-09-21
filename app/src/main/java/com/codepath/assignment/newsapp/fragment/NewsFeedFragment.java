package com.codepath.assignment.newsapp.fragment;


import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.assignment.newsapp.R;
import com.codepath.assignment.newsapp.activity.SettingsActivity;
import com.codepath.assignment.newsapp.adapter.NewsFeedAdapter;
import com.codepath.assignment.newsapp.databinding.FragmentNewsFeedBinding;
import com.codepath.assignment.newsapp.fragment.abs.VisibleFragment;
import com.codepath.assignment.newsapp.models.NewsStory;
import com.codepath.assignment.newsapp.models.Stories;
import com.codepath.assignment.newsapp.network.ArticleSearchController;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedFragment extends VisibleFragment implements SharedPreferences.OnSharedPreferenceChangeListener{


    private static final String TAG = NewsFeedFragment.class.getSimpleName();
    private static final String ARGS_SEARCH_QUERY = "ARGS_SEARCH_QUERY";
    private FragmentNewsFeedBinding mNewsFeedBinding;
    private NewsFeedAdapter mNewsFeedAdapter;
    private List<NewsStory> mNewsStories;

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
        mNewsStories = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mNewsFeedBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_news_feed, container, false);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mNewsFeedBinding.toolbar);

        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        mNewsFeedBinding.rvVertical.setLayoutManager(gridLayoutManager);
        mNewsFeedAdapter = new NewsFeedAdapter(getActivity(),mNewsStories);
        mNewsFeedBinding.rvVertical.setAdapter(mNewsFeedAdapter);
        loadData(0);

        //loadData();
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .registerOnSharedPreferenceChangeListener(this);

        // Inflate the layout for this fragment
        return mNewsFeedBinding.getRoot();
    }

    public void loadData(int page){
        ArticleSearchController c = new ArticleSearchController(getActivity());
        Call<Stories> call = c.getStories("Hurricane",page);
        call.enqueue(new Callback<Stories>() {
            @Override
            public void onResponse(Call<Stories> call, Response<Stories> response) {
                if(response.isSuccessful()){
                    Stories stories = response.body();
                    if(stories != null){
                        mNewsStories.clear();
                        mNewsStories.addAll(stories.getNewsStories());
                        mNewsFeedAdapter.notifyDataSetChanged();
                    }

                }else{
                    Log.e("RESPONSE", String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<Stories> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void handleSearchQuery(String query) {
        Log.d(TAG,"Got the query: " + query);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_search: getActivity().onSearchRequested();
                return true;
            case R.id.menu_settings:
                startActivity(SettingsActivity.newIntent(getActivity()));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.pref_arts_key))){
            Log.d(TAG,sharedPreferences
                    .getBoolean(key,getResources().getBoolean(R.bool.pref_default_arts_value))
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
