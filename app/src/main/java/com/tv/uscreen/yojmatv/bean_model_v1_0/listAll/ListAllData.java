
package com.tv.uscreen.yojmatv.bean_model_v1_0.listAll;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ListAllData implements Parcelable
{

    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("pageSize")
    @Expose
    private Integer pageSize;
    @SerializedName("totalElements")
    @Expose
    private Integer totalElements;
    @SerializedName("pageNumber")
    @Expose
    private Integer pageNumber;
    @SerializedName("totalPages")
    @Expose
    private Integer totalPages;
    public final static Creator<ListAllData> CREATOR = new Creator<ListAllData>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ListAllData createFromParcel(android.os.Parcel in) {
            return new ListAllData(in);
        }

        public ListAllData[] newArray(int size) {
            return (new ListAllData[size]);
        }

    }
    ;

    protected ListAllData(android.os.Parcel in) {
        in.readList(this.items, (Item.class.getClassLoader()));
        this.pageSize = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.totalElements = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.pageNumber = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.totalPages = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public ListAllData() {
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeList(items);
        dest.writeValue(pageSize);
        dest.writeValue(totalElements);
        dest.writeValue(pageNumber);
        dest.writeValue(totalPages);
    }

    public int describeContents() {
        return  0;
    }

}
