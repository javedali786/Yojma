package com.tv.uscreen.utils;

import android.content.Context;


import com.tv.uscreen.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public final class TimeUtil {
    public static final int ONE_MINUTE_IN_MS = 60 * 1000;
    public static final int ONE_HOUR_IN_MS = 60 * 60 * 1000;
    public static final long ONE_DAY_IN_MS = 24L * ONE_HOUR_IN_MS;
    public static final String DATE_FORMAT_DD_MM_YYYY = "dd/MM/yyyy";
    public static final String DATE_FORMAT_HH_MM = "HH:mm";
    public static final String DATE_FORMAT_MMMM_DD_YYYY = "MMMM dd, yyyy";


    private TimeUtil() {
    }

    public static String getTimeLabel(Context context, long timeInMs) {
        final long current = System.currentTimeMillis();

        final long diff = current - timeInMs;
        if (diff < 5000) {
            return context.getString(R.string.few_seconds_ago);
        } else {
            final long minutes = diff/ONE_MINUTE_IN_MS;
            if (minutes == 0) {
                return context.getString(R.string.seconds_ago, diff/1000);
            } else {
                if (minutes < 60) {
                    return context.getString(R.string.minute_ago, minutes);
                } else {
                    final long hour = minutes / ONE_HOUR_IN_MS;
                    if (hour > 1) {
                        return context.getString(R.string.hours_ago, hour);
                    } else {
                        return context.getString(R.string.hour_ago);
                    }
                }
            }
        }
    }

    public static String convertMsToFormat(long timeInMs, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(new Date(timeInMs));
    }

    public static long getTodayStart() {
        final Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime().getTime();
    }

}
