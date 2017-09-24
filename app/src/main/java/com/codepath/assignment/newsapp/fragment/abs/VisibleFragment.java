package com.codepath.assignment.newsapp.fragment.abs;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v4.app.Fragment;

/**
 * Created by saip92 on 9/19/2017.
 */

public abstract class VisibleFragment extends Fragment {

    public abstract void handleSearchQuery(String query);

    private BroadcastReceiver connectivityReceiver = null;

    @Override
    public void onResume() {
        super.onResume();
        connectivityReceiver = createConnectivityBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(connectivityReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(connectivityReceiver);
    }

    protected abstract BroadcastReceiver createConnectivityBroadcastReceiver();

}
