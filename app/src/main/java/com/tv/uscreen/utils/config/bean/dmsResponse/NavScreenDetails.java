package com.tv.uscreen.utils.config.bean.dmsResponse;

import com.google.gson.annotations.SerializedName;

public class NavScreenDetails {
    @SerializedName("navScreenkey")
    private String navScreenkey;

    public String getNavScreenkey() {
        return navScreenkey;
    }

    public void setNavScreenkey(String navScreenkey) {
        this.navScreenkey = navScreenkey;
    }

    @SerializedName("navScreenDisplayName")
    private NavScreenDiaplayName displayName;

    public NavScreenDiaplayName getDisplayName() {
        return displayName;
    }

    public void setDisplayName(NavScreenDiaplayName displayName) {
        this.displayName = displayName;
    }
}
