package com.tv.uscreen.yojmatv.utils.config.bean.dmsResponse;

import com.google.gson.annotations.SerializedName;

public class SearchKeys{

	@SerializedName("en-US")
	private String enUS;

	@SerializedName("es-ES")
	private String esES;

	public String getEnUS(){
		return enUS;
	}

	public String getEsES(){
		return esES;
	}
}