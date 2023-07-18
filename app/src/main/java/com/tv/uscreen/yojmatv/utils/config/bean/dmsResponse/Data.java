package com.tv.uscreen.yojmatv.utils.config.bean.dmsResponse;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("lastUpdated")
	private long lastUpdated;

	@SerializedName("appConfig")
	private AppConfig appConfig;

	@SerializedName("dateCreated")
	private long dateCreated;

	@SerializedName("isLatest")
	private boolean isLatest;

	@SerializedName("version")
	private int version;

	@SerializedName("status")
	private String status;


	public long getLastUpdated(){
		return lastUpdated;
	}

	public AppConfig getAppConfig(){
		return appConfig;
	}

	public long getDateCreated(){
		return dateCreated;
	}

	public boolean isIsLatest(){
		return isLatest;
	}

	public int getVersion(){
		return version;
	}

	public String getStatus(){
		return status;
	}
}