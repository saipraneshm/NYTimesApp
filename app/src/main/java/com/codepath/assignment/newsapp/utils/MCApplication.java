package com.codepath.assignment.newsapp.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by saip92 on 9/21/2017.
 */

public class MCApplication extends Application {

    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
    }


    public static Context getAppContext(){
        return mAppContext;
    }
}
