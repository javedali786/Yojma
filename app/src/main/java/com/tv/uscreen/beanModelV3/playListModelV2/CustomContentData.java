package com.tv.uscreen.beanModelV3.playListModelV2;

import android.os.Parcelable;

import com.enveu.client.playlist.beanv1_0.Image;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CustomContentData implements Serializable {
    @SerializedName("dateCreated")
    @Expose
    private long dateCreated;
    @SerializedName("lastUpdated")
    @Expose
    private long lastUpdated;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("customType")
    @Expose
    private String customType;
    @SerializedName("images")
    @Expose
    private List<Image> images;


    public final static Parcelable.Creator<CustomContentData> CREATOR = new Parcelable.Creator<CustomContentData>() {



        public CustomContentData createFromParcel(android.os.Parcel in) {
            return new CustomContentData(in);
        }

        public CustomContentData[] newArray(int size) {
            return (new CustomContentData[size]);
        }

    };

    protected CustomContentData(android.os.Parcel in) {
        this.customType = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.dateCreated = ((Long) in.readValue((Integer.class.getClassLoader())));
        this.lastUpdated = ((Long) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.images, (NewImages.class.getClassLoader()));

    }


    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomType() {
        return customType;
    }

    public void setCustomType(String customType) {
        this.customType = customType;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(customType);
        dest.writeValue(id);
        dest.writeValue(dateCreated);
        dest.writeValue(lastUpdated);
        dest.writeList(images);
    }

}
