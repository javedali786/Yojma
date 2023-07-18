package com.tv.activities.profile.model;

public class OrderHistoryModal {


    public OrderHistoryModal(String annually, String active, String validity, String validityDate, String paymentMode, String apple, String orderId, String euroPay, String date, String expireDate, String payStatus, String completeStatus) {
        this.annually = annually;
        this.active = active;
        this.validity = validity;
        this.validityDate = validityDate;
        this.paymentMode = paymentMode;
        this.apple = apple;
        this.orderId = orderId;
        this.EuroPay = euroPay;
        this.date = date;
        this.expireDate = expireDate;
        this.payStatus = payStatus;
        this.completeStatus = completeStatus;
    }

    public String getAnnually() {
        return annually;
    }

    public void setAnnually(String annually) {
        this.annually = annually;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(String validityDate) {
        this.validityDate = validityDate;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getApple() {
        return apple;
    }

    public void setApple(String apple) {
        this.apple = apple;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getEuroPay() {
        return EuroPay;
    }

    public void setEuroPay(String euroPay) {
        EuroPay = euroPay;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getCompleteStatus() {
        return completeStatus;
    }

    public void setCompleteStatus(String completeStatus) {
        this.completeStatus = completeStatus;
    }
    String annually;
    String active;
    String validity;
    String validityDate;
    String paymentMode;
    String apple;
    String orderId;
    String EuroPay;
    String date;
    String expireDate;
    String payStatus;
    String completeStatus;
}
