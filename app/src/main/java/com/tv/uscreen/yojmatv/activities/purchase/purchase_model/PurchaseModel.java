package com.tv.uscreen.yojmatv.activities.purchase.purchase_model;


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

    private String description_en;
    private String description_es;
    private String title_en;
    private String trialType_en;
    private String title_es;
    private String trialType_es;

    private boolean fromClick;
    private String subscriptionType;
    private String trialType;
    private Boolean allowedTrial;

    private int trialDuration;
    private List<String> subscriptionList;
    private List productList;
    private boolean entitlementState;
    private PaymentCustomData customData;
    String paymentProvider;
    String transactionID;
    String orderStatus;

    Long subscriptionOrder;

    public Boolean getAllowedTrial() {
        return allowedTrial;
    }

    public void setAllowedTrial(Boolean allowedTrial) {
        this.allowedTrial = allowedTrial;
    }

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

    public Long getSubscriptionOrder() {
        return subscriptionOrder;
    }

    public void setSubscriptionOrder(Long subscriptionOrder) {
        this.subscriptionOrder = subscriptionOrder;
    }

    public void setCreatedDate(Long expiryDate) {
        this.createdDate = expiryDate;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

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

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getTrialType_en() {
        return trialType_en;
    }

    public void setTrialType_en(String trialType_en) {
        this.trialType_en = trialType_en;
    }

    public String getTitle_es() {
        return title_es;
    }

    public void setTitle_es(String title_es) {
        this.title_es = title_es;
    }

    public String getTrialType_es() {
        return trialType_es;
    }

    public void setTrialType_es(String trialType_es) {
        this.trialType_es = trialType_es;
    }
}
