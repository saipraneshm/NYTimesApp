package com.codepath.assignment.newsapp.utils;

import android.content.Context;
import android.preference.PreferenceManager;

import com.codepath.assignment.newsapp.R;

import java.util.Date;

/**
 * Created by saip92 on 9/20/2017.
 */

public class QueryPreferences {

    public static boolean getArtsPref(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.pref_arts_key),
                        context.getResources().getBoolean(R.bool.pref_default_arts_value));
    }

    public static boolean getSportsPref(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.pref_sports_key),
                        context.getResources().getBoolean(R.bool.pref_default_sports_value));
    }

    public static boolean getFashionPref(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.pref_fashion_key),
                        context.getResources().getBoolean(R.bool.pref_default_fashion_value));
    }

    public static String getSortPref(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_sort_key),
                        context.getResources().getString(R.string.pref_sort_default_value));
    }

    public static String getBeginDatePref(Context context){
        Date date = new Date();
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_select_begin_date_key),
                        date.toString());
    }
}
