package com.tv.utils.helpers.shimmer;

public class NavigationItem {

    private static NavigationItem navigationItem;
    private String tab;
    private String detailTabName;


    public static NavigationItem getInstance() {
        if (navigationItem == null) {
            navigationItem = new NavigationItem();
        }
        return navigationItem;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getDetailTabName() {
        return detailTabName;
    }

    public void setDetailTabName(String detailTabName) {
        this.detailTabName = detailTabName;
    }

    public String getTab() {
        return tab;
    }
}
