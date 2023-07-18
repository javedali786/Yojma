package com.tv.beanModel.seriesVideoList;

import com.tv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;

import java.util.List;

public class Data {
    private Object seasonName;
    private List<EnveuVideoItemBean> videos;
    private String seasonNumber;

    public Object getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(Object seasonName) {
        this.seasonName = seasonName;
    }

    public List<EnveuVideoItemBean> getVideos() {
        return videos;
    }

    public void setVideos(List<EnveuVideoItemBean> videos) {
        this.videos = videos;
    }

    public String getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(String seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    @Override
    public String toString() {
        return
                "Data{" +
                        "seasonName = '" + seasonName + '\'' +
                        ",videos = '" + videos + '\'' +
                        ",seasonNumber = '" + seasonNumber + '\'' +
                        "}";
    }
}