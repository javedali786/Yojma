package com.tv.uscreen.yojmatv.utils.BindingUtils;

import android.content.Context;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;

import com.tv.uscreen.yojmatv.R;


public final class FontUtil {
    private FontUtil() {
    }

    public static Typeface getNormal(Context context) {
        return ResourcesCompat.getFont(context, R.font.inter_medium);
    }

    public static Typeface getMedium(Context context) {
        return ResourcesCompat.getFont(context, R.font.inter_medium);
    }

    public static Typeface getBold(Context context) {
        return ResourcesCompat.getFont(context, R.font.inter_medium);
    }
}
