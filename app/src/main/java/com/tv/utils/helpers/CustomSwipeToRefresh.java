package com.tv.utils.helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tv.R;
import com.tv.utils.constants.AppConstants;
import com.tv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;


public class CustomSwipeToRefresh extends SwipeRefreshLayout {

    private final int mTouchSlop;
    private float mPrevX;


    public CustomSwipeToRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (KsPreferenceKeys.getInstance().getCurrentTheme().equalsIgnoreCase(AppConstants.LIGHT_THEME)) {
            setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.unselected_indicator_color));
            setColorSchemeResources(R.color.series_detail_all_episode_txt_color);
        }else {
            setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.unselected_indicator_color));
            setColorSchemeResources(R.color.series_detail_all_episode_txt_color);
        }
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = MotionEvent.obtain(event).getX();
                break;

            case MotionEvent.ACTION_MOVE:
                final float eventX = event.getX();
                float xDiff = Math.abs(eventX - mPrevX);

                if (xDiff > mTouchSlop) {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(event);
    }

}