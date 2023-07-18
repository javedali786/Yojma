
package com.tv.beanModel.responseModels.listAllAccounts;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class AllSecondaryAccountDetails {

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("debugMessage")
    @Expose
    private Object debugMessage;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
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

    @NonNull
    @Override
    public String toString() {
        return "AllSecondaryAccountDetails{" +
                "data=" + data +
                ", responseCode=" + responseCode +
                ", debugMessage=" + debugMessage +
                '}';
    }
}
