
package com.tv.uscreen.bean_model_v1_0.videoDetailBean;


import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class DetailParentalCustomData implements Serializable {

    @Override
    public String toString() {
        return "DetailParentalCustomData{" +
                "display_tags='" + display_tags + '\'' +
                '}';
    }

    @SerializedName("display_tags")
    @Expose
    private String display_tags;

    public String getDisplay_tags() {
        return display_tags;
    }

    public void setDisplay_tags(String display_tags) {
        this.display_tags = display_tags;
    }

    public final static  Parcelable.Creator<DetailParentalCustomData> CREATOR = new Parcelable.Creator<DetailParentalCustomData>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DetailParentalCustomData createFromParcel(android.os.Parcel in) {
            return new DetailParentalCustomData(in);
        }

        public DetailParentalCustomData[] newArray(int size) {
            return (new DetailParentalCustomData[size]);
        }
    }
    ;

    protected DetailParentalCustomData(android.os.Parcel in) {
        this.display_tags = ((String) in.readValue((String.class.getClassLoader())));
    }

    public DetailParentalCustomData() {
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(display_tags);

    }

    public int describeContents() {
        return  0;
    }

}
