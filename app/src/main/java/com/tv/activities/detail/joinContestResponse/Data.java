package com.tv.activities.detail.joinContestResponse;

public class Data{
	private String lastUpdated;
	private String customType;
	private String dateCreated;
	private int contentId;
	private String participationStatus;
	private String id;
	private int userId;

	public void setLastUpdated(String lastUpdated){
		this.lastUpdated = lastUpdated;
	}

	public String getLastUpdated(){
		return lastUpdated;
	}

	public void setCustomType(String customType){
		this.customType = customType;
	}

	public String getCustomType(){
		return customType;
	}

	public void setDateCreated(String dateCreated){
		this.dateCreated = dateCreated;
	}

	public String getDateCreated(){
		return dateCreated;
	}

	public void setContentId(int contentId){
		this.contentId = contentId;
	}

	public int getContentId(){
		return contentId;
	}

	public void setParticipationStatus(String participationStatus){
		this.participationStatus = participationStatus;
	}

	public String getParticipationStatus(){
		return participationStatus;
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

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"lastUpdated = '" + lastUpdated + '\'' + 
			",customType = '" + customType + '\'' + 
			",dateCreated = '" + dateCreated + '\'' + 
			",contentId = '" + contentId + '\'' + 
			",participationStatus = '" + participationStatus + '\'' + 
			",id = '" + id + '\'' + 
			",userId = '" + userId + '\'' + 
			"}";
		}
}
