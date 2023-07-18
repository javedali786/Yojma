package com.tv.utils.TrackerUtil;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.JsonObject;
import com.tv.tarcker.EventConstant;
import com.tv.utils.Logger;

import java.util.List;

public class TrackerUtil {
    //public static GoogleAnalytics sAnalytics;

    private static TrackerUtil trackerUtil = null;
    Context context;

    private TrackerUtil(Context context) {
        if (context != null) {
            this.context=context;

          //  Branch.enableLogging();

            // Branch object initialization
           // Branch.getAutoInstance(context);
           // sAnalytics = GoogleAnalytics.getInstance(context);
           // Branch.getInstance().initSession((referringParams, error) -> Logger.d("Branch", "onInitFinished() with deep link data: " + referringParams));
           // MobileAds.initialize(context, "ca-app-pub-3940256099942544~3347511713");


            MobileAds.initialize(context, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
            Logger.d("sdkInitialzed", "successfull");


        }
    }

    public static TrackerUtil getInstance(Context context) {
        if (trackerUtil == null) {
            trackerUtil = new TrackerUtil(context);
        }
        return trackerUtil;
    }

//    public static GoogleAnalytics getAnalyticsInstance() {
//        return sAnalytics;
//    }

    public void track(TrackerEvent login, JsonObject name, List<PlatformType> trackingPlatforms) {
        switch (login) {
            case REGISTER:
                trackRegisterEvent(name, trackingPlatforms);
                break;
            case CLICK_CONTENT:
                trackClickContentEvent(name, trackingPlatforms);
                break;
            case PLAY_CONTENT:
                trackPlayContentEvent(name, trackingPlatforms);
                break;
            case SEARCH:
                trackSearchEvent(name, trackingPlatforms);
                break;
            case LOGIN:
                trackLoginEvent(name, trackingPlatforms);
                break;

            case DETAIL_VISIT:
                break;

            default:

        }
    }

    private void trackPlayContentEvent(JsonObject params, List<PlatformType> trackingPlatforms) {
        for (PlatformType tracking : trackingPlatforms) {
            switch (tracking) {
                case GTM:
                    Logger.d("EventTracked", "GTM");
                    break;
                case GCM:
                    break;
                case MOENGAGE:
                    break;
                case FCM:
                    try {
                        String name = params.get(EventConstant.Name).toString();
                        String contentType = params.get(EventConstant.ContentType).toString();
                        Logger.d( "ValueForFcm-->>" + name+"  "+contentType);
                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                        Bundle bundle = new Bundle();
                        bundle.putString(EventConstant.Name, name);
                        bundle.putString(EventConstant.ContentType, contentType);
                        mFirebaseAnalytics.logEvent(TrackerEvent.PLAY_CONTENT.name(), bundle);

                    } catch (Exception ignored) {

                    }
                    break;
                default:
            }
        }
    }

    private void trackSearchEvent(JsonObject params, List<PlatformType> trackingPlatforms) {
        for (PlatformType tracking : trackingPlatforms) {
            switch (tracking) {
                case GTM:
                    Logger.d("EventTracked", "GTM");
                    break;
                case GCM:
                    break;
                case MOENGAGE:
                    break;
                case FCM:
                    try {
                        String name = params.get(EventConstant.SearchTitle).toString();
                        Logger.d( "ValueForFcm-->>" + name+"  ");
                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                        Bundle bundle = new Bundle();
                        bundle.putString(EventConstant.SearchTitle, name);
                        mFirebaseAnalytics.logEvent(TrackerEvent.SEARCH.name(), bundle);

                    } catch (Exception ignored) {

                    }
                    break;
                default:
            }
        }

    }

    private void trackClickContentEvent(JsonObject params, List<PlatformType> trackingPlatforms) {
        for (PlatformType tracking : trackingPlatforms) {
            switch (tracking) {
                case GTM:
                    Logger.d("EventTracked", "GTM");
                    break;
                case GCM:
                    break;
                case MOENGAGE:
                    break;
                case FCM:
                    try {
                        String name = params.get(EventConstant.Name).toString();
                        String contentType = params.get(EventConstant.ContentType).toString();
                        Logger.d( "ValueForFcm-->>" + name+"  "+contentType);
                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                        Bundle bundle = new Bundle();
                        bundle.putString(EventConstant.Name, name);
                        bundle.putString(EventConstant.ContentType, contentType);
                        mFirebaseAnalytics.logEvent(TrackerEvent.CLICK_CONTENT.name(), bundle);

                    } catch (Exception ignored) {

                    }
                    break;
                default:
            }
        }
    }

    private void trackRegisterEvent(JsonObject params, List<PlatformType> trackingPlatforms) {
        for (PlatformType tracking : trackingPlatforms) {
            switch (tracking) {
                case GTM:
                    Logger.d("EventTracked", "GTM");
                    break;
                case GCM:
                    break;
                case MOENGAGE:
                    break;
                case FCM:
                    try{
                        String name=params.get(EventConstant.Name).toString();
                        String platformType=params.get(EventConstant.PlatformType).toString();
                        Logger.d("ValueForFcm-->>"+name);
                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                        Bundle bundle = new Bundle();
                        bundle.putString(EventConstant.Name, name);
                        bundle.putString(EventConstant.PlatformType, platformType);
                        mFirebaseAnalytics.logEvent(TrackerEvent.REGISTER.name(), bundle);

                    }catch (Exception ignored){

                    }
                    break;
                default:
            }
        }
    }

    private void trackLoginEvent(JsonObject params, List<PlatformType> trackingPlatforms) {

        for (PlatformType tracking : trackingPlatforms) {
            switch (tracking) {
                case GTM:
                    Logger.d("EventTracked", "GTM");
                    break;
                case GCM:
                    break;
                case MOENGAGE:
                    break;
                case FCM:
                    try{
                        String name=params.get(EventConstant.Name).toString();
                        String platformType=params.get(EventConstant.PlatformType).toString();
                        Logger.d("ValueForFcm-->>"+name+"  "+platformType);
                        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                        Bundle bundle = new Bundle();
                        bundle.putString(EventConstant.Name, name);
                        bundle.putString(EventConstant.PlatformType, platformType);
                        mFirebaseAnalytics.logEvent(TrackerEvent.LOGIN.name(), bundle);

                    }catch (Exception ignored){

                    }
                    break;
                default:
            }
        }
    }

}
