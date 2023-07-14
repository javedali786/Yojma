package com.breadgangtvnetwork.utils.config.bean.dmsResponse;

import com.google.gson.annotations.SerializedName;

public class ContentPreferences{

	@SerializedName("fishing")
	private Fishing fishing;

	@SerializedName("hunting")
	private Hunting hunting;

	@SerializedName("both")
	private Both both;

	public Fishing getFishing(){
		return fishing;
	}

	public Hunting getHunting(){
		return hunting;
	}

	public Both getBoth(){
		return both;
	}
}