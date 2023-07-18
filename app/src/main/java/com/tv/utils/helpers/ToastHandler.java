package com.tv.utils.helpers;

import android.app.Activity;
import android.view.Gravity;

import androidx.core.content.ContextCompat;

import com.tv.R;


public final class ToastHandler {
    private static ToastHandler instance;

    private ToastHandler() {
    }

    public static ToastHandler getInstance() {
        if (instance == null) {
            instance = new ToastHandler();
        }
        return instance;
    }

    public void show(Activity activity, String message) {
        new MaterialToast(activity).show(message, ContextCompat.getDrawable(activity, R.drawable.toast_drawable),
                ContextCompat.getColor(activity, R.color.buy_now_pay_now_btn_text_color),
                Gravity.BOTTOM);
    }
}
