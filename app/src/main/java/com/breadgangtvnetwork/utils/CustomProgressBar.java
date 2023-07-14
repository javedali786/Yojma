package com.breadgangtvnetwork.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.breadgangtvnetwork.R;

public class CustomProgressBar extends ProgressBar {
    public CustomProgressBar(Context context) {
        super(context);
        this.setIndeterminate(true);
        this.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.series_detail_all_episode_txt_color), PorterDuff.Mode.MULTIPLY);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.series_detail_all_episode_txt_color), PorterDuff.Mode.MULTIPLY);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
