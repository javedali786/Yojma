package com.tv.uscreen.activities.purchase.purchase_model;

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

