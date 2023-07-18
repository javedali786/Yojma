
package com.tv.bean_model_v1_0.listAll;


import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VideoCustomData implements Serializable {
    @SerializedName("display_tags")
    @Expose
    private String display_tags;

    public String getDisplay_tags() {
        return display_tags;
    }

    public void setDisplay_tags(String display_tags) {
        this.display_tags = display_tags;
    }

    @Override
    public String toString() {
        return "VideoCustomData{" +
                "display_tags='" + display_tags + '\'' +
                '}';
    }

    public final static Parcelable.Creator<VideoCustomData> CREATOR = new Parcelable.Creator<VideoCustomData>() {


        @SuppressWarnings({
            "unchecked"
        })
        public VideoCustomData createFromParcel(android.os.Parcel in) {

            return new VideoCustomData(in);
        }

        public VideoCustomData[] newArray(int size) {
            return (new VideoCustomData[size]);
        }

    }
    ;

    protected VideoCustomData(android.os.Parcel in) {
        this.display_tags = ((String) in.readValue((String.class.getClassLoader())));

    }

    public VideoCustomData() {
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(display_tags);
    }

    public int describeContents() {
        return  0;
    }

}
