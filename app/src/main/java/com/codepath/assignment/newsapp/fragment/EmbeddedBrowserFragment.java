package com.codepath.assignment.newsapp.fragment;


import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.codepath.assignment.newsapp.R;
import com.codepath.assignment.newsapp.activity.EmbeddedBrowserActivity;
import com.codepath.assignment.newsapp.databinding.FragmentEmbeddedBrowserBinding;
import com.codepath.assignment.newsapp.fragment.abs.VisibleFragment;
import com.codepath.assignment.newsapp.models.NewsStory;
import com.codepath.assignment.newsapp.receiver.ConnectivityBroadcastReceiver;
import com.codepath.assignment.newsapp.utils.AppUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmbeddedBrowserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmbeddedBrowserFragment extends VisibleFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NEWS_STORY = "ARG_NEWS_STORY";

    // TODO: Rename and change types of parameters
    private NewsStory mNewsStory;

    private FragmentEmbeddedBrowserBinding mBinding;

    public EmbeddedBrowserFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static EmbeddedBrowserFragment newInstance(NewsStory newsStory) {
        EmbeddedBrowserFragment fragment = new EmbeddedBrowserFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NEWS_STORY, newsStory);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mNewsStory = getArguments().getParcelable(ARG_NEWS_STORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_embedded_browser,container,false);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mBinding.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBinding.webView.getSettings().setLoadsImagesAutomatically(true);
        mBinding.webView.getSettings().setJavaScriptEnabled(true);
        mBinding.webView.setScrollBarStyle(ViewStub.SCROLLBARS_INSIDE_OVERLAY);

        mBinding.webView.setWebViewClient(new WebViewClient(){

            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        });

        mBinding.webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress == 100){
                    mBinding.progressBar.setVisibility(View.GONE);
                }else{
                    mBinding.progressBar.setVisibility(View.VISIBLE);
                    mBinding.progressBar.setProgress(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                mBinding.toolbar.setTitle(title);

            }
        });

        mBinding.webView.loadUrl(mNewsStory.getWebUrl());

        // Inflate the layout for this fragment
        return mBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_share,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_share){
            startActivity(createShareNewsIntent());
            return true;
        }else if(id == android.R.id.home){
            getActivity().finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void handleSearchQuery(String query) {

    }

    private Intent createShareNewsIntent(){
        Intent shareIntent = ShareCompat.IntentBuilder.from(getActivity())
                .setType("text/plain")
                .setText("Read the following news article on NYTimes: " + Uri.parse(mNewsStory.getWebUrl()))
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return shareIntent;
    }

    @Override
    protected BroadcastReceiver createConnectivityBroadcastReceiver() {
        ConnectivityBroadcastReceiver receiver = new ConnectivityBroadcastReceiver();
        receiver.setListener(isConnected -> {
            mBinding.webView.setNetworkAvailable(isConnected);
            if(!isConnected){
                AppUtils.showNoInternetDialog(getActivity());
            }
        });
        return receiver;
    }

    public boolean onBackPressed(){
        if(mBinding.webView.canGoBack())
            mBinding.webView.goBack();
        return mBinding.webView.canGoBack();
    }
}
