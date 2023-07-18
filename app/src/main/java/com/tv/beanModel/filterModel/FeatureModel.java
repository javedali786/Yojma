package com.tv.beanModel.filterModel;

public class FeatureModel {
    private final int title;
    private final String key;
    private boolean selected;

    public FeatureModel(int feature, String key, boolean selected) {
        this.title = feature;
        this.key = key;
        this.selected = selected;
    }

    public int getTitle() {
        return title;
    }

    public String getKey() {
        return key;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
