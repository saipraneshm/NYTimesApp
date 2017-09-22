package com.codepath.assignment.newsapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.codepath.assignment.newsapp.utils.AppUtils;


/**
 * This class is used to listen to changes in network connectivity and handles screen changes
 * accordingly
 */

public class ConnectivityBroadcastReceiver extends BroadcastReceiver {

    private OnNetworkChangeListener mListener;

    public interface OnNetworkChangeListener{
        void onNetworkChange(boolean isConnected);
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        boolean isConnected = AppUtils.isNetworkAvailableAndConnected(context);
        mListener.onNetworkChange(isConnected);
    }

    public void setListener(OnNetworkChangeListener listener) {
        mListener = listener;
    }
}
