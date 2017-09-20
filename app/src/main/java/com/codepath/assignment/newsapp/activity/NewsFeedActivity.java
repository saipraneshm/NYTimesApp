package com.codepath.assignment.newsapp.activity;

import android.support.v4.app.Fragment;

import com.codepath.assignment.newsapp.activity.abs.SingleFragmentActivity;
import com.codepath.assignment.newsapp.fragment.NewsFeedFragment;

public class NewsFeedActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new NewsFeedFragment();
    }
}
