
package com.tv.uscreen.yojmatv.beanModel.entitle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tv.uscreen.yojmatv.activities.purchase.purchase_model.PaymentCustomData;

import java.io.Serializable;
import java.util.List;

public class PurchaseA implements Serializable {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("identifier")
    @Expose
    private String identifier;
    @SerializedName("entitlementState")
    @Expose
    private Boolean entitlementState;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("voDOfferType")
    @Expose
    private String voDOfferType;
    @SerializedName("rentalPeriod")
    @Expose
    private RentalPeriod rentalPeriod;
    @SerializedName("prices")
    @Expose
    private List<Price> prices = null;
    @SerializedName("offerStatus")
    @Expose
    private String offerStatus;
    @SerializedName("linkedContentSKUs")
    @Expose
    private Object linkedContentSKUs;
    @SerializedName("offerType")
    @Expose
    private String offerType;
    @SerializedName("subscriptionOrder")
    @Expose
    private Long subscriptionOrder;
    @SerializedName("oneTimeOffer")
    @Expose
    private OneTimeOffer oneTimeOffer;
    @SerializedName("recurringOffer")
    @Expose
    private RecurringOffer recurringOffer;
    @SerializedName("isDefault")
    @Expose
    private Object isDefault;

    @SerializedName("customData")
    @Expose
    private PaymentCustomData customData;

    @SerializedName("expiryDate")
    @Expose
    private Long expiryDate;

    @SerializedName("currentExpiry")
    @Expose
    private Long currentExpiry;

    @SerializedName("nextChargeDate")
    @Expose
    private Long nextChargeDate;

    @SerializedName("onTrial")
    @Expose
    private Object onTrial;

    @SerializedName("description")
    @Expose
    private String description;



    public Object isOnTrial() {
        return onTrial;
    }

    public void setOnTrial(Object selected) {
        onTrial = selected;
    }

    public Long getNextChargeDate() {
        return nextChargeDate;
    }

    public void setNextChargeDate(Long nextChargeDate) {
        this.nextChargeDate = nextChargeDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Boolean getEntitlementState() {
        return entitlementState;
    }

    public void setEntitlementState(Boolean entitlementState) {
        this.entitlementState = entitlementState;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVoDOfferType() {
        return voDOfferType;
    }

    public void setVoDOfferType(String voDOfferType) {
        this.voDOfferType = voDOfferType;
    }

    public RentalPeriod getRentalPeriod() {
        return rentalPeriod;
    }

    public void setRentalPeriod(RentalPeriod rentalPeriod) {
        this.rentalPeriod = rentalPeriod;
    }

    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

    public String getOfferStatus() {
        return offerStatus;
    }

    public void setOfferStatus(String offerStatus) {
        this.offerStatus = offerStatus;
    }

    public Object getLinkedContentSKUs() {
        return linkedContentSKUs;
    }

    public void setLinkedContentSKUs(Object linkedContentSKUs) {
        this.linkedContentSKUs = linkedContentSKUs;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public Long getSubscriptionOrder() {
        return subscriptionOrder;
    }

    public void setSubscriptionOrder(Long subscriptionOrder) {
        this.subscriptionOrder = subscriptionOrder;
    }

    public OneTimeOffer getOneTimeOffer() {
        return oneTimeOffer;
    }

    public void setOneTimeOffer(OneTimeOffer oneTimeOffer) {
        this.oneTimeOffer = oneTimeOffer;
    }

    public RecurringOffer getRecurringOffer() {
        return recurringOffer;
    }

    public void setRecurringOffer(RecurringOffer recurringOffer) {
        this.recurringOffer = recurringOffer;
    }

    public Object getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Object isDefault) {
        this.isDefault = isDefault;
    }

    public PaymentCustomData getCustomData() {
        return customData;
    }

    public void setCustomData(PaymentCustomData recurringOffer) {
        this.customData = recurringOffer;
    }

    public Long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Long expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Long getCurrentExpiry() {
        return currentExpiry;
    }

    public void setCurrentExpiry(Long currentExpiry) {
        this.currentExpiry = currentExpiry;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
