
package com.breadgangtvnetwork.bean_model_v1_0.listAll;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class ParentalImage implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("imageKey")
    @Expose
    private String imageKey;
    @SerializedName("src")
    @Expose
    private String src;
    @SerializedName("height")
    @Expose
    private Double height;
    @SerializedName("width")
    @Expose
    private Double width;
    @SerializedName("colorPalette")
    @Expose
    private List<String> colorPalette = null;
    @SerializedName("dominantColor")
    @Expose
    private String dominantColor;
    @SerializedName("tag")
    @Expose
    private Object tag;
    @SerializedName("isDefault")
    @Expose
    private Boolean isDefault;
    public final static Parcelable.Creator<ParentalImage> CREATOR = new Parcelable.Creator<ParentalImage>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ParentalImage createFromParcel(android.os.Parcel in) {
            return new ParentalImage(in);
        }

        public ParentalImage[] newArray(int size) {
            return (new ParentalImage[size]);
        }

    }
    ;

    protected ParentalImage(android.os.Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.imageKey = ((String) in.readValue((String.class.getClassLoader())));
        this.src = ((String) in.readValue((String.class.getClassLoader())));
        this.height = ((Double) in.readValue((Double.class.getClassLoader())));
        this.width = ((Double) in.readValue((Double.class.getClassLoader())));
        in.readList(this.colorPalette, (String.class.getClassLoader()));
        this.dominantColor = ((String) in.readValue((String.class.getClassLoader())));
        this.tag = in.readValue((Object.class.getClassLoader()));
        this.isDefault = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
    }

    public ParentalImage() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public List<String> getColorPalette() {
        return colorPalette;
    }

    public void setColorPalette(List<String> colorPalette) {
        this.colorPalette = colorPalette;
    }

    public String getDominantColor() {
        return dominantColor;
    }

    public void setDominantColor(String dominantColor) {
        this.dominantColor = dominantColor;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(imageKey);
        dest.writeValue(src);
        dest.writeValue(height);
        dest.writeValue(width);
        dest.writeList(colorPalette);
        dest.writeValue(dominantColor);
        dest.writeValue(tag);
        dest.writeValue(isDefault);
    }

    public int describeContents() {
        return  0;
    }

}
