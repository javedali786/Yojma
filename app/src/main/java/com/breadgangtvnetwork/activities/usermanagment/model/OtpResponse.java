package com.breadgangtvnetwork.activities.usermanagment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtpResponse {
    @SerializedName("data")
    @Expose
    private Object data;
    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("debugMessage")
    @Expose
    private Object debugMessage;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public Object getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(Object debugMessage) {
        this.debugMessage = debugMessage;
    }

}
