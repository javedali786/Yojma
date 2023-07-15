package com.breadgangtvnetwork.activities.purchase.purchase_model;


import java.io.Serializable;
import java.util.List;
import java.util.Objects;
@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
public class PurchaseModel implements Serializable {

    private String purchaseOptions;
    private String price;
    private String index;
    private boolean isSelected;
    private Object onTrial;
    private String subscribedText;
    private String currency;
    private String customIdentifier;
    private String identifier;
    private String title;
    private String offerPeriod;
    private int offerPeriodDuration;
    private String periodType;
    private Long expiryDate;
    private Long currentExpiryDate;
    private Long nextChargeDate;
    private Long createdDate;

    private String description;
    private boolean fromClick;
    private String subscriptionType;
    private String trialType;
    private int trialDuration;
    private List<String> subscriptionList;
    private List productList;
    private boolean entitlementState;
    private PaymentCustomData customData;
    String paymentProvider;
    String transactionID;
    String orderStatus;

    int subscriptionOrder;

    public void setTrialType(String trialType) {
        this.trialType = trialType;
    }

    public String getTrialType() {
        return trialType;
    }

    public void setTrialDuration(int trialDuration) {
        this.trialDuration = trialDuration;
    }

    public int getTrialDuration() {
        return trialDuration;
    }

    public List<String> getSubscriptionList() {
        return subscriptionList;
    }

    public void setSubscriptionList(List<String> subscriptionList) {
        this.subscriptionList = subscriptionList;
    }

    public List<String> getProductList() {
        return productList;
    }

    public void setProductList(List<String> productList) {
        this.productList = productList;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public PurchaseModel() {
    }

    public PurchaseModel(String purchaseOptions, String price, boolean isSelected) {
        this.purchaseOptions = purchaseOptions;
        this.price = price;
        this.isSelected = isSelected;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSubscribedText() {
        return subscribedText;
    }

    public void setSubscribedText(String subscribedText) {
        this.subscribedText = subscribedText;
    }

    public String getPurchaseOptions() {
        return purchaseOptions;
    }

    public void setPurchaseOptions(String purchaseOptions) {
        this.purchaseOptions = purchaseOptions;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Object isOnTrial() {
        return onTrial;
    }

    public void setOnTrial(Object selected) {
        onTrial = selected;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setCustomIdentifier(String identifier) {
        this.customIdentifier = identifier;
    }

    public String getCustomIdentifier() {
        return customIdentifier;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setOfferPeriod(String offerPeriod) {
        this.offerPeriod = offerPeriod;
    }

    public String getOfferPeriod() {
        return offerPeriod;
    }

    public int getOfferPeriodDuration() {
        return offerPeriodDuration;
    }

    public void setOfferPeriodDuration(int offerPeriodDuration) {
        this.offerPeriodDuration = offerPeriodDuration;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public String getPaymentProvider() {
        return paymentProvider;
    }

    public void setPaymentProvider(String periodType) {
        this.paymentProvider = periodType;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String periodType) {
        this.transactionID = periodType;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String periodType) {
        this.orderStatus = periodType;
    }

    public void setExpiryDate(Long expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Long getExpiryDate() {
        return expiryDate;
    }

    public void setCurrentExpiryDate(Long expiryDate) {
        this.currentExpiryDate = expiryDate;
    }

    public Long getCurrentExpiryDate() {
        return currentExpiryDate;
    }

    public void setNextChargeDate(Long expiryDate) {
        this.nextChargeDate = expiryDate;
    }

    public Long getNextChargeDate() {
        return nextChargeDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setFromClick(boolean fromClick) {
        this.fromClick = fromClick;
    }

    public boolean isFromClick() {
        return fromClick;
    }

    public Boolean getEntitlementState() {
        return entitlementState;
    }

    public void setEntitlementState(Boolean entitlementState) {
        this.entitlementState = entitlementState;
    }

    public PaymentCustomData getCustomData() {
        return customData;
    }

    public void setCustomData(PaymentCustomData recurringOffer) {
        this.customData = recurringOffer;
    }

    @Override
    public boolean equals(Object obj) {
        PurchaseModel purchaseModel = (PurchaseModel) obj;
        return (this.isSelected == purchaseModel.isSelected());
    }

    @Override
    public int hashCode() {
        return Objects.hash(purchaseOptions, price, isSelected);
    }

    public int getSubscriptionOrder() {
        return subscriptionOrder;
    }

    public void setSubscriptionOrder(int subscriptionOrder) {
        this.subscriptionOrder = subscriptionOrder;
    }

    public void setCreatedDate(Long expiryDate) {
        this.createdDate = expiryDate;
    }

    public Long getCreatedDate() {
        return createdDate;
    }
}
