package com.tv.uscreen.yojmatv.activities.profile.model;

public class AccountItem {
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    int image ;

    public AccountItem(String name, int image) {
        this.name = name;
        this.image = image;
    }
}
