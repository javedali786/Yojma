package com.tv.uscreen.utils.config.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DisplayNameValue {
    @SerializedName("en-US")
    @Expose
    private String enUS;
    @SerializedName("th-TH")
    @Expose
    private String th;

    public String getEnUS() {
        return enUS;
    }

    public void setEnUS(String enUS) {
        this.enUS = enUS;
    }

    public String getTh() {
        return th;
    }

    public void setTh(String th) {
        this.th = th;
    }
}
