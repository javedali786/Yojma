package com.breadgangtvnetwork.utils.helpers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.breadgangtvnetwork.callbacks.commonCallbacks.NoInternetConnectionCallBack;
import com.breadgangtvnetwork.databinding.NoConnectionBinding;
import com.breadgangtvnetwork.utils.Logger;
import com.breadgangtvnetwork.utils.cropImage.helpers.NetworkConnectivity;


public class NoInternetConnection {
    final Activity activity;
    NoInternetConnectionCallBack noInternetConnectionCallBack;
    private final BroadcastReceiver ReceivefromService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (NetworkConnectivity.isOnline(activity)) {
                noInternetConnectionCallBack.isOnline(true);
            } else {
                noInternetConnectionCallBack.isOffline(true);
            }

        }
    };


    public NoInternetConnection(Activity context) {
        this.activity = context;
    }

    public void hanleAction(NoConnectionBinding connection, NoInternetConnectionCallBack callBack) {
        this.noInternetConnectionCallBack = callBack;

        if (NetworkConnectivity.isOnline(activity)) {
            noInternetConnectionCallBack.isOnline(true);
            try {
                if (ReceivefromService != null) {
                    activity.unregisterReceiver(ReceivefromService);
                }
            } catch (IllegalArgumentException e) {
                Logger.e("NoInternetConnection", "" + e);
            }

        } else {
            noInternetConnectionCallBack.isOffline(true);
           /* IntentFilter filter= new IntentFilter();
            filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            activity.registerReceiver(ReceivefromService, filter);*/
        }
    }
}
