package com.breadgangtvnetwork.utils.config.bean.dmsResponse;

import com.google.gson.annotations.SerializedName;

public class TypesItem{

	@SerializedName("en-US")
	private String enUS;

	@SerializedName("es-ES")
	private String esES;

	@SerializedName("id")
	private String id;

	@SerializedName("url")
	private String url;

	public String getEnUS(){
		return enUS;
	}

	public String getEsES(){
		return esES;
	}

	public String getId(){
		return id;
	}

	public String getUrl(){
		return url;
	}
}