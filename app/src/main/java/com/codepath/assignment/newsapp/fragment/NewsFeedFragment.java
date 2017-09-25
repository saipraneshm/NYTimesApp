package com.codepath.assignment.newsapp.fragment;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.assignment.newsapp.R;
import com.codepath.assignment.newsapp.activity.EmbeddedBrowserActivity;
import com.codepath.assignment.newsapp.adapter.NewsFeedAdapter;
import com.codepath.assignment.newsapp.databinding.FragmentNewsFeedBinding;
import com.codepath.assignment.newsapp.fragment.abs.VisibleFragment;
import com.codepath.assignment.newsapp.models.NewsStory;
import com.codepath.assignment.newsapp.models.Stories;
import com.codepath.assignment.newsapp.network.ArticleSearchController;
import com.codepath.assignment.newsapp.receiver.ConnectivityBroadcastReceiver;
import com.codepath.assignment.newsapp.utils.AppUtils;
import com.codepath.assignment.newsapp.utils.EndlessRecyclerViewScrollListener;
import com.codepath.assignment.newsapp.utils.ItemClickSupport;
import com.codepath.assignment.newsapp.utils.QueryPreferences;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedFragment extends VisibleFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener{


    private static final String TAG = NewsFeedFragment.class.getSimpleName();
    private static final String ARGS_SEARCH_QUERY = "ARGS_SEARCH_QUERY";
    private FragmentNewsFeedBinding mNewsFeedBinding;
    private NewsFeedAdapter mNewsFeedAdapter;
    private List<NewsStory> mNewsStories;
    private String mQuery;
    private EndlessRecyclerViewScrollListener mScrollListener;
    private Boolean hasPreferencesChanged = false;
    private Boolean hasInternet = true;
    private static int sRetryCount = 5;
    private BroadcastReceiver mPreferenceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            hasPreferencesChanged = intent
                    .getBooleanExtra(getString(R.string.has_preference_changed_key),false);
            if(hasInternet && hasPreferencesChanged){
                makeNewSearch();
            }

        }
    };

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
        if(getArguments() != null){
            mQuery = getArguments().getString(ARGS_SEARCH_QUERY,null);
            if(mQuery != null) makeNewSearch();
        }
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(mPreferenceReceiver,
                        new IntentFilter(getString(R.string.broadcast_event_preference_changed)));
        super.onResume();
        //Log.d(TAG,"onResume has been called");
        if(hasPreferencesChanged){
            //Log.d(TAG,"calling make search <- preferences had changed");
            makeNewSearch();
        }


    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mPreferenceReceiver);
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mNewsFeedBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_news_feed, container, false);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mNewsFeedBinding.toolbar);
        mNewsFeedBinding.fab.setOnClickListener(view -> {
            String newest = getString(R.string.pref_sort_newer_value);
            String oldest = getString(R.string.pref_sort_older_value);

            if(QueryPreferences.getSortPref(getActivity()).equals(newest)){
                QueryPreferences.setSortPref(getActivity(),oldest);
            }else{
                QueryPreferences.setSortPref(getActivity(),newest);
            }
            makeNewSearch();
        });
        setUpRecyclerView();
        setupSwipeRefresh();
        loadData(0);

        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .registerOnSharedPreferenceChangeListener(this);

        return mNewsFeedBinding.getRoot();
    }

    private void setupSwipeRefresh(){
        mNewsFeedBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                makeNewSearch();
            }
        });

        mNewsFeedBinding.swipeRefreshLayout
                .setColorSchemeColors
                        (ContextCompat.getColor(getActivity(),android.R.color.holo_blue_bright),
                        ContextCompat.getColor(getActivity(),android.R.color.holo_green_light),
                        ContextCompat.getColor(getActivity(),android.R.color.holo_orange_light),
                        ContextCompat.getColor(getActivity(),android.R.color.holo_red_light));
    }

    private void setUpRecyclerView(){
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        mNewsFeedBinding.rvVertical.setLayoutManager(gridLayoutManager);
        mNewsFeedAdapter = new NewsFeedAdapter(getActivity(),mNewsStories);
        mNewsFeedBinding.rvVertical.setAdapter(mNewsFeedAdapter);
        mScrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
               // Log.d(TAG,"calling load more " + page);
                    loadData(page);
            }
        };
        mNewsFeedBinding.rvVertical.addOnScrollListener(mScrollListener);

        ItemClickSupport.addTo(mNewsFeedBinding.rvVertical)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    Log.d(TAG, mNewsFeedAdapter.getNewsStory(position).toString());
                    if(hasInternet){
                        //launchChromeTab(mNewsFeedAdapter.getNewsStory(position).getWebUrl());
                        if(QueryPreferences.getEmbeddedBrowserPref(getActivity())){
                            startActivity(EmbeddedBrowserActivity.newIntent(getActivity(),
                                    mNewsFeedAdapter.getNewsStory(position)));
                        }else{
                            launchChromeTab(mNewsFeedAdapter.getNewsStory(position).getWebUrl());
                        }

                    }

                });

    }

    private void launchChromeTab(String url){
        if(url != null){
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            builder.setToolbarColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
            builder.addDefaultShareMenuItem();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_share_white_24dp);
            builder.setActionButton(bitmap, "Share Link", getPendingIntent(url), true);
            builder.setStartAnimations(getActivity(),R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            builder.setExitAnimations(getActivity(),R.anim.slide_in_from_left, R.anim.slide_out_to_right);
            customTabsIntent.launchUrl(getActivity(), Uri.parse(url));
        }
    }

    //Prepares the pending intent for sharing the News Article
    private PendingIntent getPendingIntent(String url){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,url);
        int requestCode = 100;
        return PendingIntent.getActivity(getActivity(),
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void loadData(int page){
        if(hasInternet){
            mNewsFeedBinding.swipeRefreshLayout.setRefreshing(true);
            ArticleSearchController c = new ArticleSearchController(getActivity());
            Call<Stories> call = c.getStories(mQuery,page);
            call.enqueue(new Callback<Stories>() {
                @Override
                public void onResponse(Call<Stories> call, Response<Stories> response) {
                    mNewsFeedBinding.swipeRefreshLayout.setRefreshing(false);
                    if(response.code() != 200){
                        if(sRetryCount > 0){
                            --sRetryCount;
                            new Handler().postDelayed(() -> loadData((page)), 200);
                        }else{
                            showNoMoreDataToast();
                        }
                    }
                    if(response.isSuccessful()){
                        sRetryCount = 5;
                        Stories stories = response.body();
                        if(stories != null){
                            mNewsFeedAdapter.addMoreData(stories.getNewsStories());
                        }else{
                            showNoMoreDataToast();
                        }
                    }else{
                        Log.e("RESPONSE", String.valueOf(response.errorBody()));
                        showNoMoreDataToast();
                    }
                }

                @Override
                public void onFailure(Call<Stories> call, Throwable t) {
                    mNewsFeedBinding.swipeRefreshLayout.setRefreshing(false);
                    showNoMoreDataToast();
                    t.printStackTrace();
                }
            });
        }else{
            mNewsFeedBinding.swipeRefreshLayout.setRefreshing(false);
            showNoInternetConnectionSnackBar();
        }

    }

    private void showNoMoreDataToast(){
        Toast.makeText(getActivity(),R.string.no_data_to_display, Toast.LENGTH_SHORT).show();
    }

    //Resets state whenever a new search is made
    private void makeNewSearch(){
        if(hasInternet && mNewsFeedAdapter != null && mScrollListener != null){
            mNewsStories.clear();
            mNewsFeedAdapter.notifyDataSetChanged();
            mScrollListener.resetState();
        }
        loadData(0);
    }

    @Override
    public void handleSearchQuery(String query) {
        Log.d(TAG,"Got the query: " + query);
        mQuery = query;
        makeNewSearch();
    }

    @Override
    protected BroadcastReceiver createConnectivityBroadcastReceiver() {
        ConnectivityBroadcastReceiver receiver = new ConnectivityBroadcastReceiver();
        receiver.setListener(isConnected -> {
            if(hasInternet == isConnected){
                return;
            }

            if(isConnected){
                hasInternet = true;
                showInternetConnectedSnackBar();
                makeNewSearch();
                //Log.d(TAG,"Connected to the internet");
            }else{
                hasInternet = false;
                AppUtils.showNoInternetDialog(getActivity());
                //Log.d(TAG,"Is not connected to the internet");
            }
        });
        return receiver;
    }


    private void showInternetConnectedSnackBar(){
        final Snackbar snackbar = Snackbar.make(mNewsFeedBinding.getRoot(),
                R.string.connected_to_internet,
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor
                (ContextCompat.getColor(getActivity(),R.color.colorPrimary))
                .setAction(android.R.string.cancel,
                        view -> snackbar.dismiss()).show();
    }

    private void showNoInternetConnectionSnackBar(){
        final Snackbar snackbar = Snackbar.make(mNewsFeedBinding.getRoot(),
                R.string.not_connected_to_internet,
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor
                (ContextCompat.getColor(getActivity(),R.color.colorGreen))
                .setAction(android.R.string.cancel,
                        view -> snackbar.dismiss()).show();
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
       // Log.d(TAG,"sharedpreferences has changed");
        hasPreferencesChanged = true;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
