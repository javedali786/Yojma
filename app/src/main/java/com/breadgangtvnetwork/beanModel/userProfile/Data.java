package com.breadgangtvnetwork.beanModel.userProfile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data{
	@SerializedName("id")
	@Expose
	private Integer id;
	@SerializedName("email")
	@Expose
	private Object email;
	@SerializedName("password")
	@Expose
	private Object password;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("deletionRequestStatus")
	@Expose
	private String deletionRequestStatus;
	@SerializedName("deletionRequestIdentifier")
	@Expose
	private String deletionRequestIdentifier;

	@SerializedName("verificationDate")
	@Expose
	private Long verificationDate;
	@SerializedName("profilePicURL")
	@Expose
	private Object profilePicURL;
	@SerializedName("dateOfBirth")
	@Expose
	private Object dateOfBirth;
	@SerializedName("gender")
	@Expose
	private Object gender;
	@SerializedName("phoneNumber")
	@Expose
	private Object phoneNumber;
	@SerializedName("status")
	@Expose
	private String status;
	@SerializedName("expiryDate")
	@Expose
	private Object expiryDate;
	@SerializedName("profileStep")
	@Expose
	private Object profileStep;
	@SerializedName("isFbLinked")
	@Expose
	private Boolean isFbLinked;
	@SerializedName("appUserPlans")
	@Expose
	private Object appUserPlans;
	@SerializedName("accountId")
	@Expose
	private String accountId;
	@SerializedName("subscriptions")
	@Expose
	private Object subscriptions;
	@SerializedName("customData")
	@Expose
	private CustomData customData;
	@SerializedName("userEconomics")
	@Expose
	private Object userEconomics;
	@SerializedName("killBillSubscriptionId")
	@Expose
	private Object killBillSubscriptionId;
	@SerializedName("entitlementState")
	@Expose
	private Object entitlementState;
	@SerializedName("secondaryAccounts")
	@Expose
	private Object secondaryAccounts;
	@SerializedName("primaryAccountRef")
	@Expose
	private PrimaryAccountRef primaryAccountRef;
	@SerializedName("fbLinked")
	@Expose
	private Boolean fbLinked;
	@SerializedName("gplusLinked")
	@Expose
	private Boolean gplusLinked;
	@SerializedName("manualLinked")
	@Expose
	private Boolean manualLinked;
	@SerializedName("appleLinked")
	@Expose
	private Boolean appleLinked;
	@SerializedName("primaryAccount")
	@Expose
	private Boolean primaryAccount;
	@SerializedName("kidsAccount")
	@Expose
	private Boolean kidsAccount;
	@SerializedName("verified")
	@Expose
	private Boolean verified;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Object getEmail() {
		return email;
	}

	public void setEmail(Object email) {
		this.email = email;
	}

	public Object getPassword() {
		return password;
	}

	public void setPassword(Object password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getVerificationDate() {
		return verificationDate;
	}

	public void setVerificationDate(Long verificationDate) {
		this.verificationDate = verificationDate;
	}

	public String getDeletionRequestStatus() {
		return deletionRequestStatus;
	}

	public void setDeletionRequestStatus(String deletionRequestStatus) {
		this.deletionRequestStatus = deletionRequestStatus;
	}

	public String getDeletionRequestIdentifier() {
		return deletionRequestIdentifier;
	}

	public void setDeletionRequestIdentifier(String deletionRequestIdentifier) {
		this.deletionRequestIdentifier = deletionRequestIdentifier;
	}

	public Object getProfilePicURL() {
		return profilePicURL;
	}

	public void setProfilePicURL(Object profilePicURL) {
		this.profilePicURL = profilePicURL;
	}

	public Object getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Object dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Object getGender() {
		return gender;
	}

	public void setGender(Object gender) {
		this.gender = gender;
	}

	public Object getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Object phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Object getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Object expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Object getProfileStep() {
		return profileStep;
	}

	public void setProfileStep(Object profileStep) {
		this.profileStep = profileStep;
	}

	public Boolean getIsFbLinked() {
		return isFbLinked;
	}

	public void setIsFbLinked(Boolean isFbLinked) {
		this.isFbLinked = isFbLinked;
	}

	public Object getAppUserPlans() {
		return appUserPlans;
	}

	public void setAppUserPlans(Object appUserPlans) {
		this.appUserPlans = appUserPlans;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Object getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(Object subscriptions) {
		this.subscriptions = subscriptions;
	}

	public CustomData getCustomData() {
		return customData;
	}

	public void setCustomData(CustomData customData) {
		this.customData = customData;
	}

	public Object getUserEconomics() {
		return userEconomics;
	}

	public void setUserEconomics(Object userEconomics) {
		this.userEconomics = userEconomics;
	}

	public Object getKillBillSubscriptionId() {
		return killBillSubscriptionId;
	}

	public void setKillBillSubscriptionId(Object killBillSubscriptionId) {
		this.killBillSubscriptionId = killBillSubscriptionId;
	}

	public Object getEntitlementState() {
		return entitlementState;
	}

	public void setEntitlementState(Object entitlementState) {
		this.entitlementState = entitlementState;
	}

	public Object getSecondaryAccounts() {
		return secondaryAccounts;
	}

	public void setSecondaryAccounts(Object secondaryAccounts) {
		this.secondaryAccounts = secondaryAccounts;
	}

	public PrimaryAccountRef getPrimaryAccountRef() {
		return primaryAccountRef;
	}

	public void setPrimaryAccountRef(PrimaryAccountRef primaryAccountRef) {
		this.primaryAccountRef = primaryAccountRef;
	}

	public Boolean getFbLinked() {
		return fbLinked;
	}

	public void setFbLinked(Boolean fbLinked) {
		this.fbLinked = fbLinked;
	}

	public Boolean getGplusLinked() {
		return gplusLinked;
	}

	public void setGplusLinked(Boolean gplusLinked) {
		this.gplusLinked = gplusLinked;
	}

	public Boolean getManualLinked() {
		return manualLinked;
	}

	public void setManualLinked(Boolean manualLinked) {
		this.manualLinked = manualLinked;
	}

	public Boolean getAppleLinked() {
		return appleLinked;
	}

	public void setAppleLinked(Boolean appleLinked) {
		this.appleLinked = appleLinked;
	}

	public Boolean getPrimaryAccount() {
		return primaryAccount;
	}

	public void setPrimaryAccount(Boolean primaryAccount) {
		this.primaryAccount = primaryAccount;
	}

	public Boolean getKidsAccount() {
		return kidsAccount;
	}

	public void setKidsAccount(Boolean kidsAccount) {
		this.kidsAccount = kidsAccount;
	}

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	@Override
	public String toString() {
		return "Data{" +
				"id=" + id +
				", email=" + email +
				", password=" + password +
				", name='" + name + '\'' +
				", deletionRequestStatus='" + deletionRequestStatus + '\'' +
				", deletionRequestIdentifier='" + deletionRequestIdentifier + '\'' +
				", verificationDate=" + verificationDate +
				", profilePicURL=" + profilePicURL +
				", dateOfBirth=" + dateOfBirth +
				", gender=" + gender +
				", phoneNumber=" + phoneNumber +
				", status='" + status + '\'' +
				", expiryDate=" + expiryDate +
				", profileStep=" + profileStep +
				", isFbLinked=" + isFbLinked +
				", appUserPlans=" + appUserPlans +
				", accountId='" + accountId + '\'' +
				", subscriptions=" + subscriptions +
				", customData=" + customData +
				", userEconomics=" + userEconomics +
				", killBillSubscriptionId=" + killBillSubscriptionId +
				", entitlementState=" + entitlementState +
				", secondaryAccounts=" + secondaryAccounts +
				", primaryAccountRef=" + primaryAccountRef +
				", fbLinked=" + fbLinked +
				", gplusLinked=" + gplusLinked +
				", manualLinked=" + manualLinked +
				", appleLinked=" + appleLinked +
				", primaryAccount=" + primaryAccount +
				", kidsAccount=" + kidsAccount +
				", verified=" + verified +
				'}';
	}
}