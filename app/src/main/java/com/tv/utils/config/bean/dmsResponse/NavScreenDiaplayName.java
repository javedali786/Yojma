package com.tv.utils.config.bean.dmsResponse;

import com.google.gson.annotations.SerializedName;

public class NavScreenDiaplayName {
    @SerializedName("en-US")
    private String enUs;
    @SerializedName("es-ES")
    private String enEs;

    public String getEnUs() {
        return enUs;
    }

    public void setEnUs(String enUs) {
        this.enUs = enUs;
    }

    public String getEnEs() {
        return enEs;
    }

    public void setEnEs(String enEs) {
        this.enEs = enEs;
    }
}
