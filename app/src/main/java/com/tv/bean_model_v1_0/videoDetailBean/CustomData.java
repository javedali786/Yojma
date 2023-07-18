
package com.tv.bean_model_v1_0.videoDetailBean;


import android.os.Parcelable;


public class CustomData implements Parcelable
{

    public final static Creator<CustomData> CREATOR = new Creator<CustomData>() {


        @SuppressWarnings({
            "unchecked"
        })
        public CustomData createFromParcel(android.os.Parcel in) {
            return new CustomData(in);
        }

        public CustomData[] newArray(int size) {
            return (new CustomData[size]);
        }

    }
    ;

    protected CustomData(android.os.Parcel in) {
    }

    public CustomData() {
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
    }

    public int describeContents() {
        return  0;
    }

}
