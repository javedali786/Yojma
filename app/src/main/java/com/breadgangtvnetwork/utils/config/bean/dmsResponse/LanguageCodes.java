package com.breadgangtvnetwork.utils.config.bean.dmsResponse;

import com.google.gson.annotations.SerializedName;

public class LanguageCodes{

	@SerializedName("spanish")
	private String spanish;

	@SerializedName("english")
	private String english;

	public String getSpanish(){
		return spanish;
	}

	public String getEnglish(){
		return english;
	}
}