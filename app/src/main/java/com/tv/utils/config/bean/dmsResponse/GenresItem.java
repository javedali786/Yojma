package com.tv.utils.config.bean.dmsResponse;

import com.google.gson.annotations.SerializedName;

public class GenresItem{

	@SerializedName("displayName")
	private DisplayName displayName;

	@SerializedName("searchKeys")
	private SearchKeys searchKeys;

	public DisplayName getDisplayName(){
		return displayName;
	}

	public SearchKeys getSearchKeys(){
		return searchKeys;
	}
}