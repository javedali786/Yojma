package com.tv.uscreen.utils;

import android.content.Context;

import com.tv.uscreen.R;


public class ImageSize {

    private static ImageSize imageLayerInstance;

    private ImageSize() {

    }

    public static ImageSize getInstance() {
        if (imageLayerInstance == null) {
            imageLayerInstance = new ImageSize();
        }
        return (imageLayerInstance);
    }

    int width = 346;
    int height = 194;
    boolean tabletSize;

    public int getWidth(Context context) {
        tabletSize = context.getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            return 376;
        } else {
            return width;
        }
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight(Context context) {
        tabletSize = context.getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            return 214;
        } else {
            return height;
        }
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
