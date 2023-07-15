package com.breadgangtvnetwork.utils;

import com.breadgangtvnetwork.utils.config.bean.dmsResponse.ConfigBean;

public class MediaTypeConstants {
    private static MediaTypeConstants mediaTypeConstantsInstance;
    String Movie = "MOVIE";
    String Show = "SHOW";
    String Season = "SEASON";
    String Episode = "EPISODE";
    String Series = "SERIES";
    String Video = "VIDEO";
    String Live = "LIVE";
    String Article = "ARTICLE";
    String Podcast = "PODCAST";
    String Gaming = "GAMING";
    String Reel = "REEL";
    String Documentaries = "DOCUMENTARIES";
    ConfigBean configBean;
    public static String VIDEO="VIDEO";

 /*   public String getMovie() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getMediaTypes().getMovie();
    }*/

    public String getMovie() {
        return configBean == null ? "" : Movie;
    }

    public String getShow() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getMediaTypes().getShow();
    }

    public String getEpisode() {
       return configBean == null ? "" : configBean.getData().getAppConfig().getMediaTypes().getEpisode();
    }

    public String getSeries() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getMediaTypes().getSeries();
    }

    public String getLive() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getMediaTypes().getLive();
    }

    public String getNews() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getMediaTypes().getNews();
    }

    public String getContest() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getMediaTypes().getContest();
    }

    public String getExpedition() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getMediaTypes().getExpedition();
    }
    public String getEvent() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getMediaTypes().getEvent();
    }

    public String getOffer() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getMediaTypes().getOffer();
    }

    public String getMarketPlace() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getMediaTypes().getMarketPlace();
    }

    public String getAgency() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getMediaTypes().getAgency();
    }

    public String getImages() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getMediaTypes().getImage();
    }

    public String getTrailer() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getMediaTypes().getTrailer();
    }

    public String getPodcast() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getMediaTypes().getPodcast();
    }

    public String getGaming() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getMediaTypes().getGaming();
    }

    public String getDocumentaries() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getMediaTypes().getDocumentaries();
    }

    public String getReel() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getMediaTypes().getReel();
    }

    public static String getVIDEO() {
        return VIDEO;
    }

    public static MediaTypeConstants getInstance() {
        if (mediaTypeConstantsInstance == null) {
            mediaTypeConstantsInstance = new MediaTypeConstants();
        }
        return (mediaTypeConstantsInstance);
    }

    public void setConfigObject(ConfigBean configBean) {
        this.configBean=configBean;
    }
}
