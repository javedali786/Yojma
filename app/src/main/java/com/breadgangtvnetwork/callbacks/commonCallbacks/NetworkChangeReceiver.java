package com.breadgangtvnetwork.callbacks.commonCallbacks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import com.breadgangtvnetwork.utils.Logger;
import com.breadgangtvnetwork.utils.Network;

public class NetworkChangeReceiver extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;

    public static boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            Logger.w(e);
            return false;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {

            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                if (isOnline(context)) {
                    if (connectivityReceiverListener != null) {
                        connectivityReceiverListener.onNetworkConnectionChanged(true);
                    }
                } else {
                    if (Network.isMobileDataNetworkAvailable(context) || Network.checkConnectivityTypeMobile(context)) {
                        if (connectivityReceiverListener != null) {
                            connectivityReceiverListener.onNetworkConnectionChanged(true);
                        }
                    } else {
                        if (connectivityReceiverListener != null) {
                            connectivityReceiverListener.onNetworkConnectionChanged(false);
                        }
                    }
                }
            }, 1000);

        } catch (NullPointerException e) {
            Logger.w(e);
        }
    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}