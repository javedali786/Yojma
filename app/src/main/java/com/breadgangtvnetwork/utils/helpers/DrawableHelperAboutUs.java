package com.breadgangtvnetwork.utils.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

public class DrawableHelperAboutUs {
    @NonNull
    final Context mContext;

    private Drawable mDrawable;
    private Drawable mWrappedDrawable;

    public DrawableHelperAboutUs(@NonNull Context context) {
        mContext = context;
    }

    public static DrawableHelperAboutUs withContext(@NonNull Context context) {
        return new DrawableHelperAboutUs(context);
    }

    public DrawableHelperAboutUs withDrawable(@DrawableRes int drawableRes) {
        mDrawable = ContextCompat.getDrawable(mContext, drawableRes);
        return this;
    }

    public DrawableHelperAboutUs withDrawable(@NonNull Drawable drawable) {
        mDrawable = drawable;
        return this;

    }

    @SuppressLint("ResourceAsColor")
    public DrawableHelperAboutUs tint() {
        if (mDrawable == null) {
            throw new NullPointerException("É preciso informar o recurso drawable pelo método withDrawable()");
        }


        mWrappedDrawable = mDrawable.mutate();
        mWrappedDrawable = DrawableCompat.wrap(mWrappedDrawable);
        DrawableCompat.setTintMode(mWrappedDrawable, PorterDuff.Mode.SRC_IN);

        return this;
    }

    @SuppressWarnings("deprecation")
    public void applyToBackground(@NonNull View view) {
        if (mWrappedDrawable == null) {
            throw new NullPointerException("É preciso chamar o método tint()");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(mWrappedDrawable);
        } else {
            view.setBackgroundDrawable(mWrappedDrawable);
        }
    }

    public void applyTo(@NonNull ImageView imageView) {
        if (mWrappedDrawable == null) {
            throw new NullPointerException("É preciso chamar o método tint()");
        }

        imageView.setImageDrawable(mWrappedDrawable);
    }

    public void applyTo(@NonNull MenuItem menuItem) {
        if (mWrappedDrawable == null) {
            throw new NullPointerException("É preciso chamar o método tint()");
        }

        menuItem.setIcon(mWrappedDrawable);
    }

    public Drawable get() {
        if (mWrappedDrawable == null) {
            throw new NullPointerException("É preciso chamar o método tint()");
        }

        return mWrappedDrawable;
    }
}


