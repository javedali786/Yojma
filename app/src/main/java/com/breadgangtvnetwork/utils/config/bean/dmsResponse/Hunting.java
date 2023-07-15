package com.breadgangtvnetwork.utils.config.bean.dmsResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Hunting{

	@SerializedName("types")
	private List<TypesItem> types;

	@SerializedName("species")
	private List<SpeciesItem> species;

	@SerializedName("profile")
	private Profile profile;

	@SerializedName("id")
	private String id;

	public List<TypesItem> getTypes(){
		return types;
	}

	public List<SpeciesItem> getSpecies(){
		return species;
	}

	public Profile getProfile(){
		return profile;
	}

	public String getId(){
		return id;
	}
}