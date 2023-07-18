
package com.tv.uscreen.beanModel.entitle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {

    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("purchaseAs")
    @Expose
    private List<PurchaseA> purchaseAs = null;
    @SerializedName("entitledAs")
    @Expose
    private List<EntitledAs> entitledAs;
    @SerializedName("brightcoveVideoId")
    @Expose
    private String brightcoveVideoId;
    @SerializedName("entitled")
    @Expose
    private Boolean entitled;

    @SerializedName("externalRefId")
    @Expose
    private String externalRefId;

    @SerializedName("externalUrl")
    @Expose
    private String externalUrl;

    public String getExternalRefId() {
        return externalRefId;
    }

    public void setExternalRefId(String externalRefId) {
        this.externalRefId = externalRefId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public List<PurchaseA> getPurchaseAs() {
        return purchaseAs;
    }

    public void setPurchaseAs(List<PurchaseA> purchaseAs) {
        this.purchaseAs = purchaseAs;
    }

    public List<EntitledAs> getEntitledAs() {
        return entitledAs;
    }

    public void setEntitledAs(List<EntitledAs> entitledAs) {
        this.entitledAs = entitledAs;
    }

    public String getBrightcoveVideoId() {
        return brightcoveVideoId;
    }

    public void setBrightcoveVideoId(String brightcoveVideoId) {
        this.brightcoveVideoId = brightcoveVideoId;
    }

    public Boolean getEntitled() {
        return entitled;
    }

    public void setEntitled(Boolean entitled) {
        this.entitled = entitled;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }
}
