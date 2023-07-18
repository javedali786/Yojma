package com.tv.utils.config.bean.dmsResponse;

import com.google.gson.annotations.SerializedName;

public class Profile{

	@SerializedName("name-US")
	private String nameUS;

	@SerializedName("name-ES")
	private String nameES;

	@SerializedName("profilePic")
	private String profilePic;

	public String getNameUS(){
		return nameUS;
	}

	public String getNameES(){
		return nameES;
	}

	public String getProfilePic(){
		return profilePic;
	}
}