package com.tv.uscreen.beanModel.userProfile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomData {
    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("city")
    @Expose
    private String city;



    @SerializedName("mobileNumber")
    @Expose
    private Object mobileNumber;


    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("NotificationCheck")
    @Expose
    private String isNotification;

    @SerializedName("contentPreferences")
    @Expose
    private String contentPreferences;

    public Object getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(Object mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIsNotification() {
        return isNotification;
    }

    public void setIsNotification(String isNotification) {
        this.isNotification = isNotification;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfileAvatar() {
        return profileAvatar;
    }

    public void setProfileAvatar(String profileAvatar) {
        this.profileAvatar = profileAvatar;
    }

    public void setContentPreferences(String contentPreferences) {
        this.contentPreferences = contentPreferences;
    }

    public String getContentPreferences() {
        return contentPreferences;
    }

    @SerializedName("profileAvatar")
    @Expose
    private String profileAvatar;

    public String getParentalPin() {
        return parentalPin;
    }

    public void setParentalPin(String parentalPin) {
        this.parentalPin = parentalPin;
    }

    @SerializedName("parentalPin")
    @Expose
    private String parentalPin;

    @Override
    public String toString() {
        return "CustomData{" +
                "address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", mobileNumber=" + mobileNumber +
                ", country='" + country + '\'' +
                ", isNotification='" + isNotification + '\'' +
                ", contentPreferences='" + contentPreferences + '\'' +
                ", profileAvatar='" + profileAvatar + '\'' +
                ", parentalPin='" + parentalPin + '\'' +
                '}';
    }
}
