package com.breadgangtvnetwork.bean_model_v1_0.listAll.likeList;

public class ItemsItem{
	private long lastUpdated;
	private long dateCreated;
	private int assetId;
	private String id;
	private int userId;
	private String assetType;

	public void setLastUpdated(long lastUpdated){
		this.lastUpdated = lastUpdated;
	}

	public long getLastUpdated(){
		return lastUpdated;
	}

	public void setDateCreated(long dateCreated){
		this.dateCreated = dateCreated;
	}

	public long getDateCreated(){
		return dateCreated;
	}

	public void setAssetId(int assetId){
		this.assetId = assetId;
	}

	public int getAssetId(){
		return assetId;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setAssetType(String assetType){
		this.assetType = assetType;
	}

	public String getAssetType(){
		return assetType;
	}

	@Override
 	public String toString(){
		return 
			"ItemsItem{" + 
			"lastUpdated = '" + lastUpdated + '\'' + 
			",dateCreated = '" + dateCreated + '\'' + 
			",assetId = '" + assetId + '\'' + 
			",id = '" + id + '\'' + 
			",userId = '" + userId + '\'' + 
			",assetType = '" + assetType + '\'' + 
			"}";
		}
}
