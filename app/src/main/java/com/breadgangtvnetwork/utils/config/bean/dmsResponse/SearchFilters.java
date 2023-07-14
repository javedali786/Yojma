package com.breadgangtvnetwork.utils.config.bean.dmsResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchFilters{

	@SerializedName("genres")
	private List<GenresItem> genres;

	public List<GenresItem> getGenres(){
		return genres;
	}
}