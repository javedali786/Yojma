package com.breadgangtvnetwork.beanModel.responseModels.landingTabResponses;

import com.breadgangtvnetwork.beanModel.ContinueRailModel.CommonContinueRail;
import com.breadgangtvnetwork.beanModel.responseModels.landingTabResponses.railData.PlaylistRailData;
import com.breadgangtvnetwork.beanModel.responseModels.series.season.SeasonResponse;
import com.breadgangtvnetwork.utils.helpers.carousel.model.Slide;

import java.util.ArrayList;

public class CommonRailData {

    private int progressType;
    private int type;
    private String contentType;
    private SeasonResponse seasonResponse;
    private PlaylistRailData railData;
    private ArrayList<Slide> slides;
    private ArrayList<CommonContinueRail> continueRailAdapter;

    public ArrayList<CommonContinueRail> getContinueRailAdapter() {
        return continueRailAdapter;
    }

    public void setContinueRailAdapter(ArrayList<CommonContinueRail> continueRailAdapter) {
        this.continueRailAdapter = continueRailAdapter;
    }

    public int getProgressType() {
        return progressType;
    }

    public void setProgressType(int progressType) {
        this.progressType = progressType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public SeasonResponse getSeasonResponse() {
        return seasonResponse;
    }

    public void setSeasonResponse(SeasonResponse seasonResponse) {
        this.seasonResponse = seasonResponse;
    }

    public PlaylistRailData getRailData() {
        return railData;
    }

    public void setRailData(PlaylistRailData railData) {
        this.railData = railData;
    }

    public ArrayList<Slide> getSlides() {
        return slides;
    }

    public void setSlides(ArrayList<Slide> slides) {
        this.slides = slides;
    }
}
