package com.tv.uscreen.yojmatv.utils.config.bean;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ParentalControl {
    @SerializedName("ratings")
    @Expose
    private List<Ratings> ratings = null;

    public List<Ratings> getRatings() {
        return ratings;
    }

    public void setRatings(List<Ratings> ratings) {
        this.ratings = ratings;
    }
}
