
package com.breadgangtvnetwork.bean_model_v1_0.videoDetailBean;


import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class EnvVideoDetailsBean implements Parcelable
{

    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("debugMessage")
    @Expose
    private Object debugMessage;
    public final static Creator<EnvVideoDetailsBean> CREATOR = new Creator<EnvVideoDetailsBean>() {


        @SuppressWarnings({
            "unchecked"
        })
        public EnvVideoDetailsBean createFromParcel(android.os.Parcel in) {
            return new EnvVideoDetailsBean(in);
        }

        public EnvVideoDetailsBean[] newArray(int size) {
            return (new EnvVideoDetailsBean[size]);
        }

    }
    ;

    protected EnvVideoDetailsBean(android.os.Parcel in) {
        this.data = ((Data) in.readValue((Data.class.getClassLoader())));
        this.responseCode = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.debugMessage = in.readValue((Object.class.getClassLoader()));
    }

    public EnvVideoDetailsBean() {
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
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
