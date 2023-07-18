package com.tv.uscreen.yojmatv.beanModel.userProfile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomDataTwo {
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("parentalPin")
    @Expose
    private String parentalPin;
    @SerializedName("NotificationCheck")
    @Expose
    private String notificationCheck;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParentalPin() {
        return parentalPin;
    }

    public void setParentalPin(String parentalPin) {
        this.parentalPin = parentalPin;
    }

    public String getNotificationCheck() {
        return notificationCheck;
    }

    public void setNotificationCheck(String notificationCheck) {
        this.notificationCheck = notificationCheck;
    }

}

