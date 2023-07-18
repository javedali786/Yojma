
package com.tv.utils.config.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MediaTypes {

    @SerializedName("episode")
    @Expose
    private String episode;
    @SerializedName("movie")
    @Expose
    private String movie;
    @SerializedName("series")
    @Expose
    private String series;

    @SerializedName("live")
    @Expose
    private String live;
    @SerializedName("show")
    @Expose
    private String show;

    public String getLive() {
        return live;
    }

    public void setLive(String live) {
        this.live = live;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

}
