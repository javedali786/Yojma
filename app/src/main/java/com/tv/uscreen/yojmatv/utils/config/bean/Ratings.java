package com.tv.uscreen.yojmatv.utils.config.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ratings {
    public DisplayNameValue getDisplayName() {
        return displayName;
    }

    public void setDisplayName(DisplayNameValue displayName) {
        this.displayName = displayName;
    }

    @SerializedName("displayName")
    @Expose
    private DisplayNameValue displayName;

    @SerializedName("level")
    @Expose
    private String level;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(String ratingValue) {
        this.ratingValue = ratingValue;
    }

    @SerializedName("ratingValue")
    @Expose
    private String ratingValue;
}
