package com.tv.utils.config.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Genre {
    @SerializedName("searchKeys")
    @Expose
    private SearchKeys searchKeys;
    @SerializedName("displayName")
    @Expose
    private DisplayName displayName;

    public boolean isGenereChecked() {
        return isGenereChecked;
    }

    public void setGenereChecked(boolean genereChecked) {
        isGenereChecked = genereChecked;
    }

    private boolean isGenereChecked;

    public SearchKeys getSearchKeys() {
        return searchKeys;
    }

    public void setSearchKeys(SearchKeys searchKeys) {
        this.searchKeys = searchKeys;
    }

    public DisplayName getDisplayName() {
        return displayName;
    }

    public void setDisplayName(DisplayName displayName) {
        this.displayName = displayName;
    }
}
