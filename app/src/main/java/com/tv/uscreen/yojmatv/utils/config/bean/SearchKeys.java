package com.tv.uscreen.yojmatv.utils.config.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchKeys {
    @SerializedName("en-US")
    @Expose
    private String enUS;
    @SerializedName("th")
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
