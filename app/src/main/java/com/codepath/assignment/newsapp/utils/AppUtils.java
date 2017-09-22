package com.codepath.assignment.newsapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.codepath.assignment.newsapp.R;


/**
 * A utility class that helps to check internet availability and to hide keyboard
 * */
public class AppUtils {
    public static boolean isNetworkAvailableAndConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void hideKeyboard(Context context, View view) {
        if (context == null) return;
        InputMethodManager inputMethodManager= (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showNoInternetDialog(Context context){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(context.getString(R.string.no_internet_dialog_title));
        alertDialogBuilder.setMessage(context.getString(R.string.no_internet_dialog_message));
        alertDialogBuilder.setPositiveButton(context.getString(android.R.string.ok),null);
        alertDialogBuilder.setNegativeButton(context.getString(android.R.string.cancel),null);
        alertDialogBuilder.create().show();
    }

}
