package com.tv.uscreen.utils.config.bean.dmsResponse;

import com.google.gson.annotations.SerializedName;

public class Version{

	@SerializedName("recommendedUpdate")
	private boolean recommendedUpdate;

	@SerializedName("forceUpdate")
	private boolean forceUpdate;

	@SerializedName("updatedVersion")
	private String updatedVersion;

	public boolean isRecommendedUpdate(){
		return recommendedUpdate;
	}

	public boolean isForceUpdate(){
		return forceUpdate;
	}

	public String getUpdatedVersion(){
		return updatedVersion;
	}
}