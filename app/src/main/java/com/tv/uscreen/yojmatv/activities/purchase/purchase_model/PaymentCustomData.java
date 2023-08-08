package com.tv.uscreen.yojmatv.activities.purchase.purchase_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaymentCustomData implements Serializable {

    @SerializedName("key1")
    @Expose
    private String key1;
    @SerializedName("key2")
    @Expose
    private String key2;
    @SerializedName("key3")
    @Expose
    private String key3;
    @SerializedName("orderIdentifier")
    @Expose
    private String orderIdentifier;

    @SerializedName("description_en")
    @Expose
    private String description_en;

    @SerializedName("description_es")
    @Expose
    private String description_es;

    @SerializedName("trialType_en")
    @Expose
    private String trialType_en;

    @SerializedName("trialType_es")
    @Expose
    private String trialType_es;

    @SerializedName("title_en")
    @Expose
    private String title_en;
    
    @SerializedName("title_es")
    @Expose
    private String title_es;


    public String getDescription_en() {
        return description_en;
    }

    public void setDescription_en(String description_en) {
        this.description_en = description_en;
    }

    public String getDescription_es() {
        return description_es;
    }

    public void setDescription_es(String description_es) {
        this.description_es = description_es;
    }

    public String getTrialType_en() {
        return trialType_en;
    }

    public void setTrialType_en(String trialType_en) {
        this.trialType_en = trialType_en;
    }

    public String getTrialType_es() {
        return trialType_es;
    }

    public void setTrialType_es(String trialType_es) {
        this.trialType_es = trialType_es;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getTitle_es() {
        return title_es;
    }

    public void setTitle_es(String title_es) {
        this.title_es = title_es;
    }

    public String getPaymentProvider() {
        return paymentProvider;
    }

    public void setPaymentProvider(String paymentProvider) {
        this.paymentProvider = paymentProvider;
    }

    @SerializedName("paymentProvider")
    @Expose
    private String paymentProvider;

    @SerializedName("androidProductId")
    @Expose
    private String androidProductId;

    public String getAndroidProductId() {
        return androidProductId;
    }

    public void setAndroidProductId(String androidProductId) {
        this.androidProductId = androidProductId;
    }


    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        this.key1 = key1;
    }

    public String getKey2() {
        return key2;
    }

    public void setKey2(String key2) {
        this.key2 = key2;
    }

    public String getKey3() {
        return key3;
    }

    public void setKey3(String key3) {
        this.key3 = key3;
    }

    public String getOrderIdentifier() {
        return orderIdentifier;
    }

    public void setOrderIdentifier(String orderIdentifier) {
        this.orderIdentifier = orderIdentifier;
    }

}

