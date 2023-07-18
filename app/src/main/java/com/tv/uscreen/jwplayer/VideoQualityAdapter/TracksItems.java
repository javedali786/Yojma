package com.tv.uscreen.jwplayer.VideoQualityAdapter;

public class TracksItems {
    public String getTextQuality() {
        return textQuality;
    }

    public void setTextQuality(String textQuality) {
        this.textQuality = textQuality;
    }

    public TracksItems(String textQuality, boolean isChecked) {
        this.textQuality = textQuality;
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    String textQuality;
    private boolean isChecked = false;
}
