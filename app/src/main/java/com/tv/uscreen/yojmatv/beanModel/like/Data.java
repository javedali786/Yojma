package com.tv.uscreen.yojmatv.beanModel.like;

public class Data {
    private boolean isLike;

    public boolean isIsLike() {
        return isLike;
    }

    public void setIsLike(boolean isLike) {
        this.isLike = isLike;
    }

    @Override
    public String toString() {
        return
                "PlayListDetailsResponse{" +
                        "isLike = '" + isLike + '\'' +
                        "}";
    }
}
