package com.tv.utils.config.bean.dmsResponse;

import com.google.gson.annotations.SerializedName;

public class DisplayName{

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