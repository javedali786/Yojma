
package com.tv.uscreen.bean_model_v1_0.videoDetailBean;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tv.uscreen.beanModelV3.playListModelV2.CustomContentData;

import java.util.List;


public class Data implements Parcelable
{

    @SerializedName("dateCreated")
    @Expose
    private Long dateCreated;
    @SerializedName("lastUpdated")
    @Expose
    private Long lastUpdated;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("contentType")
    @Expose
    private String contentType;
    @SerializedName("keywords")
    @Expose
    private List<Object> keywords = null;
    @SerializedName("premium")
    @Expose
    private Boolean premium;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("publishedDate")
    @Expose
    private Long publishedDate;
    @SerializedName("customData")
    @Expose
    private Object customData;
    @SerializedName("parentalRating")
    @Expose
    private Object parentalRating;
    @SerializedName("parentContent")
    @Expose
    private ParentContent parentContent;
    @SerializedName("video")
    @Expose
    private DetailVideo video;
    @SerializedName("externalRefId")
    @Expose
    private String externalRefId;
    @SerializedName("contentSource")
    @Expose
    private String contentSource;

    @SerializedName("customContent")
    @Expose
    private CustomContentData customContent;



    public CustomContentData getCustomContent() {
        return customContent;
    }

    public void setCustomContent(CustomContentData customContent) {
        this.customContent = customContent;
    }

    public final static Creator<Data> CREATOR = new Creator<Data>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Data createFromParcel(android.os.Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return (new Data[size]);
        }

    }
    ;

    protected Data(android.os.Parcel in) {
        this.dateCreated = ((Long) in.readValue((Long.class.getClassLoader())));
        this.lastUpdated = ((Long) in.readValue((Long.class.getClassLoader())));
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.contentType = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.keywords, (Object.class.getClassLoader()));
        this.premium = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.sku = ((String) in.readValue((String.class.getClassLoader())));
        this.publishedDate = ((Long) in.readValue((Long.class.getClassLoader())));
        this.customData = in.readValue((CustomData.class.getClassLoader()));
        this.parentalRating = in.readValue((Object.class.getClassLoader()));
        this.parentContent = ((ParentContent) in.readValue((ParentContent.class.getClassLoader())));
        this.video = ((DetailVideo) in.readValue((DetailVideo.class.getClassLoader())));
        this.externalRefId = ((String) in.readValue((String.class.getClassLoader())));
        this.contentSource = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Data() {
    }

    public Long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public List<Object> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<Object> keywords) {
        this.keywords = keywords;
    }

    public Boolean getPremium() {
        return premium;
    }

    public void setPremium(Boolean premium) {
        this.premium = premium;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Long getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Long publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Object getCustomData() {
        return customData;
    }

    public void setCustomData(Object customData) {
        this.customData = customData;
    }

    public Object getParentalRating() {
        return parentalRating;
    }

    public void setParentalRating(Object parentalRating) {
        this.parentalRating = parentalRating;
    }

    public ParentContent getParentContent() {
        return parentContent;
    }

    public void setParentContent(ParentContent parentContent) {
        this.parentContent = parentContent;
    }

    public DetailVideo getVideo() {
        return video;
    }

    public void setVideo(DetailVideo video) {
        this.video = video;
    }

    public String getExternalRefId() {
        return externalRefId;
    }

    public void setExternalRefId(String externalRefId) {
        this.externalRefId = externalRefId;
    }

    public String getContentSource() {
        return contentSource;
    }

    public void setContentSource(String contentSource) {
        this.contentSource = contentSource;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(dateCreated);
        dest.writeValue(lastUpdated);
        dest.writeValue(id);
        dest.writeValue(title);
        dest.writeValue(description);
        dest.writeValue(contentType);
        dest.writeList(keywords);
        dest.writeValue(premium);
        dest.writeValue(sku);
        dest.writeValue(publishedDate);
        dest.writeValue(customData);
        dest.writeValue(parentalRating);
        dest.writeValue(parentContent);
        dest.writeValue(video);
        dest.writeValue(externalRefId);
        dest.writeValue(contentSource);
    }

    public int describeContents() {
        return  0;
    }

}
