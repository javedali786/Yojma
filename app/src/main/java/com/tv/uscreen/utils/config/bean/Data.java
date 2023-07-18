
package com.tv.uscreen.utils.config.bean;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Data {

    @SerializedName("appConfig")
    @Expose
    private AppConfig appConfig;
    @SerializedName("dateCreated")
    @Expose
    private Long dateCreated;
    @SerializedName("isLatest")
    @Expose
    private Boolean isLatest;
    @SerializedName("lastUpdated")
    @Expose
    private Long lastUpdated;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("version")
    @Expose
    private Integer version;

    public AppConfig getAppConfig() {
        return appConfig;
    }

    public void setAppConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public Long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Boolean getIsLatest() {
        return isLatest;
    }

    public void setIsLatest(Boolean isLatest) {
        this.isLatest = isLatest;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}
