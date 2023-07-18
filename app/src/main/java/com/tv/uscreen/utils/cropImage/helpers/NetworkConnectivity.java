package com.tv.uscreen.utils.cropImage.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tv.uscreen.utils.Logger;

public class NetworkConnectivity {

    /**
     * Called for checking Internet connection
     */
    public static boolean isOnline(Context activity) {

        ConnectivityManager cm = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo;

        try {
            netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
        } catch (Exception e) {
            Logger.w(e);
        }
        return false;
    }

}
