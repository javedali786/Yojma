package com.breadgangtvnetwork.utils;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.breadgangtvnetwork.BuildConfig;


public final class Logger {
    public static final String LOG_MSG_CALLED_FROM = "called from: ";

    private Logger() {
    }

    public static void i(@NonNull String message) {
        if (BuildConfig.DEBUG) {
            i(getTag(), message);
        }
    }

    public static void i(@NonNull String tag, @NonNull String message) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message);
        }
    }

    public static void v(@NonNull String message) {
        if (BuildConfig.DEBUG) {
            v(getTag(), message);
        }
    }

    public static void v(@NonNull String tag, @NonNull String message) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, message);
        }
    }

    public static void d(@NonNull String message) {
        if (BuildConfig.DEBUG) {
            d(getTag(), message);
        }
    }

    public static void d(@NonNull String tag, @NonNull String message) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void w(@NonNull String message) {
        if (BuildConfig.DEBUG) {
            w(getTag(), message);
        }
    }

    public static void w(@NonNull String tag, @NonNull String message) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, message);
        }
    }

    public static void w(Throwable throwable) {
        if (BuildConfig.DEBUG) {
            Log.w(getTag(), throwable.getMessage(), throwable);
        }
    }

    public static void e(Throwable throwable) {
        if (BuildConfig.DEBUG) {
            Log.e(getTag(), throwable.getMessage(), throwable);
        }
    }

    public static void e(@NonNull String message) {
        if (BuildConfig.DEBUG) {
            e(getTag(), message);
        }
    }

    public static void e(@NonNull String tag, @NonNull String message) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message);
        }
    }

    @NonNull
    public static String getTag() {
        StackTraceElement element = Thread.currentThread().getStackTrace()[4];
        String tag = getSimpleClassName(element.getClassName()) + ':' + element.getMethodName()
                + "() ";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return tag;
        } else {
            return tag.substring(0, Math.min(23, tag.length()));
        }
    }

    @NonNull
    private static String getSimpleClassName(String className) {
        int last = className.lastIndexOf('.') + 1;
        return className.substring(last);
    }
}

