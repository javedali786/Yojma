
package com.tv.uscreen.yojmatv.bean_model_v1_0.listAll;


import android.os.Parcelable;

public class ParentalCustomData implements Parcelable
{

    public final static Creator<ParentalCustomData> CREATOR = new Creator<ParentalCustomData>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ParentalCustomData createFromParcel(android.os.Parcel in) {
            return new ParentalCustomData(in);
        }

        public ParentalCustomData[] newArray(int size) {
            return (new ParentalCustomData[size]);
        }

    }
    ;

    protected ParentalCustomData(android.os.Parcel in) {
    }

    public ParentalCustomData() {
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
    }

    public int describeContents() {
        return  0;
    }

}
