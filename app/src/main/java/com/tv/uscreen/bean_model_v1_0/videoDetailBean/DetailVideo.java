
package com.tv.uscreen.bean_model_v1_0.videoDetailBean;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class DetailVideo implements Serializable
{

    @SerializedName("cuePoints")
    @Expose
    private Object cuePoints;
    @SerializedName("seasonNo")
    @Expose
    private Integer seasonNo;
    @SerializedName("episodeNo")
    @Expose
    private Integer episodeNo;
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
    private List<DetailVideoImage> images = null;
    public final static Parcelable.Creator<DetailVideo> CREATOR = new Parcelable.Creator<DetailVideo>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DetailVideo createFromParcel(android.os.Parcel in) {
            return new DetailVideo(in);
        }

        public DetailVideo[] newArray(int size) {
            return (new DetailVideo[size]);
        }

    }
    ;

    protected DetailVideo(android.os.Parcel in) {
        this.cuePoints = in.readValue((Object.class.getClassLoader()));
        this.seasonNo = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.episodeNo = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.chapterNo = in.readValue((Object.class.getClassLoader()));
        this.duration = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.videoType = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.seasons, (Object.class.getClassLoader()));
        this.offlineEnabled = in.readValue((Object.class.getClassLoader()));
        this.drmDisabled = in.readValue((Object.class.getClassLoader()));
        this.textTracks = in.readValue((Object.class.getClassLoader()));
        in.readList(this.images, (DetailVideoImage.class.getClassLoader()));
    }

    public DetailVideo() {
    }

    public Object getCuePoints() {
        return cuePoints;
    }

    public void setCuePoints(Object cuePoints) {
        this.cuePoints = cuePoints;
    }

    public Integer getSeasonNo() {
        return seasonNo;
    }

    public void setSeasonNo(Integer seasonNo) {
        this.seasonNo = seasonNo;
    }

    public Integer getEpisodeNo() {
        return episodeNo;
    }

    public void setEpisodeNo(Integer episodeNo) {
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

    public List<DetailVideoImage> getImages() {
        return images;
    }

    public void setImages(List<DetailVideoImage> images) {
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
