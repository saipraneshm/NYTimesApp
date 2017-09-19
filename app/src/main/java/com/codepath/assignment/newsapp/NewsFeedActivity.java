package com.codepath.assignment.newsapp;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NewsFeedActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new NewsFeedFragment();
    }
}
