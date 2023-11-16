package com.tv.uscreen.yojmatv.bean_model_v1_0.listAll;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AudioTrackListItem implements Serializable {

	@SerializedName("langCode")
	private String langCode;

	@SerializedName("encodingRates")
	private List<Object> encodingRates;

	@SerializedName("errorMessage")
	private Object errorMessage;

	@SerializedName("language")
	private String language;

	@SerializedName("mimeType")
	private String mimeType;

	@SerializedName("type")
	private String type;

	@SerializedName("duration")
	private Object duration;

	@SerializedName("originalSizeInBytes")
	private String originalSizeInBytes;

	@SerializedName("default")
	private boolean jsonMemberDefault;

	@SerializedName("variant")
	private Object variant;

	@SerializedName("name")
	private String name;

	@SerializedName("externalIdentifier")
	private String externalIdentifier;

	@SerializedName("id")
	private int id;

	@SerializedName("status")
	private String status;

	public String getLangCode(){
		return langCode;
	}

	public List<Object> getEncodingRates(){
		return encodingRates;
	}

	public Object getErrorMessage(){
		return errorMessage;
	}

	public String getLanguage(){
		return language;
	}

	public String getMimeType(){
		return mimeType;
	}

	public String getType(){
		return type;
	}

	public Object getDuration(){
		return duration;
	}

	public String getOriginalSizeInBytes(){
		return originalSizeInBytes;
	}

	public boolean isJsonMemberDefault(){
		return jsonMemberDefault;
	}

	public Object getVariant(){
		return variant;
	}

	public String getName(){
		return name;
	}

	public String getExternalIdentifier(){
		return externalIdentifier;
	}

	public int getId(){
		return id;
	}

	public String getStatus(){
		return status;
	}

	public static final Parcelable.Creator<AudioTrackListItem> CREATOR = new Parcelable.Creator<AudioTrackListItem>() {
		@Override
		public AudioTrackListItem createFromParcel(Parcel in) {
			return new AudioTrackListItem(in);
		}

		@Override
		public AudioTrackListItem[] newArray(int size) {
			return new AudioTrackListItem[size];
		}
	};

	private AudioTrackListItem(Parcel in) {
		langCode = in.readString();
		this.errorMessage =  in.readValue((Object.class.getClassLoader()));
		this.errorMessage =  in.readValue((Object.class.getClassLoader()));

	}
}