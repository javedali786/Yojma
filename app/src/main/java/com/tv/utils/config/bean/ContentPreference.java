package com.tv.utils.config.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContentPreference {
    @SerializedName("identifier")
    @Expose
    private String identifier;
    @SerializedName("displayName")
    @Expose
    private String name;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
