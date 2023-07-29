
package com.tv.uscreen.yojmatv.utils.config.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppConfig {

    @SerializedName("aboutUsUrl")
    @Expose
    private String aboutUsUrl;
    @SerializedName("baseUrl")
    @Expose
    private String baseUrl;
    @SerializedName("bingeWatchingEnabled")
    @Expose
    private Boolean bingeWatchingEnabled;
    @SerializedName("couponBaseUrl")
    @Expose
    private String couponBaseUrl;
    @SerializedName("imageTransformBaseURL")
    @Expose
    private String imageTransformBaseURL;
    @SerializedName("downloadEnable")
    @Expose
    private Boolean downloadEnable;
    @SerializedName("languageCodes")
    @Expose
    private LanguageCodes languageCodes;
    @SerializedName("mediaTypes")
    @Expose
    private MediaTypes mediaTypes;
    @SerializedName("navScreens")
    @Expose
    private List<NavScreen> navScreens = null;
    @SerializedName("ovpBaseUrl")
    @Expose
    private String ovpBaseUrl;
    @SerializedName("popularSearchId")
    @Expose
    private String popularSearchId;
    @SerializedName("primaryLanguage")
    @Expose
    private String primaryLanguage;
    @SerializedName("privacyPolicyUrl")
    @Expose
    private String privacyPolicyUrl;
    @SerializedName("searchBaseUrl")
    @Expose
    private String searchBaseUrl;
    @SerializedName("subscriptionBaseUrl")
    @Expose
    private String subscriptionBaseUrl;
    @SerializedName("termsConditionUrl")
    @Expose
    private String termsConditionUrl;
    @SerializedName("timer")
    @Expose
    private Integer timer;
    @SerializedName("vastTagUrl")
    @Expose
    private String vastTagUrl;
    @SerializedName("version")
    @Expose
    private Version version;

    @SerializedName("jwPlayerDiliveryBaseUrl")
    private String jwPlayerDiliveryBaseUrl;

    public String getJwPlayerDiliveryBaseUrl() {
        return jwPlayerDiliveryBaseUrl;
    }

    public void setJwPlayerDiliveryBaseUrl(String jwPlayerDiliveryBaseUrl) {
        this.jwPlayerDiliveryBaseUrl = jwPlayerDiliveryBaseUrl;
    }

    public ParentalControl getParentalControl() {
        return parentalControl;
    }

    public void setParentalControl(ParentalControl parentalControl) {
        this.parentalControl = parentalControl;
    }

    @SerializedName("parentalControl")
    @Expose
    private ParentalControl parentalControl;

    public String getAboutUsUrl() {
        return aboutUsUrl;
    }

    public void setAboutUsUrl(String aboutUsUrl) {
        this.aboutUsUrl = aboutUsUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Boolean getBingeWatchingEnabled() {
        return bingeWatchingEnabled;
    }

    public void setBingeWatchingEnabled(Boolean bingeWatchingEnabled) {
        this.bingeWatchingEnabled = bingeWatchingEnabled;
    }

    public String getCouponBaseUrl() {
        return couponBaseUrl;
    }

    public void setCouponBaseUrl(String couponBaseUrl) {
        this.couponBaseUrl = couponBaseUrl;
    }

    public String getImageTransformBaseURL() {
        return imageTransformBaseURL;
    }

    public void setImageTransformBaseURL(String imageTransformBaseURL) {
        this.imageTransformBaseURL = imageTransformBaseURL;
    }

    public Boolean getDownloadEnable() {
        return downloadEnable;
    }

    public void setDownloadEnable(Boolean downloadEnable) {
        this.downloadEnable = downloadEnable;
    }

    public LanguageCodes getLanguageCodes() {
        return languageCodes;
    }

    public void setLanguageCodes(LanguageCodes languageCodes) {
        this.languageCodes = languageCodes;
    }

    public MediaTypes getMediaTypes() {
        return mediaTypes;
    }

    public void setMediaTypes(MediaTypes mediaTypes) {
        this.mediaTypes = mediaTypes;
    }

    public List<NavScreen> getNavScreens() {
        return navScreens;
    }

    public void setNavScreens(List<NavScreen> navScreens) {
        this.navScreens = navScreens;
    }

    public String getOvpBaseUrl() {
        return ovpBaseUrl;
    }

    public void setOvpBaseUrl(String ovpBaseUrl) {
        this.ovpBaseUrl = ovpBaseUrl;
    }

    public String getPopularSearchId() {
        return popularSearchId;
    }

    public void setPopularSearchId(String popularSearchId) {
        this.popularSearchId = popularSearchId;
    }

    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    public void setPrimaryLanguage(String primaryLanguage) {
        this.primaryLanguage = primaryLanguage;
    }

    public String getPrivacyPolicyUrl() {
        return privacyPolicyUrl;
    }

    public void setPrivacyPolicyUrl(String privacyPolicyUrl) {
        this.privacyPolicyUrl = privacyPolicyUrl;
    }

    public String getSearchBaseUrl() {
        return searchBaseUrl;
    }

    public void setSearchBaseUrl(String searchBaseUrl) {
        this.searchBaseUrl = searchBaseUrl;
    }

    public String getSubscriptionBaseUrl() {
        return subscriptionBaseUrl;
    }

    public void setSubscriptionBaseUrl(String subscriptionBaseUrl) {
        this.subscriptionBaseUrl = subscriptionBaseUrl;
    }

    public String getTermsConditionUrl() {
        return termsConditionUrl;
    }

    public void setTermsConditionUrl(String termsConditionUrl) {
        this.termsConditionUrl = termsConditionUrl;
    }

    public Integer getTimer() {
        return timer;
    }

    public void setTimer(Integer timer) {
        this.timer = timer;
    }

    public String getVastTagUrl() {
        return vastTagUrl;
    }

    public void setVastTagUrl(String vastTagUrl) {
        this.vastTagUrl = vastTagUrl;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public SearchFilters getSearchFilters() {
        return searchFilters;
    }

    public void setSearchFilters(SearchFilters searchFilters) {
        this.searchFilters = searchFilters;
    }

    @SerializedName("searchFilters")
    @Expose
    private SearchFilters searchFilters;

    public String getCloudFrontBaseURL() {
        return cloudFrontBaseURL;
    }

    public void setCloudFrontBaseURL(String cloudFrontBaseURL) {
        this.cloudFrontBaseURL = cloudFrontBaseURL;
    }

    @SerializedName("cloudFrontBaseURL")
    @Expose
    private String cloudFrontBaseURL;

    public String getPaymentBaseUrl() {
        return paymentBaseUrl;
    }

    public void setPaymentBaseUrl(String paymentBaseUrl) {
        this.paymentBaseUrl = paymentBaseUrl;
    }
    @SerializedName("paymentBaseUrl")
    @Expose
    private String paymentBaseUrl;


    @SerializedName("feedbackUrl")
    @Expose
    private String feedbackUrl;

    @SerializedName("faqUrl")
    @Expose
    private String faqUrl;
    public String getFeedbackUrl() {
        return feedbackUrl;
    }

    public void setFeedbackUrl(String feedbackUrl) {
        this.feedbackUrl = feedbackUrl;
    }

    public String getFaqUrl() {
        return faqUrl;
    }

    public void setFaqUrl(String faqUrl) {
        this.faqUrl = faqUrl;
    }

    @SerializedName("contactUsUrl")
    @Expose
    private String contactUsUrl;
    public String getContactUsUrl() {
        return contactUsUrl;
    }

    public void setContactUsUrl(String contactUsUrl) {
        this.contactUsUrl = contactUsUrl;
    }

    @SerializedName("supportedCurrencies")
    @Expose
    private List<String> supportedCurrencies = null;
    public List<String> getSupportedCurrencies() {
        return supportedCurrencies;
    }

    public void setSupportedCurrencies(List<String> supportedCurrencies) {
        this.supportedCurrencies = supportedCurrencies;
    }


    public void setDownloadEnable(boolean downloadEnable) {
        this.downloadEnable = downloadEnable;
    }

    public boolean isDownloadEnable() {
        return downloadEnable;
    }

    @SerializedName("contentPreferences")
    @Expose
    private List<ContentPreference> contentPreferences = null;
    public List<ContentPreference> getContentPreference() {
        return contentPreferences;
    }

    public void setContentPreference(List<ContentPreference> contentPreference) {
        this.contentPreferences = contentPreference;
    }

    @SerializedName("pubnub")
    @Expose
    private Pubnub pubnub;
    public Pubnub getPubnub() {
        return pubnub;
    }

    public void setPubnub(Pubnub pubnub) {
        this.pubnub = pubnub;
    }

    @SerializedName("prerollVideoID")
    @Expose
    private String prerollVideoID;
    public String getPrerollVideoID() {
        return prerollVideoID;
    }

    public void setPrerollVideoID(String prerollVideoID) {
        this.prerollVideoID = prerollVideoID;
    }

    @SerializedName("avatarImages")
    @Expose
    private List<AvatarImages> avatarImages = null;
    public List<AvatarImages> getavatarImages() {
        return avatarImages;
    }

    public void setavatarImages(List<AvatarImages> avatarImages) {
        this.avatarImages = avatarImages;
    }

    @SerializedName("profilePictureBaseFolder")
    @Expose
    private String profilePictureBaseFolder;

    public String getProfilePictureBaseFolder() {
        return profilePictureBaseFolder;
    }

    public void setProfilePictureBaseFolder(String profilePictureBaseFolder) {
        this.profilePictureBaseFolder = profilePictureBaseFolder;
    }

}
