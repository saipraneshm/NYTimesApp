package com.codepath.assignment.newsapp.utils;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.preference.Preference;

import com.codepath.assignment.newsapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by saip92 on 9/20/2017.
 */

public class QueryPreferences {

    public static boolean getArtsPref(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.pref_arts_key),
                        context.getResources().getBoolean(R.bool.pref_default_arts_value));
    }

    public static void setArtsPref(Context context, Boolean artsPref){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putBoolean(context.getString(R.string.pref_arts_key),
                artsPref).apply();
    }

    public static boolean getSportsPref(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.pref_sports_key),
                        context.getResources().getBoolean(R.bool.pref_default_sports_value));
    }

    public static void setSportsPref(Context context, Boolean sportsRef){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putBoolean(context.getString(R.string.pref_sports_key),
                sportsRef).apply();
    }

    public static boolean getFashionPref(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.pref_fashion_key),
                        context.getResources().getBoolean(R.bool.pref_default_fashion_value));
    }

    public static void setFashionPref(Context context, Boolean fashionPref){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putBoolean(context.getString(R.string.pref_fashion_key),
                fashionPref).apply();
    }

    public static String getSortPref(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_sort_key),
                        context.getResources().getString(R.string.pref_sort_default_value));
    }

    public static void setSortPref(Context context, String sortPref){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putString(context.getString(R.string.pref_sort_key),
                sortPref).apply();
    }

    public static String getBeginDatePref(Context context){
        Date date = new Date();
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_select_begin_date_key),
                        date.toString());
    }

    public static void setBeginDatePref(Context context,Date date){
        DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
        String strDate = dateFormat.format(date);
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(context.getString(R.string.pref_select_begin_date_key),strDate).apply();
    }

    private static void setSettingsDialogPref(Context context, Boolean isModern){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(context.getString(R.string.pref_settings_dialog_key),isModern)
                .apply();
    }

    private boolean getSettingsDialoPref(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.pref_settings_dialog_key),
                        context.getResources().getBoolean(R.bool.pref_default_settings_dialog_value));
    }
}
