
package com.breadgangtvnetwork.bean_model_v1_0.listAll;


import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ListAllContent implements Parcelable
{

    @SerializedName("data")
    @Expose
    private ListAllData data;
    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("debugMessage")
    @Expose
    private Object debugMessage;
    public final static Creator<ListAllContent> CREATOR = new Creator<ListAllContent>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ListAllContent createFromParcel(android.os.Parcel in) {
            return new ListAllContent(in);
        }

        public ListAllContent[] newArray(int size) {
            return (new ListAllContent[size]);
        }

    }
    ;

    protected ListAllContent(android.os.Parcel in) {
        this.data = ((ListAllData) in.readValue((ListAllData.class.getClassLoader())));
        this.responseCode = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.debugMessage = in.readValue((Object.class.getClassLoader()));
    }

    public ListAllContent() {
    }

    public ListAllData getData() {
        return data;
    }

    public void setData(ListAllData data) {
        this.data = data;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public Object getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(Object debugMessage) {
        this.debugMessage = debugMessage;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(data);
        dest.writeValue(responseCode);
        dest.writeValue(debugMessage);
    }

    public int describeContents() {
        return  0;
    }

}
