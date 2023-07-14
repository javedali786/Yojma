package com.breadgangtvnetwork.jwplayer.utils;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ktx.BuildConfig;


public final class Logger {
    public static final String LOG_MSG_CALLED_FROM = "called from: ";
    private static boolean ENABLE_LOG = BuildConfig.DEBUG;

    private Logger() {
    }

    public static void enableLog(boolean enable) {
        ENABLE_LOG = enable;
    }

    public static void i(@NonNull String message) {
        if (ENABLE_LOG) {
            i(getTag(), message);
        }
    }

    public static void i(@NonNull String tag, @NonNull String message) {
        if (ENABLE_LOG) {
            Log.i(getFilteredTag(tag), message);
        }
    }

    public static void v(@NonNull String message) {
        if (ENABLE_LOG) {
            v(getTag(), message);
        }
    }

    public static void v(@NonNull String tag, @NonNull String message) {
        if (ENABLE_LOG) {
            Log.v(getFilteredTag(tag), message);
        }
    }

    public static void d(@NonNull String message) {
        if (ENABLE_LOG) {
            d(getTag(), message);
        }
    }

    public static void d(@NonNull String tag, @NonNull String message) {
        if (ENABLE_LOG) {
            Log.d(getFilteredTag(tag), message);
        }
    }

    public static void w(@NonNull String message) {
        if (ENABLE_LOG) {
            w(getTag(), message);
        }
    }

    public static void w(@NonNull String tag, @NonNull String message) {
        if (ENABLE_LOG) {
            Log.w(getFilteredTag(tag), message);
        }
    }

    public static void w(Throwable throwable) {
        if (ENABLE_LOG) {
            Log.w(getTag(), throwable.getMessage(), throwable);
        }
    }

    public static void e(Throwable throwable) {
        if (ENABLE_LOG) {
            Log.e(getTag(), throwable.getMessage(), throwable);
        }
    }

    public static void e(@NonNull String message) {
        if (ENABLE_LOG) {
            e(getTag(), message);
        }
    }

    public static void e(@NonNull String tag, @NonNull String message) {
        if (ENABLE_LOG) {
            Log.e(getFilteredTag(tag), message);
        }
    }

    @NonNull
    private static String getTag() {
        StackTraceElement element = Thread.currentThread().getStackTrace()[4];
        String tag = getSimpleClassName(element.getClassName()) + ':' + element.getMethodName()
                + "() ";
        return getFilteredTag(tag);
    }

    @NonNull
    private static String getFilteredTag(String tag) {
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

