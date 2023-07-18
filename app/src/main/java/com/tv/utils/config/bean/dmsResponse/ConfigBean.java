package com.tv.utils.config.bean.dmsResponse;

import com.google.gson.annotations.SerializedName;

public class ConfigBean {

	@SerializedName("data")
	private Data data;

	@SerializedName("debugMessage")
	private Object debugMessage;

	@SerializedName("responseCode")
	private int responseCode;

	public Data getData(){
		return data;
	}

	public Object getDebugMessage(){
		return debugMessage;
	}

	public int getResponseCode(){
		return responseCode;
	}
}