package com.tv.tarcker;

import android.content.Context;

import com.google.gson.JsonObject;

public class FCMEvents {

    private static FCMEvents instance;
    Context context;

    private FCMEvents() {
    }

    public static FCMEvents getInstance() {
        if (instance == null) {
            instance = new FCMEvents();
        }
        return (instance);
    }

    public void trackEvent(int eventType, JsonObject requestParam) {
//        switch (eventType) {
//            case 1:
//                break;
//            case 2:
//                break;
//            case 3:
//                break;
//
//            case 4:
//            case 5:
//                break;
//            default:
//                break;
//        }
    }

    public FCMEvents setContext(Context context) {
        this.context=context;
        return this;
    }
}
