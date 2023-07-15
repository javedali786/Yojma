package com.breadgangtvnetwork.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.breadgangtvnetwork.R;

public class CustomBlackProgressBar extends ProgressBar {
    public CustomBlackProgressBar(Context context) {
        super(context);
        this.setIndeterminate(true);
        this.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.buy_now_pay_now_btn_text_color), PorterDuff.Mode.MULTIPLY);
    }

    public CustomBlackProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.buy_now_pay_now_btn_text_color), PorterDuff.Mode.MULTIPLY);
    }

    public CustomBlackProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
