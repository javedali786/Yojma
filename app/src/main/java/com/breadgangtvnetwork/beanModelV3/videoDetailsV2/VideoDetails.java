package com.breadgangtvnetwork.beanModelV3.videoDetailsV2;

import android.os.Parcelable;

import com.breadgangtvnetwork.beanModelV3.playListModelV2.NewImages;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class VideoDetails implements Serializable
{

    @SerializedName("cuePoints")
    @Expose
    private Object cuePoints;
    @SerializedName("seasonNo")
    @Expose
    private Object seasonNo;
    @SerializedName("episodeNo")
    @Expose
    private Object episodeNo;
    @SerializedName("chapterNo")
    @Expose
    private Object chapterNo;
    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("videoType")
    @Expose
    private String videoType;
    @SerializedName("seasons")
    @Expose
    private List<Object> seasons = null;
    @SerializedName("offlineEnabled")
    @Expose
    private Object offlineEnabled;
    @SerializedName("drmDisabled")
    @Expose
    private Object drmDisabled;
    @SerializedName("textTracks")
    @Expose
    private Object textTracks;
    @SerializedName("images")
    @Expose
    private List<NewImages> images = null;
    public final static Parcelable.Creator<VideoDetails> CREATOR = new Parcelable.Creator<VideoDetails>() {



        public VideoDetails createFromParcel(android.os.Parcel in) {
            return new VideoDetails(in);
        }

        public VideoDetails[] newArray(int size) {
            return (new VideoDetails[size]);
        }

    };

    protected VideoDetails(android.os.Parcel in) {
        this.cuePoints = in.readValue((Object.class.getClassLoader()));
        this.seasonNo = in.readValue((Object.class.getClassLoader()));
        this.episodeNo = in.readValue((Object.class.getClassLoader()));
        this.chapterNo = in.readValue((Object.class.getClassLoader()));
        this.duration = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.videoType = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.seasons, (Object.class.getClassLoader()));
        this.offlineEnabled = in.readValue((Object.class.getClassLoader()));
        this.drmDisabled = in.readValue((Object.class.getClassLoader()));
        this.textTracks = in.readValue((Object.class.getClassLoader()));
        in.readList(this.images, (NewImages.class.getClassLoader()));
    }

    public VideoDetails() {
    }

    public Object getCuePoints() {
        return cuePoints;
    }

    public void setCuePoints(Object cuePoints) {
        this.cuePoints = cuePoints;
    }

    public Object getSeasonNo() {
        return seasonNo;
    }

    public void setSeasonNo(Object seasonNo) {
        this.seasonNo = seasonNo;
    }

    public Object getEpisodeNo() {
        return episodeNo;
    }

    public void setEpisodeNo(Object episodeNo) {
        this.episodeNo = episodeNo;
    }

    public Object getChapterNo() {
        return chapterNo;
    }

    public void setChapterNo(Object chapterNo) {
        this.chapterNo = chapterNo;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public List<Object> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Object> seasons) {
        this.seasons = seasons;
    }

    public Object getOfflineEnabled() {
        return offlineEnabled;
    }

    public void setOfflineEnabled(Object offlineEnabled) {
        this.offlineEnabled = offlineEnabled;
    }

    public Object getDrmDisabled() {
        return drmDisabled;
    }

    public void setDrmDisabled(Object drmDisabled) {
        this.drmDisabled = drmDisabled;
    }

    public Object getTextTracks() {
        return textTracks;
    }

    public void setTextTracks(Object textTracks) {
        this.textTracks = textTracks;
    }

    public List<NewImages> getImages() {
        return images;
    }

    public void setImages(List<NewImages> images) {
        this.images = images;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(cuePoints);
        dest.writeValue(seasonNo);
        dest.writeValue(episodeNo);
        dest.writeValue(chapterNo);
        dest.writeValue(duration);
        dest.writeValue(videoType);
        dest.writeList(seasons);
        dest.writeValue(offlineEnabled);
        dest.writeValue(drmDisabled);
        dest.writeValue(textTracks);
        dest.writeList(images);
    }

    public int describeContents() {
        return  0;
    }

}


