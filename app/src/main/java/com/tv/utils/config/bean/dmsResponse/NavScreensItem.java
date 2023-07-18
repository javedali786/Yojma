package com.tv.utils.config.bean.dmsResponse;

import com.google.gson.annotations.SerializedName;

public class NavScreensItem{

	@SerializedName("id")
	private int id;

	@SerializedName("screenName")
	private String screenName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
}