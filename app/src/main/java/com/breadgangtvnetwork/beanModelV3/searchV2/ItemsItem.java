
package com.breadgangtvnetwork.beanModelV3.searchV2;

import com.breadgangtvnetwork.beanModelV3.playListModelV2.CustomContentData;
import com.breadgangtvnetwork.beanModelV3.playListModelV2.Images;
import com.breadgangtvnetwork.beanModelV3.videoDetailsV2.VideoDetails;
import com.breadgangtvnetwork.bean_model_v1_0.videoDetailBean.ParentContent;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ItemsItem {

	@SerializedName("dateCreated")
	@Expose
	private long dateCreated;
	@SerializedName("lastUpdated")
	@Expose
	private long lastUpdated;
	@SerializedName("contentSource")
	@Expose
	private String contentSource;
	@SerializedName("id")
	@Expose
	private Integer id;
	@SerializedName("adKeys")
	@Expose
	private Object adKeys;
	@SerializedName("cuePoints")
	@Expose
	private Object cuePoints;
	@SerializedName("customFields")
	@Expose
	private Object customFields;
	@SerializedName("deliveryType")
	@Expose
	private String deliveryType;
	@SerializedName("description")
	@Expose
	private String description;
	@SerializedName("duration")
	@Expose
	private Integer duration;
	@SerializedName("economics")
	@Expose
	private String economics;
	@SerializedName("brightcoveContentId")
	@Expose
	private String brightcoveContentId;
	@SerializedName("images")
	@Expose
	private Images images;
	@SerializedName("longDescription")
	@Expose
	private Object longDescription;
	@SerializedName("title")
	@Expose
	private String title;
	@SerializedName("status")
	@Expose
	private String status;
	@SerializedName("keywords")
	@Expose
	private List<Object> keywords = null;
	@SerializedName("textTracks")
	@Expose
	private List<Object> textTracks = null;
	@SerializedName("audioTracks")
	@Expose
	private Object audioTracks;
	@SerializedName("contentType")
	@Expose
	private String contentType;
	@SerializedName("genres")
	@Expose
	private List<String> genres = null;
	@SerializedName("cast")
	@Expose
	private List<String> cast = null;
	@SerializedName("sku")
	@Expose
	private String sku;
	@SerializedName("seasonNumber")
	@Expose
	private Integer seasonNumber;
	@SerializedName("episodeNumber")
	@Expose
	private Integer episodeNumber;
	@SerializedName("publishedDate")
	@Expose
	private long publishedDate;
	@SerializedName("seasons")
	@Expose
	private ArrayList<Integer> seasons;
	@SerializedName("contentMonetization")
	@Expose
	private Object contentMonetization;
	@SerializedName("linkedContent")
	@Expose
	private Object linkedContent;
	@SerializedName("contentProvider")
	@Expose
	private String contentProvider;
	@SerializedName("variant")
	@Expose
	private String variant;
	@SerializedName("complete")
	@Expose
	private Object complete;
	@SerializedName("drmdisabled")
	@Expose
	private Boolean drmdisabled;
	@SerializedName("sVariants")
	@Expose
	private Object sVariants;
	@SerializedName("premium")
	@Expose
	private Boolean premium;
	@SerializedName("offlineEnabled")
	@Expose
	private Boolean offlineEnabled;
	@SerializedName("video")
	@Expose
	private VideoDetails video;

	@SerializedName("customContent")
	@Expose
	private CustomContentData customContent;

	@SerializedName("externalRefId")
	@Expose
	private String externalRefId;

	@SerializedName("customData")
	@Expose
	private Object customData;

	@SerializedName("parentContent")
	@Expose
	private ParentContent parentContent;

	public ParentContent getParentContent() {
		return parentContent;
	}

	public void setParentContent(ParentContent parentContent) {
		this.parentContent = parentContent;
	}

	public Object getCustomData() {
		return customData;
	}

	public void setCustomData(Object customData) {
		this.customData = customData;
	}

	public String getExternalRefId() {
		return externalRefId;
	}

	public void setExternalRefId(String externalRefId) {
		this.externalRefId = externalRefId;
	}

	public CustomContentData getCustomContent() {
		return customContent;
	}

	public void setCustomContent(CustomContentData customContent) {
		this.customContent = customContent;
	}


	public VideoDetails getVideo() {
		return video;
	}

	public void setVideo(VideoDetails video) {
		this.video = video;
	}

	public long getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(long dateCreated) {
		this.dateCreated = dateCreated;
	}

	public long getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(long lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getContentSource() {
		return contentSource;
	}

	public void setContentSource(String contentSource) {
		this.contentSource = contentSource;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Object getAdKeys() {
		return adKeys;
	}

	public void setAdKeys(Object adKeys) {
		this.adKeys = adKeys;
	}

	public Object getCuePoints() {
		return cuePoints;
	}

	public void setCuePoints(Object cuePoints) {
		this.cuePoints = cuePoints;
	}

	public Object getCustomFields() {
		return customFields;
	}

	public void setCustomFields(Object customFields) {
		this.customFields = customFields;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getEconomics() {
		return economics;
	}

	public void setEconomics(String economics) {
		this.economics = economics;
	}

	public String getBrightcoveContentId() {
		return brightcoveContentId;
	}

	public void setBrightcoveContentId(String brightcoveContentId) {
		this.brightcoveContentId = brightcoveContentId;
	}

	public Images getImages() {
		return images;
	}

	public void setImages(Images images) {
		this.images = images;
	}

	public Object getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(Object longDescription) {
		this.longDescription = longDescription;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Object> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<Object> keywords) {
		this.keywords = keywords;
	}

	public List<Object> getTextTracks() {
		return textTracks;
	}

	public void setTextTracks(List<Object> textTracks) {
		this.textTracks = textTracks;
	}

	public Object getAudioTracks() {
		return audioTracks;
	}

	public void setAudioTracks(Object audioTracks) {
		this.audioTracks = audioTracks;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public List<String> getGenres() {
		return genres;
	}

	public void setGenres(List<String> genres) {
		this.genres = genres;
	}

	public List<String> getCast() {
		return cast;
	}

	public void setCast(List<String> cast) {
		this.cast = cast;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Integer getSeasonNumber() {
		return seasonNumber;
	}

	public void setSeasonNumber(Integer seasonNumber) {
		this.seasonNumber = seasonNumber;
	}

	public Integer getEpisodeNumber() {
		return episodeNumber;
	}

	public void setEpisodeNumber(Integer episodeNumber) {
		this.episodeNumber = episodeNumber;
	}

	public long getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(long publishedDate) {
		this.publishedDate = publishedDate;
	}

	public ArrayList<Integer> getSeasons() {
		return seasons;
	}

	public void setSeasons(ArrayList<Integer> seasons) {
		this.seasons = seasons;
	}

	public Object getContentMonetization() {
		return contentMonetization;
	}

	public void setContentMonetization(Object contentMonetization) {
		this.contentMonetization = contentMonetization;
	}

	public Object getLinkedContent() {
		return linkedContent;
	}

	public void setLinkedContent(Object linkedContent) {
		this.linkedContent = linkedContent;
	}

	public String getContentProvider() {
		return contentProvider;
	}

	public void setContentProvider(String contentProvider) {
		this.contentProvider = contentProvider;
	}

	public String getVariant() {
		return variant;
	}

	public void setVariant(String variant) {
		this.variant = variant;
	}

	public Object getComplete() {
		return complete;
	}

	public void setComplete(Object complete) {
		this.complete = complete;
	}

	public Boolean getDrmdisabled() {
		return drmdisabled;
	}

	public void setDrmdisabled(Boolean drmdisabled) {
		this.drmdisabled = drmdisabled;
	}

	public Object getSVariants() {
		return sVariants;
	}

	public void setSVariants(Object sVariants) {
		this.sVariants = sVariants;
	}

	public Boolean getPremium() {
		return premium;
	}

	public void setPremium(Boolean premium) {
		this.premium = premium;
	}

	public Boolean getOfflineEnabled() {
		return offlineEnabled;
	}

	public void setOfflineEnabled(Boolean offlineEnabled) {
		this.offlineEnabled = offlineEnabled;
	}

}