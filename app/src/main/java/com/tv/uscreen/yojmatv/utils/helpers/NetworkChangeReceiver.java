package com.tv.uscreen.yojmatv.utils.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tv.uscreen.yojmatv.utils.Logger;

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Logger.w("broadcstcalledd", "true");
            if (isOnline(context)) {
                //dialog(true);
                Logger.e("keshav", "Online Connect Intenet ");
            } else {
                // dialog(false);
                Logger.e("keshav", "Conectivity Failure !!! ");
            }
        } catch (NullPointerException e) {
            Logger.w(e);
        }
    }

    private boolean isOnline(Context context) {
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
}