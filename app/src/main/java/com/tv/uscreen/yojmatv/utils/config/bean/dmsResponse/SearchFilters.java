package com.tv.uscreen.yojmatv.utils.config.bean.dmsResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchFilters{

	@SerializedName("genres")
	private List<GenresItem> genres;

	public List<GenresItem> getGenres(){
		return genres;
	}
}