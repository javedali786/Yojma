package com.tv.uscreen.utils.helpers;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tv.uscreen.utils.Logger;

import java.util.Objects;

public class CheckInternetConnection {

    /**
     * Called for checking Internet connection
     */
    public static boolean isOnline(Activity activity) {

        ConnectivityManager cm = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo;

        try {
            netInfo = Objects.requireNonNull(cm).getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Logger.w(e);
        }

        return false;
    }

}
