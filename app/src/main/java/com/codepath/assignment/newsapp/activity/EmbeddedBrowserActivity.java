package com.codepath.assignment.newsapp.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codepath.assignment.newsapp.R;
import com.codepath.assignment.newsapp.activity.abs.SingleFragmentActivity;
import com.codepath.assignment.newsapp.fragment.EmbeddedBrowserFragment;
import com.codepath.assignment.newsapp.models.NewsStory;

public class EmbeddedBrowserActivity extends SingleFragmentActivity {


    private static final String EXTRA_NEWS_STORY = "EXTRA_NEWS_STORY";
    private EmbeddedBrowserFragment mBrowserFragment;

    public static Intent newIntent(Context context, NewsStory newsStory){
        Intent intent = new Intent(context,EmbeddedBrowserActivity.class);
        intent.putExtra(EXTRA_NEWS_STORY,newsStory);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        NewsStory newsStory = getIntent().getParcelableExtra(EXTRA_NEWS_STORY);
        mBrowserFragment = EmbeddedBrowserFragment.newInstance(newsStory);
        return mBrowserFragment;
    }

    @Override
    public void onBackPressed() {
        if(mBrowserFragment.onBackPressed()){
            return;
        }
        super.onBackPressed();
    }

}
