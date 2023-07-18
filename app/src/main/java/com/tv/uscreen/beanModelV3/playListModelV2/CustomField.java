package com.tv.uscreen.beanModelV3.playListModelV2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomField {
    @SerializedName("season_name")
    @Expose
    private String season_name;

    public String getSeason_name() {
        return season_name;
    }

    public void setSeason_name(String season_name) {
        this.season_name = season_name;
    }
}
