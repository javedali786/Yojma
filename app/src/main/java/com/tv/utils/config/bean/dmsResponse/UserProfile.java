package com.tv.utils.config.bean.dmsResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserProfile {
    @SerializedName("id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("profile_identifier")
    private String profile_identifier;

    public String getProfile_identifier() {
        return profile_identifier;
    }

    public void setProfile_identifier(String profile_identifier) {
        this.profile_identifier = profile_identifier;
    }

    public List<NavScreenDetails> getNavScreens() {
        return navScreens;
    }

    public void setNavScreens(List<NavScreenDetails> navScreens) {
        this.navScreens = navScreens;
    }

    @SerializedName("navScreens")
    private List<NavScreenDetails> navScreens;
}
