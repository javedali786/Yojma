package com.tv.uscreen.yojmatv.utils.config.bean.dmsResponse;

import com.google.gson.annotations.SerializedName;

public class MediaTypes{

	@SerializedName("trailer")
	private String trailer;

	@SerializedName("movie")
	private String movie;

	@SerializedName("series")
	private String series;

	@SerializedName("show")
	private String show;

	@SerializedName("episode")
	private String episode;

	@SerializedName("live")
	private String live;

	@SerializedName("news")
	private String news;

	@SerializedName("contest")
	private String contest;

	@SerializedName("expedition")
	private String expedition;

	@SerializedName("event")
	private String event;

	@SerializedName("offer")
	private String offer;

	@SerializedName("agency")
	private String agency;

	@SerializedName("image")
	private String image;

	@SerializedName("marketPlace")
	private String marketPlace;

	@SerializedName("podcast")
	private String podcast;

	@SerializedName("gaming")
	private String gaming;

	@SerializedName("reel")
	private String reel;

	@SerializedName("documentaries")
	private String documentaries;


	public String getPodcast() {
		return podcast;
	}

	public String getGaming() {
		return gaming;
	}

	public String getReel() {
		return reel;
	}

	public String getDocumentaries() {
		return documentaries;
	}

	public String getTrailer(){
		return trailer;
	}

	public String getMovie(){
		return movie;
	}

	public String getSeries(){
		return series;
	}

	public String getShow(){
		return show;
	}

	public String getEpisode(){
		return episode;
	}

	public String getLive(){
		return live;
	}

	public String getNews() {
		return news;
	}

	public String getContest() {
		return contest;
	}

	public String getExpedition() {
		return expedition;
	}

	public String getEvent() {
		return event;
	}

	public String getOffer() {
		return offer;
	}

	public String getMarketPlace() {
		return marketPlace;
	}

	public String getAgency() {
		return agency;
	}

	public String getImage() {
		return image;
	}
}