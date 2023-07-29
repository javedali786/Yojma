package com.tv.uscreen.yojmatv.utils.config.bean.dmsResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tv.uscreen.yojmatv.utils.config.bean.AvatarImages;

import java.util.List;

public class AppConfig{

	@SerializedName("vastTagUrl")
	private String vastTagUrl;

	@SerializedName("contentPreferences")
	private ContentPreferences contentPreferences;

	@SerializedName("helpCenterUrl")
	private String helpCenterUrl;

	@SerializedName("cloudFrontBaseURL")
	private String cloudFrontBaseURL;

	@SerializedName("profilePictureBaseFolder")
	private String profilePictureBaseFolder;

	@SerializedName("bingeWatchingEnabled")
	private boolean bingeWatchingEnabled;

	@SerializedName("liveNavPlayId")
	private String liveNavPlayId;

	@SerializedName("timer")
	private int timer;

	@SerializedName("supportedCurrencies")
	private List<String> supportedCurrencies;

	@SerializedName("aboutUsUrl")
	private String aboutUsUrl;

	@SerializedName("termsConditionUrl")
	private String termsConditionUrl;

	@SerializedName("imageTransformBaseURL")
	private String imageTransformBaseURL;

	@SerializedName("popularSearchId")
	private int popularSearchId;

	@SerializedName("faqUrl")
	private String faqUrl;

	@SerializedName("subscriptionBaseUrl")
	private String subscriptionBaseUrl;


	@SerializedName("jwPlayerDiliveryBaseUrl")
	private String jwPlayerDiliveryBaseUrl;
	@SerializedName("paymentBaseUrl")
	private String paymentBaseUrl;

	@SerializedName("participationBaseUrl")
	private String participationBaseUrl;

	public String getParticipationBaseUrl() {
		return participationBaseUrl;
	}

	public void setParticipationBaseUrl(String participationBaseUrl) {
		this.participationBaseUrl = participationBaseUrl;
	}

	@SerializedName("contactUsUrl")
	private String contactUsUrl;

	@SerializedName("privacyPolicyUrl")
	private String privacyPolicyUrl;

	@SerializedName("version")
	private Version version;

	@SerializedName("feedbackUrl")
	private String feedbackUrl;

	@SerializedName("languageCodes")
	private LanguageCodes languageCodes;

	@SerializedName("baseUrl")
	private String baseUrl;

	@SerializedName("playbackBaseUrl")
	private String playbackBaseUrl;

	public String getPlaybackBaseUrl() {
		return playbackBaseUrl;
	}

	public void setPlaybackBaseUrl(String playbackBaseUrl) {
		this.playbackBaseUrl = playbackBaseUrl;
	}

	@SerializedName("searchBaseUrl")
	private String searchBaseUrl;

	@SerializedName("mediaTypes")
	private MediaTypes mediaTypes;

	@SerializedName("couponBaseUrl")
	private String couponBaseUrl;

	@SerializedName("navScreens")
	private List<NavScreensItem> navScreens;

	public List<UserProfile> getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(List<UserProfile> userProfile) {
		this.userProfile = userProfile;
	}

	@SerializedName("userProfile")
	private List<UserProfile> userProfile;

	@SerializedName("searchFilters")
	private SearchFilters searchFilters;

	@SerializedName("ovpBaseUrl")
	private String ovpBaseUrl;

	@SerializedName("primaryLanguage")
	private String primaryLanguage;

	@SerializedName("liveBaseURL")
	private String liveBaseURL;

	@SerializedName("avatarImages")
	@Expose
	private List<AvatarImages> avatarImages = null;
	public List<AvatarImages> getAvatarImages() {
		return avatarImages;
	}

	public void setAvatarImages(List<AvatarImages> avatarImages) {
		this.avatarImages = avatarImages;
	}

	public String getJwPlayerDiliveryBaseUrl() {
		return jwPlayerDiliveryBaseUrl;
	}

	public String getVastTagUrl(){
		return vastTagUrl;
	}

	public ContentPreferences getContentPreferences(){
		return contentPreferences;
	}

	public String getHelpCenterUrl(){
		return helpCenterUrl;
	}

	public String getCloudFrontBaseURL(){
		return cloudFrontBaseURL;
	}

	public String getProfilePictureBaseFolder(){
		return profilePictureBaseFolder;
	}

	public boolean isBingeWatchingEnabled(){
		return bingeWatchingEnabled;
	}

	public String getLiveNavPlayId(){
		return liveNavPlayId;
	}

	public int getTimer(){
		return timer;
	}

	public List<String> getSupportedCurrencies(){
		return supportedCurrencies;
	}

	public String getAboutUsUrl(){
		return aboutUsUrl;
	}

	public String getTermsConditionUrl(){
		return termsConditionUrl;
	}

	public String getImageTransformBaseURL(){
		return imageTransformBaseURL;
	}

	public int getPopularSearchId(){
		return popularSearchId;
	}

	public String getFaqUrl(){
		return faqUrl;
	}

	public String getSubscriptionBaseUrl(){
		return subscriptionBaseUrl;
	}

	public String getPaymentBaseUrl(){
		return paymentBaseUrl;
	}

	public String getContactUsUrl(){
		return contactUsUrl;
	}

	public String getPrivacyPolicyUrl(){
		return privacyPolicyUrl;
	}

	public Version getVersion(){
		return version;
	}

	public String getFeedbackUrl(){
		return feedbackUrl;
	}

	public LanguageCodes getLanguageCodes(){
		return languageCodes;
	}

	public String getBaseUrl(){
		return baseUrl;
	}

	public String getSearchBaseUrl(){
		return searchBaseUrl;
	}

	public MediaTypes getMediaTypes(){
		return mediaTypes;
	}

	public String getCouponBaseUrl(){
		return couponBaseUrl;
	}

	public List<NavScreensItem> getNavScreens(){
		return navScreens;
	}

	public SearchFilters getSearchFilters(){
		return searchFilters;
	}

	public String getOvpBaseUrl(){
		return ovpBaseUrl;
	}

	public String getPrimaryLanguage(){
		return primaryLanguage;
	}

	public String getLiveBaseURL() {
		return liveBaseURL;
	}

	public void setLiveBaseURL(String liveBaseURL) {
		this.liveBaseURL = liveBaseURL;
	}
}