package com.breadgangtvnetwork.utils.config.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pubnub {
    @SerializedName("channelName")
    @Expose
    private String channelName;

    @SerializedName("liveChatEnabled")
    @Expose
    private boolean liveChatEnabled;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public boolean isLiveChatEnabled() {
        return liveChatEnabled;
    }

    public void setLiveChatEnabled(boolean liveChatEnabled) {
        this.liveChatEnabled = liveChatEnabled;
    }
}
