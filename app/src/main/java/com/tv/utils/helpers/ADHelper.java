package com.tv.utils.helpers;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.ads.AdView;

public class ADHelper {

    private static final ADHelper ourInstance = new ADHelper();
    private static AdView adView;

    public static ADHelper getInstance(Context context) {
        adView = new AdView(context);
        return ourInstance;
    }


    public AdView getPublisherView() {
        return adView;
    }

    Activity pipAct;
    public void pipActivity(Activity episodeActivity) {
        this.pipAct=episodeActivity;
    }

    public Activity getPipAct() {
        return pipAct;
    }
}
